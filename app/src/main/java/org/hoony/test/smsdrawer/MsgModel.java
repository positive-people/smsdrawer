package org.hoony.test.smsdrawer;

import android.graphics.drawable.Drawable;
import android.net.Uri;

public class MsgModel {
    private String name;
    private String content;
    private String time;
    private String phonenumber;
    private Uri profile;

    public MsgModel(String name, String content, String time, String phonenumber, Uri profile) {
        this.name = name;
        this.content = content;
        this.time = time;
        this.phonenumber = phonenumber;
        this.profile = profile;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }

    public String getPhonenumber() { return phonenumber; }

    public Uri getProfile() {
        return profile;
    }

    public void setName(String name) {
        this.name = name;
    }
//
//    public void setContent(String content) {
//        this.content = content;
//    }
//
//    public void setTime(String time) {
//        this.time = time;
//    }
//
//    public void setPhonenumber(String phonenumber) { this.phonenumber = phonenumber; }
//
    public void setProfile(Uri profile) {
        this.profile = profile;
    }
}
