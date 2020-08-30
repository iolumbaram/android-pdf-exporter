package com.example.exporttemplate;

import android.Manifest;
import android.content.Context;
<<<<<<< HEAD
=======
import android.content.DialogInterface;
import android.content.pm.PackageManager;
<<<<<<< HEAD
import android.os.Build;
=======
>>>>>>> ed153e14023e7446a452108e1c25988fe57ec5fb
>>>>>>> 0800400db4dc18272fed7bf4fa6a090cabb139ad
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

<<<<<<< HEAD
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;


import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

=======
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private int STORAGE_PERMISSION_CODE = 1;
>>>>>>> ed153e14023e7446a452108e1c25988fe57ec5fb
    Button btn_create_pdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
<<<<<<< HEAD
//        BottomNavigationView navView = findViewById(R.id.nav_view);
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(navView, navController);
=======
>>>>>>> ed153e14023e7446a452108e1c25988fe57ec5fb

        btn_create_pdf = (Button) findViewById(R.id.btn_create_pdf);

        btn_create_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
<<<<<<< HEAD
                Log.e("Error", "persmission ?");
                CreatePDFFile(Common.getAppPath(MainActivity.this)+"test_pdf.pdf");
=======
                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                    Log.e("error", "???");
                    CreatePDFFile(Common.getAppPath(MainActivity.this)+"test_pdf2.pdf");
                }else{
                    requestStoragePermission();
                }
>>>>>>> ed153e14023e7446a452108e1c25988fe57ec5fb
            }
        });
    }

<<<<<<< HEAD
=======
    private void requestStoragePermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            new AlertDialog.Builder(this)
                    .setTitle("Permission Needed")
                    .setMessage("hello")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        }else{
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == STORAGE_PERMISSION_CODE){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

<<<<<<< HEAD
    @RequiresApi(api = Build.VERSION_CODES.O)
=======
>>>>>>> ed153e14023e7446a452108e1c25988fe57ec5fb
>>>>>>> 0800400db4dc18272fed7bf4fa6a090cabb139ad
    private void CreatePDFFile(String path) {
        if(new File(path).exists())
            new File(path).delete();

<<<<<<< HEAD
        try{
            Document document = new Document();

            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();

            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("Author");
            document.addCreator("NAN");

            BaseColor colorAccent = new BaseColor(0,153,204,255);
            float fontSize = 20.0f;
            float valueFontSize = 26.0f;

            BaseFont fontName = BaseFont.createFont("assets/fonts/MontserratAlternates-Black.otf","UTF-8", BaseFont.EMBEDDED);

            Font titleFont = new Font(fontName, 36.0f, Font.NORMAL, BaseColor.BLACK);
            addNewItem(document, "회의록 -Meeting Minutes", Element.ALIGN_CENTER, titleFont);

            Font orderNumberFont = new Font(fontName, fontSize, Font.NORMAL, colorAccent);
            addNewItem(document, "Prepared by:", Element.ALIGN_LEFT, orderNumberFont);

            Font orderNumberFValueFont = new Font(fontName, fontSize, Font.NORMAL, BaseColor.BLACK);
            addNewItem(document, "mr.helloWorld", Element.ALIGN_LEFT, orderNumberFValueFont);
            addLineSeperator(document);

            addNewItem(document, "Creation Date", Element.ALIGN_LEFT, orderNumberFont);
            addNewItem(document, "DateTimeNow", Element.ALIGN_LEFT, titleFont);
            addLineSeperator(document);

            addNewItem(document, "Some Title", Element.ALIGN_LEFT, orderNumberFont);
            addNewItem(document, "someContent", Element.ALIGN_LEFT, titleFont);
            addLineSeperator(document);

            addLineSpace(document);
            addNewItem(document, "Meeting Agenda", Element.ALIGN_CENTER, titleFont);
            addLineSeperator(document);

            addNewItemWithLeftAndRight(document, "PDF Export", "(0.0%)", titleFont, orderNumberFValueFont);
            addNewItemWithLeftAndRight(document, "PDF Export", "12000.0", titleFont, orderNumberFValueFont);
            addLineSeperator(document);

            addNewItemWithLeftAndRight(document, "line", "(0.0%)", titleFont, orderNumberFValueFont);
            addNewItemWithLeftAndRight(document, "line", "12000.0", titleFont, orderNumberFValueFont);
            addLineSeperator(document);

            addLineSpace(document);
            addLineSeperator(document);

            addNewItemWithLeftAndRight(document, "line", "100.0", titleFont, orderNumberFValueFont);
            document.close();

            Toast.makeText(this, "DONE!", Toast.LENGTH_SHORT).show();

            printPDF();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
=======
        MeetingMinuteTemplateA mm = new MeetingMinuteTemplateA(MainActivity.this, R.id.tv_pdf);
        mm.Create(Common.getAppPath(MainActivity.this));

        Toast.makeText(this, "DONE!", Toast.LENGTH_SHORT).show();

        printPDF();
>>>>>>> ed153e14023e7446a452108e1c25988fe57ec5fb
    }

    private void printPDF() {
        PrintManager printManager = (PrintManager)getSystemService(Context.PRINT_SERVICE);
        try{
<<<<<<< HEAD
            PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapter(MainActivity.this, Common.getAppPath(MainActivity.this)+"test_pdf.pdf");
=======
            PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapter(MainActivity.this, Common.getAppPath(MainActivity.this)+"pdfsend.pdf");
>>>>>>> ed153e14023e7446a452108e1c25988fe57ec5fb
            printManager.print("Document", printDocumentAdapter, new PrintAttributes.Builder().build());

        }catch(Exception e){
            Log.e("Error", ""+e.getMessage());
        }
    }
<<<<<<< HEAD


    private void addNewItemWithLeftAndRight(Document document, String textLeft, String textRight, Font textLeftFont, Font textRightFont) throws DocumentException{
        Chunk chunkTextLeft = new Chunk(textLeft, textLeftFont);
        Chunk chunkTextRight = new Chunk(textLeft, textRightFont);
        Paragraph p = new Paragraph(chunkTextLeft);
        p.add(new Chunk(new VerticalPositionMark()));
        p.add(chunkTextRight);
        document.add(p);
    }

    private void addLineSeperator(Document document) throws DocumentException  {
        LineSeparator lineSeperator = new LineSeparator();
        lineSeperator.setLineColor(new BaseColor(0,0,0,68));
        addLineSpace(document);
        document.add(new Chunk(lineSeperator));
        addLineSpace(document);
    }

    private void addLineSpace(Document document) throws DocumentException {
        document.add(new Paragraph(""));
    }

    private void addNewItem(Document document, String text, int align, Font font) throws DocumentException {
        Chunk chunk = new Chunk(text, font);
        Paragraph paragraph = new Paragraph(chunk);
        paragraph.setAlignment((align));
        document.add(paragraph);

    }

=======
>>>>>>> ed153e14023e7446a452108e1c25988fe57ec5fb
}