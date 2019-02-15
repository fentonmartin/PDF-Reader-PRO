package comvc.example.pdfviewerlite;

import comvc.ebtabsss.tabs.Fragment_bookmark;
import comvc.ebtabsss.tabs.Fragment_fileBrowser;
import comvc.ebtabsss.tabs.Fragment_listPDF;

import com.github.clans.fab.FloatingActionButton;
import com.pdf.reader.pro.ebin.R;

import comvc.util.IabHelper;
import comvc.util.IabResult;
import pdf.materialtabs.MaterialTab;
import pdf.materialtabs.MaterialTabHost;
import pdf.materialtabs.MaterialTabListener;
import transaction.pdf.PdfCreator;
import transaction.pdf.util.Key;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar.LayoutParams;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;




public class ActivityFront extends AppCompatActivity implements MaterialTabListener,NavigationView.OnNavigationItemSelectedListener, 
   Fragment_fileBrowser.Communicate_to_frag_Bookmark,
		Fragment_listPDF.Communicate_to_frag_Bookmark_fromListPDF {
	
	private static final int PERMISSION_REQUEST_CODE = 1;
	
	Toolbar toolbar;
	NavigationView mDrawer;
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    int mSelectedId;
    boolean mUserSawDrawer = false;
    public ViewPager mPager;
	public MaterialTabHost materialTabHost;
	ViewPagerAdapter adapter;
	CollapsingToolbarLayout mCollapsingToolbarLayout;
	AppBarLayout mappBarLayout;
	
	Fragment_bookmark frg_bookmark = null;
	Fragment_fileBrowser frag_storage = null;
	Fragment_listPDF frag_pdf_list = null;
	
	private static final String SELECTED_ITEM_ID = "selected_item_id";
    private static final String FIRST_TIME = "first_time";

	FloatingActionButton fab;


//	AdView mAdView;
//	InterstitialAd mInterstitialAd;
//	AdRequest adRequest;
	boolean isActivityIsVisible = true;




	@Override
	public void onDestroy() {
//        this.mWakeLock.release();
//		if(mAdView!=null)mAdView.destroy();
		super.onDestroy();
	}


//	private void requestNewInterstitial() {
//		final AdRequest adRequest = new AdRequest.Builder()
//				.addTestDevice("SEE_YOUR_LOGCAT_TO_GET_YOUR_DEVICE_ID")
//				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//				.addTestDevice("C87242295BB59DB07A32E8637643B55E")
//				.build();
//
//
//
//		final Handler handler = new Handler();
//		handler.postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				//Do something after 100ms
//				mInterstitialAd.loadAd(adRequest);
//			}
//		}, 60000);
//
//	}


	@Override
	protected void onPause() {
		super.onPause();
		isActivityIsVisible = false;
//		if(mAdView!=null)mAdView.pause();
//        this.mWakeLock.release();
	}



	@Override
	protected void onResume() {
		super.onResume();
//		mAdView.resume();
//        this.mWakeLock.acquire();
		isActivityIsVisible = true;
//		if(mInterstitialAd!=null)if(mInterstitialAd.isLoaded()) mInterstitialAd.show();

	}

	Boolean d_user_addfree_actual = false;
    SharedPreferences pref;
	SharedPreferences.Editor prefEditor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_front);

		pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		prefEditor = pref.edit();
		d_user_addfree_actual = pref.getBoolean(Key.PREFERNCE_ADFREE,false);
		setUpPaymentInitialization();

		findViewById(R.id.fab22).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
             startActivity(new Intent(ActivityFront.this, PdfCreator.class));
			}
		});
		toolbar = (Toolbar) findViewById(R.id.app_baar);
		mappBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
		setSupportActionBar(toolbar);
 		
		
		toolbar = (Toolbar) findViewById(R.id.app_baar);
		mappBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
	    setSupportActionBar(toolbar);
//		mAdView = (AdView) findViewById(R.id.adView) ;


		if(!d_user_addfree_actual) {
			//Loading Aword ad
//			adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//					.addTestDevice("C87242295BB59DB07A32E8637643B55E")
//					.build();
//			mAdView.loadAd(adRequest);
//			mAdView.setAdListener(new AdListener() {
//				@Override
//				public void onAdLoaded() {
//					super.onAdLoaded();
//					mAdView.setVisibility(View.VISIBLE);
//				}
//			});
//			mInterstitialAd = new InterstitialAd(this);
//			mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
//
//			mInterstitialAd.setAdListener(new AdListener() {
//				@Override
//				public void onAdClosed() {
//					requestNewInterstitial();
////                beginPlayingGame();
//				}
//
//				public void onAdLoaded() {
//					if (isActivityIsVisible && !d_user_addfree_actual) {
//						mInterstitialAd.show();
//					}
//				}
//			});
//			requestNewInterstitial();
		}




		// here we add Apptitle to toolbar as TEXTVIEW
	    TextView text = new TextView(this);
	    text.setText("PDF Reader PRO");
	    if(Build.VERSION.SDK_INT>20){
	    text.setTextAppearance(this,android.R.style.TextAppearance_Material_Widget_ActionBar_Title_Inverse);
	    }else{
	    	text.setTextAppearance(this,R.style.CollapsedAppBar);
	    	text.setGravity(Gravity.CENTER_VERTICAL);
	    }
	    int ORIENTATION_PORTRAIT =1;
	    if(this.getResources().getConfiguration().orientation==ORIENTATION_PORTRAIT)
	    {
	    LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	    llp.setMargins(0, 10, 0, 0); // llp.setMargins(left, top, right, bottom);
	    text.setLayoutParams(llp);
	    }
	    
	    text.setGravity(Gravity.CENTER_VERTICAL);
	    toolbar.addView(text);
	    mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
	    
	    
	    
 		
 		mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
 	    
 		
 		
 		

 	    
 	// Navigation drawer starts here.
 	    mDrawer = (NavigationView) findViewById(R.id.main_drawer);
 	    mDrawer.setNavigationItemSelectedListener(this);
 	    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
 	    mDrawerToggle = new ActionBarDrawerToggle(this,
 	            mDrawerLayout,
 	            toolbar,
 	            R.string.drawer_open,
 	            R.string.drawer_closed);
 	    mDrawerLayout.setDrawerListener(mDrawerToggle);
 	    mDrawerToggle.syncState();
 	    if (!didUserSeeDrawer()) {
 	        showDrawer();
 	        markDrawerSeen();
 	    } else {
 	        hideDrawer();
 	    }
 	   mSelectedId = savedInstanceState == null ? R.id.menu_nav_drawer_list_pdf : savedInstanceState.getInt(SELECTED_ITEM_ID);
 	    navigate(mSelectedId);
		setupTabs();
 	    
 	   if(Build.VERSION.SDK_INT>=23){
			Log.e("EEE","SDK>MARSHMA");
			if(!checkPermission()){
				this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						requestPermission();
					}
				});
			}else{
				//Snackbar.make(getView(),"Permission already Granted.",Snackbar.LENGTH_LONG).show();

			}
		}else{

		}
 	    
 	    

 		
}
	
	
	@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

		Log.e("1212","PERMISSION_REQUEST_CODE");
		switch (requestCode) {
	        case PERMISSION_REQUEST_CODE:
	        	Log.e("PERMISSION_REQUEST_CODE","PERMISSION_REQUEST_CODE");
	            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

	                Snackbar.make(this.findViewById(android.R.id.content).findViewById(android.R.id.content),"Permission Granted, Now you can use..",Snackbar.LENGTH_LONG).show();
	                setupTabs();
					frag_pdf_list.refreshThList();
					if(frag_storage!=null)frag_storage.loadAll();

					Fragment_fileBrowser frag_storage = null;
	                
//	                Intent mStartActivity = new Intent(this, ActivityFront.class);
//	                int mPendingIntentId = 123456;
	                
//	                this.finish();
//
//	                PendingIntent mPendingIntent = PendingIntent.getActivity(this, mPendingIntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
//	                AlarmManager mgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
//	                mgr.set(AlarmManager.RTC, 100, mPendingIntent);
//	                //mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
//	                System.exit(0);
	                
	                
	            } else {

	                Snackbar.make(this.getWindow().getDecorView().findViewById(android.R.id.content),"Permission Denied, So can't work.",Snackbar.LENGTH_LONG).show();
                    this.finish();
	            }
	            break;
	    }
	}

	private boolean checkPermission(){
	    int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
	    if (result == PackageManager.PERMISSION_GRANTED){
	       return true;

	    } else {

	        return false;

	    }
	}

	private void requestPermission(){

	    if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){

	        Snackbar.make(this.getWindow().getDecorView().findViewById(android.R.id.content),"PDF Reader needs  this Permission to access PDF Files, Please  Allow it",Snackbar.LENGTH_LONG).show();
	        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
	    // Dont use AppCompat.requestPermissions
	       // Since Fragment class import from supp V4 library 
	    }else{
	        Snackbar.make(this.getWindow().getDecorView().findViewById(android.R.id.content),"PDF Reader needs  this Permission to access PDF Files, Please  Allow it",Snackbar.LENGTH_LONG).show();
	        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
	    // Dont use AppCompat.requestPermissions
	       // Since Fragment class import from supp V4 library 
	    }
	}

	
	
	public void setupTabs(){
		  // Here set up tabs
		materialTabHost = (MaterialTabHost) findViewById(R.id.materialTabHost);
		mPager = (ViewPager) findViewById(R.id.view_pager_home);
//         mTabs.setCustomTabView(R.layout.custom_tab_view, R.id.tv_tab_view_text);
//        mTabs.setDistributeEvenly(true);
       adapter = new ViewPagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(adapter);
	    mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
	        @Override
	        	public void onPageSelected(int position) {
	        		materialTabHost.setSelectedNavigationItem(position);        		
	        	}
	        });
	        
	    for (int i = 0; i < adapter.getCount(); i++) {
	            materialTabHost.addTab( materialTabHost.newTab() .setText(adapter.getPageTitle(i)).setTabListener(this));
	              }
	    mPager.setCurrentItem(0);
	}
	

	  private boolean didUserSeeDrawer() {
	        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
	        mUserSawDrawer = sharedPreferences.getBoolean(FIRST_TIME, false);
	        return mUserSawDrawer;
	    }

	    private void markDrawerSeen() {
	        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
	        mUserSawDrawer = true;
	        sharedPreferences.edit().putBoolean(FIRST_TIME, mUserSawDrawer).apply();
	    }

	    private void showDrawer() {
	        mDrawerLayout.openDrawer(GravityCompat.START);
	    }

	    private void hideDrawer() {
	        mDrawerLayout.closeDrawer(GravityCompat.START);
	    }

	    private void navigate(int mSelectedId) {
	        Intent intent = null;
    switch(mSelectedId){
    case 0:
   	    break;
    case 1:
	    break;    	    
    case 2:
	    break;
    case 3:
	    break;
    case 4:
	    break;
    case 5:
	    break;
    case 6: this.finish();Toast.makeText(this, "fgdf", Toast.LENGTH_LONG).show();
	    break;
    }
	
	
}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if(id==R.id.action_settings){
			startActivity(new Intent(this, PreferencesExampleActivity.class));
		}
		return super.onOptionsItemSelected(item);
	
	}
	
	
	@Override
	public void onTabSelected(MaterialTab tab) {
		// TODO Auto-generated method stub
		mPager.setCurrentItem(tab.getPosition());
		//if(tab.getPosition()==2){
			//m.appBarLayout.setExpanded(false);
		//}
	}




	@Override
	public void onTabReselected(MaterialTab tab) {
		// TODO Auto-generated method stub
	}




	@Override
	public void onTabUnselected(MaterialTab tab) {
		// TODO Auto-generated method stub
	}
	
	
	
	
 class ViewPagerAdapter extends FragmentStatePagerAdapter{

	public ViewPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		if(arg0 == 0){
			frag_pdf_list = new Fragment_listPDF();
			frag_pdf_list.set_Communicate_to_frag_Bookmark(ActivityFront.this);
			return frag_pdf_list;
		}
		if(arg0 ==1){
			frg_bookmark = new Fragment_bookmark();
			return frg_bookmark;
			
		}
		if(arg0 ==2){
			frag_storage =  new Fragment_fileBrowser();
			frag_storage.set_Communicate_to_frag_Bookmark(ActivityFront.this);
			return frag_storage;
			
		}
		return null;
		
	}
	
	


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 3;
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		return getResources().getStringArray(R.array.tabs)[position];
	}
	 
	 
	 
 }
 

 
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}


	static final String TAG = "MainViewActivity";
	IabHelper mHelper;
	HandlePayment handlePayment =  new HandlePayment();
	public void setUpPaymentInitialization(){
		//payment - code start- ------------------- -------------- ------
		String base64EncodedPublicKey =  getString(R.string.IN_APP_ID);
		mHelper = new IabHelper(this, base64EncodedPublicKey);
		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			public void onIabSetupFinished(IabResult result) {
				if (!result.isSuccess()) {
					Log.e(TAG, "In-app Billing setup failed: " +
							result);
				} else {
					Log.e(TAG, "In-app Billing is set up OK");

//					consumeItem();
				}
			}
		});




		//payment - code end- ------------------- -------------- ------
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);

		// Pass on the activity result to the helper for handling
		if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
			// not handled, so handle it ourselves (here's where you'd
			// perform any handling of activity results not related to in-app
			// billing...
			super.onActivityResult(requestCode, resultCode, data);
		}
		else {
			Log.d(TAG, "onActivityResult handled by IABUtil.");
		}
	}


	@Override
	public boolean onNavigationItemSelected(MenuItem arg0) {
		// TODO Auto-generated method stub
		
		switch(arg0.getItemId()){
		
		case R.id.menu_nav_drawer_list_pdf: 
			    hideDrawer();
			    //materialTabHost.
			   break;
			   
		case R.id.menu_nav_drawer_settings:
			startActivity(new Intent(this, PreferencesExampleActivity.class));
			   break;
			   
		case R.id.menu_nav_drawer_upgrade:

			if(!d_user_addfree_actual){
				handlePayment.perfromAdfreePayMent(ActivityFront.this,mHelper,pref,prefEditor);
			}else{
				showDialog(false, this, "Ads already removed.", "Info", R.drawable.ic_credit_card_black_48dp);
			}


	    	
	    	
			break;
			
//		case R.id.menu_nav_drawer_star:
//
//			final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
//			Log.e("appPackageName",appPackageName);
//			try {
//			    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
//			} catch (android.content.ActivityNotFoundException anfe) {
//			    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
//			}
//
//			 break;
			 
		case R.id.menu_nav_drawer_feedback:
			
			
			int drawableResource = R.drawable.ic_feedback_black_48dp;
        	AlertDialog.Builder newAlert22 = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
	    	newAlert22.setCancelable(true);
//	    	Drawable d = getResources().getDrawable(R.drawable.ic_add_alert_black_36dp);
//    		d.setColorFilter(new PorterDuffColorFilter(R.color.accentColor,PorterDuff.Mode.MULTIPLY));
	    	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
	    		newAlert22.setIcon(getResources().getDrawable(drawableResource, this.getTheme()));
	    	} else {
	    		newAlert22.setIcon(getResources().getDrawable(drawableResource));
	    	}
	    	LinearLayout layout = new LinearLayout(this);
	    	layout.setOrientation(LinearLayout.VERTICAL);
	    	LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
	    		     LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

	    	layoutParams.setMargins(30, 20, 30, 0);

	    	final AppCompatEditText titleBox = new AppCompatEditText(this);
	    	titleBox.setHint("Title");
	    	titleBox.setLayoutParams(layoutParams);
	    	titleBox.setTextColor(getResources().getColor(R.color.Black));
	    	titleBox.setHintTextColor(getResources().getColor(R.color.Black));
	    	layout.addView(titleBox);

	    	final AppCompatEditText descriptionBox = new AppCompatEditText(this);
	    	descriptionBox.setHint("Description");
	    	descriptionBox.setLayoutParams(layoutParams);
	    	descriptionBox.setLines(6);
	    	descriptionBox.setTextColor(getResources().getColor(R.color.Black));
	    	descriptionBox.setHintTextColor(getResources().getColor(R.color.Black));
	    	layout.addView(descriptionBox);
	    	
	    	layout.setLayoutParams(layoutParams);
	    	newAlert22.setView(layout);
	    	
	    	newAlert22.setTitle("Feedback");
	       	newAlert22.setPositiveButton("OK", new OnClickListener() {
				
	    				@Override
	    				public void onClick(DialogInterface dialog, int which) {
	    					// TODO Auto-generated method stub
	    					Intent i = new Intent(Intent.ACTION_SEND);
	    					i.setType("message/rfc822");
	    					i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"aheaddesigner@gmail.com"});
	    					i.putExtra(Intent.EXTRA_SUBJECT, titleBox.getText().toString());
	    					i.putExtra(Intent.EXTRA_TEXT   , descriptionBox.getText().toString());
	    					try {
	    					    startActivity(Intent.createChooser(i, "Send mail..."));
	    					} catch (android.content.ActivityNotFoundException ex) {
	    					    Toast.makeText(ActivityFront.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
	    					}
	    					
	    				}
	    			});
	    	newAlert22.setNegativeButton("CANCEL", null);
	    	    	
	    	AlertDialog dialog = newAlert22.show();
	    	ImageView iv = (ImageView)dialog.findViewById(android.R.id.icon);
	    	iv.setColorFilter(Color.argb(255, 255, 87, 34));
	    	TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
	    	messageText.setGravity(Gravity.CENTER_HORIZONTAL);

	    	dialog.show();
	    	
			break;
			
		case R.id.menu_nav_drawer_aboutapp:
			showDialog(true,ActivityFront.this, "PDF Reader PRO \n 2016\n\n " +
	    			"AHEAD DESIGNER\n aheaddesigner@gmail.com","About Us",R.drawable.ic_brush_black_48dp);
		   break;
		   
		case R.id.menu_nav_drawer_exit:
			   this.finish();
			break;
		   
		
		
		}
		return false;
	}


	public void showDialog(Boolean terms, Context context, String setMessage, String setTitle, int drawableResource){
		AlertDialog.Builder newAlert = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
	    	newAlert.setPositiveButton("OK", null);
	    	newAlert.setCancelable(true);
//	    	Drawable d = getResources().getDrawable(R.drawable.ic_add_alert_black_36dp);
//    		d.setColorFilter(new PorterDuffColorFilter(R.color.accentColor,PorterDuff.Mode.MULTIPLY));
	    	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
	    		newAlert.setIcon(getResources().getDrawable(drawableResource, context.getTheme()));
	    	} else {
	    		newAlert.setIcon(getResources().getDrawable(drawableResource));
	    	}
	    	newAlert.setMessage(setMessage);
	    	newAlert.setTitle(setTitle);
	    	AlertDialog dialog = newAlert.show();
	    	
	    	ImageView iv = (ImageView)dialog.findViewById(android.R.id.icon);
	    	iv.setColorFilter(Color.argb(255, 255, 87, 34));
	    	TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
	    	
	    	if(terms==true){
	    		messageText.setGravity(Gravity.CENTER_HORIZONTAL);
	    	}else{
	    		messageText.setGravity(Gravity.LEFT);
	    	}

	    	dialog.show();}

	@Override
	public void refreshBookList() {
		// TODO Auto-generated method stub
		if(frg_bookmark!=null){
			frg_bookmark.reloadBookmarks();
		}
		
	}




	@Override
	public void refreshBookListFromListPDF() {
		// TODO Auto-generated method stub
		if(frag_pdf_list!=null){
			frg_bookmark.reloadBookmarks();
		}
	}
	
}

