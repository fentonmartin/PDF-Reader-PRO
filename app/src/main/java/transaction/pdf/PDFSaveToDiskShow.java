package transaction.pdf;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.github.clans.fab.FloatingActionButton;
import com.pdf.reader.pro.ebin.R;

import java.io.File;

public class PDFSaveToDiskShow extends AppCompatActivity {

    ImageView im;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfsave_to_disk_show);
        im = (ImageView) findViewById(R.id.imageView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Saved PDF as Image");

        FloatingActionButton fab_view = (FloatingActionButton) findViewById(R.id.fab22);
        fab_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imgPath = getIntent().getStringExtra("path");
                if(imgPath!=null){
                        try{
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.parse(imgPath), "image/*");
                            startActivity(intent);

                        }catch (Exception exc){
                            Snackbar.make(findViewById(android.R.id.content),"Sorry, Something went wrong.",Snackbar.LENGTH_LONG);
                        }
                }
            }
        });
        FloatingActionButton fab_del = (FloatingActionButton) findViewById(R.id.fab33_del);
        fab_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imgPath = getIntent().getStringExtra("path");
                if(imgPath!=null){
                    try{
                       new File(imgPath).delete();
                        onBackPressed();

                    }catch (Exception exc){
                        Snackbar.make(findViewById(android.R.id.content),"Sorry, Something went wrong.",Snackbar.LENGTH_LONG);
                    }
                }
            }
        });

        FloatingActionButton fab_share = (FloatingActionButton) findViewById(R.id.fab2);
        fab_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imgPath = getIntent().getStringExtra("path");
                if(imgPath!=null){
                  try {
                      Intent intent = new Intent(Intent.ACTION_SEND);
                      intent.setType("image/jpeg");
                      intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(imgPath));
                      startActivity(Intent.createChooser(intent, "Share Image"));
                  }catch (Exception exc){
                      Snackbar.make(findViewById(android.R.id.content),"Sorry, Something went wrong.",Snackbar.LENGTH_LONG);
                  };
                }
            }
        });

        String imgPath = getIntent().getStringExtra("path");
        if(imgPath!=null){
          im.setTag(imgPath);
           new Loadlocalimage(im).execute();
        }
    }


    class Loadlocalimage extends AsyncTask<Object, Void, Bitmap> {

        private ImageView imv;
        private String path;

        public Loadlocalimage(ImageView imv) {
            this.imv = imv;
            this.path = imv.getTag().toString();
        }

        @Override
        protected Bitmap doInBackground(Object... params) {
            Bitmap bitmap = null;
            File file = new File(path);

            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (!imv.getTag().toString().equals(path)) {
           /* The path is not same. This means that this
              image view is handled by some other async task.
              We don't do anything and return. */
                return;
            }
            try {
                if (result != null && imv != null) {
                    imv.setVisibility(View.VISIBLE);
                    imv.setImageBitmap(result);
                } else {
                    imv.setVisibility(View.GONE);
                }

            } catch (Exception s) {
            }
        }
    }
}
