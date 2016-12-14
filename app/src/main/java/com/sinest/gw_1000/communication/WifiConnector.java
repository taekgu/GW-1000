package com.sinest.gw_1000.communication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.nio.channels.SocketChannel;
import java.util.List;

/**
 * Created by Jinwook on 2016-12-14.
 */

public class WifiConnector extends Thread {

    private Context context;

    private boolean isRun = false;

    private WifiManager wifiManager;
    private BroadcastReceiver broadcastReceiver;
    private String bssid;
    private boolean isConnected = false;

    public WifiConnector(Context _context) {

        context = _context;
        init();
    }

    private void init() {

        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        // 와이파이 사용가능여부
        if (!wifiManager.isWifiEnabled()) {

            // 와이파이 on/off 정보
            if (wifiManager.getWifiState() != WifiManager.WIFI_STATE_ENABLING) {

                // 와이파이 on
                wifiManager.setWifiEnabled(true);
            }
        }

        // 스캔 시작
        wifiManager.startScan();
    }

    @Override
    public void run() {
        super.run();

        while (isRun) {

            try {


            } catch (Exception e) {

            }
        }
    }

    private void registReceiver() {

        if (broadcastReceiver == null) {

            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    isConnected = checkInternetState();

                    // 와이파이 연결 안돼있을 시
                    if (!isConnected) {

                        List<ScanResult> scanResults = wifiManager.getScanResults();

                        for (int i = 0; i < scanResults.size(); i++) {

                            if (scanResults.get(i).SSID.contains("GW1000")) {

                                ScanResult ap = scanResults.get(i);

                                bssid = ap.BSSID;

                                tryToConnect();
                            }
                        }
                    }
                }
            };

            context.registerReceiver(broadcastReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        }
    }

    private void unregistReceiver() {

        if (broadcastReceiver != null) {

            context.unregisterReceiver(broadcastReceiver);
        }
    }

    private void tryToConnect() {

        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();

        WifiConfiguration wfc = null;

        for (int i=0; i<list.size(); i++) {

            if (list.get(i).BSSID.equals(bssid)) {

                wfc = list.get(i);
                break;
            }
        }

        if (wfc != null) {

            wfc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            wfc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            wfc.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            wfc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            wfc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            wfc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);

            wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            wfc.wepKeys[0] = "1234567890";
            wfc.wepTxKeyIndex = 0;

            int networkId = wifiManager.addNetwork(wfc);
            if (networkId != -1) {

                wifiManager.enableNetwork(networkId, true);
                Log.i("JW", "WIFI 연결 완료: " + bssid);
            }

            isConnected = checkInternetState();
        }
    }

    private boolean checkInternetState() {

        if (context == null)
            return false;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
    }
}
