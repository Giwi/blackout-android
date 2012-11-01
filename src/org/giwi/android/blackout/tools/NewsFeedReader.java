package org.giwi.android.blackout.tools;

import java.net.URL;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.giwi.android.blackout.model.RSSFeed;
import org.giwi.android.blackout.model.RSSHandler;
import org.giwi.android.blackout.model.RSSItem;
import org.giwi.android.blackout.model.RSSTypes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.content.Context;
import android.util.Log;

/**
 * @author Xavier Marin | Giwi Softwares
 */
public class NewsFeedReader {

	private static NewsFeedReader instance;

	private NewsFeedReader() {
		// Vide
	}

	/**
	 * @return
	 */
	public static NewsFeedReader getInstance() {
		if (instance == null) {
			instance = new NewsFeedReader();
		}
		return instance;
	}

	/**
	 * @param urlToRssFeed
	 *            le flux
	 * @return un flux
	 */
	public RSSFeed parseFeed(final String urlToRssFeed) {
		try {
			// setup the url
			final URL url = new URL(urlToRssFeed);
			ProxySetting.setProxy();
			// create the factory
			final SAXParserFactory factory = SAXParserFactory.newInstance();
			// create a parser
			final SAXParser parser = factory.newSAXParser();

			// create the reader (scanner)
			final XMLReader xmlreader = parser.getXMLReader();
			// instantiate our handler
			final RSSHandler theRssHandler = new RSSHandler();
			// assign our handler
			xmlreader.setContentHandler(theRssHandler);
			// get our data via the url class
			final InputSource is = new InputSource(url.openStream());
			// perform the synchronous parse
			xmlreader.parse(is);
			// get the results - should be a fully populated RSSFeed instance, or null on error
			return theRssHandler.getFeed();
		} catch (final Exception ee) {
			// if we have a problem, simply return null
			return null;
		}
	}

	/**
	 * @param feedUrl
	 *            url du flux
	 * @param context
	 *            le contexte
	 * @param type
	 *            le type de flux
	 * @param toRefresh
	 *            à rafraîchir ou non
	 * @return un flux
	 */
	public List<RSSItem> populate(final String feedUrl, final Context context, final RSSTypes type, final boolean toRefresh) {
		RSSFeed feed = null;
		if (!toRefresh) {
			Log.d("TAG", "get from cache : " + feedUrl);
			feed = CacheHelper.getInstance().getNewsFeed(context, type);
		}
		if (feed == null || feed.getAllItems() == null || feed.getAllItems().isEmpty()) {
			Log.d("TAG", "Reading : " + feedUrl);
			feed = parseFeed(feedUrl);
			CacheHelper.getInstance().saveNewsFeed(feed, context, type);
		}
		if (feed != null) {
			return feed.getAllItems();
		}
		return null;
	}

}
