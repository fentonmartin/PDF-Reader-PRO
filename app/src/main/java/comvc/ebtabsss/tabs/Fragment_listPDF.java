package comvc.ebtabsss.tabs;

import java.io.File;
import java.sql.Date;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import com.artifex.mupdfdemo.MuPDFActivity;
import comvc.ebtabsss.tabs.Fragment_listPDF_Adapter.ClickListener;
import comvc.ebtabsss.tabs.Fragment_listPDF_Adapter.LongClickListener;
import comvc.example.pdfviewerlite.Dir_size_Human_Redable;

import com.pdf.reader.pro.ebin.R;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.TextView;
import android.widget.Toast;

public class Fragment_listPDF extends Fragment implements OnRefreshListener, ClickListener, LongClickListener {

	
	
	Fragment_listPDF c;
	SQLiteDatabase myDB ;
	SharedPreferences setting;
    String it[];
    Fragment_listPDF_SingleItem o;
	RecyclerView rv_search_pdf_result;
	File currentDir;
	List<Fragment_listPDF_SingleItem> s_fils = new ArrayList<Fragment_listPDF_SingleItem>();
	List<Fragment_listPDF_SingleItem> s_filsss = new ArrayList<Fragment_listPDF_SingleItem>();
	
	boolean search_mode = false;
    boolean runner =false;
	Fragment_listPDF_Adapter adapter;
	SwipeRefreshLayout sr_layout;
	TextView tv_pdf_not;
	Communicate_to_frag_Bookmark_fromListPDF mCommunicate_to_frag_Bookmark_fromListPDF;
	 SharedPreferences prefs ;
	
	@Override
	public View onCreateView(LayoutInflater inflater, final ViewGroup container,Bundle savedInstanceState) {
        
		final View rootView = inflater.inflate(R.layout.fragmen_list_pdf, container, false);
		myDB = getActivity().openOrCreateDatabase("myDB1", getActivity().MODE_PRIVATE, null);
		myDB.execSQL("create table if not exists PDFLIST( image VARCHAR,name VARCHAR, data VARCHAR, date VARCHAR, path VARCHAR);");
		
		prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		
		
		rv_search_pdf_result = (RecyclerView) rootView.findViewById(R.id.frag_nav_drawer_recycler_pdf_list);
        rv_search_pdf_result.setLayoutManager(new LinearLayoutManager(getActivity()));
		
		
		sr_layout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout_pdf_list);
        tv_pdf_not = (TextView) rootView.findViewById(R.id.tv_pdf_not_found);
       
		sr_layout.setOnRefreshListener(this);
	    c = this;   
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			

	        
	        /////////////// ACCEss from DB
	         
	        Cursor resultSet = myDB.rawQuery("select * from PDFLIST", null);
	        if(resultSet.getCount()>0){
                s_fils.clear();
	        	resultSet.moveToFirst();
	        	Fragment_listPDF_SingleItem resa;
                resa= new Fragment_listPDF_SingleItem(resultSet.getString(0),resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4));
                s_fils.add(resa);
	                   while(resultSet.moveToNext()){
	        	                resa= new Fragment_listPDF_SingleItem(resultSet.getString(0),resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4));
	        	                s_fils.add(resa);
	        	            	
	                           }
	                   
					  adapter = new Fragment_listPDF_Adapter(getActivity().getApplicationContext(), s_fils);
					  rv_search_pdf_result.setAdapter(adapter);
					  adapter.setClickListener(this);
					  adapter.setLongClickListener(this);
					  rv_search_pdf_result.setLayoutManager(new LinearLayoutManager(getActivity()));
                      //adapter.setClickListener(c);
			          //adapter.setLongClickListener(c);
   	    			  runner=false;
	        }else if(resultSet.getCount()<=0){
	        	
	        currentDir = new File(prefs.getString("home_location", Environment.getExternalStorageDirectory().toString()));
        	 Log.e("",""+currentDir.getPath());
	        if(!sr_layout.isRefreshing()){
		        sr_layout.post(new Runnable() {
			    @Override
			    public void run() {
			    	sr_layout.setRefreshing(true);
			    }
			});
	   }
		   
       new Asy_search_for_pdf().execute(currentDir);
       
	        }
       
		return rootView;
		
	}
	
	
	

	
	
	











class Asy_search_for_pdf extends AsyncTask<File, Fragment_listPDF_SingleItem, String> {

	
@Override
public void onPreExecute() {
	// TODO Auto-generated method stub
	super.onPreExecute();
	runner=true;

	s_filsss = new ArrayList<Fragment_listPDF_SingleItem>();
	s_filsss.clear();
}
	




@Override
	public String doInBackground(File... params) {
		// TODO Auto-generated method stub

	   final boolean  n = prefs.getBoolean("hidden_file_chk", false); 
		File f = params[0];
        Log.e("root",""+f.getPath());

	
	
    class Read_pdf_in_dir {
      
    	public void read_pdf(File f){
		  if(runner){
		  File[]dirs = f.listFiles(); 
		  try{
			for(File ff: dirs) {
		       if(ff.isFile()){
                String temp_name = ff.getName();
				String[] it=temp_name.split("\\.");
				String ext=it[it.length-1].toString();
		        if(ext.toUpperCase().equals("PDF")&&((ff.getName().toString().charAt(0)!='.')||n==true)){
					String file_icon_name = "a_pdf";
		            Date lastModDate = new Date(ff.lastModified()); 
	        	    DateFormat formater = DateFormat.getDateInstance();
					String date_modify = formater.format(lastModDate);
					Dir_size_Human_Redable newobj = new Dir_size_Human_Redable();
					long f_size= ff.length();
		        	String ss = newobj.humanReadableByteCount(f_size, false);         
		        	//fls.add(new Item(ff.getName(),ss, date_modify, ff.getAbsolutePath(),file_icon_name));
		        	Fragment_listPDF_SingleItem ooo = new Fragment_listPDF_SingleItem(file_icon_name,ff.getName(),ss, date_modify, ff.getAbsolutePath());
		        	//oo.onProgressUpdate(fls);
		        	publishProgress(ooo);

				}
		      }else{    read_pdf(ff);    }
			}
	
		  }catch(Exception exc){}
		}
	}
 }

	if(runner){
    Read_pdf_in_dir obj = new Read_pdf_in_dir();
    obj.read_pdf(f);
      }

	return null;
	
   }
	




@Override
	public void onProgressUpdate(Fragment_listPDF_SingleItem... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);

	s_filsss .add(values[0]);
	//s_fils.clear();
	//s_fils = s_filsss;	
	//s_fils.clear();
	//s_fils = s_filsss;		
              try{
            	  if(tv_pdf_not!=null)tv_pdf_not.setVisibility(View.GONE);
			     adapter = new Fragment_listPDF_Adapter(getActivity(), s_filsss);
				  rv_search_pdf_result.setAdapter(adapter);
				  adapter.setClickListener(c);
				  adapter.setLongClickListener(c);
				  rv_search_pdf_result.setLayoutManager(new LinearLayoutManager(getActivity()));  
				  if(tv_pdf_not.isShown())tv_pdf_not.setVisibility(View.GONE); //remove null view
			}catch(Exception f){} //user move from frag
	}


	public void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
		runner=false;
		  // if(sr_layout.isRefreshing())
			  // sr_layout.setRefreshing(false);
		try{
			   sr_layout.post(new Runnable() {
				    @Override
				    public void run() {
				    	sr_layout.setRefreshing(false);
				    }
				});
		}catch(Exception v){Log.e("ASY","Can't find ui");};  
		s_fils.clear();
		s_fils = s_filsss;		
int t;
myDB.execSQL("drop table PDFLIST");
myDB.execSQL("create table if not exists PDFLIST(image VARCHAR, name VARCHAR, data VARCHAR, date VARCHAR, path VARCHAR);");
		for (t=0;t<s_fils.size() ;t++)	
		{
		   String name = 	s_fils.get(t).getTitle().replace("'","''");
		   String data = s_fils.get(t).getData().replace("'","''");
		   String date = 	s_fils.get(t).getDate().replace("'","''");
		   String path = s_fils.get(t).getPath().replace("'","''");
		   String image = s_fils.get(t).getImage().replace("'","''");
			
			myDB.execSQL("insert into PDFLIST values('"+image+"','"+name+"','"+data+"','"+date+"','"+path+"')");
		}
		
		if(s_fils.isEmpty()){
			   Log.e("Empry","list");
			   try{
			     adapter = new Fragment_listPDF_Adapter(getActivity(), s_fils);
			     rv_search_pdf_result.setAdapter(adapter);
			     adapter.setClickListener(c);
				  adapter.setLongClickListener(c);
			     rv_search_pdf_result.setLayoutManager(new LinearLayoutManager(getActivity())); 
			     //rv_search_pdf_result.setVisibility(View.INVISIBLE);
			     tv_pdf_not.setVisibility(View.VISIBLE);
			     
			     getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
                   Snackbar.make(getView(), "No files found at current location, Please change search location at settings.",  Snackbar.LENGTH_LONG).show();
					}
				});
			     }catch(Exception d){
			    	 //user gone from fragment -hence look on that is not exit
			     }
		}else{
			Log.e("No-Empry","list"+s_fils.size());
			Log.e("No-Empry","list"+s_fils.get(0).path.toString());
			//rv_search_pdf_result.setVisibility(View.VISIBLE);
			if(tv_pdf_not!=null)tv_pdf_not.setVisibility(View.GONE);
		}
		runner=false;
 
	}
		
	
	

}

@Override
public void onPause() {
	// TODO Auto-generated method stub
	super.onPause();
	runner=false;
}

@Override
public void onDestroy() {
	// TODO Auto-generated method stub
	super.onDestroy();
	runner=false;
}

@Override
public void onRefresh() {
	// TODO Auto-generated method stub
	
		   //sr_layout.setRefreshing(true);
	if(!runner){
		 currentDir = new File(prefs.getString("home_location", Environment.getExternalStorageDirectory().toString()));
   	    try{  new Asy_search_for_pdf().execute(currentDir); }catch(Exception ds){}
	 }

}

@Override
public void itemLongClicked(View view, int position) {
	// TODO Auto-generated method stub

	if(!runner) {

		if (runner == false) o = s_fils.get(position);
		else o = s_filsss.get(position);
		////////////////////////////////////////////////////// 11111111111111111111111111111111111


		if (new File(o.getPath()).isFile()) {
			AlertDialog.Builder ad_m = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
			final CharSequence[] items;
			if (o.getTitle().toString().charAt(0) == '.') {
				final CharSequence[] itemss = {"Open", "Open by System", "Open by Other", "Share", "Delete", "UnHide", "Add BookMark"};
				items = itemss;
			} else {
				final CharSequence[] itemss = {"Open", "Open by System", "Open by Other", "Share", "Delete", "Hide", "Add BookMark"};
				items = itemss;
			}

			runner = false;
			ad_m.setItems(items, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub

					switch (which) {

						case 0:
							runner = false;
							String pdf_path = o.getPath();
							Uri uri33 = Uri.parse(pdf_path);
							Intent intent33 = new Intent(getActivity(), MuPDFActivity.class);
							intent33.setAction(Intent.ACTION_VIEW);
							intent33.setData(uri33);
							startActivity(intent33);

							break;
						case 1:        // by sys
							runner = false;
							File file_sys = new File(o.getPath());
							Intent intent = new Intent(Intent.ACTION_VIEW);
							File file = new File(o.getPath());
							Uri uri = Uri.fromFile(file);
							intent.setDataAndType(uri, "application/pdf");
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent);

							break;
						case 2:     // by other
							File file1 = new File(o.getPath());
							Uri uri1 = Uri.fromFile(file1);
							Intent intent1 = new Intent(Intent.ACTION_VIEW);
							intent1.setDataAndType(uri1, "*/*");
							intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							getActivity().startActivity(intent1);


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
							} catch (Exception exc) {
								Snackbar.make(getView(), "Something went wrong while sharing.", Snackbar.LENGTH_LONG).show();
								Log.e("Share", exc.toString());
							}

							break;

						case 4:     //dele

							AlertDialog.Builder alert = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
							alert.setMessage("Are you Sure to Delete (Can't Undo)?");
							alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
									File f = new File(o.getPath());
									//  Toast.makeText(getActivity(), o.getPath(), Toast.LENGTH_LONG).show();

									Snackbar.make(getView(), o.getPath() + " has been deleted", Snackbar.LENGTH_LONG).show();

									if (!f.delete())
										Snackbar.make(getView(), "Sorry, something went wrong", Snackbar.LENGTH_LONG).show();


							refreshThList();


								}
							});

							alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
									// Do nothing.
								}
							}).show();
							break;


						case 5:  //hide

							File from = new File(o.getPath());

							String Cpath = o.getPath();

							if (!(o.getTitle().toString().charAt(0) == '.')) {

								it = null;
								it = Cpath.split("\\/");

								String path_name = "";
								for (int i = 0; (i < it.length - 1); i++)
									path_name = path_name + it[i] + "/";

								String ton = path_name + "." + o.getTitle();
								//  to_name = path_name + to_name;

								File to = new File(ton);
								if (!from.renameTo(to))
									Toast.makeText(getActivity(), "ACCESS DENIED !", Toast.LENGTH_LONG).show();

							} else {
								String toname = o.getTitle().toString().substring(1);
								it = null;
								it = Cpath.split("\\/");

								String path_name = "";
								for (int i = 0; (i < it.length - 1); i++)
									path_name = path_name + it[i] + "/";

								String ton = path_name + toname;
								File to = new File(ton);
								if (!from.renameTo(to))
									Toast.makeText(getActivity(), "ACCESS DENIED !", Toast.LENGTH_LONG).show();


							}
							if (!runner) {
								runner = true;
								if (!sr_layout.isRefreshing()) {
									sr_layout.post(new Runnable() {
										@Override
										public void run() {
											sr_layout.setRefreshing(true);
										}
									});
								}
								currentDir = new File(prefs.getString("home_location", Environment.getExternalStorageDirectory().toString()));
								try {
									new Asy_search_for_pdf().execute(currentDir);
								} catch (Exception ds) {
								}

							}
							break;


						case 6: //bookmark

							String name = o.getTitle().replace("'", "''");
							String data = o.getData().replace("'", "''");
							String date = o.getDate().replace("'", "''");
							String path = o.getPath().replace("'", "''");
							String image = o.getImage().replace("'", "''");
							try {

								Cursor r = myDB.rawQuery("select * from PDFBOOK"+ " where path  = '"+path+"' ", null);
								if(r.moveToFirst()){

									Snackbar.make(getView(), "Already Exist in Bookmark!", Snackbar.LENGTH_LONG).show();

								}else {
									myDB.execSQL("insert into PDFBOOK values('" + image + "','" + name + "','" + data + "','" + date + "','" + path + "')");
									Snackbar.make(getView(), "Bookmark Added to Bookmark Tab!", Snackbar.LENGTH_LONG).show();
									//      			           AlertDialog.Builder addd = new AlertDialog.Builder(getActivity());
									mCommunicate_to_frag_Bookmark_fromListPDF.refreshBookListFromListPDF();

								}
							} catch (Exception exc) {
								Log.e("888",exc.toString());
								Snackbar.make(getView(), "Something went wrong!", Snackbar.LENGTH_LONG).show();

							}

							break;
					}


				}
			});
			try {
				ad_m.show();
			} catch (Exception e) {
			}

		} else {

			Snackbar.make(getView(), "Sorry, File not Found", Snackbar.LENGTH_LONG).show();
			if (!runner) {
				runner = true;
				if (!sr_layout.isRefreshing()) {
					sr_layout.post(new Runnable() {
						@Override
						public void run() {
							sr_layout.setRefreshing(true);
						}
					});
				}
				currentDir = new File(prefs.getString("home_location", Environment.getExternalStorageDirectory().toString()));
				try {
					new Asy_search_for_pdf().execute(currentDir);
				} catch (Exception ds) {
				}
			}

		}
	}
}

	public void refreshThList() {
		if (!runner) {
			runner = true;
			if (!sr_layout.isRefreshing()) {
				sr_layout.post(new Runnable() {
					@Override
					public void run() {
						sr_layout.setRefreshing(true);
					}
				});
			}
			currentDir = new File(prefs.getString("home_location", Environment.getExternalStorageDirectory().toString()));
			try {
				new Asy_search_for_pdf().execute(currentDir);
			} catch (Exception ds) {
			}

		}
	}

	@Override
public void itemClicked(View view, int position) {
	// TODO Auto-generated method stub
	
if(!runner) {
	Fragment_listPDF_SingleItem o;


	if (runner == false) o = s_fils.get(position);
	else o = s_filsss.get(position);


	runner = false;

	if (new File(o.getPath()).isFile()) {
		String pdf_path = o.getPath();
		Uri uri = Uri.parse(pdf_path);
		Intent intent = new Intent(getActivity(), MuPDFActivity.class);
		intent.setAction(Intent.ACTION_VIEW);
		intent.setData(uri);
		startActivity(intent);
	} else {
		Snackbar.make(getView(), "Sorry, File not Found", Snackbar.LENGTH_LONG).show();
		if (!runner) {
			runner = true;
			if (!sr_layout.isRefreshing()) {
				sr_layout.post(new Runnable() {
					@Override
					public void run() {
						sr_layout.setRefreshing(true);
					}
				});
			}
			currentDir = new File(prefs.getString("home_location", Environment.getExternalStorageDirectory().toString()));
			try {
				new Asy_search_for_pdf().execute(currentDir);
			} catch (Exception ds) {
			}
		}
	}
}else{
	Snackbar.make(getActivity().getWindow().getDecorView(),"Loading ...",Snackbar.LENGTH_LONG).show();
}
}
 
public void set_Communicate_to_frag_Bookmark(Communicate_to_frag_Bookmark_fromListPDF c){
	mCommunicate_to_frag_Bookmark_fromListPDF = c;
}


public interface Communicate_to_frag_Bookmark_fromListPDF{
	public void refreshBookListFromListPDF();
}
}
