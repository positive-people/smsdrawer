package org.hoony.test.smsdrawer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private ArrayList<MsgModel> mDataset;

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
        holder.mTextName.setText(mDataset.get(position).getName());
        holder.mTextContent.setText(mDataset.get(position).getContent());
        holder.mTextTime.setText(mDataset.get(position).getTime());
        if(mDataset.get(position).getProfile() != null)
            holder.mImageProfile.setImageDrawable(mDataset.get(position).getProfile());
    }

    public int getItemCount() {
            return mDataset.size();
    }
}
