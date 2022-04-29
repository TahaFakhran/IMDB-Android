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
public class FavoriteMovieAdapter extends BaseAdapter {
    Context context;
    ArrayList<FavoriteMovie> arrayList;

    public FavoriteMovieAdapter(Context context, ArrayList<FavoriteMovie> arrayList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_favoritemovie_list, parent, false);
        }
        TextView txtTitle;
        ImageView imgView;
        txtTitle = (TextView) convertView.findViewById(R.id.titleFavoriteMovie);
        imgView = (ImageView) convertView.findViewById(R.id.imageFavoriteMovie);

        txtTitle.setText(arrayList.get(position).getTitle());
        Picasso.get().load(arrayList.get(position).getImage()).placeholder(R.drawable.notfound).into(imgView);

        return convertView;
    }
}
