package com.example.mausam;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    EditText cityName;
    TextView resultMau;
    public void checkWeather(View view) throws UnsupportedEncodingException {
        InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(cityName.getWindowToken(),0);
        try {
            String q = URLEncoder.encode(cityName.getText().toString(), "UTF-8");
            DownloadTask downloadTask = new DownloadTask();

            downloadTask.execute("http://api.openweathermap.org/data/2.5/weather?q="+q+"&appid=94d671bf22b97439c73f0abc78ab2ad9");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"could not find weather",Toast.LENGTH_LONG).show();
        }

    }
    public class DownloadTask extends AsyncTask<String , Void, String>{



        @Override
        protected String doInBackground(String... urls) {
            String result="";
            URL url;
            HttpURLConnection urlConnection=null;
            try {
                url=new URL(urls[0]);
                urlConnection=(HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                int data=reader.read();
                while(data!=-1)
                {
                    char current=(char)data;
                    result+=current;
                    data=reader.read();
                }

                return result;
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"could not find weather",Toast.LENGTH_LONG).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                String message="";
                JSONObject jsonObject=new JSONObject(result);
                String p=jsonObject.getString("weather");
                JSONArray jsonArray=new JSONArray(p);
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                    String main="";
                    String description="";
                    main= jsonObject1.getString("main");
                    description=jsonObject1.getString("description");
                    if(main!=""&& description!=""){
                        message+=main + ":"+description+"\r\n";
                    }

                }
                if(message!=""){
                    resultMau.setText(message);
                }
                else{
                    Toast.makeText(getApplicationContext(),"could not find weather",Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityName=(EditText) findViewById(R.id.cityName);
        resultMau=(TextView)findViewById(R.id.resultMau);
    }
}