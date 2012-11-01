package org.giwi.android.blackout.activities;

import org.giwi.android.blackout.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewStub;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;

/**
 * @author Xavier Marin | Giwi Softwares
 */
@EActivity(R.layout.web_view)
public class WebViewActivity extends Activity {
	@ViewById(R.id.webView1)
	WebView mWebView;
	private ProgressDialog m_ProgressDialog;
	@Extra("myUrl")
	String myUrl;
	@Extra("title")
	String title;
	@ViewById(R.id.vsHeader)
	ViewStub stub;

	@AfterViews
	protected void update() {
		final View inflated = stub.inflate();
		final TextView txtTitle = (TextView) inflated.findViewById(R.id.mainTitle);
		final Button backBtn = (Button) inflated.findViewById(R.id.backIcon);
		final Button menuBtn = (Button) inflated.findViewById(R.id.menuIcon);
		menuBtn.setVisibility(View.GONE);
		backBtn.setVisibility(View.VISIBLE);
		txtTitle.setText(title);
		WebView.enablePlatformNotifications();
		mWebView.setWebViewClient(new BOWebViewClient());
		mWebView.getSettings().setJavaScriptEnabled(true);
		m_ProgressDialog = ProgressDialog.show(WebViewActivity.this, getResources().getText(R.string.wait_title), getResources().getText(R.string.wait_message), true);
		mWebView.loadUrl(myUrl);

	}

	@UiThread
	void loaded() {
		m_ProgressDialog.dismiss();
	}

	/**
	 * @author Xavier Marin
	 */
	private class BOWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onReceivedError(final WebView view, final int errorCode, final String description, final String failingUrl) {
			Toast.makeText(WebViewActivity.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onPageFinished(final WebView view, final String url) {
			super.onPageFinished(view, url);
			loaded();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(final int keyCode, final KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
			mWebView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void btnHomeClick(final View v) {
		super.onBackPressed();
	}
}
