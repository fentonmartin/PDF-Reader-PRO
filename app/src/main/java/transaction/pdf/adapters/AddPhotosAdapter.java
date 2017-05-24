package transaction.pdf.adapters;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pdf.reader.pro.ebin.R;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

import transaction.pdf.model.SinglePostImage;

public class AddPhotosAdapter  extends RecyclerView.Adapter<AddPhotosAdapter.MyViewHolder> {

    IntrerfaceTakePostPhotoC clickListener1;
    private LayoutInflater inflate;
    private List<SinglePostImage> items;
    Bitmap placeHolderBitmap;
    float height;


    public class AsyDrawable extends BitmapDrawable {
        WeakReference<LoadImageAsyncItem> taskRefer ;

        AsyDrawable(Resources res,
                    Bitmap bmapPlace,
                    LoadImageAsyncItem taskRef){
            super(res,bmapPlace);

            taskRefer = new WeakReference<LoadImageAsyncItem>(taskRef);
        }
        public LoadImageAsyncItem getBitMapTask(){
            return taskRefer.get();
        }
    }

    public static boolean checkBitmapWorkerTask (File imagesFile,
                                                 ImageView imageView){
        LoadImageAsyncItem bmWork = getBitmapWorkerTask(imageView);
        if(bmWork != null){
            final File workFile = bmWork.getmImageFile();
            if(workFile != null){
                bmWork.cancel(true);
            }else {
                // bitmap worker task file is same as imageView expecting.
                return  false;
            }
        }
        return  true;
    }

    public static LoadImageAsyncItem getBitmapWorkerTask(ImageView imageView) {
        Drawable drawable = imageView.getDrawable();
        if (drawable instanceof AsyDrawable) {
            AsyDrawable asyDrawable = (AsyDrawable) drawable;
            return asyDrawable.getBitMapTask();
        }
        return null;
    }





    public  AddPhotosAdapter(Context ct, List<SinglePostImage> items, float height){
        inflate = LayoutInflater.from(ct);
        this.items = items;
        this.height = height;
    }

    public void setClickListener(IntrerfaceTakePostPhotoC clickListener1){
        this.clickListener1 = clickListener1;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflate.inflate(R.layout.add_post_single_image, parent, false);
       view.getLayoutParams().height = (int) height;
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SinglePostImage item = items.get(position);
        if(item.isHeadder) {
            holder.title.setText(item.resourceTitle);
            holder.imIcon.setImageResource(item.resource);
            holder.imIcon.setColorFilter(Color.argb(255, 114, 114, 114));
        }else {
            holder.title.setVisibility(View.GONE);


//           Bitmap b = BitmapFactory.decodeFile(item.imgPath);
//           holder.imIcon.setImageBitmap(b);

            if(checkBitmapWorkerTask(new File(item.imgPath),
                    holder.imIcon)){
                LoadImageAsyncItem bw = (LoadImageAsyncItem) new LoadImageAsyncItem(new File(item.imgPath),holder.imIcon,200,150);

                AsyDrawable asyDrawable = new AsyDrawable(holder.imIcon.getResources(),
                        placeHolderBitmap,
                        bw);
                holder.imIcon.setImageDrawable(asyDrawable);
                bw.execute();
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView title;
        ImageView imIcon;

        public MyViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.textView3) ;
            imIcon = (ImageView) itemView.findViewById(R.id.imageView) ;
            itemView.setOnClickListener(this);

//            itemView.getLayoutParams().width = width;
//            itemView.getLayoutParams().height = height;
        }

        @Override
        public void onClick(View v) {

            if( items.get(getPosition()).isHeadder ) {
                clickListener1.intrerfaceTakePostPhotoClick(getPosition(), items);

            }
        }
    }




    public interface  IntrerfaceTakePostPhotoC{
        public void intrerfaceTakePostPhotoClick(int position, List<SinglePostImage> items);
    }

}