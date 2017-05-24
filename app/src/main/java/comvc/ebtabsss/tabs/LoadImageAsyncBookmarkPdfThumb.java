package comvc.ebtabsss.tabs;




import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class LoadImageAsyncBookmarkPdfThumb extends AsyncTask<Void,Void, Bitmap> {



    File imf;
    ImageView im;
    int width;
    ImageView pb;
    TextView tv_count;
    int count = 0;
    int position;
    int TARGET_IMAGE_WIDTH = 200;
    int TARGET_IMAGE_HEIGHT = 200;




    LoadImageAsyncBookmarkPdfThumb(File imf, ImageView im, int width, int h){
        this.imf = imf;
        this.pb = pb;
        this.im = im;
        this.width = width;
        TARGET_IMAGE_WIDTH = width;
        TARGET_IMAGE_HEIGHT = h;
        this.tv_count = tv_count;
        this.position = position;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        // Bitmap bitmap = null;




        try {
            File im_f = imf;
//            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//
//
//             bitmap = BitmapFactory.decodeFile(im_f.getAbsolutePath(), bmOptions);
//            bitmap = Bitmap.createScaledBitmap(bitmap, width, width, true);
            return  decode_from_a_file(imf);
        }catch (Exception e){
            // return bitmap;
            Log.e("inv_file","f");
        }
        return null;
    }


    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
// try {
//     im.setVisibility(View.VISIBLE);
//     im.setImageBitmap(bitmap);
//     pb.setVisibility(View.INVISIBLE);
//     tv_count.setText(String.valueOf(count-1)+" item(s)");
// }catch(Exception d){
//     Log.e("View not found",d.toString());}

        if(isCancelled()){
            bitmap = null;
        }

        if(bitmap != null && im !=null){
            //  ImageView viewImage = imgRef.get();
            LoadImageAsyncBookmarkPdfThumb wk = Fragment_bookmark_Adapter.getBitmapWorkerTask(im);
            if(this == wk && im != null){
               try{ im.setVisibility(View.VISIBLE);
                im.setImageBitmap(bitmap) ;}catch (Exception e){}
            }
        }
    }



    private int calculateIntSampleSize(BitmapFactory.Options bmOptions){
        // A power of two value is calculated because the decoder uses a final value by
        // rounding down to the nearest power of two, as per in sample size documentation.

        final int photoWidth = bmOptions.outWidth;
        final  int photoHeight  = bmOptions.outHeight;
        int scaleFactor = 1;

        if(photoWidth > TARGET_IMAGE_WIDTH || photoHeight > TARGET_IMAGE_HEIGHT){
            final int halfPhotoWidth = photoWidth / 2;
            final int halfPhotoHeight = photoHeight / 2;


            while (halfPhotoWidth/scaleFactor > TARGET_IMAGE_WIDTH || halfPhotoHeight/scaleFactor >TARGET_IMAGE_HEIGHT){
                scaleFactor *= 2;
            }
        }
        return scaleFactor;
    }


    private  Bitmap decode_from_a_file(File imageFile){
        // Fetch decode with inJustDecodeBounds = true
        BitmapFactory.Options bmoptions = new BitmapFactory.Options();
        bmoptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile.getAbsolutePath(), bmoptions);
        // Calculate inSampleSize
        bmoptions.inSampleSize = calculateIntSampleSize(bmoptions);
        // Decode bitmap with inSampleSize set
        bmoptions.inJustDecodeBounds  = false;
        return BitmapFactory.decodeFile(imageFile.getAbsolutePath(), bmoptions);
    }

    public File getmImageFile(){
        return  imf;
    }
}