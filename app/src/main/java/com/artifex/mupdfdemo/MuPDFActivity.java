package com.artifex.mupdfdemo;


import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.pdf.reader.pro.ebin.R;


import comvc.example.pdfviewerlite.Dir_size_Human_Redable;
import pdet.artifex.mupdf.cut.CutActivity;
import transaction.pdf.PDFSaveToDiskShow;
import transaction.pdf.util.Key;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.util.Locale;
import java.util.Random;


class SearchTaskResult {
	public final String txt;
	public final int pageNumber;
	public final RectF searchBoxes[];
	static private SearchTaskResult singleton;
	SearchTaskResult(String _txt, int _pageNumber, RectF _searchBoxes[]) {
		txt = _txt;
		pageNumber = _pageNumber;
		searchBoxes = _searchBoxes;
	}

	static public SearchTaskResult get() {
		return singleton;
	}

	static public void set(SearchTaskResult r) {
		singleton = r;
	}
}


class ProgressDialogX extends ProgressDialog {
	public ProgressDialogX(Context context) {
		super(context);
	}
	private boolean mCancelled = false;

	public boolean isCancelled() {
		return mCancelled;
	}

	@Override
	public void cancel() {
		mCancelled = true;
		super.cancel();
	}
}

public class MuPDFActivity extends AppCompatActivity {
	private enum LinkState {
		DEFAULT, HIGHLIGHT, INHIBIT
	};
    
	private final int TAP_PAGE_MARGIN = 5;
	/**�ӳ�����*/
	private static final int SEARCH_PROGRESS_DELAY = 200;
	private MuPDFCore core;
	/**�ļ���*/
	private String mFileName;
	/**�Ķ���,������ʾpdf�ĵ�������*/
	private ReaderView mDocView;
	/**��ť��ͼ*/
	private View mButtonsView;
	private boolean mButtonsVisible;
	private EditText mPasswordView;
	private SeekBar mPageSlider;
	private TextView mPageNumberView;
	private ImageButton mCancelButton;
	private ViewSwitcher mTopBarSwitcher;
	private boolean mTopBarIsSearch;
	private ImageButton mSearchBack;
	private ImageButton mSearchFwd;
	private EditText mSearchText;
	private ImageView cutP;
	private FloatingActionMenu back;
	private AsyncTask<Integer, Integer, SearchTaskResult> mSearchTask;
	// private SearchTaskResult mSearchTaskResult;
	private AlertDialog.Builder mAlertBuilder;
	private LinkState mLinkState = LinkState.DEFAULT;
	private final Handler mHandler = new Handler();

	FloatingActionButton fabContent, fabShare, fabSearch, fabCreateImage,fabRead;
	Toolbar toolbar1, toolbar2;
	TextToSpeech tts;
	String path = null;

	private  MuPDFCore openFile(String path) {
		this.path = path;
		int d =0;
	int lastSlashPos = path.lastIndexOf('/');
	mFileName = new String(lastSlashPos == -1 ? path: path.substring(lastSlashPos + 1));
		System.out.println("Trying to open " + path);
 if(d>0){this.finish();}
		d++;
	   
		try {
			core = new MuPDFCore(MuPDFActivity.this,path);
			// New file: drop the old outline data
			OutlineActivityData.set(null);
		} catch (Exception e) {
			System.out.println(e);
			this.finish();
			return null;
			
		}
		return core;
	}


	@Override
	protected void onPause() {

		isActivityIsVisible = false;

		if(tts!=null) {
			if(tts.isSpeaking()) tts.stop();
			if(fabRead != null) fabRead.setImageResource(R.drawable.ic_play_circle_outline_white_24dp);
		}


		killSearch();

		if (mFileName != null && mDocView != null) {
			SharedPreferences prefs = getSharedPreferences("remember", 0);
			//	SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
			SharedPreferences.Editor edit = prefs.edit();
			edit.putInt("page" + mFileName, mDocView.getDisplayedViewIndex());
			edit.commit();
		}
		super.onPause();
	}


	@Override
	protected void onResume() {
		super.onResume();

		isActivityIsVisible = true;
		if(mInterstitialAd!=null)if(mInterstitialAd.isLoaded()) mInterstitialAd.show();

		connectTakeClickfab();
		try{
			tts = new TextToSpeech(getApplicationContext(), null);
			tts.setLanguage(Locale.US);


			if (!core.hasOutline()) fabContent.setEnabled(false);}catch(Exception d){}
//		Log.e("ssss",mDocView.toString());

	}

	public void onDestroy() {

		if(tts!=null) {
			if (tts.isSpeaking()) tts.stop();
		}


		if (core != null)
			core.onDestroy();
		core = null;
		super.onDestroy();

	}




	InterstitialAd mInterstitialAd;
	AdRequest adRequest;
	boolean isActivityIsVisible = true;





	private void requestNewInterstitial() {
		final AdRequest adRequest = new AdRequest.Builder()
				.addTestDevice("SEE_YOUR_LOGCAT_TO_GET_YOUR_DEVICE_ID")
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
				.addTestDevice("C87242295BB59DB07A32E8637643B55E")
				.build();



		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				//Do something after 100ms
				mInterstitialAd.loadAd(adRequest);
			}
		}, 80000);

	}

	Boolean d_user_addfree_actual = false;
	SharedPreferences pref;
	SharedPreferences.Editor prefEditor;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		mAlertBuilder = new AlertDialog.Builder(this);

		if (core == null) {
			//core = (MuPDFCore) getLastNonConfigurationInstance();
			core = (MuPDFCore) getLastCustomNonConfigurationInstance();

			if (savedInstanceState != null
					&& savedInstanceState.containsKey("FileName")) {
				mFileName = savedInstanceState.getString("FileName");
			}
		}
		
		
		
		if (core == null) {
			Intent intent = getIntent();
			if (Intent.ACTION_VIEW.equals(intent.getAction())) {
				Uri uri = intent.getData();
				if (uri.toString().startsWith("content://media/external/file")) {
					/* Handle view requests from the Transformer Prime's file
					 manager
					 Hopefully other file managers will use this same scheme,
					 if not
					 using explicit paths.*/
					Cursor cursor = getContentResolver().query(uri,
							new String[] { "_data" }, null, null, null);
					if (cursor.moveToFirst()) {
						uri = Uri.parse(cursor.getString(0));
					}
				}
			 core = openFile(Uri.decode(uri.getEncodedPath()));
	
			}



			if (core != null && core.needsPassword()) {
				requestPassword(savedInstanceState);
				return;
			}
		}
		
		
		
		if (core == null) {
			AlertDialog alert = mAlertBuilder.create();
			alert.setTitle(R.string.open_failed);
			alert.setButton(AlertDialog.BUTTON_POSITIVE, "Dismiss",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
 							finish();
						}
					});
			alert.show();
			return;
		}

		createUI(savedInstanceState);
	}





	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.pdf_menu, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		try {
			View view = mDocView.getDisplayedView();
			try {
				if (false == view.isDrawingCacheEnabled()) {
					view.setDrawingCacheEnabled(true);
				}
			} catch (Exception e) {
				Log.e("EXC", e.toString());
			}

			Bitmap b = getBitmapFromView(view);
			SaveImage(b);
		}catch (Exception e){Log.e("error sav bm",e.toString());}
		super.onBackPressed();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:


				onBackPressed();
			    break;
			case R.id.action_bookmark:

				String name = 	new File(getIntent().getData().toString()).getName().replace("'","''");;
				Dir_size_Human_Redable newobj = new Dir_size_Human_Redable();
				long f_size= new File(getIntent().getData().toString()).length();
				String data  = newobj.humanReadableByteCount(f_size, false).replace("'","''");;
				Date lastModDate = new Date(new File(getIntent().getData().toString()).lastModified());
				DateFormat formater = DateFormat.getDateInstance();
				String date  = formater.format(lastModDate).replace("'","''");;
				String path = getIntent().getData().toString().replace("'","''");
				String image = "a_pdf";
				try{
					SQLiteDatabase myDB = openOrCreateDatabase("myDB1", this.MODE_PRIVATE, null);
					Cursor r = myDB.rawQuery("select * from PDFBOOK"+ " where path  = '"+path+"' ", null);
					if(r.moveToFirst()){

						Snackbar.make(findViewById(android.R.id.content), "Already Exist in Bookmark!", Snackbar.LENGTH_LONG).show();

					}else {

						myDB.execSQL("insert into PDFBOOK values('"+image+"','"+name+"','"+data+"','"+date+"','"+path+"')");
						Snackbar.make(findViewById(android.R.id.content),"Bookmark Added", Snackbar.LENGTH_LONG).show();
					}


				}catch(Exception exc){

					Snackbar.make(findViewById(android.R.id.content),"Something went wrong!", Snackbar.LENGTH_LONG).show();
				}

				break;
		}

		return super.onOptionsItemSelected(item);
	}



	public String extracttext()
	{
		String word = "";
		TextWord[][] textWord = core.textLines(mDocView.getDisplayedViewIndex());
		int z, j;

		for (z = 0; z < textWord.length; z++) {
			for (j = 0; j < textWord[z].length; j++) {
				word = word + textWord[z][j].w + " ";
			}
		}
		return  word;
	}


	private View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {

				case R.id.fab_read_page:
                    back.close(true);
					String s = "";
					s = extracttext();
					if(s=="") Snackbar.make(MuPDFActivity.this.findViewById(android.R.id.content),"Can't read this page",Snackbar.LENGTH_LONG).show();

					if(!tts.isSpeaking()  && s!="") {
						final AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
						int a = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//						Log.e("ud",a+"");
						if(a==0)
						Snackbar.make(MuPDFActivity.this.findViewById(android.R.id.content),"Please increase volume",Snackbar.LENGTH_LONG).show();

						if(Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
							tts.speak(s, TextToSpeech.QUEUE_FLUSH, null, null);
						} else {
							tts.speak(s, TextToSpeech.QUEUE_FLUSH, null);
						}

						fabRead.setImageResource(R.drawable.ic_pause_circle_outline_white_24dp);
					}else {
						tts.stop();
						fabRead.setImageResource(R.drawable.ic_play_circle_outline_white_24dp);
					}


					break;

				case R.id.fab_extractImage:

//						int with = (int) (page.getWidth() * scaleBy);
//						int height = (int) (page.getHeight() * scaleBy);

						// you can change these values as you to zoom in/out
						// and even distort (scale without maintaining the aspect ratio)
						// the resulting images

						// Long running
					back.close(true);
//					mTopBarSwitcher.setVisibility(View.INVISIBLE);
//					mPageSlider.setVisibility(View.INVISIBLE);
//					back.setVisibility(View.INVISIBLE);
//					mPageNumberView.setVisibility(View.INVISIBLE);


					View view = mDocView.getDisplayedView();

              try {
				  if (false == view.isDrawingCacheEnabled()) {
					  view.setDrawingCacheEnabled(true);
				  }
			  }catch (Exception e){Log.e("EXC",e.toString());}
						Bitmap bitmap = getBitmapFromView(view);
//					Bitmap croppedBitmap = Bitmap.createBitmap(bitmap, 0, 10, bitmap.getWidth(), bitmap.getHeight() - 20);

						try {
							new File(Environment.getExternalStorageDirectory()+"/PDF Reader").mkdirs();
							File outputFile = new File(Environment.getExternalStorageDirectory()+"/PDF Reader", System.currentTimeMillis()+"_pdf.jpg");
							FileOutputStream outputStream = new FileOutputStream(outputFile);

							// a bit long running
							bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

							outputStream.close();
//							Snackbar.make(getWindow().getDecorView().getRootView(), "Image saved successfully...\n Location: "+outputFile.getAbsolutePath(), Snackbar.LENGTH_LONG).show();
							try{sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));}catch(Exception e){Log.e("Er gallery refresh",e.toString());}
                             startActivity(new Intent(MuPDFActivity.this, PDFSaveToDiskShow.class).putExtra("path",outputFile.getAbsolutePath()));
						} catch (IOException e) {
							Log.e("During IMAGE formation", e.toString());
							Snackbar.make(findViewById(android.R.id.content), "Somethig went wrong can't save...", Snackbar.LENGTH_LONG).show();
						}

					break;


				case R.id.fab_share:
					 back.close(true);

//					ArrayList<Uri> iuri =new ArrayList<Uri>();
//
//					File file11=new File(getIntent().getData().toString());
//					Uri uri11 = Uri.fromFile(file11);
//					iuri.add(uri11);
//					Intent iintent = new Intent();
//					iintent.setAction(Intent.ACTION_SEND_MULTIPLE);
//					iintent.setType("*/*");
// 					iintent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, iuri);
//					startActivity(iintent);
						try {
							File fileWithinMyDir = new File(getIntent().getData().toString());
							Intent intentShareFile = new Intent(Intent.ACTION_SEND);
							if (fileWithinMyDir.exists()) {
								intentShareFile.setType("application/pdf");
								intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + fileWithinMyDir.toString()));
								Snackbar.make(findViewById(android.R.id.content), "Select App to share...", Snackbar.LENGTH_LONG).show();
								intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
										"Sharing File...");
								intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");

								startActivity(Intent.createChooser(intentShareFile, "Share File"));
							}
						}catch (Exception exc){
							Snackbar.make(findViewById(android.R.id.content), "Something went wrong while sharing.", Snackbar.LENGTH_LONG).show();
							 Log.e("Share",exc.toString());
						}

					break;

				case R.id.fab11:
					       back.close(true);
							OutlineItem outline[] = core.getOutline();
							if (outline != null) {
								OutlineActivityData.get().items = outline;
								Intent intent = new Intent(MuPDFActivity.this,
										OutlineActivity.class);
								startActivityForResult(intent, 0);
							}

					break;

				case R.id.fab_search:
					searchModeOn();
					break;

			}
		}
	};


	private void connectTakeClickfab() {
		try {
			fabContent = (FloatingActionButton) findViewById(R.id.fab11);
			fabContent.setOnClickListener(clickListener);
			fabSearch = (FloatingActionButton) findViewById(R.id.fab_search);
			fabSearch.setOnClickListener(clickListener);
			fabShare = (FloatingActionButton) findViewById(R.id.fab_share);
			fabShare.setOnClickListener(clickListener);
			fabCreateImage = (FloatingActionButton) findViewById(R.id.fab_extractImage);
			fabCreateImage.setOnClickListener(clickListener);
			fabRead = (FloatingActionButton) findViewById(R.id.fab_read_page);
			fabRead.setOnClickListener(clickListener);

			fabRead.setVisibility(View.GONE);
			fabContent.setVisibility(View.GONE);
			fabCreateImage.setVisibility(View.GONE);
			fabSearch.setVisibility(View.GONE);
			fabShare.setVisibility(View.GONE);

		}catch (Exception s){Log.e("Can't nect FloatingBt",s.toString());}
	}


	public void requestPassword(final Bundle savedInstanceState) {
		mPasswordView = new EditText(this);
		mPasswordView.setInputType(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
		mPasswordView
				.setTransformationMethod(new PasswordTransformationMethod());

		AlertDialog alert = mAlertBuilder.create();
		alert.setTitle(R.string.enter_password);
		alert.setView(mPasswordView);
		alert.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (core.authenticatePassword(mPasswordView.getText()
								.toString())) {
							createUI(savedInstanceState);
						} else {
							requestPassword(savedInstanceState);
						}
					}
				});
		alert.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				});
		alert.show();
	}

	private void SaveImage(Bitmap finalBitmap) {

		String root = Environment.getExternalStorageDirectory().toString();
		File myDir = new File(root + "/."+getResources().getString(R.string.app_name).replace(" ",""));
		myDir.mkdirs();
		String fname = path.replace("/","_")+".jpg";
		File file = new File (myDir, fname);
		if (file.exists ()) file.delete ();
		try {
			FileOutputStream out = new FileOutputStream(file);
			finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void createUI(Bundle savedInstanceState) {

		if (core == null)
			return;
		// Now create the UI.
		// First create the document view making use of the ReaderView's
		// internal
		// gesture recognition
		
		//mDocView.setBackgroundColor(getResources().getColor(R.color.b_blk));
	
		
		
		mDocView = new ReaderView(this) {
			private boolean showButtonsDisabled;

			public boolean onSingleTapUp(MotionEvent e) {
				if (e.getX() < super.getWidth() / TAP_PAGE_MARGIN) {
					super.moveToPrevious();
				} else if (e.getX() > super.getWidth() * (TAP_PAGE_MARGIN - 1)
						/ TAP_PAGE_MARGIN) {
					super.moveToNext();
				} else if (!showButtonsDisabled) {
					int linkPage = -1;
					if (mLinkState != LinkState.INHIBIT) {
						MuPDFPageView pageView = (MuPDFPageView) mDocView
								.getDisplayedView();
//						pageView.setFocusable(true);



					//	pageView.setBackgroundResource(android.R.color.background_dark);
						
						
						if (pageView != null) {
							// XXX linkPage = pageView.hitLinkPage(e.getX(),
							// e.getY());
						}
					}

					if (linkPage != -1) {
						mDocView.setDisplayedViewIndex(linkPage);
					} else {
						if (!mButtonsVisible) {
							showButtons();
						} else {
							hideButtons();
						}
					}
				}
				return super.onSingleTapUp(e);
			}

			public boolean onScroll(MotionEvent e1, MotionEvent e2,
					float distanceX, float distanceY) {
				if (!showButtonsDisabled)
					hideButtons();

				return super.onScroll(e1, e2, distanceX, distanceY);
			}

			public boolean onTouchEvent(MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN)
					showButtonsDisabled = false;

				return super.onTouchEvent(event);
			}

			protected void onChildSetup(int i, View v) {
				if (SearchTaskResult.get() != null
						&& SearchTaskResult.get().pageNumber == i)
					((PageView) v)
							.setSearchBoxes(SearchTaskResult.get().searchBoxes);
				else
					((PageView) v).setSearchBoxes(null);

				((PageView) v)
						.setLinkHighlighting(mLinkState == LinkState.HIGHLIGHT);
			}

			protected void onMoveToChild(int i) {
				if (core == null)
					return;
				mPageNumberView.setText(String.format("%d/%d", i + 1,
						core.countPages()));
				mPageSlider.setMax((core.countPages() - 1));

				mPageSlider.setProgress(i);
				if (SearchTaskResult.get() != null
						&& SearchTaskResult.get().pageNumber != i) {
					SearchTaskResult.set(null);
					mDocView.resetupChildren();
				}
			}

			protected void onSettle(View v) {
				// When the layout has settled ask the page to render
				// in HQ
				((PageView) v).addHq();
			}

			protected void onUnsettle(View v) {
				// When something changes making the previous settled view
				// no longer appropriate, tell the page to remove HQ
				((PageView) v).removeHq();
			}
		};

		MuPDFPageAdapter adapter = new MuPDFPageAdapter(this, core);
//		adapter.
		mDocView.setAdapter(adapter);





		

		
		
		
		
		
		
		
		
		
		
		
		
		
		// Make the buttons overlay, and store all its
		// controls in variables
		makeButtonsView();
		// Activate the seekbar
		mPageSlider
				.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
					public void onStopTrackingTouch(SeekBar seekBar) {
						mDocView.setDisplayedViewIndex(seekBar.getProgress());
					}

					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						updatePageNumView(progress);
					}
				});
		
		

		
		

		// Activate the search-preparing button


		mCancelButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				searchModeOff();
			}
		});

		// Search invoking buttons are disabled while there is no text specified
		mSearchBack.setEnabled(false);
		mSearchFwd.setEnabled(false);

		// React to interaction with the text widget
		mSearchText.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
				boolean haveText = s.toString().length() > 0;
				mSearchBack.setEnabled(haveText);
				mSearchFwd.setEnabled(haveText);

				// Remove any previous search results
				if (SearchTaskResult.get() != null
						&& !mSearchText.getText().toString()
								.equals(SearchTaskResult.get().txt)) {
					SearchTaskResult.set(null);
					mDocView.resetupChildren();
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});

		// React to Done button on keyboard
		mSearchText
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_DONE)
							search(1);
						return false;
					}
				});

		mSearchText.setOnKeyListener(new View.OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN
						&& keyCode == KeyEvent.KEYCODE_ENTER)
					search(1);
				return false;
			}
		});
		/**������һЩ��������*/
		cutP.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				//��һЩ��ť����
				cutP.setVisibility(View.INVISIBLE);
				mTopBarSwitcher.setVisibility(View.INVISIBLE);
				mPageSlider.setVisibility(View.INVISIBLE);
				back.setVisibility(View.INVISIBLE);
				mPageNumberView.setVisibility(View.INVISIBLE);
				View view = MuPDFActivity.this.getWindow().getDecorView();
				if (false == view.isDrawingCacheEnabled()) {
					view.setDrawingCacheEnabled(true);
				}
				Bitmap bitmap = view.getDrawingCache();
				ImageView imgv = new ImageView(MuPDFActivity.this);
				imgv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
						LayoutParams.FILL_PARENT-200));
				imgv.setImageBitmap(bitmap);
				backBitmap = bitmap;
				//���ݸ���һ��Activity���вü�
				Intent intent = new Intent();
				intent.setClass(MuPDFActivity.this, CutActivity.class);
				startActivity(intent);

			}
			
		});

		back.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
			@Override
			public void onMenuToggle(boolean opened) {
				Log.e("sdfs","Sdf");

				if(fabRead==null) connectTakeClickfab();

				if( !opened) {
					fabRead.setVisibility(View.GONE);
					fabContent.setVisibility(View.GONE);
					fabCreateImage.setVisibility(View.GONE);
					fabSearch.setVisibility(View.GONE);
					fabShare.setVisibility(View.GONE);
				}else{
					fabRead.setVisibility(View.VISIBLE);
					fabContent.setVisibility(View.VISIBLE);
					fabCreateImage.setVisibility(View.VISIBLE);
					fabSearch.setVisibility(View.VISIBLE);
					fabShare.setVisibility(View.VISIBLE);
				}
			}
		});

		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//����ͼ��Ŀ¼
//				MuPDFActivity.this.finish();
//				MuPDFActivity.this.onDestroy();
//				onBackPressed();


		
			}
			
		});
		// Activate search invoking buttons
		mSearchBack.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				search(-1);
			}
		});
		mSearchFwd.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				search(1);
			}
		});

	


		// Reenstate last state if it was recorded
		SharedPreferences prefs = getSharedPreferences("remember", 0);      
	//	SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
		mDocView.setDisplayedViewIndex(prefs.getInt("page" + mFileName, 0));

		if (savedInstanceState == null
				|| !savedInstanceState.getBoolean("ButtonsHidden", false))
			showButtons();

		if (savedInstanceState != null
				&& savedInstanceState.getBoolean("SearchMode", false))
			searchModeOn();

		// Stick the document view and the buttons overlay into a parent view
		RelativeLayout layout = new RelativeLayout(this);
		//mDocView.setBackgroundResource(R.color.Grey);
		layout.addView(mDocView);
		layout.addView(mButtonsView);

	//layout.setBackgroundResource(R.drawable.pdet_pdet_bg);

		
		//layout.setBackgroundResource(R.drawable.pdet_tiled_background);
		
	
	
//	 layout.setBackgroundResource(R.color.Black);
	
	
		setContentView(layout);
		
		SharedPreferences setting =  PreferenceManager.getDefaultSharedPreferences(this);              //retrieve saved value;
		 boolean n = setting.getBoolean("full_screen", false);                //default 4       
		 
		 if(n==true)
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		


		 
		 toolbar1 = (Toolbar) findViewById(R.id.topBartool1);
		 toolbar2 = (Toolbar) findViewById(R.id.topBar2);
		 setSupportActionBar(toolbar1);
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(mFileName);


		pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		prefEditor = pref.edit();
		d_user_addfree_actual = pref.getBoolean(Key.PREFERNCE_ADFREE,false);

		if(!d_user_addfree_actual) {
			//Loading Aword ad
			adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
					.addTestDevice("C87242295BB59DB07A32E8637643B55E")
					.build();

			mInterstitialAd = new InterstitialAd(this);
			mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));

			mInterstitialAd.setAdListener(new AdListener() {
				@Override
				public void onAdClosed() {
					requestNewInterstitial();
//                beginPlayingGame();
				}

				public void onAdLoaded() {
					if (isActivityIsVisible && !d_user_addfree_actual) {
						mInterstitialAd.show();
					}
				}
			});
			requestNewInterstitial();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode >= 0)
			mDocView.setDisplayedViewIndex(resultCode);
		super.onActivityResult(requestCode, resultCode, data);
	}

//	public Object onRetainNonConfigurationInstance() {
//		MuPDFCore mycore = core;
//		core = null;
//		return mycore;
//	}
	
	public Object onRetainCustomNonConfigurationInstance() {
		MuPDFCore mycore = core;
		core = null;
		return mycore;
	}
	

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		if (mFileName != null && mDocView != null) {
			outState.putString("FileName", mFileName);

			// Store current page in the prefs against the file name,
			// so that we can pick it up each time the file is loaded
			// Other info is needed only for screen-orientation change,
			// so it can go in the bundle
	
			
			SharedPreferences prefs = getSharedPreferences("remember", 0);      
			//SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
			SharedPreferences.Editor edit = prefs.edit();
			edit.putInt("page" + mFileName, mDocView.getDisplayedViewIndex());
			edit.commit();
		}

		if (!mButtonsVisible)
			outState.putBoolean("ButtonsHidden", true);

		if (mTopBarIsSearch)
			outState.putBoolean("SearchMode", true);
	}




	@Override
	protected void onStop() {
		if(tts!=null) if(tts.isSpeaking()) tts.stop();
		super.onStop();

	}

	void showButtons() {
		if (core == null)
			return;
		if (!mButtonsVisible) {
			mButtonsVisible = true;
			// Update page number text and slider
			int index = mDocView.getDisplayedViewIndex();
			updatePageNumView(index);
			mPageSlider.setMax(core.countPages() - 1);
			mPageSlider.setProgress(index);
			if (mTopBarIsSearch) {
				mSearchText.requestFocus();
				showKeyboard();
			}

			Animation anim = new TranslateAnimation(0, 0,
					-mTopBarSwitcher.getHeight(), 0);
			anim.setDuration(200);
			anim.setAnimationListener(new Animation.AnimationListener() {
				public void onAnimationStart(Animation animation) {
					mTopBarSwitcher.setVisibility(View.VISIBLE);
				}

				public void onAnimationRepeat(Animation animation) {
				}

				public void onAnimationEnd(Animation animation) {
				}
			});
			mTopBarSwitcher.startAnimation(anim);

			anim = new TranslateAnimation(0, 0, mPageSlider.getHeight(), 0);
			anim.setDuration(200);
			anim.setAnimationListener(new Animation.AnimationListener() {
				public void onAnimationStart(Animation animation) {
					mPageSlider.setVisibility(View.VISIBLE);
				}

				public void onAnimationRepeat(Animation animation) {
				}

				public void onAnimationEnd(Animation animation) {
					mPageNumberView.setVisibility(View.VISIBLE);
				}
			});
			mPageSlider.startAnimation(anim);
			
			anim = new TranslateAnimation(0, 0, cutP.getHeight(), 0);
			anim.setDuration(200);
			anim.setAnimationListener(new Animation.AnimationListener() {
				public void onAnimationStart(Animation animation) {
					cutP.setVisibility(View.VISIBLE);
				}

				public void onAnimationRepeat(Animation animation) {
				}

				public void onAnimationEnd(Animation animation) {
					cutP.setVisibility(View.VISIBLE);
				}
			});
			cutP.startAnimation(anim);
			
			anim = new TranslateAnimation(0, 0, back.getHeight(), 0);
			anim.setDuration(200);
			anim.setAnimationListener(new Animation.AnimationListener() {
				public void onAnimationStart(Animation animation) {
					back.setVisibility(View.VISIBLE);
				}

				public void onAnimationRepeat(Animation animation) {
				}

				public void onAnimationEnd(Animation animation) {
					back.setVisibility(View.VISIBLE);
				}
			});
			back.startAnimation(anim);
		}
	}

	void hideButtons() {
		if (mButtonsVisible) {
			mButtonsVisible = false;
			hideKeyboard();

			Animation anim = new TranslateAnimation(0, 0, 0,
					-mTopBarSwitcher.getHeight());
			anim.setDuration(200);
			anim.setAnimationListener(new Animation.AnimationListener() {
				public void onAnimationStart(Animation animation) {
				}

				public void onAnimationRepeat(Animation animation) {
				}

				public void onAnimationEnd(Animation animation) {
					mTopBarSwitcher.setVisibility(View.INVISIBLE);
				}
			});
			mTopBarSwitcher.startAnimation(anim);

			anim = new TranslateAnimation(0, 0, 0, mPageSlider.getHeight());
			anim.setDuration(200);
			anim.setAnimationListener(new Animation.AnimationListener() {
				public void onAnimationStart(Animation animation) {
					mPageNumberView.setVisibility(View.INVISIBLE);
				}

				public void onAnimationRepeat(Animation animation) {
				}

				public void onAnimationEnd(Animation animation) {
					mPageSlider.setVisibility(View.INVISIBLE);
				}
			});
			mPageSlider.startAnimation(anim);
			
			anim = new TranslateAnimation(0, 0, 0, cutP.getHeight());
			anim.setDuration(200);
			anim.setAnimationListener(new Animation.AnimationListener() {
				public void onAnimationStart(Animation animation) {
					cutP.setVisibility(View.INVISIBLE);
				}

				public void onAnimationRepeat(Animation animation) {
				}

				public void onAnimationEnd(Animation animation) {
					cutP.setVisibility(View.INVISIBLE);
				}
			});
            cutP.startAnimation(anim);
			anim = new TranslateAnimation(0, 0, 0, back.getHeight());
			anim.setDuration(200);
			anim.setAnimationListener(new Animation.AnimationListener() {
				public void onAnimationStart(Animation animation) {
					back.setVisibility(View.INVISIBLE);
				}

				public void onAnimationRepeat(Animation animation) {
				}

				public void onAnimationEnd(Animation animation) {
					back.setVisibility(View.INVISIBLE);
				}
			});
			back.startAnimation(anim);
		}
	}

	void searchModeOn() {
		mTopBarIsSearch = true;
		// Focus on EditTextWidget
		mSearchText.requestFocus();
		showKeyboard();
		mTopBarSwitcher.showNext();
	}

	void searchModeOff() {
		mTopBarIsSearch = false;
		hideKeyboard();
		mTopBarSwitcher.showPrevious();
		SearchTaskResult.set(null);
		// Make the ReaderView act on the change to mSearchTaskResult
		// via overridden onChildSetup method.
		mDocView.resetupChildren();
	}

	void updatePageNumView(int index) {
		if (core == null)
			return;
		mPageNumberView.setText(String.format("%d/%d", index + 1,
				core.countPages()));
	}

	void makeButtonsView() {
		mButtonsView = getLayoutInflater().inflate(R.layout.pdet_buttons, null);
		mPageSlider = (SeekBar) mButtonsView.findViewById(R.id.pageSlider);
		mPageNumberView = (TextView) mButtonsView.findViewById(R.id.pageNumber);
		mCancelButton = (ImageButton) mButtonsView.findViewById(R.id.cancel);

		mTopBarSwitcher = (ViewSwitcher) mButtonsView
				.findViewById(R.id.switcher);
		mSearchBack = (ImageButton) mButtonsView.findViewById(R.id.searchBack);
		mSearchFwd = (ImageButton) mButtonsView
				.findViewById(R.id.searchForward);
		mSearchText = (EditText) mButtonsView.findViewById(R.id.searchText);
		cutP = (ImageView)mButtonsView.findViewById(R.id.cutP);
		//System.out.println(cutP);
		cutP.setVisibility(View.INVISIBLE);
		back = (FloatingActionMenu) mButtonsView.findViewById(R.id.goback);
		back.setVisibility(View.VISIBLE);
		mTopBarSwitcher.setVisibility(View.INVISIBLE);
		mPageNumberView.setVisibility(View.INVISIBLE);
		mPageSlider.setVisibility(View.INVISIBLE);
	}

	void showKeyboard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null)
			imm.showSoftInput(mSearchText, 0);
	}

	void hideKeyboard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null)
			imm.hideSoftInputFromWindow(mSearchText.getWindowToken(), 0);
	}

	void killSearch() {
		if (mSearchTask != null) {
			mSearchTask.cancel(true);
			mSearchTask = null;
		}
	}

	void search(int direction) {
		hideKeyboard();
		if (core == null)
			return;
		killSearch();

	
//		
		final ProgressDialogX progressDialog = new ProgressDialogX(MuPDFActivity.this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		//progressDialog.setProgressStyle(R.style.Widget_AppCompat_ProgressBar_Horizontal);
		progressDialog.setTitle(getString(R.string.searching_));
		progressDialog
				.setOnCancelListener(new DialogInterface.OnCancelListener() {
					public void onCancel(DialogInterface dialog) {
						killSearch();
					}
				});
		progressDialog.setMax(core.countPages());

		mSearchTask = new AsyncTask<Integer, Integer, SearchTaskResult>() {
			@Override
			protected SearchTaskResult doInBackground(Integer... params) {
				int index;
				if (SearchTaskResult.get() == null)
					index = mDocView.getDisplayedViewIndex();
				else
					index = SearchTaskResult.get().pageNumber
							+ params[0].intValue();

				while (0 <= index && index < core.countPages()
						&& !isCancelled()) {
					publishProgress(index);
					RectF searchHits[] = core.searchPage(index, mSearchText
							.getText().toString());

					if (searchHits != null && searchHits.length > 0)
						return new SearchTaskResult(mSearchText.getText()
								.toString(), index, searchHits);

					index += params[0].intValue();
				}
				return null;
			}

			@Override
			protected void onPostExecute(SearchTaskResult result) {
				progressDialog.cancel();
				if (result != null) {
					// Ask the ReaderView to move to the resulting page
					mDocView.setDisplayedViewIndex(result.pageNumber);
					SearchTaskResult.set(result);
					// Make the ReaderView act on the change to
					// mSearchTaskResult
					// via overridden onChildSetup method.
					mDocView.resetupChildren();
				} else {
					mAlertBuilder.setTitle(R.string.text_not_found);
					AlertDialog alert = mAlertBuilder.create();
					alert.setButton(AlertDialog.BUTTON_POSITIVE, "Dismiss",
							(DialogInterface.OnClickListener) null);
					alert.show();
				}
			}



			@Override
			protected void onCancelled() {
				super.onCancelled();
				progressDialog.cancel();
			}

			@Override
			protected void onProgressUpdate(Integer... values) {
				super.onProgressUpdate(values);
				progressDialog.setProgress(values[0].intValue());
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				mHandler.postDelayed(new Runnable() {
					public void run() {
						if (!progressDialog.isCancelled())
							progressDialog.show();
					}
				}, SEARCH_PROGRESS_DELAY);
			}
		};

		mSearchTask.execute(new Integer(direction));
	}
	public static Bitmap backBitmap = null;

	public static Bitmap getBitmapFromView(View view) {
		//Define a bitmap with the same size as the view
		Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
		//Bind a canvas to it
		Canvas canvas = new Canvas(returnedBitmap);
		//Get the view's background
		Drawable bgDrawable =view.getBackground();
		if (bgDrawable!=null)
			//has background drawable, then draw it on the canvas
			bgDrawable.draw(canvas);
		else
			//does not have background drawable, then draw white background on the canvas
			canvas.drawColor(Color.WHITE);
		// draw the view on the canvas
		view.draw(canvas);
		//return the bitmap
		return returnedBitmap;
	}
}
