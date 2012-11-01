package org.giwi.android.blackout.tools;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author Xavier Marin | Giwi Softwares
 */
public class DateFormats {
	public static SimpleDateFormat SDF = new SimpleDateFormat("EE, dd MMM yyyy kk:mm:ss Z", Locale.US);
	public static DateFormat DF = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.FRANCE);

	/**
	 * @param date
	 *            date à formater
	 * @return une date formatée
	 */
	public static String convertRSSDate(final String date) {
		try {
			return DF.format(SDF.parse(date));
		} catch (final ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
}
