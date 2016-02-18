package com.malbarando.tweetmap.helpers;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Maica Albarando on 2/17/2016.
 */
public class TweetSocketTask extends AsyncTask<Void, Void, Void> {


    private static final String TAG = "tweet-tag";
    private final String url;
    private final int port;
    private String response;

    public TweetSocketTask(String url, int port, String response) {
        this.url = url;
        this.port = port;
        this.response = response;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Socket socket = null;
        try {
            socket = new Socket(url, port);
            ByteArrayOutputStream byteArrayOutputStream =
                    new ByteArrayOutputStream(1024);
            byte[] buffer = new byte[1024];

            int bytesRead;
            InputStream inputStream = socket.getInputStream();

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
                response += byteArrayOutputStream.toString("UTF-8");
            }


        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        Log.d(TAG, response);
        super.onPostExecute(result);
    }
    
}
