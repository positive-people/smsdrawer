package org.hoony.test.smsdrawer.adapter;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.Telephony;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.hoony.test.smsdrawer.AddDrawerActivity;
import org.hoony.test.smsdrawer.MainActivity;
import org.hoony.test.smsdrawer.model.DrawerModel;
import org.hoony.test.smsdrawer.R;

public class SideAdapter extends RecyclerView.Adapter<SideAdapter.SideViewHolder> {
    private MainActivity main;

    public static final String EXTRA_DRAWER_MODEL = "org.hoony.test.smsdrawer.DRAWER_MODEL";
    public static final String EXTRA_SELECTED_DRAWER = "org.hoony.test.smsdrawer.SELECTED_DRAWER";
    public static final String EXTRA_SELECTED_POSITION = "org.hoony.test.smsdrawer.SELECTED_POSITION";

    public class SideViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {
        public TextView mTextName;
        public ImageView mImage;
        public TextView mTextCount;
        public View mViewLine;
        public View drawerView;
        private Handler mHandler;
        private float x;
        private float y;
        private boolean isActive = true;

        private final int LONGPRESS_TIMEOUT = ViewConfiguration.getLongPressTimeout();
        private final int TAP_TIMEOUT = ViewConfiguration.getTapTimeout();

        public SideViewHolder(View drawerView) {
            super(drawerView);
            mTextName = drawerView.findViewById(R.id.text_name);
            mImage = drawerView.findViewById(R.id.image);
            mTextCount = drawerView.findViewById(R.id.text_count);
            mViewLine = drawerView.findViewById(R.id.view_line);
            mHandler = new keyHandler();
            this.drawerView = drawerView;
            drawerView.setOnTouchListener(this);
            //drawerView.setOnLongClickListener(this);
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (!isActive) return false;
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                view.setBackgroundColor(Color.LTGRAY);
                mHandler.removeMessages(3);
                if (main.getDrawers().get(getAdapterPosition()).getSpec() != DrawerModel.ALL_DRAWER_TYPE && main.getDrawers().get(getAdapterPosition()).getSpec() != DrawerModel.ADD_DRAWER_TYPE)
                    mHandler.sendEmptyMessageAtTime(3, motionEvent.getDownTime()+ TAP_TIMEOUT + LONGPRESS_TIMEOUT);
                x = motionEvent.getX();
                y = motionEvent.getY();
                return true;
            } else if (motionEvent.getAction() == MotionEvent.ACTION_OUTSIDE || motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                view.setBackgroundColor(getAdapterPosition() == main.getSelectedDrawerPosition() ? Color.rgb(255, 238, 187) : Color.WHITE);
                mTextName.setTypeface(null, getAdapterPosition() == main.getSelectedDrawerPosition() ? Typeface.BOLD : Typeface.NORMAL);
                mHandler.removeMessages(3);
                return false;
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP && main.getDrawers().get(getAdapterPosition()).getSpec() == DrawerModel.ADD_DRAWER_TYPE) {
                view.setBackgroundColor(Color.WHITE);
                mTextName.setTypeface(null, Typeface.NORMAL);
                Intent intent = new Intent(main, AddDrawerActivity.class);
                main.startActivityForResult(intent, 0);
                mHandler.removeMessages(3);
                return false;
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                int prev = main.getSelectedDrawerPosition();
                main.setSelectedDrawerPosition(getAdapterPosition());
                main.readSMS();
                main.notifyChanged(prev);
                view.setBackgroundColor(getAdapterPosition() == main.getSelectedDrawerPosition() ? Color.rgb(255, 238, 187) : Color.WHITE);
                mTextName.setTypeface(null, getAdapterPosition() == main.getSelectedDrawerPosition() ? Typeface.BOLD : Typeface.NORMAL);
                main.closeSideDrawer();
                mHandler.removeMessages(3);
                return true;
            } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                if ((x-motionEvent.getX())*(x-motionEvent.getX())+(y-motionEvent.getY())*(y-motionEvent.getY()) > 1000)
                    mHandler.removeMessages(3);
            }
            return false;
        }

        private class keyHandler extends Handler {



            public void handleMessage(Message msg){
                switch(msg.what){
                    case 3:
                        isActive = false;
                        AlertDialog.Builder builder = new AlertDialog.Builder(main);
                        builder.setCancelable(false);
                        builder.setTitle(main.getDrawers().get(getAdapterPosition()).getName());
                        builder.setItems(new String[]{"수정", "삭제"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                isActive = true;
                                if (i == 0) {
                                    Intent intent = new Intent(main, AddDrawerActivity.class);
                                    intent.putExtra(EXTRA_SELECTED_DRAWER, main.getDrawers().get(getAdapterPosition()));
                                    intent.putExtra(EXTRA_SELECTED_POSITION, getAdapterPosition());
                                    main.startActivityForResult(intent, 0);
                                } else if (i == 1) {
                                    main.deleteDrawer(getAdapterPosition());
                                }
                            }
                        });
                        builder.create().show();

                        break;
                }
            }
        }
    }

    public void setMain(MainActivity main) {
        this.main = main;
    }

    public SideAdapter.SideViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_item, parent, false);

        SideViewHolder vh = new SideViewHolder(v);
        return vh;
    }

    public void onBindViewHolder(SideViewHolder holder, int position) {
        setMsgCount(position);
        holder.mTextName.setText(main.getDrawers().get(position).getName());
            if(main.getDrawers().get(position).getCount() > 0) {
            holder.mTextCount.setText("(" + new Integer(main.getDrawers().get(position).getCount()).toString() + ")");
        } else {
            holder.mTextCount.setText("");
        }
        if(position == main.getDrawers().size()-1) {
            holder.mViewLine.setVisibility(View.INVISIBLE);
        } else {
            holder.mViewLine.setVisibility(View.VISIBLE);
        }
        holder.drawerView.setBackgroundColor(position == main.getSelectedDrawerPosition() ? Color.rgb(255, 238, 187) : Color.WHITE);
            holder.mTextName.setTypeface(null, position == main.getSelectedDrawerPosition() ? Typeface.BOLD : Typeface.NORMAL);
            if (main.getDrawers().get(position).getSpec() == DrawerModel.ADD_DRAWER_TYPE) {
                holder.mTextName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            } else {
                holder.mTextName.setTextAlignment(View.TEXT_ALIGNMENT_INHERIT);
            }
    }

    void setMsgCount(int position) {
        if (ContextCompat.checkSelfPermission(main,
                Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) return;
        Uri smsUri = Uri.parse("content://sms/");
        DrawerModel model = main.getDrawers().get(position);
            if (model.getSpec() == DrawerModel.ALL_DRAWER_TYPE) {
                Cursor smsCursor = main.getContentResolver().query(smsUri, null, null, null, null);
                if (smsCursor == null) return;
                model.setCount(smsCursor.getCount());

                smsCursor.close();
            } else if (model.getSpec() == DrawerModel.ADD_DRAWER_TYPE) {
                model.setCount(0);
            } else {
                int count = 0;
                String criteria = "";
                for(int i = 0; i < model.getKeywords().size(); i ++) {
                    criteria += "body LIKE '%" + model.getKeywords().get(i) + "%' OR ";
                }
                for(int i = 0; i < model.getNumbers().size(); i ++) {
                    criteria += "address LIKE '%" + model.getNumbers().get(i) + "%' OR ";
                }
                if(model.getSpec() == DrawerModel.ALL_DRAWER_TYPE || criteria.equals("")) {
                    criteria = "1=1 OR ";
                }
                criteria += "1=0";
                Cursor mCursor = main.getContentResolver().query(smsUri, null, criteria, null, null);
                if (mCursor == null) return;
                count += mCursor.getCount();

                model.setCount(count);
                mCursor.close();
            }
    }

    public int getItemCount() {
        return main.getDrawers().size();
    }
}
