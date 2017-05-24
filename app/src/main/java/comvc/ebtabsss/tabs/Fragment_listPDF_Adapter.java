package comvc.ebtabsss.tabs;

import java.util.Collections;
import java.util.List;

import com.amulyakhare.textdrawable.TextDrawable;
import com.pdf.reader.pro.ebin.R;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class Fragment_listPDF_Adapter extends RecyclerView.Adapter<Fragment_listPDF_Adapter.MyViewHolder> {

	public Context context;
	private LayoutInflater inflate;
	ClickListener clickListener;
    LongClickListener longClickListener;
	int i = 0;
	
	List<Fragment_listPDF_SingleItem> data = Collections.emptyList();
	
	public Fragment_listPDF_Adapter(Context context, List<Fragment_listPDF_SingleItem> data){
		
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
		
		Fragment_listPDF_SingleItem item = data.get(arg1);
		holder.tv_title.setText(item.title);

		String a = item.title.charAt(0)+"";
		a = a.toUpperCase();

//		holder.tv_icon_text.setText(a);
//		if(item.iconID.equals("directory_icon"))
//		holder.iv_icon.setImageResource(R.drawable.directory_icon);
//		else if(item.iconID.equals("a_pdf"))
//		holder.iv_icon.setImageResource(R.drawable.a_pdf);	
//		else if(item.iconID.equals("directory_up"))
//	    holder.iv_icon.setImageResource(R.drawable.directory_up);	
		 int sdk = android.os.Build.VERSION.SDK_INT;
		switch(i){
			case 0:  		TextDrawable drawable = TextDrawable.builder().buildRound(a,  Color.parseColor("#ff8a65"));
				holder.iv_icon_text.setImageDrawable(drawable);

				break;
			case 1:
				TextDrawable drawable2 = TextDrawable.builder().buildRound(a,  Color.parseColor("#795548"));
				holder.iv_icon_text.setImageDrawable(drawable2);
				break;
			case 2:   	TextDrawable drawable3 = TextDrawable.builder().buildRound(a,  Color.parseColor("#ba68c8"));
				holder.iv_icon_text.setImageDrawable(drawable3);
				break;
			case 3:   TextDrawable drawable4 = TextDrawable.builder().buildRound(a,  Color.parseColor("#f6bf26"));
				holder.iv_icon_text.setImageDrawable(drawable4);
				break;
			case 4:   TextDrawable drawable5 = TextDrawable.builder().buildRound(a, Color.parseColor("#7986cb"));
				holder.iv_icon_text.setImageDrawable(drawable5);
				break;

			case 5:   TextDrawable drawable6 = TextDrawable.builder().buildRound(a, Color.parseColor("#b39ddb"));
				holder.iv_icon_text.setImageDrawable(drawable6);
				break;
			case 6:     TextDrawable drawable7 = TextDrawable.builder().buildRound(a, Color.parseColor("#4db6ac"));
				holder.iv_icon_text.setImageDrawable(drawable7);
				break;
			case 7:     TextDrawable drawable8 = TextDrawable.builder().buildRound(a, Color.parseColor("#a1887f"));
				holder.iv_icon_text.setImageDrawable(drawable8);
				break;

			case 8:    TextDrawable drawable9 = TextDrawable.builder().buildRound(a, Color.parseColor("#78c8a2"));
				holder.iv_icon_text.setImageDrawable(drawable9);
				break;
			case 9:     TextDrawable drawable10 = TextDrawable.builder().buildRound(a, Color.parseColor("#9E9E9E"));
				holder.iv_icon_text.setImageDrawable(drawable10);
				break;
			default:     TextDrawable drawable11 = TextDrawable.builder().buildRound(a, Color.parseColor("#7986cb"));
				holder.iv_icon_text.setImageDrawable(drawable11);
				break;
		}
		i++;
		if(i>9) i=0;
		
		
		holder.tv_items.setText(item.items);
		holder.tv_date.setText(item.date);
		
		
	}
	
	
	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		View view = inflate.inflate(R.layout.fragmen_list_pdf_single_item, arg0, false);
		MyViewHolder holder = new MyViewHolder(view);
		
		return holder;
	}

	class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener{

		TextView tv_title;
		TextView tv_items;
		ImageView iv_icon_text;
		TextView tv_date;
		
		public MyViewHolder(View view) {
			super(view);
			// TODO Auto-generated constructor stub
			tv_title = (TextView) view.findViewById(R.id.tv_adapter_energy_title);
			iv_icon_text = (ImageView) view.findViewById(R.id.iv_icon_text);


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
