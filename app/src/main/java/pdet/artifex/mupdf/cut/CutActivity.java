package pdet.artifex.mupdf.cut;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

//import pdet.email.Welcome;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;


import com.artifex.mupdfdemo.Crop_Canvas;
import com.artifex.mupdfdemo.MuPDFActivity;
import com.pdf.reader.pro.ebin.R;

public class CutActivity extends Activity {
	private Crop_Canvas canvas = null;
	private Bitmap backBitmap;
	private Button toPDF;
	private Button sendEmail;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.pdet_cut_image);
		backBitmap = MuPDFActivity.backBitmap;
		init();
	
        sendEmail = (Button)findViewById(R.id.send_email);
        /**
         * �Ը�������ʽ���Ͳü���ͼƬ��ת���ɵ�pdf�ĵ�*/
        sendEmail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//�����Լ��ʼ��ͻ��˽��з���.
				Intent intent = new Intent();
		//		intent.setClass(CutActivity.this, Welcome.class);
				startActivity(intent);//������Ҫ��������ϢҲ����ȥ
			}
		});
		toPDF = (Button)findViewById(R.id.toPDF);
		toPDF.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//ͼƬ�����·����֮��֮ת��ΪPDF�����Ը��������Ʒ����ʼ�
				File tmp = new File("/sdcard/lovereader/pic");
				tmp.mkdirs();
				File f = new File("/sdcard/lovereader/pic/" + "testpic" + ".png");
		        try {
					f.createNewFile();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		        FileOutputStream fOut = null;
		        try {
		                fOut = new FileOutputStream(f);
		        } catch (FileNotFoundException e) {
		                e.printStackTrace();
		        }
		        canvas.getSubsetBitmap().compress(Bitmap.CompressFormat.PNG, 100, fOut);
		        try {
		                fOut.flush();
		        } catch (IOException e) {
		                e.printStackTrace();
		        }
		        try {
		                fOut.close();
		        } catch (IOException e) {
		                e.printStackTrace();
		        }
			
				// TODO Auto-generated method stub
				ArrayList<String> imageUrllist = new ArrayList<String>();
				imageUrllist.add("/sdcard/lovereader/pic/" + "testpic" + ".png");
				String pdfUrl = "/sdcard/lovereader/tmp/Foreverlove.pdf";
				File tmp2 = new File("/sdcard/lovereader/tmp");
				tmp2.mkdirs();
				File file = PdfManager.Pdf(imageUrllist, pdfUrl);
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		});
	}

	private void init() {
		canvas = (Crop_Canvas) findViewById(R.id.myCanvas);
		Bitmap bitmap = backBitmap;
		canvas.setBitmap(bitmap);
	}

}
