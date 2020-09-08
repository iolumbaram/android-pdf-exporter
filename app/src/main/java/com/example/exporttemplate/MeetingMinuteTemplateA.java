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
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.O)

public class MeetingMinuteTemplateA {
    Context context = null;
    int textViewId;
    String path;
    int height;
    int width;

    int ssBuilderSpanSizeLimit = 100;
    ArrayList<SpannableStringBuilder> templateContent = null;

    public MeetingMinuteTemplateA(Context context, int textViewId, String path){
        this.context = context;
        this.textViewId = textViewId;
        this.path = path;
    }

    public ArrayList<SpannableStringBuilder> Create(String speechToText){
        getDisplayMetrics();

        templateContent = new ArrayList<SpannableStringBuilder>();
        int len = createChuncks(speechToText).size();
        SpannableStringBuilder ss = new SpannableStringBuilder(createChuncks(speechToText).get(0));
        for(int i=1; i < len; i++){
            ss.append(createChuncks(speechToText).get(i));

            if(i%27 == 0){ //tentative max no of lines in a page
                Spanned formattedLine = SpannableString.valueOf(ss);
                SpannableStringBuilder formattedPage = new SpannableStringBuilder();

                formattedPage.append(formattedLine);
                templateContent.add(formattedPage);

                ss.delete(0, ss.length()-1);
            }

            if(i == len -1){
                Spanned formattedLine = SpannableString.valueOf(ss);
                SpannableStringBuilder formattedPage = new SpannableStringBuilder();

                formattedPage.append(formattedLine);
                templateContent.add(formattedPage);

                ss.delete(0, ss.length()-1);
            }
        }
        return templateContent;
    }

    private int maxParaLines = 10; //every 10th lines create a new page


//    private ArrayList<SpannableString> createPage(SpannableString para){
//        ArrayList<SpannableString> pages = new ArrayList<SpannableString>();
//        String _para = para.toString();
//        String page = "";
//        String[] lines = _para.split("\n", -1);
//
//        for(int i=0; i<lines.length; i++){
//            if(i%10 == 0){
//                pages.add(page);
//            }
//
//            page += lines[i];
//
//            if(i==lines.length-1)
//                pages.add(page);
//        }
//        return pages;
//    }

    private int maxChunkLength = 43;

    private ArrayList<SpannableString> createChuncks(String text){
        ArrayList<SpannableString> chunks = new ArrayList<SpannableString>();
        String[] splited = text.split("\\s+");
        String chunk = splited[0];

        chunks.add(addBulletLine("\r")); //first chuck has a bullet; subsequent chucks has a tab(whitespace) instead

        for(int i=1;i<splited.length;i++){
            if(!check(chunk, splited[i])) {
                chunks.add(addTab(chunk+"\r\n"));
                chunk = splited[i];
                continue;
            }

            chunk += "\r" + splited[i];

            if(i==splited.length-1)
                chunks.add(addTab(chunk+"\r\n"));
        }
        return chunks;
    }

    private boolean check(String textChunk, String word){
        if(maxChunkLength - textChunk.length() > word.length())
            return true;
        return false;
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
        int absoluteSizeSpan = 15;

        Date currentTime = Calendar.getInstance().getTime();

        SpannableString ss = new SpannableString(text+"\n"+currentTime+"\n");
        ss.setSpan(new AbsoluteSizeSpan(absoluteSizeSpan, true), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //ss.setSpan(new RelativeSizeSpan(2f),0,text.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new UnderlineSpan(), 0, text.length(), 0);

//        InSpanLimit(absoluteSizeSpan);
        return ss;
    }

    private SpannableString addHeader(CharSequence text){
        if (TextUtils.isEmpty(text)) {
            return null;
        }
        int absoluteSizeSpan = 15;

        StyleSpan styleSpan = new StyleSpan(android.graphics.Typeface.BOLD);

        SpannableString ss = new SpannableString(text+"\n");
        ss.setSpan(new AbsoluteSizeSpan(absoluteSizeSpan, true), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //ss.setSpan(new RelativeSizeSpan(3f),0,text.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(styleSpan, 0, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(Color.RED), 0,  ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

//        InSpanLimit(absoluteSizeSpan);
        return ss;
    }

    private SpannableString addSubHeader(CharSequence text){
        if (TextUtils.isEmpty(text)) {
            return null;
        }
        int absoluteSizeSpan = 10;

        SpannableString ss = new SpannableString(text+"\n");
        //ss.setSpan(new RelativeSizeSpan(2f),0,text.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new AbsoluteSizeSpan(absoluteSizeSpan, true), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

//        InSpanLimit(absoluteSizeSpan);
        return ss;
    }

    private SpannableString addBulletLine(CharSequence text){
        if (TextUtils.isEmpty(text)) {
            return null;
        }
        int absoluteSizeSpan = 8;

        SpannableString ss = new SpannableString(text);
        ss.setSpan(new BulletSpan(10), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new AbsoluteSizeSpan(absoluteSizeSpan, true), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

//        InSpanLimit(absoluteSizeSpan);
        return ss;
    }

    private SpannableString addTab(CharSequence text){
        if (TextUtils.isEmpty(text)) {
            return null;
        }
        //text = String.format("%1$-" + 1 + "s", text);
        int absoluteSizeSpan = 8;

        SpannableString ss = new SpannableString("\r\r\r"+text);
        ss.setSpan(new AbsoluteSizeSpan(absoluteSizeSpan, true), 0, text.length()+3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

//        InSpanLimit(absoluteSizeSpan);
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

//    private boolean InSpanLimit(int spanCount){
//        ssBuilderSpanSize += spanCount;
//        if(ssBuilderSpanSize >= ssBuilderSpanSizeLimit) {
//            SpannableStringBuilders[0] = meetingMinuteContentText;
//            meetingMinuteContentText.clear();
//            return false;
//        }
//        return true;
//    }

    private void drawSeperator(int x, int y){

    }
}

//    private String createChunck(String text){
//        String[] splited = text.split("\\s+");
//        String _textChunk = splited[0];
//
//        for(int i=1;i<splited.length;i++){
//            if(!check(_textChunk, splited[i])) {
//                chunk += _textChunk+"\r\n";
//                _textChunk = splited[i];
//                continue;
//            }
//
//            _textChunk += "\r" + splited[i];
//
//            if(i==splited.length-1)
//                chunk += _textChunk+"\r\n";
//        }
//        return chunk;
//    }

//        meetingMinuteContentText = new SpannableStringBuilder();
//
//                meetingMinuteContentText.append(addTitle("title"));
//
//                meetingMinuteContentText.append(addHeader("header"));
//                meetingMinuteContentText.append(addSubHeader("subHeader"));
//
//                meetingMinuteContentText.append(addBulletLine("point1"));
//                meetingMinuteContentText.append(addBulletLine("point2"));
//                meetingMinuteContentText.append(addBulletLine("point3"));
//
//                meetingMinuteContentText.append(addHeader("header"));
//                meetingMinuteContentText.append(addSubHeader("subHeader"));
//
//                meetingMinuteContentText.append(addBulletLine("point1"));
//                meetingMinuteContentText.append(addBulletLine("point2"));

//        while(speechToText.length() > 0){
//            meetingMinuteContentText.append(addBulletLine(createChuck("asd")));
//            speechToText.substring(1);
//        }
//SpannableString para = addBulletLine(createChuncks(speechToText));

//        SpannableStringBuilders = new SpannableStringBuilder[createPage(para.toString()).size()];

//        meetingMinuteContentText.append(para);
//        SpannableStringBuilders[0] = meetingMinuteContentText;