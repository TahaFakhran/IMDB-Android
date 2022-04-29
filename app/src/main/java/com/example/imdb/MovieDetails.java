package com.example.imdb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// class to display all movie details
public class MovieDetails extends AppCompatActivity {

    String selected_id;
    TextView txtTitle;
    ImageView imgMovie;
    String image_url;
    Boolean bookmarked = false;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        txtTitle = (TextView) findViewById(R.id.movieDetailsTitle);
        imgMovie = (ImageView) findViewById(R.id.movieDetailsImage);
        fab = findViewById(R.id.floating_action_button_details);

        // on favorite button press read from db if it exists it will delete else it will save
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readFromDB();
                if (bookmarked) {
                    deleteFromDB();
                } else {
                    saveToDB();
                }
            }
        });

        // get parameters from previous fragment/activity
        selected_id = getIntent().getStringExtra("Imdb_id");
        image_url = getIntent().getStringExtra("image_url");

        readFromDB();

        // api request with the movie id to get all of its details
        String url = "https://data-imdb1.p.rapidapi.com/movie/id/" + selected_id + "/";
        String token = "4920bbf220mshb82d577d1fb1c4ap1f2115jsn30db28914797";

        CustomStringRequest postRequest = new CustomStringRequest(Request.Method.GET, url, token, onSuccess, onFailure);

        try {
            SimpleRequestQueueFactory factory = SimpleRequestQueueFactory.getInstance(this.getApplicationContext());
            RequestQueue queue = factory.getQueueInstance();
            queue.add(postRequest);

        } catch (Exception e) {
            Log.e("Volley", e.getMessage());
        }


    }

    // if api succeed  display all details
    Response.Listener<String> onSuccess = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            JSONObject jsonObject = null;
            JSONObject result = null;
            JSONArray resultsKeywords = null;
            String keywordText = "";
            ImageView movieDetailsImage = findViewById(R.id.movieDetailsImage);
            TextView movieDetailsTitle = findViewById(R.id.movieDetailsTitle);
            TextView movieDetailsRelease = findViewById(R.id.movieDetailsRelease);
            TextView movieDetailsRating = findViewById(R.id.movieDetailsRating);
            TextView movieDetailsDescription = findViewById(R.id.movieDetailsDescription);
            TextView movieDetailsTrailer = findViewById(R.id.movieDetailsTrailer);
            TextView movieDetailsKeywords = findViewById(R.id.movieDetailsKeywords);

            try {
                jsonObject = new JSONObject(response);
                result = jsonObject.getJSONObject("results");

                String imageUri = result.getString("image_url");
                Picasso.get().load(imageUri).placeholder(R.drawable.notfound).into(movieDetailsImage);
                movieDetailsTitle.setText(result.getString("title"));
                movieDetailsRelease.setText(result.getString("release"));
                movieDetailsRating.setText(movieDetailsRating.getText() + result.getString("rating"));
                movieDetailsDescription.setText(result.getString("description"));
                String trailerURL = result.getString("trailer");
                movieDetailsTrailer.setText(trailerURL);

                // on url click open web inside the app to play trailer
                movieDetailsTrailer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailerURL));
                        startActivity(browserIntent);
                    }
                });

                // iterate through the keyword Array to get all the keywords and then display them
                resultsKeywords = result.getJSONArray("keywords");
                for (int i = 0; i < resultsKeywords.length(); i++) {
                    JSONObject resultKeywords = resultsKeywords.getJSONObject(i);
                    keywordText += resultKeywords.getString("keyword") + ", ";
                }
                movieDetailsKeywords.setText(keywordText);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d("Volley", response);
        }
    };

    // if api fails
    Response.ErrorListener onFailure = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("Volley", error.getMessage());
            error.printStackTrace();
        }
    };

    // save the id, title, image url and title inside the database
    private void saveToDB() {
        SQLiteDatabase database = new SampleSQLiteDBHelper(this).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SampleSQLiteDBHelper.FAVORITE_COLUMN_IMDB_ID, selected_id);
        values.put(SampleSQLiteDBHelper.FAVORITE_COLUMN_IMAGE, image_url);
        values.put(SampleSQLiteDBHelper.FAVORITE_COLUMN_TITLE, txtTitle.getText().toString());
        long newRowId = database.insert(SampleSQLiteDBHelper.FAVORITE_TABLE_NAME, null, values);
        Toast.makeText(this, "The new Row Id is " + newRowId, Toast.LENGTH_LONG).show();
        fab.setImageResource(R.drawable.ic_baseline_bookmark_remove_24);
        bookmarked = true;
    }

    // remove the movie from the database
    private void deleteFromDB() {
        SQLiteDatabase database = new SampleSQLiteDBHelper(this).getReadableDatabase();

        Cursor c = database.rawQuery("DELETE FROM " + SampleSQLiteDBHelper.FAVORITE_TABLE_NAME + " WHERE imdb_id = '" + selected_id + "' ;", null);
        c.moveToNext();
        fab.setImageResource(R.drawable.ic_baseline_bookmark_24);
        bookmarked = false;
        c.close();
    }

    // search if the movie is bookmarked or not
    private void readFromDB() {
        SQLiteDatabase database = new SampleSQLiteDBHelper(this.getApplicationContext()).getReadableDatabase();

        Cursor c = database.rawQuery("SELECT * FROM " + SampleSQLiteDBHelper.FAVORITE_TABLE_NAME + " WHERE " + SampleSQLiteDBHelper.FAVORITE_COLUMN_IMDB_ID + " = '" + selected_id + "' ;", null);

        if (c.getCount() <= 0) {
            fab.setImageResource(R.drawable.ic_baseline_bookmark_24);
            bookmarked = false;
        } else {
            fab.setImageResource(R.drawable.ic_baseline_bookmark_remove_24);
            bookmarked = true;
        }

        c.close();

    }

}