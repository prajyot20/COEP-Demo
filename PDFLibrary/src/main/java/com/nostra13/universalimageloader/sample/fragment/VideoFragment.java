package com.nostra13.universalimageloader.sample.fragment;

import android.app.ActionBar;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.artifex.mupdfdemo.R;
import com.nostra13.universalimageloader.sample.activity.AudioActivity2;
import com.universalvideoview.UniversalMediaController;
import com.universalvideoview.UniversalVideoView;
//import net.protyposis.android.mediaplayer.MediaSource;

//import net.protyposis.android.mediaplayer.MediaSource;

//import net.protyposis.android.mediaplayer.MediaPlayer;
//import net.protyposis.android.mediaplayer.MediaSource;
//import net.protyposis.android.mediaplayer.VideoView;


public class VideoFragment extends BaseFragment implements UniversalVideoView.VideoViewCallback {

    private static final String TAG = AudioActivity2.class.getSimpleName();

    UniversalVideoView mVideoView;
    UniversalMediaController mMediaController;

    View mBottomLayout;
    View mVideoLayout;
    TextView mStart;
    String videourl;

    private int mSeekPosition;
    private int cachedHeight;
    private boolean isFullscreen;
    private static final String SEEK_POSITION_KEY = "SEEK_POSITION_KEY";


    /*private VideoView mVideoView; */
    private ProgressBar mProgress;
    TextView title_textview;
    TextView desc_textview;

    /*  private MediaController.MediaPlayerControl mMediaPlayerControl;
      private MediaController mMediaController;
  */
    private Uri mVideoUri;
    private int mVideoPosition;
    private float mVideoPlaybackSpeed;
    private boolean mVideoPlaying;
    String title;
    String description;
//    private MediaSource mMediaSource;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.activity_video, container, false);
        // setContentView(R.layout.activity_video);
        //Utils.setActionBarSubtitleEllipsizeMiddle(this);
        mVideoLayout = rootView;
        // mBottomLayout=rootView.findViewById(R.id.bottom_layout);


        title_textview = (TextView) rootView.findViewById(R.id.title);
        //desc_textview= (TextView) rootView.findViewById(R.id.desc);

        //old code
//        mVideoView = (VideoView) rootView.findViewById(R.id.vv1);
        mProgress = (ProgressBar) rootView.findViewById(R.id.progress1);

//        mMediaPlayerControl = mVideoView; //new MediaPlayerDummyControl();

        /* //old code
               mMediaController = new MediaController(getActivity());
        mMediaController.setAnchorView(mVideoView);
*/


//        mMediaController.setMediaPlayer(mMediaPlayerControl);
//        mMediaController.setEnabled(false);

        // mProgress.setVisibility(View.VISIBLE);
        Bundle bundle = this.getArguments();
        String[] urlsComp = bundle.getString("url").split("/");
        title = bundle.getString("title");
        description = bundle.getString("desc");
        // Init video playback state (will eventually be overwritten by saved instance state)
        // String[] urlsComp = getActivity().getIntent().getStringExtra("url111").split("/");
        urlsComp[urlsComp.length - 1] = Uri.encode(urlsComp[urlsComp.length - 1]);
        StringBuilder sb = new StringBuilder();
        for (String n : urlsComp) {
            if (n.contains("http")) {
                sb.append(n).append('/');
            } else if (!n.trim().equals("")) {
                sb.append("/").append(n);
            }
        }

        String finalUrl = sb.toString();
        videourl = finalUrl;
        title_textview.setText("Title: " + title + "\n Description: " + description);
        //desc_textview.setText(description);


        //old code
       /* mVideoUri = Uri.parse(finalUrl);
        mVideoPosition = 0;
        mVideoPlaybackSpeed = 1;
        mVideoPlaying = false;
        mVideoView.setMediaController(mMediaController);
        mVideoView.setVideoURI(mVideoUri);
        mVideoView.start();
*/

        //new code


        mVideoView = (UniversalVideoView) rootView.findViewById(R.id.videoView);
        mMediaController = (UniversalMediaController) rootView.findViewById(R.id.media_controller);
        mVideoView.setMediaController(mMediaController);
        setVideoAreaSize();
        mVideoView.start();
        //mVideoView.setVideoViewCallback(g);
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d(TAG, "onCompletion ");
            }
        });

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Video Lecture");

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause ");
        if (mVideoView != null && mVideoView.isPlaying()) {
            mSeekPosition = mVideoView.getCurrentPosition();
            Log.d(TAG, "onPause mSeekPosition=" + mSeekPosition);
            mVideoView.pause();
        }
    }

    private void setVideoAreaSize() {
        mVideoLayout.post(new Runnable() {
            @Override
            public void run() {
                int width = mVideoLayout.getWidth();
                cachedHeight = (int) (width * 405f / 720f);
//                cachedHeight = (int) (width * 3f / 4f);
//                cachedHeight = (int) (width * 9f / 16f);
                ViewGroup.LayoutParams videoLayoutParams = mVideoLayout.getLayoutParams();
                videoLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                videoLayoutParams.height = cachedHeight;
                mVideoLayout.setLayoutParams(videoLayoutParams);
                mVideoView.setVideoPath(videourl);
                mVideoView.requestFocus();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState Position=" + mVideoView.getCurrentPosition());
        outState.putInt(SEEK_POSITION_KEY, mSeekPosition);
    }


    @Override
    public void onScaleChange(boolean isFullscreen) {
        this.isFullscreen = isFullscreen;
        if (isFullscreen) {
            ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.FILL_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.FILL_PARENT;
            mVideoLayout.setLayoutParams(layoutParams);
            mBottomLayout.setVisibility(View.GONE);

        } else {
            ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = this.cachedHeight;
            mVideoLayout.setLayoutParams(layoutParams);
            mBottomLayout.setVisibility(View.VISIBLE);
        }

        switchTitleBar(!isFullscreen);
    }

    private void switchTitleBar(boolean show) {
        ActionBar supportActionBar = getActivity().getActionBar();
        if (supportActionBar != null) {
            if (show) {
                supportActionBar.show();
            } else {
                supportActionBar.hide();
            }
        }
    }


    @Override
    public void onPause(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onStart(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onBufferingStart(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onBufferingEnd(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        mVideoView.setFullscreen(true);
        super.onConfigurationChanged(newConfig);
    }
}