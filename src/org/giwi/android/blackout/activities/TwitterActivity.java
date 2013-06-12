package org.giwi.android.blackout.activities;

import java.util.ArrayList;
import java.util.List;

import org.giwi.android.blackout.R;
import org.giwi.android.blackout.model.RSSTypes;
import org.giwi.android.blackout.tools.CacheHelper;
import org.giwi.android.blackout.tools.DateFormats;
import org.giwi.android.blackout.tools.ProxySetting;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ItemClick;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.res.StringRes;

/**
 * @author Xavier Marin | Giwi Softwares
 * 
 */
@EActivity(R.layout.twitterlayout)
@OptionsMenu(R.menu.option_menu)
public class TwitterActivity extends ListActivity {
	@ViewById(R.id.vsHeader)
	ViewStub stub;
	@StringRes(R.string.wait_title)
	String waitTitle;
	@StringRes(R.string.wait_message)
	String waitMessage;
	@StringRes(R.string.twitter_account)
	String screenName;

	private ProgressDialog m_ProgressDialog;
	protected List<String> newsFeed;
	private ArrayAdapter<String> m_adapter;

	/**
	 * 
	 */
	@AfterViews
	protected void update() {
		final View inflated = stub.inflate();
		final TextView txtTitle = (TextView) inflated.findViewById(R.id.mainTitle);
		txtTitle.setText("@" + screenName);
		m_adapter = new ArrayAdapter<String>(this, R.layout.timeline_item, new ArrayList<String>());
		setListAdapter(m_adapter);
		m_ProgressDialog = ProgressDialog.show(this, waitTitle, waitMessage, true);
		getTwittFeed(false);
	}

	/**
	 * @param toRefresh
	 */
	@Background
	protected void getTwittFeed(final boolean toRefresh) {
		newsFeed = getTimeLine(toRefresh);
		updateView(toRefresh);
	}

	/**
	 * @param toRefresh
	 */
	@UiThread
	protected void updateView(final boolean toRefresh) {
		if (newsFeed != null && newsFeed.size() > 0) {
			m_adapter.clear();
			m_adapter.notifyDataSetChanged();
			for (int i = 0; i < newsFeed.size(); i++) {
				m_adapter.add(newsFeed.get(i));
			}
		}
		m_ProgressDialog.dismiss();
		m_adapter.notifyDataSetChanged();
		if (toRefresh) {
			Toast.makeText(this, getResources().getText(R.string.refresh_succes), Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * @param selectedItem
	 */
	@ItemClick
	protected void listItemClicked(final String selectedItem) {
		DetailView_.intent(getApplicationContext()).flags(Intent.FLAG_ACTIVITY_NEW_TASK).url("").titleContent("@" + screenName).htmlContent(selectedItem).type(RSSTypes.NEWS).start();
	}

	/**
	 * 
	 * @param screenName
	 * @return
	 */
	private List<String> getTimeLine(final boolean toRefresh) {
		final List<String> result = new ArrayList<String>();
		ResponseList<Status> userTimeline = null;
		if (!toRefresh) {
			userTimeline = CacheHelper.getInstance().getTwittFeed(getApplicationContext(), RSSTypes.TWITTER);
		}
		if (userTimeline == null || userTimeline.isEmpty()) {
			try {
				ProxySetting.setProxy();
				final Twitter twitter = new TwitterFactory().getInstance();
				userTimeline = twitter.getUserTimeline(screenName);
				for (final Status status : userTimeline) {
					result.add(DateFormats.DF.format(status.getCreatedAt()) + " - " + status.getText());
				}
				CacheHelper.getInstance().saveTweetFeed(userTimeline, getApplicationContext(), RSSTypes.TWITTER);
			} catch (final TwitterException e) {
				Log.e("TwitterException", e.getMessage());
			}

		}

		return result;
	}

	/**
	 * 
	 */
	@OptionsItem
	protected void refresh() {
		m_ProgressDialog = ProgressDialog.show(this, waitTitle, waitMessage, true);
		getTwittFeed(true);
	}

	/**
	 * @param v
	 */
	@Click(R.id.backIcon)
	public void btnHomeClick(final View v) {
		super.onBackPressed();
	}

	/**
	 * @param v
	 */
	public void btnMenuClick(final View v) {
		openOptionsMenu();
	}
}
