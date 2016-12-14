package com.sinest.gw_1000.communication;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.nio.channels.SocketChannel;
import java.util.List;

/**
 * Created by Jinwook on 2016-12-14.
 */

public class SocketConnector {

    private WifiManager wifiManager;
    private Context context;

    public SocketConnector(Context _context) {

        context = _context;
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        WifiInfo info = wifiManager.getConnectionInfo();
        Log.i("JW", "Wifi status: " + info.toString());

        // Get AP list
        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();

        // AP 이름 검사

        // AP 연결 후 소켓매니저로 연결
    }
}
