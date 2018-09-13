package org.hoony.test.smsdrawer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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

//        dataset.add(new MsgModel("곽효림", "바보", "오후 5:40", null));
//        dataset.add(new MsgModel("원동훈", "바보", "오후 3:42", null));
//        dataset.add(new MsgModel("현지훈", "바보", "오전 8:02", null));
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
            Cursor mCursor = getContentResolver().query(uri, null, null, null, null);

            int bodyIndex = mCursor.getColumnIndex("body");
            int addressIndex = mCursor.getColumnIndex("address");
            int dateIndex = mCursor.getColumnIndex("date");
            for(mCursor.moveToFirst();!mCursor.isAfterLast();mCursor.moveToNext()) {
                dataset.add(new MsgModel(mCursor.getString(addressIndex), mCursor.getString(bodyIndex), mCursor.getString(dateIndex), null));
            }
            mMainAdapter.notifyDataSetChanged();
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
                            Cursor mCursor = getContentResolver().query(uri, null, null, null, null);

                            int bodyIndex = mCursor.getColumnIndex("body");
                            int addressIndex = mCursor.getColumnIndex("address");
                            int dateIndex = mCursor.getColumnIndex("date");
                            for(mCursor.moveToFirst();!mCursor.isAfterLast();mCursor.moveToNext()) {
                                dataset.add(new MsgModel(mCursor.getString(addressIndex), mCursor.getString(bodyIndex), mCursor.getString(dateIndex), null));
                            }
                            mMainAdapter.notifyDataSetChanged();
                        }
                    }
                }
                break;
        }
    }
}
