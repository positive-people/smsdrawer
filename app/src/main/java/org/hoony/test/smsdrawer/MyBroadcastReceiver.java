package org.hoony.test.smsdrawer;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsMessage;

import org.hoony.test.smsdrawer.model.DrawerModel;
import org.hoony.test.smsdrawer.model.MsgModel;

import static org.hoony.test.smsdrawer.MainActivity.EXTRA_MSG_MODEL;
import static org.hoony.test.smsdrawer.adapter.SideAdapter.EXTRA_DRAWER_MODEL;

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String str = "";
        String str1 = "";
        String num = "";

        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            SmsMessage[] msgs = new SmsMessage[pdus.length];

            for (int i = 0; i < msgs.length; i++) {
                // PDU 포맷으로 되어 있는 메시지를 복원합니다.
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                str = msgs[i].getOriginatingAddress();
                num = str;
                str1 = msgs[i].getMessageBody();
            }
            Cursor cursor = getContactName(str, context);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                str = cursor.getString(0);
            }
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);
                notificationChannel.setDescription("Channel description");
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                notificationChannel.enableVibration(true);
                mNotificationManager.createNotificationChannel(notificationChannel);
            }
            Intent ni = new Intent(context, MessagesActivity.class);
            MsgModel mm = new MsgModel(str, str1, "0", num, null);
            DrawerModel dm = new DrawerModel("전체");
            dm.setSpec(DrawerModel.ALL_DRAWER_TYPE);
            ni.putExtra(EXTRA_MSG_MODEL, mm);
            ni.putExtra(EXTRA_DRAWER_MODEL, dm);
            PendingIntent mPendingIntent = PendingIntent.getActivity(context, 0, ni, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle(str)
                    .setContentText(str1)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setContentIntent(mPendingIntent)
                    .setNumber(1)
                    .setAutoCancel(true);

            Intent bi = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
            // 패키지 네임과 클래스 네임 설정
            bi.putExtra("badge_count_package_name", "org.hoony.test.smsdrawer");
            bi.putExtra("badge_count_class_name", "MessagesActivity");
            // 업데이트 카운트
            bi.putExtra("badge_count", 1);
            context.sendBroadcast(bi);
            mNotificationManager.notify(Integer.parseInt(num), mBuilder.build());
        }
    }

    private Cursor getContactName(final String phoneNumber, Context context)
    {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        if(phoneNumber == null || phoneNumber.isEmpty() ) {
            return null;
        }
        Uri uri=Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,Uri.encode(phoneNumber));

        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup.PHOTO_THUMBNAIL_URI};

        return context.getContentResolver().query(uri,projection,null,null,null);
    }
}
