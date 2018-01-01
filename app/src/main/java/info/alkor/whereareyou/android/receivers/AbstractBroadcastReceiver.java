package info.alkor.whereareyou.android.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import info.alkor.whereareyou.WhereAreYouContext;

/**
 * Abstract broadcast receiver specialized fo the application.
 * Created by Marlena on 2018-01-01.
 */
public abstract class AbstractBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        onReceive(getApplicationContext(context), intent);
    }

    protected abstract void onReceive(WhereAreYouContext context, Intent intent);

    private WhereAreYouContext getApplicationContext(Context context) {
        return (WhereAreYouContext) context.getApplicationContext();
    }
}
