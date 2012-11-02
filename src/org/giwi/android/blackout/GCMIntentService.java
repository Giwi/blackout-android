/**
 *
 */
package org.giwi.android.blackout;

import org.giwi.android.blackout.services.RssService;
import org.giwi.android.blackout.tools.HttpHelper;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gcm.GCMBaseIntentService;

/**
 * @author Xavier Marin | Giwi Softwares
 *
 */
public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCMIntentService";
	public static final String SENDER_ID = "1077698015655";

	public GCMIntentService() {
		super(SENDER_ID);
	}

	@Override
	protected void onRegistered(final Context arg0, final String registrationId) {
		Log.i(TAG, "Device registered: regId = " + registrationId);
		HttpHelper.postRegId(registrationId, "register");

	}

	@Override
	protected void onUnregistered(final Context arg0, final String registrationId) {
		Log.i(TAG, "unregistered = " + registrationId);
		HttpHelper.postRegId(registrationId, "unregister");
	}

	@Override
	protected void onMessage(final Context arg0, final Intent arg1) {
		Log.i(TAG, "new message= " + arg1.getExtras().getString("message"));
		Toast.makeText(getApplicationContext(), getResources().getText(R.string.refresh_succes), Toast.LENGTH_LONG).show();
		final Intent intent = new Intent(this, RssService.class);
		startService(intent);
	}

	@Override
	protected void onError(final Context arg0, final String errorId) {
		Log.i(TAG, "Received error: " + errorId);
	}

	@Override
	protected boolean onRecoverableError(final Context context, final String errorId) {
		Log.i(TAG, "onRecoverableError error: " + errorId);
		return super.onRecoverableError(context, errorId);
	}

}
