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
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.artifex.mupdfdemo.R;
import com.artifex.utils.ConstantValues;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import me.grantland.widget.AutofitTextView;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class CategoryFragment extends AbsListViewBaseFragment {

    public static final int INDEX = 27;
    public String[] IMAGE_URLS;
    public String[] IMAGE_URLS_NEW;
    public String urlbase;
    public String urlcategory;
    public String classname;
    public String location;

    public String[] filename;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fr_image_grid, container, false);
        MyTask myTask = new MyTask();
        MySecondTask my2ndtask = new MySecondTask();
        listView = (GridView) rootView.findViewById(R.id.grid);

        Bundle bundle = this.getArguments();
        urlbase = bundle.getString("url");
        urlcategory = bundle.getString("url_category");
        classname = bundle.getString("class");
        location = bundle.getString("location");

        Log.d("offline test","Category fraG"+"url : "+urlbase+" urlcategory: "+urlcategory+" classname : "+classname);

        //urlbase="http://192.168.1.73:8081/SignInProject/category";
        //urlbase=getActivity().getIntent().getStringExtra("url");
        //myTask.execute();
        my2ndtask.execute("default", "Physics");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Category List");
        return rootView;
    }

    class ImageAdapter extends BaseAdapter {

        //private static final String[] IMAGE_URLS = Constants.IMAGES;

        private LayoutInflater inflater;


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
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
        }

        @Override
        public int getCount() {
            return IMAGE_URLS.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            View view = convertView;
            if (view == null) {
                view = inflater.inflate(R.layout.item_list_categories, parent, false);
                holder = new ViewHolder();
                assert view != null;

                //holder.imageView = (ImageView) view.findViewById(R.id.image);
                holder.textView = (AutofitTextView) view.findViewById(R.id.text);
                holder.cardView = (CardView) view.findViewById(R.id.card_view);
                //holder.progressBar = (ProgressBar) view.findViewById(R.id.progress);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            view.setBackgroundColor(color);
            //holder.cardView.setCardBackgroundColor(color);
            holder.cardView.setBackgroundColor(color);
            holder.textView.setText(IMAGE_URLS_NEW[position]);
            //ImageLoader.getInstance().displayImage(IMAGE_URLS[position], holder.imageView, options, animateFirstListener);

//			/*ImageLoader.getInstance()
//					.displayImage(IMAGE_URLS[position], holder.imageView, options, new SimpleImageLoadingListener() {
//						@Override
//						public void onLoadingStarted(String imageUri, View view) {
//							holder.progressBar.setProgress(0);
//							holder.progressBar.setVisibility(View.VISIBLE);
//						}
//
//						@Override
//						public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//							holder.progressBar.setVisibility(View.GONE);
//						}
//
//						@Override
//						public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//							holder.progressBar.setVisibility(View.GONE);
//						}
//					}, new ImageLoadingProgressListener() {
//						@Override
//						public void onProgressUpdate(String imageUri, View view, int current, int total) {
//							holder.progressBar.setProgress(Math.round(100.0f * current / total));
//						}
//					});*/

            return view;
        }
    }

    static class ViewHolder {
        ImageView imageView;
        ProgressBar progressBar;
        AutofitTextView textView;
        CardView cardView;

    }

    class MyTask extends AsyncTask<Void, Void, ArrayList<String>> {

        ArrayList<String> arr_linkText = new ArrayList<String>();
        ArrayList<String> arr_filename = new ArrayList<String>();


        @Override
        protected ArrayList<String> doInBackground(Void... params) {

            Document doc;
            String linkText = "";


            //String url1="http://server.mediacallz.com/ContentStore/files/Image/";
            //String url1="http://primeclass.biz/tution/aundh_class/documents/";
            try {
                doc = Jsoup.connect(urlbase).get();
                //Elements links = doc.select("td.right td a").get();
                for (Element el : doc.select("pre a")) {
                    // linkText = el.attr("href");
                    linkText = el.text();

                    Log.d("filename----", urlbase + linkText);
                    if (linkText.contains(".")) {
                        //String temp[]=linkText.split()
                        //String temp =linkText.substring(linkText.length()-27);
                        arr_filename.add(linkText);
                        arr_linkText.add(urlbase + linkText);
                    }// add value to ArrayList
                    Log.d("offline","Array list do in banckground : arr_filename : "+arr_filename + " link text : "+arr_linkText);
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
            filename = arr_filename.toArray(new String[0]);
            ((GridView) listView).setAdapter(new ImageAdapter(getActivity()));
            ((GridView) listView).setNumColumns(2);
            listView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    startPDFPagerActivity(arr_linkText.get(position));
                }
            });


        }
    }

    class MySecondTask extends AsyncTask<String, String, String> {
        ArrayList<String> arr_linkText = new ArrayList<String>();
        ArrayList<String> categorytitle = new ArrayList<String>();
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

            if(ConstantValues.offlineDemoFlag){
/*
                BufferedReader reader = new BufferedReader(new InputStreamReader("{\"id\":[\"72\",\"75\",\"76\",\"77\",\"78\",\"79\",\"84\",\"85\",\"86\",\"87\",\"88\",\"89\",\"90\",\"91\",\"92\"],\"title\":[\"Physics\",\"Chemisty\",\"Mathematics\",\"Biology\",\"JEE Question Papers\",\"JEE Main and Advanced Materials\",\"Computer \\u0026 IT Engg\",\"Electronic \\u0026 Telecom\",\"Electrical Engg\",\"Mechanical Engg\",\"Civil Engineering\",\"Instrument \\u0026 Control\",\"Productio\\u0026Industrial\",\"Gate Competative Exams \",\"Business and Management\"]}"));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                    Log.d("offline test", "DO IN BACKGROUND : " + "Result : " + result.toString());

                }
*/
                Log.d("offline test", "DO IN BACKGROUND : " + "passing Result : " +"\"{\\\"id\\\":[\\\"72\\\",\\\"75\\\",\\\"76\\\",\\\"77\\\",\\\"78\\\",\\\"79\\\",\\\"84\\\",\\\"85\\\",\\\"86\\\",\\\"87\\\",\\\"88\\\",\\\"89\\\",\\\"90\\\",\\\"91\\\",\\\"92\\\"],\\\"title\\\":[\\\"Physics\\\",\\\"Chemisty\\\",\\\"Mathematics\\\",\\\"Biology\\\",\\\"JEE Question Papers\\\",\\\"JEE Main and Advanced Materials\\\",\\\"Computer \\\\u0026 IT Engg\\\",\\\"Electronic \\\\u0026 Telecom\\\",\\\"Electrical Engg\\\",\\\"Mechanical Engg\\\",\\\"Civil Engineering\\\",\\\"Instrument \\\\u0026 Control\\\",\\\"Productio\\\\u0026Industrial\\\",\\\"Gate Competative Exams \\\",\\\"Business and Management\\\"]}\"" );

                // Pass data to onPostExecute method
                return ("{\"id\":[\"72\",\"75\",\"76\",\"77\",\"78\",\"79\",\"84\",\"85\",\"86\",\"87\",\"88\",\"89\",\"90\",\"91\",\"92\"],\"title\":[\"Physics\",\"Chemisty\",\"Mathematics\",\"Biology\",\"JEE Question Papers\",\"JEE Main and Advanced Materials\",\"Computer \\u0026 IT Engg\",\"Electronic \\u0026 Telecom\",\"Electrical Engg\",\"Mechanical Engg\",\"Civil Engineering\",\"Instrument \\u0026 Control\",\"Productio\\u0026Industrial\",\"Gate Competative Exams \",\"Business and Management\"]}");
            }
            else {
                try {

                    // Enter URL address where your php file resides
                    url = new URL(ConstantValues.baseDomainUrl + "/category");
                    conn = (HttpURLConnection) url.openConnection();
                    //conn.setReadTimeout(READ_TIMEOUT);
                    //conn.setConnectTimeout(CONNECTION_TIMEOUT);
//				conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    //conn.setDoOutput(true);
                /*Uri.Builder builder = new Uri.Builder()
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
				os.close();*/
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
                            Log.d("offline test", "DO IN BACKGROUND : " + "Result : " + result.toString());

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
        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            pdLoading.dismiss();

            try {

                JSONObject jsonObj = new JSONObject(result);

                //JSONArray list = new JSONArray(result);

                JSONArray list = jsonObj.getJSONArray("id");

                JSONArray list_title = jsonObj.getJSONArray("title");
                ArrayList<String> filenames = new ArrayList<>();
                ArrayList<String> description = new ArrayList<>();
                for (int i = 0; i < list.length(); i++) {
                    arr_linkText.add(list.getString(i));
                }
                for (int i = 0; i < list_title.length(); i++) {
                    categorytitle.add(list_title.getString(i));
                }
                IMAGE_URLS_NEW = categorytitle.toArray(new String[0]);
                IMAGE_URLS = arr_linkText.toArray(new String[0]);

                Log.d("offline","onPostExecute " +"IMAGE_URLS_NEW : "+IMAGE_URLS_NEW+" IMAGE_URLS : "+IMAGE_URLS);
                //filename=filenames.toArray(new String[0]);
                ((GridView) listView).setAdapter(new ImageAdapter(getActivity()));
                ((GridView) listView).setNumColumns(2);
                listView.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //startPDFPagerActivity(arr_linkText.get(position));
                        Fragment fr = null;
                        String tag = null;
                        if (location.equalsIgnoreCase("documents")) {
                            tag = PdfGridFragment.class.getSimpleName();
                            fr = getFragmentManager().findFragmentByTag(tag);
                            Bundle b = new Bundle();
                            b.putString("url", urlbase);
                            b.putString("classname", classname);
                            b.putString("category", IMAGE_URLS[position]);
                            if (fr == null) {
                                fr = new PdfGridFragment();
                                //fr.setArguments(getIntent().getExtras());
                                fr.setArguments(b);
                            }
                        } else if (location.equalsIgnoreCase("images")) {
                            tag = ImageGridFragment.class.getSimpleName();
                            fr = getFragmentManager().findFragmentByTag(tag);
                            Bundle b = new Bundle();
                            b.putString("url", urlbase);
                            b.putString("classname", classname);
                            b.putString("category", IMAGE_URLS[position]);
                            if (fr == null) {
                                fr = new ImageGridFragment();
                                fr.setArguments(b);
                            }
                        } else if (location.equalsIgnoreCase("videos")) {
                            tag = VideoListFragment.class.getSimpleName();
                            fr = getFragmentManager().findFragmentByTag(tag);
                            Bundle b = new Bundle();
                            b.putString("url", urlbase);
                            b.putString("classname", classname);
                            b.putString("category", IMAGE_URLS[position]);
                            if (fr == null) {
                                fr = new VideoListFragment();
                                fr.setArguments(b);
                            }
                        } else if (location.equalsIgnoreCase("music")) {
                            tag = ImageMusicListFragment.class.getSimpleName();
                            fr = getFragmentManager().findFragmentByTag(tag);
                            Bundle b = new Bundle();
                            b.putString("url", urlbase);
                            b.putString("classname", classname);
                            b.putString("category", IMAGE_URLS[position]);
                            if (fr == null) {
                                fr = new ImageMusicListFragment();
                                fr.setArguments(b);
                            }
                        }


                        getFragmentManager().beginTransaction().replace(R.id.fragmentid, fr).addToBackStack("test").commit();
                        //getFragmentManager().beginTransaction().addToBackStack(null);
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onResume() {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        super.onResume();
    }
}