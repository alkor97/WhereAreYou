package info.alkor.whereareyou.android.receivers;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;

import info.alkor.whereareyou.WhereAreYouContext;
import info.alkor.whereareyou.common.TextHelper;

/**
 * Abstract SMS receiver targeted for this application.
 * Created by Marlena on 2018-01-01.
 */
public abstract class AbstractSmsReceiver extends AbstractBroadcastReceiver {

    private static final TextHelper TEXT_HELPER = new TextHelper();

    @Override
    protected void onReceive(WhereAreYouContext context, Intent intent) {
        final SmsMessage message = getMessage(intent);
        if (message != null) {
            final String phone = TEXT_HELPER.normalizePhone(message.getOriginatingAddress());
            final String name = context.getContactsHelper().getDisplayName(phone);
            onReceive(context, phone, name, message.getMessageBody());
        }
    }

    public abstract void onReceive(WhereAreYouContext context, String originatingAddress, String
            originatingName, String messageBody);

    private SmsMessage getMessage(Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
            return messages[0];
        } else {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                final Object[] pdus = (Object[]) bundle.get("pdus");
                if (pdus != null && pdus.length > 0) {
                    return SmsMessage.createFromPdu((byte[]) pdus[0]);
                }
            }
        }
        return null;
    }
}
