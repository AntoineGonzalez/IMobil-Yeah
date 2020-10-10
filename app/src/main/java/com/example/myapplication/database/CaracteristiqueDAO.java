package com.example.myapplication.database;

import android.content.Context;
import android.database.Cursor;

/**
 * Gestionnaire de base de donnée qui permet de gérer les actions sur la table Caractéristique.
 */
public class CaracteristiqueDAO extends BaseDAO{
    public CaracteristiqueDAO(Context context) {
        super(context);
    }

    /**
     * ajoute une caractéristique à la table
     * @param content
     * @return
     */
    public boolean ajouter(String content) {
        this.open();
        if(fetch(content) == -1) {
            String rqt = "INSERT INTO " + myManager.TABLE_CARACTERISTIQUE + " ("
                    + myManager.CARACTERISTIQUE_CONTENT + ") "
                    + " VALUES(\""
                    + content + "\");";
            myDatabase.execSQL(rqt);
            return true;
        }else{
            return false;
        }
    }


    /**
     * recupére une caractéristique à partir de son contenue/libéllé
     * @param content
     * @return
     */
    public int fetch(String content){
        this.open();
        int res;
        String rqt = "SELECT "+ myManager.CARACTERISTIQUE_ID +" FROM " + myManager.TABLE_CARACTERISTIQUE + " WHERE " + myManager.CARACTERISTIQUE_CONTENT +
                " LIKE ? ;";
        Cursor curseur = myDatabase.rawQuery(rqt, new String[] {content});
        if(!curseur.moveToFirst()) {
            res=-1;
        }else{
            res=curseur.getInt(0);
        }
        curseur.close();
        return res;
    }

    /**
     * récupére une caractéristique en fonction de son id.
     * @param id
     * @return
     */
    public String fetchId( int id ){
        this.open();
        String rqt = "SELECT " + myManager.CARACTERISTIQUE_CONTENT + " FROM " + myManager.TABLE_CARACTERISTIQUE + " WHERE " + myManager.CARACTERISTIQUE_ID + " = ? ;";
        Cursor curseur = myDatabase.rawQuery(rqt,new String[] {String.valueOf(id)});
        curseur.moveToFirst();
        String res=curseur.getString(0);
        curseur.close();
        return res;
    }

    /**
     * supprime une caractéristique de la tabke en fonction de son id.
     * @param id
     */
    public void supprimer( int id) {
        myDatabase.delete(myManager.TABLE_CARACTERISTIQUE, myManager.CARACTERISTIQUE_ID + " = ?",
                new String[]{String.valueOf(id)});
    }
}
