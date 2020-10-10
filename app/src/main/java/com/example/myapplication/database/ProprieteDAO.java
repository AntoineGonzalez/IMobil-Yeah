package com.example.myapplication.database;

import android.content.Context;
import android.database.Cursor;
import com.example.myapplication.model.Propriete;
import com.example.myapplication.model.Vendeur;

import java.util.ArrayList;
import java.util.List;

/**
 * gestionnaire de base de donnée qui gére les actions sur la table Propriete.
 */
public class ProprieteDAO extends BaseDAO {

    public ProprieteDAO(Context context) {
        super(context);
    }

    /**
     * Ajoute en bdd la proprité passer en parametre
     * @param p
     */
    public boolean ajouter(Propriete p) {
        if(fetchPropriete(p.getId()) == null) {
            myDatabase = myManager.getWritableDatabase();
            String rqt = "INSERT INTO " + myManager.TABLE_PROPRIETE + "("
                    + myManager.PROPRIETE_ID + ", "
                    + myManager.PROPRIETE_TITRE + ", "
                    + myManager.PROPRIETE_DESCRIPTION + ", "
                    + myManager.PROPRIETE_NB_PIECE + ", "
                    + myManager.PROPRIETE_PRIX + ", "
                    + myManager.PROPRIETE_VILLE + ", "
                    + myManager.PROPRIETE_CODE_POSTAL + ", "
                    + myManager.PROPRIETE_VENDEUR + ", "
                    + myManager.PROPRIETE_DATE + ") VALUES(\""
                    + p.getId() + "\",\"" + p.getTitre() + "\",\"" + p.getDescription() + "\","
                    + p.getNb_principal_piece() + "," + p.getPrix() + ",\"" + p.getVille() + "\",\""
                    + p.getCode_postal() + "\",\"" + p.getVendeur().getId() + "\"," + p.getDate() + ");";
            myDatabase.execSQL(rqt);
            return true;
        }else{
            return false;
        }
    }

    /**
     * recupére une propriete en bdd grace à son id
     * @param id_p
     * @return
     */
    public Propriete fetchPropriete(String id_p){
        myDatabase = myManager.getReadableDatabase();
        Propriete p  = null;
        String rqt = "SELECT * FROM " + myManager.TABLE_PROPRIETE + " WHERE " + myManager.PROPRIETE_ID +
                     " LIKE ? ;";
        Cursor curseur = myDatabase.rawQuery(rqt, new String[] {id_p});
        curseur.moveToNext();
        if(!curseur.isAfterLast()) {
            String id = curseur.getString(0);
            String titre = curseur.getString(1);
            String description = curseur.getString(2);
            int nb_piece = curseur.getInt(3);
            int prix = curseur.getInt(4);
            String ville = curseur.getString(5);
            String code_postale = curseur.getString(6);
            String id_vendeur = curseur.getString(7);
            int date = curseur.getInt(8);
            p = new Propriete(id, titre, description, nb_piece, prix, ville, code_postale, new Vendeur(id_vendeur , null ,null,null, null), date);
        }
        curseur.close();
        return p;
    }

    /**
     * recupére toutes les proprites presentent en bdd.
     * @return
     */
    public List<Propriete> fetchAll(){
        myDatabase = myManager.getReadableDatabase();
        Propriete p  = null;
        List<Propriete> proprietelist = new ArrayList<>();
        String rqt= "SELECT * FROM " + myManager.TABLE_PROPRIETE + ";";
        Cursor curseur = myDatabase.rawQuery(rqt,null);
        while(curseur.moveToNext()){
            String id = curseur.getString(0);
            String titre = curseur.getString(1);
            String description = curseur.getString(2);
            int nb_piece = curseur.getInt(3);
            int prix = curseur.getInt(4);
            String ville = curseur.getString(5);
            String code_postale = curseur.getString(6);
            String id_vendeur = curseur.getString(7);
            int date = curseur.getInt(8);
            p = new Propriete(id, titre, description, nb_piece, prix, ville, code_postale, new Vendeur(id_vendeur , null ,null,null, null), date);
            proprietelist.add(p);
        }
        return proprietelist;
    }

    /**
     * supprime une propriete de la bdd grace à son id
     * @param id
     */
    public void supprimer(String id) {
        myDatabase.delete(myManager.TABLE_PROPRIETE, myManager.PROPRIETE_ID + " LIKE ?",
                new String[]{id});
    }
}
