package com.artifex.mupdfdemo;



import com.pdf.reader.pro.ebin.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

class Hggh extends Activity {
	
	public String returnSharedp(){
		
		SharedPreferences setting = getSharedPreferences("settings3", 0);                //retrieve saved value;
		String sss = setting.getString("bg_colour_main", "bteal");                //default 4       	
		return sss;
		
	}
}


public class OutlineAdapter extends BaseAdapter {
	private final OutlineItem    mItems[];
	private final LayoutInflater mInflater;
	public OutlineAdapter(LayoutInflater inflater, OutlineItem items[]) {
		mInflater = inflater;
		mItems    = items;
	}

	public int getCount() {
		return mItems.length;
	}

	public Object getItem(int arg0) {
		return null;
	}

	public long getItemId(int arg0) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v;
		if (convertView == null) {
			v = mInflater.inflate(R.layout.pdet_outline_entry, null);

			
		} else {
			v = convertView;
		}
		
		
		
	
		
		RelativeLayout Relative_L_index_pdf= (RelativeLayout) v.findViewById(R.id.Relative_L_index_pdf);
	
		
	
//Hggh ff =  new Hggh();
	//	String sss =ff.returnSharedp();
	//	int nn = obb.get_main_linear_layout_bg(sss);
	//	Relative_L_index_pdf.setBackgroundColor(R.color.Cyan); //gradient all effect work

		
		
		
		
		
		
		
		int level = mItems[position].level;
		if (level > 8) level = 8;
		String space = "";
		for (int i=0; i<level;i++)
			space += "   ";
		((TextView)v.findViewById(R.id.title)).setText(space+mItems[position].title);
		((TextView)v.findViewById(R.id.page)).setText(String.valueOf(mItems[position].page+1));

		return v;
	}

}
