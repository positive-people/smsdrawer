package org.hoony.test.smsdrawer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import static org.hoony.test.smsdrawer.SideAdapter.EXTRA_DRAWER_MODEL;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mMainRecyclerView;
    private RecyclerView.Adapter mMainAdapter;
    private RecyclerView.LayoutManager mMainLayoutManager;
    private ArrayList<MsgModel> dataSet = new ArrayList<>();
    private RecyclerView mSideRecyclerView;
    private RecyclerView.Adapter mSideAdapter;
    private RecyclerView.LayoutManager mSideLayoutManager;
    private ArrayList<DrawerModel> drawers = new ArrayList<>();
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawers.add(new DrawerModel("전체"));
        drawers.add(new DrawerModel("학교"));
        drawers.add(new DrawerModel("결제"));
        drawers.add(new DrawerModel("서랍 추가"));

        drawers.get(0).setSpec(DrawerModel.ALL_DRAWER_TYPE);
        drawers.get(drawers.size()-1).setSpec(DrawerModel.ADD_DRAWER_TYPE);

        RecyclerView mMainRecyclerView = findViewById(R.id.main_recycler);
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

        mMainAdapter = new MsgAdapter(dataSet);
        ((MsgAdapter) mMainAdapter).setMain(this);
        mMainRecyclerView.setAdapter(mMainAdapter);


        mSideRecyclerView = findViewById(R.id.side_recycler);
        mSideRecyclerView.setHasFixedSize(true);


        mSideLayoutManager = new GridLayoutManager(getApplicationContext(), 1);//new LinearLayoutManager(this);
        mSideRecyclerView.setLayoutManager(mSideLayoutManager);

        mSideAdapter = new SideAdapter();
        ((SideAdapter) mSideAdapter).setMain(this);
        mSideRecyclerView.setAdapter(mSideAdapter);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_SMS)) {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.READ_SMS},
                                1);
                    }
        } else if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS)
                == PackageManager.PERMISSION_GRANTED) {
            readSMS();
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.READ_CONTACTS},
                                2);
                    }
        } else if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            //연락처 권한 받고 할 일
            mMainAdapter.notifyDataSetChanged();

        }

        // 액션바 이름 변경
        getSupportActionBar().setTitle("메시지");
        // 액션바 홈 버튼 추가
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_name);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode) {
            case 1:
                for(int i = 0; i < permissions.length; i++) {
                    if(permissions[i].equals(Manifest.permission.READ_SMS)) {
                        if(grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            //문자 읽어오기
                            readSMS();
                        }
                    }
                }
                break;
                case 2:
                for(int i = 0; i < permissions.length; i++) {
                    if(permissions[i].equals(Manifest.permission.READ_CONTACTS)) {
                        if(grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            mMainAdapter.notifyDataSetChanged();
                        }
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 0) {
            DrawerModel model = data.getParcelableExtra(EXTRA_DRAWER_MODEL);
            drawers.add(drawers.size()-1, model);
            Log.i("Model", model.getName());
            Log.i("Model", model.getKeywords().toString());
            Log.i("Model", model.getNumbers().toString());
            mSideAdapter.notifyItemInserted(drawers.size()-2);
        }
    }

    void readSMS() {
        Uri uri = Uri.parse("content://sms");
        Cursor mCursor = getContentResolver().query(uri, null, "1=1) GROUP BY (address", null, null);

        if(mCursor == null) return;
        int bodyIndex = mCursor.getColumnIndex("body");
        int addressIndex = mCursor.getColumnIndex("address");
        int dateIndex = mCursor.getColumnIndex("date");
        for(mCursor.moveToFirst();!mCursor.isAfterLast();mCursor.moveToNext()) {
            dataSet.add(new MsgModel(null, mCursor.getString(bodyIndex), mCursor.getString(dateIndex), mCursor.getString(addressIndex),null));
        }
        mCursor.close();
        mMainAdapter.notifyDataSetChanged();
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


        return super.onOptionsItemSelected(item);
    }


}
