package com.homecontrol.andrew.homecontrol;

import android.os.AsyncTask;
import android.util.Log;

import com.homecontrol.andrew.homecontrollibrary.HANService;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by andrew on 7/9/14.
 */
public class DownloadTask extends AsyncTask<Void, Void, String> { // first String is what is passed in (.execute(url)), second is for progress update,
    // last if what is passed on doInBackground to onPostExecute. Nothing is returned to the calling method
    private static final String TAG = "DownloadJSONTask";
    private HANService hanService;  // stores a MobileActivity object so I dont have to keep calling getActivity
    private JSONArray jsonArray;
    private String phpUrl;

    public DownloadTask(HANService service, String phpScript){
        hanService = service;
        Log.d(TAG, "beginning download");
        phpUrl = phpScript;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        return getJSONText(phpUrl);    // might still be null if there was an exception
    }

    @Override
    protected void onPostExecute(String result) {
        // if we get a valid result from server
        if (result != null) {
            //create jsonArray and return data to Service
            //this.jsonArray = new JSONArray(result);
            Log.d(TAG, "assigned result to JSONArray");
            hanService.returnJSONDownload(result);
        } else {
            Log.d(TAG, "No response from Server");   // so far so good. if we get an exception we catch it and we will have null here. so i need to deal with the null. change the toast message
        }
    }

    private String getJSONText(String myUrl){
        InputStream is = null;  // input stream
        String content = null;
        //setUpCertificate();
        try{
            URL url = new URL(myUrl);
            Log.d(TAG, myUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();  // openConnetion() may throw IOException
            //urlConnection = (HttpsURLConnection) url.openConnection();  // openConnetion() may throw IOException
            // using ssl, the url is hard coded below in the setUpCertificate method
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            Log.d(TAG, "connected");
            int response = conn.getResponseCode();
            Log.i(TAG, "The response is " + response);
            is = conn.getInputStream(); // create input stream from http connection
            content = readInput(is);    // read input stream and extract data as string
            //Log.d(TAG, content);  // trying to catch when the php script might return an error trying to access database
        } catch (MalformedURLException mue){
            Log.e(TAG, mue.toString());
        } catch (IOException ioe){  // catch IOException of readInput
            Log.e(TAG, ioe.toString());
        }
        finally{
            if(is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                }
            }
            return content; // return string read from input stream
        }
    }

    private String readInput(InputStream stream) throws IOException{
        Log.d(TAG, "reading inputStream");
        String result = "";
        BufferedReader reader = null;
        reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String buffer;
        while((buffer = reader.readLine()) != null){
            result += buffer;
        }
        return result;
    }
}
