package com.example.imdb;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

// class to display all videos by rating and you can play the trailer
public class VideoFragment extends Fragment {

    GridView gridView;
    ArrayList<VideoDetails> arrayList;
    VideoDetailsAdapter adapter;

    public VideoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        gridView = (GridView) view.findViewById(R.id.gridviewVideo);
        arrayList = new ArrayList<VideoDetails>();

        // api request to get all movies by rating
        String url = "https://data-imdb1.p.rapidapi.com/movie/order/byRating/?page_size=50";
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

    // if api succeed
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

            for (int i = 0; i < results.length(); i++) {
                try {
                    JSONObject result = results.getJSONObject(i);

                    // another api request to get the movie details (parameters : the movie id)
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

    // if the api request fails
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

                VideoDetails videoDetails = new VideoDetails();
                videoDetails.setImdb_id(result1.getString("imdb_id"));
                videoDetails.setTitle(result1.getString("title"));
                videoDetails.setUrl(result1.getString("trailer"));

                String imageUri = result1.getString("image_url");
                videoDetails.setIcon(imageUri);

                arrayList.add(videoDetails);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            adapter = new VideoDetailsAdapter(getActivity(), arrayList);
            gridView.setAdapter(adapter);
            Log.d("Volley", response);

            // on item click open new Intent to play video
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    VideoDetails selectedValue = (VideoDetails) parent.getAdapter().getItem(position);
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(selectedValue.getUrl())));
                    Log.i("Video", "Video Playing....");
                }
            });
        }
    };
}