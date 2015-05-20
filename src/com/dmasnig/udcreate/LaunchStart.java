package com.dmasnig.udcreate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import com.dmasnig.udcreate.utilities.FileHelper;
import com.dmasnig.udcreate.utilities.Misc;

/**
 * Created by UDSWAGZ on 6/2/2014.
 */
public class LaunchStart extends Activity {

    Context c = this ;
    Misc misc = new Misc(c);
    boolean isLoggedin ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        new BackgroundTask().execute() ;
        isLoggedin = misc.isLoggedin() ;
        if(isLoggedin){
            // re-direct to news activity
            Intent i = new Intent(c,News.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
            startActivity(i);
            finish();
        }else{
            // re-direct to StartScreen activity
            Intent i = new Intent(c,StartScreen.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
            startActivity(i);
            finish();
        }
        super.onCreate(savedInstanceState);
    }

    public class BackgroundTask extends AsyncTask<String , Integer , String> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... strings) {
                FileHelper file = new FileHelper() ;
                file.CreateAppDir();
            return null ;
        }

        @Override
        protected void onPostExecute(String s) {

        }
    }


}
