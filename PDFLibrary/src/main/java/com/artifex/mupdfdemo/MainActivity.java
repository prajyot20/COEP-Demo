package com.artifex.mupdfdemo;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static String flag = "flag";
    static String about = "abuot";
    static String uri = "uri";

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String SAMPLE_FILE = "book.pdf";
    // PDFView pdfView;
    Integer pageNumber = 0;
    String pdfFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /*
        pdfView= (PDFView)findViewById(R.id.pdfView);
       displayFromAsset(SAMPLE_FILE);*/

   /*  findViewById(R.id.imageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri;// = Uri.parse(mFiles[position].getAbsolutePath());
                uri = Uri.parse("/storage/emulated/0/Download/testingpdf.pdf");
                Intent intent = new Intent(MainActivity.this,MuPDFActivity.class);
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(uri);
                startActivity(intent);

                // startActivity(new Intent(MainActivity.this,ChoosePDFActivity.class));
            }
        });*/
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

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

     /*   if (id == R.id.nav_book) {
            startActivity(new Intent(MainActivity.this, com.mapedutech.akshar.WebviewActivity1.class).putExtra(flag,
                    about));

        } else if (id == R.id.nav_clg_gallery) {
            //startActivity(new Intent(MainActivity.this, ImageActivity.class));
            Toast.makeText(getApplicationContext(),"Coming Soon...\nGallery will be uploaded once Books Inaugration is done. \n Thanks",Toast.LENGTH_SHORT).show();


        } else if (id == R.id.nav_videolectures) {
            startActivity(new Intent(MainActivity.this, com.mapedutech.akshar.VideoviewActivity.class));

        } else if (id == R.id.nav_share) {


            // Create and fire off our Intent in one fell swoop
            ShareCompat.IntentBuilder
                    .from(this) // getActivity() or activity field if within Fragment
                    .setText(("Amazing Application You must use it."))
                    .setType("text/plain") // most general text sharing MIME type
                    .setChooserTitle(getResources().getString(R.string.shareTitle))
                    .startChooser();

        } else if (id == R.id.nav_aboutColg) {
            startActivity(new Intent(MainActivity.this, com.mapedutech.akshar.WebviewActivity1.class).putExtra(flag,
                    "bookwiki"));
        } else if (id == R.id.nav_academics) {
            startActivity(new Intent(MainActivity.this, com.mapedutech.akshar.WebviewActivity1.class).putExtra(flag,
                    "purchase"));
        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
