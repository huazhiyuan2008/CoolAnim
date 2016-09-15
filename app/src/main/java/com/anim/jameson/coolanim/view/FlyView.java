package com.anim.jameson.coolanim.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.anim.jameson.coolanim.R;

import jameson.io.library.util.ViewUtil;

/**
 * Created by jameson on 9/14/16.
 */
public class FlyView extends LinearLayout {

    public static final long TRANS_DURATION = 500;
    private CardView mCardView;
    private ImageView mMovieImageView;
    private SpreadView mSpreadView;
    private Rect mFromRect;
    private Rect mToRect;
    private TipViewModel mTipViewModel;

    public FlyView(Context context) {
        super(context);
        initView();
    }

    public FlyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public FlyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.view_fly, this);
        mCardView = (CardView) findViewById(R.id.cardView);
        mMovieImageView = (ImageView) findViewById(R.id.movieImageView);
        mSpreadView = (SpreadView) findViewById(R.id.spreadView);
    }

    public void startAnim(Rect fromRect, int imageResId) {
        mFromRect = fromRect;
        this.setVisibility(View.VISIBLE);

        mMovieImageView.setImageResource(imageResId);
        mToRect = ViewUtil.getOnScreenRect(mCardView);

        AnimatorSet tranAnimSet = transAnim(fromRect, mToRect);
        tranAnimSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mSpreadView.setMaskColor(0xffc1c1c1);
                mTipViewModel = new TipViewModel(new RectF(mToRect.left, mToRect.top + (mToRect.height() - mToRect.width()) / 2, mToRect.right, mToRect.bottom - (mToRect.height() - mToRect.width()) / 2), TipViewModel.TipViewModelType.CIRCLE);
                mSpreadView.startAnim(mTipViewModel, true);
            }
        });
        tranAnimSet.start();
        mSpreadView.setMaskColor(Color.TRANSPARENT);
    }

    @NonNull
    private AnimatorSet transAnim(Rect fromRect, Rect toRect) {
        AnimatorSet tranAnimSet = new AnimatorSet();
        ObjectAnimator transxAnim = ObjectAnimator.ofFloat(mCardView, "translationX", fromRect.centerX() - toRect.centerX(), 0);
        ObjectAnimator transyAnim = ObjectAnimator.ofFloat(mCardView, "translationY", fromRect.centerY() - toRect.centerY(), 0);
        ObjectAnimator cardElevationAnim = ObjectAnimator.ofFloat(mCardView, "cardElevation", mCardView.getMaxCardElevation(), mCardView.getMaxCardElevation(),
                mCardView.getMaxCardElevation(), mCardView.getMaxCardElevation(), 0);
        tranAnimSet.setDuration(TRANS_DURATION);
        tranAnimSet.play(transxAnim).with(transyAnim).with(cardElevationAnim);
        tranAnimSet.setInterpolator(new AccelerateDecelerateInterpolator());
        return tranAnimSet;
    }

    @NonNull
    private AnimatorSet transAnimRevert(Rect fromRect, Rect toRect, AnimatorListenerAdapter animatorListenerAdapter) {
        AnimatorSet tranAnimSet = new AnimatorSet();
        ObjectAnimator transxAnim = ObjectAnimator.ofFloat(mCardView, "translationX", fromRect.centerX() - toRect.centerX());
        ObjectAnimator transyAnim = ObjectAnimator.ofFloat(mCardView, "translationY", fromRect.centerY() - toRect.centerY());
        ObjectAnimator cardElevationAnim = ObjectAnimator.ofFloat(mCardView, "cardElevation", mCardView.getMaxCardElevation(), mCardView.getMaxCardElevation(),
                mCardView.getMaxCardElevation(), mCardView.getMaxCardElevation(), 0);
        tranAnimSet.setDuration(TRANS_DURATION);
        tranAnimSet.play(transxAnim).with(transyAnim).with(cardElevationAnim);
        if (animatorListenerAdapter != null) {
            tranAnimSet.addListener(animatorListenerAdapter);
        }
        tranAnimSet.setInterpolator(new AccelerateDecelerateInterpolator());
        return tranAnimSet;
    }

    public void revertAnim(final OnAnimFinishCallback animatorListenerAdapter) {
        mSpreadView.startAnim(mTipViewModel, false, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mSpreadView.setVisibility(INVISIBLE);

                transAnimRevert(mFromRect, mToRect, new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);

                        if (animatorListenerAdapter != null) {
                            animatorListenerAdapter.onAnimationEnd();
                        }

                        // 回到初始位置
                        mCardView.animate().translationX(0).translationY(0).setDuration(1).start();
                    }
                }).start();
            }
        });
    }

    public View getCardView() {
        return mCardView;
    }

    public interface OnAnimFinishCallback {
        void onAnimationEnd();
    }
}
