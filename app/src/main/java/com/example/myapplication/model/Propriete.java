package com.example.myapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe Représentant une Annonce dans notre model de donnée.
 */
public class Propriete implements Parcelable,Comparable<Propriete>{
    private  String id;
    private String titre;
    private String description;
    private int nbPieces;
    private List<String> caracteristiques;
    private int prix;
    private String ville;
    private String codePostal;
    private Vendeur vendeur;
    private List<String> images;
    private long date;
    private String note;

    public Propriete(String id, String titre, String description, int nbPieces, int prix, String ville, String code_postal, Vendeur vendeur, long date) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.nbPieces = nbPieces;
        this.caracteristiques= new ArrayList<>();
        this.prix = prix;
        this.ville = ville;
        this.codePostal = code_postal;
        this.vendeur = vendeur;
        this.images = new ArrayList<>();
        this.date = date;
        this.note="";
    }

    protected Propriete(Parcel in) {
        id = in.readString();
        titre = in.readString();
        description = in.readString();
        nbPieces = in.readInt();
        caracteristiques = in.createStringArrayList();
        prix = in.readInt();
        ville = in.readString();
        codePostal = in.readString();
        vendeur=(Vendeur) in.readParcelable(Vendeur.class.getClassLoader());
        images = in.createStringArrayList();
        date = in.readLong();
        note = in.readString();
    }

    public static final Creator<Propriete> CREATOR = new Creator<Propriete>() {
        @Override
        public Propriete createFromParcel(Parcel in) {
            return new Propriete(in);
        }

        @Override
        public Propriete[] newArray(int size) {
            return new Propriete[size];
        }
    };

    public String getNote(){ return this.note; }

    public void setNote(String s){ this.note=s; }

    public void addCaracteristic(String c){
        this.caracteristiques.add(c);
    }

    public void addImage(String i){
        this.images.add(i);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNb_principal_piece() {
        return nbPieces;
    }

    public void setNb_principal_piece(int nb_principal_piece) {
        this.nbPieces = nb_principal_piece;
    }

    public List<String> getList_caracteristic() {
        return caracteristiques;
    }

    public void setList_caracteristic(ArrayList<String> list_caracteristic) {
        this.caracteristiques = list_caracteristic;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        ville = ville;
    }

    public String getCode_postal() {
        return codePostal;
    }

    public void setCode_postal(String code_postal) {
        this.codePostal = code_postal;
    }

    public Vendeur getVendeur() {
        return vendeur;
    }

    public void setVendeur(Vendeur vendeur) {
        this.vendeur = vendeur;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public long  getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(titre);
        dest.writeString(description);
        dest.writeInt(nbPieces);
        dest.writeStringList(caracteristiques);
        dest.writeInt(prix);
        dest.writeString(ville);
        dest.writeString(codePostal);
        dest.writeParcelable(vendeur,flags);
        dest.writeStringList(images);
        dest.writeLong(date);
        dest.writeString(note);
    }

    @Override
    public int compareTo(Propriete otherPropriete) {
        if(this.date > otherPropriete.date){
          return -1;
        }else if(this.date < otherPropriete.date){
            return 1;
        }else{
            return 0;
        }
    }
}
