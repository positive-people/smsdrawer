package org.hoony.test.smsdrawer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SideAdapter extends RecyclerView.Adapter<SideAdapter.SideViewHolder> {
    private MainActivity main;

    public static final String EXTRA_DRAWER_MODEL = "org.hoony.test.smsdrawer.DRAWER_MODEL";

    public class SideViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {
        public TextView mTextName;
        public ImageView mImage;
        public TextView mTextCount;
        public View mViewLine;
        public View drawerView;

        public SideViewHolder(View drawerView) {
            super(drawerView);
            mTextName = drawerView.findViewById(R.id.text_name);
            mImage = drawerView.findViewById(R.id.image);
            mTextCount = drawerView.findViewById(R.id.text_count);
            mViewLine = drawerView.findViewById(R.id.view_line);
            this.drawerView = drawerView;
            drawerView.setOnTouchListener(this);
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                view.setBackgroundColor(Color.LTGRAY);
                return true;
            } else if (motionEvent.getAction() == MotionEvent.ACTION_OUTSIDE || motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                view.setBackgroundColor(Color.WHITE);
                return false;
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP && main.getDrawers().get(getAdapterPosition()).getSpec() == DrawerModel.ADD_DRAWER_TYPE) {
                view.setBackgroundColor(Color.WHITE);
                Intent intent = new Intent(main, AddDrawerActivity.class);
                main.startActivityForResult(intent, 0);
                return false;
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                view.setBackgroundColor(Color.WHITE);
                return false;
            }
            return false;
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
    }

    void setMsgCount(int position) {
        Uri uri = Uri.parse("content://sms");
        DrawerModel model = main.getDrawers().get(position);
            if (model.getSpec() == DrawerModel.ALL_DRAWER_TYPE) {
                Cursor mCursor = main.getContentResolver().query(uri, null, null, null, null);
                if (mCursor == null) return;
                model.setCount(mCursor.getCount());
                mCursor.close();
            } else if (model.getSpec() == DrawerModel.ADD_DRAWER_TYPE) {
                model.setCount(0);
            } else {
                model.setCount(position*10);
            }
    }

    public int getItemCount() {
        return main.getDrawers().size();
    }
}
