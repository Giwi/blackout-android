package org.giwi.android.blackout.activities;

import java.util.ArrayList;
import java.util.List;

import org.giwi.android.blackout.GCMIntentService;
import org.giwi.android.blackout.R;
import org.giwi.android.blackout.model.MyPagerAdapter;
import org.giwi.android.blackout.model.RSSItem;
import org.giwi.android.blackout.model.RSSTypes;
import org.giwi.android.blackout.tools.HttpHelper;
import org.giwi.android.blackout.tools.NewsFeedReader;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gcm.GCMRegistrar;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.res.StringRes;

/**
 * @author Xavier Marin | Giwi Softwares
 * 
 *         Main activity
 */
@EActivity(R.layout.main)
public class BlackOutActivity extends FragmentActivity {
	@ViewById(R.id.splashscreen)
	ImageView splash;
	@ViewById(R.id.content)
	LinearLayout content;
	@ViewById(R.id.newsBtn)
	Button buttonNews;
	@ViewById(R.id.calendarBtn)
	Button buttonAgenda;
	@ViewById(R.id.mediaBtn)
	Button buttonMedia;
	@ViewById(R.id.aboutBtn)
	Button buttonAbout;
	@ViewById(R.id.website)
	Button website;
	@ViewById(R.id.twitter)
	Button twitter;
	@ViewById(R.id.vsHeader)
	ViewStub stub;
	@ViewById(R.id.awesomepager)
	ViewPager pager;
	@StringRes(R.string.featured_feed)
	String theFeed;

	private boolean pagerReady = false;
	private boolean splashDone = false;
	private PagerAdapter mPagerAdapter;

	/**
	 * 
	 */
	@AfterViews
	protected void update() {
		startService();
		if (!splashDone) {
			stopSplash();
		} else {
			hideSplash(null);
		}
		initialisePaging();
		handler.postDelayed(drawer, 5000);
	}

	private final Handler handler = new Handler();
	private final Runnable drawer = new Runnable() {
		/*
		 * (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			if (pagerReady) {
				if (pager.getAdapter().getCount() > pager.getCurrentItem() + 1) {
					pager.setCurrentItem(pager.getCurrentItem() + 1, true);
				} else {
					pager.setCurrentItem(0, true);
				}
				handler.postDelayed(drawer, 5000);
			}
		}

	};

	/**
	 * Register device with GCM
	 */
	@Background
	protected void startService() {
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		final String regId = GCMRegistrar.getRegistrationId(this);
		if ("".equals(regId)) {
			GCMRegistrar.register(this, GCMIntentService.SENDER_ID);
		} else {
			Log.v("hahaha", "Already registered : " + regId);
			HttpHelper.postRegId(regId, "register");
		}
	}

	/**
	 * @param v
	 */
	@Click(R.id.splashscreen)
	protected void hideSplash(final View v) {
		splash.setVisibility(View.GONE);
		content.setVisibility(View.VISIBLE);
	}

	/**
	 * 
	 */
	@UiThread(delay = 3000l)
	protected void stopSplash() {
		hideSplash(null);
		splashDone = true;
	}

	/**
	 * @param view
	 */
	@Click(R.id.newsBtn)
	protected void newBtnClick(final View view) {
		NewsActivity_.intent(view.getContext()).start();
	}

	/**
	 * @param view
	 */
	@Click(R.id.calendarBtn)
	protected void calendarBtnClick(final View view) {
		AgendaActivity_.intent(view.getContext()).start();
	}

	/**
	 * @param view
	 */
	@Click(R.id.mediaBtn)
	protected void buttonMediaClick(final View view) {
		MediaActivity_.intent(view.getContext()).start();
	}

	/**
	 * @param view
	 */
	@Click(R.id.aboutBtn)
	protected void buttonAboutClick(final View view) {
		AboutActivity_.intent(view.getContext()).start();
	}

	/**
	 * @param view
	 */
	@Click(R.id.website)
	protected void websiteBtnClick(final View view) {
		WebViewActivity_.intent(view.getContext()).myUrl(getResources().getString(R.string.webSiteLink)).title((String) getResources().getText(R.string.app_name)).start();
	}

	/**
	 * @param view
	 */
	@Click(R.id.twitter)
	protected void twitterBtnClick(final View view) {
		WebViewActivity_.intent(view.getContext()).myUrl(getResources().getString(R.string.tweeterLink)).title((String) getResources().getText(R.string.app_name)).start();
		// TwitterActivity_.intent(view.getContext()).start();
	}

	/**
	 * Initialize the fragments to be paged
	 */
	@Background
	protected void initialisePaging() {
		final List<Fragment> fragments = new ArrayList<Fragment>();
		final List<RSSItem> newsFeed = NewsFeedReader.getInstance().populate(theFeed, getApplicationContext(), RSSTypes.NEWS, true);
		for (final RSSItem item : newsFeed) {
			item.sanitizeDesc();
			fragments.add(newInstance(item.getImgUrl(), "<b>" + item.getTitle() + "  : </b>" + item.getDescription()));
		}
		mPagerAdapter = new MyPagerAdapter(super.getSupportFragmentManager(), fragments);
		pagerReady = true;
		pager.setAdapter(mPagerAdapter);

	}

	/**
	 * @param imageURL
	 * @param text
	 * @return
	 */
	private FeaturedActivity_ newInstance(final String imageURL, final String text) {
		final FeaturedActivity_ fragment = new FeaturedActivity_();
		final Bundle args = new Bundle();
		args.putString("imageURL", imageURL);
		args.putString("text", text);
		fragment.setArguments(args);
		return fragment;
	}

}