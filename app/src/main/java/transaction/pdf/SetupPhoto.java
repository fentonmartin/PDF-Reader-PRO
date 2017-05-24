package transaction.pdf;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.pdf.reader.pro.ebin.R;

import java.util.ArrayList;
import java.util.List;

import transaction.pdf.adapters.AddPhotosAdapter;
import transaction.pdf.model.SinglePostImage;
import transaction.pdf.util.ScreenSpecFetch;

class SetupPhoto extends AsyncTask<Void,Void,Void> {

    List<SinglePostImage> items;
    AddPhotosAdapter adapter;
    Bundle saveInstStte;
    ProgressBar pb;
    RecyclerView rv;
    Context ctPdfCreator;
    GridLayoutManager layoutManager;

    public SetupPhoto(Bundle save,     List<SinglePostImage> items ,ProgressBar pb,
            RecyclerView rv,
            Context ctPdfCreator){
        this.items = items;
        this.saveInstStte = save;
        this.pb = pb;
        this.rv = rv;
        this.ctPdfCreator = ctPdfCreator;
    }

    @Override
    protected Void doInBackground(Void... params) {
        if(items==null)items = new ArrayList<>();



        Log.e("1111",items.size()+"e33");
        if(items.size()==0) {
            items.add(0, new SinglePostImage(true, true, R.drawable.icon_camera, "Camera", "null"));
            items.add(1, new SinglePostImage(true, false, R.drawable.galleryicon, "Gallery", "null"));
        }

        if(saveInstStte!=null)
            if(saveInstStte.getStringArrayList("items")!=null){
                Log.e("11","restoring");
                ArrayList<String> aa=saveInstStte.getStringArrayList("items");
                if(aa.size()>0){
                    Log.e("11resSize",aa.size()+"restoring");
                    for(String a:aa){
                        Log.e("11ds",a);
                        items.add(new SinglePostImage(false,false,0,"",a));
                    }
                }

            }

        int orientation=ctPdfCreator.getResources().getConfiguration().orientation;
        if(orientation== Configuration.ORIENTATION_PORTRAIT){
            //code for portrait mode
              layoutManager
                    = new GridLayoutManager(ctPdfCreator,3,GridLayoutManager.VERTICAL,false);

            float v = ScreenSpecFetch.calculateScreenWidth(ctPdfCreator) - ScreenSpecFetch.convertDpToPixel(24, ctPdfCreator);
            adapter = new AddPhotosAdapter(ctPdfCreator, items, ((v*4)/(3*3)));
        }
        else{
            //code for landscape mode
              layoutManager
                    = new GridLayoutManager(ctPdfCreator,4,GridLayoutManager.VERTICAL,false);

            float v = ScreenSpecFetch.calculateScreenWidth(ctPdfCreator) - ScreenSpecFetch.convertDpToPixel(24, ctPdfCreator);
            adapter = new AddPhotosAdapter(ctPdfCreator, items, ((v*4)/(3*4)));
        }

        try {
            ((PdfCreator) ctPdfCreator).items = items; // syncing
            ((Activity) ctPdfCreator).runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    rv.setAdapter(adapter);
                    rv.setItemAnimator(new DefaultItemAnimator());
                    rv.setHasFixedSize(true);
                    rv.setLayoutManager(layoutManager);
                    adapter.setClickListener(((PdfCreator) ctPdfCreator));
                }
            });
        }catch (Exception e){Log.e("wrong in doimB",e.toString());}
        //recycler_view.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        try{
            pb.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
        }catch (Exception a){Log.e("E SetUpPhotoasyn post", a.toString());}
    }
}
