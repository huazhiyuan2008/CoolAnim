package com.anim.jameson.coolanim.view;

import android.graphics.RectF;

/**
 * Created by jameson on 9/14/16.
 */
public class TipViewModel implements Cloneable {
    public enum TipViewModelType {
        RECT, CIRCLE
    }

    private TipViewModelType type;
    private RectF rectF;

    public TipViewModel(RectF rectf, TipViewModelType type) {
        this.rectF = rectf;
        this.type = type;
    }

    public TipViewModelType getType() {
        return type;
    }

    public void setType(TipViewModelType type) {
        this.type = type;
    }

    public RectF getRectF() {
        return rectF;
    }

    public void setRectF(RectF rectF) {
        this.rectF = rectF;
    }

    @Override
    protected Object clone() {
        return new TipViewModel(new RectF(this.getRectF().left, this.getRectF().top, this.getRectF().right, this.getRectF().bottom), this.getType());
    }
}
