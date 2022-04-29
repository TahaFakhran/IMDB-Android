package com.example.imdb;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

// class to display favorite movies
public class FavoriteFragment extends Fragment {

    ListView listView;
    ArrayList<FavoriteMovie> arrayList;
    FavoriteMovieAdapter adapter;
    Button btnClearAll;
    TextView txtError;

    public FavoriteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        txtError = (TextView) view.findViewById(R.id.txtWhoopsMessage);
        listView = (ListView) view.findViewById(R.id.listFavorites);
        arrayList = new ArrayList<FavoriteMovie>();
        btnClearAll = (Button) view.findViewById(R.id.btnClearAllBookmarks);
        btnClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAllFromDB();
            }
        });
        // checks if there is any data to display when favorite is clicked
        readFromDB();

    }

    // function that read all rows from the table
    private void readFromDB() {
        SQLiteDatabase database = new SampleSQLiteDBHelper(this.getContext().getApplicationContext()).getReadableDatabase();

        Cursor c = database.rawQuery("SELECT * FROM " + SampleSQLiteDBHelper.FAVORITE_TABLE_NAME + " ;", null);

        if (c.getCount() <= 0) {
            txtError.setVisibility(View.VISIBLE);
            btnClearAll.setVisibility(View.INVISIBLE);
        } else {
            btnClearAll.setVisibility(View.VISIBLE);
            txtError.setVisibility(View.INVISIBLE);
        }

        // insert the selected rows inside an arrayList
        while (c.moveToNext()) {
            FavoriteMovie fm = new FavoriteMovie();
            fm.setImdb_id(c.getString(1));
            fm.setTitle(c.getString(2));
            fm.setImage(c.getString(3));
            arrayList.add(fm);
        }

        // display all data selected inside a listView
        adapter = new FavoriteMovieAdapter(getActivity(), arrayList);
        listView.setAdapter(adapter);
        Log.d("TAG", "The total cursor count is " + c.getCount());

        // on favorite clicked fo to details page
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FavoriteMovie selectedValue = (FavoriteMovie) parent.getAdapter().getItem(position);
                Intent i = new Intent(getActivity().getApplicationContext(), MovieDetails.class);
                i.putExtra("Imdb_id", selectedValue.getImdb_id());
                i.putExtra("image_url", selectedValue.getImage());
                startActivity(i);
            }
        });
    }

    // clears all data from the table
    private void deleteAllFromDB() {
        SQLiteDatabase database = new SampleSQLiteDBHelper(this.getContext()).getReadableDatabase();

        Cursor c = database.rawQuery("DELETE FROM " + SampleSQLiteDBHelper.FAVORITE_TABLE_NAME + " ;", null);

        c.moveToNext();

        reloadFragment();

        c.close();

    }

    // refreshes the page
    private void reloadFragment() {
        FavoriteFragment fg = new FavoriteFragment();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.body_container, fg).commit();
    }

}