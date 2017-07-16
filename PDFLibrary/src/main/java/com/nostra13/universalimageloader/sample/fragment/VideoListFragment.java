/*******************************************************************************
 * Copyright 2011-2014 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.nostra13.universalimageloader.sample.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.artifex.mupdfdemo.R;
import com.artifex.utils.ConstantValues;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class VideoListFragment extends AbsListViewBaseFragment {

    public static final int INDEX = 33;
    public String[] IMAGE_URLS;
    public String[] filenames;
    public String urlbase;
    public String[] filename;
    public String classname;
    public String category;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fr_image_list, container, false);
        /*if (container != null) {
			container.removeAllViews();
		}*/
        rootView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT));


        MyTask myTask = new MyTask();
        MySecondTask mySecondTask = new MySecondTask();

        Bundle bundle = this.getArguments();
        urlbase = bundle.getString("url");
        classname = bundle.getString("classname");
        category = bundle.getString("category");

        //urlbase=getActivity().getIntent().getStringExtra("url");
        listView = (ListView) rootView.findViewById(android.R.id.list);
        //myTask.execute();
        mySecondTask.execute(classname, category);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Video Lecture List");
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AnimateFirstDisplayListener.displayedImages.clear();
    }

    private class ImageAdapter extends BaseAdapter {

//        private static String[] IMAGE_URLS;
//
//        static {
//
//                IMAGE_URLS = Constants.IMAGES;
//
//        }

        private LayoutInflater inflater;
        private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

        private DisplayImageOptions options;

        ImageAdapter(Context context) {
            inflater = LayoutInflater.from(context);

            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.ic_stub)
                    .showImageForEmptyUri(R.drawable.ic_empty)
                    .showImageOnFail(R.drawable.ic_error)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .displayer(new CircleBitmapDisplayer(Color.WHITE, 5))
                    .build();
        }

        @Override
        public int getCount() {
            return IMAGE_URLS.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = convertView;
            final ViewHolder holder;
            if (convertView == null) {
                view = inflater.inflate(R.layout.item_list_video, parent, false);
                holder = new ViewHolder();
                holder.text = (TextView) view.findViewById(R.id.text);
                holder.image = (ImageView) view.findViewById(R.id.image);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            //holder.text.setText("Track " + (position + 1));
            holder.text.setText(filename[position]);
            holder.image.setImageDrawable(getResources().getDrawable(R.drawable.video));

            // ImageLoader.getInstance().displayImage(IMAGE_URLS[position], holder.image, options, animateFirstListener);

            return view;
        }
    }

    static class ViewHolder {
        TextView text;
        ImageView image;
    }

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }


    class MyTask extends AsyncTask<Void, Void, ArrayList<String>> {

        ArrayList<String> arr_linkText = new ArrayList<String>();

        @Override
        protected ArrayList<String> doInBackground(Void... params) {

            Document doc;
            String linkText = "";
            //String url1="http://server.mediacallz.com/ContentStore/files/Audio/";
            try {
                doc = Jsoup.connect(urlbase).get();
                //Elements links = doc.select("td.right td a").get();
                int i = 0;
                filenames = new String[100];
                for (Element el : doc.select("td a")) {
                    linkText = el.text();

                    Log.d("filename----", urlbase + linkText);
                    if (linkText.contains(".")) {
                        filenames[i] = linkText;
                        arr_linkText.add(urlbase + linkText);
                        i++;
                    }// add value to ArrayList

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
            ((ListView) listView).setAdapter(new ImageAdapter(getActivity()));
            listView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //startAugioPagerActivity(position,urlbase);
                    startVideoPagerActivityNew(arr_linkText.get(position));
                    //startVideoPagerActivity(position);
                }
            });

        }
    }

    class MySecondTask extends AsyncTask<String, String, String> {
        ArrayList<String> arr_linkText = new ArrayList<String>();
        HttpURLConnection conn;
        URL url = null;
        ProgressDialog pdLoading = new ProgressDialog(getActivity());

        //String url;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                // Enter URL address where your php file resides
                url = new URL(ConstantValues.baseDomainUrl + "/" + "VideoAndroid");
                conn = (HttpURLConnection) url.openConnection();
                //conn.setReadTimeout(READ_TIMEOUT);
                //conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("classname", params[0])
                        .appendQueryParameter("categoryname", params[1]);
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {
                    return ("false");
                }
                //cookie code

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return ("false");
                //return "exception";
            } catch (ProtocolException e) {
                e.printStackTrace();
                return ("false");
            } catch (IOException e) {
                e.printStackTrace();
                return ("false");
            } finally {
                conn.disconnect();
            }

        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            pdLoading.dismiss();

            try {

                //JSONObject jsonObj = new JSONObject(result);

                JSONArray list = new JSONArray(result);

                ArrayList<String> filenames = new ArrayList<>();
                final ArrayList<String> description = new ArrayList<>();
                for (int i = 0; i < list.length(); i++) {

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        JSONArray list1 = (JSONArray) list.get(i);


                        arr_linkText.add(urlbase + list1.getString(0));
                        filenames.add(list1.getString(1));
                        description.add(list1.getString(2));

                    }

                    //JSONObject c = user.getJSONObject(i);
                    //arr_linkText.add(urlbase+list.getString(i));
                }

                IMAGE_URLS = arr_linkText.toArray(new String[0]);
                filename = filenames.toArray(new String[0]);
                ((ListView) listView).setAdapter(new ImageAdapter(getActivity()));
                listView.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //startAugioPagerActivity(position,urlbase);
                        //startVideoPagerActivityNew(arr_linkText.get(position));
                        //startVideoPagerActivity(position);

                        android.support.v4.app.Fragment fr;
                        String tag = VideoFragment.class.getSimpleName();
                        fr = getFragmentManager().findFragmentByTag(tag);
                        Bundle b = new Bundle();
                        b.putString("url", arr_linkText.get(position));
                        b.putString("title", filename[position]);
                        b.putString("desc", description.get(position));
                        //b.putString("classname", classname);
                        //b.putString("category", IMAGE_URLS[position]);
                        if (fr == null) {
                            fr = new VideoFragment();
                            fr.setArguments(b);
                        }
                        getFragmentManager().beginTransaction().replace(R.id.fragmentid, fr).addToBackStack("test").commit();
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


}
