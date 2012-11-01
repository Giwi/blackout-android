/**
 * 
 */
package org.giwi.android.blackout.activities;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.giwi.android.blackout.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;

/**
 * @author b3605
 * 
 */
@EFragment(R.layout.featured)
public class FeaturedActivity extends Fragment {
	@ViewById(R.id.fragmentFeaturedMain)
	ImageView fragmentFeaturedMain;
	@ViewById(R.id.featuredText)
	TextView featuredText;

	@AfterViews
	void initFragment() {
		featuredText.setText(Html.fromHtml(getArguments().getString("text")));
		final String url = getArguments().getString("imageURL");
		final String fileName = url.substring(url.lastIndexOf('/') + 1, url.lastIndexOf('.'));
		final Drawable drw = imageOperations(getActivity().getApplicationContext(), url, fileName);
		fragmentFeaturedMain.setImageDrawable(drw);
	}

	private Drawable imageOperations(final Context ctx, final String url, final String saveFilename) {
		Log.i("img", saveFilename);
		try {
			final InputStream is = (InputStream) fetch(url);
			final Drawable d = Drawable.createFromStream(is, saveFilename);
			return d;
		} catch (final MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (final IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private Object fetch(final String address) throws MalformedURLException, IOException {
		final URL url = new URL(address);
		final Object content = url.getContent();
		return content;
	}
}
