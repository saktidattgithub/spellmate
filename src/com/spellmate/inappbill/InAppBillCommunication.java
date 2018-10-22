package com.spellmate.inappbill;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;



public class InAppBillCommunication {

	static final String TAG = "TrivialDrive";

	// Does the user have the premium upgrade?
	boolean mIsPremium = false;

	// Does the user have an active subscription to the infinite gas plan?
	boolean mSubscribedToInfiniteGas = false;

	// SKUs for our products: the premium upgrade (non-consumable) and gas (consumable)
	static final String SKU_PREMIUM = "premium";
	static final String SKU_GAS = "gas";

	// SKU for our subscription (infinite gas)
	static final String SKU_INFINITE_GAS = "infinite_gas";

	// (arbitrary) request code for the purchase flow
	static final int RC_REQUEST = 10001;

	// Graphics for the gas gauge
	/* static int[] TANK_RES_IDS = { R.drawable.gas0, R.drawable.gas1, R.drawable.gas2,
	                                   R.drawable.gas3, R.drawable.gas4 };*/

	// How many units (1/4 tank is our unit) fill in the tank.
	static final int TANK_MAX = 4;

	// Current amount of gas in tank, in units
	int mTank;
	private Activity inappActivity;
	// The helper object
	public IabHelper mHelper;

	//Constructor created by sakti
	private ProgressDialog dialog; 
	private String SKU_PALEO;
	public InAppBillCommunication(Activity activity)
	{
		inappActivity=activity;
		//SKU_PALEO=productid;
		// loadData();

		/* base64EncodedPublicKey should be YOUR APPLICATION'S PUBLIC KEY
		 * (that you got from the Google Play developer console). This is not your
		 * developer public key, it's the *app-specific* public key.
		 *
		 * Instead of just storing the entire literal string here embedded in the
		 * program,  construct the key at runtime from pieces or
		 * use bit manipulation (for example, XOR with some other string) to hide
		 * the actual key.  The key itself is not secret information, but we don't
		 * want to make it easy for an attacker to replace the public key with one
		 * of their own and then fake messages from the server.
		 */
		String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA4xYPy9G6Di4if26UYOxKdzUofr1RnuwjTV2jDyqG5up7xwGF6RdaOj6PFh48FjUWwsb4pBn6BIoWTrGZBQSvGvtAsXLcZ5DTQrW6Ur7LwS5fug0gYL5Fv8EdIWu3Wg6i9+VyVX/R3IKCJbPHldi96nPvK9PwQMBG9QMSRg1NA+x422R5IoDlmkKmqkA2UY5122Xc/ILOl7lnjR5C3qIeYx4kWSu8TJ1ooXsQH11tyFP2irswDXLuswCWfVtOIh9VXTIaWd9zWDabqX1KImchHurNhPAQuN1XzzVbsHQPmw1rmcIadWpxsKrzVcl31crMMHcF8B+JTJqAJQUwuUJVTQIDAQAB";

		// Some sanity checks to see if the developer (that's you!) really followed the
		// instructions to run this sample (don't put these checks on your app!)
		if (base64EncodedPublicKey.contains("CONSTRUCT_YOUR")) {
			throw new RuntimeException("Please put your app's public key in MainActivity.java. See README.");
		}
		if (inappActivity.getPackageName().startsWith("com.example")) {
			throw new RuntimeException("Please change the sample's package name! See README.");
		}

		// Create the helper, passing it our context and the public key to verify signatures with
		Log.d(TAG, "Creating IAB helper.");
		mHelper = new IabHelper(inappActivity, base64EncodedPublicKey);

		// enable debug logging (for a production application, you should set this to false).
		mHelper.enableDebugLogging(true);

		// Start setup. This is asynchronous and the specified listener
		// will be called once setup completes.
		Log.d(TAG, "Starting setup.");
		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			public void onIabSetupFinished(IabResult result) {
				Log.d(TAG, "Setup finished.");

				if (!result.isSuccess()) {
					// Oh noes, there was a problem.
					complain("Problem setting up in-app billing: " + result);
					return;
				}

				// Hooray, IAB is fully set up. Now, let's get an inventory of stuff we own.
				Log.v(TAG, "Setup successful. Querying inventory.");
				// mHelper.queryInventoryAsync(mGotInventoryListener);
			}
		});

	}

	// Listener that's called when we finish querying the items and subscriptions we own
	/* IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");
            if (result.isFailure()) {
                complain("Failed to query inventory: " + result);
                return;
            }

            Log.d(TAG, "Query inventory was successful.");


	 * Check for items we own. Notice that for each purchase, we check
	 * the developer payload to see if it's correct! See
	 * verifyDeveloperPayload().


            // Do we have the premium upgrade?
            Purchase premiumPurchase = inventory.getPurchase(SKU_PREMIUM);
            mIsPremium = (premiumPurchase != null && verifyDeveloperPayload(premiumPurchase));
            Log.d(TAG, "User is " + (mIsPremium ? "PREMIUM" : "NOT PREMIUM"));

            // Do we have the infinite gas plan?
            Purchase infiniteGasPurchase = inventory.getPurchase(SKU_INFINITE_GAS);
            mSubscribedToInfiniteGas = (infiniteGasPurchase != null && 
                    verifyDeveloperPayload(infiniteGasPurchase));
            Log.d(TAG, "User " + (mSubscribedToInfiniteGas ? "HAS" : "DOES NOT HAVE") 
                        + " infinite gas subscription.");
            if (mSubscribedToInfiniteGas) mTank = TANK_MAX;

            // Check for gas delivery -- if we own gas, we should fill up the tank immediately
            Purchase gasPurchase = inventory.getPurchase(SKU_GAS);
            if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
                Log.d(TAG, "We have gas. Consuming it.");
                mHelper.consumeAsync(inventory.getPurchase(SKU_GAS), mConsumeFinishedListener);
                return;
            }

           // updateUi();
           // setWaitScreen(false);
            Log.d(TAG, "Initial inventory query finished; enabling main UI.");
        }
    };*/

	// User clicked the "Buy Gas" button
	public void onBuy(String SKU_PALEO) {
		Log.d(TAG, "Paleo app buy resqest");
		this.SKU_PALEO=SKU_PALEO;
		/* if (mSubscribedToInfiniteGas) {
            complain("No need! You're subscribed to infinite gas. Isn't that awesome?");
            return;
        }

        if (mTank >= TANK_MAX) {
            complain("Your tank is full. Drive around a bit!");
            return;
        }*/

		// launch the gas purchase UI flow.
		// We will be notified of completion via mPurchaseFinishedListener
		// setWaitScreen(true);
		Log.d(TAG, "Launching purchase flow for product.");

		/* TODO: for security, generate your payload here for verification. See the comments on 
		 *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use 
		 *        an empty string, but on a production app you should carefully generate this. */
		String payload = "PaleoAppPurchages"; 

		mHelper.launchPurchaseFlow(inappActivity, SKU_PALEO, RC_REQUEST, 
				mPurchaseFinishedListener, payload);
	}

	// User clicked the "Upgrade to Premium" button.

	/* public void onUpgradeAppButtonClicked(View arg0) {
        Log.d(TAG, "Upgrade button clicked; launching purchase flow for upgrade.");
        setWaitScreen(true);

         TODO: for security, generate your payload here for verification. See the comments on 
	 *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use 
	 *        an empty string, but on a production app you should carefully generate this. 
        String payload = ""; 

        mHelper.launchPurchaseFlow(inappActivity, SKU_PREMIUM, RC_REQUEST, 
                mPurchaseFinishedListener, payload);
    }*/


	// "Subscribe to infinite gas" button clicked. Explain to user, then start purchase
	// flow for subscription.


	/* public void onInfiniteGasButtonClicked(View arg0) {
        if (!mHelper.subscriptionsSupported()) {
            complain("Subscriptions not supported on your device yet. Sorry!");
            return;
        }

         TODO: for security, generate your payload here for verification. See the comments on 
	 *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use 
	 *        an empty string, but on a production app you should carefully generate this. 
        String payload = ""; 

        setWaitScreen(true);
        Log.d(TAG, "Launching purchase flow for infinite gas subscription.");
        mHelper.launchPurchaseFlow(inappActivity,
                SKU_INFINITE_GAS, IabHelper.ITEM_TYPE_SUBS, 
                RC_REQUEST, mPurchaseFinishedListener, payload);        
    }
	 */
	/* @Override
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
    }*/

	/** Verifies the developer payload of a purchase. */
	boolean verifyDeveloperPayload(Purchase p) {
		String payload = p.getDeveloperPayload();

		/*
		 * TODO: verify that the developer payload of the purchase is correct. It will be
		 * the same one that you sent when initiating the purchase.
		 * 
		 * WARNING: Locally generating a random string when starting a purchase and 
		 * verifying it here might seem like a good approach, but this will fail in the 
		 * case where the user purchases an item on one device and then uses your app on 
		 * a different device, because on the other device you will not have access to the
		 * random string you originally generated.
		 *
		 * So a good developer payload has these characteristics:
		 * 
		 * 1. If two different users purchase an item, the payload is different between them,
		 *    so that one user's purchase can't be replayed to another user.
		 * 
		 * 2. The payload must be such that you can verify it even when the app wasn't the
		 *    one who initiated the purchase flow (so that items purchased by the user on 
		 *    one device work on other devices owned by the user).
		 * 
		 * Using your own server to store and verify developer payloads across app
		 * installations is recommended.
		 */

		return true;
	}

	// Callback for when a purchase is finished


	IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
		public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
			Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);
			if (result.isFailure()) {
				complain("Error purchasing: " + result);
				setWaitScreen(false);
				return;
			}
			if (!verifyDeveloperPayload(purchase)) {
				complain("Error purchasing. Authenticity verification failed.");
				setWaitScreen(false);
				return;
			}

			Log.d(TAG, "Purchase successful.");

			 if (purchase.getSku().equals(SKU_PALEO)) {
                // bought 1/4 tank of gas. So consume it.
                Log.d(TAG, "Purchase is paleo recipe product");
                //inappActivity.startActivity(inappActivity.getIntent());
               // mHelper.consumeAsync(purchase, mConsumeFinishedListener);
            }
			/* else if (purchase.getSku().equals(SKU_PREMIUM)) {
                // bought the premium upgrade!
                Log.d(TAG, "Purchase is premium upgrade. Congratulating user.");
                alert("Thank you for upgrading to premium!");
                mIsPremium = true;
                updateUi();
                setWaitScreen(false);
            }
            else if (purchase.getSku().equals(SKU_INFINITE_GAS)) {
                // bought the infinite gas subscription
                Log.d(TAG, "Infinite gas subscription purchased.");
                alert("Thank you for subscribing to infinite gas!");
                mSubscribedToInfiniteGas = true;
                mTank = TANK_MAX;
                updateUi();
                setWaitScreen(false);
            }*/
		}
	};

	// Called when consumption is complete
	/* IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);

            // We know this is the "gas" sku because it's the only one we consume,
            // so we don't check which sku was consumed. If you have more than one
            // sku, you probably should check...
            if (result.isSuccess()) {
                // successfully consumed, so we apply the effects of the item in our
                // game world's logic, which in our case means filling the gas tank a bit
                Log.d(TAG, "Consumption successful. Provisioning.");
                mTank = mTank == TANK_MAX ? TANK_MAX : mTank + 1;
                saveData();
                alert("You filled 1/4 tank. Your tank is now " + String.valueOf(mTank) + "/4 full!");
            }
            else {
                complain("Error while consuming: " + result);
            }
            //updateUi();
            setWaitScreen(false);
            Log.d(TAG, "End consumption flow.");
        }
    };*/

	// Drive button clicked. Burn gas!
	/* public void onDriveButtonClicked(View arg0) {
        Log.d(TAG, "Drive button clicked.");
        if (!mSubscribedToInfiniteGas && mTank <= 0) alert("Oh, no! You are out of gas! Try buying some!");
        else {
            if (!mSubscribedToInfiniteGas) --mTank;
            saveData();
            alert("Vroooom, you drove a few miles.");
           // updateUi();
            Log.d(TAG, "Vrooom. Tank is now " + mTank);
        }
    }*/

	// We're being destroyed. It's important to dispose of the helper here!
	/* @Override
    public void onDestroy() {
        super.onDestroy();

        // very important:
        Log.d(TAG, "Destroying helper.");
        if (mHelper != null) mHelper.dispose();
        mHelper = null;
    }*/

	public void destroy_inappservice()
	{
		Log.d(TAG, "Destroying helper.");
		if (mHelper != null) mHelper.dispose();
		mHelper = null;	
	}

	// updates UI to reflect model
	/* public void updateUi() {
        // update the car color to reflect premium status or lack thereof
        ((ImageView)findViewById(R.id.free_or_premium)).setImageResource(mIsPremium ? R.drawable.premium : R.drawable.free);

        // "Upgrade" button is only visible if the user is not premium
        findViewById(R.id.upgrade_button).setVisibility(mIsPremium ? View.GONE : View.VISIBLE);

        // "Get infinite gas" button is only visible if the user is not subscribed yet
        findViewById(R.id.infinite_gas_button).setVisibility(mSubscribedToInfiniteGas ? 
                View.GONE : View.VISIBLE);

        // update gas gauge to reflect tank status
        if (mSubscribedToInfiniteGas) {
            ((ImageView)findViewById(R.id.gas_gauge)).setImageResource(R.drawable.gas_inf);
        }
        else {
            int index = mTank >= TANK_RES_IDS.length ? TANK_RES_IDS.length - 1 : mTank;
            ((ImageView)findViewById(R.id.gas_gauge)).setImageResource(TANK_RES_IDS[index]);
        }        
    }*/

	// Enables or disables the "please wait" screen.
	void setWaitScreen(boolean set) {
		/*findViewById(R.id.screen_main).setVisibility(set ? View.GONE : View.VISIBLE);
        findViewById(R.id.screen_wait).setVisibility(set ? View.VISIBLE : View.GONE);*/
		if(set)
		{
			dialog = new ProgressDialog(inappActivity);
			dialog.setTitle("Processing...");
			dialog.setMessage("Please wait.");
			dialog.setCancelable(false);
			dialog.setIndeterminate(true);
			dialog.show();
		}
	}

	void complain(String message) {
		Log.e(TAG, "****  Error: " + message);
		alert("Error: " + message);
	}

	void alert(String message) {
		AlertDialog.Builder bld = new AlertDialog.Builder(inappActivity);
		bld.setMessage(message);
		bld.setNeutralButton("OK", null);
		Log.d(TAG, "Showing alert dialog: " + message);
		bld.create().show();
	}

	void saveData() {

		/*
		 * WARNING: on a real application, we recommend you save data in a secure way to
		 * prevent tampering. For simplicity in this sample, we simply store the data using a
		 * SharedPreferences.
		 */

		SharedPreferences.Editor spe = inappActivity.getPreferences(inappActivity.MODE_PRIVATE).edit();
		spe.putInt("tank", mTank);
		spe.commit();
		Log.d(TAG, "Saved data: tank = " + String.valueOf(mTank));
	}

	void loadData() {
		SharedPreferences sp = inappActivity.getPreferences(inappActivity.MODE_PRIVATE);
		mTank = sp.getInt("tank", 2);
		Log.d(TAG, "Loaded data: tank = " + String.valueOf(mTank));
	}

}
