package com.example.tengshinan.newsreader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

/**
 * Created by TengShinan on 22/04/2016.
 */
public class NewsAdapter extends BaseAdapter {

    private TextView titleTv;
    private TextView descTv;
    private ImageView thumbnailIv;

    private Context context;
    private ArrayList<News> newsAl;

    public NewsAdapter(Context context, ArrayList<News> newsAl) {
        this.context = context;
        this.newsAl = newsAl;
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).build();
        ImageLoader.getInstance().init(config);
    }

    @Override
    public int getCount() {
        return newsAl.size();
    }

    @Override
    public Object getItem(int position) {
        return newsAl.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if view has been created for the row. If not, lets inflate it
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_news_item, null);
        }

        // Setup controllers and put them on the view
        titleTv = (TextView) convertView.findViewById(R.id.titleTv);
        descTv = (TextView) convertView.findViewById(R.id.descTv);
        thumbnailIv = (ImageView) convertView.findViewById(R.id.thumbnailIv);

        // Setup relative News object and setup TextViews' text
        News news = newsAl.get(position);
        titleTv.setText(news.getTitle());
        descTv.setText(news.getDesc());

        // Setup ImageView
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(news.getThumbnail(), thumbnailIv);
        return convertView;
    }
}
