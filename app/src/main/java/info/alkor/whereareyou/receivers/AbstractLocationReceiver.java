package info.alkor.whereareyou.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import java.util.HashMap;
import java.util.Map;

import info.alkor.whereareyou.senders.LocationActionsSender;

/**
 * Created by Marlena on 2017-12-21.
 */

public abstract class AbstractLocationReceiver {

    protected final LocationActionsSender helper;
    private final BroadcastReceiver handler;
    private final Map<String, IntentReceiver> intentReceivers = new HashMap<>();

    public interface IntentReceiver {
        void onReceive(Intent intent);
    }

    AbstractLocationReceiver(LocationActionsSender helper) {
        this.helper = helper;
        this.handler = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                IntentReceiver intentReceiver = intentReceivers.get(intent.getAction());
                if (intentReceiver != null) {
                    intentReceiver.onReceive(intent);
                }
            }
        };
    }

    protected void registerReceiver(String action, IntentReceiver intentReceiver) {
        intentReceivers.put(action, intentReceiver);
    }

    public void registerHandler(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        for (String action : intentReceivers.keySet()) {
            intentFilter.addAction(action);
        }
        getBroadcastReceiver(context.getApplicationContext()).registerReceiver(handler, intentFilter);
    }

    public void unregisterHandler(Context context) {
        getBroadcastReceiver(context).unregisterReceiver(handler);
    }

    private LocalBroadcastManager getBroadcastReceiver(Context context) {
        return LocalBroadcastManager.getInstance(context);
    }
}
