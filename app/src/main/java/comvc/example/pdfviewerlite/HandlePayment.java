package comvc.example.pdfviewerlite;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

import comvc.util.IabHelper;
import comvc.util.IabResult;
import comvc.util.Inventory;
import comvc.util.Purchase;
import transaction.pdf.util.Key;


/**
 * Created by mac on 07/02/17.
 */

public class HandlePayment {

    public void perfromAdfreePayMent(final Context mainViewActivityContext, final IabHelper mHelper, SharedPreferences pref, final SharedPreferences.Editor prefEditor){
//         final String ITEM_SKU = "android.test.purchased";
        final String ITEM_SKU = "remove_ads";
        IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
                = new IabHelper.OnIabPurchaseFinishedListener() {
            public void onIabPurchaseFinished(IabResult result,
                                              Purchase purchase)
            {
                if (result.isFailure()) {
                    // Handle error
                    Log.e("123","Failure");

                    int n = result.getResponse();
                    if(n == 7){
                        prefEditor.putBoolean(Key.PREFERNCE_ADFREE,true).commit();
                        if(((ActivityFront) mainViewActivityContext).mAdView != null) {
                            ((ActivityFront) mainViewActivityContext).mAdView.setVisibility(View.GONE);
                            ((ActivityFront) mainViewActivityContext).d_user_addfree_actual = true;
                        }
                    }

                    return;
                }
                else if (purchase.getSku().equals(ITEM_SKU)) {
                    Log.e("123","PURCAsed");

                    //error Should save local first
                    prefEditor.putBoolean(Key.PREFERNCE_ADFREE,true).commit();
                    if(((ActivityFront) mainViewActivityContext).mAdView != null) {
                        ((ActivityFront) mainViewActivityContext).mAdView.setVisibility(View.GONE);
                        ((ActivityFront) mainViewActivityContext).d_user_addfree_actual = true;
                    }
                    consumeItem(mHelper); //CONSUME PURCHASED ITEM SO RESET
//
                }

            }
        };

        Log.e("mainAct1","pay-end");
        mHelper.enableDebugLogging(true, "TAG");
        if (mHelper != null) mHelper.flagEndAsync();
        mHelper.launchPurchaseFlow((Activity) mainViewActivityContext, ITEM_SKU, 1001, mPurchaseFinishedListener, ITEM_SKU);
        Log.e("mainAct2","pay-end");
    }

    public  void consumeItem(final IabHelper mHelper) {
        final String ITEM_SKU = "remove_ads";
        final IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
                new IabHelper.OnConsumeFinishedListener() {
                    public void onConsumeFinished(Purchase purchase,
                                                  IabResult result) {

                        if (result.isSuccess()) {
                        } else {
                            // handle error
                            Log.e("11","sucConsume");
                        }
                    }
                };

        IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener
                = new IabHelper.QueryInventoryFinishedListener() {
            public void onQueryInventoryFinished(IabResult result,
                                                 Inventory inventory) {


                if (result.isFailure()) {
                    // Handle failure
                    Log.e("11","Failure");



                } else {
                    Log.e("11","suc");
                    mHelper.consumeAsync(inventory.getPurchase(ITEM_SKU), mConsumeFinishedListener);
                }


            }
        };



        mHelper.queryInventoryAsync(mReceivedInventoryListener);

    }




}
