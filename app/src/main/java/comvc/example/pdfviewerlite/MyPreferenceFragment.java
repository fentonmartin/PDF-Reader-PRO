package comvc.example.pdfviewerlite;

import java.io.File;
import java.io.FilenameFilter;

import com.pdf.reader.pro.ebin.R;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.widget.Toast;

public class MyPreferenceFragment extends PreferenceFragment{
	
	final int DIALOG_LOAD_FILE = 1000;
	File mPath = new File(Environment.getExternalStorageDirectory().toString());
	Preference pref_name ;
	private String[] mFileList;
	private static final String FTYPE = ".txt";    
	private String mChosenFile;
	 SharedPreferences prefs ;
	 SharedPreferences.Editor editor;

    @Override
    public void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        
         pref_name  = (Preference) findPreference("pref_file_loc");
         prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
         editor = prefs.edit();	
         
         pref_name.setSummary(prefs.getString("home_location", Environment.getExternalStorageDirectory().toString()));
         pref_name.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO Auto-generated method stub
				
		mPath = new File(prefs.getString("home_location", Environment.getExternalStorageDirectory().toString()));		
				loadFileList(mPath.getAbsolutePath());
			onCreateDialog(DIALOG_LOAD_FILE);
				
				
				return false;
			}
		});
    }
    
    
	
	
	
	
	 void loadFileList(String mp) {
	    try {
	        mPath=new File(mp);
	    }
	    catch(SecurityException e) {
	        //Log.e(TAG, "unable to write on the sd card " + e.toString());
	    }
	    if(mPath.exists()) {
	        FilenameFilter filter = new FilenameFilter() {

	            @Override
	            public boolean accept(File dir, String filename) {
	                File sel = new File(dir, filename);
	                return filename.contains(FTYPE) || sel.isDirectory();
	            }

	        };
	        mFileList = mPath.list(filter);
	    }
	    else {
	        mFileList= new String[0];
	    }
	}

	 
	 
	 
	 
	 
	 public Dialog onCreateDialog(int id) {
	    Dialog dialog = null;
	    AlertDialog.Builder builder = new Builder(getActivity());

	    switch(id) {
	        case DIALOG_LOAD_FILE:
	            builder.setTitle(mPath.getAbsolutePath());
	            
	            
	            builder.setPositiveButton("OK", new OnClickListener() {
					
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
					
						
			if(mPath.canWrite()){
						String GUIpath = null;
						editor.putString("home_location", mPath.getAbsolutePath()); //////////// saving path
					    editor.commit();
						 pref_name.setSummary(mPath.getAbsolutePath());
						try{ 
						 getActivity().runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
			                   Snackbar.make(getView(), "Refresh your LIST PDF tab by swiping down.",  Snackbar.LENGTH_LONG).show();
								}
							});
						}catch(Exception h){}
					}else{
						 editor.putString("home_location",Environment.getExternalStorageDirectory().toString()); //////////// saving path
						 editor.commit();
						pref_name.setSummary( Environment.getExternalStorageDirectory().toString());
					 	//Toast.makeText(getActivity(), "Write permission denied.. \n Location change to default", Toast.LENGTH_LONG).show();
					 	Snackbar.make(getView(),"Write permission denied.. \n Location change to default", Snackbar.LENGTH_LONG).show();
					}
						
					}

					
				});
	            builder.setNegativeButton("UP", new OnClickListener() {

					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						
						if(!mPath.toString().equalsIgnoreCase("/")){mPath = new File(mPath.getParent());}
						else{Toast.makeText(getActivity(), "Root directory, no more UP!", Toast.LENGTH_LONG).show();}
						loadFileList(mPath.getAbsolutePath());
						onCreateDialog(DIALOG_LOAD_FILE);
					}

		
					
	

				
				});
	            
	            
	            if(mFileList == null) {
	           //     Log.e(TAG, "Showing file picker before loading the file list");
	                dialog = builder.create();
	                return dialog;
	            }
	            builder.setItems(mFileList, new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int which) {
	                    mChosenFile = mFileList[which];
	                    mChosenFile = mPath+"/"+mChosenFile+"/";
	            		loadFileList(mChosenFile);
	            		onCreateDialog(DIALOG_LOAD_FILE);
	                    //you can do stuff with the file here too
	                }
	            });
	            break;
	    }
	    dialog = builder.show();
	    return dialog;
	}
}