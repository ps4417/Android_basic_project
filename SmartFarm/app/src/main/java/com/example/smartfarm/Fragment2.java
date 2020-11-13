package com.example.smartfarm;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Fragment2 extends Fragment {
    TextView temp_avg,humid_avg,light_avg,soil_avg;
    SecondActivity s;
    ArrayList<Item> list;
    public Fragment2(SecondActivity s) {
        this.s  = s;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup viewGroup = null;
        viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_2,container,false);
        temp_avg = viewGroup.findViewById(R.id.temp_avg);
        humid_avg = viewGroup.findViewById(R.id.humid_avg);
        light_avg = viewGroup.findViewById(R.id.light_avg);
        soil_avg = viewGroup.findViewById(R.id.soil_avg);


        return viewGroup;
    }





}
