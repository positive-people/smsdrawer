package org.hoony.test.smsdrawer.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.hoony.test.smsdrawer.MainActivity;
import org.hoony.test.smsdrawer.MessagesActivity;
import org.hoony.test.smsdrawer.model.MsgModel;
import org.hoony.test.smsdrawer.R;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Locale;

import static org.hoony.test.smsdrawer.MainActivity.EXTRA_MSG_MODEL;
import static org.hoony.test.smsdrawer.adapter.SideAdapter.EXTRA_DRAWER_MODEL;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.MyViewHolder> {
    private ArrayList<MsgModel> mDataset;
    private MainActivity main;
    private Drawable defaultImage;

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {
        TextView mTextName;
        TextView mTextContent;
        TextView mTextTime;
        TextView mTextNameFirst;
        ImageView mImageProfile;
        View mLine;

        MyViewHolder(View msgView) {
            super(msgView);
            mTextName = msgView.findViewById(R.id.text_name);
            mTextContent = msgView.findViewById(R.id.text_content);
            mTextTime = msgView.findViewById(R.id.text_time);
            mImageProfile = msgView.findViewById(R.id.image_profile);
            mLine = msgView.findViewById(R.id.line);
            mTextNameFirst = msgView.findViewById(R.id.text_name_first);
            msgView.setOnTouchListener(this);
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                view.setBackgroundColor(Color.LTGRAY);
                return true;
            } else if (motionEvent.getAction() == MotionEvent.ACTION_OUTSIDE || motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                view.setBackgroundColor(Color.rgb(250,250,250));
                return false;
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                view.setBackgroundColor(Color.rgb(250,250,250));
                Intent intent = new Intent(main, MessagesActivity.class);
                intent.putExtra(EXTRA_MSG_MODEL, mDataset.get(getAdapterPosition()));
                intent.putExtra(EXTRA_DRAWER_MODEL, main.getCurrentDrawerModel());
                main.startActivity(intent);
                return false;
            }
            return false;
        }
    }

    public void setMain(MainActivity main) {
        this.main = main;
        defaultImage = main.getResources().getDrawable(R.drawable.ic_default_person);
    }

    public MsgAdapter(ArrayList<MsgModel> myDataSet) {
        mDataset = myDataSet;
    }

    @NonNull
    public MsgAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_item, parent, false);

        return new MyViewHolder(v);
    }

    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Cursor cursor = getContactName(mDataset.get(position).getPhoneNumber());
        if(mDataset.get(position).getName() == null && cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            mDataset.get(position).setName(cursor.getString(0));
            if (cursor.getString(1) != null ) {
                mDataset.get(position).setProfile(Uri.parse(cursor.getString(1)));
            }
            cursor.close();
        }
        if(mDataset.get(position).getName() == null || mDataset.get(position).getName().isEmpty())
            holder.mTextName.setText(mDataset.get(position).getPhoneNumber());
        else
            holder.mTextName.setText(mDataset.get(position).getName());
        holder.mTextContent.setText(mDataset.get(position).getContent());


        String sLatestDate;
        try {
            java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
            sLatestDate = formatter.format(new Date(Long.parseLong(mDataset.get(position).getTime())));
        } catch (Exception ex) {
            sLatestDate = "";
        }

        holder.mTextTime.setText(sLatestDate);
        holder.mImageProfile.setVisibility(View.VISIBLE);
        if(mDataset.get(position).getProfile() != null) {
            holder.mImageProfile.setImageURI(mDataset.get(position).getProfile());
            holder.mTextNameFirst.setText("");
        } else {
            if(defaultImage != null) {
                holder.mImageProfile.setImageDrawable(defaultImage);

            } else {
                holder.mImageProfile.setImageResource(R.drawable.ic_default_person);

            }
            if(mDataset.get(position).getName() != null && !mDataset.get(position).getName().isEmpty()) {
                holder.mTextNameFirst.setText(mDataset.get(position).getName().subSequence(0, 1).toString());
                holder.mImageProfile.setImageDrawable(new ColorDrawable(0xFFFFEEBB));
            } else
                holder.mTextNameFirst.setText("");
            //holder.mTextNameFirst.setText(mDataSet.get(position).getName().charAt(0));
        }
        holder.mImageProfile.setBackground(new ShapeDrawable(new OvalShape()));
        holder.mImageProfile.setClipToOutline(true);
    }

    public int getItemCount() {
            return mDataset.size();
    }

    private Cursor getContactName(final String phoneNumber)

    {
        if (ContextCompat.checkSelfPermission(main,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        if(phoneNumber == null || phoneNumber.isEmpty() ) {
            return null;
        }
        Uri uri=Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,Uri.encode(phoneNumber));

        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup.PHOTO_THUMBNAIL_URI};

        return main.getContentResolver().query(uri,projection,null,null,null);
    }
}
