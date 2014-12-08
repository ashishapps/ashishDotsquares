package com.phonegap.astroapp;


import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.phonegap.plugthat.R;

public class VideoPlay extends Activity implements OnCompletionListener, OnErrorListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.videoplay);
		Intent intent = getIntent();
		  String path = intent.getStringExtra("vid");
		  Log.i("path video", path);
		final ProgressBar mProgressBar;
		mProgressBar= (ProgressBar)findViewById(R.id.progressBar1);
		if (mProgressBar != null)
		    mProgressBar.setVisibility(View.VISIBLE);
		 VideoView mVideoView;
		 mVideoView = (VideoView)findViewById(R.id.videoView1);
		  mVideoView.setOnCompletionListener(this);
		 mVideoView.setVideoURI(Uri.parse(path));
		  mVideoView.setOnErrorListener(this);
		         mVideoView.setMediaController(new MediaController(this));
		         mVideoView.requestFocus();
		         mVideoView.start();
		         mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
		         @Override
		         public void onPrepared(MediaPlayer mediaPlayer) {
		          
		          if (mProgressBar != null) 
		          mProgressBar.setVisibility(View.INVISIBLE);
		             // optional need Vitamio 4.0
//		             mediaPlayer.setPlaybackSpeed(1.0f);
		         }
		         });
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		finish();
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		// TODO Auto-generated method stub
		return false;
	}

}
