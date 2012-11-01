package org.giwi.android.blackout.tools;

import android.net.Proxy;

/**
 * @author Xavier Marin | Giwi Softwares
 * 
 */
public class ProxySetting {

	/**
	 */
	public static void setProxy() {
		final String myProxy = Proxy.getDefaultHost();
		final int myPort = Proxy.getDefaultPort();
		if (!"".equalsIgnoreCase(myProxy) && myPort > 0) {
			System.setProperty("http.proxyHost", myProxy);
			System.setProperty("https.proxyHost", myProxy);
			System.setProperty("http.proxyPort", String.valueOf(myPort));
			System.setProperty("https.proxyPort", String.valueOf(myPort));
		}
	}
}
