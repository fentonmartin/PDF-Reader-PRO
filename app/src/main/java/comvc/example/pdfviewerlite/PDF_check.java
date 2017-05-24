package comvc.example.pdfviewerlite;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

public class PDF_check {
	  public static final String MIME_TYPE_PDF = "application/pdf";

	  /**
	   * Check if the supplied context can render PDF files via some installed application that reacts to a intent
	   * with the pdf mime type and viewing action.
	   *
	   * @param context
	   * @return
	   */
	  public  boolean canDisplayPdf(Context context) {
	      PackageManager packageManager = context.getPackageManager();
	      Intent testIntent = new Intent(Intent.ACTION_VIEW);
	      testIntent.setType(MIME_TYPE_PDF);
	      if (packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0) {
	          return true;
	      } else {
	          return false;
	      }
}
}