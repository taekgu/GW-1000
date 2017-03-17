package com.sinest.gw_1000.setting;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.sinest.gw_1000.management.Application_manager;

/**
 * Created by Administrator on 2016-12-03.
 */

public class ShutdownAdminReceiver extends DeviceAdminReceiver {
    @Override
    public void onDisabled(Context context, Intent intent) {
        Application_manager.getToastManager().popToast(12);
    }

    @Override
    public void onEnabled(Context context, Intent intent) {
        Application_manager.getToastManager().popToast(13);
    }
}
