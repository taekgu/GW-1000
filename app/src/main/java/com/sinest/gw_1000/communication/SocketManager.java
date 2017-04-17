package com.sinest.gw_1000.communication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.sinest.gw_1000.management.Application_manager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * Created by Jinwook on 2016-11-18.
 *
 * 서버와의 연결, 데이터 교환
 */

public class SocketManager {

    private final static int LENGTH_TX = 11;
    private final static int LENGTH_RX = 21;

    private final static int SERVER_CONNECTED     = 1001;
    private final static int SERVER_DISCONNECTED  = 1002;

    private static final String IP_ADDRESS  = "192.168.0.1";
    private static final int PORT           = 20002;

    private SocketAddress socketAddress;
    private Socket mSocket;
    private int timeout = 500;

    private Handler mHandler;
    private Handler handler_for_toast;

    private InputStream inputStream;
    private OutputStream outputStream;

    private Communicator communicator;

    private boolean isRun = false;
    private boolean isConnected = false;

    private Thread thread;
    private Runnable runnable;

    private SharedPreferences sharedPreferences = null;

    // 초기 데이터(세팅값, 매뉴얼 모드 설정값) 전송되었는지 확인
    public static byte ACK = 0x0f;
    private boolean isSent_predata = false;
    private synchronized void set_isSent_predata(boolean val) {

        isSent_predata = val;
    }
    private synchronized boolean get_isSent_predata() {

        return isSent_predata;
    }

    private boolean lock = false;
    private boolean wait = false;

    int cnt_tx = 0, cnt_rx = 0;

    public SocketManager(Handler handler, Communicator _communicator) {

        communicator = _communicator;
        this.mHandler = handler;
        setHandler();

        sharedPreferences = Application_manager.getSharedPreferences();

        new Thread(new Runnable() {
            @Override
            public void run() {

                init();
            }
        }).start();
    }

    /**
     * 지정 IP, PORT 로 연결 시도
     */
    private void init() {

        if (!isRun) {

            Log.i("JW_COMM", "init()");
            try {

                Thread.sleep(3000);

                socketAddress = new InetSocketAddress(IP_ADDRESS, PORT);
                mSocket = new Socket();
                mSocket.connect(socketAddress);

                if (mSocket.isConnected()) {

                    mSocket.setSoTimeout(timeout); // InputStream 데이터 읽을 때의 timeout
                    outputStream = mSocket.getOutputStream();
                    inputStream = mSocket.getInputStream();

                    isConnected = true;
                    handler_for_toast.sendEmptyMessage(SERVER_CONNECTED);

                    set_isSent_predata(false);
                    send_setting();

                    // setting 정보가 모두 전달될 때까지 대기
                    while (!get_isSent_predata());

                    set_isSent_predata(false);
                    send_manual(0);

                    // manual 정보가 모두 전달될 때까지 대기
                    while (!get_isSent_predata());

                    set_isSent_predata(false);
                    setThread();
                    start_thread();
                }
            } catch (SocketException e) {

                Log.i("JW_COMM_EX", "Socket 연결 exception");
                init();
            } catch (IOException e) {

                Log.i("JW_COMM_EX", "IO exception: " + e.getMessage());
                init();
            } catch (InterruptedException e) {

                Log.i("JW_COMM_EX", "InterruptedException exception on sleep(3000): " + e.getMessage());
                init();
            }
        }
    }

    /**
     * 서버 연결 이후의 통신 담당 스레드
     */
    private void setThread() {

        Log.i("JW_COMM", "setThread()");
        thread = null;

        if (runnable == null) {

            runnable = new Runnable() {
                @Override
                public void run() {

                    byte[] msg_out;

                    try {

                        while (isRun) {

                            // 통신 주기 500ms
                            Thread.sleep(500);

                            if (!lock) {

                                if (!wait) {

                                    // 활성 액티비티에 따라 TX / Engineer 보내기
                                    if (Application_manager.getIsEngineerMode()) {

                                        msg_out = communicator.get_engineer();
                                        Log.i("JW_COMM", "엔지니어링");
                                    } else {

                                        msg_out = communicator.get_tx();
                                        Log.i("JW_COMM", "동작명령");
                                    }
                                    msg_out[msg_out.length - 2] = communicator.calcCheckSum(msg_out);
                                    send_msg_and_receive_rx(msg_out);
                                }
                            }
                        }
                    } catch (InterruptedException e) {

                        Log.i("JW_COMM_EX", "InterruptedException exception on sleep(500): " + e.getMessage());
                        stop_thread();
                    }
                    Log.i("JW_COMM", "Socket manager 스레드 종료");
                }
            };
        }

        thread = new Thread(runnable);
    }

    /**
     * 통신 스레드 시작
     */
    private void start_thread() {

        Log.i("JW_COMM", "start_thread()");
        if (thread != null) {

            if (!thread.isAlive()) {

                isRun = true;
                isConnected = true;
                thread.start();
            } else {

                Log.i("JW_COMM", "Socket manager 스레드가 이미 동작중입니다.");
            }
        }
    }

    /**
     * 통신 스레드 정지, 서버 재연결 시도
     */
    private void stop_thread() {

        if (isConnected) {

            cnt_rx = 0;
            cnt_tx = 0;
            Log.i("JW_COMM", "stop_thread()");
            isConnected = false;
            isRun = false;
            handler_for_toast.sendEmptyMessage(SERVER_DISCONNECTED);
            try {

                if (!mSocket.isClosed()) {

                    byte[] buf = {0x00};
                    outputStream.close();
                    // 서버가 보낸 남은 패킷 모두 전송받은 후 연결 종료
                    Log.i("JW_COMM", "남은 패킷 읽기");
                    while (inputStream.read(buf, 0, 1) != 0);
                    Log.i("JW_COMM", "남은 패킷 읽기 완료");
                    inputStream.close();
                    mSocket.close();
                }

            } catch (IOException e) {

                Log.i("JW_COMM_EX", "IO stream exception (stopping): " + e.getMessage());
            }
            thread.interrupt();

            init();
        }
    }

    /**
     * This method sends setting message to device.
     */
    public void send_setting() {

        if (isConnected) {

            new Thread(new Runnable() {
                @Override
                public void run() {

                    lock = true;

                    byte[] msg_out;

                    // setting 보내기
                    msg_out = communicator.get_setting();
                    msg_out[msg_out.length - 2] = communicator.calcCheckSum(msg_out);

                    send_msg_and_receive_rx(msg_out);
                    Log.i("JW_COMM", "설정명령");

                    lock = false;
                    set_isSent_predata(true);
                    Log.i("JW_COMM", "setting msg 전송 완료");
                }
            }).start();
        }
    }

    /**
     * This method sends RFID message to device.
     */
    public void send_rfid() {

        if (isConnected) {

            new Thread(new Runnable() {
                @Override
                public void run() {

                    byte[] msg_in = new byte[LENGTH_RX];
                    byte[] msg_out;
                    int read_len;

                    try {

                        // rfid 보내기
                        msg_out = communicator.get_rfid();
                        msg_out[msg_out.length - 2] = communicator.calcCheckSum(msg_out);

                        outputStream.write(msg_out, 0, msg_out.length);
                        Log.i("JW_COMM", "Transferred: " + msg_out.length + "byte");
                        Log.i("JW_COMM", "RFID 명령");

                        // RX 초기화
                        Arrays.fill(msg_in, (byte) 0x00);

                        // RX 받기, timeout = 500ms
                        read_len = inputStream.read(msg_in);

                        if (LENGTH_RX == read_len) {

                            Log.i("JW_COMM", "Received: " + read_len + "byte");

                            // Communicator 에서 처리
                            Bundle data = new Bundle();
                            for (int i = 0; i < read_len; i++) {

                                data.putByte("" + i, msg_in[i]);
                                //Log.i("WIFI", "Received : " + String.format("%02x", msg_in[i] & 0xff));
                            }
                            Message msg = new Message();
                            msg.setData(data);
                            mHandler.sendMessage(msg);
                        }
                    } catch (SocketTimeoutException e) {

                        send_rfid();
                        Log.i("JW_COMM", "Socket timeout exception: " + e.getMessage());
                    } catch (IOException e) {

                        send_rfid();
                        Log.i("JW_COMM", "IO stream exception: " + e.getMessage());
                    }
                    Log.i("JW_COMM", "rfid msg 전송 완료");
                }
            }).start();
        }
    }

    /**
     * This method sends manual mode setting messages to device.
     * @param manualNum 전송할 매뉴얼 모드 번호를 입력 (1-5). 0 입력 시 5개 모두 전송.
     */
    public void send_manual(final int manualNum) {

        if (isConnected) {

            new Thread(new Runnable() {
                @Override
                public void run() {

                    lock = true;

                    int min_i_manualNum, max_i_manualNum;
                    int start_of_loop, end_of_loop;

                    if (manualNum == 0) {

                        min_i_manualNum = 1;
                        max_i_manualNum = 5;
                        start_of_loop = 0;
                        end_of_loop = 5;
                    } else {

                        min_i_manualNum = manualNum;
                        max_i_manualNum = manualNum;
                        start_of_loop = manualNum - 1;
                        end_of_loop = manualNum;
                    }

                    // 매뉴얼 모드 정보 불러오기
                    int patternNum[][] = new int[5][3];
                    int patternStart[][] = new int[5][3];
                    int patternEnd[][] = new int[5][3];
                    int patternTime[][] = new int[5][3];

                    for (int i_manualNum = min_i_manualNum; i_manualNum <= max_i_manualNum; i_manualNum++) {

                        int manualIdx = i_manualNum - 1;
                        for (int i_patternNum = 0; i_patternNum < 3; i_patternNum++) {

                            patternNum[manualIdx][i_patternNum] = sharedPreferences.getInt(Application_manager.DB_MANUAL_MODE_PATTERN_ + i_manualNum + "_" + i_patternNum, 1);
                            patternStart[manualIdx][i_patternNum] = sharedPreferences.getInt(Application_manager.DB_MANUAL_MODE_SECTION_MIN_ + i_manualNum + "_" + i_patternNum, 0);
                            patternEnd[manualIdx][i_patternNum] = sharedPreferences.getInt(Application_manager.DB_MANUAL_MODE_SECTION_MAX_ + i_manualNum + "_" + i_patternNum, 14);
                            patternTime[manualIdx][i_patternNum] = sharedPreferences.getInt(Application_manager.DB_MANUAL_MODE_TIME_ + i_manualNum + "_" + i_patternNum, 30);
                        }
                    }

                    byte[] msg_out;

                    // 매뉴얼모드 가져오기
                    msg_out = communicator.get_manual();

                    for (int i_manualNum = start_of_loop; i_manualNum < end_of_loop; i_manualNum++) {

                        msg_out[2] = (byte) (i_manualNum + 16);

                        msg_out[3] = (byte) patternNum[i_manualNum][0];
                        msg_out[4] = (byte) patternStart[i_manualNum][0];
                        msg_out[5] = (byte) patternEnd[i_manualNum][0];
                        msg_out[6] = (byte) patternTime[i_manualNum][0];

                        msg_out[7] = (byte) patternNum[i_manualNum][1];
                        msg_out[8] = (byte) patternStart[i_manualNum][1];
                        msg_out[9] = (byte) patternEnd[i_manualNum][1];
                        msg_out[10] = (byte) patternTime[i_manualNum][1];

                        msg_out[11] = (byte) patternNum[i_manualNum][2];
                        msg_out[12] = (byte) patternStart[i_manualNum][2];
                        msg_out[13] = (byte) patternEnd[i_manualNum][2];
                        msg_out[14] = (byte) patternTime[i_manualNum][2];

                        msg_out[msg_out.length - 2] = communicator.calcCheckSum(msg_out);

                        send_msg_and_receive_rx(msg_out);
                        Log.i("JW_COMM", "매뉴얼 모드 설정 명령");
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {


                        }
                    }
                    Log.i("JW_COMM", "메뉴얼 모드 설정 msg 전송 완료");

                    lock = false;
                    set_isSent_predata(true);

                }
            }).start();
        }
    }

    /**
     * Toast 출력을 위한 핸들러 초기화
     */
    private void setHandler() {

        handler_for_toast = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {

                    case SERVER_CONNECTED:

                        Log.i("JW", "Socket is connected");
                        Application_manager.getToastManager().popToast(8);
                        break;
                    case SERVER_DISCONNECTED:

                        Log.i("JW", "Socket is disconnected");
                        Application_manager.getToastManager().popToast(14);
                        break;
                }
            }
        };
    }

    private void send_msg_and_receive_rx(byte[] msg_out) {

        wait = true;

        try {

            ACK = (byte) (msg_out[1] & 0x0f);
            outputStream.write(msg_out, 0, msg_out.length);
            Log.i("JW_COMM", "Transferred: " + msg_out.length + "byte");
            cnt_tx++;

            /////////////////////// TEST
            show_msg("TX", msg_out);
            /////////////////////////

            int read_len;
            byte[] msg_in = new byte[LENGTH_RX];

            // RX 초기화
            Arrays.fill(msg_in, (byte) 0x00);

            // RX 받기, timeout = 500ms
            read_len = inputStream.read(msg_in);
            cnt_rx++;

            /////////////////////// TEST
            show_msg("RX", msg_in);
            /////////////////////////

            // 읽어온 메시지 길이 검사
            if (read_len == LENGTH_RX) {

                // 시작 과 끝 올바른지 검사
                if (msg_in[0] == (byte) communicator.STX && msg_in[LENGTH_RX - 1] == communicator.ETX) {

                    // 체크섬 검사
                    if (communicator.checkCheckSum(msg_in)) {

                        Log.i("JW_COMM", "Received: " + read_len + "byte");

                        // 디바이스 상태와 ACK 신호 확인
                        byte signal_deviceState = (byte) (0xf0 & msg_in[1]);
                        byte signal_ack = (byte) (0x0f & msg_in[1]);

                        check_ack_and_state(signal_deviceState, signal_ack);

                        // ACK 검사
                        if (ACK == signal_ack) {

                            ACK = 0x0f;

                            // Communicator 의 핸들러에서 처리
                            Bundle data = new Bundle();
                            for (int i = 0; i < read_len; i++) {

                                data.putByte("" + i, msg_in[i]);
                            }
                            Message msg = new Message();
                            msg.setData(data);
                            mHandler.sendMessage(msg);
                        }
                        else {

                            Log.i("JW_COMM_ERROR", "ACK is wrong");
                        }
                    } else {

                        Log.i("JW_COMM_ERROR", "Rx data is wrong (checkSum error)");
                    }
                }
                // 읽어온 메시지의 시작점, 끝점이 맞지 않는 경우
                else {

                    Log.i("JW_COMM_ERROR", "Error: STX, ETX 불일치");


                    for (int i=1; i<LENGTH_RX; i++) {

                        if (msg_in[i] == communicator.STX ) {

                            byte[] buf = {0x00};
                            while (true) {

                                inputStream.read(buf, 0, 1);
                                if (buf[0] == communicator.ETX) {

                                    break;
                                }
                            }
                            Log.i("JW_COMM_ERROR", "Error: STX, ETX 위치 재설정 완료");
                        }
                    }
                }
            }
            else {

                Log.i("JW_COMM_ERROR", "Error: RX 길이만큼 읽어오지 못함");
                send_msg_and_receive_rx(msg_out);
            }
        } catch (SocketTimeoutException e) {

            Log.i("JW_COMM_EX", "Socket timeout exception - TX(" + cnt_tx + "): "+ e.getMessage());
            if (mSocket.isConnected()) {

                cnt_tx--;
                send_msg_and_receive_rx(msg_out);
            }

        } catch (IOException e) {

            Log.i("JW_COMM_EX", "IO stream exception (sending): " + e.getMessage());
            stop_thread();
        }

        wait = false;

        Log.i("JW_COMM_CNT", "TX = " + cnt_tx + ", RX = " + cnt_rx);
    }

    private void show_msg(String name, byte[] msg) {

        String string_msg = "";
        for (int i=0; i<msg.length; i++) {

                string_msg = string_msg + String.format("%02x", msg[i] & 0xff) + " | ";
        }
        Log.i("JW_COMM_MSG", name + " : " + string_msg);
    }

    private void check_ack_and_state(byte signal_deviceState, byte signal_ack) {

        // 원점 복귀 완료 신호 받으면 isWaiting_init -> false
        if (signal_deviceState == 0x20 || signal_deviceState == 0x10) {

            if (Application_manager.getIsWaiting_init()) {

                Application_manager.setIsWaiting_init(false);
            }
        }

        String ack = "";
        switch (signal_ack) {

            case 0x00:

                ack = "정지 ACK - ";
                break;
            case 0x01:

                ack = "동작 ACK - ";
                break;
            case 0x02:

                ack = "일시정지 ACK - ";
                break;
            case 0x0A:

                ack = "설정 ACK - ";
                break;
            case 0x0B:

                ack = "RFID 읽기 ACK - ";
                break;
            case 0x0C:

                ack = "RFID 쓰기 ACK - ";
                break;
            case 0x0D:

                ack = "엔지니어모드 ACK - ";
                break;
            case 0x09:

                ack = "매뉴얼 ACK - ";
                break;
        }
        Log.i("JW_COMM_ACK", ack + String.format("%02x", signal_ack & 0xff));

        String state = "";
        switch (signal_deviceState) {

            case 0x00:

                state = "전원 인가 후 원점이동 중 - ";
                break;
            case 0x10:

                state = "원점상태 (인버터 정지 중) - ";
                break;
            case 0x20:

                state = "원점상태 (인버터 정지) - ";
                break;
            case 0x30:

                state = "정지하여 원점 이동 중 - ";
                break;
            case 0x40:

                state = "동작 중 - ";
                break;
            case 0x50:

                state = "일시정지 중 - ";
                break;
        }
        Log.i("JW_COMM_STATE", state + String.format("%02x", signal_deviceState & 0xff));
    }
}