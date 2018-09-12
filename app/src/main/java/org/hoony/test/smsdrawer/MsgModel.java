package org.hoony.test.smsdrawer;

import android.graphics.drawable.Drawable;

public class MsgModel {
    private String name;
    private String content;
    private String time;
    private Drawable profile;

    public MsgModel(String name, String content, String time, Drawable profile) {
        this.name = name;
        this.content = content;
        this.time = time;
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

    public Drawable getProfile() {
        return profile;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setProfile(Drawable profile) {
        this.profile = profile;
    }
}
