package com.example.exporttemplate;

import android.app.Activity;
import android.content.Context;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfDocument.Page;
import android.graphics.pdf.PdfDocument.PageInfo;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MeetingMinuteTemplateA {
    private Context context = null;

    private int pageWidth = 300;
    private int pageHeight = 300;

    private int textViewId;

    private OutputStream os;

    public MeetingMinuteTemplateA(Context context, int textViewId){
        this.context = context;
        this.textViewId = textViewId;
    }

    public void Create(String path){
        if(new File(path).exists())
            new File(path).delete();

        PdfDocument document = new PdfDocument();
        PageInfo pageInfo = new PageInfo.Builder(pageWidth, pageHeight, 1).create();
        Page page = document.startPage(pageInfo);

        Log.e("error", "hello");
        Log.e("error", path);
        Log.e("error", Integer.toString(textViewId));

        LinearLayout layout = new LinearLayout(context);

        View content = ((Activity) context).findViewById(textViewId);
        content.measure(800, 480);
        content.layout(100, 100, 300, 200);

//        content.layout(0, 0, pageWidth,pageHeight);
        Log.i("draw view", " content size: "+content.getWidth()+" / "+content.getHeight());

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

//    private void addNewItemWithLeftAndRight(Document document, String textLeft, String textRight, Font textLeftFont, Font textRightFont) throws DocumentException{
//        Chunk chunkTextLeft = new Chunk(textLeft, textLeftFont);
//        Chunk chunkTextRight = new Chunk(textLeft, textRightFont);
//        Paragraph p = new Paragraph(chunkTextLeft);
//        p.add(new Chunk(new VerticalPositionMark()));
//        p.add(chunkTextRight);
//        document.add(p);
//    }
//
//    private void addLineSeperator(Document document) throws DocumentException  {
//        LineSeparator lineSeperator = new LineSeparator();
//        lineSeperator.setLineColor(new BaseColor(0,0,0,68));
//        addLineSpace(document);
//        document.add(new Chunk(lineSeperator));
//        addLineSpace(document);
//    }
//
//    private void addLineSpace(Document document) throws DocumentException {
//        document.add(new Paragraph(""));
//    }
//
//    private void addNewItem(Document document, String text, int align, Font font) throws DocumentException {
//        Chunk chunk = new Chunk(text, font);
//        Paragraph paragraph = new Paragraph(chunk);
//        paragraph.setAlignment((align));
//        document.add(paragraph);
//    }
}
