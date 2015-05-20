package com.dmasnig.udcreate;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.dmasnig.udcreate.utilities.Misc;
import com.dmasnig.udcreate.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by UDSWAGZ on 5/21/2014.
 */
public class Settings extends ActionBarActivity {

    MyCustomAdapter myCustomAdapter = new MyCustomAdapter();
    GridView MyGridView ;
    Context context = this ;
    Misc misc = new Misc(context);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_settings);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setCustomView(R.layout.header_bar2);
        TextView iconButton = (TextView) findViewById(R.id.header_title);
        iconButton.setText("Settings");

        //Disable main button and make active
        ImageButton settingsBtn = (ImageButton) findViewById(R.id.settingsBtn);
        settingsBtn.setEnabled(false);
        settingsBtn.setBackgroundResource(R.drawable.style_footer_menu_btn_active);
        settingsBtn.setImageResource(R.drawable.ic_menu_settings_active);
        overridePendingTransition(R.anim.fade_in , R.anim.fade_out);

        MyGridView = (GridView) findViewById(R.id.SettingsGrid);
        MyGridView.setAdapter(myCustomAdapter);
        MyGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MyCustomAdapter myAdapter = new MyCustomAdapter();
                MenuItems selectedItem = myAdapter.getItemsList(i);
                String title = selectedItem.title;
                String url ;

                if(title.equalsIgnoreCase("gallery")){
                    url = "http://dmas-nig.com/news/?gallery=gallery" ;
                }
                else if(title.equalsIgnoreCase("Downloads")){
                    url = "http://dmas-nig.com/news/?collection=dowloads" ;
                }
                else if(title.equalsIgnoreCase("Resources")){
                    url = "http://dmas-nig.com/news/?cat=6" ;
                }
                else if(title.equalsIgnoreCase("Testimonials")){
                    url = "http://dmas-nig.com/news/?cat=7" ;
                }
                else if(title.equalsIgnoreCase("Mercy Videos")){
                    url = "http://dmas-nig.com/news/?cat=8" ;
                }
                else if(title.equalsIgnoreCase("History")){
                    url = "file:///android_asset/www/history.html" ;
                }
                else if(title.equalsIgnoreCase("Prayers")){
                    url = "file:///android_asset/www/prayers.html" ;
                }
                else{
                    url = null ;
                }

                if( url == null ){
                    Intent about = new Intent(context,About.class);
                    startActivity(about);
                }else{
                    Intent intent = new Intent(context,Webview.class);
                    intent.putExtra("url",url);
                    intent.putExtra("title", title) ;
                    startActivity(intent);
                }

            }
        });

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


    //////// GRID VIEW HANDLING //////////////////////////////////

    public class MyCustomAdapter extends BaseAdapter {
        MenuItems menuItems = new MenuItems();
        List<MenuItems> itemsList = menuItems.getMenuItems();

        @Override
        public int getCount() {
            return itemsList.size();
        }

        @Override
        public MenuItems getItem(int i) {
            return itemsList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {

            if (arg1 == null) {
                LayoutInflater inflater = (LayoutInflater) Settings.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                arg1 = inflater.inflate(R.layout.widget_settings_grid, arg2, false);
            }

            MenuItems Item = itemsList.get(arg0);

            TextView itemTitle = (TextView) arg1.findViewById(R.id.itemTitle);
            ImageView itemImage = (ImageView) arg1.findViewById(R.id.itemImage) ;

            itemTitle.setText(Item.title);
            itemImage.setImageResource(Item.image);

            return arg1;
        }

        public MenuItems getItemsList(int position) {
            return itemsList.get(position);
        }
    }


    public class MenuItems {
        String title ;
        int image ;
        List<MenuItems> menuItemsList = new ArrayList<MenuItems>();

        public List<MenuItems> getMenuItems(){

            MenuItems item1 = new MenuItems();
            item1.title = "Gallery" ;
            item1.image = R.drawable.ic_gallery ;
            menuItemsList.add(item1);

            MenuItems item2 = new MenuItems();
            item2.title = "Resources" ;
            item2.image = R.drawable.ic_resources ;
            menuItemsList.add(item2);

            MenuItems item3 = new MenuItems();
            item3.title = "Prayers" ;
            item3.image = R.drawable.ic_prayer ;
            menuItemsList.add(item3);

            MenuItems item4 = new MenuItems();
            item4.title = "Mercy Videos" ;
            item4.image = R.drawable.ic_videos ;
            menuItemsList.add(item4);

            MenuItems item5 = new MenuItems();
            item5.title = "Testimonials" ;
            item5.image = R.drawable.ic_testimonials ;
            menuItemsList.add(item5);

            MenuItems item6 = new MenuItems();
            item6.title = "Downloads" ;
            item6.image = R.drawable.ic_downloads ;
            menuItemsList.add(item6);

            MenuItems item7 = new MenuItems();
            item7.title = "History" ;
            item7.image = R.drawable.ic_launcher ;
            menuItemsList.add(item7);

            MenuItems item8 = new MenuItems();
            item8.title = "About" ;
            item8.image = android.R.drawable.ic_menu_info_details ;
            menuItemsList.add(item8);

            return menuItemsList ;
        }

    }

    //////// END OF GRID VIEW HANDLING ///////////////////////////

}
