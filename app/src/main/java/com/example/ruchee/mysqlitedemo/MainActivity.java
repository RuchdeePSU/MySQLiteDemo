package com.example.ruchee.mysqlitedemo;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    ArrayList listAllMovies;
    ArrayAdapter movieAdapter;
    ListView lv_movie;
    ActionMode mActionMode;
    int movie_list_pos = -1;
    Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHandler = new MyDBHandler(MainActivity.this, null, null, 1);
        listAllMovies = dbHandler.getAllMovies();
//        AddMovieToListView();

        lv_movie = (ListView) findViewById(R.id.lvMovie);
        movieAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listAllMovies);
        lv_movie.setAdapter(movieAdapter);

        lv_movie.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mActionMode != null) {
                    return;
                }
                movie_list_pos = position;
                // start contextual action bar
                mActionMode = startActionMode((ActionMode.Callback) ctx);
                lv_movie.setSelected(true);
                lv_movie.setSelector(R.color.highlight);
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
//                        MyDBHandler dbHandler = new MyDBHandler(MainActivity.this, null, null, 1);
                        Movie new_movie =
                                new Movie(mname.getText().toString(), myear.getText().toString());
                        dbHandler.addNewMovie(new_movie);

                        //update arraylist
                        listAllMovies.add(mname.getText().toString());

                        // update arrayAdapter
                        movieAdapter.notifyDataSetChanged();

                        // show snackbar here
                        Snackbar snackbar = Snackbar.make(colayout,
                                mname.getText().toString() + " is added.", Snackbar.LENGTH_LONG);
                        snackbar.setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

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

                edit_builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // edit selected item function(movie_list_pos)

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
                del_builder.setMessage("Are you sure to delete " + movieAdapter.getItem(movie_list_pos) + "?");
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
