package org.hoony.test.smsdrawer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mMainRecyclerView;
    private RecyclerView.Adapter mMainAdapter;
    private RecyclerView.LayoutManager mMainLayoutManager;
    private ArrayList<MsgModel> dataset = new ArrayList<MsgModel>();
    private RecyclerView mSideRecyclerView;
    private RecyclerView.Adapter mSideAdapter;
    private RecyclerView.LayoutManager mSideLayoutManager;
    private DrawerLayout mDrawerLayout;
    private String[] data1 = {"전체","스팸","제주대","결제","컴공","가족","추가"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMainRecyclerView = findViewById(R.id.main_recycler);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mMainRecyclerView.setHasFixedSize(true);

        //액션바 홈버튼 이벤트마다 아이콘 변경
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            public void onDrawerClosed(View drawerView) {
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_name);
            }
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_left_arrow);
            }
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }
            public void onDrawerStateChanged(int newState) {
        }});


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
            Cursor mCursor = getContentResolver().query(uri, null, "1=1) GROUP BY (address", null, null);

            int bodyIndex = mCursor.getColumnIndex("body");
            int addressIndex = mCursor.getColumnIndex("address");
            int dateIndex = mCursor.getColumnIndex("date");
            for(mCursor.moveToFirst();!mCursor.isAfterLast();mCursor.moveToNext()) {
                dataset.add(new MsgModel(mCursor.getString(addressIndex), mCursor.getString(bodyIndex), mCursor.getString(dateIndex), null));
            }
            mMainAdapter.notifyDataSetChanged();
        }

        // 연락처 권한 얻어오는 부분
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
        }

        // 액션바 이름 변경
        getSupportActionBar().setTitle("메시지");
        // 액션바 홈 버튼 추가
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_name);
        getSupportActionBar().setHomeButtonEnabled(true);
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
                            Cursor mCursor = getContentResolver().query(uri, null, ") GROUP BY (address", null, null);

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
    // 액션바 표시
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    //액션바 액션 이벤트 처리
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (mDrawerLayout.isDrawerOpen(mSideRecyclerView)) {
                mDrawerLayout.closeDrawer(mSideRecyclerView);
            } else {
                mDrawerLayout.openDrawer(mSideRecyclerView);
            }
            Toast.makeText(this, "홈아이콘클릭", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.action_setting) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
