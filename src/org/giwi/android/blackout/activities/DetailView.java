package org.giwi.android.blackout.activities;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;

import org.giwi.android.blackout.R;
import org.giwi.android.blackout.model.RSSTypes;
import org.giwi.android.blackout.tools.DateFormats;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.ViewById;

/**
 * @author Xavier Marin | Giwi Softwares
 * 
 */
@EActivity(R.layout.detailview)
public class DetailView extends Activity {
	@Extra("url")
	String url;
	@Extra("type")
	RSSTypes type;
	@Extra("title")
	String titleContent;
	@Extra("content")
	String htmlContent;
	@Extra("date")
	String date;
	@Extra("imageUrl")
	String imageUrl;

	@ViewById(R.id.content)
	TextView content;
	@ViewById(R.id.storeCal)
	Button storeCal;
	@ViewById(R.id.share)
	Button share;
	@ViewById(R.id.readMore)
	Button more;
	@ViewById(R.id.vsHeader)
	ViewStub stub;
	@ViewById(R.id.featuredImage)
	ImageView featuredImage;
	@ViewById(R.id.detTitle)
	TextView detTitle;

	@AfterViews
	protected void update() {
		final View inflated = stub.inflate();
		final TextView txtTitle = (TextView) inflated.findViewById(R.id.mainTitle);
		txtTitle.setText(getString(R.string.app_name) + " : " + titleContent);
		detTitle.setText(titleContent);
		content.setText(Html.fromHtml(htmlContent));
		final Button menuBtn = (Button) inflated.findViewById(R.id.menuIcon);
		menuBtn.setVisibility(View.GONE);
		content.setMovementMethod(LinkMovementMethod.getInstance());
		switch (type) {
		case DATES:
			more.setVisibility(View.GONE);
			featuredImage.setVisibility(View.GONE);
			break;
		case NEWS:
			storeCal.setVisibility(View.GONE);
			if (imageUrl != null) {
				final String fileName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1, imageUrl.lastIndexOf('.'));
				final Drawable drw = imageOperations(getApplicationContext(), imageUrl, fileName);
				featuredImage.setImageDrawable(drw);
			} else {
				featuredImage.setVisibility(View.GONE);
			}
			break;
		case MEDIA:
			featuredImage.setVisibility(View.GONE);
		default:
			break;
		}
	}

	@Click(R.id.storeCal)
	void storeCalClick(final View v) {
		if (RSSTypes.DATES.equals(type)) {
			final Intent intent = new Intent(Intent.ACTION_EDIT);
			intent.setType("vnd.android.cursor.item/event");
			try {
				intent.putExtra("beginTime", DateFormats.SDF.parse(date).getTime());
				// intent.putExtra("allDay", true);
				intent.putExtra("endTime", DateFormats.SDF.parse(date).getTime() + 180 * 60 * 1000);
				intent.putExtra("title", titleContent);
				startActivity(intent);
			} catch (final ParseException e) {
				Log.e("ERROR", "ERROR IN CODE: " + e.toString());
				e.printStackTrace();
			}
		}
	}

	@Click(R.id.share)
	void shareClick(final View v) {
		final Intent MessIntent = new Intent(Intent.ACTION_SEND);
		MessIntent.setType("text/plain");
		MessIntent.putExtra(Intent.EXTRA_TEXT, htmlContent + " " + url);
		DetailView.this.startActivity(Intent.createChooser(MessIntent, "Partager avec..."));
	}

	@Click(R.id.readMore)
	void readMoreClick(final View v) {
		WebViewActivity_.intent(getApplicationContext()).flags(Intent.FLAG_ACTIVITY_NEW_TASK).myUrl(url).title(titleContent).start();
	}

	public void btnHomeClick(final View v) {
		super.onBackPressed();
	}

	public void btnMenuClick(final View v) {
		openOptionsMenu();
	}

	private Drawable imageOperations(final Context ctx, final String url, final String saveFilename) {
		Log.i("img", saveFilename);
		try {
			final InputStream is = (InputStream) fetch(url);
			final Drawable d = Drawable.createFromStream(is, saveFilename);
			return d;
		} catch (final MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (final IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private Object fetch(final String address) throws MalformedURLException, IOException {
		final URL url = new URL(address);
		final Object content = url.getContent();
		return content;
	}
}
