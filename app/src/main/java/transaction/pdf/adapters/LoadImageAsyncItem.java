package transaction.pdf.adapters;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;


public class LoadImageAsyncItem extends AsyncTask<Void,Void, Bitmap> {

    File imf;
    ImageView im;
    int TARGET_IMAGE_WIDTH = 200;
    int TARGET_IMAGE_HEIGHT = 200;

    LoadImageAsyncItem(File imf, ImageView im, int width, int height){
        this.imf = imf;
        this.im = im;
        TARGET_IMAGE_WIDTH = width;
        TARGET_IMAGE_HEIGHT = height;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        // Bitmap bitmap = null;




        try {
            File im_f = imf;
            return  decode_from_a_file(imf);
        }catch (Exception e){
            // return bitmap;
            Log.e("inv_file", "f");
        }
        return null;
    }


    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);


        if(isCancelled()){
            bitmap = null;
        }

        if(bitmap != null && im !=null){
            //  ImageView viewImage = imgRef.get();
            LoadImageAsyncItem wk = AddPhotosAdapter.getBitmapWorkerTask(im);
            if(this == wk && im != null){
                im.setImageBitmap(bitmap) ;
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