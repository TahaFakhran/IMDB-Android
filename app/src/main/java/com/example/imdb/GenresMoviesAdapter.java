package com.example.imdb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

// adapter class to display the content from the arrayList
public class GenresMoviesAdapter extends BaseAdapter {

    Context context;
    ArrayList<GenresMovies> arrayList;

    public GenresMoviesAdapter(Context context, ArrayList<GenresMovies> arrayList) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_genresmovies_list, parent, false);
        }
        TextView genre;
        genre = (TextView) convertView.findViewById(R.id.txtGenres);
        genre.setText(arrayList.get(position).getGenre());
        return convertView;
    }
}
