package com.sinest.gw_1000.communication;

import android.icu.util.Output;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;

/**
 * Created by Jinwook on 2016-11-18.
 */

public class SocketManager extends Thread {

    private final static int LENGTH_TX          = 14;
    private final static int LENGTH_RX          = 25;

    private Socket mSocket;
    private Handler mHandler;

    private InputStream inputStream;
    private OutputStream outputStream;

    private Communicator communicator;

    private boolean isRun = false;
    /*
    private SocketReceiver socketReceiver;
    private SocketSender socketSender;
    */

    public SocketManager() {

    }

    public void init(Socket socket, Handler handler, Communicator _communicator) {

        communicator = _communicator;
        this.mSocket = socket;
        this.mHandler = handler;

        try {

            mSocket.setSoTimeout(500);
            outputStream = mSocket.getOutputStream();
            inputStream = mSocket.getInputStream();

        } catch (SocketException e) {

            Log.i("JW", "Socket timeout 설정 exception");
        } catch (IOException e) {

            Log.i("JW", "Input/output stream 초기화 exception");
        }
    }

    @Override
    public void run() {

        byte[] msg_in = new byte[LENGTH_RX];
        byte[] msg_out;
        int read_len;

        try {

            while (isRun) {

                sleep(500);

                // TX 보내기
                msg_out = communicator.get_tx();
                outputStream.write(msg_out, 0, msg_out.length);
                Log.i("JW", "Transferred: " + msg_out.length + "byte");

                // RX 초기화
                Arrays.fill(msg_in, (byte) 0x00);

                // RX 받기, timeout = 500ms
                read_len = inputStream.read(msg_in);

                if (LENGTH_RX == read_len) {

                    Log.i("JW", "Received: " + read_len + "byte");

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
            }

        } catch (SocketTimeoutException e) {

            stop_thread();
            Log.i("JW", "Socket timeout exception: " + e.getMessage());
        } catch (IOException e) {

            stop_thread();
            Log.i("JW", "IO stream exception: " + e.getMessage());
        } catch (InterruptedException e) {

            stop_thread();
            Log.i("JW", "InterruptedException exception on sleep(500): " + e.getMessage());
        }
        Log.i("JW", "Socket manager 스레드 종료");
    }

    public void start_thread() {

        if (!this.isAlive()) {

            isRun = true;
            this.run();
        }
        else {

            Log.i("JW", "Socket manager 스레드가 이미 동작중입니다.");
        }
    }

    public void stop_thread() {

        isRun = false;
        try {

            mSocket.close();
            inputStream.close();
            outputStream.close();

        } catch (IOException e) {

            Log.i("JW", "IO stream exception: " + e.getMessage());
        }
        this.interrupt();
    }

    /*
    public int send(byte[] msg) {

        return socketSender.send(msg);
    }

    public void start_receiver() {

        socketReceiver.start_thread();
    }

    public void stop_receiver() {

        socketReceiver.stop_thread();
    }*/
}
/*
class SocketSender {

    private Socket mSocket;
    private OutputStream outputStream;

    public SocketSender(Socket socket) {

        this.mSocket = socket;

        try {

            outputStream = socket.getOutputStream();
        }
        catch (IOException e) {}
    }

    public int send(byte[] msg) {

        try {

            outputStream.write(msg, 0, msg.length);
            Log.i("WIFI", "Transferred");

        } catch (IOException e) {

            return -1;
        }
        return 0;
    }
}

class SocketReceiver extends Thread{

    private Socket mSocket;
    private Handler mHandler;
    private Boolean isRun;

    private byte[] buf = new byte[30];
    private int buf_len = -1;

    public SocketReceiver(Socket socket, Handler handler) {

        this.mSocket = socket;
        this.mHandler = handler;
    }

    public void start_thread() {

        isRun = true;
        this.start();
    }

    public void stop_thread() {

        isRun = false;
        this.interrupt();
    }

    @Override
    public void run() {

        try {

            InputStream inputStream = mSocket.getInputStream();

            while (isRun) {

                Arrays.fill(buf, (byte)0x00);
                buf_len = inputStream.read(buf);

                Bundle data = new Bundle();
                for (int i = 0; i < buf_len; i++) {

                    data.putByte("" + i, buf[i]);
                    Log.i("WIFI", "Received : " + String.format("%02x", buf[i]&0xff));
                }
                Message msg = new Message();
                msg.setData(data);
                mHandler.sendMessage(msg);
            }

        } catch (IOException e) {

            stop_thread();
        }
        Log.i("JW", "SocketReceiver is stopped");
    }
}*/