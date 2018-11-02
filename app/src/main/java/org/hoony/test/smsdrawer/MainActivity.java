package org.hoony.test.smsdrawer;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mMainRecyclerView;
    private RecyclerView.Adapter mMainAdapter;
    private RecyclerView.LayoutManager mMainLayoutManager;
    private ArrayList<MsgModel> dataset = new ArrayList<MsgModel>();
    private RecyclerView mSideRecyclerView;
    private RecyclerView.Adapter mSideAdapter;
    private RecyclerView.LayoutManager mSideLayoutManager;
    private String[] data1 = {"전체","스팸","제주대","결제","컴공","가족","추가"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMainRecyclerView = findViewById(R.id.main_recycler);
        mMainRecyclerView.setHasFixedSize(true);

        mMainLayoutManager = new LinearLayoutManager(this);
        mMainRecyclerView.setLayoutManager(mMainLayoutManager);

        mMainAdapter = new MyAdapter(dataset);
        mMainRecyclerView.setAdapter(mMainAdapter);


        mSideRecyclerView = findViewById(R.id.side_recycler);
        mSideRecyclerView.setHasFixedSize(true);


        mSideLayoutManager = new GridLayoutManager(getApplicationContext(), 1);//new LinearLayoutManager(this);
        mSideRecyclerView.setLayoutManager(mSideLayoutManager);

        mSideAdapter = new SideAdapter(data1);
        mSideRecyclerView.setAdapter(mSideAdapter);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_SMS)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_SMS},
                        1);
            }
        } else if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS)
                == PackageManager.PERMISSION_GRANTED) {
            Uri uri = Uri.parse("content://sms");
            Cursor mCursor = getContentResolver().query(uri, null, "1=1) GROUP BY (address", null, null);

            int bodyIndex = mCursor.getColumnIndex("body");
            int addressIndex = mCursor.getColumnIndex("address");
            int dateIndex = mCursor.getColumnIndex("date");
            for(mCursor.moveToFirst();!mCursor.isAfterLast();mCursor.moveToNext()) {
                dataset.add(new MsgModel(getContactName(mCursor.getString(addressIndex), this), mCursor.getString(bodyIndex), mCursor.getString(dateIndex), mCursor.getString(addressIndex), null));
            }
            mMainAdapter.notifyDataSetChanged();
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        2);
            }
        } else if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            //연락처 권한 받고 할 일


        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode) {
            case 1:
                for(int i = 0; i < permissions.length; i++) {
                    if(permissions[i].equals(Manifest.permission.READ_SMS)) {
                        if(grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            //문자 읽어오기
                            Uri uri = Uri.parse("content://sms");
                            Cursor mCursor = getContentResolver().query(uri, null, "1=1) GROUP BY (address", null, null);

                            int bodyIndex = mCursor.getColumnIndex("body");
                            int addressIndex = mCursor.getColumnIndex("address");
                            int dateIndex = mCursor.getColumnIndex("date");
                            for(mCursor.moveToFirst();!mCursor.isAfterLast();mCursor.moveToNext()) {
                                dataset.add(new MsgModel(getContactName(mCursor.getString(addressIndex), this), mCursor.getString(bodyIndex), mCursor.getString(dateIndex), mCursor.getString(addressIndex),null));
                            }
                            mMainAdapter.notifyDataSetChanged();
                        }
                    }
                }
                break;
        }
    }

    public String getContactName(final String phoneNumber, Context context)

    {
        if(phoneNumber == null || phoneNumber == "" || phoneNumber.isEmpty() ) {
            return "";
        }
        Uri uri=Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,Uri.encode(phoneNumber));

        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};

        String contactName="";

        Cursor cursor=context.getContentResolver().query(uri,projection,null,null,null);

        if (cursor != null) {
            if(cursor.moveToFirst()) {
                contactName=cursor.getString(0);
            }
            cursor.close();
        }

        return contactName;
    }

}
