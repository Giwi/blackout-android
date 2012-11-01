/**
 * 
 */
package org.giwi.android.blackout.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

/**
 * @author Xavier Marin | Giwi Softwares
 * 
 */
public class HttpHelper {

	/**
	 * @param regId
	 *            registration ID
	 * @param operation
	 *            opération
	 */
	public static void postRegId(final String regId, final String operation) {
		// Create a new HttpClient and Post Header
		final HttpClient httpclient = new DefaultHttpClient();
		final HttpPost httppost = new HttpPost("http://www.theblackout.fr/wordpress/wp-content/plugins/gcmPub/gcm.php");

		try {
			// Add your data
			final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("regId", regId));
			nameValuePairs.add(new BasicNameValuePair("operation", operation));
			nameValuePairs.add(new BasicNameValuePair("model", android.os.Build.MODEL));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			final HttpResponse response = httpclient.execute(httppost);
			Log.i("HttpHelper", String.valueOf(response.getStatusLine().getStatusCode()));
		} catch (final ClientProtocolException e) {
			Log.e("HttpHelper", e.getMessage(), e);
		} catch (final IOException e) {
			Log.e("HttpHelper", e.getMessage(), e);
		}
	}
}
