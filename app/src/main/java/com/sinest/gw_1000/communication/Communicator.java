package com.sinest.gw_1000.communication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.sinest.gw_1000.management.Application_manager;

/**
 * Created by Jinwook on 2016-11-15.
 *
 * 통신, 메시지 관리
 */

public class Communicator {

    private Handler handler_data;

    // Define length of protocols
    private final static int LENGTH_TX          = 11;
    private final static int LENGTH_SETTING     = 7;
    private final static int LENGTH_MANUAL      = 17;
    private final static int LENGTH_RFID        = 5;
    private final static int LENGTH_ENGINEER    = 11;
    private final static int LENGTH_RX          = 21;

    // Define default protocols
    public final static byte STX               = (byte) 0xFD;
    public final static byte ETX               = (byte) 0xFE;

    private final byte[] msg_tx_default         = {STX, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, ETX};
    private final byte[] msg_setting_default    = {STX, 0x0A, 0x00, 0x00, 0x00, 0x0A, ETX};
    private final byte[] msg_manual_default     = {STX, 0x09, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, ETX};
    private final byte[] msg_rfid_default       = {STX, 0x0B, 0x00, 0x0B, ETX};
    private final byte[] msg_engineer_default   = {STX, 0x0D, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0D, ETX};

    // Tx messages
    private byte[] msg_tx       = new byte[LENGTH_TX];
    private byte[] msg_setting  = new byte[LENGTH_SETTING];
    private byte[] msg_manual   = new byte[LENGTH_MANUAL];
    private byte[] msg_rfid     = new byte[LENGTH_RFID];
    private byte[] msg_engineer = new byte[LENGTH_ENGINEER];

    // Rx message
    private byte[] msg_rx       = new byte[LENGTH_RX];

    private Context mContext;

    // Wifi 연결 매니저
    private WifiConnector wifiConnector;

    // 소켓 통신 매니저
    private SocketManager socketManager;

    public Communicator(Context context) {

        init(context);
    }

    public WifiConnector getWifiConnector() {

        return wifiConnector;
    }

    public SocketManager getSocketManager() {

        return socketManager;
    }

    public void init(Context context) {

        this.mContext = context;
        setHandler();

        initMessages();

        wifiConnector = new WifiConnector(mContext);
        wifiConnector.registReceiver();

        socketManager = new SocketManager(handler_data, this);
    }

    /**
     * 수신 메시지 관리 핸들러
     */
    private void setHandler() {

        // SocketManager 에서 수신한 메시지 byte 단위로 쪼개어 msg_rx에 저장
        handler_data = new Handler() {

            @Override
            public void handleMessage(Message msg) {

                Bundle data = msg.getData();

                for (int i = 0; i < data.size(); i++) {

                    byte b = data.getByte("" + i);
                    set_rx(i, b);
                }

                // waiting 화면 업데이트 (수온, 내부온도, 산소농도, 습도)
                Intent intent = new Intent("update.data");
                mContext.sendBroadcast(intent);
            }
        };
    }

    /**
     * 메시지 초기값 세팅
     */
    private void initMessages() {

        for (int i=0; i<LENGTH_TX; i++) {

            msg_tx[i] = msg_tx_default[i];
        }
        for (int i=0; i<LENGTH_SETTING; i++) {

            msg_setting[i] = msg_setting_default[i];
        }
        for (int i=0; i<LENGTH_MANUAL; i++) {

            msg_manual[i] = msg_manual_default[i];
        }
        for (int i=0; i<LENGTH_RFID; i++) {

            msg_rfid[i] = msg_rfid_default[i];
        }
        for (int i=0; i<LENGTH_ENGINEER; i++) {

            msg_engineer[i] = msg_engineer_default[i];
        }
        for (int i=0; i<LENGTH_RX; i++) {

            msg_rx[i] = 0x00;
        }
    }

    /**
     * 메시지의 checkSum 올바른지 확인
     * @param msg 검증할 메시지
     */
    synchronized public Boolean checkCheckSum(byte[] msg) {

        int len = msg.length;

        //Log.i("JW", "받아온 checkSum: " + String.format("%02x", msg[len-2] & 0xff));
        //Log.i("JW", "계산한 checkSum: " + String.format("%02x", calcCheckSum(msg) & 0xff));
        if (msg[len-2] == calcCheckSum(msg)) {

            return true;
        }
        return false;
    }

    /**
     * 인자로 받은 메시지의 체크섬 계산
     * @param msg checkSum 계산할 메시지
     */
    synchronized public byte calcCheckSum(byte[] msg) {

        int len = msg.length;
        byte res = msg[1];

        for (int i=2; i<len-2; i++) {

            res = (byte)(res ^ msg[i]);
        }
        //Log.i("JW_CHECKSUM", String.format("%02x", res));
        res = (byte) (res & 0xff);

        return res;
    }

    synchronized public void set_tx(int idx, byte val) {

        msg_tx[idx] = val;

        // 동작 상태에서만 팬 동작 Ventilation fan on/off -> deprecated
//        if (idx == 1) {
//            switch (val) {
//                case 0x00:
//                case 0x02:
//                    msg_tx[7] = (byte) 0x00;
//                    break;
//                case 0x01:
//                    if (Application_manager.mVentilationFan == 1 && Application_manager.getProgramMode() != Application_manager.MODE_L) {
//                        msg_tx[7] = (byte) 0x01;
//                    }
//                    else {
//                        msg_tx[7] = (byte) 0x00;
//                    }
//                    break;
//            }
//        }
    }

    synchronized public byte[] get_tx() {

        return msg_tx;
    }

    synchronized public byte get_tx_idx(int idx) {

        return msg_tx[idx];
    }

    synchronized public void set_setting(int idx, byte val) {

        msg_setting[idx] = val;
    }

    synchronized public byte[] get_setting() {

        return msg_setting;
    }

    synchronized public byte get_setting_idx(int idx) {

        return msg_setting[idx];
    }

    synchronized public void set_rfid(int idx, byte val) {

        msg_rfid[idx] = val;
    }

    synchronized public byte[] get_rfid() {

        return msg_rfid;
    }

    synchronized public byte get_rfid_idx(int idx) {

        return msg_rfid[idx];
    }

    synchronized public void set_engineer(int idx, byte val) {

        msg_engineer[idx] = val;
    }

    synchronized public byte[] get_engineer() {

        return msg_engineer;
    }

    synchronized public byte get_engineer_idx(int idx) {

        return msg_engineer[idx];
    }

    synchronized public void set_rx(int idx, byte val) {

        msg_rx[idx] = val;
    }

    synchronized public byte[] get_rx() {

        return msg_rx;
    }

    synchronized public byte get_rx_idx(int idx) {

        return msg_rx[idx];
    }

    synchronized public byte[] get_manual() {

        return msg_manual;
    }
}
