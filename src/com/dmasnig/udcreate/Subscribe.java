package com.dmasnig.udcreate;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dmasnig.udcreate.utilities.Config;
import com.dmasnig.udcreate.utilities.Misc;
import com.dmasnig.udcreate.utilities.ServerResponse;
import com.dmasnig.udcreate.utilities.Subscription;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * Created by UDSWAGZ on 5/20/2014.
 */
public class Subscribe extends ActionBarActivity{

    Context context = this ;
    Misc misc = new Misc(context);
    String SubscriptionStatus , Subscriptiontype ;
    Subscription subscription = new Subscription(context);
    String apiKey ;
    ServerResponse subscribeResp = new ServerResponse() ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_subscribe);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setCustomView(R.layout.header_bar2);
        TextView iconButton = (TextView) findViewById(R.id.header_title);
        iconButton.setText("Subscription");

        //Disable home button and make active
        ImageButton subBtn = (ImageButton) findViewById(R.id.subscribeBtn);
        subBtn.setEnabled(false);
        subBtn.setBackgroundResource(R.drawable.style_footer_menu_btn_active);
        subBtn.setImageResource(R.drawable.ic_menu_subscribe_active);
        overridePendingTransition(R.anim.fade_in , R.anim.fade_out);

        apiKey = misc.getApiKey() ; // should be done in background thread, but later

        new BackGroundTask().execute() ;

    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void navHome(View v){
        Intent intent = new Intent(this,News.class);
        startActivity(intent);
    }

    public void navMessages(View v){
        Intent intent = new Intent(this,Messages.class);
        startActivity(intent);
    }

    public void navSubscribe(View v){
        Intent intent = new Intent(this,Subscribe.class);
        startActivity(intent);
    }

    public void navSettings(View v){
        Intent intent = new Intent(this,Settings.class);
        startActivity(intent);
    }

    public void actionSubscribe(View v){
        EditText Pincode = (EditText) findViewById(R.id.PinCode);
        String pincode = Pincode.getText().toString();
        if(pincode.length() < 8){
            misc.Toast("Please enter a valid pincode");
        }else{
            // Send to webservice
            new SubscribeUser().execute(pincode);
        }
    }

    public void UpdateView(String subType, String subStat){

        TextView substat , subtype  ;

        substat = (TextView) findViewById(R.id.subscription_status) ;
        subtype = (TextView) findViewById(R.id.subscription_type);

        substat.setText( subStat );
        subtype.setText( subType );

        if(subStat.equalsIgnoreCase("Active")){
            substat.setBackgroundResource(R.drawable.style_btn_green_normal);
        }

        LinearLayout typelayout = (LinearLayout) findViewById(R.id.typeParent);
        LinearLayout subscribeParent = (LinearLayout) findViewById(R.id.subscribeParent) ;

        if( subType.equalsIgnoreCase("0")){ // if inactive
            typelayout.setVisibility(View.GONE);    // hide subscription type
            subscribeParent.setVisibility(View.VISIBLE);  // show subscribe form
            substat.setBackgroundResource(android.R.color.darker_gray);
        }else{
            typelayout.setVisibility(View.VISIBLE);
            subscribeParent.setVisibility(View.GONE);  // hide subscribe form
        }
    }


    private class SubscribeUser extends AsyncTask<String , Integer , String> {
        boolean isConnected = false ;
        ProgressDialog progress = new ProgressDialog(context);
        @Override
        protected void onPreExecute(){
            progress.setMessage("Processing....");
            progress.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            isConnected = misc.deviceIsConnected() ;
            String pinCode = strings[0];
            if(isConnected){
                /// Subscribe users via post
                URI targetUrl = UriComponentsBuilder.fromUriString(Config.webservice)
                        .path(Config.SUBSCRIBE)
                        .build()
                        .toUri();

                RestTemplate restTemplate = new RestTemplate();
                MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
                body.add("Authorization", apiKey );
                body.add("pinCode", pinCode );
                HttpEntity<?> httpEntity = new HttpEntity<Object>(body);
                try {
                    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                    ResponseEntity<ServerResponse> subscribeReq = restTemplate.exchange(targetUrl, HttpMethod.POST, httpEntity, ServerResponse.class);
                    subscribeResp = subscribeReq.getBody();
                }catch (Exception e){
                    Log.d("subscribe response",e.getMessage());
                }

                if(subscribeResp.getError() != null && subscribeResp.getError() != null ) {
                    if (subscribeResp.getError().equalsIgnoreCase("false")) {
                        subscription.saveSubscriptionData(subscribeResp.getMessage()); // update subscription data
                    }
                }

                // re-fetch parsed data
                SubscriptionStatus = subscription.getSubscriptionStatus();
                Subscriptiontype = subscription.getSubscriptionType();
            }
            return null ;
        }

        @Override
        protected void onPostExecute(String s) {
            progress.dismiss();
            if(!isConnected){
                misc.Toast(misc.getConnErr());
            }else{
                String error = subscribeResp.getError() ;
                String message = subscribeResp.getMessage() ;
                if(error.equalsIgnoreCase("false")){
                    misc.Alert("Subscription successful", misc.getSubs_success() ,android.R.drawable.ic_dialog_info);
                }else{
                    misc.Alert("Subscription failed",message,android.R.drawable.ic_dialog_alert);
                }
                // Update view
                UpdateView( Subscriptiontype,  SubscriptionStatus) ;
            }
        }
    }

    public class BackGroundTask extends AsyncTask<String , Integer , String> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... strings) {
            SubscriptionStatus = subscription.getSubscriptionStatus();
            Subscriptiontype = subscription.getSubscriptionType();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
           UpdateView( Subscriptiontype,  SubscriptionStatus) ; // update view
        }
    }

}

