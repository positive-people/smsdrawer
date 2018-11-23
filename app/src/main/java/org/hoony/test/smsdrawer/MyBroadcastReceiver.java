package org.hoony.test.smsdrawer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String str = ""; // 출력할 문자열 저장
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            SmsMessage[] msgs
                    = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                // PDU 포맷으로 되어 있는 메시지를 복원합니다.
                msgs[i] = SmsMessage
                        .createFromPdu((byte[]) pdus[i]);
                str += msgs[i].getOriginatingAddress()
                        + "에게 문자왔음, " +
                        msgs[i].getMessageBody().toString()
                        + "\n";
            }
            Toast.makeText(context,
                    str, Toast.LENGTH_LONG).show();
        }
    }
}
