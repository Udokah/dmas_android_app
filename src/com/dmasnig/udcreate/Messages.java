package com.dmasnig.udcreate;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.dmasnig.udcreate.utilities.Config;
import com.dmasnig.udcreate.utilities.FileHelper;
import com.dmasnig.udcreate.utilities.XMLParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by UDSWAGZ on 5/15/2014.
 */
public class Messages extends ActionBarActivity {

    public String MESSAGEDATA = null ;
    // XML node keys
    static final String KEY_ITEM = "log"; // parent node
    static final String KEY_DATE = "date";
    static final String KEY_MESSAGE = "message";
    Context context = Messages.this ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_messages);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setCustomView(R.layout.header_bar2);
        TextView iconButton = (TextView) findViewById(R.id.header_title);
        iconButton.setText("Daily Messages");

        //Disable main button and make active
        ImageButton mesgbtn = (ImageButton) findViewById(R.id.messagesBtn);
        mesgbtn.setEnabled(false);
        mesgbtn.setBackgroundResource(R.drawable.style_footer_menu_btn_active);
        mesgbtn.setImageResource(R.drawable.ic_menu_message_active);
        overridePendingTransition(R.anim.fade_in , R.anim.fade_out);
        clearNotifications();

        new LoadMessages().execute() ;
    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in , R.anim.fade_out);
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

    public void clearNotifications(){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(Config.NotificationID);
    }

    //////// LIST VIEW HANDLING //////////////////////////////////

    public class MyCustomAdapter extends BaseAdapter {
        messageData messagedata = new messageData();
        List<messageData> messageList = messagedata.getMessages();

        boolean i = true ;

        @Override
        public int getCount() {
            return messageList.size();
        }

        @Override
        public messageData getItem(int i) {
            return messageList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {

            if (arg1 == null) {
                LayoutInflater inflater = (LayoutInflater) Messages.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                arg1 = inflater.inflate(R.layout.widget_message_list, arg2, false);
            }

            messageData usermessages = messageList.get(arg0);

            TextView messageText = (TextView) arg1.findViewById(R.id.message);
            TextView timeText = (TextView) arg1.findViewById(R.id.time);
            LinearLayout layout = (LinearLayout) arg1.findViewById(R.id.ListViewParent) ;
            String substr = usermessages.message ;

            if( substr.length() > 20 ){
                substr = substr.substring(0, 20) + "..." ;
            }

            messageText.setText(substr);
            timeText.setText(usermessages.time);

            if( i ){
                layout.setBackgroundResource(R.drawable.widget_messages_list_white);
                i = false ;
            }else{
                layout.setBackgroundResource(R.drawable.widget_messages_list_blue);
                i = true ;
            }

            return arg1;
        }

        public messageData getMessageList(int position) {
            return messageList.get(position);
        }

    }

     public class messageData{
        public String message , time ;

        public List<messageData> getMessages(){
            List<messageData> messageList = new ArrayList<messageData>();
            XMLParser parser = new XMLParser();
            Document doc = parser.getDomElement(MESSAGEDATA); // getting DOM element

            NodeList nl = doc.getElementsByTagName(KEY_ITEM);
            // looping through all item nodes <item>
            int arrLen = nl.getLength() ;
            for (int i = (arrLen-1); i >= 0; i--) {
                // creating new user msg instance
                messageData msgobj = new messageData();
                Element e = (Element) nl.item(i);
                msgobj.time = parser.getValue(e, KEY_DATE) ;
                msgobj.message = parser.getValue(e, KEY_MESSAGE) ;
                // adding msg object to ArrayList
                messageList.add(msgobj);
            }

            return messageList ;
        }

    }

    public class LoadMessages extends AsyncTask<String , Integer , String> {
        ProgressDialog progress = new ProgressDialog(Messages.this);

        @Override
        protected void onPreExecute() {
            progress.setMessage("Loading messages");
            progress.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            FileHelper MsgFile = new FileHelper();
            try {
                MESSAGEDATA = MsgFile.ReadFromMsgFile();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null ;
        }

        @Override
        protected void onPostExecute(String s) {
            progress.dismiss();
            MyCustomAdapter myCustomAdapter = new MyCustomAdapter();
            ListView MyListView = (ListView) findViewById(android.R.id.list);
            MyListView.setAdapter(myCustomAdapter);

            // Set Onclick listener
            MyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    MyCustomAdapter myAdapter = new MyCustomAdapter();
                    messageData selectedMessage = myAdapter.getMessageList(i);
                    String theMessage = selectedMessage.message;
                    String theDate = selectedMessage.time;
                    Intent intent = new Intent(context,Messages_selected.class);
                    intent.putExtra("theMessage" , theMessage) ;
                    intent.putExtra("theDate" , theDate) ;
                    startActivity(intent);
                    finish();
                }
            });
        }
    }
    //////// END OF LIST VIEW HANDLING ///////////////////////////


}
