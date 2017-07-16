package com.nostra13.universalimageloader.sample.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.artifex.mupdfdemo.R;
import com.artifex.utils.ConstantValues;
import com.ikimuhendis.ldrawer.DrawerArrowDrawable;
import com.nostra13.universalimageloader.sample.fragment.CategoryFragment;
import com.nostra13.universalimageloader.sample.fragment.ProfileFragment;
import com.nostra13.universalimageloader.sample.fragment.QuizViewFragment;
import com.nostra13.universalimageloader.sample.fragment.WebViewFragment;

import java.util.HashMap;
import java.util.List;

//import com.mikepenz.materialize.MaterializeBuilder;

public class DrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ProfileFragment.OnFragmentInteractionListener {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;
    private DrawerArrowDrawable drawerArrow;
    FragmentManager mFragmentManager;
    private boolean drawerArrowColor;
    TextView username;
    TextView password;
    TextView lastname;
    TextView phone;
    TextView email;
    TextView path;
    TextView demo;
    String baseurl;
    TextView name;
    boolean finish;
    private List<String> myData;
    ExpandableListAdapter mMenuAdapter;
    ExpandableListView expandableList;
    List<ExpandedMenuModel> listDataHeader;
    HashMap<ExpandedMenuModel, List<String>> listDataChild;
    TextView email1;
    Fragment fr = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();

        if (savedInstanceState == null) {

            Bundle b = new Bundle();
            if(ConstantValues.offlineDemoFlag){
                b.putString("url","\n" +"file:///android_asset/coep_aboutus.html");
            }else
            b.putString("url","\n" +
                    ConstantValues.baseDomainUrl + "/aboutusstudentandroid");
            WebViewFragment wbf = new WebViewFragment();
            wbf.setArguments(b);
            tx.replace(R.id.fragmentid, wbf);
            tx.commit();

        } else {

        }

        setContentView(R.layout.activity_drawer);


        //      new MaterializeBuilder().withActivity(this).build();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //mDrawerList = (ListView) findViewById(R.id.navdrawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        baseurl = ConstantValues.fetchDataUrl + "/";

        drawerArrow = new DrawerArrowDrawable(this) {
            @Override
            public boolean isLayoutRtl() {
                return false;
            }
        };

        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        ////////////////////////////////////////////////////////////////////////////////////////////////////


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View header = navigationView.getHeaderView(0);
        name = (TextView) header.findViewById(R.id.name22);
        email = (TextView) header.findViewById(R.id.textViewemail);
        name.setText(getIntent().getStringExtra("username"));
        email.setText(getIntent().getStringExtra("email"));

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
        FragmentManager fm = getSupportFragmentManager();

        Fragment fr = null;
        String tag = null;

        tag = CategoryFragment.class.getSimpleName();
        fr = getSupportFragmentManager().findFragmentByTag(tag);
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Bundle b = new Bundle();

        if (id == R.id.nav_book) {

            tag = CategoryFragment.class.getSimpleName();
            fr = getSupportFragmentManager().findFragmentByTag(tag);
            // Bundle b = new Bundle();
            b.putString("url", baseurl);
            b.putString("url_category", baseurl);
            b.putString("class", getIntent().getStringExtra("path"));
            b.putString("servleturl", "");
            b.putString("location", "documents");
            if (fr == null) {
                fr = new CategoryFragment();
                fr.setArguments(getIntent().getExtras());
                fr.setArguments(b);

            }

        } else if (id == R.id.nav_clg_gallery) {

            tag = CategoryFragment.class.getSimpleName();
            fr = getSupportFragmentManager().findFragmentByTag(tag);
            // Bundle b = new Bundle();
            b.putString("url", baseurl);
            b.putString("class", getIntent().getStringExtra("path"));
            b.putString("location", "images");
            if (fr == null) {
                fr = new CategoryFragment();
                fr.setArguments(getIntent().getExtras());
                fr.setArguments(b);
            }



        } else if (id == R.id.nav_videolectures) {

            tag = CategoryFragment.class.getSimpleName();
            fr = getSupportFragmentManager().findFragmentByTag(tag);
            // Bundle b = new Bundle();
            //b.putString("url","http://192.168.1.73:8080/SignInProject/");
            b.putString("url", baseurl);
            b.putString("class", getIntent().getStringExtra("path"));
            b.putString("location", "videos");
            if (fr == null) {
                fr = new CategoryFragment();
                fr.setArguments(getIntent().getExtras());
                fr.setArguments(b);
            }

        } else if (id == R.id.nav_audio_lectures) {

            tag = CategoryFragment.class.getSimpleName();
            fr = getSupportFragmentManager().findFragmentByTag(tag);
            // Bundle b = new Bundle();
            b.putString("url", baseurl);
            b.putString("class", getIntent().getStringExtra("path"));
            b.putString("location", "music");
            if (fr == null) {
                fr = new CategoryFragment();
                fr.setArguments(getIntent().getExtras());
                fr.setArguments(b);
            }


        } else if (id == R.id.nav_aboutColg) {
            tag = WebViewFragment.class.getSimpleName();
            fr = getSupportFragmentManager().findFragmentByTag(tag);
            // Bundle b = new Bundle();

            if(ConstantValues.offlineDemoFlag){
                b.putString("url","\n" +"file:///android_asset/coep_aboutus.html");
            }else{
                b.putString("url", "\n" +
                    ConstantValues.baseDomainUrl + "/aboutusstudentandroid");
            }
            // b.putString("class",getIntent().getStringExtra("path"));
            //b.putString("location","music");
            if (fr == null) {
                fr = new WebViewFragment();
                fr.setArguments(getIntent().getExtras());
                fr.setArguments(b);
            }

        } else if (id == R.id.logout) {
            finish = true;

            tag = CategoryFragment.class.getSimpleName();
            fr = getSupportFragmentManager().findFragmentByTag(tag);
            // Bundle b = new Bundle();
            b.putString("url", baseurl);
            b.putString("class", getIntent().getStringExtra("path"));
            b.putString("location", "images");
            if (fr == null) {
                fr = new CategoryFragment();
                fr.setArguments(getIntent().getExtras());
                fr.setArguments(b);
            }

        } else if (id == R.id.nav_quiz) {

            tag = QuizViewFragment.class.getSimpleName();
            fr = getSupportFragmentManager().findFragmentByTag(tag);

            if (fr == null) {
                fr = new QuizViewFragment();
                fr.setArguments(getIntent().getExtras());
                //fr.setArguments(b);
            }
        } else if (id == R.id.nav_eresources) {
            tag = WebViewFragment.class.getSimpleName();
            fr = getSupportFragmentManager().findFragmentByTag(tag);
            b.putString("url", "http://www.academicjournals.org/");
            if (fr == null) {
                fr = new WebViewFragment();
                fr.setArguments(getIntent().getExtras());
                fr.setArguments(b);
            }

        } else if (id == R.id.nav_jobOpening) {
            tag = WebViewFragment.class.getSimpleName();
            fr = getSupportFragmentManager().findFragmentByTag(tag);
            b.putString("url", "http://alljobopenings.in/");
            if (fr == null) {
                fr = new WebViewFragment();
                fr.setArguments(getIntent().getExtras());
                fr.setArguments(b);
            }

        } else if (id == R.id.nav_emagzine) {
            // startActivity(new Intent(DrawerActivity.this,ChoosePDFActivity.class));
            tag = WebViewFragment.class.getSimpleName();
            fr = getSupportFragmentManager().findFragmentByTag(tag);
            b.putString("url", "http://learnerstv.tradepub.com/");
            if (fr == null) {
                fr = new WebViewFragment();
                fr.setArguments(getIntent().getExtras());
                fr.setArguments(b);
            }

        } else if (id == R.id.nav_department) {
            tag = WebViewFragment.class.getSimpleName();
            fr = getSupportFragmentManager().findFragmentByTag(tag);
            if(ConstantValues.offlineDemoFlag){
                b.putString("url","\n" +"file:///android_asset/departments.html");
            }else
            b.putString("url", "https://www.google.co.in/search?safe=strict&q=akshar+science+academy+pune+maharashtra+address&oq=akshar+science+academy+pune+maharashtra+ad&gs_l=serp.3.0.33i21k1l2j33i160k1.66471.67690.0.69441.3.3.0.0.0.0.230.601.0j1j2.3.0....0...1.1.64.serp..0.2.370.oU7q-W1RPAg");
            if (fr == null) {
                fr = new WebViewFragment();
                fr.setArguments(getIntent().getExtras());
                fr.setArguments(b);
            }
        } else if (id == R.id.nav_aboutColg) {

            tag = WebViewFragment.class.getSimpleName();
            fr = getSupportFragmentManager().findFragmentByTag(tag);

            if(ConstantValues.offlineDemoFlag){
                b.putString("url","\n" +"file:///android_asset/coep_aboutus.html");
            }else
            b.putString("url", "https://www.facebook.com/Akshar-Science-Academy-1013269658716722/");
            if (fr == null) {
                fr = new WebViewFragment();
                fr.setArguments(getIntent().getExtras());
                fr.setArguments(b);
            }
            //startActivity(new Intent(DrawerActivity.this, WebViewActivityUrlLoad.class).putExtra("url","http://www.coep.org.in/about"));
        } else if (id == R.id.nav_academics) {
            tag = WebViewFragment.class.getSimpleName();
            fr = getSupportFragmentManager().findFragmentByTag(tag);
            if(ConstantValues.offlineDemoFlag){
                b.putString("url","\n" +"file:///android_asset/academics.html");
            }else
            b.putString("url", "http://www.coep.org.in/academics");
            if (fr == null) {
                fr = new WebViewFragment();
                fr.setArguments(getIntent().getExtras());
                fr.setArguments(b);
            }
            //    startActivity(new Intent(DrawerActivity.this, WebViewActivityUrlLoad.class).putExtra("url","http://www.coep.org.in/academics"));
        } else if (id == R.id.nav_placementcell) {
            tag = WebViewFragment.class.getSimpleName();
            fr = getSupportFragmentManager().findFragmentByTag(tag);
            if(ConstantValues.offlineDemoFlag){
                b.putString("url","\n" +"file:///android_asset/placement.html");
            }else
            b.putString("url", "http://www.coep.org.in/placementcell");
            if (fr == null) {
                fr = new WebViewFragment();
                fr.setArguments(getIntent().getExtras());
                fr.setArguments(b);
            }
//            startActivity(new Intent(DrawerActivity.this, WebViewActivityUrlLoad.class).putExtra("url","http://www.coep.org.in/placementcell"));
        } else if (id == R.id.nav_wikisearch) {
            tag = WebViewFragment.class.getSimpleName();
            fr = getSupportFragmentManager().findFragmentByTag(tag);
            b.putString("url", "https://en.wikipedia.org/w/index.php?search=&title=Special%3ASearch&profile=default&fulltext=1");
            if (fr == null) {
                fr = new WebViewFragment();
                fr.setArguments(getIntent().getExtras());
                fr.setArguments(b);
            }
            //          startActivity(new Intent(DrawerActivity.this, WebViewActivityUrlLoad.class).putExtra("url","https://en.wikipedia.org/w/index.php?search=&title=Special%3ASearch&go=Go"));
        } else if (id == R.id.nav_newspaper) {
            tag = WebViewFragment.class.getSimpleName();
            fr = getSupportFragmentManager().findFragmentByTag(tag);
            b.putString("url", "http://www.onlinenewspapers.com/");
            if (fr == null) {
                fr = new WebViewFragment();
                fr.setArguments(getIntent().getExtras());
                fr.setArguments(b);
            }
            //startActivity(new Intent(DrawerActivity.this, WebViewActivityUrlLoad.class).putExtra("url","http://www.onlinenewspapers.com/"));
        } else if (id == R.id.nav_interviewMaterial) {

            tag = WebViewFragment.class.getSimpleName();
            fr = getSupportFragmentManager().findFragmentByTag(tag);
            b.putString("url", "http://news.efinancialcareers.com/uk-en/213388/70-top-banking-interview-questions-and-how-to-answer-them");
            if (fr == null) {
                fr = new WebViewFragment();
                fr.setArguments(getIntent().getExtras());
                fr.setArguments(b);
            }
            //startActivity(new Intent(DrawerActivity.this, WebViewActivityUrlLoad.class).putExtra("url","http://www.onlinenewspapers.com/"));
        } else if (id == R.id.nav_share) {

            ShareCompat.IntentBuilder
                    .from(this) // getActivity() or activity field if within Fragment
                    .setText("Must try this application")
                    .setType("text/plain") // most general text sharing MIME type
                    .setChooserTitle(getResources().getString(R.string.shareTitle))
                    .startChooser();
            tag = WebViewFragment.class.getSimpleName();
            fr = getSupportFragmentManager().findFragmentByTag(tag);
            b.putString("url","\n" +
                    ConstantValues.baseDomainUrl + "/aboutusstudentandroid");

            if (fr == null) {//
                fr = new WebViewFragment();
                fr.setArguments(getIntent().getExtras());
                fr.setArguments(b);
            }

        }
       /* //FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragmentid,fr).addToBackStack("test").commit();
        getSupportFragmentManager().executePendingTransactions();
*/
        if (finish) {
            ConstantValues.loginSessionFlag = false;
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
           // fm.beginTransaction().replace(R.id.fragmentid, fr).addToBackStack("test").commit();

        } else {
            fm.beginTransaction().replace(R.id.fragmentid, fr).addToBackStack("test").commit();
            getSupportFragmentManager().executePendingTransactions();
//        getSupportFragmentManager().beginTransaction().addToBackStack("test");

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }


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


    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBackPressed() {
        getSupportFragmentManager().getBackStackEntryCount();
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {

            boolean done = getSupportFragmentManager().popBackStackImmediate();
        }
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putSerializable("list", (Serializable) myData);
    }


}