package com.example.tanyayuferova.franklin.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tanya Yuferova on 10/5/2017.
 */

public class Virtue implements Parcelable {
    private int id;
    private String name;
    private String shortName;
    private String description;

    public Virtue(int id, String name, String shortName, String description) {
        this.id = id;
        this.name = name;
        this.shortName = shortName;
        this.description = description;
    }

    public Virtue(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Virtue)
            return ((Virtue)obj).getId() == this.getId();
        return super.equals(obj);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(id);

        String[] array = new String[3];
        array[0] = name;
        array[1] = shortName;
        array[2] = description;
        parcel.writeStringArray(array);
    }
    public static final Parcelable.Creator<Virtue> CREATOR = new Parcelable.Creator<Virtue>() {
        public Virtue createFromParcel(Parcel in) {
            return new Virtue(in);
        }

        public Virtue[] newArray(int size) {
            return new Virtue[size];
        }
    };

    private Virtue(Parcel parcel) {
        id = parcel.readInt();

        String[] array = new String[3];
        parcel.readStringArray(array);
        name = array[0];
        shortName = array[1];
        description = array[2];
    }
}
