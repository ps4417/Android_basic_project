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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Fragment1 extends Fragment {
    SecondActivity s;
    ListView listView;
    LinearLayout container;
    ArrayList<Item> list;

    public Fragment1(SecondActivity s) {
        this.s = s;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup viewGroup = null;
        viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_1,container,false);
        listView = viewGroup.findViewById(R.id.listView);
        container = viewGroup.findViewById(R.id.container);
        list = new ArrayList<>();
        getData();

        return viewGroup;
    }

    private void getData(){
        String url = "http://192.168.0.3/smartFarm/item.jsp";
        ItemAsync itemAsync = new ItemAsync();
        itemAsync.execute(url);
    }

    class ItemAsync extends AsyncTask<String,Void,String>{
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(s);
            progressDialog.setTitle("Get Data...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String url = strings[0].toString();
            String result = HttpConnect.getString(url);
            return result;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(final String s) {
            progressDialog.dismiss();
            JSONArray ja = null;
            try {
                ja = new JSONArray(s);
                for(int i=0;i<ja.length();i++){
                    JSONObject jo = ja.getJSONObject(i);
                    String id = jo.getString("id");
                    String temp = jo.getString("temp");
                    String humid = jo.getString("humid");
                    String light = jo.getString("light");
                    String soil = jo.getString("soil");



                    Item item = new Item(id,temp,humid,light,soil);
                    list.add(item);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ItemAdapter itemAdapter = new ItemAdapter();
            listView.setAdapter(itemAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("id:" +list.get(position).getId()+
                            "\n온도:" +list.get(position).getTemp()+
                            "ºC\n습도:" +list.get(position).getHumid()+
                            "%\n조도:" +list.get(position).getLight()+
                            "lx\n토양습도:" +list.get(position).getSoil()+ "%");

                    builder.show();
                }
            });
        }
    } // end AsyncTask

               class ItemAdapter extends BaseAdapter{

                @Override
                public int getCount() {
                    return list.size();
                }

                @Override
                public Object getItem(int position) {
                    return list.get(position);
                }

                @Override
                public long getItemId(int position) {
                    return 0;
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View itemView = null;
                    LayoutInflater inflater = (LayoutInflater) s.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    itemView = inflater.inflate(R.layout.item,container,true);

                    TextView id = itemView.findViewById(R.id.id);
                    TextView temp = itemView.findViewById(R.id.temp);
                    TextView humid = itemView.findViewById(R.id.humid);
                    TextView light = itemView.findViewById(R.id.light);
                    TextView soil = itemView.findViewById(R.id.soil);


                    id.setText(list.get(position).getId());
                    temp.setText(list.get(position).getTemp());
                    humid.setText(list.get(position).getHumid());
                    light.setText(list.get(position).getLight());
                    soil.setText(list.get(position).getSoil());


            return itemView;
        }
    }
}