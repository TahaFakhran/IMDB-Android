package com.example.imdb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SampleSQLiteDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "IMDB_database";
    public static final String FAVORITE_TABLE_NAME = "favorite";
    public static final String FAVORITE_COLUMN_ID = "_id";
    public static final String FAVORITE_COLUMN_IMDB_ID = "imdb_id";
    public static final String FAVORITE_COLUMN_TITLE = "title";
    public static final String FAVORITE_COLUMN_IMAGE = "image_url";

    public SampleSQLiteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + FAVORITE_TABLE_NAME + " (" +
                FAVORITE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FAVORITE_COLUMN_IMDB_ID + " TEXT, " +
                FAVORITE_COLUMN_TITLE + " TEXT, " +
                FAVORITE_COLUMN_IMAGE + " TEXT" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FAVORITE_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
