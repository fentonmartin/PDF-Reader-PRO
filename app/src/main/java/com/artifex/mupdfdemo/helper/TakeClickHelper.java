package com.artifex.mupdfdemo.helper;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.artifex.mupdfdemo.MuPDFActivity;
import com.artifex.mupdfdemo.OutlineActivity;
import com.artifex.mupdfdemo.OutlineActivityData;
import com.artifex.mupdfdemo.OutlineItem;
import com.pdf.reader.pro.ebin.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;

import comvc.example.pdfviewerlite.Dir_size_Human_Redable;
import transaction.pdf.PDFSaveToDiskShow;

/**
 * Created by ebinjoy999 on 25/05/17.
 */

public class TakeClickHelper {
    public static void takeMenuClick(int menuID, MuPDFActivity muPDFActivity){
        switch (menuID) {
            case android.R.id.home:
                muPDFActivity.onBackPressed();
                break;
            case R.id.action_bookmark:
                String name = 	new File(muPDFActivity.getIntent().getData().toString()).getName().replace("'","''");;
                Dir_size_Human_Redable newobj = new Dir_size_Human_Redable();
                long f_size= new File(muPDFActivity.getIntent().getData().toString()).length();
                String data  = newobj.humanReadableByteCount(f_size, false).replace("'","''");;
                Date lastModDate = new Date(new File(muPDFActivity.getIntent().getData().toString()).lastModified());
                DateFormat formater = DateFormat.getDateInstance();
                String date  = formater.format(lastModDate).replace("'","''");;
                String path = muPDFActivity.getIntent().getData().toString().replace("'","''");
                String image = "a_pdf";
                try{
                    SQLiteDatabase myDB = muPDFActivity.openOrCreateDatabase("myDB1", muPDFActivity.MODE_PRIVATE, null);
                    Cursor r = myDB.rawQuery("select * from PDFBOOK"+ " where path  = '"+path+"' ", null);
                    if(r.moveToFirst()){

                        Snackbar.make(muPDFActivity.findViewById(android.R.id.content), "Already Exist in Bookmark!", Snackbar.LENGTH_LONG).show();

                    }else {

                        myDB.execSQL("insert into PDFBOOK values('"+image+"','"+name+"','"+data+"','"+date+"','"+path+"')");
                        Snackbar.make(muPDFActivity.findViewById(android.R.id.content),"Bookmark Added", Snackbar.LENGTH_LONG).show();
                    }


                }catch(Exception exc){

                    Snackbar.make(muPDFActivity.findViewById(android.R.id.content),"Something went wrong!", Snackbar.LENGTH_LONG).show();
                }

                break;
            case R.id.action_content:
                OutlineItem outline[] = muPDFActivity.core.getOutline();
                if (outline != null) {
                    OutlineActivityData.get().items = outline;
                    Intent intent = new Intent(muPDFActivity,OutlineActivity.class);
                    muPDFActivity.startActivityForResult(intent, 0);
                }else{
                    Snackbar.make(muPDFActivity.findViewById(android.R.id.content),"Sorry, No content found.", Snackbar.LENGTH_LONG).show();
                }
                break;
            case R.id.action_extr_image:
                View view = muPDFActivity.mDocView.getDisplayedView();

                try {
                    if (false == view.isDrawingCacheEnabled()) {
                        view.setDrawingCacheEnabled(true);
                    }
                }catch (Exception e){Log.e("EXC",e.toString());}
                Bitmap bitmap = BitMapHelper.getBitmapFromView(view);
//					Bitmap croppedBitmap = Bitmap.createBitmap(bitmap, 0, 10, bitmap.getWidth(), bitmap.getHeight() - 20);

                try {
                    new File(Environment.getExternalStorageDirectory()+"/PDF Reader").mkdirs();
                    File outputFile = new File(Environment.getExternalStorageDirectory()+"/PDF Reader", System.currentTimeMillis()+"_pdf.jpg");
                    FileOutputStream outputStream = new FileOutputStream(outputFile);

                    // a bit long running
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

                    outputStream.close();
//							Snackbar.make(getWindow().getDecorView().getRootView(), "Image saved successfully...\n Location: "+outputFile.getAbsolutePath(), Snackbar.LENGTH_LONG).show();
                    try{muPDFActivity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));}catch(Exception e){Log.e("Er gallery refresh",e.toString());}
                    muPDFActivity.startActivity(new Intent(muPDFActivity, PDFSaveToDiskShow.class).putExtra("path",outputFile.getAbsolutePath()));
                } catch (IOException e) {
                    Log.e("During IMAGE formation", e.toString());
                    Snackbar.make(muPDFActivity.findViewById(android.R.id.content), "Somethig went wrong can't save...", Snackbar.LENGTH_LONG).show();
                }

                break;
            case R.id.action_share:
//					ArrayList<Uri> iuri =new ArrayList<Uri>();
//
//					File file11=new File(getIntent().getData().toString());
//					Uri uri11 = Uri.fromFile(file11);
//					iuri.add(uri11);
//					Intent iintent = new Intent();
//					iintent.setAction(Intent.ACTION_SEND_MULTIPLE);
//					iintent.setType("*/*");
// 					iintent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, iuri);
//					startActivity(iintent);
                try {
                    File fileWithinMyDir = new File(muPDFActivity.getIntent().getData().toString());
                    Intent intentShareFile = new Intent(Intent.ACTION_SEND);
                    if (fileWithinMyDir.exists()) {
                        intentShareFile.setType("application/pdf");
                        intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + fileWithinMyDir.toString()));
                        Snackbar.make(muPDFActivity.findViewById(android.R.id.content), "Select App to share...", Snackbar.LENGTH_LONG).show();
                        intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                                "Sharing File...");
                        intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");

                        muPDFActivity.startActivity(Intent.createChooser(intentShareFile, "Share File"));
                    }
                }catch (Exception exc){
                    Snackbar.make(muPDFActivity.findViewById(android.R.id.content), "Something went wrong while sharing.", Snackbar.LENGTH_LONG).show();
                    Log.e("Share",exc.toString());
                }

                break;
            case R.id.action_search:
                muPDFActivity.searchModeOn();
                break;
        }
    }

}
