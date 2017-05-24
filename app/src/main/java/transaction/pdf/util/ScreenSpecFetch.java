package transaction.pdf.util;


import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Display;

public class ScreenSpecFetch {

    public  ScreenSpecFetch(){

    }




    public static float calculateTopPicksFragmentHeight(Context c){
        try{  float tottalSpsize = 14; //14+14
            float totalDpPadding = 15 ; //5+5+5
            float totalPxtext = sptoPixel(c,tottalSpsize);
            float totalPxPadding = convertDpToPixel(totalDpPadding, c);
            float calculateTopPicksFragmentHeigh = getSingleItemHeight(c) + totalPxPadding + totalPxtext;
            return  calculateTopPicksFragmentHeigh;
        }catch (Exception ex){ //Things gone beyond the context
            return 0;
        }
    }

    public static float calculateMoreChannelsFragmentHeight(Context c){
        try{
            float tottalSpsize = 14; //14+14
            float totalDpPadding = 15; //5+5+5
            float totalPxtext = sptoPixel(c,tottalSpsize);
            float totalPxPadding = convertDpToPixel(totalDpPadding, c);
            float calculateMoreChannelsFragmentHeigh =( getSingleItemHeight(c) * 3) + totalPxPadding + totalPxtext;
            return  calculateMoreChannelsFragmentHeigh;
        }catch (Exception ex){ //Things gone beyond the context
            return 0;
        }
    }

    public static float getSingleItemHeight(Context c){
        try{
            float tottalSpsize = 30; //14+14
            float totalDpPadding = 78; //2+2+5+5+5+5+30

            float dpPaddingToolbar = 48;

            float totalPxtext = sptoPixel(c,tottalSpsize);
            float totalPxPadding = convertDpToPixel(totalDpPadding, c);
            float itemHeight = (float) ((calculateScreenHeight(c) - totalPxPadding - totalPxtext - dpPaddingToolbar - getStatusBarHeight(c))/4);
            return itemHeight;
        }catch (Exception ex){ //Things gone beyond the context
            return 0;
        }

    }

    public static int getStatusBarHeight(Context c) {
        try{
            int result = 0;

            int resourceId = c.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = c.getResources().getDimensionPixelSize(resourceId);
            }
            return result;
        }catch (Exception ex){ //Things gone beyond the context
            return 0;
        }
    }




    ////////////////////////////////////////////////////////

    public static  int calculateScreenWidth(Context c){

        try{
            Display dis = ((Activity) c).getWindowManager().getDefaultDisplay();
            int width = dis.getWidth();
            int height = dis.getHeight();
            return  width;
        }catch (Exception ex){ //Things gone beyond the context
            return 0;
        }
    }

    public static  int calculateScreenHeight(Context c){
        try{
            Display dis = ((Activity) c).getWindowManager().getDefaultDisplay();
            int width = dis.getWidth();
            int height = dis.getHeight();
            return  height;
        }catch (Exception ex){ //Things gone beyond the context
            return 0;
        }
    }



    public static float sptoPixel(Context context, float sp) {
        try {
            float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
            return sp * scaledDensity;
        }catch (Exception ex){ //Things gone beyond the context
            return 0;
        }
    }

    public static float convertDpToPixel(float dp, Context context){
        try {
            Resources resources = context.getResources();
            DisplayMetrics metrics = resources.getDisplayMetrics();
            float px = dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
            return px;
        }catch (Exception ex){ //Things gone beyond the context
            return 0;
        }
    }

    public static float convertPixelsToDp(float px, Context context){
        try{
            Resources resources = context.getResources();
            DisplayMetrics metrics = resources.getDisplayMetrics();
            float dp = px / (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
            return dp;
        }catch (Exception ex){ //Things gone beyond the context
            return 0;
        }
    }

}