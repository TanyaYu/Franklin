package com.tanyayuferova.franklin.entity;

import android.content.Context;
import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;

import com.tanyayuferova.franklin.R;

import java.util.Arrays;

/**
 * Virtue entity
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

    /**
     * Creates new Virtue object using id
     * @param context is needed to find name and description values in resources
     * @param id
     * @return
     */
    public static Virtue newVirtueById(Context context, int id) {
        Resources res  = context.getResources();

        int[] ids = res.getIntArray(R.array.virtues_ids);
        String[] names = res.getStringArray(R.array.virtues_names);
        String[] shortNames = res.getStringArray(R.array.virtues_short_names);
        String[] descriptions = res.getStringArray(R.array.virtues_descriptions);

        //Finding index of element in int array. Probably needs refactoring.
        for(int index = 0; index < ids.length; index++) {
            if(ids[index] == id)
                return new Virtue(id, names[index], shortNames[index], descriptions[index]);
        }
        throw new UnsupportedOperationException("Invalid id = " + id);
    }
}
