package com.tanyayuferova.franklin.entity;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;

import com.tanyayuferova.franklin.R;

/**
 * Virtue entity
 * Created by Tanya Yuferova on 10/5/2017.
 */

public class Virtue implements Parcelable {
    private int id;
    private String name;
    private String shortName;
    private String description;
    private @DrawableRes int iconRes;

    public Virtue(int id, String name, String shortName, String description, @DrawableRes int iconRes) {
        this.id = id;
        this.name = name;
        this.shortName = shortName;
        this.description = description;
        this.iconRes = iconRes;
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

    public int getIconRes() {
        return iconRes;
    }

    public void setIconRes(int iconRes) {
        this.iconRes = iconRes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(id);
        parcel.writeInt(iconRes);
        parcel.writeString(name);
        parcel.writeString(shortName);
        parcel.writeString(description);
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
        iconRes = parcel.readInt();
        name = parcel.readString();
        shortName = parcel.readString();
        description = parcel.readString();
    }

    /**
     * Creates new Virtue object using id
     * @param context is needed to find name and description values in resources
     * @param id
     * @return
     */
    public static Virtue fromId(Context context, int id) {
        Resources res  = context.getResources();

        int[] ids = res.getIntArray(R.array.virtues_ids);
        String[] names = res.getStringArray(R.array.virtues_names);
        String[] shortNames = res.getStringArray(R.array.virtues_short_names);
        String[] descriptions = res.getStringArray(R.array.virtues_descriptions);

        //todo refactor
        TypedArray tArray = res.obtainTypedArray(R.array.virtues_icons);
        int[] icons = new int[tArray.length()];
        for (int i = 0; i < icons.length; i++) {
            icons[i] = tArray.getResourceId(i, 0);
        }
        tArray.recycle();

        //todo refactor
        //Finding index of element in int array. Probably needs refactoring.
        for(int index = 0; index < ids.length; index++) {
            if(ids[index] == id)
                return new Virtue(
                        id,
                        names[index],
                        shortNames[index],
                        descriptions[index],
                        icons[index]
                );
        }
        throw new UnsupportedOperationException("Invalid id = " + id);
    }

    public final static int[] IDS = new int[]{ 1,2,3,4,5,6,7,8,9,10,11,12,13 };
    private final static int[] NAMES = new int[]{
            R.string.tem_name,
            R.string.sil_name,
            R.string.ord_name,
            R.string.res_name,
            R.string.fru_name,
            R.string.ind_name,
            R.string.sin_name,
            R.string.jus_name,
            R.string.mod_name,
            R.string.cle_name,
            R.string.tra_name,
            R.string.cha_name,
            R.string.hum_name
    };
    private final static int[] SHORT_NAMES = new int[]{  };
    private final static int[] DESCRIPTIONS = new int[]{  };
    private final static int[] ICONS_RES = new int[]{  };

}
