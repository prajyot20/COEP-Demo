package com.nostra13.universalimageloader.sample.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.artifex.mupdfdemo.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.sample.Constants;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.util.ArrayList;

/**
 * Created by maitray on 9/8/16.
 */
public class VideoPagerFragment extends Fragment {
    public static final int INDEX = 9;
    public static Integer viewon = null;
    public String[] IMAGE_URLS;
    ViewPager pager;
    View imageLayout;
    Button button;
    ImageView image;
    VideoView2 videoview = null;
    VideoView2[] videoViews;
    View rootView;
    String simage;
    Uri video;
    public String urlbase;
    MediaController mediacontroller;
    public static Boolean videoPlaying = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fr_image_pager, container, false);
//        videoViews = new ArrayList<>();
        MyTask myTask = new MyTask();


        pager = (ViewPager) rootView.findViewById(R.id.pager);
        pager.setVisibility(View.GONE);
//        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
        urlbase = getActivity().getIntent().getStringExtra("url");
        myTask.execute();
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Toast.makeText(getContext(), "position " + isVisibleToUser, Toast.LENGTH_LONG).show();
        if (isVisibleToUser) {
            videoview.start();
        } else {
            videoview.stopPlayback();
        }
    }


    private class ImageAdapter extends PagerAdapter {
        private Integer prev;
        private LayoutInflater inflater;
        private DisplayImageOptions options;

        ImageAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return IMAGE_URLS.length;
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            final int pos = position;
            mediacontroller = new MediaController(getActivity());
            mediacontroller.setAnchorView(videoview);
            imageLayout = inflater.inflate(R.layout.item_video_pager, view, false);
            assert imageLayout != null;

            videoview = (VideoView2) imageLayout.findViewById(R.id.videoView);
            final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);
            button = (Button) imageLayout.findViewById(R.id.button);
            simage = IMAGE_URLS[position];
            try {
                // Start the MediaController
                // Get the URL from String VideoURL
                video = Uri.parse(simage);
                videoview.setMediaController(mediacontroller);
                videoview.setVideoURI(video);

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

            videoview.requestFocus();
            videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                // Close the progress bar and play the video
                public void onPrepared(MediaPlayer mp) {
//                        if(prev == null || prev == pos){
//                            prev = pos;
//                            videoview.start();
//                        }

                }
            });
//            videoViews.add(position,videoview);
            videoViews[position] = videoview;
            if (viewon == null) {
                videoViews[position].start();
                viewon = position;
            }

            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    new DownloadImage().execute(simage);
                    String path1 = Environment.getExternalStorageDirectory().toString();
                    path1 = path1 + "/imageloader";
                    Toast.makeText(getActivity().getApplication().getBaseContext(), "The file is downloaded at " + path1, Toast.LENGTH_LONG).show();
                }
            });

            view.addView(imageLayout, 0);
            return imageLayout;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);

//            videoview.stopPlayback();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }
    }


    class MyTask extends AsyncTask<Void, Void, ArrayList<String>> {

        ArrayList<String> arr_linkText = new ArrayList<String>();

        @Override
        protected ArrayList<String> doInBackground(Void... params) {

            Document doc;
            String linkText = "";
            //String url1="http://primeclass.biz/tution/aundh_class/videos/";
            try {
                doc = Jsoup.connect(urlbase).get();
                //Elements links = doc.select("td.right td a").get();
                for (Element el : doc.select("pre a")) {
                    linkText = el.text();
                    Log.d("filename----", urlbase + linkText);
                    if (linkText.contains(".")) {
                        arr_linkText.add(urlbase + linkText);
                    }
                    // add value to ArrayList
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return arr_linkText;     //<< retrun ArrayList from here
        }


        @Override
        protected void onPostExecute(ArrayList<String> result) {

            IMAGE_URLS = arr_linkText.toArray(new String[0]);
            videoViews = new VideoView2[IMAGE_URLS.length];

            //pager.setOffscreenPageLimit(0);
            pager.setAdapter(new ImageAdapter(getActivity()));
            pager.setVisibility(View.VISIBLE);
            pager.setCurrentItem(getArguments().getInt(Constants.Extra.IMAGE_POSITION));
//            pager.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    pager.setCurrentItem(getArguments().getInt(Constants.Extra.IMAGE_POSITION));
//                }
//            },200);
            pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    Toast.makeText(getContext(), "position " + position + " prev " + viewon, Toast.LENGTH_SHORT).show();
                    if (viewon != position) {
                        videoViews[viewon].pause();
                        videoViews[position].start();
                        viewon = position;
                    }
                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    //videoview.stopPlayback();
                }
            });


        }
    }

    // DownloadImage AsyncTask
    private class DownloadImage extends AsyncTask<String, Void, String> {

        ProgressDialog PD;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            PD = ProgressDialog.show(getActivity(), null, "Downloading ...", true);
            PD.setCancelable(true);
        }

        @Override
        protected String doInBackground(String... URL) {
            String filename = new BigInteger(130, new SecureRandom()).toString(32) + ".mp4";
            DownloadFile(URL[0], filename);
            return URL[0];
        }

        @Override
        protected void onPostExecute(String result) {
            PD.dismiss();
        }


    }

    public void DownloadFile(String fileURL, String fileName) {
        try {
            String RootDir = Environment.getExternalStorageDirectory()
                    + File.separator + "ImageLoader" + File.separator + "Video";
            File RootFile = new File(RootDir);
            RootFile.mkdir();
            File RootFile1 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

            // File root = Environment.getExternalStorageDirectory();
            URL u = new URL(fileURL);
            HttpURLConnection c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();
            FileOutputStream f = new FileOutputStream(new File(RootFile1,
                    fileName));
            InputStream in = c.getInputStream();
            byte[] buffer = new byte[1024];
            int len1 = 0;

            while ((len1 = in.read(buffer)) > 0) {
                f.write(buffer, 0, len1);
            }
            f.close();


        } catch (Exception e) {

            Log.d("Error....", e.toString());
        }

    }
}
