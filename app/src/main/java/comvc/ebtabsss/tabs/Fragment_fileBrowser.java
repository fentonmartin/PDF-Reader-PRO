package comvc.ebtabsss.tabs;

import java.io.File;
import java.sql.Date;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import com.artifex.mupdfdemo.MuPDFActivity;

import com.pdf.reader.pro.ebin.R;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class Fragment_fileBrowser extends Fragment implements Fragment_fileBrowser_Adapter_recycler_view.LongClickListener,Fragment_fileBrowser_Adapter_recycler_view.ClickListener, OnRefreshListener{

	File CurrentLocation;
	SQLiteDatabase myDB ;
	View rootView ;
	private File currentDir;
    ListView lv;
    List<Fragment_fileBrowser_single_item>dir = new ArrayList<Fragment_fileBrowser_single_item>();
    boolean hiddd_f  = false;
    boolean dir_rem=false;		
    EditText edit_pdf;
    boolean search_mode = false;
    List<Fragment_fileBrowser_single_item>   match_current_result ;
    Communicate_to_frag_Bookmark mCommunicate_to_frag_Bookmark;
    
SharedPreferences setting ;          //retrieve saved value;            //retrieve saved value;
SharedPreferences.Editor editor;
private Fragment_fileBrowser_Adapter_recycler_view adapter;
private RecyclerView frag_nav_drawer_recycler_view_file_browser;
SwipeRefreshLayout sr_layout;
Fragment_fileBrowser c = this;	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		match_current_result = new ArrayList<Fragment_fileBrowser_single_item> ();
		myDB = getActivity().openOrCreateDatabase("myDB1", this.getActivity().MODE_PRIVATE, null);
		myDB.execSQL("create table if not exists PDFBOOK( image VARCHAR,name VARCHAR, data VARCHAR, date VARCHAR, path VARCHAR);");
		
		
		rootView = inflater.inflate(R.layout.fragment_file_browser, null, false);
		
		frag_nav_drawer_recycler_view_file_browser = (RecyclerView) rootView.findViewById(R.id.frag_nav_drawer_recycler_view_file_browser);
		sr_layout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout_file_browser);
        sr_layout.setOnRefreshListener(this);
		

        setting=PreferenceManager.getDefaultSharedPreferences(getActivity());  
       	editor = setting.edit();
       	
       	dir_rem = setting.getBoolean("directory_remember",false);   
       	


		loadAll();


		
		return rootView;


	}

	public void loadAll() {

		String dir_last ="NULL";
		if(dir_rem==true){
			dir_last = setting.getString("directory_last","NULL");
		}

		if(dir_last.equalsIgnoreCase("NULL")){
			dir_last = Environment.getExternalStorageDirectory().getPath();
		}
		currentDir=new File(dir_last);
		match_current_result = getData(new  File(dir_last));
		adapter = new Fragment_fileBrowser_Adapter_recycler_view(getActivity(), match_current_result);
		frag_nav_drawer_recycler_view_file_browser.setAdapter(adapter);
		frag_nav_drawer_recycler_view_file_browser.setLayoutManager(new LinearLayoutManager(getActivity()));
		adapter.setClickListener(this);
		adapter.setLongClickListener(this);
	}


	private List<Fragment_fileBrowser_single_item> getData(File f)
	    {
	    	
	  	if(!(f.isDirectory())){
	    		f=new File(Environment.getExternalStorageDirectory().getPath());
	  	                      }
	    	
	editor.putString("directory_last", f.toString());   /// Saved for next restart
    CurrentLocation = f;
	editor.commit();
	        
	        
	    	File[]dirs = f.listFiles(); 
			 dir = new ArrayList<Fragment_fileBrowser_single_item>();
			 List<Fragment_fileBrowser_single_item>fls = new ArrayList<Fragment_fileBrowser_single_item>();
			 try{
				 for(File ff: dirs)
				 { 
					Date lastModDate = new Date(ff.lastModified()); 
					DateFormat formater = DateFormat.getDateInstance();
					String date_modify = formater.format(lastModDate);
					if(ff.isDirectory()){
						
						
						File[] fbuf = ff.listFiles(); 
						int buf = 0;
						if(fbuf != null){ 
							buf = fbuf.length;
						} 
						else buf = 0; 
						String num_item = String.valueOf(buf);
						if(buf == 0) num_item = num_item + " item";
						else num_item = num_item + " items";
						
						//String formated = lastModDate.toString();
						dir.add(new Fragment_fileBrowser_single_item("directory_icon",ff.getName(),num_item,date_modify,ff.getAbsolutePath())); 
											}
					else
					{
						
						String[] it=ff.getName().toString().split("\\.");
						String ext=it[it.length-1].toString();
				//		Toast.makeText(getApplicationContext(), ext, Toast.LENGTH_LONG).show();
						
						if(ext.toUpperCase().equals("PDF")){
	                    hiddd_f = setting.getBoolean("hidden_file_chk",false);   			
							if(hiddd_f==false){
								
							                	if(ff.getName().charAt(0)=='.'){
						                        
							                     	}else{ fls.add(new Fragment_fileBrowser_single_item("a_pdf",ff.getName(),ff.length() + " Byte", date_modify, ff.getAbsolutePath()));}
							}else{
								fls.add(new Fragment_fileBrowser_single_item("a_pdf",ff.getName(),ff.length() + " Byte", date_modify, ff.getAbsolutePath()));
							}
						
						}
					}
				 }
			 }catch(Exception e)
			 {    
				 
			 }
//			 Collections.sort(dir).;
//			 Collections.sort(fls);
			 dir.addAll(fls);
			 if(!f.getPath().equalsIgnoreCase("/")||f.equals(Environment.getRootDirectory()))
				 dir.add(0,new Fragment_fileBrowser_single_item("directory_up","..","Parent Directory","",f.getParent()));
			 
			 return dir;
			 
	    }


	@Override
	public void itemClicked(View view, int position) {
		// TODO Auto-generated method stub
		Fragment_fileBrowser_single_item o ;
	        o= match_current_result.get(position);

			
			if(o.getImage().equalsIgnoreCase("directory_icon")||o.getImage().equalsIgnoreCase("directory_up")){
					currentDir = new File(o.getPath());
					match_current_result = getData(currentDir);
					  adapter = new Fragment_fileBrowser_Adapter_recycler_view(getActivity(), getData(currentDir));
					  frag_nav_drawer_recycler_view_file_browser.setAdapter(adapter);
					frag_nav_drawer_recycler_view_file_browser.setLayoutManager(new LinearLayoutManager(getActivity()));
			          adapter.setClickListener(this);
			          adapter.setLongClickListener(this);
					//adapter.notifyDataSetChanged();
			       //frag_nav_drawer_recycler_view_file_browser.setAdapter(adapter);
			}
			else
			{
		
				
			    String pdf_path = o.getPath();
						Uri uri = Uri.parse(pdf_path);
						Intent intent = new Intent(rootView.getContext(),MuPDFActivity.class);
						intent.setAction(Intent.ACTION_VIEW);
						intent.setData(uri);
						startActivity(intent);
			}	
	}


	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		//Toast.makeText(getActivity(), "l", Toast.LENGTH_LONG).show();
		if(CurrentLocation.isDirectory()){
			match_current_result = getData(CurrentLocation);
			  adapter = new Fragment_fileBrowser_Adapter_recycler_view(getActivity(), getData(currentDir));
			  frag_nav_drawer_recycler_view_file_browser.setAdapter(adapter);
			frag_nav_drawer_recycler_view_file_browser.setLayoutManager(new LinearLayoutManager(getActivity()));
	       if(sr_layout.isRefreshing()){
				sr_layout.setRefreshing(false);
			}
	       adapter.setClickListener(c);
	       adapter.setLongClickListener(c);
		
		}
		
	}


	@Override
	public void itemLongClicked(View view, int position) {
		// TODO Auto-generated method stub
    final Fragment_fileBrowser_single_item o ;
	o= match_current_result.get(position);
	
	if(new File(o.getPath()).isFile()){
	
		AlertDialog.Builder ad_m = new AlertDialog.Builder(getActivity(),R.style.AppCompatAlertDialogStyle);
    	CharSequence[] items = {"Open","Open by System","Open by Other", "Share","Add to BookMarks","Delete"};



ad_m.setItems(items, new DialogInterface.OnClickListener() {
        

	   
	   public void onClick(DialogInterface dialog, int which) {
                   // TODO Auto-generated method stub
       
		   switch(which){
        	
	     	
		   case 0: 
			   String pdf_path = o.getPath();
			Uri uri33 = Uri.parse(pdf_path);
			Intent intent33 = new Intent(rootView.getContext(),MuPDFActivity.class);
			intent33.setAction(Intent.ACTION_VIEW);
			intent33.setData(uri33);
			startActivity(intent33);
			   
			   break;
        	case 1 :        // by sys 
                                      File file_sys=new File(o.getPath());
			Intent intent = new Intent(Intent.ACTION_VIEW);
			File file=new File(o.getPath());
			Uri uri = Uri.fromFile(file);
			intent.setDataAndType(uri, "application/pdf");
			 intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
			 startActivity(intent);
        		
        		             break;
        	case 2 :     // by other
        		              File file1=new File(o.getPath());
        	                   Uri uri1 = Uri.fromFile(file1);
        	                   Intent intent1= new Intent(Intent.ACTION_VIEW);
        	                   intent1.setDataAndType(uri1, "*/*");
        	                   intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
        	                  rootView.getContext().startActivity(intent1);
        		
        		
        		              break;
        	case 3:  //share
				try {
					File fileWithinMyDir = new File(o.getPath());
					Intent intentShareFile = new Intent(Intent.ACTION_SEND);
					if (fileWithinMyDir.exists()) {
						intentShareFile.setType("application/pdf");
						intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + fileWithinMyDir.toString()));
						Snackbar.make(getView(), "Select App to share...", Snackbar.LENGTH_LONG).show();
						intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
								"Sharing File...");
						intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");

						startActivity(Intent.createChooser(intentShareFile, "Share File"));
					}
				}catch (Exception exc){
					Snackbar.make(getView(), "Something went wrong while sharing.", Snackbar.LENGTH_LONG).show();
					Log.e("Share",exc.toString());
				}
        		
	break;
	
	
        	case 4 :  //bookmark
        		
        		

        		String name = 	o.getTitle().replace("'","''");
    			String data =o.getData().replace("'","''");
    		String date = 	o.getDate().replace("'","''");
    			String path = o.getPath().replace("'","''");
    			String image = o.getImage().replace("'","''");
    		try{
				Cursor r = myDB.rawQuery("select * from PDFBOOK"+ " where path  = '"+path+"' ", null);
				if(r.moveToFirst()){

					Snackbar.make(getView(), "Already Exist in Bookmark!", Snackbar.LENGTH_LONG).show();

				}else {
					myDB.execSQL("insert into PDFBOOK values('"+image+"','"+name+"','"+data+"','"+date+"','"+path+"')");

					Snackbar.make(getView(),"Bookmark Added to Bookmark Tab!", Snackbar.LENGTH_LONG).show();
					mCommunicate_to_frag_Bookmark.refreshBookList();//refresh bookmark
				}



    		}catch(Exception exc){

				Log.e("888",exc.toString());
				Snackbar.make(getView(), "Something went wrong!", Snackbar.LENGTH_LONG).show();
    		}
    		
        		
        		break;
        		
        		
        		
        	case 5:  //delete
        		
        		
         		
        		AlertDialog.Builder alert = new AlertDialog.Builder(getActivity(),R.style.AppCompatAlertDialogStyle);
                alert.setMessage("Are you Sure to Delete (Can't Undo)?");
	       alert .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {

	      		File f = new File(o.getPath());
                 if(!f.delete()) Toast.makeText(rootView.getContext(), "ACCESS DENIED !", Toast.LENGTH_LONG).show();
                
             	match_current_result = getData(CurrentLocation);
				  adapter = new Fragment_fileBrowser_Adapter_recycler_view(getActivity(), getData(currentDir));
				  frag_nav_drawer_recycler_view_file_browser.setAdapter(adapter);
				frag_nav_drawer_recycler_view_file_browser.setLayoutManager(new LinearLayoutManager(getActivity()));
		       if(sr_layout.isRefreshing()){
					sr_layout.setRefreshing(false);
				}
		       adapter.setClickListener(c);
		       adapter.setLongClickListener(c);
	                      }
	                            });
	    
	                     alert .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	                     public void onClick(DialogInterface dialog, int whichButton) {
	                        // Do nothing.
//	     					edit_pdf.setText("");
	                                  }
	                           }).show();
        		break;
        		
        		
        	
                  }
                       
        
                  }
      });ad_m.show();

	}
}

	public void set_Communicate_to_frag_Bookmark(Communicate_to_frag_Bookmark c){
		mCommunicate_to_frag_Bookmark =c;
	}

	
public interface Communicate_to_frag_Bookmark{
	public void refreshBookList();
}
	    

	 
}
