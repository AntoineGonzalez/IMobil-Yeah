package com.example.myapplication.database;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestionnaire de base de donnée qui permet de gérer les actions sur la table Posséder.
 * Cette table créer les lien entre caractéristique et propriete.
 */
public class PossederDAO extends BaseDAO{

    public PossederDAO(Context context) {
        super(context);
    }

    /**
     * ajoute un lien à la table
     * @param idP
     * @param idC
     * @return
     */
    public boolean ajouter(String idP ,int idC) {
        if(fetchPosseder(idP,idC)==null){
            this.open();
            String rqt = "INSERT INTO " + myManager.TABLE_POSSEDER + " ("
                    + myManager.POSSEDER_PROPRIETE + " , "
                    + myManager.POSSEDER_CARACTERISTIQUE + ") VALUES (\""
                    + idP + "\"," + idC + ");";
            myDatabase.execSQL(rqt);
            return true;
        }else{
            return false;
        }
    }

    /**
     * récupére un lien de la table en fonction de l'id de la propriete et l'id de la caracteristique
     * @param idP
     * @param idC
     * @return
     */
    public String fetchPosseder(String idP, int idC) {
        this.open();
        String res=null;
        String rqt = "SELECT * FROM " + myManager.TABLE_POSSEDER+ " WHERE " + myManager.POSSEDER_CARACTERISTIQUE
                + " = ? AND " + myManager.POSSEDER_PROPRIETE + " LIKE ? ;" ;
        Cursor curseur = myDatabase.rawQuery(rqt, new String[]{String.valueOf(idC),idP});
        if(curseur.moveToNext()){
            res= curseur.getString(0)+" "+curseur.getInt(1);
        }
        curseur.close();
        return res;
    }

    /**
     * recupere la liste des caracteristique associées à la propriété passé en parametre (recupére la liste des id des caractéristiques)
     * @param idP
     * @return
     */
    public List<Integer> fetchID(String idP){
        this.open();
        List<Integer> res = new ArrayList<>();
        String rqt = "SELECT "+ myManager.POSSEDER_CARACTERISTIQUE +" FROM " + myManager.TABLE_POSSEDER+ " WHERE " + myManager.POSSEDER_PROPRIETE
                + " LIKE ? ;" ;
        Cursor curseur = myDatabase.rawQuery(rqt, new String[] {idP});
        while(curseur.moveToNext()){
            res.add(curseur.getInt(0));
        }
        curseur.close();
        return res;
    }

    /**
     * supprime un lien de la table
     * @param idP
     * @param idC
     */
    public void supprimer(String idP ,int idC){
        myDatabase.delete(myManager.TABLE_POSSEDER, myManager.POSSEDER_PROPRIETE + " LIKE ? AND "+ myManager.POSSEDER_CARACTERISTIQUE + "= ?" ,
                new String[]{idP, String.valueOf(idC)});
    }
}