package com.example.imdb;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

// class to display genres
public class CategoryFragment extends Fragment {

    ListView listView;
    ArrayList<GenresMovies> arrayList;
    GenresMoviesAdapter adapter;

    public CategoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = (ListView) view.findViewById(R.id.listGenresMovies);
        arrayList = new ArrayList<GenresMovies>();

        // 'GET' api request to get all movie genres
        String url = "https://data-imdb1.p.rapidapi.com/genres/";
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
            for (int i = 0; i < results.length(); i++) {
                try {
                    JSONObject result = results.getJSONObject(i);
                    GenresMovies genresMovies = new GenresMovies();
                    genresMovies.setGenre(result.getString("genre"));
                    arrayList.add(genresMovies);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            // display arrayList items inside a listView
            adapter = new GenresMoviesAdapter(getActivity(), arrayList);
            listView.setAdapter(adapter);
            Log.d("Volley", response);

            // on genre click go to activity DisplayMoviesByGenre
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    GenresMovies selectedValue = (GenresMovies) parent.getAdapter().getItem(position);
                    Intent i = new Intent(getActivity().getApplicationContext(), DisplayMoviesByGenre.class);
                    i.putExtra("genre", selectedValue.getGenre());
                    startActivity(i);
                }
            });
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
}