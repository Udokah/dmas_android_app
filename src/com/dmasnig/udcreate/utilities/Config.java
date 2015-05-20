package com.dmasnig.udcreate.utilities;

import android.app.Activity;

/**
 * Created by UDSWAGZ on 5/30/2014.
 */
public class Config extends Activity{
    public static final String server = "http://dmas-nig.com" ;
    public static final String webservice = server + "/engine/webservice/v1" ;
    public static final String REGISTER = "/register" ;
    public static final String LOGIN =  "/login" ;
    public static final String SUB_INFO = "/subscription" ;
    public static final String SUBSCRIBE = "/subscribe" ;
    public static final String DAILY_MESSAGE = "/daily";

    public static final String PREFERENCES = "MyPrefs" ;
    public static final String prefs_LogInStatus = "LogKey" ;
    public static final String prefs_ApiKey = "ApiKey" ;
    public static final String prefs_sub_stats = "Subscription_Status" ;
    public static final String prefs_sub_type = "Subscription_Type" ;
    public static final String prefs_last_sub_call = "Last_Sub_check" ;

    public static final String APPFOLDER = "/DMAS" ;
    public static final String msgs_file = "/dmasMSG.ud" ;

    public static final int NotificationID = 199208 ;
}
