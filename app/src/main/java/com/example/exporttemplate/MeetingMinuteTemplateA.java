package com.example.exporttemplate;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BulletSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;

import androidx.annotation.RequiresApi;

import java.util.Calendar;
import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.O)

public class MeetingMinuteTemplateA {
    Context context = null;
    int textViewId;
    String path;
    int height;
    int width;

    public MeetingMinuteTemplateA(Context context, int textViewId, String path){
        this.context = context;
        this.textViewId = textViewId;
        this.path = path;
    }

    public SpannableStringBuilder Create(){
        getDisplayMetrics();

        SpannableStringBuilder meetingMinuteContentText = new SpannableStringBuilder();

        meetingMinuteContentText.append(addTitle("title"));

        meetingMinuteContentText.append(addHeader("header"));
        meetingMinuteContentText.append(addSubHeader("subHeader"));

        meetingMinuteContentText.append(addBulletLine("point1"));
        meetingMinuteContentText.append(addBulletLine("point2"));
        meetingMinuteContentText.append(addBulletLine("point3"));

        meetingMinuteContentText.append(addHeader("header"));
        meetingMinuteContentText.append(addSubHeader("subHeader"));

        meetingMinuteContentText.append(addBulletLine("point1"));
        meetingMinuteContentText.append(addBulletLine("point2"));

        return meetingMinuteContentText;
    }

    private Activity getActivity(Context context)
    {
        if (context == null)
        {
            return null;
        }
        else if (context instanceof ContextWrapper)
        {
            if (context instanceof Activity)
            {
                return (Activity) context;
            }
            else
            {
                return getActivity(((ContextWrapper) context).getBaseContext());
            }
        }

        return null;
    }

    private void getDisplayMetrics(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity(context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
    }

//    Set the text size to size physical pixels, or to size device-independent pixels if dip is true.
//    a4 - 3508 x 2480 size in pixel 300 dpi
    private SpannableString addTitle(CharSequence text){
        if (TextUtils.isEmpty(text)) {
            return null;
        }

        Date currentTime = Calendar.getInstance().getTime();

        SpannableString ss = new SpannableString(text+"\n"+currentTime+"\n");
        ss.setSpan(new AbsoluteSizeSpan(15, true), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //ss.setSpan(new RelativeSizeSpan(2f),0,text.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new UnderlineSpan(), 0, text.length(), 0);

        return ss;
    }

    private SpannableString addHeader(CharSequence text){
        if (TextUtils.isEmpty(text)) {
            return null;
        }
        StyleSpan styleSpan = new StyleSpan(android.graphics.Typeface.BOLD);

        SpannableString ss = new SpannableString(text+"\n");
        ss.setSpan(new AbsoluteSizeSpan(15, true), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //ss.setSpan(new RelativeSizeSpan(3f),0,text.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(styleSpan, 0, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(Color.RED), 0,  ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return ss;
    }

    private SpannableString addSubHeader(CharSequence text){
        if (TextUtils.isEmpty(text)) {
            return null;
        }
        SpannableString ss = new SpannableString(text+"\n");
        //ss.setSpan(new RelativeSizeSpan(2f),0,text.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new AbsoluteSizeSpan(10, true), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return ss;
    }

    private SpannableString addBulletLine(CharSequence text){
        if (TextUtils.isEmpty(text)) {
            return null;
        }

        SpannableString ss = new SpannableString(text+"\n");
        ss.setSpan(new BulletSpan(10), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new AbsoluteSizeSpan(8, true), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return ss;
    }

    private StringBuilder addLineBreaker(int num){
        if(num <= 0) return null;
        StringBuilder s = new StringBuilder();
        for(int i=0; i<=num;i++){
            s.append("\n");
        }

        return s;
    }

    private void drawSeperator(int x, int y){

    }
}
