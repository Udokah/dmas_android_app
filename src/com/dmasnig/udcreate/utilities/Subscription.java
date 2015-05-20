package com.dmasnig.udcreate.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.Serializable;
import java.net.URI;

/**
 * Created by UDSWAGZ on 6/4/2014.
 */
public class Subscription implements Serializable{

    Context context ;

    public Subscription(Context c){
        this.context = c ;
    }

    Misc misc = new Misc(context);
    String KEY_ITEM = "subscription" ;
    String KEY_STATUS = "status" ;
    String KEY_TYPE = "type" ;
    ServerResponse resp = new ServerResponse();

    public void FetchAndSave(String apiKey){
        resp = fetchSubscriptionData(apiKey) ;
        if(resp.getError().equalsIgnoreCase("false")){
            saveSubscriptionData(resp.getMessage());
        }
    }


    public ServerResponse fetchSubscriptionData(String apiKey){
        URI targetUrl = UriComponentsBuilder.fromUriString(Config.webservice)
                .path(Config.SUB_INFO)
                .build()
                .toUri();
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
        body.add("Authorization", apiKey );

            HttpEntity<?> httpEntity = new HttpEntity<Object>(body);
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<ServerResponse> sub_info = restTemplate.exchange(targetUrl, HttpMethod.GET, httpEntity, ServerResponse.class);
            return sub_info.getBody() ;
    }

    public class messageData{
        public String status , type ;

        public messageData parseXMlData(String data){
            XMLParser parser = new XMLParser();
            Document doc = parser.getDomElement(data);

            NodeList nl = doc.getElementsByTagName(KEY_ITEM);
            messageData msgobj = new messageData();

            for (int i = 0; i < nl.getLength(); i++) {
                Element e = (Element) nl.item(i);
                msgobj.status = parser.getValue(e, KEY_STATUS) ;
                msgobj.type = parser.getValue(e, KEY_TYPE) ;
            }
            return msgobj ;
        }
    }

    /**
     * Save subcription data to preferences
     * @param data
     */
    public void saveSubscriptionData(String data){
        messageData datobj = new messageData();
        messageData msgObj = datobj.parseXMlData(data);
        String sub_status = msgObj.status ;
        String sub_type = msgObj.type ;
        SharedPreferences sharedpreferences = context.getSharedPreferences(Config.PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Config.prefs_sub_stats, sub_status);
        editor.putString(Config.prefs_sub_type, sub_type);
        editor.commit();
    }

    public String getSubscriptionStatus(){
        SharedPreferences sharedpreferences = context.getSharedPreferences(Config.PREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString(Config.prefs_sub_stats, "Not set");
    }

    public String getSubscriptionType(){
        SharedPreferences sharedpreferences = context.getSharedPreferences(Config.PREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString(Config.prefs_sub_type, "0");
    }



}
