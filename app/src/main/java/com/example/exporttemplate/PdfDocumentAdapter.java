package com.example.exporttemplate;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.pdf.PrintedPdfDocument;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class PdfDocumentAdapter extends PrintDocumentAdapter {
    Context context;
    String path;
    ArrayList<SpannableStringBuilder> speechToTextContent;

    private int pageHeight;
    private int pageWidth;
    public PdfDocument myPdfDocument;
    public int totalpages = 0;

    public PdfDocumentAdapter(Context context, String path, ArrayList<SpannableStringBuilder> speechToTextContent){
        this.context = context;
        this.path = path;
        this.speechToTextContent = speechToTextContent;
    }
    @Override
    public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras) {
        myPdfDocument = new PrintedPdfDocument(context, newAttributes);
        pageHeight = newAttributes.getMediaSize().getHeightMils()/1000 * 72;
        pageWidth = newAttributes.getMediaSize().getWidthMils()/1000 * 72;

        totalpages = speechToTextContent.size();

        if(cancellationSignal.isCanceled()){
            callback.onLayoutCancelled();
            return;
        }

//        if (totalpages > 0) {
//            PrintDocumentInfo.Builder builder = new PrintDocumentInfo.Builder("meetingMinutes");
//            builder.setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
//                    //.setPageCount(PrintDocumentInfo.PAGE_COUNT_UNKNOWN)
//                    .setPageCount(totalpages)
//                    .build();
//            callback.onLayoutFinished(builder.build(), !oldAttributes.equals(newAttributes));
//        }

        PrintDocumentInfo.Builder builder = new PrintDocumentInfo.Builder("meetingMinutes");
        builder.setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                //.setPageCount(PrintDocumentInfo.PAGE_COUNT_UNKNOWN)
                .setPageCount(totalpages)
                .build();
        callback.onLayoutFinished(builder.build(), !oldAttributes.equals(newAttributes));
    }

    @Override
    public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {
        for (int i = 0; i < totalpages; i++) {
            PdfDocument.PageInfo newPage = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, i).create();

            PdfDocument.Page page = myPdfDocument.startPage(newPage);

            if (cancellationSignal.isCanceled()) {
                callback.onWriteCancelled();
                myPdfDocument.close();
                myPdfDocument = null;
                return;
            }
            //drawPage(page, i);
            drawPage(page, speechToTextContent.get(i));
            myPdfDocument.finishPage(page);
//            if (pageInRange(pages, i))
//            {
//                PdfDocument.PageInfo newPage = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, i).create();
//
//                PdfDocument.Page page = myPdfDocument.startPage(newPage);
//
//                if (cancellationSignal.isCanceled()) {
//                    callback.onWriteCancelled();
//                    myPdfDocument.close();
//                    myPdfDocument = null;
//                    return;
//                }
//                //drawPage(page, i);
//                Log.e("error", String.valueOf(speechToTextContent.get(i)));
//                drawPage(page, speechToTextContent.get(i));
//                myPdfDocument.finishPage(page);
//            }
        }

        try {
            myPdfDocument.writeTo(new FileOutputStream(
                    destination.getFileDescriptor()));
        } catch (IOException e) {
            callback.onWriteFailed(e.toString());
            return;
        } finally {
            myPdfDocument.close();
            myPdfDocument = null;
        }

        callback.onWriteFinished(pages);
    }

//    public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {
//        for (int i = 0; i < totalpages; i++) {
//            if (pageInRange(pages, i))
//            {
//                PdfDocument.PageInfo newPage = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, i).create();
//
//                PdfDocument.Page page = myPdfDocument.startPage(newPage);
//
//                if (cancellationSignal.isCanceled()) {
//                    callback.onWriteCancelled();
//                    myPdfDocument.close();
//                    myPdfDocument = null;
//                    return;
//                }
//                //drawPage(page, i);
//                drawPage(page, speechToTextContent[i]);
//                myPdfDocument.finishPage(page);
//            }
//        }
//
//        try {
//            myPdfDocument.writeTo(new FileOutputStream(
//                    destination.getFileDescriptor()));
//        } catch (IOException e) {
//            callback.onWriteFailed(e.toString());
//            return;
//        } finally {
//            myPdfDocument.close();
//            myPdfDocument = null;
//        }
//
//        callback.onWriteFinished(pages);
//    }

    private boolean pageInRange(PageRange[] pageRanges, int page)
    {
        Log.e("error", String.valueOf(pageRanges.length));

        for (int i = 0; i<pageRanges.length; i++)
        {
            if ((page >= pageRanges[i].getStart()) &&  (page <= pageRanges[i].getEnd()))
                return true;
        }
        return false;
    }

    private void drawPage(PdfDocument.Page page, SpannableStringBuilder content){
        LinearLayout layout = new LinearLayout(context);
        TextView textView = new TextView(context);
        textView.setText(content);
        layout.addView(textView);
        Canvas canvas = page.getCanvas();
        layout.measure(canvas.getWidth(), canvas.getHeight());
        layout.layout(0, 0, canvas.getWidth(), canvas.getHeight());
        layout.draw(canvas);
    }

    private void drawPage(PdfDocument.Page page, int pagenumber) {

        Canvas canvas = page.getCanvas();

        pagenumber++; // Make sure page numbers start at 1

        int titleBaseLine = 72;
        int leftMargin = 54;

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(40);

//        SpannableStringBuilder meetingMinuteContentText = new SpannableStringBuilder("HELLO\nHELLO2\n");
//        meetingMinuteContentText.append("HELLO\r\n");

        canvas.drawText("asdfasdf\nasdfa123sdf\n".toString(),leftMargin,titleBaseLine,paint);
        paint.setTextSize(14);
        canvas.drawText("This is some test content to verify that custom document printing works", leftMargin, titleBaseLine + 35, paint);

//        if (pagenumber % 2 == 0)
//            paint.setColor(Color.RED);
//        else
//            paint.setColor(Color.GREEN);
//
//        PdfDocument.PageInfo pageInfo = page.getInfo();
//
//        canvas.drawCircle(pageInfo.getPageWidth()/2,
//                pageInfo.getPageHeight()/2,
//                150,
//                paint);
    }
}