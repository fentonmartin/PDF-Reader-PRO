package comvc.example.pdfviewerlite;

import java.util.List;

import com.pdf.reader.pro.ebin.R;




import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class Adapter_saerch_result extends ArrayAdapter<Item1>{

	Context context;
	TextView name, path, size;
	ImageView icon;
	List<Item1> result;
	
	public Adapter_saerch_result(Context context, int resource,List<Item1> result) {
		super(context, resource, result);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.result = result;
	}


@Override
public View getView(int position, View convertView, ViewGroup parent) {
	// TODO Auto-generated method stub
	
	LayoutInflater la = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	View v = la.inflate(R.layout.listview_search, null);
    Item1 o = result.get(position);
	

	name = (TextView) v.findViewById(R.id.textView_saerch_name);
	path = (TextView) v.findViewById(R.id.textView2_search_path);
	size = (TextView) v.findViewById(R.id.textView3_search_size);
	icon = (ImageView) v.findViewById(R.id.imageView1_search_icon);
	
	name.setText(o.getName());
	path.setText(o.getPath());
   size.setText(o.getData());
   


	String uri = "drawable/" + "a_pdf";
	int imageResource = context.getResources().getIdentifier(uri, null,context.getPackageName());
	Drawable image = context.getResources().getDrawable(imageResource);
icon.setImageDrawable(image);
	

if((o.getName()=="Sorry, No match found")){
	icon.setVisibility(View.INVISIBLE);
}
	
	
	
	return v;
}
	
	
	
}