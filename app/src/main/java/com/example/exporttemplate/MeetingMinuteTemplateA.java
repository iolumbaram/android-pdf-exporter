package com.example.exporttemplate;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfDocument.Page;
import android.graphics.pdf.PdfDocument.PageInfo;
import android.os.Build;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.BulletSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.O)

public class MeetingMinuteTemplateA {
    Context context = null;
    int textViewId;
    OutputStream os;
    int height;
    int width;

    TextView meetingMinuteContent;

    public MeetingMinuteTemplateA(Context context, int textViewId){
        this.context = context;
        this.textViewId = textViewId;
    }

    public void Create(String path){
        if(new File(path).exists())
            new File(path).delete();

        getDisplayMetrics();

        PdfDocument document = new PdfDocument();
        PageInfo pageInfo = new PageInfo.Builder(width, height, 1).create();
        Page page = document.startPage(pageInfo);

        meetingMinuteContent = (TextView)getActivity(context).findViewById(textViewId);

        SpannableStringBuilder meetingMinuteContentText = new SpannableStringBuilder();

        meetingMinuteContentText.append(addTitle("title"));

        meetingMinuteContentText.append(addLineBreaker(2));

        meetingMinuteContentText.append(addHeader("header"));
        meetingMinuteContentText.append(addSubHeader("subHeader"));

        meetingMinuteContentText.append(addBulletLine("point1"));
        meetingMinuteContentText.append(addBulletLine("point2"));
        meetingMinuteContentText.append(addBulletLine("point3"));

        meetingMinuteContentText.append(addLineBreaker(1));

        meetingMinuteContentText.append(addHeader("header"));
        meetingMinuteContentText.append(addSubHeader("subHeader"));

        meetingMinuteContentText.append(addBulletLine("point1"));
        meetingMinuteContentText.append(addBulletLine("point2"));
        meetingMinuteContentText.append(addBulletLine("point3"));

        meetingMinuteContent.setText(meetingMinuteContentText);

        View content = ((Activity) context).findViewById(textViewId); //Log.i("draw view", " content size: "+content.getWidth()+" / "+content.getHeight());
        content.draw(page.getCanvas());
        document.finishPage(page);

        try {
            File pdfDirPath = new File(path);
            pdfDirPath.mkdirs();
            File file = new File(pdfDirPath, "pdfsend.pdf");
            os = new FileOutputStream(file);
            document.writeTo(os);
            document.close();
            os.close();

        } catch (IOException e) {
            throw new RuntimeException("Error generating file", e);
        }
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

    private SpannableString addTitle(CharSequence text){
        //title of meeting
        //date-time of meeting
        if (TextUtils.isEmpty(text)) {
            return null;
        }

        Date currentTime = Calendar.getInstance().getTime();

        SpannableString ss = new SpannableString(text+"\n"+currentTime+"\n");
        ss.setSpan(new RelativeSizeSpan(2f),0,text.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new UnderlineSpan(), 0, text.length(), 0);

        return ss;
    }

    private SpannableString addHeader(CharSequence text){
        if (TextUtils.isEmpty(text)) {
            return null;
        }
        StyleSpan styleSpan = new StyleSpan(android.graphics.Typeface.BOLD);

        //spannableString.setSpan(new RelativeSizeSpan(2f),0,4,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableString ss = new SpannableString(text+"\n");
        ss.setSpan(new RelativeSizeSpan(3f),0,text.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(styleSpan, 0, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(Color.RED), 0,  ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return ss;
    }

    private SpannableString addSubHeader(CharSequence text){
        if (TextUtils.isEmpty(text)) {
            return null;
        }
        SpannableString ss = new SpannableString(text+"\n");
        ss.setSpan(new RelativeSizeSpan(2f),0,text.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return ss;
    }

    private SpannableString addBulletLine(CharSequence text){
        if (TextUtils.isEmpty(text)) {
            return null;
        }

        SpannableString ss = new SpannableString(text+"\n");
        ss.setSpan(new BulletSpan(10), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        //        SpannableString bulletss = new SpannableString("My text \nbullet one\nbullet two\n");
//        bulletss.setSpan(new BulletSpan(10), 9, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        bulletss.setSpan(new BulletSpan(10), 20, 21, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spannableString.append(bulletss);

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
