package org.hoony.test.smsdrawer;

import android.graphics.drawable.Drawable;

public class DrawerModel {
    private String name;
    private Drawable image;

    public DrawerModel(String name, Drawable image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public Drawable getImage() {
        return image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }
}
