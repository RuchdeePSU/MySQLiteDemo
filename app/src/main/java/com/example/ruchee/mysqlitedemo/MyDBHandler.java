package com.example.ruchee.mysqlitedemo;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by ruchee on 11/6/2016 AD.
 */

public class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "movieDB.db";
    public static final String TABLE_MOVIE = "movies";

    public static final String COLUMN_MID = "movie_id";
    public static final String COLUMN_MOVIE_NAME = "movie_name";
    public static final String COLUMN_RELEASE_YEAR = "release_year";


    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_movie_table = "CREATE TABLE " + TABLE_MOVIE + "(" +
                COLUMN_MID + " INTEGER PRIMARY KEY," +
                COLUMN_MOVIE_NAME + " TEXT," +
                COLUMN_RELEASE_YEAR + " TEXT" +
                ")";
        db.execSQL(create_movie_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIE);
        onCreate(db);
    }

    public void addNewMovie(Movie movie) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_MOVIE_NAME, movie.get_movie_name());
        contentValues.put(COLUMN_RELEASE_YEAR, movie.get_release_year());
        db.insert(TABLE_MOVIE, null, contentValues);
        db.close();
    }

    public boolean updateMovie(Movie movie) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_MOVIE_NAME, movie.get_movie_name());
        contentValues.put(COLUMN_RELEASE_YEAR, movie.get_release_year());
        db.update(TABLE_MOVIE, contentValues, COLUMN_MID + " = ? ", new String[] {
                Integer.toString(movie.get_mid())
        });
        db.close();
        return true;
    }

    public boolean deleteMovie(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MOVIE, COLUMN_MID + " = ? ", new String[] {
                Integer.toString(id)
        });
        db.close();
        return true;
    }

    public Cursor getMoviebyID(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String select_one = "SELECT * FROM " + TABLE_MOVIE +
                " WHERE " + COLUMN_MID + " = "+ id;
        Cursor resultSet = db.rawQuery(select_one, null);
        return resultSet;
    }

    public Cursor getAllMovies() {
        SQLiteDatabase db = this.getReadableDatabase();
        String select_all = "SELECT * FROM " + TABLE_MOVIE;
        Cursor resultSet =  db.rawQuery(select_all, null);
        return resultSet;
    }

    // get movie_id of last record
    public int getLastID() {
        int _id = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String select_last = "SELECT * FROM " + TABLE_MOVIE +
                " ORDER BY " + COLUMN_MID + " DESC LIMIT 1";
        Cursor resultSet = db.rawQuery(select_last, null);
        if (resultSet.moveToLast()) {
            _id = resultSet.getInt(0);
        }
        resultSet.close();
        db.close();
        return _id;
    }

//    public ArrayList<String> getAllMovies() {
//        ArrayList<String> movie_list = new ArrayList<String>();
//
//        SQLiteDatabase db = this.getReadableDatabase();
//        String select_all = "SELECT * FROM " + TABLE_MOVIE;
//        Cursor resultSet =  db.rawQuery(select_all, null);
//        resultSet.moveToFirst();
//
//        while(resultSet.isAfterLast() == false){
//            movie_list.add(resultSet.getString(resultSet.getColumnIndex(COLUMN_MOVIE_NAME)));
////            movie_list.add(resultSet.getString(2));
//            resultSet.moveToNext();
//        }
//        resultSet.close();
//        db.close();
//
//        return movie_list;
//    }

}
