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

public class SocketManager {

    private final static int LENGTH_TX = 14;
    private final static int LENGTH_RX = 25;

    private static final String IP_ADDRESS  = "192.168.0.1";
    private static final int PORT           = 20002;

    private Socket mSocket;
    private Handler mHandler;

    private InputStream inputStream;
    private OutputStream outputStream;

    private Communicator communicator;

    private boolean isRun = false;
    private boolean isConnected = false;

    private Thread thread;
    private Runnable runnable;

    public SocketManager(Handler handler, Communicator _communicator) {

        communicator = _communicator;
        this.mHandler = handler;

        new Thread(new Runnable() {
            @Override
            public void run() {

                init();
            }
        }).start();
    }

    private void init() {

        if (!isRun) {

            Log.i("COMM", "init()");
            try {

                Thread.sleep(3000);

                mSocket = new Socket(IP_ADDRESS, PORT);
                //      if (mSocket != null) {

                if (mSocket.isConnected()) {

                    mSocket.setSoTimeout(500);
                    outputStream = mSocket.getOutputStream();
                    inputStream = mSocket.getInputStream();

                    setThread();
                    start_thread();
                }
                //      }
            } catch (SocketException e) {

                Log.i("JW", "Socket timeout 설정 exception");
                init();
            } catch (IOException e) {

                Log.i("JW", "Input/output stream 초기화 exception");
                init();
            } catch (InterruptedException e) {

                Log.i("JW", "InterruptedException exception on sleep(3000): " + e.getMessage());
                init();
            }
        }
    }

    private void setThread() {

        Log.i("COMM", "setThread()");
        thread = null;

        if (runnable == null) {

            runnable = new Runnable() {
                @Override
                public void run() {

                    byte[] msg_in = new byte[LENGTH_RX];
                    byte[] msg_out;
                    int read_len;

                    try {

                        while (isRun) {

                            Thread.sleep(500);

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
            };
        }

        thread = new Thread(runnable);
    }

    private void start_thread() {

        Log.i("COMM", "start_thread()");
        if (thread != null) {

            if (!thread.isAlive()) {

                isRun = true;
                isConnected = true;
                thread.start();
            } else {

                Log.i("JW", "Socket manager 스레드가 이미 동작중입니다.");
            }
        }
    }

    private void stop_thread() {

        Log.i("COMM", "stop_thread()");
        isConnected = false;
        isRun = false;
        try {

            mSocket.close();
            inputStream.close();
            outputStream.close();

        } catch (IOException e) {

            Log.i("JW", "IO stream exception: " + e.getMessage());
        }
        thread.interrupt();


        init();
    }
}