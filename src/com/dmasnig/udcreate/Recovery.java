package com.dmasnig.udcreate;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import com.dmasnig.udcreate.R;

/**
 * Created by UDSWAGZ on 5/14/2014.
 */
public class Recovery extends ActionBarActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_recovery);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Password recovery");
    }

    public void navBack(View v){
        super.onBackPressed();
    }
}
