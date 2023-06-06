package com.my.memorizeapp.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    private static DBHelper instance;
    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static synchronized DBHelper getInstance(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        if (instance == null) {
            instance = new DBHelper(context.getApplicationContext(), name, factory, version);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE folders (_id INTEGER PRIMARY KEY AUTOINCREMENT, folder TEXT)");
        db.execSQL("CREATE TABLE notes (_id INTEGER PRIMARY KEY AUTOINCREMENT, folder_id INTEGER, question TEXT, answer TEXT, FOREIGN KEY(folder_id) REFERENCES folders(_id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS notes");
        db.execSQL("DROP TABLE IF EXISTS folders");
        onCreate(db);
    }
}
