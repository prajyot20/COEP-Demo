package com.nostra13.universalimageloader.sample.fragment;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.artifex.mupdfdemo.R;

//import net.protyposis.android.mediaplayer.MediaSource;

//import net.protyposis.android.mediaplayer.MediaSource;

//import net.protyposis.android.mediaplayer.MediaPlayer;
//import net.protyposis.android.mediaplayer.MediaSource;
//import net.protyposis.android.mediaplayer.VideoView;


public class AudioFragment extends BaseFragment {

    private static final String TAG = AudioFragment.class.getSimpleName();

    private VideoView mVideoView;
    private ProgressBar mProgress;

    private MediaController.MediaPlayerControl mMediaPlayerControl;
    private MediaController mMediaController;

    private Uri mVideoUri;
    private int mVideoPosition;
    private float mVideoPlaybackSpeed;
    private boolean mVideoPlaying;
    TextView name1;

//    private MediaSource mMediaSource;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (container != null) {
            container.removeAllViews();
        }
        //setContentView(R.layout.activity_audio2);
        View rootView = inflater.inflate(R.layout.activity_audio2, container, false);
        //Utils.setActionBarSubtitleEllipsizeMiddle(this);

        mVideoView = (VideoView) rootView.findViewById(R.id.vv);
        mProgress = (ProgressBar) rootView.findViewById(R.id.progress);
        name1 = (TextView) rootView.findViewById(R.id.name1);
//        mMediaPlayerControl = mVideoView; //new MediaPlayerDummyControl();
        mMediaController = new MediaController(getActivity());
        mMediaController.setAnchorView(mVideoView);
        mMediaController.setMediaPlayer(mVideoView);
//       if(!mMediaController.isShowing())
//        {mMediaController.show();}
//        mMediaController.setMediaPlayer(mMediaPlayerControl);
//        mMediaController.setEnabled(false);

        // mProgress.setVisibility(View.VISIBLE);

        // Init video playback state (will eventually be overwritten by saved instance state)
        Bundle bundle = this.getArguments();
        String[] urlsComp = bundle.getString("url").split("/");

        //String[] urlsComp = getActivity().getIntent().getStringExtra("url111").split("/");
        String nameofaudio = urlsComp[urlsComp.length - 1];
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

        mVideoUri = Uri.parse(finalUrl);
        mVideoPosition = 0;
        mVideoPlaybackSpeed = 1;
        mVideoPlaying = false;
        mVideoView.setMediaController(mMediaController);
        mVideoView.setVideoURI(mVideoUri);
        name1.setText(nameofaudio);
//        mMediaController.requestFocus();
//        mVideoView.start();
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mMediaController.show(0);
//                mMediaController.setEnabled(true);
            }
        });
//        mMediaController.show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Audio Lecture");
        return rootView;
    }
}