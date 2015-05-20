package com.dmasnig.udcreate;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import com.dmasnig.udcreate.R;

/**
 * Created by UDSWAGZ on 6/27/2014.
 */
public class About extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_about);
    }

    public void OpenDeveloperSite(View v){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.udonline.net/"));
        startActivity(intent);
    }



}
