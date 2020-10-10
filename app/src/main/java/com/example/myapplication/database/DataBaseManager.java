package com.example.myapplication.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Manager de la base de donnée permet de créer et supprimer les tables ou de mettre à jour la version
 */
public class DataBaseManager extends SQLiteOpenHelper {

    //DEFINITION NOM TABLE:
    public static final String TABLE_PROPRIETE = "Propriete";
    public static final String TABLE_CARACTERISTIQUE = "Categorie";
    public static final String TABLE_IMAGE = "Image";
    public static final String TABLE_VENDEUR = "Vendeur";
    public static final String TABLE_POSSEDER = "Posseder";
    public static final String TABLE_REPRESENTER = "Representer";
    public static final String TABLE_NOTE="Note";

    //DEFINITION ATTRIBUTS PROPRIETE:
    public static final String PROPRIETE_ID = "id_propriete";
    public static final String PROPRIETE_TITRE = "titre";
    public static final String PROPRIETE_DESCRIPTION = "description";
    public static final String PROPRIETE_NB_PIECE = "nbPieces";
    public static final String PROPRIETE_PRIX = "prix";
    public static final String PROPRIETE_VILLE = "ville";
    public static final String PROPRIETE_CODE_POSTAL = "codePostal";
    public static final String PROPRIETE_VENDEUR = "id_vendeur";
    public static final String PROPRIETE_DATE = "date";

    //DEFINITION ATTRIBUTS VENDEUR:
    public static final String VENDEUR_ID = "id_vendeur";
    public static final String VENDEUR_NOM = "nom";
    public static final String VENDEUR_PRENOM = "prenom";
    public static final String VENDEUR_EMAIL= "email";
    public static final String VENDEUR_TELEPHONE = "telephone";

    //DEFINITION ATTRIBUTS CARACTERISTIQUE:
    public static final String CARACTERISTIQUE_ID = "id_caracteristique";
    public static final String CARACTERISTIQUE_CONTENT= "content_caracteristique";

    //DEFINITION ATTRIBUTS IMAGES:
    public static final String IMAGES_ID="id_image";
    public static final String IMAGES_URL="url_image";

    //DEFINITION ATTRIBUTS NOTES:
    public static final String NOTE_ID="id_note";
    public static final String NOTE_CONTENT="id_content";
    public static final String NOTE_PROPRIETE="id_propriete";

    //DEFINITION ATTRIBUTS PRESENTER:
    public static final String POSSEDER_PROPRIETE="id_propriete";
    public static final String POSSEDER_CARACTERISTIQUE="id_caracteristique";

    //DEFINITION ATTRIBUTS REPRESENTER:
    public static final String REPRESENTER_PROPRIETE="id_propriete";
    public static final String REPRESENTER_IMAGE="id_image";

    public DataBaseManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, null, version);
    }


    public String createTablePropriete() {
        String rqt = "CREATE TABLE " + TABLE_PROPRIETE + "("
                + PROPRIETE_ID + " TEXT PRIMARY KEY, "
                + PROPRIETE_TITRE + " TEXT NOT NULL, "
                + PROPRIETE_DESCRIPTION + " TEXT NOT NULL, "
                + PROPRIETE_NB_PIECE + " INTEGER NOT NULL, "
                + PROPRIETE_PRIX + " INTEGER NOT NULL, "
                + PROPRIETE_VILLE + " TEXT NOT NULL, "
                + PROPRIETE_CODE_POSTAL + " TEXT NOT NULL, "
                + PROPRIETE_VENDEUR + " TEXT, "
                + PROPRIETE_DATE + " INTEGER NOT NULL ,"
                + "FOREIGN KEY("+PROPRIETE_VENDEUR+") REFERENCES "+TABLE_VENDEUR+"("+VENDEUR_ID+")"
                + ");";
        return rqt;
    }

    public String createTableVendeur() {
        String rqt = "CREATE TABLE " + TABLE_VENDEUR + "("
                + VENDEUR_ID + " TEXT PRIMARY KEY,"
                + VENDEUR_NOM + " TEXT NOT NULL,"
                + VENDEUR_PRENOM + " TEXT NOT NULL,"
                + VENDEUR_EMAIL + " TEXT NOT NULL,"
                + VENDEUR_TELEPHONE + " TEXT NOT NULL"
                + ");";
        return rqt;
    }

    public String createTableImage() {
        Log.i("database","create image invoked");
        String rqt = "CREATE TABLE " + TABLE_IMAGE + "("
                + IMAGES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + IMAGES_URL + " TEXT NOT NULL"
                + ");";
        return rqt;
    }

    public String createTableCaracteristique() {
        String rqt = "CREATE TABLE " + TABLE_CARACTERISTIQUE + "("
                + CARACTERISTIQUE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CARACTERISTIQUE_CONTENT + " INTEGER NOT NULL"
                + ");";
        return rqt;
    }

    public String createTableNote(){
        String rqt = "CREATE TABLE " + TABLE_NOTE + "("
                + NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NOTE_CONTENT + " TEXT NOT NULL ,"
                + NOTE_PROPRIETE + " TEXT ,"
                + " FOREIGN KEY("+NOTE_PROPRIETE+") REFERENCES "+TABLE_PROPRIETE+"("+PROPRIETE_ID+")"
                + ");";
        return rqt;
    }


    public String createTablePosseder() {
        String rqt = "CREATE TABLE " + TABLE_POSSEDER + "("
                + POSSEDER_CARACTERISTIQUE + " INTEGER,"
                + POSSEDER_PROPRIETE + " INTEGER,"
                + "PRIMARY KEY("+POSSEDER_PROPRIETE+","+POSSEDER_CARACTERISTIQUE
                + "));";
        return rqt;
    }

    public String createTableRepresenter() {
        String rqt = "CREATE TABLE " + TABLE_REPRESENTER + "("
                + REPRESENTER_PROPRIETE + " INTEGER,"
                + REPRESENTER_IMAGE + " INTEGER,"
                + "PRIMARY KEY("+REPRESENTER_PROPRIETE+","+REPRESENTER_IMAGE
                + "));";
        return rqt;
    }


    public String dropTablePropriete() {
        String rqt = "DROP TABLE IF EXISTS"+TABLE_PROPRIETE +";";
        return rqt;
    }

    public String dropTableVendeur() {
        String rqt = "DROP TABLE IF EXISTS "+TABLE_VENDEUR +";";
        return rqt;
    }

    public String dropTableImage() {
        String rqt = "DROP TABLE IF EXISTS "+TABLE_IMAGE +";";
        return rqt;
    }

    public String dropTableCaracteristique() {
        String rqt = "DROP TABLE IF EXISTS "+TABLE_CARACTERISTIQUE +";";
        return rqt;
    }

    public String dropTablePosseder() {
        String rqt = "DROP TABLE IF EXISTS "+TABLE_POSSEDER +";";
        return rqt;
    }

    public String dropTableRepresenter() {
        String rqt = "DROP TABLE IF EXISTS "+TABLE_REPRESENTER +";";
        return rqt;
    }

    public String dropTableNote() {
        String rqt = "DROP TABLE IF EXISTS "+TABLE_NOTE +";";
        return rqt;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("database ", "sa grosse race");
        db.execSQL(createTablePropriete());
        db.execSQL(createTableVendeur());
        db.execSQL(createTableImage());
        db.execSQL(createTableNote());
        db.execSQL(createTableCaracteristique());
        db.execSQL(createTablePosseder());
        db.execSQL(createTableRepresenter());
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(dropTablePropriete());
        db.execSQL(dropTableVendeur());
        db.execSQL(dropTableImage());
        db.execSQL(dropTableCaracteristique());
        db.execSQL(dropTablePosseder());
        db.execSQL(dropTableRepresenter());
        db.execSQL(dropTableNote());
        onCreate(db);
    }
}
