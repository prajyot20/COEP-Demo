package com.nostra13.universalimageloader.sample.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.artifex.mupdfdemo.R;
import com.artifex.utils.ConstantValues;
import com.artifex.utils.FCMIDService;
import com.artifex.utils.FCMTestService;
import com.nostra13.universalimageloader.sample.WebviewActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {

    // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private EditText etEmail;
    private EditText etPassword;
    JSONObject jsonObj;
    String username;
    String password;
    String lastname;
    String phone;
    String email;
    String path;

    // Creating JSON Parser object
    // JSONParser jParser = new JSONParser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       /* // Notification Service start
        Intent intent = new Intent(this, FCMTestService.class);
        startService(intent);
        intent = new Intent(this, FCMIDService.class);
        startService(intent);
*/

       if(ConstantValues.loginSessionFlag){
          startActivity(new Intent(MainActivity.this,DrawerActivity.class));
           MainActivity.this.finish();
       }


        boolean hasPermission = (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE ) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }


        // Get Reference to variables
        etEmail = (EditText) findViewById(R.id.email1);
        etPassword = (EditText) findViewById(R.id.password1);

    }

    public void forgotPassword(View arg0) {
        Intent i = new Intent(this, WebviewActivity.class);
        i.putExtra("url", "\n" +
                ConstantValues.baseDomainUrl + "/forgetpasswordjspandroid.jsp");
        startActivity(i);
        //finish();
    }

    public void signup(View arg0) {
        Intent i = new Intent(this, WebviewActivity.class);
        i.putExtra("url", "\n" +
                ConstantValues.baseDomainUrl + "/StudentRegistrationAndroid");
        startActivity(i);
        //finish();
    }

    // Triggers when LOGIN Button clicked
    public void checkLogin(View arg0) {

        // Get text from email and passord field
        final String email = etEmail.getText().toString();
        final String password = etPassword.getText().toString();
        Log.d("offline test","MainActivity fraG"+" user :"+email+" password : "+password);

        // Initialize  AsyncLogin() class with email and password
        if(email.contains("demo") && password.contains("offline")){
            Intent intent = new Intent(MainActivity.this, DrawerActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("password", password);
            intent.putExtra("lastname", lastname);
            intent.putExtra("phone", phone);
            intent.putExtra("email", email);
            intent.putExtra("path", path);
            startActivity(intent);
            ConstantValues.loginSessionFlag = true;
            ConstantValues.offlineDemoFlag = true;
            MainActivity.this.finish();
        }else{
        new AsyncLogin().execute(email, password);
        }

    }

    private class AsyncLogin extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);
        HttpURLConnection conn;
        URL url = null;
        CookieManager msCookieManager = new CookieManager();

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
                //url = new URL("http://primeclass.biz/tution/login.inc.php");

                url = new URL("\n" +
                        ConstantValues.baseDomainUrl + "/loginandroid");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                //conn.setReadTimeout(READ_TIMEOUT);
                //conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");
                //cookie code


                conn.setRequestProperty("Cookie", "cookieName=__test; domain=www.chronbyte.com");
                //cookie code
                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);


                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("email", params[0])
                        .appendQueryParameter("password", params[1]);
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

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    //cookie code
                    Map<String, List<String>> headerFields = conn.getHeaderFields();
                    List<String> cookiesHeader = headerFields.get("Set-Cookie");
                    if (cookiesHeader != null) {
                        String cookie = cookiesHeader.get(0);
                        HttpCookie httpCookie = HttpCookie.parse(cookie).get(0);
                        String name = httpCookie.getName();
                        String value = httpCookie.getValue();
                        String domain = httpCookie.getDomain();
                    }
                    //cookie code

                    // Read data sent from server
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

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            pdLoading.dismiss();

            try {
                jsonObj = new JSONObject(result);
                showList();

            } catch (JSONException e) {
                e.printStackTrace();
            }


//            if(result.equalsIgnoreCase("true"))
//            {
//                /* Here launching another activity when login successful. If you persist login state
//                use sharedPreferences of Android. and logout button to clear sharedPreferences.
//                 */
//
//                showList();
//
//                Intent intent = new Intent(MainActivity.this,DrawerActivity.class);
//                startActivity(intent);
//                MainActivity.this.finish();
//
//            }else if (result.equalsIgnoreCase("false")){
//
//                // If username and password does not match display a error message
//                Toast.makeText(MainActivity.this, "Invalid email or password", Toast.LENGTH_LONG).show();
//
//            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {
//
//                Toast.makeText(MainActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
//
//            }
        }

    }

    private void showList() {
        JSONObject user = null;
        try {

            if (jsonObj.getInt("success") == 1) {
                user = jsonObj.getJSONObject("product");
                //for (int i = 0; i < user.length(); i++) {
                //JSONObject c = user.getJSONObject(i);
                username = user.getString("username");
                password = user.getString("password");
                lastname = user.getString("lastname");
                phone = user.getString("phone");
                email = user.getString("email");
                path = user.getString("path");
//                }

                Intent intent = new Intent(MainActivity.this, DrawerActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("password", password);
                intent.putExtra("lastname", lastname);
                intent.putExtra("phone", phone);
                intent.putExtra("email", email);
                intent.putExtra("path", path);
                startActivity(intent);

                ConstantValues.loginSessionFlag = true;
                MainActivity.this.finish();
            } else {
                Toast.makeText(MainActivity.this, "Invalid email or password", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        super.onBackPressed();
    }
}
