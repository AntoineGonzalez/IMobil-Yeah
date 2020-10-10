package com.example.myapplication.database;

import android.content.Context;
import android.database.Cursor;
import com.example.myapplication.model.Propriete;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestionnaire de base de donnée qui permet de gérer les actions sur la table Note.
 */
public class NoteDAO extends BaseDAO {

    public NoteDAO(Context context) {
        super(context);
    }

    /**
     * ajoute une note en bdd
     * @param content
     * @param id_p
     */
    public void ajouterNote(String content, String id_p){
        this.open();
        String rqt = "INSERT INTO " + myManager.TABLE_NOTE + "("
                + myManager.NOTE_CONTENT  + ", "
                + myManager.NOTE_PROPRIETE
                + ")" + " VALUES(\""
                + content + "\",\"" + id_p + "\");";
        myDatabase.execSQL(rqt);
    }

    /**
     * liste toutes les notes associées à la propriété passer en parametre.
     * @param p
     * @return
     */
    public List<String> fetchAll(Propriete p){
        this.open();
        List<String> notelist = new ArrayList<>();
        String rqt= "SELECT "+myManager.NOTE_CONTENT+" FROM " + myManager.TABLE_NOTE + " WHERE "+myManager.NOTE_PROPRIETE+" LIKE ? ;";
        Cursor curseur = myDatabase.rawQuery(rqt, new String[]{ p.getId() });
        while(curseur.moveToNext()){
            String content = curseur.getString(0);
            notelist.add(content);
        }
        return notelist;
    }

    /**
     * supprime les notes assccier à la propriété specifié en parametre.
     * @param id_p
     */
    public void supprimerNote(String id_p){
        this.open();
        myDatabase.delete(myManager.TABLE_NOTE, myManager.NOTE_PROPRIETE + " LIKE ?",
                new String[]{id_p});
    }

    /**
     * supprime une note en fonction de son contenue et de la propriete auquelle elle appartient.
     * @param p
     * @param content
     */
    public void supprimerNote(Propriete p, String content){
        this.open();
        myDatabase.delete(myManager.TABLE_NOTE, myManager.NOTE_CONTENT+ " LIKE ? AND "+myManager.NOTE_PROPRIETE +" LIKE ?",
                new String[]{content,p.getId()});
    }
}
