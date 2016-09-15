package com.anim.jameson.coolanim;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by jameson on 9/14/16.
 */
public class MovieFragment extends Fragment {

    private ImageView mMovieImageView;
    private ImageView mImageViewBar;
    private TextView mTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMovieImageView = (ImageView) view.findViewById(R.id.movieImageView);
        mImageViewBar = (ImageView) view.findViewById(R.id.imageViewBar);
        mTextView = (TextView) view.findViewById(R.id.title);
    }

    public void showWhenAnim() {
        mMovieImageView.setVisibility(View.INVISIBLE);
        mImageViewBar.setVisibility(View.INVISIBLE);
        mTextView.setVisibility(View.INVISIBLE);
    }

    public void showAllUI() {
        mMovieImageView.setVisibility(View.VISIBLE);
        mImageViewBar.setVisibility(View.VISIBLE);
        mTextView.setVisibility(View.VISIBLE);
    }

}
