package com.nostra13.universalimageloader.sample.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.artifex.mupdfdemo.MuPDFActivity;

import com.artifex.mupdfdemo.R;
import com.artifex.utils.ConstantValues;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class PdfViewFragment extends BaseFragment implements OnPageChangeListener, OnLoadCompleteListener {

    PDFView pdfView;
    TextView title_textview;
    //MultiAutoCompleteTextView desc_textview;
    ByteArrayOutputStream bytearray = new ByteArrayOutputStream();
    String filename;
    String title;
    String desccription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.item_pdf_pager, container, false);
        //setContentView(R.layout.item_pdf_pager);

        setRetainInstance(true);
        pdfView = (PDFView) rootView.findViewById(R.id.pdfView);
        title_textview = (TextView) rootView.findViewById(R.id.title);
        //desc_textview= (MultiAutoCompleteTextView) rootView.findViewById(R.id.desc);
        String samplefile = "samle2.pdf";
//        Uri fileuri = Uri.parse(getActivity().getIntent().getStringExtra("filename"));
        Bundle bundle = this.getArguments();
        filename = bundle.getString("url");
        title = bundle.getString("title");
        desccription = bundle.getString("desc");
        //filename=getActivity().getIntent().getStringExtra("filename");
        //URL url= null;
        ByteArrayTask bt = new ByteArrayTask();
        bt.execute();
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        //getActivity().setSupportActionBar(toolbar);
/*
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        ////////////////////////////////////////////////////////////////////////////////////////////////////


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
*/
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(filename);
        return rootView;
    }

/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intent2 = new Intent(Main2Activity.this, SimpleImageActivity.class);
            intent2.putExtra(Constants.Extra.FRAGMENT_INDEX, PdfGridFragment.INDEX);
            intent2.putExtra("url",baseurl+path.getText()+"/documents/");
            startActivity(intent2);
            Toast.makeText(getApplicationContext(),"TEST",Toast.LENGTH_SHORT).show();
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(Main2Activity.this, SimpleImageActivity.class);
            intent.putExtra(Constants.Extra.FRAGMENT_INDEX, ImageGridFragment.INDEX);
            intent.putExtra("url",baseurl+path.getText()+"/images/");
            startActivity(intent);


        } else if (id == R.id.nav_slideshow) {
            Intent intent1 = new Intent(Main2Activity.this,SimpleImageActivity.class);
            intent1.putExtra(Constants.Extra.FRAGMENT_INDEX, VideoListFragment.INDEX);
            intent1.putExtra("url",baseurl+path.getText()+"/videos/");
            startActivity(intent1);


        } else if (id == R.id.nav_manage) {
            Intent intent3 = new Intent(Main2Activity.this,SimpleImageActivity.class);
            intent3.putExtra(Constants.Extra.FRAGMENT_INDEX, ImageMusicListFragment.INDEX);
            intent3.putExtra("url",baseurl+path.getText()+"/audio/");
            startActivity(intent3);

        } else if (id == R.id.nav_share) {
            Intent intent4 = new Intent(Main2Activity.this,WebviewActivity.class);
            //intent3.putExtra(Constants.Extra.FRAGMENT_INDEX, ImageMusicListFragment.INDEX);
            //intent3.putExtra("url",baseurl+path.getText()+"/audio/");
            startActivity(intent4);


        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

*/


    @Override
    public void loadComplete(int nbPages) {

    }

    @Override
    public void onPageChanged(int page, int pageCount) {

    }


    FileOutputStream fileOutput;
    File writeFile;
    String lastFileName;

    class ByteArrayTask extends AsyncTask<Object, Object, FileOutputStream> {

        private ProgressDialog pdia = new ProgressDialog(getActivity());

       // ByteArrayOutputStream baos = new ByteArrayOutputStream();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia.setMessage("Loading File from Server... It might takes some time depending on internet speed and file size.");
           pdia.show();
        }
        @Override
        protected FileOutputStream doInBackground(Object... strings) {
            URL url = null;

            Log.d("Prajyot"," file name : "+filename);
            lastFileName = ConstantValues.getLastWord(filename);
            File file = new File(ConstantValues.fileStoragePath + lastFileName);
            if(!file.exists()){
                try {
                url = new URL(filename);
                writeFile = new File(ConstantValues.fileStoragePath + lastFileName);
                    Log.d("Prajyot","write file");
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                //urlConnection.setRequestMethod("GET");
                //urlConnection.setDoOutput(true);
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                FileOutputStream fileOutputStream = new FileOutputStream(writeFile);
                int totalSize = urlConnection.getContentLength();

                byte[] buffer = new byte[MEGABYTE];
                int bufferLength = 0;
                while((bufferLength = inputStream.read(buffer))>0 ){
                    fileOutputStream.write(buffer, 0, bufferLength);
                }
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
/*
                try {
                url = new URL(filename);
                writeFile = new File(ConstantValues.fileStoragePath + lastFileName);
                fileOutput = new FileOutputStream(writeFile);
                //Toast.makeText(getActivity(),"write File Name "+writeFile.getName(),Toast.LENGTH_SHORT).show();
                    InputStream is = null;
                    try {
                        is = url.openStream();
                        byte[] byteChunk = new byte[4000]; // Or whatever size you want to read in at a time.
                        int n;
                        while ((n = is.read(byteChunk)) > 0) {
                             //baos.write(byteChunk, 0, n);
                            Log.d("Prajyot n : ",""+n);
                            fileOutput.write(byteChunk);
                        }

                        ////////////////////////

                    } catch (IOException e) {
                        System.err.printf("Failed while reading bytes from %s: %s", url.toExternalForm(), e.getMessage());
                        e.printStackTrace();
                        // Perform any other exception handling that's appropriate.
                    } finally {
                        if (is != null) {
                            try {
                                is.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
*/


            }
            else{

                writeFile = new File(ConstantValues.fileStoragePath + lastFileName);
            }


            return fileOutput;
        }

        @Override
        protected void onPostExecute(FileOutputStream result) {
            //super.onPostExecute(result);
            //bytearray=baos;
            pdia.dismiss();

            showPDF(writeFile);
        }
    }

    private static final int  MEGABYTE = 1024 * 1024;


    public void showPDF(File writeFile) {

        try {

            File f = new File(filename);

           // Uri uri = Uri.parse("/sdcard/Download/testing.pdf");
            Intent intent = new Intent(getActivity(), MuPDFActivity.class);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.fromFile(writeFile));
            startActivity(intent);
            getActivity().finish();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
