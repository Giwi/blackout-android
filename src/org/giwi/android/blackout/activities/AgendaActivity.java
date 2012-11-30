package org.giwi.android.blackout.activities;

import java.util.ArrayList;
import java.util.List;

import org.giwi.android.blackout.R;
import org.giwi.android.blackout.model.RSSItem;
import org.giwi.android.blackout.model.RSSTypes;
import org.giwi.android.blackout.tools.NewsAdapter;
import org.giwi.android.blackout.tools.NewsFeedReader;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.FromHtml;
import com.googlecode.androidannotations.annotations.ItemClick;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.res.StringRes;

/**
 * @author Xavier Marin | Giwi Softwares
 */
@EActivity(R.layout.agendalayout)
@OptionsMenu(R.menu.option_menu)
public class AgendaActivity extends ListActivity {

	@ViewById(R.id.mainTitle)
	@FromHtml(R.string.calendar)
	TextView title;
	@StringRes(R.string.gigpress_feed)
	String theFeed;
	@StringRes(R.string.wait_title)
	String waitTitle;
	@StringRes(R.string.wait_message)
	String waitMessage;
	@ViewById(R.id.vsHeader)
	ViewStub stub;

	private ProgressDialog m_ProgressDialog;
	private List<RSSItem> newsFeed = new ArrayList<RSSItem>();
	private NewsAdapter m_adapter;

	/**
	 * 
	 */
	@AfterViews
	protected void update() {
		final View inflated = stub.inflate();
		final TextView txtTitle = (TextView) inflated.findViewById(R.id.mainTitle);
		txtTitle.setText(getString(R.string.app_name) + " : " + getString(R.string.calendar));

		m_adapter = new NewsAdapter(this, R.layout.row, newsFeed, RSSTypes.DATES);
		setListAdapter(m_adapter);
		getNewsFeed(false);
		m_ProgressDialog = ProgressDialog.show(this, waitTitle, waitMessage, true);
	}

	/**
	 * @param toRefresh
	 */
	@Background
	protected void getNewsFeed(final boolean toRefresh) {
		try {
			newsFeed = NewsFeedReader.getInstance().populate(theFeed, getApplicationContext(), RSSTypes.DATES, toRefresh);
			Log.i("ARRAY", "" + newsFeed.size());
		} catch (final Exception e) {
			m_ProgressDialog.dismiss();
			Log.e("BACKGROUND_PROC", e.getMessage());
		}
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
	protected void listItemClicked(final RSSItem selectedItem) {
		DetailView_
				.intent(getApplicationContext())
				.flags(Intent.FLAG_ACTIVITY_NEW_TASK)
				.url(selectedItem.getLink())
				.titleContent(selectedItem.getTitle())
				.htmlContent(
						selectedItem.getDescription().replaceAll("(<li>)|(<ul>)|(\n)|\\s{2,}", "").replaceAll("(</li>)|(</ul>)", "<br />").replaceAll("<strong>", "<b>")
								.replaceAll("</strong>", "</b>")).date(selectedItem.getPubDate()).type(RSSTypes.DATES).start();
	}

	/**
	 * 
	 */
	@OptionsItem
	protected void refresh() {
		m_ProgressDialog = ProgressDialog.show(this, waitTitle, waitMessage, true);
		getNewsFeed(true);
	}

	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		BlackOutActivity_.intent(stub.getContext()).start();
	}

	/**
	 * @param v
	 */
	protected void btnHomeClick(final View v) {
		BlackOutActivity_.intent(v.getContext()).start();
	}

	/**
	 * @param v
	 */
	protected void btnMenuClick(final View v) {
		openOptionsMenu();
	}

}
