package com.example.animdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static android.app.PendingIntent.getActivity;

public class NavActivity extends AppCompatActivity {

    AutoCompleteTextView searchcitytv;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        searchcitytv = (AutoCompleteTextView) findViewById(R.id.citysearch);

        String[] cities = getcity().toArray(new String[0]);
        Log.i("Citiess", Arrays.toString(cities));
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cities);
        searchcitytv.setAdapter(adapter);
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getApplicationContext().getAssets().open("cities.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public ArrayList<String> getcity(){

        ArrayList<String> city = new ArrayList<>();
        try{
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            for(int i = 0; i<obj.names().length(); i++){

                Log.i("Country", Objects.requireNonNull(obj.names()).getString(i));
                JSONArray cityarr = obj.getJSONArray(obj.names().getString(i));

                for(int j=0;j<cityarr.length();j++){
                    city.add(cityarr.getString(j));

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return city;
    }
}