package com.example.mirry.chat.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import com.example.mirry.chat.common.Common;
import com.example.mirry.chat.service.NetBroadcastReceiver;
import com.example.mirry.chat.utils.NetUtil;

public class BaseActivity extends Activity implements NetBroadcastReceiver.NetEvent{
    public static NetBroadcastReceiver.NetEvent event;
    private int netType;
    protected boolean isNetConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        event = this;
        isNetConnected = inspectNet();
    }

    @Override
    public void onNetChange(int netType) {

    }

    public boolean inspectNet() {
        netType = NetUtil.getNetWorkState(BaseActivity.this);
        return isNetConnect(netType);
    }

    public boolean isNetConnect(int netType) {
        if (netType == Common.Wifi) {
            return true;
        } else if (netType == Common.MobileNet) {
            return true;
        } else if (netType == Common.NoNet) {
            return false;
        }
        return false;
    }

    public int getScreenWidth() {
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }
}
