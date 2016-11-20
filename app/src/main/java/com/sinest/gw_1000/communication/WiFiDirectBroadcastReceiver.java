package com.sinest.gw_1000.communication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/**
 * Created by Jinwook on 2016-11-16.
 */

public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

    private static String TAG = "WIFI";

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;

    private ArrayList<WifiP2pDevice> mPeers;

    private NetworkInfo networkInfo;
    private Handler mHandler;

    public WiFiDirectBroadcastReceiver(final WifiP2pManager manager, WifiP2pManager.Channel channel, ArrayList<WifiP2pDevice> peers, Handler handler) {
        super();
        this.mManager = manager;
        this.mChannel = channel;
        this.mPeers = peers;
        this.mHandler = handler;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Wifi p2p 활성화 되었는지
            // Indicates whether Wi-Fi P2P is enabled
         //    Log.i(TAG, "WIFI_P2P_STATE_CHANGED_ACTION");

            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {

            //    Log.i(TAG, "WIFI_P2P_STATE_ENABLED");
            } else {

            //    Log.i(TAG, "WIFI_P2P_STATE_DISABLED");
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            // 연결 가능한 피어 목록이 변경시 requestPeers()
            // Indicates that the available peer list has changed
        //    Log.i(TAG, "WIFI_P2P_PEERS_CHANGED_ACTION");
                // 검색한 와이파이 다이렉트 디바이스 얻기
            mHandler.sendEmptyMessage(1);

        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            // Wifi p2p 연결상태 (1:N 연결을 위해서는 여기서 requestConnectionInfo() 호출
            // Indicates the state of Wi-Fi P2P connectivity has changed
         //   Log.i(TAG, "WIFI_P2P_CONNECTION_CHANGED_ACTION");
            networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
            if (networkInfo.isConnected()) {

                mHandler.sendEmptyMessage(3);
                Log.i(TAG, "Connected");
            }
            else {

                mHandler.sendEmptyMessage(4);
                Log.i(TAG, "Disconnected");
            }

        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // 현재 단말 상태정보
            // Indicates this device's configuration details have changed
         //   Log.i(TAG, "WIFI_P2P_THIS_DEVICE_CHANGED_ACTION");
        }
    }
}