package com.example.imdb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

// class to display movies by genre
public class DisplayMoviesByGenre extends AppCompatActivity {

    GridView gridView;
    ArrayList<MoviesByGenre> arrayList;
    DisplayMoviesByGenreAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_movies_by_genre);

        gridView = (GridView) findViewById(R.id.gridviewMovieListByGenre);
        arrayList = new ArrayList<MoviesByGenre>();

        // the selected genre from the previous class
        String selected_id = getIntent().getStringExtra("genre");

        // api request to get all movies depending on a certain genre
        String url = "https://data-imdb1.p.rapidapi.com/movie/byGen/" + selected_id + "/?page_size=50";
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

    // if the api request succeed
    Response.Listener<String> onSuccess = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            JSONObject jsonObject = null;
            JSONArray results = null;
            try {
                jsonObject = new JSONObject(response);
                results = jsonObject.getJSONArray("results");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            for (int j = 0; j < results.length(); j++) {
                try {
                    JSONObject result = results.getJSONObject(j);

                    // another api request with each movie id to get all of his details
                    String url_details = "https://data-imdb1.p.rapidapi.com/movie/id/" + result.getString("imdb_id") + "/";
                    String token_details = "4920bbf220mshb82d577d1fb1c4ap1f2115jsn30db28914797";

                    CustomStringRequest postRequest1 = new CustomStringRequest(Request.Method.GET, url_details, token_details, onSuccess_details, onFailure);

                    SimpleRequestQueueFactory factory1 = SimpleRequestQueueFactory.getInstance(getApplicationContext());
                    RequestQueue queue1 = factory1.getQueueInstance();
                    queue1.add(postRequest1);

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            Log.d("Volley", response);

        }
    };
    // if there is an error while calling the api
    Response.ErrorListener onFailure = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("Volley", error.getMessage());
            error.printStackTrace();
        }
    };

    // if the second api request succeed
    Response.Listener<String> onSuccess_details = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {

            JSONObject jsonObject1 = null;
            JSONObject result1 = null;

            try {
                jsonObject1 = new JSONObject(response);
                result1 = jsonObject1.getJSONObject("results");

                MoviesByGenre moviesByGenre = new MoviesByGenre();
                moviesByGenre.setImdb_id(result1.getString("imdb_id"));
                moviesByGenre.setTitle(result1.getString("title"));
                moviesByGenre.setRating(result1.getString("rating"));
                moviesByGenre.setRelease(result1.getString("release"));
                String imageUri = result1.getString("image_url");
                moviesByGenre.setImage(imageUri);

                if (moviesByGenre != null) {
                    arrayList.add(moviesByGenre);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            // display arraylist items inside a gridview
            adapter = new DisplayMoviesByGenreAdapter(getApplicationContext(), arrayList);
            gridView.setAdapter(adapter);
            Log.d("Volley", response);

            // on item click go to movie details
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    MoviesByGenre selectedValue = (MoviesByGenre) parent.getAdapter().getItem(position);
                    Intent i = new Intent(getApplicationContext(), MovieDetails.class);
                    i.putExtra("Imdb_id", selectedValue.getImdb_id());
                    i.putExtra("image_url", selectedValue.getImage());
                    startActivity(i);
                }
            });

        }

    };

}