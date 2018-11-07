package org.hoony.test.smsdrawer.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class DrawerModel implements Parcelable {
    private String name;
    private ArrayList<String> keywords = new ArrayList<>();
    private ArrayList<String> numbers = new ArrayList<>();
    private int count = 0;

    static final int DEFAULT_DRAWER_TYPE = 0;
    public static final int ALL_DRAWER_TYPE = 1;
    public static final int ADD_DRAWER_TYPE = 2;

    private int spec = DEFAULT_DRAWER_TYPE;

    public DrawerModel(String name) {
        this.name = name;
    }

    protected DrawerModel(Parcel in) {
        name = in.readString();
        keywords = in.createStringArrayList();
        numbers = in.createStringArrayList();
        count = in.readInt();
        spec = in.readInt();
    }

    public static final Creator<DrawerModel> CREATOR = new Creator<DrawerModel>() {
        @Override
        public DrawerModel createFromParcel(Parcel in) {
            return new DrawerModel(in);
        }

        @Override
        public DrawerModel[] newArray(int size) {
            return new DrawerModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setKeywords(ArrayList<String> keywords) {
        this.keywords = keywords;
    }

    public void setNumbers(ArrayList<String> numbers) {
        this.numbers = numbers;
    }

    public void setSpec(int spec) {
        this.spec = spec;
    }

    public int getSpec() {
        return spec;
    }

    public ArrayList<String> getKeywords() {
        return keywords;
    }

    public ArrayList<String> getNumbers() {
        return numbers;
    }

    public void addKeyword(String keyword) {
        this.keywords.add(keyword);
    }

    public void addNumber(String number) {
        this.numbers.add(number);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeStringList(keywords);
        parcel.writeStringList(numbers);
        parcel.writeInt(count);
        parcel.writeInt(spec);
    }
}
