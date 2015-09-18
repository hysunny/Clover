package com.clover.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by dan on 2015/6/26.
 */
public class PushMessageReceiver extends BroadcastReceiver {

    @SuppressWarnings("deprecation")
    @Override
    public void onReceive(final Context context, Intent intent) {
        String json = intent.getStringExtra("msg");
        Intent intent1 = new Intent("MESSAGE_COMMING");
        intent1.putExtra("msg", json);
        context.sendOrderedBroadcast(intent1, null);
    }
}