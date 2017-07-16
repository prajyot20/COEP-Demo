package com.nostra13.universalimageloader.sample.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

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
public class ImageMusicPagerFragment extends BaseFragment {
    public static final int INDEX = 14;
    public String[] IMAGE_URLS;
    ViewPager pager;
    View imageLayout;
    Button button;
    ImageView image;
    VideoView videoview = null;
    TextView textview = null;
    View rootView;
    String simage;
    Uri video;
    MediaController mediacontroller;
    String[] filenames;
    public String urlbase = null;

    public static Boolean videoPlaying = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fr_image_pager, container, false);

        mediacontroller = new MediaController(getActivity()) {

            @Override
            public void hide() {
                mediacontroller.show();
            }

            public boolean dispatchKeyEvent(KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_BACK)
                    ((Activity) getContext()).finish();

                return super.dispatchKeyEvent(event);
            }
        };

        //      mediacontroller.setAnchorView(videoview);

        MyTask myTask = new MyTask();

        urlbase = getActivity().getIntent().getStringExtra("url");
        pager = (ViewPager) rootView.findViewById(R.id.pager);
        //pager.setOffscreenPageLimit(0);
        myTask.execute();
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // videoPlaying = false;
            }


            @Override
            public void onPageSelected(int position) {
                simage = IMAGE_URLS[position];
                try {
                    // Start the MediaController
                    // Get the URL from String VideoURL
                    video = Uri.parse(simage);

                    videoview.setMediaController(mediacontroller);
                    if (!mediacontroller.isShowing()) {
                        mediacontroller.show();
                    }
                    videoview.setVideoURI(video);

                    textview.setText(filenames[position]);
                    videoview.start();

                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }

                //videoview.requestFocus();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return rootView;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible()) {
            if (!isVisibleToUser) {
                videoview = (VideoView) pager.getFocusedChild();
                videoview.pause();
            }
            if (isVisibleToUser) {
            }
        }
    }

    private class ImageAdapter extends PagerAdapter {

        //	private static final String[] IMAGE_URLS = Constants.IMAGES;
        //final RecyclerView.ViewHolder holder;
        private LayoutInflater inflater;
        private DisplayImageOptions options;

        ImageAdapter(Context context) {
            inflater = LayoutInflater.from(context);

//			options = new DisplayImageOptions.Builder()
//					.showImageForEmptyUri(R.drawable.ic_empty)
//					.showImageOnFail(R.drawable.ic_error)
//					.resetViewBeforeLoading(true)
//					.cacheOnDisk(true)
//					.imageScaleType(ImageScaleType.EXACTLY)
//					.bitmapConfig(Bitmap.Config.RGB_565)
//					.considerExifParams(true)
//					.displayer(new FadeInBitmapDisplayer(300))
//					.build();
//            holder = (RecyclerView.ViewHolder) view.getTag();;
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
            imageLayout = inflater.inflate(R.layout.item_audio_pager_new, view, false);
            assert imageLayout != null;

            videoview = (VideoView) imageLayout.findViewById(R.id.videoView);
            final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);
            button = (Button) imageLayout.findViewById(R.id.button);
            textview = (TextView) imageLayout.findViewById(R.id.textview);
            textview.setText(filenames[position]);
//

//            final String simage=IMAGE_URLS[position];
//            try {
//                // Start the MediaController
//                MediaController mediacontroller = new MediaController(getActivity());
//                mediacontroller.setAnchorView(videoview);
//                // Get the URL from String VideoURL
//                Uri video = Uri.parse(simage);
//                videoview.setMediaController(mediacontroller);
//                videoview.setVideoURI(video);
//
//            } catch (Exception e) {
//                Log.e("Error", e.getMessage());
//                e.printStackTrace();
//            }
//
//            videoview.requestFocus();
//            videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                // Close the progress bar and play the video
//                public void onPrepared(MediaPlayer mp) {
////                    if(!videoPlaying){
////                        videoview.start();
////                        videoPlaying = true;
////                    }
////                    if(videoPlaying)
////                    {
////
////                        videoview.stopPlayback();
////                        videoPlaying=false;
////                    }
//                        videoview.start();
//                }
//            });
//            ImageLoader.getInstance().displayImage(IMAGE_URLS[position], imageView, options, new SimpleImageLoadingListener() {
//				@Override
//				public void onLoadingStarted(String imageUri, View view) {
//					spinner.setVisibility(View.VISIBLE);
//				}
//
//				@Override
//				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//					String message = null;
//					switch (failReason.getType()) {
//						case IO_ERROR:
//							message = "Input/Output error";
//							break;
//						case DECODING_ERROR:
//							message = "Image can't be decoded";
//							break;
//						case NETWORK_DENIED:
//							message = "Downloads are denied";
//							break;
//						case OUT_OF_MEMORY:
//							message = "Out Of Memory error";
//							break;
//						case UNKNOWN:
//							message = "Unknown error";
//							break;
//					}
//					Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
//
//					spinner.setVisibility(View.GONE);
//				}
//
//				@Override
//				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//					spinner.setVisibility(View.GONE);
//				}
//			});

            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {

                    // Execute DownloadImage AsyncTask
                    new DownloadImage().execute(simage);
                    String path1 = Environment.getExternalStorageDirectory().toString();
                    path1 = path1 + "/imageloader";

                    Toast.makeText(getActivity().getApplication().getBaseContext(), "The file is downloaded at " + path1, Toast.LENGTH_LONG).show();
                }
            });

            view.addView(imageLayout, 0);
            return imageLayout;


        }

//        @Override
//        public void setPrimaryItem(ViewGroup container, int position, Object object) {
//            super.setPrimaryItem(container, position, object);
//            videoview.stopPlayback();
//
//        }

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
            // String url1="http://server.mediacallz.com/ContentStore/files/Audio/";
            try {
                doc = Jsoup.connect(urlbase).get();
                //Elements links = doc.select("td.right td a").get();
                int i = 0;
                filenames = new String[100];
                for (Element el : doc.select("pre a")) {
                    linkText = el.text();

                    Log.d("filename----", urlbase + linkText);
                    if (linkText.contains(".")) {
                        filenames[i] = linkText;
                        arr_linkText.add(urlbase + linkText);
                    }
                    // add value to ArrayList
                    i++;
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
            pager.setAdapter(new ImageAdapter(getActivity()));
            pager.setCurrentItem(getArguments().getInt(Constants.Extra.IMAGE_POSITION, 0));


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
