package com.example.exporttemplate.ui;

import android.graphics.Paint;
import android.text.style.LineHeightSpan;

public class LineHeightSetter implements LineHeightSpan {
    private final int height;

    public LineHeightSetter(int height){
        this.height = height;
    }
    @Override
    public void chooseHeight(CharSequence text, int start, int end, int spanstartv, int lineHeight, Paint.FontMetricsInt fm) {
        fm.bottom += height;
        fm.descent += height;
    }
}
