/**
 * 
 */
package org.giwi.android.blackout.services;

import java.util.List;

import org.giwi.android.blackout.R;
import org.giwi.android.blackout.activities.AgendaActivity_;
import org.giwi.android.blackout.activities.MediaActivity_;
import org.giwi.android.blackout.activities.NewsActivity_;
import org.giwi.android.blackout.model.DateTestResult;
import org.giwi.android.blackout.model.RSSItem;
import org.giwi.android.blackout.model.RSSTypes;
import org.giwi.android.blackout.tools.NewsFeedReader;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.util.Log;

/**
 * @author Xavier Marin | Giwi Softwares
 */
public class RssService extends IntentService {

	private final Handler handler = new Handler();
	private final Runnable drawer = new Runnable() {
		@Override
		public void run() {
			checkFeeds();
		}

	};

	public RssService() {
		super("org.giwi.blackout.rssService");

	}

	/**
	 * check de tous les flux
	 */
	private void checkFeeds() {
		Log.i("TAG", "checkFeeds");
		lauchService(getString(org.giwi.android.blackout.R.string.gigpress_feed), RSSTypes.DATES);
		lauchService(getString(org.giwi.android.blackout.R.string.news_feed), RSSTypes.NEWS);
		lauchService(getString(org.giwi.android.blackout.R.string.mp3_feed), RSSTypes.MEDIA);
		handler.removeCallbacks(drawer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.IntentService#onHandleIntent(android.content.Intent)
	 */
	@Override
	protected void onHandleIntent(final Intent intent) {
		Log.i("TAG", "Service started");
		handler.post(drawer);
	}

	/**
	 * @param url
	 *            url du flux
	 * @param type
	 *            le type de flux
	 */
	private void lauchService(final String url, final RSSTypes type) {
		// Retrieve the date from SharedPreferences
		String lastPubDate = getDateFromSharedPrefs(type);
		final DateTestResult res = new DateTestResult();
		// The AndroidFeedParser class helps us parse the Rss Feed.
		final List<RSSItem> newsFeed = NewsFeedReader.getInstance().populate(url, getApplicationContext(), type, true);

		if (newsFeed != null) {
			for (int i = 0; i < newsFeed.size(); i++) {
				// Verify the pubDate of each item, against pubDate stored in SharedPreferences
				res.verifyDates(newsFeed.get(i).getPubDate(), lastPubDate, type);
			}
			// Get the last pubDate and save it to SharedPreferences.
			lastPubDate = newsFeed.get(0).getPubDate();
			saveToSharedPreferences(lastPubDate, type);
		}

		if (res.getNewArticles() > 0) {
			displayNotification(res.getNewArticles(), type);
		} else {
			Log.i("GREC", "No new articles ");
		}
	}

	/**
	 * @param lastPubDate
	 *            la dernière date de publication du flux
	 * @param type
	 *            le type de flux
	 */
	private void saveToSharedPreferences(final String lastPubDate, final RSSTypes type) {
		final SharedPreferences settings = getSharedPreferences("org.giwi.blackout", MODE_PRIVATE);
		final Editor editor = settings.edit();
		editor.putString("org.giwi.blackout." + type.name(), lastPubDate);
		editor.commit();

	}

	/**
	 * @param type
	 *            le type de flux
	 * @return la date stockée
	 */
	private String getDateFromSharedPrefs(final RSSTypes type) {
		final SharedPreferences settings = getSharedPreferences("org.giwi.blackout", MODE_PRIVATE);
		return settings.getString("org.giwi.blackout." + type.name(), "");
	}

	/**
	 * @param newArticles
	 *            le nombre de nouveaux articles
	 * @param type
	 *            le type de flux
	 */
	public void displayNotification(final int newArticles, final RSSTypes type) {
		final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// Instantiate the Notification with an icon, ticker text, and when to be displayed
		final int icon = R.drawable.icon;
		CharSequence tickerText = "";
		Intent notificationIntent = null;
		final CharSequence title = "Blackout";
		CharSequence text = "Notification description";
		switch (type) {
		case MEDIA:
			if (newArticles == 1) {
				tickerText = "Blackout : un nouveau morceau en ligne";
				text = "Un  nouveau morceau vient d'être publié";
			} else {
				tickerText = "Blackout : de nouveaux morceaux en ligne";
				text = newArticles + "  nouveaux morceaux viennent d'être publiés";
			}
			notificationIntent = new Intent(this, MediaActivity_.class);
			break;
		case NEWS:
			if (newArticles == 1) {
				tickerText = "Blackout : une nouvelle actualité";
				text = "Une  nouvelle actualité vient d'être publiée";
			} else {
				tickerText = "Blackout : de nouvelles actualités en ligne";
				text = newArticles + "  nouvelles actualités viennent d'être publiées";
			}
			notificationIntent = new Intent(this, NewsActivity_.class);
			break;
		case DATES:
			if (newArticles == 1) {
				tickerText = "Blackout : une nouvelle date de concert";
				text = "Une  nouvelle date de concert vient d'être annoncée";
			} else {
				tickerText = "Blackout : de nouvelles dates de concert";
				text = newArticles + "  nouvelles  dates de concert viennent d'être annoncées";
			}
			notificationIntent = new Intent(this, AgendaActivity_.class);
			break;
		default:
			break;
		}
		final long when = System.currentTimeMillis(); // now
		final Notification notification = new Notification(icon, tickerText, when);
		// Define the notification's message and PendingIntent
		final Context context = getApplicationContext();
		// The activity PostNotification.class will be fired when notification will be opened.
		final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
		// Makes the notification disappear after clicked in the status bar
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		// When the notification will be fired, notify the user with default notification phone sound.
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.setLatestEventInfo(context, title, text, pendingIntent);
		// Pass the Notification object to NotificationManager
		notificationManager.notify(type.ordinal(), notification);

	}
}
