package com.example.myapplication.database;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class RepresenterDAO extends BaseDAO{

    /**
     * Gestionnaire de base de donnée qui permet de gérer les actions sur la table Represente.
     * Cette table créer les lien entre image et propriete.
     */
    public RepresenterDAO(Context context) {
        super(context);
    }

    /**
     * ajoute un lien à la table
     * @param idP
     * @param idI
     * @return
     */
    public boolean ajouter(String idP ,int idI) {
        if(fetchRepresenter(idP,idI)==null){
            this.open();
            String rqt = "INSERT INTO " + myManager.TABLE_REPRESENTER + " ("
                    + myManager.REPRESENTER_PROPRIETE + " , "
                    + myManager.REPRESENTER_IMAGE + ") VALUES (\""
                    + idP + "\"," + idI + ");";
            myDatabase.execSQL(rqt);
            return true;
        }else{
            return false;
        }
    }

    /**
     * récupére un lien de la table en fonction de l'id de la propriete et l'id de l'image
     * @param idP
     * @param idI
     * @return
     */
    public String fetchRepresenter(String idP, int idI) {
        this.open();
        String res=null;
        String rqt = "SELECT * FROM " + myManager.TABLE_REPRESENTER+ " WHERE " + myManager.REPRESENTER_IMAGE
                   + " = ? AND " + myManager.REPRESENTER_PROPRIETE + " LIKE ? ;" ;
        Cursor curseur = myDatabase.rawQuery(rqt, new String[]{String.valueOf(idI),idP});
        if(curseur.moveToNext()){
          res= curseur.getString(0)+" "+curseur.getInt(1);
        }
        curseur.close();
        return res;
    }

    /**
     * recupere la liste des images associées à la propriété passé en parametre (recupére la liste des id des images)
     * @param idP
     * @return
     */
    public List<Integer> fetchID(String idP){
        this.open();
        List<Integer> res = new ArrayList<>();
        String rqt = "SELECT "+ myManager.REPRESENTER_IMAGE +" FROM " + myManager.TABLE_REPRESENTER+ " WHERE " + myManager.REPRESENTER_PROPRIETE
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
     * @param idI
     */
    public void supprimer(String idP ,int idI){
        myDatabase.delete(myManager.TABLE_REPRESENTER, myManager.REPRESENTER_PROPRIETE + " LIKE ? AND "+ myManager.REPRESENTER_IMAGE + "= ?" ,
                new String[]{idP, String.valueOf(idI)});
    }
}
