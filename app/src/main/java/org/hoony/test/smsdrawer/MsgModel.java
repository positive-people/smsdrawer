package org.hoony.test.smsdrawer;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class MsgModel implements Parcelable {
    private String name;
    private String content;
    private String time;
    private String phoneNumber;
    private Uri profile;

    MsgModel(String name, String content, String time, String phoneNumber, Uri profile) {
        this.name = name;
        this.content = content;
        this.time = time;
        this.phoneNumber = phoneNumber;
        this.profile = profile;
    }

    protected MsgModel(Parcel in) {
        name = in.readString();
        content = in.readString();
        time = in.readString();
        phoneNumber = in.readString();
        profile = in.readParcelable(Uri.class.getClassLoader());
    }

    public static final Creator<MsgModel> CREATOR = new Creator<MsgModel>() {
        @Override
        public MsgModel createFromParcel(Parcel in) {
            return new MsgModel(in);
        }

        @Override
        public MsgModel[] newArray(int size) {
            return new MsgModel[size];
        }
    };

    String getName() {
        return name;
    }

    String getContent() {
        return content;
    }

    String getTime() {
        return time;
    }

    String getPhoneNumber() { return phoneNumber; }

    Uri getProfile() {
        return profile;
    }

    public void setName(String name) {
        this.name = name;
    }

    void setProfile(Uri profile) {
        this.profile = profile;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(content);
        parcel.writeString(time);
        parcel.writeString(phoneNumber);
        parcel.writeParcelable(profile, i);
    }
}
