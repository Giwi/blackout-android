package org.giwi.android.blackout.model;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.api.Scope;

/**
 * The Class RSSFeed.
 * 
 * @author Xavier Marin | Giwi Softwares
 * 
 */
@EBean(scope = Scope.Singleton)
public class RSSFeed {

	/** The _title. */
	private String title = null;

	/** The _pubdate. */
	private String pubdate = null;

	/** The _itemcount. */
	private int itemcount = 0;

	/** The _itemlist. */
	private final List<RSSItem> itemlist;

	/**
	 * Instantiates a new rSS feed.
	 */
	public RSSFeed() {
		itemlist = new ArrayList<RSSItem>(0);
	}

	/**
	 * Adds the item.
	 * 
	 * @param item
	 *            the item
	 * @return the int
	 */
	int addItem(final RSSItem item) {
		itemlist.add(item);
		itemcount++;
		return itemcount;
	}

	/**
	 * Gets the item.
	 * 
	 * @param location
	 *            the location
	 * @return the item
	 */
	public RSSItem getItem(final int location) {
		return itemlist.get(location);
	}

	/**
	 * Gets the all items.
	 * 
	 * @return the all items
	 */
	public List<RSSItem> getAllItems() {
		return itemlist;
	}

	/**
	 * Gets the item count.
	 * 
	 * @return the item count
	 */
	public int getItemCount() {
		return itemcount;
	}

	/**
	 * Sets the title.
	 * 
	 * @param title
	 *            the new title
	 */
	public void setTitle(final String title) {
		this.title = title;
	}

	/**
	 * Sets the pub date.
	 * 
	 * @param pubdate
	 *            the new pub date
	 */
	public void setPubDate(final String pubdate) {
		this.pubdate = pubdate;
	}

	/**
	 * Gets the title.
	 * 
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Gets the pub date.
	 * 
	 * @return the pub date
	 */
	public String getPubDate() {
		return pubdate;
	}

}
