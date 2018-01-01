package info.alkor.whereareyou.android;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;

/**
 * SMS helper.
 * Created by Marlena on 2018-01-01.
 */
public class SmsHelper {
    public SmsMessage getMessage(Intent intent) {
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
