package com.example.myapplication.database;
import android.content.Context;
import android.database.Cursor;

/**
 * Gestionnaire de base de donnée qui permet de gérer les actions sur la table Image.
 */
public class ImageDAO extends BaseDAO {

    public ImageDAO(Context context) {
        super(context);
    }

    /**
     * ajout d'une image dans la table
     * @param url
     * @return
     */
    public boolean ajouter(String url) {
        this.open();
        if(fetch(url) == -1) {
            String rqt = "INSERT INTO " + myManager.TABLE_IMAGE + " ("
                    + myManager.IMAGES_URL + ") "
                    + " VALUES(\""
                    + url + "\");";
            myDatabase.execSQL(rqt);
            return true;
        }else{
            return false;
        }
    }


    /**
     * recupére en bdd l'image d'url spécifié
     * @param url
     * @return
     */
    public int fetch(String url){
        this.open();
        int res;
        String rqt = "SELECT "+ myManager.IMAGES_ID +" FROM " + myManager.TABLE_IMAGE + " WHERE " + myManager.IMAGES_URL +
                " LIKE ? ;";
        Cursor curseur = myDatabase.rawQuery(rqt, new String[] {url});
        if(!curseur.moveToFirst()) {
            res=-1;
        }else{
            res=curseur.getInt(0);
        }
        curseur.close();
        return res;
    }

    /**
     * recupére en bdd l'image d'id specifié
     * @param id
     * @return
     */
    public String fetchId( int id ){
        this.open();
        String rqt = "SELECT " + myManager.IMAGES_URL + " FROM " + myManager.TABLE_IMAGE + " WHERE " + myManager.IMAGES_ID + " = ? ;";
        Cursor curseur = myDatabase.rawQuery(rqt,new String[] {String.valueOf(id)});
        curseur.moveToFirst();
        String res=curseur.getString(0);
        curseur.close();
        return res;
    }

    /**
     * supprime une image de la table en fonction de son id.
     * @param id
     */
    public void supprimer(int id) {
        myDatabase.delete(myManager.TABLE_IMAGE, myManager.IMAGES_ID + " = ?",
                new String[]{String.valueOf(id)});
    }
}
