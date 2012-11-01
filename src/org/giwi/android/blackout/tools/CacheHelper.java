/**
 * 
 */
package org.giwi.android.blackout.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.giwi.android.blackout.model.RSSFeed;
import org.giwi.android.blackout.model.RSSTypes;

import twitter4j.ResponseList;
import twitter4j.Status;
import android.content.Context;

/**
 * @author Xavier Marin | Giwi Softwares
 * 
 */
public class CacheHelper {

	private static CacheHelper instance;

	/**
	 * 
	 */
	private CacheHelper() {
		//
	}

	/**
	 * @return une instance
	 */
	public static CacheHelper getInstance() {
		if (instance == null) {
			instance = new CacheHelper();
		}
		return instance;
	}

	/**
	 * @param obj
	 *            l'objet à mettre en cache
	 * @param type
	 *            le type de flu
	 * @param cacheDir
	 *            le dossier de stockage
	 * @return succès ou échec
	 */
	public boolean saveNewsFeed(final RSSFeed obj, final Context c, final RSSTypes type) {
		if (!c.getCacheDir().exists()) {
			c.getCacheDir().mkdirs();
		}
		final File suspend_f = new File(c.getCacheDir(), type.name());

		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		boolean keep = true;

		try {
			fos = new FileOutputStream(suspend_f);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(obj);
		} catch (final Exception e) {
			keep = false;

		} finally {
			try {
				if (oos != null) {
					oos.close();
				}
				if (fos != null) {
					fos.close();
				}
				if (keep == false) {
					suspend_f.delete();
				}
			} catch (final Exception e) { /* do nothing */
			}
		}

		return keep;

	}

	/**
	 * @param c
	 *            le contexte
	 * @param type
	 *            le type de flux
	 * @return un flux
	 */
	public RSSFeed getNewsFeed(final Context c, final RSSTypes type) {
		final File suspend_f = new File(c.getCacheDir(), type.name());
		if (!suspend_f.exists()) {
			return null;
		}
		RSSFeed simpleClass = null;
		FileInputStream fis = null;
		ObjectInputStream is = null;
		// boolean keep = true;

		try {

			fis = new FileInputStream(suspend_f);
			is = new ObjectInputStream(fis);
			simpleClass = (RSSFeed) is.readObject();
		} catch (final Exception e) {

		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
				if (is != null) {
					is.close();
				}

			} catch (final Exception e) {
			}
		}

		return simpleClass;

	}

	/**
	 * @param c
	 *            le contexte
	 * @param type
	 *            le type de feed
	 * @return une liste de status
	 */
	@SuppressWarnings("unchecked")
	public ResponseList<Status> getTwittFeed(final Context c, final RSSTypes type) {
		final File suspend_f = new File(c.getCacheDir(), type.name());
		if (!suspend_f.exists()) {
			return null;
		}
		ResponseList<Status> simpleClass = null;
		FileInputStream fis = null;
		ObjectInputStream is = null;
		// boolean keep = true;

		try {

			fis = new FileInputStream(suspend_f);
			is = new ObjectInputStream(fis);
			simpleClass = (ResponseList<Status>) is.readObject();
		} catch (final Exception e) {

		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
				if (is != null) {
					is.close();
				}

			} catch (final Exception e) {
			}
		}

		return simpleClass;
	}

	/**
	 * @param obj
	 *            la liste des statuts Twitter
	 * @param c
	 *            le contexte
	 * @param type
	 *            le type de flux
	 * @return succès ou échec
	 */
	public boolean saveTweetFeed(final ResponseList<Status> obj, final Context c, final RSSTypes type) {
		if (!c.getCacheDir().exists()) {
			c.getCacheDir().mkdirs();
		}
		final File suspend_f = new File(c.getCacheDir(), type.name());

		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		boolean keep = true;

		try {
			fos = new FileOutputStream(suspend_f);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(obj);
		} catch (final Exception e) {
			keep = false;

		} finally {
			try {
				if (oos != null) {
					oos.close();
				}
				if (fos != null) {
					fos.close();
				}
				if (keep == false) {
					suspend_f.delete();
				}
			} catch (final Exception e) { /* do nothing */
			}
		}
		return keep;

	}
}
