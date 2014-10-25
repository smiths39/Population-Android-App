package com.toyapp.sean;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class Splash extends Activity {
	
	MediaPlayer introSong;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		introSong = MediaPlayer.create(Splash.this, R.raw.toy_app_theme);
		
		SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		boolean music = getPrefs.getBoolean("checkbox",  true);
		
		if (music == true) {
			introSong.start();
		}
		
		Thread timer = new Thread() {
			
			public void run() {
				try {
					sleep(2500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					Intent openLaunch = new Intent("com.toyapp.sean.SELECTIONCHOICE");
					startActivity(openLaunch);
				}
			}
		};
		timer.start();
	}

	@Override
	protected void onPause() {
		
		super.onPause();
		
		introSong.release();
		finish();
	}
}
