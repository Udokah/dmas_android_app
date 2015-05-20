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
import android.widget.Spinner;
import com.dmasnig.udcreate.utilities.Config;
import com.dmasnig.udcreate.utilities.Misc;
import com.dmasnig.udcreate.utilities.ServerResponse;
import com.dmasnig.udcreate.utilities.Subscription;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class Signup extends ActionBarActivity{

    EditText firstname , lastname , Temail , Tpassword , Tphone ;
    Spinner callcode , usercountry ;
    String name , email , phone , password , country ;
    Context context = Signup.this ;
    ServerResponse response = new ServerResponse();
    Misc misc = new Misc(context) ;
    boolean isConnected ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_signup);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Sign up");
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

    public void navBack(View v){
        super.onBackPressed();
    }

    public void doSignup(View v){
        /// check for empty fields
        boolean isEmpty = false ;
        String errMsg = null ;

        firstname = (EditText) findViewById(R.id.firstname);
        lastname = (EditText) findViewById(R.id.lastname) ;
        Temail = (EditText) findViewById(R.id.email);
        callcode = (Spinner) findViewById(R.id.code) ;
        Tphone = (EditText) findViewById(R.id.phone);
        Tpassword = (EditText) findViewById(R.id.password);
        usercountry = (Spinner) findViewById(R.id.country) ;

        name = firstname.getText().toString() + " " + lastname.getText().toString() ;
        email = Temail.getText().toString() ;
        phone = callcode.getSelectedItem().toString() + Tphone.getText().toString() ;
        password = Tpassword.getText().toString() ;
        country = usercountry.getSelectedItem().toString() ;

        if( name.length() < 4 ){
            errMsg = "Please enter a valid name" ;
            isEmpty = true ;
        }else if( email.length() < 4 ){
            errMsg = "Please enter your email";
            isEmpty = true ;
        }else if( phone.length() < 6 ){
            errMsg = "Please enter your phone number";
            isEmpty = true ;
        }else if( password.length() < 5 ){
            errMsg = "choose a password not less than 6 characters";
            isEmpty = true ;
        }else if( country.equalsIgnoreCase("choose country") ){
            errMsg = "Please choose a country";
            isEmpty = true ;
        }

        if(isEmpty) {
            misc.Toast(errMsg) ;
        }else {
            new CheckInternet().execute() ; // check if device is connected
            if(isConnected){
                new BackgroundTask().execute();
            }else{
                misc.Alert("Connection error",misc.getConnErr(),android.R.drawable.ic_dialog_alert);
            }
        }

    }

    public String SignupUser(){
            URI targetUrl = UriComponentsBuilder.fromUriString(Config.webservice)
                    .path(Config.REGISTER)
                    .queryParam("name", name)
                    .queryParam("email", email)
                    .queryParam("phone", phone)
                    .queryParam("password", password)
                    .queryParam("country", country)
                    .build()
                    .toUri();
        RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                response = restTemplate.getForObject(targetUrl, ServerResponse.class);
        return null ;
    }

    private class BackgroundTask extends AsyncTask<String , Integer , String> {
        ProgressDialog progress = new ProgressDialog(context);
        Subscription subscriber = new Subscription(context);

        @Override
        protected void onPreExecute(){
            progress.setMessage("working.....");
            progress.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            SignupUser() ;
            /// If signup is successfull, save api key
            if(response.getError().equalsIgnoreCase("false")) {
                misc.saveApiKey(response.getMessage());
                subscriber.FetchAndSave(response.getMessage()); // Fetch ans save subscription status
            }
            return null ;
        }

        @Override
        protected void onPostExecute(String s) {
            progress.dismiss();
            if(response.getError().equalsIgnoreCase("false")){
                misc.Toast("Account creation successful") ;
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
