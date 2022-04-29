package com.example.imdb;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.util.Log;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.HashMap;

// class to display UpcomingMovies
public class HomeFragment extends Fragment {

    ListView listView;
    ArrayList<UpcomingMovie> arrayList;
    UpcomingMovieAdapter adapter;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        listView = (ListView) view.findViewById(R.id.listUpcomingMovies);
        arrayList = new ArrayList<UpcomingMovie>();

        // send 'GET' request to the api by passing a token and a url
        String url = "https://data-imdb1.p.rapidapi.com/movie/order/upcoming/?page_size=50";
        String token = "4920bbf220mshb82d577d1fb1c4ap1f2115jsn30db28914797";
        CustomStringRequest postRequest = new CustomStringRequest(Request.Method.GET, url, token, onSuccess, onFailure);

        try {
            SimpleRequestQueueFactory factory = SimpleRequestQueueFactory.getInstance(getActivity().getApplicationContext());
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
                // get the response and place it inside a jsonObject (the api response is json format)
                jsonObject = new JSONObject(response);
                // get the desired array "results" where we need to retrieve data
                results = jsonObject.getJSONArray("results");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // iterated through each result in the array
            for (int j = 0; j < results.length(); j++) {
                try {
                    JSONObject result = results.getJSONObject(j);

                    // send another api request to get the each movie details using the id we retrieved from the first api
                    String url_details = "https://data-imdb1.p.rapidapi.com/movie/id/" + result.getString("imdb_id") + "/";
                    String token_details = "4920bbf220mshb82d577d1fb1c4ap1f2115jsn30db28914797";

                    CustomStringRequest postRequest1 = new CustomStringRequest(Request.Method.GET, url_details, token_details, onSuccess_details, onFailure);

                    SimpleRequestQueueFactory factory1 = SimpleRequestQueueFactory.getInstance(getActivity().getApplicationContext());
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
                // place the response in a jsonObject
                jsonObject1 = new JSONObject(response);
                // get the data of the object results
                result1 = jsonObject1.getJSONObject("results");

                // create an upcomingMovie object and fill set the id, title, release date, description and image url
                UpcomingMovie upcomingMovie = new UpcomingMovie();
                upcomingMovie.setImdb_id(result1.getString("imdb_id"));
                upcomingMovie.setTitle(result1.getString("title"));
                upcomingMovie.setRelease(result1.getString("release"));
                upcomingMovie.setDescription(result1.getString("description"));
                String imageUri = result1.getString("image_url");
                upcomingMovie.setImage(imageUri);

                // add the object to the arrayList
                arrayList.add(upcomingMovie);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            // display the items of the arraylist inside a listView
            adapter = new UpcomingMovieAdapter(getActivity(), arrayList);
            listView.setAdapter(adapter);
            Log.d("Volley", response);

            // on UpcomingMovie click go to the movie details
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    UpcomingMovie selectedValue = (UpcomingMovie) parent.getAdapter().getItem(position);
                    Intent i = new Intent(getActivity().getApplicationContext(), MovieDetails.class);
                    i.putExtra("Imdb_id", selectedValue.getImdb_id());
                    i.putExtra("image_url", selectedValue.getImage());
                    startActivity(i);
                }
            });
        }

    };

}



