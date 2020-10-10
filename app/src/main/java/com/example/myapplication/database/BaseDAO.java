package com.example.myapplication.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Classe abstraite définissant les caractéristique principale d'un gestionnaire de table.
 */
public abstract class BaseDAO {

    protected SQLiteDatabase myDatabase = null;
    protected DataBaseManager myManager = null;

    public static final String DATABASE_NAME = "Immobillier.bd";
    public static final int DATABASE_VERSION = 1;


    public BaseDAO(Context context){
        this.myManager = new DataBaseManager(context , DATABASE_NAME , null , DATABASE_VERSION );
    }

    public SQLiteDatabase open() {
        myDatabase = myManager.getWritableDatabase();
        return myDatabase;
    }

    public void close(){
        myDatabase.close();
    }

    public SQLiteDatabase getMyDatabase(){
        return myDatabase;
    }
}
