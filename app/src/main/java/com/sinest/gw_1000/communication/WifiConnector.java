package com.sinest.gw_1000.communication;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.sinest.gw_1000.management.Application_manager;
import com.sinest.gw_1000.mode.Activity_waiting;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

/**
 * Created by Jinwook on 2016-12-14.
 */

public class WifiConnector {

    private final static int SERVER_CONNECTED     = 1001;
    private final static int SERVER_DISCONNECTED  = 1002;
    private final static int WIFI_CONNECTED       = 1003;
    private final static int WIFI_DISCONNECTED    = 1004;

    //private static final String AP_KEYWORD  = "MALAB";
    //private static final String AP_PSWD     = "malab123";
    private static final String IP_ADDRESS  = "192.168.0.22";

    private static final String AP_KEYWORD  = "GW1000";
    private static final String AP_PSWD     = "1234567890";
    //private static final String IP_ADDRESS  = "192.168.0.1";
    private static final int PORT           = 20002;
    private SocketManager socketManager     = null;
    private Handler handler_data;

    private Context context;
    private Handler handler_for_toast;

    private boolean isRun = false; // 반복 위한 플래그
    private boolean isSet = true; // 스레드가 정상 종료 되었는지

    private WifiManager wifiManager;
    private BroadcastReceiver broadcastReceiver;
    private IntentFilter intentFilter;
    private String ssid = null;
    private String bssid = null;
    private boolean isConnected = false;
    private boolean isConnected_server = false;
    private WifiConfiguration wfc;

    private Thread thread;

    public int permission = 0;

    private Communicator communicator;

    public WifiConnector(Context _context, Handler _handler_data, Communicator _communicator) {

        context = _context;
        handler_data = _handler_data;
        communicator = _communicator;
        init();
    }

    private void init() {

        setHandler_for_toast();

        Log.i("JW", "WifiConnector - init()");

        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        // 와이파이 사용가능여부
        Log.i("JW", "와이파이 사용가능여부: " + wifiManager.isWifiEnabled());
        if (!wifiManager.isWifiEnabled()) {

            // 와이파이 on/off 정보;
            if (wifiManager.getWifiState() != WifiManager.WIFI_STATE_ENABLING) {
                Log.i("JW", "와이파이 ENABLING 아님");

                // 와이파이 on
                Log.i("JW", "와이파이 활성화");
                wifiManager.setWifiEnabled(true);
            }
        }
        else {

            // 와이파이 연결되어있으면 바로 서버에 연결
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            isConnected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();

            if (isConnected) {

                if (!isConnected_server && isSet) {

                    setThread();
                    isRun = true;
                    thread.start();
                    isSet = false;
                }
            }
        }

        // 스캔 시작
        wifiManager.startScan();
    }

    private void setHandler_for_toast() {

        handler_for_toast = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {

                    case SERVER_CONNECTED:

                        Log.i("JW", "Socket is connected");
                        Toast.makeText(context, "서버 연결 완료", Toast.LENGTH_SHORT).show();
                        break;
                    case SERVER_DISCONNECTED:

                        break;
                    case WIFI_CONNECTED:

                        if (ssid == null) {

                            ssid = wifiManager.getConnectionInfo().getSSID();
                        }
                        Log.i("JW", "WIFI 연결 완료");
                        Toast.makeText(context, "WIFI 연결 완료: " + ssid, Toast.LENGTH_SHORT).show();
                        break;
                    case WIFI_DISCONNECTED:

                        Log.i("JW", "WIFI 연결 해제");
                        Toast.makeText(context, "WIFI 연결 해제", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
    }

    private void setThread() {

        thread = new Thread(new Runnable() {

            @Override
            public void run() {

                Log.i("JW", "서버소켓 연결 스레드 시작");
                while (isRun) {

                    if (!isConnected_server) {

                        try {

                            Socket socket = new Socket(IP_ADDRESS, PORT);
                            if (socket != null) {

                                if (socket.isConnected()) {

                                    handler_for_toast.sendEmptyMessage(SERVER_CONNECTED);

                                    isConnected_server = true;
                                    isRun = false;

                                    if (socketManager == null) {

                                        socketManager = new SocketManager();
                                    }
                                    socketManager.init(socket, handler_data, communicator);
                                    socketManager.start_thread();
                                    Log.i("JW", "Start socketManager");
                                } else {

                                    Log.i("JW", "Socket is not connected");
                                }
                            } else {

                                Log.i("JW", "socket is null");
                            }

                        } catch (IOException e) {

                            //Log.i("JW", "서버에 연결 실패 :" + e.getMessage() + ", " + e.getCause());
                        }
                    }
                }
                isSet = true;
                Log.i("JW", "서버소켓 연결 스레드 종료");
            }
        });
    }

    public void registReceiver() {

        if (broadcastReceiver == null) {

            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    String action = intent.getAction();

                    switch (action) {
                        // 와이파이 상태 변경 시: 와이파이 비활성화 상태면 활성화 시킴
                        case WifiManager.WIFI_STATE_CHANGED_ACTION:

                            if (!wifiManager.isWifiEnabled()) {

                                // 와이파이 on/off 정보;
                                if (wifiManager.getWifiState() != WifiManager.WIFI_STATE_ENABLING) {

                                    // 와이파이 on
                                    wifiManager.setWifiEnabled(true);
                                    Log.i("JW", "와이파이 활성화");
                                }
                            }
                            break;
                        // 와이파이 스캔 결과 감지 시: 리스트에서 지정 AP SSID 검색해 저장
                        case WifiManager.SCAN_RESULTS_AVAILABLE_ACTION:

                            if (permission == 1) {

                                if (!isConnected) {

                                    List<ScanResult> scanResults = wifiManager.getScanResults();
                                    Log.i("JW", "와이파이 목록 스캔");

                                    for (int i = 0; i < scanResults.size(); i++) {

                                        if (scanResults.get(i).SSID.contains(AP_KEYWORD)) {

                                            Log.i("JW", "지정 AP 탐색 완료");
                                            ScanResult ap = scanResults.get(i);

                                            ssid = ap.SSID;
                                            bssid = ap.BSSID;

                                            tryToConnect();
                                            break;
                                        }
                                    }
                                }
                            }
                            break;
                        // 네트워크 상태 변화 감지 시: 연결 상태 플래그 변경
                        case ConnectivityManager.CONNECTIVITY_ACTION:

                            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                            isConnected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();

                            // 와이파이 연결 되면 서버 소켓에 연결 시도
                            if (isConnected) {

                                handler_for_toast.sendEmptyMessage(WIFI_CONNECTED);

                                if (!isConnected_server && isSet) {

                                    setThread();
                                    isRun = true;
                                    thread.start();
                                    isSet = false;
                                }
                            }
                            // 와이파이 연결 끊어지면 서버 소켓 플래그 비활성화
                            else {

                                handler_for_toast.sendEmptyMessage(WIFI_DISCONNECTED);

                                isConnected_server = false;
                                isRun = false;

                                if (socketManager != null) {

                                    socketManager.stop_thread();
                                }
                            }

                            break;
                    }
                }
            };

            intentFilter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
            intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

            context.registerReceiver(broadcastReceiver, intentFilter);
        }
    }

    public void unregistReceiver() {

        if (broadcastReceiver != null) {

            context.unregisterReceiver(broadcastReceiver);
        }
    }

    private void tryToConnect() {

        Log.i("JW", "와이파이 연결 시도");
//        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();

        if (wfc == null) {
            wfc = new WifiConfiguration();

            wfc.allowedAuthAlgorithms.clear();
            wfc.allowedGroupCiphers.clear();
            wfc.allowedKeyManagement.clear();
            wfc.allowedPairwiseCiphers.clear();
            wfc.allowedPairwiseCiphers.clear();
            wfc.allowedProtocols.clear();
            wfc.SSID = "\"" + ssid + "\"";

            // web 사용 시
            wfc.hiddenSSID = true;
            wfc.wepKeys[0] = "\"" + AP_PSWD + "\"";
            wfc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            wfc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            wfc.wepTxKeyIndex = 0;
            wfc.priority = 40;
            wfc.BSSID = bssid;


            // WSK 사용 시
            /*
            wfc.preSharedKey = "\"" + AP_PSWD + "\"";
            wfc.hiddenSSID = true;
            wfc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            wfc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            wfc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            wfc.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            wfc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            wfc.status = WifiConfiguration.Status.ENABLED;
            wfc.BSSID = bssid;
            */
        }

        int networkId = wifiManager.addNetwork(wfc);
        if (networkId != -1) {

            wifiManager.enableNetwork(networkId, true);
        }
    }
}
