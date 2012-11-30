package org.giwi.android.blackout.activities;

import java.util.List;

import org.giwi.android.blackout.R;
import org.giwi.android.blackout.model.RSSItem;
import org.giwi.android.blackout.tools.ProxySetting;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.FromHtml;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.res.DrawableRes;

/**
 * @author Xavier Marin | Giwi Softwares
 */
@EActivity(R.layout.mediaplayerlayout)
public class StreamingMp3Player extends Activity {
	@Extra("listOfMedia")
	List<RSSItem> listOfMedia;
	@Extra("currentItem")
	int currentItem;
	@ViewById(R.id.button_play)
	ImageButton buttonPlayPause;
	@ViewById(R.id.progress_bar)
	SeekBar seekBarProgress;
	@DrawableRes(android.R.drawable.ic_media_play)
	Drawable playIcon;
	@DrawableRes(android.R.drawable.ic_media_pause)
	Drawable pauseIcon;
	@ViewById(R.id.textView2)
	@FromHtml(R.string.buffering)
	TextView status;
	@ViewById(R.id.vsHeader)
	ViewStub stub;
	@ViewById(R.id.waitSpinner)
	ProgressBar waitSpinner;
	private final Handler handler = new Handler();
	private MediaPlayer mediaPlayer;
	private TextView txtTitle;
	private boolean started = false;

	/** This method initialise all the views in project */
	@AfterViews
	void initView() {
		final View inflated = stub.inflate();
		txtTitle = (TextView) inflated.findViewById(R.id.mainTitle);
		final Button backBtn = (Button) inflated.findViewById(R.id.backIcon);
		final Button menuBtn = (Button) inflated.findViewById(R.id.menuIcon);
		menuBtn.setVisibility(View.GONE);
		backBtn.setVisibility(View.VISIBLE);
		seekBarProgress.setMax(99); // It means 100% .0-99
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {
			@Override
			public void onBufferingUpdate(final MediaPlayer mp, final int percent) {
				secondarySeekBarProgressUpdater(percent);
			}
		});
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(final MediaPlayer mp) {
				buttonPlayPause.setImageDrawable(playIcon);
			}
		});
		seekBarProgress.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(final View v, final MotionEvent event) {
				seekMediaPlayerToSeekBarTouch(v);
				return false;
			}
		});
		playItem(currentItem);

	}

	/**
	 * @param i
	 */
	private void playItem(int i) {
		started = false;
		seekBarProgress.setProgress(0);
		seekBarProgress.setSecondaryProgress(0);
		waitSpinner.setVisibility(View.VISIBLE);
		txtTitle.setText(getString(R.string.app_name) + " : " + listOfMedia.get(i).getTitle());
		starBuffering(i);
	}

	@Background
	protected void starBuffering(int i) {
		try {
			ProxySetting.setProxy();
			mediaPlayer.setDataSource(listOfMedia.get(i).getMedia());
			mediaPlayer.prepareAsync();
			primarySeekBarProgressUpdater();
		} catch (final Exception e) {
			Log.e(this.getClass().getName(), String.valueOf(e.getMessage()));
		}
	}

	/**
	 * ImageButton onClick event handler. Method which start/pause mediaplayer playing
	 */
	@Click(R.id.button_play)
	void buttonPlayPauseClick(final View view) {
		if (!started) {
			return;
		}
		if (!mediaPlayer.isPlaying()) {
			mediaPlayer.start();
			buttonPlayPause.setImageDrawable(pauseIcon);
		} else {
			mediaPlayer.pause();
			buttonPlayPause.setImageDrawable(playIcon);
		}
		// primarySeekBarProgressUpdater();
	}

	@Click(R.id.button_next)
	void buttonNextClick(final View view) {
		if (currentItem + 1 < listOfMedia.size()) {
			currentItem += 1;
			mediaPlayer.reset();
			playItem(currentItem);
		}
	}

	@Click(R.id.button_prev)
	void buttonPrevClick(final View view) {
		if (currentItem - 1 >= 0) {
			currentItem -= 1;
			mediaPlayer.reset();
			playItem(currentItem);
		}
	}

	/**
	 * Method which updates the SeekBar primary progress by current song playing position
	 */
	void primarySeekBarProgressUpdater() {
		// This math construction give a percentage of
		// "was playing"/"song length"
		try {
			if (mediaPlayer != null && mediaPlayer.isPlaying()) {
				seekBarProgress.setProgress((int) ((float) mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration() * 100));
				final Runnable notification = new Runnable() {
					@Override
					public void run() {
						primarySeekBarProgressUpdater();
					}
				};
				handler.postDelayed(notification, 100);
			}
		} catch (final Exception e) {
			Log.e(this.getClass().getName(), String.valueOf(e.getMessage()));
		}
	}

	/**
	 * Method which updates the SeekBar secondary progress by current song loading from URL position
	 */
	@UiThread
	void secondarySeekBarProgressUpdater(final int percent) {
		if (percent >= 99) {
			status.setText("loaded");
		}
		if (percent >= 10 && !mediaPlayer.isPlaying() && !started) {
			mediaPlayer.start();
			started = true;
			waitSpinner.setVisibility(View.INVISIBLE);
			buttonPlayPause.setImageDrawable(pauseIcon);
		}
		seekBarProgress.setSecondaryProgress(percent);
		status.setText("buffering " + percent + "%");
	}

	@Override
	public void onBackPressed() {
		// super.onBackPressed();
		mediaPlayer.stop();
		mediaPlayer.release();
		MediaActivity_.intent(stub.getContext()).start();
	}

	/**
	 * Seekbar onTouch event handler. Method which seeks MediaPlayer to seekBar primary progress position
	 */
	void seekMediaPlayerToSeekBarTouch(final View v) {
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.seekTo(mediaPlayer.getDuration() / 100 * seekBarProgress.getProgress());
		}
	}

	public void btnHomeClick(final View v) {
		mediaPlayer.stop();
		mediaPlayer.release();
		MediaActivity_.intent(v.getContext()).start();
	}

}
