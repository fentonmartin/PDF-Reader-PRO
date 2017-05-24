package transaction.pdf;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.artifex.mupdfdemo.MuPDFActivity;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import transaction.pdf.model.SinglePostImage;

public class CreateAndSavePDF extends AsyncTask<Void,Void,Boolean>{

    List<SinglePostImage> items;
    Context ct;
    ProgressDialog dialog;
    String path;

    public CreateAndSavePDF(List<SinglePostImage> items, Context ct) {
        this.items = items;
        this.ct = ct;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
         dialog = new ProgressDialog(ct);
        dialog.setMessage("Creating PDF..");
        dialog.show();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {

  if(items.size()>2) {
      File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/PDF Reader");

      File f = new File(file, "pdf_" + System.currentTimeMillis() + ".pdf");
      path = f.getAbsolutePath();

      Log.v("stage 1", "store the pdf in sd card");

      Document document = new Document(PageSize.A4, 10, 10, 5, 5);


      Log.v("stage 2", "Document Created");

      PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(f));

      Log.v("Stage 3", "Pdf writer");

      document.open();


      for (SinglePostImage i : items) {
          if (!i.isHeadder) {
              Image image = Image.getInstance(i.imgPath);

              Log.v("Stage 6", "Image path adding");
              image.setAlignment(Image.MIDDLE);
              image.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());

              Log.v("Stage 7", "Image Alignments");


//                        image.setBorder(Image.BOX);

//                        image.setBorderWidth(15);

              document.add(image);
          }
      }


      Log.v("Stage 8", "Image adding");

      document.close();
      writer.close();
      return true;
  }else return false;
        }catch (Exception s){Log.e("112",s.toString()); return false;}


    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);

            try {
                dialog.dismiss();
                 if(aBoolean){
                     Uri uri33 = Uri.parse(path);
                     Intent intent33 = new Intent(((PdfCreator) ct),MuPDFActivity.class);
                     intent33.setAction(Intent.ACTION_VIEW);
                     intent33.setData(uri33);
                     ((PdfCreator) ct).startActivity(intent33);
                 }else{
                     Snackbar.make(((PdfCreator) ct).findViewById(android.R.id.content), "Sorry Something went wrong!",Snackbar.LENGTH_LONG).show();
                 }
            }catch (Exception e){}
                }
}
