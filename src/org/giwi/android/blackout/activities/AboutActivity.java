package org.giwi.android.blackout.activities;

import org.giwi.android.blackout.R;

import android.app.Activity;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.FromHtml;
import com.googlecode.androidannotations.annotations.ViewById;

/**
 * @author Xavier Marin | Giwi Softwares
 */
@EActivity(R.layout.aboutlayout)
public class AboutActivity extends Activity {
	@ViewById(R.id.mainTitle)
	@FromHtml(R.string.about)
	TextView title;
	@ViewById(R.id.textView5)
	TextView t2;
	@ViewById(R.id.vsHeader)
	ViewStub stub;

	@AfterViews
	protected void update() {
		final View inflated = stub.inflate();
		final TextView txtTitle = (TextView) inflated.findViewById(R.id.mainTitle);
		final Button menuBtn = (Button) inflated.findViewById(R.id.menuIcon);
		menuBtn.setVisibility(View.GONE);
		txtTitle.setText(getString(R.string.app_name) + " : " + getString(R.string.about));
		t2.setMovementMethod(LinkMovementMethod.getInstance());
	}

	/**
	 * @param v
	 */
	protected void btnHomeClick(final View v) {
		super.onBackPressed();
	}

}