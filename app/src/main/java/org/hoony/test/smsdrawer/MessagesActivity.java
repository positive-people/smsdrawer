package org.hoony.test.smsdrawer;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import org.hoony.test.smsdrawer.model.Author;
import org.hoony.test.smsdrawer.model.DrawerModel;
import org.hoony.test.smsdrawer.model.Message;
import org.hoony.test.smsdrawer.model.MsgModel;

import java.util.ArrayList;
import java.util.Date;

import static org.hoony.test.smsdrawer.MainActivity.EXTRA_MSG_MODEL;
import static org.hoony.test.smsdrawer.adapter.SideAdapter.EXTRA_DRAWER_MODEL;

public class MessagesActivity extends AppCompatActivity {
    private MessagesList messagesList;
    private MsgModel model;
    private Author your;
    private Author me;
    private MessagesListAdapter<Message> adapter;
    private MessageInput inputView;
    private DrawerModel drawer;
    public static final String SENDER_ID = "org.hoony.test.smsdrawer.SENDER";

    ImageLoader imageLoader = new ImageLoader() {
        @Override
        public void loadImage(ImageView imageView, @Nullable String url, @Nullable Object payload) {
            if (url != null)
                imageView.setImageURI(Uri.parse(url));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent intent = getIntent();
        model = intent.getParcelableExtra(EXTRA_MSG_MODEL);
        drawer = intent.getParcelableExtra(EXTRA_DRAWER_MODEL);
        getSupportActionBar().setTitle(model.getName() == null ? model.getPhoneNumber() : model.getName());
        messagesList = findViewById(R.id.messagesList);
        inputView = findViewById(R.id.input);

        adapter = new MessagesListAdapter<>(SENDER_ID, imageLoader);
        messagesList.setAdapter(adapter);

        your = new Author(model.getPhoneNumber(), model.getName(), model.getProfile() == null ? null : model.getProfile().toString());
        me = new Author(SENDER_ID, "", null);
        readSMS();

        //문자보내면 보낸문자 출력
        inputView.setInputListener(new MessageInput.InputListener() {
            @Override
            public boolean onSubmit(CharSequence input) {
                Message message = new Message(me.getId(), input.toString(), me, new Date());
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(your.getId(), null, input.toString(), null, null);
                    adapter.addToStart(message, true);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "전송 실패!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                return true;
            }

        });

    }

    //액션바 액션 이벤트 처리
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }


        return super.onOptionsItemSelected(item);
    }

    void readSMS() {
        Uri uri = Uri.parse("content://sms");


        String criteria = "(";
        for(int i = 0; i < drawer.getKeywords().size(); i ++) {
            criteria += "body LIKE '%" + drawer.getKeywords().get(i) + "%' OR ";
        }
        for(int i = 0; i < drawer.getNumbers().size(); i ++) {
            criteria += "address LIKE '%" + drawer.getNumbers().get(i) + "%' OR ";
        }
        if(drawer.getSpec() == DrawerModel.ALL_DRAWER_TYPE || criteria.equals("")) {
            criteria = "(1=1 OR ";
        }
        criteria += "1=0) AND address = " + DatabaseUtils.sqlEscapeString(your.getId());

        Cursor mCursor = getContentResolver().query(uri, null, criteria, null, null);

        if (mCursor == null) return;
        int bodyIndex = mCursor.getColumnIndex("body");
        int addressIndex = mCursor.getColumnIndex("address");
        int dateIndex = mCursor.getColumnIndex("date");

        ArrayList<Message> list = new ArrayList<>();
        for(mCursor.moveToFirst();!mCursor.isAfterLast();mCursor.moveToNext()) {
            Message message = new Message(mCursor.getString(addressIndex), mCursor.getString(bodyIndex), mCursor.getString(mCursor.getColumnIndex("type")).equals("1") ? your : me, new Date(Long.parseLong(mCursor.getString(dateIndex))));
            list.add(message);
        }
        mCursor.close();
        Log.i("msg", new Integer(list.size()).toString());
        adapter.addToEnd(list, false);
    }

}
