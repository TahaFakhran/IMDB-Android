package com.example.imdb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

// adapter class to display the content from the arrayList
public class UpcomingMovieAdapter extends BaseAdapter {
    Context context;
    ArrayList<UpcomingMovie> arrayList;

    public UpcomingMovieAdapter(Context context, ArrayList<UpcomingMovie> arrayList) {
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
        TextView title, release, description;
        ImageView image;
        //  the first item in the arrayList has a different display
        if (position == 0) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_first_upcomingmovie_list, parent, false);

            title = (TextView) convertView.findViewById(R.id.titleFirstUpcomingMovie);
            image = (ImageView) convertView.findViewById(R.id.imageFirstUpcomingMovie);

            title.setText(arrayList.get(position).getTitle());
            Picasso.get().load(arrayList.get(position).getImage()).placeholder(R.drawable.notfound).into(image);

            // display for the rest of the items
        } else {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_upcomingmovie_list, parent, false);
            title = (TextView) convertView.findViewById(R.id.titleUpcomingMovie);
            image = (ImageView) convertView.findViewById(R.id.imageUpcomingMovie);
            description = (TextView) convertView.findViewById(R.id.descriptionUpcomingMovie);
            release = (TextView) convertView.findViewById(R.id.releaseUpcomingMovie);
            title.setText(arrayList.get(position).getTitle());
            Picasso.get().load(arrayList.get(position).getImage()).placeholder(R.drawable.notfound).into(image);
            description.setText(arrayList.get(position).getDescription());
            release.setText(arrayList.get(position).getRelease());
        }

        return convertView;
    }
}