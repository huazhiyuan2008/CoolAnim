package com.anim.jameson.coolanim;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jameson on 9/14/16.
 */
public class MovieAdapter extends BaseAdapter {
    private List<Integer> mList = new ArrayList<>();

    public MovieAdapter(List<Integer> list) {
        mList.clear();
        mList.addAll(list);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_movie, viewGroup, false);
        }

        ImageView imageView = (ImageView) view.findViewById(R.id.movieImageView);
        imageView.setImageResource(mList.get(i));
        return view;
    }

}
