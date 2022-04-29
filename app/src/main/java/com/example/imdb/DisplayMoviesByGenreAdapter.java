package com.example.imdb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

// adapter class to display the content from the arrayList
public class DisplayMoviesByGenreAdapter extends BaseAdapter {
    Context context;
    ArrayList<MoviesByGenre> arrayList;

    public DisplayMoviesByGenreAdapter(Context context, ArrayList<MoviesByGenre> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    // displaying arrayList content inside the xml layout
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_movie_by_genre_list, parent, false);
        }
        TextView title, release, rating;
        ImageView image;
        title = (TextView) convertView.findViewById(R.id.titleMovieByGenre);
        image = (ImageView) convertView.findViewById(R.id.imageMovieByGenre);
        rating = (TextView) convertView.findViewById(R.id.ratingMovieByGenre);
        release = (TextView) convertView.findViewById(R.id.releaseMovieByGenre);
        title.setText(arrayList.get(position).getTitle());
        Picasso.get().load(arrayList.get(position).getImage()).placeholder(R.drawable.notfound).into(image);
        release.setText(arrayList.get(position).getRelease());
        rating.setText(arrayList.get(position).getRating());

        return convertView;
    }

}
