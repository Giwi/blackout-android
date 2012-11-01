package org.giwi.android.blackout.model;

import java.text.ParseException;
import java.util.Date;

import org.giwi.android.blackout.tools.DateFormats;

import android.util.Log;

/**
 * @author Xavier Marin | Giwi Softwares
 * 
 */
public class DateTestResult {
	private int newArticles;
	private boolean hasMoreArticles = true;

	/**
	 * @return le nombre d'articles non lus
	 */
	public int getNewArticles() {
		return newArticles;
	}

	/**
	 * @param newArticles
	 *            le nombre d'articles non lus
	 */
	public void setNewArticles(final int newArticles) {
		this.newArticles = newArticles;
	}

	/**
	 * @return the hasMoreArticles
	 */
	public boolean isHasMoreArticles() {
		return hasMoreArticles;
	}

	/**
	 * @param hasMoreArticles
	 *            the hasMoreArticles to set
	 */
	public void setHasMoreArticles(final boolean hasMoreArticles) {
		this.hasMoreArticles = hasMoreArticles;
	}

	/**
	 * @param rssPubDate
	 *            la date de publication RSS
	 * @param sharedPrefLastPubDate
	 *            la dernière date sauvegardée
	 * @param type
	 *            le type de flux
	 */
	public void verifyDates(final String rssPubDate, final String sharedPrefLastPubDate, final RSSTypes type) {
		if (isHasMoreArticles()) {
			Date dLastPubDate = null;
			Date dRssPubDate = null;
			if (!"".equals(sharedPrefLastPubDate)) {
				try {
					dLastPubDate = DateFormats.SDF.parse(sharedPrefLastPubDate);
					dRssPubDate = DateFormats.SDF.parse(rssPubDate);
				} catch (final ParseException e) {
					Log.d("GREC", "Exception in verifayDates: " + e.getMessage());
					e.printStackTrace();
				}
				// We want to count how many new articles were published.
				if (dRssPubDate.getTime() > dLastPubDate.getTime()) {
					setNewArticles(getNewArticles() + 1);
				} else {
					setHasMoreArticles(false);
				}
			} else {
				setNewArticles(getNewArticles() + 1);
			}
		}
	}
}
