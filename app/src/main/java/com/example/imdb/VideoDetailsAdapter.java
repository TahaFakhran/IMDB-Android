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
public class VideoDetailsAdapter extends BaseAdapter {

    Context context;
    ArrayList<VideoDetails> arrayList;

    public VideoDetailsAdapter(Context context, ArrayList<VideoDetails> arrayList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_video_list, parent, false);
        }
        ImageView icon = (ImageView) convertView.findViewById(R.id.videoIcon);
        TextView title = (TextView) convertView.findViewById(R.id.videoTitle);

        Picasso.get().load(arrayList.get(position).getIcon()).placeholder(R.drawable.notfound).into(icon);
        title.setText(arrayList.get(position).getTitle());

        return convertView;
    }
}
