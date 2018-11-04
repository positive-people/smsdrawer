package org.hoony.test.smsdrawer;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private ArrayList<MsgModel> mDataset;
    private Activity main;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextName;
        public TextView mTextContent;
        public TextView mTextTime;
        public ImageView mImageProfile;
        public View mLine;

        public MyViewHolder(View msgView) {
            super(msgView);
            mTextName = msgView.findViewById(R.id.text_name);
            mTextContent = msgView.findViewById(R.id.text_content);
            mTextTime = msgView.findViewById(R.id.text_time);
            mImageProfile = msgView.findViewById(R.id.image_profile);
            mLine = msgView.findViewById(R.id.line);

        }
    }

    public void setMain(Activity main) {
        this.main = main;
    }

    public MyAdapter(ArrayList<MsgModel> myDataset) {
        mDataset = myDataset;
    }

    public void setDataset(ArrayList<MsgModel> myDataset) {
        mDataset = myDataset;
    }

    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_item, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        if(mDataset.get(position).getName() == null) {
            Log.i("aa", "dss");
            mDataset.get(position).setName(getContactName(mDataset.get(position).getPhonenumber()));
        }
        if(mDataset.get(position).getName().isEmpty())
            holder.mTextName.setText(mDataset.get(position).getPhonenumber());
        else
            holder.mTextName.setText(mDataset.get(position).getName());
        holder.mTextContent.setText(mDataset.get(position).getContent());


        String sLatestDate;
        try {
            java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sLatestDate = formatter.format(mDataset.get(position).getTime());
        } catch (Exception ex) {
            sLatestDate = "";
        }

        holder.mTextTime.setText(sLatestDate);
        if(mDataset.get(position).getProfile() != null)
            holder.mImageProfile.setImageDrawable(mDataset.get(position).getProfile());
        holder.mImageProfile.setBackground(new ShapeDrawable(new OvalShape()));
        holder.mImageProfile.setClipToOutline(true);
    }

    public int getItemCount() {
            return mDataset.size();
    }

    public String getContactName(final String phoneNumber)

    {
        if (ContextCompat.checkSelfPermission(main,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        if(phoneNumber == null || phoneNumber == "" || phoneNumber.isEmpty() ) {
            return "";
        }
        Uri uri=Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,Uri.encode(phoneNumber));

        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};

        String contactName="";


        Cursor cursor=main.getContentResolver().query(uri,projection,null,null,null);

        if (cursor != null) {
            if(cursor.moveToFirst()) {
                contactName=cursor.getString(0);
            }
            cursor.close();
        }

        return contactName;
    }
}
