/**
 *
 */
package com.anim.jameson.coolanim.view;

import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by jameson on 9/14/16.
 */
public class SpreadView extends View {

    public static final long SPREAD_DURATION = 500;
    private Paint mPaint;
    private Canvas mTempCanvas;
    private Bitmap mBitmap; // 画布bitmap

    private int mMaxRadius = 2000;
    private int mWidth;
    private int mHeight;
    /**
     * 遮罩层颜色
     */
    private int mMaskColor = 0x99000000;

    private TipViewModel mTipViewModel;
    private TipViewModel mSourceTipViewModel;
    private Paint mEraserPaint;

    public SpreadView(Context context) {
        super(context);
        initView(context);
    }

    public SpreadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SpreadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        setWillNotDraw(false);
        // setLayerType(LAYER_TYPE_HARDWARE, null);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mMaskColor);

        PorterDuffXfermode mBlender = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);

        mEraserPaint = new Paint();
        mEraserPaint.setColor(Color.TRANSPARENT);
        mEraserPaint.setXfermode(mBlender);

        if (isInEditMode()) return;

        post(new Runnable() {
            @Override
            public void run() {
                mMaxRadius = (int) Math.ceil(Math.sqrt(mWidth * mWidth + mHeight * mHeight));
                mBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
                mTempCanvas = new Canvas(mBitmap);
            }
        });

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mTempCanvas == null) return;

        mTempCanvas.drawRect(0, 0, mWidth, mHeight, mPaint);
        if (mTipViewModel != null) {
            if (mTipViewModel.getType() == TipViewModel.TipViewModelType.CIRCLE) {
                mTempCanvas.drawOval(mTipViewModel.getRectF(), mEraserPaint);
            } else {
                mTempCanvas.drawRect(mTipViewModel.getRectF(), mEraserPaint);
            }
        }
        canvas.drawBitmap(mBitmap, 0, 0, mPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    /**
     * 开始动画
     *
     * @param model
     * @param isSpread
     */
    public void startAnim(TipViewModel model, boolean isSpread) {
        startAnim(model, isSpread, null);
    }

    public void startAnim(TipViewModel model, boolean isSpread, AnimatorListenerAdapter animatorListenerAdapter) {
        if (model == null) return;

        setVisibility(VISIBLE);
        mSourceTipViewModel = (TipViewModel) model.clone();
        mTipViewModel = (TipViewModel) model.clone();

        final int start = isSpread ? 0 : mMaxRadius;
        int end = !isSpread ? 0 : mMaxRadius;
        ValueAnimator anim = ValueAnimator.ofInt(start, end);
        anim.setDuration(SPREAD_DURATION);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int currentRadius = (int) valueAnimator.getAnimatedValue();
                mTipViewModel.getRectF().left = mSourceTipViewModel.getRectF().left - currentRadius;
                mTipViewModel.getRectF().top = mSourceTipViewModel.getRectF().top - currentRadius;
                mTipViewModel.getRectF().right = mSourceTipViewModel.getRectF().right + currentRadius;
                mTipViewModel.getRectF().bottom = mSourceTipViewModel.getRectF().bottom + currentRadius;
                invalidate();
            }
        });
        if (animatorListenerAdapter != null) {
            anim.addListener(animatorListenerAdapter);
        }
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.start();
    }

    public void setMaskColor(int maskColor) {
        mMaskColor = maskColor;
        mPaint.setColor(mMaskColor);

        invalidate();
    }
}
