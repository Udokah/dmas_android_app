package com.dmasnig.udcreate.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.dmasnig.udcreate.utilities.Misc;

/**
 * Created by UDSWAGZ on 6/30/2014.
 */
public class MyStartServiceReceiver extends BroadcastReceiver {

    public static final int REQUEST_CODE = 81992 ;
    public static final String ACTION = "com.dmasnig.udcreate.services.UpdateService" ;

    @Override
    public void onReceive(Context context , Intent intent){
        Intent service = new Intent(context,UpdateService.class);
        Misc misc = new Misc(context) ;
        String ApiKey = misc.getApiKey() ;
        service.putExtra("ApiKey",ApiKey);
        context.startService(service);
    }

}
