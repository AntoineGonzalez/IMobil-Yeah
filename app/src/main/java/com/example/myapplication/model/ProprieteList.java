package com.example.myapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Classe Représentant une liste d'annonces dans notre model de donnée.
 */
public class ProprieteList extends ArrayList implements Parcelable {
    public ProprieteList(){

    }

    public ProprieteList(Parcel in) {
        this.getFromParcel(in);
    }

    @SuppressWarnings("rawtypes")
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator()
    {

        @Override
        public ProprieteList createFromParcel(Parcel in) {
            return new ProprieteList(in);
        }

        @Override
        public Object[] newArray(int size) {
            return null;
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        int size = this.size();
        dest.writeInt(size);
        for(int i=0; i < size; i++)
        {
            Propriete p = (Propriete) this.get(i);
            dest.writeParcelable(p,flags);
        }
    }

    private void getFromParcel(Parcel in) {
        this.clear();
        int size = in.readInt();
        for(int i = 0; i < size; i++)
        {
            Propriete p=(Propriete) in.readParcelable(Propriete.class.getClassLoader());
            this.add(p);
        }
    }
}
