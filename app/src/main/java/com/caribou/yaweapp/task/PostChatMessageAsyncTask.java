package com.caribou.yaweapp.task;

import android.os.AsyncTask;
import android.util.Log;

import com.caribou.yaweapp.ApiUrl.ListOfApiUrl;
import com.caribou.yaweapp.model.ChatMessage;
import com.caribou.yaweapp.model.CommentPicture;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;


public class PostChatMessageAsyncTask extends AsyncTask<ChatMessage, String, String> {

    protected String doInBackground(ChatMessage... chatMessages) {
        ChatMessage cm = chatMessages[0];

        InputStream inputStream = null;
        String result = "";

        String urlString = ListOfApiUrl.getUrlAddChatMessage();
        Log.i("url: ", urlString);
        String message = "";
        JSONObject jo = new JSONObject();
        try {

            Date dt = new Date(cm.getPostDate().getTime());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String curentTime = sdf.format(dt);


            jo.accumulate("text", cm.getText());
            jo.accumulate("id_user", cm.getId_user());
            jo.accumulate("postDate", curentTime);

            message = jo.toString();
            Log.i("message json:" , message);
            URL url = new URL(urlString);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setFixedLengthStreamingMode(message.getBytes().length);

            //make some HTTP header nicety
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");

            //open
            conn.connect();

            //setup send
            OutputStream os = new BufferedOutputStream(conn.getOutputStream());
            os.write(message.getBytes());
            //clean up
            os.flush();
            //do somehting with response
            inputStream = conn.getInputStream();
            Log.i("test", inputStream.toString());
            os.close();
            conn.disconnect();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return null;
    }

}
