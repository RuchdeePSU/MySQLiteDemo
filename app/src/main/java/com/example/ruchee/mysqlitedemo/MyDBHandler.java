package com.example.ruchee.mysqlitedemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by ruchee on 11/6/2016 AD.
 */

public class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "movieDB.db";
    public static final String TABLE_MOVIES = "movies";

    public static final String COLUMN_MID = "movie_id";
    public static final String COLUMN_MOVIE_NAME = "movie_name";
    public static final String COLUMN_RELEASE_YEAR = "release_year";


    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_movie_table = "CREATE TABLE " + TABLE_MOVIES + "(" +
                COLUMN_MID + " INTEGER PRIMARY KEY," +
                COLUMN_MOVIE_NAME + " TEXT," +
                COLUMN_RELEASE_YEAR + " TEXT" +
                ")";
        db.execSQL(create_movie_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES);
        onCreate(db);
    }

    public void addNewMovie(Movie movie) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_MOVIE_NAME, movie.get_movie_name());
        contentValues.put(COLUMN_RELEASE_YEAR, movie.get_release_year());
        db.insert(TABLE_MOVIES, null, contentValues);
    }

    public ArrayList<String> getAllMovies() {
        ArrayList<String> movie_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        String select_all = "SELECT * FROM " + TABLE_MOVIES;
        Cursor resultSet =  db.rawQuery(select_all, null);
        resultSet.moveToFirst();

        while(resultSet.isAfterLast() == false){
            movie_list.add(resultSet.getString(resultSet.getColumnIndex(COLUMN_MOVIE_NAME)));
//            movie_list.add(resultSet.getString(2));
            resultSet.moveToNext();
        }
        return movie_list;
    }

}
