package com.example.myapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Classe Représentant un Vendeur dans notre model de donnée.
 */
public class Vendeur implements Parcelable {
    private String id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;

    public Vendeur(String id, String nom, String prenom, String mail, String telephone) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = mail;
        this.telephone = telephone;
    }

    protected Vendeur(Parcel in) {
        id = in.readString();
        nom = in.readString();
        prenom = in.readString();
        email = in.readString();
        telephone = in.readString();
    }

    public static final Creator<Vendeur> CREATOR = new Creator<Vendeur>() {
        @Override
        public Vendeur createFromParcel(Parcel in) {
            return new Vendeur(in);
        }

        @Override
        public Vendeur[] newArray(int size) {
            return new Vendeur[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getMail() {
        return email;
    }

    public void setMail(String mail) {
        this.email = mail;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(nom);
        dest.writeString(prenom);
        dest.writeString(email);
        dest.writeString(telephone);
    }
}
