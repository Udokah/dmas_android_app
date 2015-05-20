package com.dmasnig.udcreate;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;
import com.dmasnig.udcreate.services.MyStartServiceReceiver;
import com.dmasnig.udcreate.utilities.Config;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by UDSWAGZ on 5/14/2014.
 */
public class News extends ActionBarActivity{

    Context context = News.this ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_news);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setCustomView(R.layout.header_bar2);
        TextView iconButton = (TextView) findViewById(R.id.header_title);
        iconButton.setText("News Feed");

        //Disable home button and make active
        ImageButton homebtn = (ImageButton) findViewById(R.id.homeBtn);
        homebtn.setEnabled(false);
        homebtn.setBackgroundResource(R.drawable.style_footer_menu_btn_active);
        homebtn.setImageResource(R.drawable.ic_menu_home_active);

        /// Set Recurring Alarm For Daily Messages
        SetAlarm();
        ///////////////////////////////////////////////


        overridePendingTransition(R.anim.fade_in , R.anim.fade_out);

        WebView webView = (WebView) findViewById(R.id.WebView1);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl("http://dmas-nig.com/news/?cat=9");
    }

    public void SetAlarm(){

       /* long firstMillis = System.currentTimeMillis(); // first run of alarm is immediate
        int intervalMillis = 10000; // 5 seconds */

       Calendar updateTime = Calendar.getInstance();
        updateTime.setTimeZone(TimeZone.getDefault());
        updateTime.set(Calendar.HOUR_OF_DAY, 14);
        updateTime.set(Calendar.MINUTE, 55);
        updateTime.set(Calendar.SECOND, 0);
        updateTime.set(Calendar.MILLISECOND, 0);

        Intent intent = new Intent(context , MyStartServiceReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,MyStartServiceReceiver.REQUEST_CODE,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarms = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarms.setInexactRepeating(AlarmManager.RTC_WAKEUP, updateTime.getTimeInMillis() , 24*60*60*1000 , pendingIntent);
       // alarms.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis ,intervalMillis , pendingIntent);
    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        new AlertDialog.Builder(this)
                .setTitle("Exit app")
                .setMessage("sure you want to exit ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void goBack(){
        super.onBackPressed();
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


    ///////////// END OF VIEW CODES /////////////////////////////////////////////////////

}
