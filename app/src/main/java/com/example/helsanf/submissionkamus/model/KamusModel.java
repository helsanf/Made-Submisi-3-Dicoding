package com.example.helsanf.submissionkamus.model;

import android.os.Parcel;
import android.os.Parcelable;

public class KamusModel implements Parcelable {
    private int id;
    private String kosakata;
    private String deskripsi;
    private String kategori;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKosakata() {
        return kosakata;
    }

    public void setKosakata(String kosakata) {
        this.kosakata = kosakata;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public KamusModel() {

    }

    public KamusModel(String kosakata, String deskripsi) {
        this.kosakata = kosakata;
        this.deskripsi = deskripsi;
    }

    protected KamusModel(Parcel in) {
        id = in.readInt();
        kosakata = in.readString();
        deskripsi = in.readString();
        kategori = in.readString();
    }

    public static final Creator<KamusModel> CREATOR = new Creator<KamusModel>() {
        @Override
        public KamusModel createFromParcel(Parcel in) {
            return new KamusModel(in);
        }

        @Override
        public KamusModel[] newArray(int size) {
            return new KamusModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(kosakata);
        parcel.writeString(deskripsi);
        parcel.writeString(kategori);
    }
}
