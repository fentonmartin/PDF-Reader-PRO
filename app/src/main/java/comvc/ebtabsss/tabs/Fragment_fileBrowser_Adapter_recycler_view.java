package comvc.ebtabsss.tabs;

import java.util.Collections;
import java.util.List;

import com.pdf.reader.pro.ebin.R;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class Fragment_fileBrowser_Adapter_recycler_view extends RecyclerView.Adapter<Fragment_fileBrowser_Adapter_recycler_view .MyViewHolder>{
	public Context context;
	private LayoutInflater inflate;
	 ClickListener clickListener;
	 LongClickListener longClickListener;
	  int i = 0;
	List<Fragment_fileBrowser_single_item> data = Collections.emptyList();
	               // override null pointer
	
	public Fragment_fileBrowser_Adapter_recycler_view(Context context, List<Fragment_fileBrowser_single_item> data){
		
		inflate = LayoutInflater.from(context);
		this.data = data;
		this.context = context;
	}
	
	
	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int arg1) {
		// TODO Auto-generated method stub
		
		Fragment_fileBrowser_single_item item = data.get(arg1);
		holder.tv_title.setText(item.title);
		
		if(item.iconID.equals("directory_icon"))
		holder.iv_icon.setImageResource(R.drawable.directory_icon);
		else if(item.iconID.equals("a_pdf"))
		holder.iv_icon.setImageResource(R.drawable.a_pdf);	
		else if(item.iconID.equals("directory_up"))
	    holder.iv_icon.setImageResource(R.drawable.ic_arrow_back_white_48dp);
		holder.tv_items.setText(item.items);
		holder.tv_date.setText(item.date);
		
		switch(i){
		case 0: holder.iv_icon.setColorFilter(Color.argb(255,233, 30, 99)); 
		        holder.tv_date.setTextColor(Color.argb(255,233, 30, 99));
		        break;
		case 1:holder.iv_icon.setColorFilter(Color.argb(255, 156, 39, 176));
		       holder.tv_date.setTextColor(Color.argb(255, 156, 39, 176));
		       break;
		case 2:holder.iv_icon.setColorFilter(Color.argb(255, 96, 125, 139)); 
		       holder.tv_date.setTextColor(Color.argb(255, 96, 125, 139)); 
		       break;
		case 3:holder.iv_icon.setColorFilter(Color.argb(255, 255, 193, 7));  
		       holder.tv_date.setTextColor(Color.argb(255, 255, 193, 7));
		       break;
		case 4:holder.iv_icon.setColorFilter(Color.argb(255, 0, 150, 136));  
		       holder.tv_date.setTextColor(Color.argb(255, 0, 150, 136));  
		       break;
		
		case 5:holder.iv_icon.setColorFilter(Color.argb(255, 121, 85, 72));  
		       holder.tv_date.setTextColor(Color.argb(255, 121, 85, 72));  
		       break;
		case 6:holder.iv_icon.setColorFilter(Color.argb(255, 3, 169, 244));  
	           holder.tv_date.setTextColor(Color.argb(255, 3, 169, 244));  
		       break;
		case 7:holder.iv_icon.setColorFilter(Color.argb(255, 255, 87, 34)); 
		       holder.tv_date.setTextColor(Color.argb(255, 255, 87, 34)); 
		       break;
		
		case 8:holder.iv_icon.setColorFilter(Color.argb(255, 76, 175, 80));  
			   holder.tv_date.setTextColor(Color.argb(255, 76, 175, 80)); 
		      break;
		case 9:holder.iv_icon.setColorFilter(Color.argb(255, 33, 150, 243));  
		       holder.tv_date.setTextColor(Color.argb(255, 33, 150, 243)); 
		       break;
		case 10:holder.iv_icon.setColorFilter(Color.argb(255, 63, 81, 181));   
		        holder.tv_date.setTextColor(Color.argb(255, 63, 81, 181));
		        break;
		default: holder.iv_icon.setColorFilter(Color.argb(255,233, 30, 99)); 
		         holder.tv_date.setTextColor(Color.argb(255,233, 30, 99)); 
		         break;
		} 
		i++;
		if(i>10) i=0;
		
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		View view = inflate.inflate(R.layout.fragment_file_browser_single_element, arg0, false);
		MyViewHolder holder = new MyViewHolder(view);
		
		return holder;
	}

	class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener{

		TextView tv_title;
		TextView tv_items;
		ImageView iv_icon;
		TextView tv_date;
		
		public MyViewHolder(View view) {
			super(view);
			// TODO Auto-generated constructor stub
			tv_title = (TextView) view.findViewById(R.id.tv_adapter_energy_title);
			iv_icon = (ImageView) view.findViewById(R.id.iv_adapter_icon);
			tv_items= (TextView) view.findViewById(R.id.tv_items);
			tv_date = (TextView) view.findViewById(R.id.tv_date);
			
//			Typeface tf = Typeface.createFromAsset(context.getAssets(),
//			        "fonts/Roboto-Medium.ttf");
//			tv_title.setTypeface(tf);
			view.setOnLongClickListener(this);
			view.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//Toast.makeText(context, "Item " + getPosition() , Toast.LENGTH_LONG).show();
			//context.startActivity(new Intent(context, SettingsActivty.class));
			// Don't take click directly from here plz
			if(clickListener!=null){
				
				clickListener.itemClicked(v, getPosition());
			}
			
		}

		@Override
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
         if(longClickListener!=null){
				
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
