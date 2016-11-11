package com.example.ruchee.mysqlitedemo;

/**
 * Created by ruchee on 11/6/2016 AD.
 */

public class Movie {
    private int _mid;
    private String _movie_name;
    private String _release_year;

    public Movie(int _mid, String _movie_name, String _release_year) {
        this._mid = _mid;
        this._movie_name = _movie_name;
        this._release_year = _release_year;
    }

    public Movie(String _movie_name, String _release_year) {
        this._movie_name = _movie_name;
        this._release_year = _release_year;
    }

    public int get_mid() {
        return _mid;
    }

    public void set_mid(int _mid) {
        this._mid = _mid;
    }

    public String get_movie_name() {
        return _movie_name;
    }

    public void set_movie_name(String _movie_name) {
        this._movie_name = _movie_name;
    }

    public String get_release_year() {
        return _release_year;
    }

    public void set_release_year(String _release_year) {
        this._release_year = _release_year;
    }
}
