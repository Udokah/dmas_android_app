package com.dmasnig.udcreate;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import com.dmasnig.udcreate.utilities.Config;
import com.dmasnig.udcreate.utilities.Misc;
import com.dmasnig.udcreate.utilities.ServerResponse;
import com.dmasnig.udcreate.utilities.Subscription;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class Userlogin extends ActionBarActivity{

    Context context = Userlogin.this ;
    String email, password ;
    ServerResponse response = new ServerResponse();
    Misc misc = new Misc(context) ;
    boolean isConnected ;
    String apikey ;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_userlogin);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Login");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void navRecovery(View v){
        Intent activity = new Intent(this,Recovery.class);
        startActivity(activity);
    }

    public void actionLogin(View v){
        boolean isEmpty = false ;
        String errMsg = null ;

        EditText formEmail = (EditText) findViewById(R.id.email) ;
        EditText formPassword = (EditText) findViewById(R.id.password) ;

        email = formEmail.getText().toString() ;
        password = formPassword.getText().toString() ;

        if( email.length() < 3 ){
            errMsg = "Enter a valid email address" ;
            isEmpty = true ;
        }else if( password.length() < 3 ){
            errMsg = "enter your password" ;
            isEmpty = true ;
        }

        if(isEmpty){
            misc.Toast(errMsg);
        }else{
            new CheckInternet().execute() ;
            if(isConnected){
                new BackgroundTask().execute();
            }else{
                misc.Alert("connection error",misc.getConnErr(),android.R.drawable.ic_dialog_alert);
            }
        }
    }

    private void AuthLogin(){
    URI targetUrl = UriComponentsBuilder.fromUriString(Config.webservice)
                .path(Config.LOGIN)
                .queryParam("email", email)
                .queryParam("password", password)
                .build()
                .toUri();
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            response = restTemplate.getForObject(targetUrl, ServerResponse.class);
    }

    private class BackgroundTask extends AsyncTask<String , Integer , String> {
        ProgressDialog progress = new ProgressDialog(context);
        Subscription subscriber = new Subscription(context);

        @Override
        protected void onPreExecute(){
            progress.setMessage("Authenticating.....");
            progress.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            AuthLogin() ;
            /// If Login is good, save api key
            if(response.getError().equalsIgnoreCase("false")) {
                apikey = response.getMessage() ;
                misc.saveApiKey(apikey);
                subscriber.FetchAndSave(apikey); // Fetch ans save subscription status
            }
            return null ;
        }

        @Override
        protected void onPostExecute(String s){
            progress.dismiss();
            if(response.getError().equalsIgnoreCase("false")){
                misc.Toast("Login successful" ) ;
                misc.setLoggedInStatus(true) ;
                // redirect to news activity
                Intent i = new Intent(context,News.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
                startActivity(i);
                finish();
            }else{
                misc.Alert("Connectivity error",response.getMessage(),android.R.drawable.ic_dialog_alert);
            }

        }

    }

    private class CheckInternet extends AsyncTask<String , Integer , String> {
        @Override
        protected void onPreExecute(){
        }

        @Override
        protected String doInBackground(String... strings) {
            isConnected = misc.deviceIsConnected() ;
            return null ;
        }

        @Override
        protected void onPostExecute(String s) {
        }
    }

}
