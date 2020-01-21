package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    //https://api.openweathermap.org/data/2.5/weather?q=kolkata&appid=0a68f9736494cb4a1a24ec9a99e1df4a
    EditText city;
    LinearLayout a;
    TextView result;
    Button store;
    String url="https://api.openweathermap.org/data/2.5/weather?q=";
    String API="&appid=0a68f9736494cb4a1a24ec9a99e1df4a";
    private RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        city=findViewById(R.id.editcity);
        result=findViewById(R.id.result);
        a=findViewById(R.id.layout);
        requestQueue=MySingleton.getInstance(this).getRequestQueue();
        store=findViewById(R.id.submit);
        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(city.getText().toString().trim().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter a city", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    sendAPIRequest();
                }
            }
        });
    }
    private void sendAPIRequest(){
       String myURL=url+city.getText().toString().trim()+API;
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, myURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.i("JSON","JSON:"+jsonObject);
                try {
                    String info=jsonObject.getString("weather");
                    Log.i("JSON","JSON:"+info);
                    JSONArray jsonArray=new JSONArray(info);
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject parObj=jsonArray.getJSONObject(i);
                        String myWeather=parObj.getString("main");
                        if(myWeather.equals("Haze"))
                        {
                            a.setBackgroundResource(R.drawable.haze);
                        }
                        else if(myWeather.equals("Clouds"))
                        {
                            a.setBackgroundResource(R.drawable.cloudy);
                        }
                        else if(myWeather.equals("Clear"))
                        {
                            a.setBackgroundResource(R.drawable.clear);
                        }
                        else if(myWeather.equals("Rainy"))
                        {
                            a.setBackgroundResource(R.drawable.rainy);
                        }
                        else if(myWeather.equals("Smoke"))
                        {
                            a.setBackgroundResource(R.drawable.smoky);
                        }
                        else
                        {
                            a.setBackgroundResource(R.drawable.back);
                        }
                        myWeather=myWeather+" in "+city.getText().toString().trim();
                        result.setText(myWeather);
                        Log.i("ID","ID:"+parObj.getString("id"));
                        Log.i("MAIN","MAIN:"+parObj.getString("main"));
                    }
                } catch (JSONException e) {
                    Toast.makeText(MainActivity.this,"Some error comes",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"Some error comes please check your Internet connection",Toast.LENGTH_SHORT).show();
            }
        });

        MySingleton.getInstance(MainActivity.this).addToRequestQue(jsonObjectRequest);
    }
}
