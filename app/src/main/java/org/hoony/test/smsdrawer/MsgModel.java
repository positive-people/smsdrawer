package org.hoony.test.smsdrawer;

import android.net.Uri;

public class MsgModel {
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
}
