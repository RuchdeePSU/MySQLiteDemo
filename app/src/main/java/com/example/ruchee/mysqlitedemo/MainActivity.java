package com.example.ruchee.mysqlitedemo;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ActionMode.Callback {
//    ArrayList<String> movie_list;
    MyDBHandler dbHandler;
    ArrayList<Integer>  listAllMovies_ID;
    ArrayList<String> listAllMovies_Name;
    ArrayAdapter movieAdapter;
    ListView lv_movie;
    ActionMode mActionMode;
    int sel_movie_pos = -1;
    int sel_movie_id;
    Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listAllMovies_ID = new ArrayList<Integer>();
        listAllMovies_Name = new ArrayList<String>();

        dbHandler = new MyDBHandler(MainActivity.this, null, null, 1);
//        listAllMovies = dbHandler.getAllMovies();
        Cursor allMovies = dbHandler.getAllMovies();
        Log.d("Binding ListView", "Column Name = " + allMovies.getColumnName(0));
        allMovies.moveToFirst();
        while(allMovies.isAfterLast() == false){
            listAllMovies_ID.add(allMovies.getInt(0));
            listAllMovies_Name.add(allMovies.getString(allMovies.getColumnIndex(dbHandler.COLUMN_MOVIE_NAME)));
            allMovies.moveToNext();
        }
        allMovies.close();

        lv_movie = (ListView) findViewById(R.id.lvMovie);
        movieAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listAllMovies_Name);
        lv_movie.setAdapter(movieAdapter);

        lv_movie.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mActionMode != null) {
                    return;
                }
                sel_movie_pos = position;
                sel_movie_id = listAllMovies_ID.get(sel_movie_pos);

                // start contextual action bar
                mActionMode = startActionMode((ActionMode.Callback) ctx);
                lv_movie.setSelected(true);
                lv_movie.setSelector(R.color.highlight);

//                Toast.makeText(ctx, "ID = " + sel_movie_id, Toast.LENGTH_SHORT).show();
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_movie);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                final View custom_add_dialog = getLayoutInflater().inflate(R.layout.add_movie_dialog, null);
                builder.setView(custom_add_dialog);

                final CoordinatorLayout colayout = (CoordinatorLayout) findViewById(R.id.coordinatorlayout);

                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // add new item function
                        EditText mname = (EditText) custom_add_dialog.findViewById(R.id.movie_name);
                        EditText myear = (EditText) custom_add_dialog.findViewById(R.id.release_year);

                        // input validation here - movie name can't be empty

                        // insert new movie
                        Movie new_movie =
                                new Movie(mname.getText().toString(), myear.getText().toString());
                        dbHandler.addNewMovie(new_movie);

                        // add new movie into arraylists
                        listAllMovies_ID.add(dbHandler.getLastID());
                        listAllMovies_Name.add(mname.getText().toString());

                        // update arrayAdapter
                        movieAdapter.notifyDataSetChanged();

                        // show snackbar here
                        Snackbar snackbar = Snackbar.make(colayout,
                                mname.getText().toString() + " is added.", Snackbar.LENGTH_LONG);
                        snackbar.setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // undo add new movie

                            }
                        });
                        snackbar.show();

                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog mAlertDialog = builder.create();
                mAlertDialog.show();

            }
        });
    }

//    public void AddMovieToListView() {
//        movie_list = new ArrayList<String>();
//        movie_list.add("Captain America: Civil War");
//        movie_list.add("Deadpool");
//        movie_list.add("The Lord of The Ring: The Two Towers");
//    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        // inflate main menu
        MenuInflater cMenuInflater = getMenuInflater();
        cMenuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        boolean success = true;
        switch(item.getItemId()) {
            case R.id.action_edit:
                AlertDialog.Builder edit_builder = new AlertDialog.Builder(MainActivity.this);
                View custom_edit_dialog = getLayoutInflater().inflate(R.layout.edit_movie_dialog, null);
                edit_builder.setView(custom_edit_dialog);

                final EditText mname = (EditText) custom_edit_dialog.findViewById(R.id.movie_name);
                final EditText myear = (EditText) custom_edit_dialog.findViewById(R.id.release_year);

                // search record by column: movie_id
                Cursor searchResult = dbHandler.getMoviebyID(sel_movie_id);
                searchResult.moveToFirst();

                mname.setText(searchResult.getString(searchResult.getColumnIndex(dbHandler.COLUMN_MOVIE_NAME)));
                myear.setText(searchResult.getString(searchResult.getColumnIndex(dbHandler.COLUMN_RELEASE_YEAR)));

                edit_builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // edit selected item
                        Movie update_movie =
                                new Movie(sel_movie_id, mname.getText().toString(), myear.getText().toString());
                        dbHandler.updateMovie(update_movie);

                        // update movie in arraylist
                        listAllMovies_Name.set(sel_movie_pos, mname.getText().toString());

                        // update arrayAdapter
                        movieAdapter.notifyDataSetChanged();

                        dialog.dismiss();
                    }
                });
                edit_builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog edit_alertDialog = edit_builder.create();
                edit_alertDialog.show();

                mode.finish();
                break;

            case R.id.action_delete:
                AlertDialog.Builder del_builder = new AlertDialog.Builder(MainActivity.this);
                del_builder.setMessage("Are you sure to delete " + movieAdapter.getItem(sel_movie_pos) + "?");
                del_builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // delete selected item function(movie_list_pos)

                    }
                });
                del_builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog del_alertDialog = del_builder.create();
                del_alertDialog.show();

                mode.finish();
                break;

            default:
                success = false;
        }
        return success;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        lv_movie.setSelector(R.color.normal);
        mActionMode = null;
    }


}
