package comvc.ebtabsss.tabs;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;

import com.amulyakhare.textdrawable.TextDrawable;
import com.pdf.reader.pro.ebin.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import transaction.pdf.util.MuPDFThumb;
import transaction.pdf.util.ScreenSpecFetch;


public class Fragment_bookmark_Adapter extends RecyclerView.Adapter<Fragment_bookmark_Adapter .MyViewHolder>{
	public Context context;
	private LayoutInflater inflate;
	 ClickListener clickListener;
	 LongClickListener longClickListener;
	 int i = 0;
	 int itemSize;
	int padding;
	List<Fragment_bookmark_single_item> data = Collections.emptyList();
	int orientation;
	Bitmap placeHolderBitmap = null;
	int currentRowCount;
	               // override null pointer

	public class AsyDrawable extends BitmapDrawable {
		WeakReference<LoadImageAsyncBookmarkPdfThumb> taskRefer ;

		AsyDrawable(Resources res,
					Bitmap bmapPlace,
					LoadImageAsyncBookmarkPdfThumb taskRef){
			super(res,bmapPlace);

			taskRefer = new WeakReference<LoadImageAsyncBookmarkPdfThumb>(taskRef  );
		}
		public LoadImageAsyncBookmarkPdfThumb getBitMapTask(){
			return taskRefer.get();
		}
	}

	public static boolean checkBitmapWorkerTask (File imagesFile,
												 ImageView imageView){
		LoadImageAsyncBookmarkPdfThumb bmWork = getBitmapWorkerTask(imageView);
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

	public static LoadImageAsyncBookmarkPdfThumb getBitmapWorkerTask(ImageView imageView) {
		Drawable drawable = imageView.getDrawable();
		if (drawable instanceof AsyDrawable) {
			AsyDrawable asyDrawable = (AsyDrawable) drawable;
			return asyDrawable.getBitMapTask();
		}
		return null;
	}


	public  Fragment_bookmark_Adapter(Context context, List<Fragment_bookmark_single_item> data, int itemSize, int currentRowCount){
		
		inflate = LayoutInflater.from(context);
		this.currentRowCount = currentRowCount;
		this.data = data;
		this.context = context;
		this.itemSize = itemSize;
		this.padding = (int) ScreenSpecFetch.convertDpToPixel(10,context);
		Log.e("ere","retert");
		int ORIENTATION_PORTRAIT = 1;
		this.orientation = context.getResources().getConfiguration().orientation;
//		if (this.orientation == ORIENTATION_PORTRAIT) {
			int reminder = (data.size())%currentRowCount;
			reminder=currentRowCount-reminder;
			Log.e("Dumm",reminder+"");
			if(reminder!=currentRowCount)
			for(int i=0; i<reminder;i++){
				//insert dummy values

				this.data.add(new Fragment_bookmark_single_item("null","","","",""));
			}

//		}else{
//			int reminder = (data.size())%4;
//			reminder=4-reminder;
//			if(reminder!=4)
//			for(int i=0; i<reminder;i++){
//				//insert dummy values
//				this.data.add(new Fragment_bookmark_single_item("null","","","",""));
//			}
//		}

		Log.e("DataSi1",data.size()+"");
	}
	
	
	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
//		Log.e("DataCount",data.size()+"");
		return data.size();

	}



	@Override
	public void onBindViewHolder(MyViewHolder holder, int arg1) {
		// TODO Auto-generated method stub
		Fragment_bookmark_single_item item = data.get(arg1);
		holder.iv_icon_text.getLayoutParams().height = (itemSize);
		holder.iv_icon_text.getLayoutParams().width = (itemSize);
		int ORIENTATION_PORTRAIT = 1;

//		if (this.orientation == ORIENTATION_PORTRAIT) {
			if (arg1 % currentRowCount == 0)
				holder.iv_shelf_border.setBackgroundResource(R.drawable.wooden_shelf_left);

			if (arg1 % currentRowCount == (currentRowCount-1))
				holder.iv_shelf_border.setBackgroundResource(R.drawable.wooden_shelf_right);
			else if (arg1 % currentRowCount != 0)	holder.iv_shelf_border.setBackgroundResource(R.drawable.wooden_shelf_middle);
//		}
//		} else {
//			if (arg1 % 4 == 0)
//				holder.iv_shelf_border.setBackgroundResource(R.drawable.wooden_shelf_left);
//			if (arg1 % 4 == 1)
//				holder.iv_shelf_border.setBackgroundResource(R.drawable.wooden_shelf_middle);
//			if (arg1 % 4 == 2)
//				holder.iv_shelf_border.setBackgroundResource(R.drawable.wooden_shelf_middle);
//			if (arg1 % 4 == 3)
//				holder.iv_shelf_border.setBackgroundResource(R.drawable.wooden_shelf_right);
//		}

		if(data.get(arg1).iconID.equalsIgnoreCase("null")){
			holder.cv.setVisibility(View.INVISIBLE);
//			holder.iv_shelf_border.setVisibility(View.VISIBLE);
		}else {
			holder.cv.setVisibility(View.VISIBLE);
			holder.tv_title.setText(item.title);

			String a = item.title.charAt(0) + "";
			a = a.toUpperCase();



			int sdk = android.os.Build.VERSION.SDK_INT;
			switch(i){
				case 0:  		TextDrawable drawable = TextDrawable.builder().buildRect(a,  Color.parseColor("#ff8a65"));
					holder.iv_icon_text.setImageDrawable(drawable);

					break;
				case 1:
					TextDrawable drawable2 = TextDrawable.builder().buildRect(a,  Color.parseColor("#795548"));
					holder.iv_icon_text.setImageDrawable(drawable2);
					break;
				case 2:   	TextDrawable drawable3 = TextDrawable.builder().buildRect(a,  Color.parseColor("#ba68c8"));
					holder.iv_icon_text.setImageDrawable(drawable3);
					break;
				case 3:   TextDrawable drawable4 = TextDrawable.builder().buildRect(a,  Color.parseColor("#f6bf26"));
					holder.iv_icon_text.setImageDrawable(drawable4);
					break;
				case 4:   TextDrawable drawable5 = TextDrawable.builder().buildRect(a, Color.parseColor("#7986cb"));
					holder.iv_icon_text.setImageDrawable(drawable5);
					break;

				case 5:   TextDrawable drawable6 = TextDrawable.builder().buildRect(a, Color.parseColor("#b39ddb"));
					holder.iv_icon_text.setImageDrawable(drawable6);
					break;
				case 6:     TextDrawable drawable7 = TextDrawable.builder().buildRect(a, Color.parseColor("#4db6ac"));
					holder.iv_icon_text.setImageDrawable(drawable7);
					break;
				case 7:     TextDrawable drawable8 = TextDrawable.builder().buildRect(a, Color.parseColor("#a1887f"));
					holder.iv_icon_text.setImageDrawable(drawable8);
					break;

				case 8:    TextDrawable drawable9 = TextDrawable.builder().buildRect(a, Color.parseColor("#78c8a2"));
					holder.iv_icon_text.setImageDrawable(drawable9);
					break;
				case 9:     TextDrawable drawable10 = TextDrawable.builder().buildRect(a, Color.parseColor("#9E9E9E"));
					holder.iv_icon_text.setImageDrawable(drawable10);
					break;
				default:     TextDrawable drawable11 = TextDrawable.builder().buildRect(a, Color.parseColor("#7986cb"));
					holder.iv_icon_text.setImageDrawable(drawable11);
					break;
			}
			i++;
			if (i > 9) i = 0;
		}
		try {
			Log.e("File",item.getPath().toString());
//			if(a.thumbOfFirstPage(200,300)!=null)holder.iv_icon_text.setImageBitmap(a.thumbOfFirstPage(200,300));
			String root = Environment.getExternalStorageDirectory().toString();
			File myDir = new File(root + "/."+context.getResources().getString(R.string.app_name).replace(" ",""));
			myDir.mkdirs();
			String fname = item.getPath().toString().replace("/","_")+".jpg";

			File file = null;
			file= new File (myDir, fname);
			Log.e("File--ed",file.exists()+"");

     if(file.exists())
	if(checkBitmapWorkerTask(file,
					holder.iv_icon_text)){


				LoadImageAsyncBookmarkPdfThumb bw = (LoadImageAsyncBookmarkPdfThumb) new LoadImageAsyncBookmarkPdfThumb(file,holder.iv_icon_text, itemSize,(itemSize*4)/3 );

				AsyDrawable asyDrawable = new AsyDrawable(holder.iv_icon_text.getResources(),
						placeHolderBitmap,
						bw);
				holder.iv_icon_text.setImageDrawable(asyDrawable);
				bw.execute();
//				bmWorks.add(bw);
			}

		} catch (Exception e) {
			Log.e("ERRR",e.toString());
		}

//		holder.tv_items.setText(item.items);
//		holder.tv_date.setText(item.date);
		
		
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		View view = inflate.inflate(R.layout.fragment_bookmark_single_item1, arg0, false);
		MyViewHolder holder = new MyViewHolder(view);
		
		return holder;
	}

	class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener{

		TextView tv_title;
//		TextView tv_items;
		ImageView iv_icon_text;
		ImageView iv_shelf_border;
		CardView cv;
		//ImageView iv_icon;
//		TextView tv_date;
		
		public MyViewHolder(View view) {
			super(view);
			// TODO Auto-generated constructor stub

			cv = (CardView) view.findViewById(R.id.cardView);
			tv_title = (TextView) view.findViewById(R.id.tv_adapter_energy_title);
			//iv_icon = (ImageView) view.findViewById(R.id.iv_adapter_icon);
//			tv_items= (TextView) view.findViewById(R.id.tv_items);
//			tv_date = (TextView) view.findViewById(R.id.tv_date);
			iv_shelf_border = (ImageView) view.findViewById(R.id.imageView244);
			iv_icon_text = (ImageView) view.findViewById(R.id.tv_icon);
			
//			Typeface tf = Typeface.createFromAsset(context.getAssets(),
//			        "fonts/Roboto-Medium.ttf");
//			tv_title.setTypeface(tf);
			view.setOnLongClickListener(this);
			view.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
//			Toast.makeText(context, "Item " + getPosition() , Toast.LENGTH_LONG).show();
			//context.startActivity(new Intent(context, SettingsActivty.class));
			// Don't take click directly from here plz
			if(clickListener!=null && (!data.get(getPosition()).iconID.equalsIgnoreCase("null")) ){
				
				clickListener.itemClicked(v, getPosition());
			}
			
		}

		@Override
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
         if(longClickListener!=null  && (!data.get(getPosition()).iconID.equalsIgnoreCase("null")) ){
				
				longClickListener.itemLongClicked(v, getPosition());
			}
			return true;
		}


		
	}
	
	
	public void setClickListener(ClickListener clickListener){
		this.clickListener = clickListener;
	}
	
	public void setLongClickListener(LongClickListener longClickListener) {
		this.longClickListener = longClickListener;
	}

	// Taking click on adapter is not good
	// so here we use interface help we to communicate click item to drawer.
	public interface ClickListener{
		public void itemClicked(View view, int position);
		
	}
	
	public interface LongClickListener{
		public void itemLongClicked(View view, int position);
		
	}
	

}
