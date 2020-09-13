package com.example.exporttemplate;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
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

import androidx.annotation.RequiresApi;

import com.example.exporttemplate.ui.LineHeightSetter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import static com.example.exporttemplate.R.drawable.banner_test;

@RequiresApi(api = Build.VERSION_CODES.O)

public class MeetingMinuteTemplateA {
    Context context = null;
    int textViewId;
    String path;
    int height;
    int width;

    private int maxPageLines = 13; //every 27th lines create a new page

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
            int sumChunkLines = 0;
            for(int i=0;i<paras.size();i++){
                ArrayList<SpannableString> chunks = createChuncks(paras.get(i));
                int len = chunks.size();
                int chunkCursor = 0;

                while(chunkCursor < len && chunkCursor < maxPageLines){
                    ss.append(chunks.get(chunkCursor));
                    sumChunkLines++;
                    chunkCursor++;
                }

                if(sumChunkLines >= maxPageLines){
                    SpannableStringBuilder formattedPage = new SpannableStringBuilder();
                    Spanned formattedLine = SpannableString.valueOf(ss);
                    formattedPage.append(formattedLine);
                    templateContent.add(formattedPage);

                    ss.delete(0, ss.length());

                    int remaingLineCountCursor = chunkCursor;
                    while(remaingLineCountCursor < len){
                        ss.append(chunks.get(remaingLineCountCursor));
                        remaingLineCountCursor++;
                    }

                    sumChunkLines = 0;
                }

                if(i == paras.size()-1 && chunkCursor <= maxPageLines){
                    SpannableStringBuilder formattedPage = new SpannableStringBuilder();
                    ss.delete(0, ss.length());

                    int noResidualLines = len-chunkCursor;

                    if(noResidualLines > maxPageLines){
                        for(int j=chunkCursor; j<chunkCursor+maxPageLines; j++){
                            ss.append(chunks.get(j));
                        }
                        Spanned formattedLine = SpannableString.valueOf(ss);
                        formattedPage.append(formattedLine);
                        templateContent.add(formattedPage);

                        ss.delete(0, ss.length());

                        for(int k=len-maxPageLines; k<len; k++){
                            ss.append(chunks.get(k));
                        }

                        SpannableStringBuilder formattedPage2 = new SpannableStringBuilder();
                        Spanned formattedLine2 = SpannableString.valueOf(ss);
                        formattedPage2.append(SpannableString.valueOf(formattedLine2));
                        templateContent.add(formattedPage2);
                    }
                    else{
                        for(int j=chunkCursor; j<len; j++){
                            ss.append(chunks.get(j));
                        }
                        Spanned formattedLine = SpannableString.valueOf(ss);
                        formattedPage.append(formattedLine);
                        templateContent.add(formattedPage);
                    }
                }
            }
            return templateContent;
        }
        return null;
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
        SpannableStringBuilder sb = new SpannableStringBuilder();
        int absoluteSizeSpan = 14;

        String imagePlaceHolder = "1";
        SpannableString imageSS = new SpannableString(imagePlaceHolder);
        Drawable d = context.getDrawable(banner_test);
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
        imageSS.setSpan(span, 0, imagePlaceHolder.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        sb.append(imageSS);

        sb.append("\n");

        SpannableString titleText = new SpannableString(text+"\n");
        titleText.setSpan(new AbsoluteSizeSpan(absoluteSizeSpan, true), 0, titleText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        int brown = Color.parseColor("#695d46");
        titleText.setSpan(new ForegroundColorSpan(brown), 0,  titleText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.append(titleText);

        Date currentTime = Calendar.getInstance().getTime();
        SpannableString dateTimeText = new SpannableString(currentTime+"\n");
        dateTimeText.setSpan(new AbsoluteSizeSpan(9, true), 0, dateTimeText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.append(dateTimeText);

        SpannableString seperator = new SpannableString("       \n");
        //ss.setSpan(new RelativeSizeSpan(2f),0,text.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        seperator.setSpan(new UnderlineSpan(), 0, seperator.length(), 0);
        sb.append(seperator);

//        InSpanLimit(absoluteSizeSpan);
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

        SpannableString ss = new SpannableString(headerText+"\n");
        ss.setSpan(new LineHeightSetter(20), 0, headerText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new AbsoluteSizeSpan(absoluteSizeSpan, true), 0, headerText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //ss.setSpan(new RelativeSizeSpan(3f),0,text.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(styleSpan, 0, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        int orange = Color.parseColor("#ff5e0e");
        ss.setSpan(new ForegroundColorSpan(orange), 0,  ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.append(ss);

        for(int i=0; i<attendeesList.size(); i++){
            SpannableString attendee = new SpannableString(attendeesList.get(i) +"\n");
            attendee.setSpan(new LineHeightSetter(10), 0,attendee.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            attendee.setSpan(new AbsoluteSizeSpan(8, true), 0, attendee.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            //ss.setSpan(new RelativeSizeSpan(3f),0,text.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            attendee.setSpan(styleSpan, 0, attendee.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            sb.append(attendee);
        }

//        InSpanLimit(absoluteSizeSpan);
        return  SpannableString.valueOf(sb);
    }

    private SpannableString addHeader(CharSequence text){
        if (TextUtils.isEmpty(text)) {
            return null;
        }
        int absoluteSizeSpan = 14;

        StyleSpan styleSpan = new StyleSpan(android.graphics.Typeface.BOLD);
        SpannableString ss = new SpannableString(text+"\n");
        //ss.setSpan(new RelativeSizeSpan(2f),0,text.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new LineHeightSetter(10), 0,text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new AbsoluteSizeSpan(absoluteSizeSpan, true), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(styleSpan, 0, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        int orange = Color.parseColor("#ff5e0e");
        ss.setSpan(new ForegroundColorSpan(orange), 0,  ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

//        InSpanLimit(absoluteSizeSpan);
        return ss;
    }

    private SpannableString addBulletLine(CharSequence text){
        if (TextUtils.isEmpty(text)) {
            return null;
        }
        int absoluteSizeSpan = 8;

        SpannableString ss = new SpannableString(text);
        ss.setSpan(new LineHeightSetter(10), 0,text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
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
        ss.setSpan(new LineHeightSetter(10), 0,text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new AbsoluteSizeSpan(absoluteSizeSpan, true), 0, text.length()+3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

//        InSpanLimit(absoluteSizeSpan);
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

