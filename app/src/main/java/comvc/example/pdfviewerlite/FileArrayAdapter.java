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

 
public class FileArrayAdapter extends ArrayAdapter<Item1>{

	private Context c;
	private int id;
	private List<Item1>items;
	
	public FileArrayAdapter(Context context, int textViewResourceId,List<Item1> objects) {
		super(context, textViewResourceId, objects);
		c = context;
		id = textViewResourceId;
		items = objects;
	}
	public Item1 getItem(int i)
	 {
		 return items.get(i);
	 }

	
	
	
	
	
	
	
	
	
	@Override
       public View getView(int position, View convertView, ViewGroup parent) {
               View v = convertView;
         //      if (v == null) {
                   LayoutInflater vi = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                   v = vi.inflate(R.layout.fragment_file_browser_single_element, null);
           //    }
               
               /* create a new view of my layout and inflate it in the row */
       		//convertView = ( RelativeLayout ) inflater.inflate( resource, null );
       		
               final Item1 o = items.get(position);
               if (o != null) {
                       TextView t1 = (TextView) v.findViewById(R.id.tv_adapter_energy_title);
                       TextView t2 = (TextView) v.findViewById(R.id.tv_items);
                       TextView t3 = (TextView) v.findViewById(R.id.tv_date);
                       /* Take the ImageView from layout and set the city's image */
	               		ImageView imageCity = (ImageView) v.findViewById(R.id.iv_adapter_icon);
	               		String uri = "drawable/" + o.getImage();
	               	    int imageResource = c.getResources().getIdentifier(uri, null, c.getPackageName());
	               	    Drawable image = c.getResources().getDrawable(imageResource);
	               	    imageCity.setImageDrawable(image);
                       
                       if(t1!=null)
                       	t1.setText(o.getName());
                       if(t2!=null)
                          	t2.setText(o.getData());
                       if(t3!=null)
                          	t3.setText(o.getDate());
                       
               }
               return v;
       }

}
