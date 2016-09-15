package com.anim.jameson.coolanim;

import android.app.FragmentManager;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.anim.jameson.coolanim.view.FlyView;
import com.anim.jameson.coolanim.view.SpreadView;

import java.util.ArrayList;
import java.util.List;

import jameson.io.library.util.ViewUtil;

public class MainActivity extends AppCompatActivity {

    private GridView mGridView;
    private MovieAdapter mAdapter;
    private List<Integer> mList = new ArrayList<>();
    private FlyView mFlyView;
    private View mMovieFragmentLayout;
    private MovieFragment mMovieFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initViews();
    }

    private void initData() {
        for (int i = 0; i < 10; i++) {
            mList.add(R.drawable.movie);
            mList.add(R.drawable.movie);
            mList.add(R.drawable.movie);
        }
    }

    private void initViews() {
        mMovieFragmentLayout = findViewById(R.id.movieFragmentLayout);
        FragmentManager manager = this.getFragmentManager();
        mMovieFragment = (MovieFragment) manager.findFragmentById(R.id.movieFragment);

        mFlyView = (FlyView) findViewById(R.id.flyView);
        mGridView = (GridView) findViewById(R.id.gridView);
        mAdapter = new MovieAdapter(mList);
        mGridView.setAdapter(mAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageView imageView = (ImageView) view.findViewById(R.id.movieImageView);
                Rect fromRect = ViewUtil.getOnScreenRect(imageView);
                mFlyView.startAnim(fromRect, mList.get(position));

                mGridView.setVisibility(View.INVISIBLE);
                mMovieFragmentLayout.setVisibility(View.VISIBLE);
                mMovieFragment.showWhenAnim();
                mFlyView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mMovieFragment.showAllUI();
                    }
                }, FlyView.TRANS_DURATION);
            }
        });

        mFlyView.getCardView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFlyView.revertAnim(new FlyView.OnAnimFinishCallback() {
                    @Override
                    public void onAnimationEnd() {
                        mGridView.setVisibility(View.VISIBLE);
                        mMovieFragmentLayout.setVisibility(View.INVISIBLE);
                        mFlyView.setVisibility(View.INVISIBLE);
                    }
                });

                mFlyView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mMovieFragment.showWhenAnim();
                    }
                }, SpreadView.SPREAD_DURATION);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mGridView.getVisibility() != View.VISIBLE) {
            mFlyView.getCardView().performClick();
        } else {
            super.onBackPressed();
        }
    }
}
