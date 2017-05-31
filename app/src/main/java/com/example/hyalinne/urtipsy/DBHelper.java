package com.example.hyalinne.urtipsy;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
         // TODO Auto-generated method stub
        db.execSQL("CREATE TABLE record(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "date TEXT, " + "alcohol int);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXIST today;");
        onCreate(db);
    }
}