package com.sinest.gw_1000.communication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Created by Jinwook on 2016-11-15.
 */

public class Communicator {

    //private static Communicator instance = new Communicator();
    private Handler handler_data;

    // Define length of protocols
    private final static int LENGTH_TX          = 14;
    private final static int LENGTH_SETTING     = 7;
    private final static int LENGTH_RFID        = 5;
    private final static int LENGTH_ENGINEER    = 12;
    private final static int LENGTH_RX          = 25;

    // Define default protocols
    private final static byte STX               = (byte) 0xFD;
    private final static byte ETX               = (byte) 0xFE;

    private final byte[] msg_tx_default         = {STX, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x01, ETX};
    private final byte[] msg_setting_default    = {STX, 0x0A, 0x00, 0x00, 0x00, 0x0A, ETX};
    private final byte[] msg_rfid_default       = {STX, 0x0B, 0x00, 0x0B, ETX};
    private final byte[] msg_engineer_default   = {STX, 0x0D, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0D, ETX};

    // Tx messages
    private byte[] msg_tx       = new byte[LENGTH_TX];
    private byte[] msg_setting  = new byte[LENGTH_SETTING];
    private byte[] msg_rfid     = new byte[LENGTH_RFID];
    private byte[] msg_engineer = new byte[LENGTH_ENGINEER];

    // Rx message
    private byte[] msg_rx       = new byte[LENGTH_RX];

    // Wifi direct
    private Context mContext;
//    private WifiDirectWrapper mWidiWrapper;
    private WifiConnector wifiConnector;

    // Socket

    public Communicator(Context context) {

        init(context);
    }

    public WifiConnector getWifiConnector() {

        return wifiConnector;
    }

    public void init(Context context) {

        this.mContext = context;
        setHandler();

        initMessages();

        //mWidiWrapper = new WifiDirectWrapper(mContext, handler_data);
        //mWidiWrapper.start();
        wifiConnector = new WifiConnector(mContext, handler_data, this);
        wifiConnector.registReceiver();
    }

    private void setHandler() {

        // SocketReceiver에서 수신한 메시지 byte 단위로 쪼개어 msg_rx에 저장
        handler_data = new Handler() {

            @Override
            public void handleMessage(Message msg) {

                Bundle data = msg.getData();

                byte[] temp = new byte[data.size()];
                for (int i=0; i<data.size(); i++) {

                    byte b = data.getByte(""+i);
                    set_rx(i, b);
                    temp[i] = b;
                }
                //send(temp);
                if (!checkCheckSum(msg_rx)) {

                    Log.i("JW", "Rx data is wrong (checkSum error)");
                    calcCheckSum(msg_rx);
                }
                Intent intent = new Intent("update.data");
                mContext.sendBroadcast(intent);
                Log.i("jW", "sendBroadcast");
            }
        };
    }

    private void initMessages() {

        for (int i=0; i<LENGTH_TX; i++) {

            msg_tx[i] = msg_tx_default[i];
        }
        for (int i=0; i<LENGTH_SETTING; i++) {

            msg_setting[i] = msg_setting_default[i];
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

    synchronized private Boolean checkCheckSum(byte[] msg) {

        int len = msg.length;
        
        if (msg[len-2] == calcCheckSum(msg)) {

            return true;
        }
        return false;
    }

    synchronized private byte calcCheckSum(byte[] msg) {

        int len = msg.length;
        byte res = msg[1];

        for (int i=2; i<len-2; i++) {

            res = (byte)(res ^ msg[i]);
        }

        return res;
    }

    synchronized public void set_tx(int idx, byte val) {

        msg_tx[idx] = val;
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
}
