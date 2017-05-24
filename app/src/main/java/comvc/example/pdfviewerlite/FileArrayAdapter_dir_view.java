package comvc.example.pdfviewerlite;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pdf.reader.pro.ebin.R;

public class FileArrayAdapter_dir_view extends ArrayAdapter<Item1> {


 Context c;
 int id;
 List<Item1> items;
	
   public FileArrayAdapter_dir_view(Context context, int textViewResourceId,List<Item1> objects) {
		
	   super(context,textViewResourceId,objects);//////////////////////////////////////////////
		c = context;
		id = textViewResourceId;
		items = objects;
	}

	
	
	
	
	
	
	
	
	public View getView(int position, View convertView, ViewGroup parent) {
               View v = convertView;
         //      if (v == null) {
                   LayoutInflater vi = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                   v = vi.inflate(R.layout.fragment_file_browser_single_element, null);
           //    }
               
               /* create a new view of my layout and inflate it in the row */
       		//convertView = ( RelativeLayout ) inflater.inflate( resource, null );
       		
             Item1 o = items.get(position);
               if (o != null) {
                       TextView t1 = (TextView) v.findViewById(R.id.tv_adapter_energy_title);
                       TextView t2 = (TextView) v.findViewById(R.id.tv_items);
                       TextView t3 = (TextView) v.findViewById(R.id.tv_date);
                       /* Take the ImageView from layout and set the city's image */
              			ImageView imageCity = (ImageView) v.findViewById(R.id.iv_adapter_icon);          
	              
	               		
	               		if(o.getImage()!="no"){	

	               		String uri = "drawable/" + o.getImage();
	               	    int imageResource = c.getResources().getIdentifier(uri, null, c.getPackageName());
	               	    Drawable image = c.getResources().getDrawable(imageResource);
	               	   imageCity.setImageDrawable(image);
	               	}else{
	               		imageCity.setVisibility(View.INVISIBLE);
	               	}
	               	    
	               	 
                       
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
