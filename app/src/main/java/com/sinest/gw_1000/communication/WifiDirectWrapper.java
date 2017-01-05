package com.sinest.gw_1000.communication;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

/**
 * Created by Jinwook on 2016-11-16.
 */

public class WifiDirectWrapper extends Thread {
/*
    private static String TAG = "WIFI";
    private final static String DEVICE_ADDRESS = "96:76:b7:37:9e:23";

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private WiFiDirectBroadcastReceiver mReceiver;
    private Context mContext;
    private IntentFilter mIntentFilter;
    private WifiP2pManager.PeerListListener myPeerListListener;

    private ArrayList<WifiP2pDevice> mPeers;
    private Handler mHandler;
    private boolean isConnected = false;

    private String IP_ADDRESS;
    private final static int PORT = 5003;
    private Thread thread_connectToServer;
    private Handler handler_data;
    private SocketManager socketManager;
    private Thread thread_setServerSocket;

    public WifiDirectWrapper(Context context, Handler _handler_data) {

        this.mContext = context;

        mManager = (WifiP2pManager) mContext.getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(mContext, mContext.getMainLooper(), null);
        mPeers = new ArrayList();
        setPeerListListener();
        setHandler();
        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, mPeers, mHandler);

        handler_data = _handler_data;
    }

    private void setServerSocket() {

        thread_setServerSocket = new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    ServerSocket serverSocket = new ServerSocket(PORT);
                    Log.i(TAG, "Set server socket");
                    Socket client = serverSocket.accept();
                    Log.i(TAG, "Accepted");

                    socketManager = new SocketManager(client, handler_data);
                    socketManager.start_receiver();

                } catch (IOException e) {

                }
            }
        });
        thread_setServerSocket.start();
    }

    private void connectToServer() {

        thread_connectToServer = new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    Socket socket = new Socket(IP_ADDRESS, PORT);
                    if (socket != null) {

                        if (socket.isConnected()) {

                            Log.i(TAG, "Socket is connected");
                        }
                        else {

                            Log.i(TAG, "Socket is not connected");
                        }
                    }
                    else {

                        Log.i(TAG, "socket is null");
                    }

                    socketManager = new SocketManager(socket, handler_data);
                    socketManager.start_receiver();

                } catch(IOException e) {

                    Log.i(TAG, "IOException occurred :" + e.getMessage() + ", " + e.getCause());
                }
            }
        });
        thread_connectToServer.start();
    }

    public void run() {

        registReceiver();
        discoverPeer();
    }

    public void registReceiver() {

        Log.i(TAG, "registReceiver");
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        mContext.registerReceiver(mReceiver, mIntentFilter);
    }

    public void unRegistReceiver() {

        mContext.unregisterReceiver(mReceiver);
    }

    public void discoverPeer() {

        // 와이파이 다이렉트 디바이스 검색
        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {

                Log.i(TAG, "discoverPeer");
            }

            @Override
            public void onFailure(int reasonCode) {

                discoverPeer();
            }
        });
    }

    public void doConnect(WifiP2pDevice device) {

        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        config.wps.setup = WpsInfo.PBC;

        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {

             //   Log.i(TAG, "doConnect - onSuccess");
            }

            @Override
            public void onFailure(int reason) {

                //Log.i(TAG, "doConnect - onFailure : " + reason);
            }
        });
    }

    public void doDisconnect() {

        mManager.removeGroup(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {

                Log.i(TAG, "doDisconnect - onSuccess");
            }

            @Override
            public void onFailure(int i) {

                Log.i(TAG, "doDisconnect - onFailure");
            }
        });
    }

    private void setPeerListListener() {

        myPeerListListener = new WifiP2pManager.PeerListListener() {
            @Override
            public void onPeersAvailable(WifiP2pDeviceList peerList) {

                if (!isConnected) {

                    mPeers.clear();
                    if (peerList.getDeviceList().size() != 0) {

                        mPeers.addAll(peerList.getDeviceList());
                        mHandler.sendEmptyMessage(2);
                    } else {

                        Log.i(TAG, "No devices found");
                        return;
                    }
                }
            }
        };
    }

    private void setHandler() {

        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1: // 연결 가능한 디바이스 목록 변경 시

                        if (!isConnected) {

                            mManager.requestPeers(mChannel, myPeerListListener);
                            Log.i(TAG, "requestPeers");
                        }
                        break;
                    case 2: // 디바이스에 연결

                        if (!isConnected) {
                            for (WifiP2pDevice device : mPeers) {

                                if (device.deviceAddress.equals(DEVICE_ADDRESS)) {

                                    doConnect(device);
                                    break;
                                }
                            }
                        }
                        break;
                    case 3: // 연결 성공 시

                        if (!isConnected) {

                            isConnected = true;
                            mManager.requestConnectionInfo(mChannel, new WifiP2pManager.ConnectionInfoListener() {

                                @Override
                                public void onConnectionInfoAvailable(WifiP2pInfo info) {

                                    IP_ADDRESS = info.groupOwnerAddress.getHostAddress();
                                    Log.i(TAG, "Group formed? " + info.groupFormed);
                                    Log.i(TAG, "Group owner? " + info.isGroupOwner);
                                    Log.i(TAG, "Group owner address : " + info.groupOwnerAddress);

                                    if (!info.isGroupOwner) {
                                        mHandler.sendEmptyMessage(5);
                                    } else {
                                        mHandler.sendEmptyMessage(6);
                                    }
                                }
                            });
                        }
                        break;
                    case 4: // 연결 해제 시

                        if (isConnected) {

                            isConnected = false;
                            discoverPeer();
                        }
                        break;
                    case 5: // 소켓 연결

                        connectToServer();
                        break;
                    case 6:
                        setServerSocket();
                        break;
                }
            }
        };
    }
    */
}
