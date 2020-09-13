package com.example.exporttemplate;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BulletSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.exporttemplate.ui.LineHeightSetter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.exporttemplate.R.drawable.banner_test;

@RequiresApi(api = Build.VERSION_CODES.O)

public class MeetingMinuteTemplateA {
    Context context = null;
    int textViewId;
    String path;
    int height;
    int width;

    private int maxPageLines = 12; //every 27th lines create a new page

    public MeetingMinuteTemplateA(Context context, int textViewId, String path){
        this.context = context;
        this.textViewId = textViewId;
        this.path = path;
    }

    String title = "Annual Board Meeting";
    ArrayList<String> header =  new ArrayList<>(Arrays.asList("Wendy Writer, CEO", "Ronny Reader, CFO","Abby Author, CTO"));

    public ArrayList<SpannableStringBuilder> Create(String speechToText){
        getDisplayMetrics();

        ArrayList<String> paras = splitToPara(speechToText);
        if(paras.size() > 0)
        {
            ArrayList<SpannableStringBuilder> templateContent = new ArrayList<SpannableStringBuilder>();
            SpannableStringBuilder ss = new SpannableStringBuilder();

            ss.append(addTitle(title));
            ss.append(addLineBreaker(1));
            ss.append(addAttendees(header));
            ss.append(addLineBreaker(1));
            SpannableStringBuilder fp = new SpannableStringBuilder();
            Spanned fl = SpannableString.valueOf(ss);
            fp.append(fl);
            templateContent.add(fp);
            ss.clear();

            ss.append(addHeader("Notes"));
            ArrayList<SpannableString> chunksAll = new ArrayList<SpannableString>();
            for(int i=0;i<paras.size();i++){
                ArrayList<SpannableString> chunks = createChuncks(paras.get(i));
                for(int j=0; j<chunks.size();j++){
                    chunksAll.add(chunks.get(j));
                }
            }

            ArrayList<List<SpannableString>> split = chunkArrayList(chunksAll,maxPageLines);
            for(int j=0; j<split.size();j++){
                for(int k=0; k<split.get(j).size();k++){
                    ss.append(split.get(j).get(k));
                }
                SpannableStringBuilder formattedPage = new SpannableStringBuilder();
                Spanned formattedLine = SpannableString.valueOf(ss);
                formattedPage.append(formattedLine);
                templateContent.add(formattedPage);
                ss.clear();
            }

            return templateContent;
        }
        return null;
    }

//    Set the text size to size physical pixels, or to size device-independent pixels if dip is true.
//    a4 - 3508 x 2480 size in pixel 300 dpi
    private SpannableString addTitle(CharSequence text){
        if (TextUtils.isEmpty(text)) {
            return null;
        }
        SpannableStringBuilder sb = new SpannableStringBuilder();
        int absoluteSizeSpan = 14;

        String imagePlaceHolder = "1";
        SpannableString imageSS = new SpannableString(imagePlaceHolder);
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), banner_test);
        Bitmap resized = Bitmap.createScaledBitmap(bm,(int)(bm.getWidth()*0.35), (int)(bm.getHeight()*0.75), true);
        Drawable d = new BitmapDrawable(context.getResources(), resized);
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
        imageSS.setSpan(span, 0, imagePlaceHolder.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        sb.append(imageSS);

        sb.append("\n");

        SpannableString titleText = new SpannableString("\r"+text+"\n");
        titleText.setSpan(new AbsoluteSizeSpan(absoluteSizeSpan, true), 0, titleText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        int brown = Color.parseColor("#695d46");
        titleText.setSpan(new ForegroundColorSpan(brown), 0,  titleText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.append(titleText);

        Date currentTime = Calendar.getInstance().getTime();
        SpannableString dateTimeText = new SpannableString("\r"+currentTime+"\n");
        dateTimeText.setSpan(new AbsoluteSizeSpan(9, true), 0, dateTimeText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.append(dateTimeText);

        SpannableString seperator = new SpannableString("\r"+"       \n");
        //ss.setSpan(new RelativeSizeSpan(2f),0,text.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        seperator.setSpan(new UnderlineSpan(), 1, seperator.length(), 0);
        sb.append(seperator);

        return  SpannableString.valueOf(sb);
    }

    private SpannableString addAttendees(ArrayList<String> attendeesList){
        if (attendeesList.isEmpty()) {
            return null;
        }
        SpannableStringBuilder sb = new SpannableStringBuilder();
        int absoluteSizeSpan = 14;

        StyleSpan styleSpan = new StyleSpan(android.graphics.Typeface.BOLD);

        String headerText = "Attendees";

        SpannableString ss = new SpannableString("\r"+headerText+"\n");
        ss.setSpan(new LineHeightSetter(20), 0, headerText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new AbsoluteSizeSpan(absoluteSizeSpan, true), 0, headerText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //ss.setSpan(new RelativeSizeSpan(3f),0,text.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(styleSpan, 0, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        int orange = Color.parseColor("#ff5e0e");
        ss.setSpan(new ForegroundColorSpan(orange), 0,  ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.append(ss);

        for(int i=0; i<attendeesList.size(); i++){
            SpannableString attendee = new SpannableString("\r\r"+attendeesList.get(i) +"\n");
            attendee.setSpan(new LineHeightSetter(10), 0,attendee.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            attendee.setSpan(new AbsoluteSizeSpan(8, true), 0, attendee.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            //ss.setSpan(new RelativeSizeSpan(3f),0,text.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            attendee.setSpan(styleSpan, 0, attendee.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            sb.append(attendee);
        }

        return  SpannableString.valueOf(sb);
    }

    private SpannableString addHeader(CharSequence text){
        if (TextUtils.isEmpty(text)) {
            return null;
        }
        int absoluteSizeSpan = 14;

        StyleSpan styleSpan = new StyleSpan(android.graphics.Typeface.BOLD);
        SpannableString ss = new SpannableString("\r"+text+"\n");
        //ss.setSpan(new RelativeSizeSpan(2f),0,text.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new LineHeightSetter(10), 0,text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new AbsoluteSizeSpan(absoluteSizeSpan, true), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(styleSpan, 0, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        int orange = Color.parseColor("#ff5e0e");
        ss.setSpan(new ForegroundColorSpan(orange), 0,  ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return ss;
    }

    private SpannableString addBulletLine(CharSequence text){
        if (TextUtils.isEmpty(text)) {
            return null;
        }
        int absoluteSizeSpan = 8;

        SpannableString ss = new SpannableString(text);
        ss.setSpan(new BulletSpan(5), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new AbsoluteSizeSpan(absoluteSizeSpan, true), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    private SpannableString addTab(CharSequence text){
        if (TextUtils.isEmpty(text)) {
            return null;
        }
        int absoluteSizeSpan = 8;

        SpannableString ss = new SpannableString("\r\r\r\r"+text);
        ss.setSpan(new AbsoluteSizeSpan(absoluteSizeSpan, true), 0, text.length()+3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return ss;
    }

    private StringBuilder addLineBreaker(int num){
        if(num <= 0) return null;
        String space = "";
        StringBuilder s = new StringBuilder();
        SpannableString ss = new SpannableString(space);
        ss.setSpan(new LineHeightSetter(20), 0, space.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.append(ss);
//        s.append("\n");

        return s;
    }

    private ArrayList<String> splitToPara(String text){
        ArrayList<String> paras = new ArrayList<String>();
        String[] splited = text.split("\\r?\\n");

        for(int i=0;i<splited.length;i++){
            paras.add(splited[i]);
        }
        return paras;
    }

    private int maxChunkLength = 43;

    private ArrayList<SpannableString> createChuncks(String para){
        ArrayList<SpannableString> chunks = new ArrayList<SpannableString>();
        String[] splited = para.split("\\s+");
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

    private ArrayList<List<SpannableString>> chunkArrayList(ArrayList<SpannableString> arrayToChunk, int chunkSize) {
        ArrayList<List<SpannableString>> chunkList = new ArrayList<>();
        int guide = arrayToChunk.size();
        int index = 0;
        int cursor = chunkSize;
        while (cursor < arrayToChunk.size()){
            chunkList.add(arrayToChunk.subList(index, cursor));
            guide = guide - chunkSize;
            index = index + chunkSize;
            cursor = cursor + chunkSize;
        }
        if (guide >0) {
            chunkList.add(arrayToChunk.subList(index, index + guide));
        }
        return chunkList;
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
}

