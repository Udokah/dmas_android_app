package com.dmasnig.udcreate;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.TextView;
import com.dmasnig.udcreate.utilities.Config;

/**
 * Created by UDSWAGZ on 6/3/2014.
 */
public class Messages_selected extends ActionBarActivity{
    Context context = this ;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_messages_selected);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Read Message");

        String theMessage = getIntent().getExtras().getString("theMessage") ;
        String theDate = getIntent().getExtras().getString("theDate") ;

        TextView Msg = (TextView) findViewById(R.id.message) ;
        TextView date = (TextView) findViewById(R.id.date) ;

        Msg.setText(theMessage);
        date.setText(theDate);
        clearNotifications();
    }

    public void clearNotifications(){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(Config.NotificationID);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(this,Messages.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,Messages.class);
        startActivity(intent);
        finish();
    }

}
