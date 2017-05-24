package comvc.ebtabsss.tabs;

import java.io.File;
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
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import transaction.pdf.util.ScreenSpecFetch;

public class Fragment_bookmark extends Fragment implements OnRefreshListener, Fragment_bookmark_Adapter.ClickListener, Fragment_bookmark_Adapter.LongClickListener {

	
	SQLiteDatabase myDB ;
	List<Fragment_bookmark_single_item> s_fils = new ArrayList<Fragment_bookmark_single_item>();
	Fragment_bookmark_Adapter  are_search_result   ;

	View rootView ;
	RecyclerView rv_bookmark;
	SwipeRefreshLayout sr_layout; 
	TextView tv_no_bookmark;
	int itemSize =0;
	int currentRowCount=3;
	SharedPreferences p;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		 rootView = inflater.inflate(R.layout.fragment_bookmark, container, false);
		 rv_bookmark = (RecyclerView) rootView.findViewById(R.id.frag_nav_drawer_recycler_bookmark);
		 sr_layout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout_bookmark);
		 tv_no_bookmark = (TextView) rootView.findViewById(R.id.tv_bookmark_not_found);
		 rv_bookmark.setLayoutManager(new LinearLayoutManager(getActivity()));
		rv_bookmark.setHasFixedSize(true);


		p = PreferenceManager.getDefaultSharedPreferences(getActivity());

		 sr_layout.setOnRefreshListener(this);
		 myDB = getActivity().openOrCreateDatabase("myDB1", this.getActivity().MODE_PRIVATE, null);
//		 setupRecyclerProperty();

		 
		 loadDetails();
   

        
		return rootView;
	}

	private void setupRecyclerProperty() {
		//		Log.e("wwwww",);
		int rowPortrait = Integer.valueOf(p.getString("listPref_bookmark_p","4"));
		int rowLandscape =  Integer.valueOf(p.getString("listPref_bookmark_l","6"));;
		int ORIENTATION_PORTRAIT = 1;
		if (getActivity().getResources().getConfiguration().orientation == ORIENTATION_PORTRAIT) {
			rv_bookmark.setLayoutManager(new GridLayoutManager(getActivity(),rowPortrait, GridLayoutManager.VERTICAL,false));
//			rv_bookmark.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));
			itemSize = (int) ((ScreenSpecFetch.calculateScreenWidth(getActivity()) - ScreenSpecFetch.convertDpToPixel((rowPortrait*8),getActivity())) / rowPortrait);
			currentRowCount = rowPortrait;
		} else {
			rv_bookmark.setLayoutManager(new GridLayoutManager(getActivity(),rowLandscape, GridLayoutManager.VERTICAL,false));
			itemSize = (int) ((ScreenSpecFetch.calculateScreenWidth(getActivity()) - ScreenSpecFetch.convertDpToPixel((rowLandscape*8),getActivity())) / rowLandscape);
			currentRowCount = rowLandscape;
		}

	}


	public void loadDetails(){
		setupRecyclerProperty();
		  myDB = getActivity().openOrCreateDatabase("myDB1", this.getActivity().MODE_PRIVATE, null);
		  myDB.execSQL("create table if not exists PDFBOOK( image VARCHAR, name VARCHAR, data VARCHAR, date VARCHAR, path VARCHAR UNIQUE,PRIMARY KEY(path));");
		
			are_search_result = null;
	        Cursor resultSet = myDB.rawQuery("select * from PDFBOOK", null);
	        	s_fils.clear();
	        	Fragment_bookmark_single_item resa;
	        	
	        	if(resultSet.moveToFirst()){
	           if(new File(resultSet.getString(4)).isFile())
	        	 {resa= new Fragment_bookmark_single_item(resultSet.getString(0),resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4));
	             s_fils.add(resa);}else{myDB.execSQL("delete from PDFBOOK where path='"+resultSet.getString(4)+"';");}
	        	while(resultSet.moveToNext()){
	        		           if(new File(resultSet.getString(4)).isFile()){
	        		        	   resa= new Fragment_bookmark_single_item(resultSet.getString(0),resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4));
		        		           s_fils.add(resa); 
	        		           }else{myDB.execSQL("delete from PDFBOOK where path='"+resultSet.getString(4)+"';");} 
	                           }
	        	if(s_fils.size()>=0){
                 are_search_result = new Fragment_bookmark_Adapter(getActivity(), s_fils, itemSize, currentRowCount);
				 rv_bookmark.setAdapter(are_search_result);
				 if(tv_no_bookmark.isShown())tv_no_bookmark.setVisibility(View.GONE);
	        	               }
	        	}else{
					are_search_result = new Fragment_bookmark_Adapter(getActivity(), s_fils , itemSize, currentRowCount);
					rv_bookmark.setAdapter(are_search_result);
				}
              if(are_search_result!=null){       
	    		 are_search_result.setClickListener(this);
	    		 are_search_result.setLongClickListener(this);
                 }
              if(are_search_result==null || s_fils.size()==0){
	        	
	        	if(!tv_no_bookmark.isShown())tv_no_bookmark.setVisibility(View.VISIBLE);
	        	
	        }
	}
	
	public void reloadBookmarks(){
		loadDetails();
	}

	@Override
	public void onResume() {
		super.onResume();
		reloadBookmarks();
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		loadDetails();
		if(sr_layout.isRefreshing()){
			sr_layout.setRefreshing(false);
		}
	}


	@Override
	public void itemLongClicked(View view, int position) {
		// TODO Auto-generated method stub

		
		
		 final Fragment_bookmark_single_item o = s_fils.get(position);
		 AlertDialog.Builder ad_m = new AlertDialog.Builder(getActivity(),R.style.AppCompatAlertDialogStyle);
	    CharSequence[] items = {"Open","Open by System","Open by Other", "Share","Remove Bookmark"};
		
		
		
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
	    	
	    	
	            	case 4 :
	            		myDB.execSQL("delete from PDFBOOK where path='"+o.getPath().replace("'","''")+"';");
						reloadBookmarks();
	            		
	            		break;
	                      }
	                           
	            
	                      }
	          });ad_m.show();

	
	}


	@Override
	public void itemClicked(View view, int position) {
	//	String s = String.valueOf(position);
    //	Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
	Fragment_bookmark_single_item o = null;
    try{
	    o = s_fils.get(position); ////////////////////////////////////////////////////// 11111111111111111111111111111111111
        if(new File(o.getPath()).isFile()){	
		  String pdf_path = o.getPath();
		  Uri uri = Uri.parse(pdf_path);
		  Intent intent = new Intent(getActivity().getApplicationContext(),MuPDFActivity.class);
		  intent.setAction(Intent.ACTION_VIEW);
		  intent.setData(uri);
		  startActivity(intent);
	  }else{
			Toast.makeText(getActivity(), "File not Found", Toast.LENGTH_LONG).show();
			loadDetails();
			if(sr_layout.isRefreshing()){
				sr_layout.setRefreshing(false);
			}
	  }
		
	
}catch(Exception cfvg){}
	
	}
}
