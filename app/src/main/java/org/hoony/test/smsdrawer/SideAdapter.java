package org.hoony.test.smsdrawer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SideAdapter extends RecyclerView.Adapter<SideAdapter.SideViewHolder> {
    private String[] mDataset;

    public static class SideViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextName;
        public ImageView mImage;

        public SideViewHolder(View drawerView) {
            super(drawerView);
            mTextName = drawerView.findViewById(R.id.text_name);
            mImage = drawerView.findViewById(R.id.image);;
        }
    }

    public SideAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    public SideAdapter.SideViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_item, parent, false);

        SideViewHolder vh = new SideViewHolder(v);
        return vh;
    }

    public void onBindViewHolder(SideViewHolder holder, int position) {
        holder.mTextName.setText(mDataset[position]);
        //if(mDataset.get(position).getProfile() != null)
          //  holder.mImage.setImageDrawable(mDataset.get(position).getProfile());
    }

    public int getItemCount() {
        return mDataset.length;
    }
}
