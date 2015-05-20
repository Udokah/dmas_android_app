package com.dmasnig.udcreate.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import com.dmasnig.udcreate.Messages_selected;
import com.dmasnig.udcreate.R;
import com.dmasnig.udcreate.utilities.*;
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
 * Created by UDSWAGZ on 6/30/2014.
 */
public class UpdateService extends IntentService{

    public Context context ;
    ServerResponse serverResponse = new ServerResponse();
    int NotificationID = Config.NotificationID ;
    Misc misc  ;
    String ApiKey ;
    Subscription subscription ;
    ServerResponse response ;
    String newMessage ;

    public UpdateService(){
        super("UpdateService");
    }


    public void updateSubscriptionStatus(String apiKey){
        Log.d("apikey is", apiKey);
        ServerResponse responseData = subscription.fetchSubscriptionData(apiKey);
        if (responseData.getError().equalsIgnoreCase("false")) {
            subscription.saveSubscriptionData(responseData.getMessage());
            Log.d("updateSubscriptionStatus Got message", responseData.getMessage());
        }
        Log.d("updateSubscriptionStatus","no message");
    }


    public ServerResponse getTodaysMessage(String ApiKey){
        Log.d("calling webservice","calling webservice") ;
        URI targetUrl = UriComponentsBuilder.fromUriString(Config.webservice)
                .path(Config.DAILY_MESSAGE)
                .build()
                .toUri();

        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
        body.add("Authorization", ApiKey );
        HttpEntity<?> httpEntity = new HttpEntity<Object>(body);
        try {
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<ServerResponse> subscribeReq = restTemplate.exchange(targetUrl, HttpMethod.GET, httpEntity, ServerResponse.class);
            serverResponse = subscribeReq.getBody();
        }catch (Exception e){
            Log.d("webservice Error",e.getMessage());
        }
        return serverResponse;
    }

    public void SaveMessage(String message){
        FileHelper file = new FileHelper();
        Log.d("file","saving message in file");
        try{
            file.addMessage(message) ;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void ShowNotification(String theMessage){
        Log.d("Notification","creating notificatioin");
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.drawable.ic_launcher);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(uri);
        long[] pattern = {500,500,500,500,500,500,500,500,500};
        mBuilder.setVibrate(pattern);
        mBuilder.setContentTitle("Divine Mercy Daily Message");
        mBuilder.setContentText("New message for today");
        Intent resultIntent = new Intent(context, Messages_selected.class);
        resultIntent.putExtra("theMessage" , theMessage) ;
        resultIntent.putExtra("theDate" , "Just now") ;
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(Messages_selected.class);

        // Add the intent that starts the activity at the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultpendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultpendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify(NotificationID,mBuilder.build());
    }


    @Override
    protected void onHandleIntent(Intent intent){
        context = this ;
        misc = new Misc(context) ;
        String ApiKey = misc.getApiKey() ;
        subscription = new Subscription(context);

        if(misc.deviceIsConnected()){
            response = getTodaysMessage(ApiKey);
         if( response != null && response.getMessage() != null ) {
             Log.d("webservice response message" , response.getMessage());
             String error = response.getError();
             if (error.equalsIgnoreCase("false")) { // If no error
                 newMessage = response.getMessage();
                 Log.d("New message", newMessage);
                 if(!newMessage.equalsIgnoreCase("Unable to get text message")) {
                     if(newMessage.length() > 10){
                         SaveMessage(newMessage);
                         ShowNotification(newMessage);
                     }
                     // Start update account status
                     updateSubscriptionStatus(ApiKey);
                 }
             }
         }else{
             Log.d("webservice response","response was null");
         }
       }
    }

}
