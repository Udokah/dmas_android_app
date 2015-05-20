package com.dmasnig.udcreate.utilities;

import android.os.Environment;

import java.io.*;
import java.text.DateFormat;
import java.util.Date;

/**
 * Created by UDSWAGZ on 6/2/2014.
 */
public class FileHelper {

    private String appFolder = Config.APPFOLDER ;
    String storageState = Environment.getExternalStorageState();
    File SDcard = Environment.getExternalStorageDirectory();
    private String Msgfile = appFolder + Config.msgs_file ;
    String xmlHeader = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<messages>" ;
    String xmlFooter = "</messages>" ;

    String defaultXML = "<log>\n" +
            "    <date> </date>\n" +
            "    <message>No message</message>\n" +
            "    </log>\n";

    public String FormatMsg(String message){
        Date now = new Date();
        String date = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(now);
        String xml = "<log>\n" +
                "    <date>" + date + "</date>\n" +
                "    <message>" + message + "</message>\n" +
                "    </log>\n";

        return xml;
    }

    public void CreateAppDir(){
        File folder = new File(SDcard + appFolder );
        if(!folder.exists()){
            folder.mkdir();
        }
    }

    public boolean addMessage(String data) throws Exception{
        boolean result = false;
        File myFile = new File( SDcard , Msgfile );
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {

            if(!myFile.exists()){
                try{
                    myFile.createNewFile();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            try {
                FileOutputStream fOut = new FileOutputStream(myFile, true);
                OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                myOutWriter.append( FormatMsg(data) );
                myOutWriter.close();
                fOut.close();
                result = true;
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return result ;
    }

    public void ClearMsgFile() throws Exception{
        File myFile = new File( SDcard , Msgfile );
        myFile.delete();
        myFile.createNewFile();
        addMessage(defaultXML);
    }


    public String ReadFromMsgFile() throws Exception{
        String dataFromFile = "" ;
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            File file = new File( SDcard , Msgfile );
            if(!file.exists()){
                file.createNewFile();
                dataFromFile = defaultXML;
            }else {
                BufferedReader inputReader2 = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                String inputString2;
                StringBuffer stringBuffer2 = new StringBuffer();
                while ((inputString2 = inputReader2.readLine()) != null) {
                    stringBuffer2.append(inputString2 + "\n");
                }
                dataFromFile = stringBuffer2.toString() ;
            }

        }
        return xmlHeader + dataFromFile + xmlFooter ;
    }
}
