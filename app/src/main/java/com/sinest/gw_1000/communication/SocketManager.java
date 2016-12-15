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
import java.util.Arrays;

/**
 * Created by Jinwook on 2016-11-18.
 */

public class SocketManager {

    private Socket mSocket;
    private Handler mHandler;

    private SocketReceiver socketReceiver;
    private SocketSender socketSender;

    public SocketManager(Socket socket, Handler handler) {

        this.mSocket = socket;
        this.mHandler = handler;

        socketReceiver = new SocketReceiver(mSocket, mHandler);
        socketSender = new SocketSender(mSocket);
    }

    public int send(byte[] msg) {

        return socketSender.send(msg);
    }

    public void start_receiver() {

        socketReceiver.start_thread();
    }

    public void stop_receiver() {

        socketReceiver.stop_thread();
    }
}

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
}