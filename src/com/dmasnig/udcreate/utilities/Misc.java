package com.dmasnig.udcreate.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by UDSWAGZ on 6/2/2014.
 */
public class Misc {
    Context context ;

    public Misc(Context c){
        this.context = c ;
    }

   String connErr = "Please make sure you are connected to the internet" ;
   String subs_success = "You have been successfully subscribed to the divine mercy daily sms service" ;

   public String getSubs_success(){
       return this.subs_success ;
   }

   public String getConnErr(){
       return this.connErr ;
   }

    public boolean deviceIsConnected(){
        boolean result ;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if( activeNetworkInfo == null ){
            result = false ;
        }else{
            if(activeNetworkInfo.isConnected()){
                result = true ;
            }else{
                result = true ;
            }
        }
        return result ;
    }

    public void saveApiKey(String apikey){
        SharedPreferences sharedpreferences = context.getSharedPreferences(Config.PREFERENCES, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Config.prefs_ApiKey, apikey);
        editor.commit();
    }

    public String getApiKey(){
        SharedPreferences sharedpreferences = context.getSharedPreferences(Config.PREFERENCES, context.MODE_PRIVATE);
        return sharedpreferences.getString(Config.prefs_ApiKey, "0");
    }

    public void Toast(String S){
        Toast.makeText(context, S, Toast.LENGTH_LONG).show();
    }

    public void Alert(String title, String message, int icon) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setCancelable(true)
                   .setTitle(title)
                   .setMessage(message)
                   .setIcon(icon)
                   .setNegativeButton("close", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {
                           dialogInterface.cancel();
                       }
                   });
        AlertDialog alert = alertDialog.create();
        alert.show();
    }


    public void setLoggedInStatus(boolean status){
        SharedPreferences sharedpreferences = context.getSharedPreferences(Config.PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(Config.prefs_LogInStatus, status);
        editor.commit();
    }

    public boolean isLoggedin(){
        SharedPreferences sharedpreferences = context.getSharedPreferences(Config.PREFERENCES, Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedpreferences.getBoolean(Config.prefs_LogInStatus, false);
        return isLoggedIn;
    }




}
