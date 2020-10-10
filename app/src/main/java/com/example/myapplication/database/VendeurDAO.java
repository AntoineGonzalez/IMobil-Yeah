package com.example.myapplication.database;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.example.myapplication.model.Vendeur;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestionnaire de base de donnée qui permet de gérer les actions sur laa table Vendeur.
 */
public class VendeurDAO extends BaseDAO {


    public VendeurDAO(Context context) {
        super(context);
    }

    /**
     * Ajout d'un vendeur en bdd
     * @param v
     * @return
     */
    public boolean ajouter(Vendeur v) {
        if(fetchVendeur(v.getId()) == null) {
            myDatabase = myManager.getWritableDatabase();
            String rqt = "INSERT INTO " + myManager.TABLE_VENDEUR + "("
                    + myManager.VENDEUR_ID  + ", "
                    + myManager.VENDEUR_NOM + ", "
                    + myManager.VENDEUR_PRENOM + ", "
                    + myManager.VENDEUR_EMAIL + ", "
                    + myManager.VENDEUR_TELEPHONE + ")" +
                    " VALUES(\""
                    + v.getId() + "\",\"" + v.getNom() + "\",\"" + v.getPrenom() + "\",\""
                    + v.getMail() + "\",\"" + v.getTelephone()+ "\");";
            myDatabase.execSQL(rqt);
            return true;
        }else{
            return false;
        }
    }

    /**
     * recupére un vendeur à partir de son id
     * @param id_v
     * @return
     */
    public Vendeur fetchVendeur(String id_v){
        myDatabase = myManager.getReadableDatabase();
        Vendeur v  = null;
        String rqt = "SELECT * FROM " + myManager.TABLE_VENDEUR + " WHERE " + myManager.VENDEUR_ID +
                " LIKE ? ;";
        Cursor curseur = myDatabase.rawQuery(rqt, new String[] {id_v});
        curseur.moveToNext();
        if(!curseur.isAfterLast()) {
            String id = curseur.getString(0);
            String nom = curseur.getString(1);
            String prenom = curseur.getString(2);
            String mail = curseur.getString(3);
            String telephone = curseur.getString(4);
            v = new Vendeur(id, nom, prenom, mail, telephone);
        }
        curseur.close();
        return v;
    }
}
