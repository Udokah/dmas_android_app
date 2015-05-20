package com.dmasnig.udcreate;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.dmasnig.udcreate.R;

public class StartScreen extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_startscreen);
    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        new AlertDialog.Builder(this)
                .setTitle("Exit app")
                .setMessage("sure you want to exit ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        goBack();
                        finish();
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

    public void navSignup(View v){
        Intent activity = new Intent(this, Signup.class);
        startActivity(activity);
    }

    public void navLogin(View v){
        Intent activity = new Intent(this,Userlogin.class);
        startActivity(activity);
    }
}
