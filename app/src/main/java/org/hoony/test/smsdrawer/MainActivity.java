package org.hoony.test.smsdrawer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import org.hoony.test.smsdrawer.model.DrawerModel;
import org.hoony.test.smsdrawer.model.MsgModel;
import org.hoony.test.smsdrawer.adapter.MsgAdapter;
import org.hoony.test.smsdrawer.adapter.SideAdapter;

import java.util.ArrayList;

import static org.hoony.test.smsdrawer.adapter.SideAdapter.EXTRA_DRAWER_MODEL;
import static org.hoony.test.smsdrawer.adapter.SideAdapter.EXTRA_SELECTED_POSITION;

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
    private int selectedDrawerPosition = 0;

    public final static String EXTRA_MSG_MODEL = "org.hoony.test.smsdrawer.MSG_MODEL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawers.add(new DrawerModel("전체"));
        DrawerModel jeju = new DrawerModel("제주대");
        jeju.addNumber("0647542306");
        jeju.addNumber("0647544475");
        jeju.addNumber("0647543095");
        jeju.addNumber("0647542037");
        jeju.addNumber("0647542303");
        jeju.addNumber("0647542304");
        jeju.addNumber("0647548243");
        jeju.addNumber("0647542204");
        jeju.addNumber("0647548211");
        jeju.addNumber("0647542291");
        jeju.addNumber("0647548261");
        jeju.addNumber("0647542057");
        jeju.addNumber("0647548268");
        jeju.addNumber("0647542023");
        jeju.addNumber("0647543650");
        jeju.addNumber("0647542292");
        jeju.addNumber("0647542053");
        jeju.addNumber("0647548265");
        jeju.addNumber("0647544415");
        jeju.addNumber("0647542097");
        drawers.add(jeju);

        DrawerModel purc = new DrawerModel("결제");
        purc.addKeyword("결제금액");
        purc.addKeyword("결제가 완료");
        purc.addKeyword("체크승인");
        purc.addKeyword("승인");
        drawers.add(purc);
        drawers.add(new DrawerModel("✚"));

        drawers.get(0).setSpec(DrawerModel.ALL_DRAWER_TYPE);
        drawers.get(drawers.size()-1).setSpec(DrawerModel.ADD_DRAWER_TYPE);

        mMainRecyclerView = findViewById(R.id.main_recycler);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mMainRecyclerView.setHasFixedSize(true);

        //액션바 홈버튼 이벤트마다 아이콘 변경
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            public void onDrawerClosed(@NonNull View drawerView) {
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_name);
            }
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_left_arrow);
            }
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
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
            int position = data.getIntExtra(EXTRA_SELECTED_POSITION, 999999999);
            if (position == 999999999) {
                drawers.add(drawers.size() - 1, model);
                mSideAdapter.notifyItemInserted(drawers.size()-2);
            } else {
                drawers.set(position, model);
                mSideAdapter.notifyItemChanged(position);
                if (position == getSelectedDrawerPosition())
                    readSMS();
            }
        }
    }

    public void readSMS() {
        Uri uri = Uri.parse("content://sms");
        String criteria = "";
        for(int i = 0; i < drawers.get(getSelectedDrawerPosition()).getKeywords().size(); i ++) {
            criteria += "body LIKE '%" + drawers.get(getSelectedDrawerPosition()).getKeywords().get(i) + "%' OR ";
        }
        for(int i = 0; i < drawers.get(getSelectedDrawerPosition()).getNumbers().size(); i ++) {
            criteria += "address LIKE '%" + drawers.get(getSelectedDrawerPosition()).getNumbers().get(i) + "%' OR ";
        }
        if(drawers.get(getSelectedDrawerPosition()).getSpec() == DrawerModel.ALL_DRAWER_TYPE || criteria.equals("")) {
            criteria = "1=1 OR ";
        }
        criteria += "1=0) GROUP BY (address";
        Cursor mCursor = getContentResolver().query(uri, null, criteria, null, "max(date) DESC");

        if (mCursor == null) return;
        int bodyIndex = mCursor.getColumnIndex("body");
        int addressIndex = mCursor.getColumnIndex("address");
        int dateIndex = mCursor.getColumnIndex("date");
        dataSet.clear();
        for(mCursor.moveToFirst();!mCursor.isAfterLast();mCursor.moveToNext()) {
            dataSet.add(new MsgModel(null, mCursor.getString(bodyIndex), mCursor.getString(dateIndex), mCursor.getString(addressIndex),null));
        }
        mCursor.close();
        mMainAdapter.notifyDataSetChanged();
    }

    public ArrayList<DrawerModel> getDrawers() {
        return drawers;
    }

    //액션바 액션 이벤트 처리
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (mDrawerLayout.isDrawerOpen(mSideRecyclerView)) {
                mDrawerLayout.closeDrawer(mSideRecyclerView);
            } else {
                mDrawerLayout.openDrawer(mSideRecyclerView);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //취소버튼 조작
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mDrawerLayout.isDrawerOpen(mSideRecyclerView)) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    mDrawerLayout.closeDrawer(mSideRecyclerView);
                }
            }
        } else {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return super.onKeyDown(keyCode, event);
                }
            }
        }
        return true;
    }

    public void closeSideDrawer() {
        if (mDrawerLayout.isDrawerOpen(mSideRecyclerView)) mDrawerLayout.closeDrawer(mSideRecyclerView);
    }

    public void notifyChanged(int pos) {
        mSideAdapter.notifyItemChanged(pos);
    }


    public int getSelectedDrawerPosition() {
        return selectedDrawerPosition;
    }

    public void setSelectedDrawerPosition(int selectedDrawerPosition) {
        this.selectedDrawerPosition = selectedDrawerPosition;
    }

    public DrawerModel getCurrentDrawerModel() {
        return drawers.get(getSelectedDrawerPosition());
    }

    public void deleteDrawer(int position) {
        boolean change = position == getSelectedDrawerPosition();
        boolean adjust = position < getSelectedDrawerPosition();
        if (drawers.get(position).getSpec() != DrawerModel.ALL_DRAWER_TYPE && drawers.get(position).getSpec() != DrawerModel.ADD_DRAWER_TYPE) {
            drawers.remove(position);
            mSideAdapter.notifyItemRemoved(position);
        }
        if (drawers.size() >= getSelectedDrawerPosition() + 1) {
            setSelectedDrawerPosition(drawers.size()-2);
            mSideAdapter.notifyItemChanged(getSelectedDrawerPosition());
        }
        if (change) {
            readSMS();
            mSideAdapter.notifyItemChanged(getSelectedDrawerPosition());
        } else if (adjust) {
            setSelectedDrawerPosition(getSelectedDrawerPosition()-1);
        }
    }
}
