package transaction.pdf;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.pdf.reader.pro.ebin.R;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;

import transaction.pdf.adapters.AddPhotosAdapter;
import transaction.pdf.model.SinglePostImage;

public class PdfCreator extends AppCompatActivity implements AddPhotosAdapter.IntrerfaceTakePostPhotoC{


    RecyclerView rv_add_photos;
    ProgressBar pb_photos;
    SetupPhoto asyPhotoLoader;
    List<SinglePostImage> items = new ArrayList<>();
    File newfile = new File("");
    int TAKE_PHOTO_CODE = 10;
    int RESULT_LOAD_IMAGE = 11;
    final int MAX_IMG = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_creator);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create PDF from Images");
        rv_add_photos = (RecyclerView) findViewById(R.id.add_photos);
        pb_photos = (ProgressBar) findViewById(R.id.progressBar);
        asyPhotoLoader = new SetupPhoto(savedInstanceState,items, pb_photos, rv_add_photos, PdfCreator.this);
        asyPhotoLoader.execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void intrerfaceTakePostPhotoClick(int position, List<SinglePostImage> items) {
        if(items.get(position).isHeadder && items.get(position).isCamera){
            final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/";
            File newdir = new File(dir);
            newdir.mkdirs();
            DateFormat df = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
            String date = df.format(Calendar.getInstance().getTime());
            String file = dir+"pdf_"+date+".jpg";
            newfile = new File(file.replace(" ","_").replace("+","_").replace(":","_"));
            Log.e("111w1", newfile.getAbsolutePath().toString());
//            try {
//                newfile.createNewFile();
//            }
//            catch (IOException e)
//            {
//                Log.e("111341", e.toString());
//            }

            Uri outputFileUri = Uri.fromFile(newfile);

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);;
            this.startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
        } ;
        if(items.get(position).isHeadder && (!items.get(position).isCamera)){
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select Picture"), RESULT_LOAD_IMAGE);

        } ;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("1111", newfile.getAbsolutePath()+"wqe32");


        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK){
//            Log.e("CameraDemo", "Pic saved from Gall");
            try {
                Uri selectedImageUri = data.getData();
                String selectedImagePath = getPath(PdfCreator.this, selectedImageUri);
                Log.e("GalleryDemo", selectedImagePath);
                items.add(new SinglePostImage(false, false, 0, "", selectedImagePath));
                if (items.size() >= MAX_IMG) {
                    items.remove(0);
                    items.remove(0);
                }
                asyPhotoLoader = new SetupPhoto(null, items, pb_photos, rv_add_photos, PdfCreator.this);
                asyPhotoLoader.execute();
            }catch (Exception exc){
                Snackbar.make(PdfCreator.this.findViewById(android.R.id.content),"Sorry, Something went wrong",Snackbar.LENGTH_LONG).show();
            }

        }



        if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
            Log.e("CameraDemo", "Pic saved");

            items.add(new SinglePostImage(false,false,0,"",newfile.getAbsolutePath()));
            if(items.size()>= MAX_IMG){
                items.remove(0);
                items.remove(0);
            }
            asyPhotoLoader = new SetupPhoto(null,items, pb_photos, rv_add_photos, PdfCreator.this);
            asyPhotoLoader.execute();


        }
    }

//    public String getRealPathFromURI(Uri uri) {
//        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
//        cursor.moveToFirst();
//        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
//        return cursor.getString(idx);
//    }

    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.KITKAT;
        Log.i("URI",uri+"");
        String result = uri+"";
        // DocumentProvider
        //  if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
        if (isKitKat && (result.contains("media.documents"))) {

            String[] ary = result.split("/");
            int length = ary.length;
            String imgary = ary[length-1];
            final String[] dat = imgary.split("%3A");

            final String docId = dat[1];
            final String type = dat[0];

            Uri contentUri = null;
            if ("image".equals(type)) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            } else if ("video".equals(type)) {

            } else if ("audio".equals(type)) {
            }

            final String selection = "_id=?";
            final String[] selectionArgs = new String[] {
                    dat[1]
            };

            return getDataColumn(context, contentUri, selection, selectionArgs);
        }
        else
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("111","savi");
        ArrayList<String> lSet = new ArrayList<>();
        for (SinglePostImage a:items){
            if(!a.isHeadder) lSet.add(a.imgPath);
        }
        outState.putStringArrayList("items",lSet);
        outState.putString("path",newfile.getAbsolutePath());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        newfile = new File(savedInstanceState.getString("path"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab11);
        fab.setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fab11:

                    Log.e("fab","clk");
                    String path = null  ;
                    ((FloatingActionMenu) findViewById(R.id.goback)).close(true);
                   new CreateAndSavePDF(items,PdfCreator.this).execute();

                    break;
            }
        }
    };

            }
