package com.toyapp.sean;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class CityList extends Activity {

	private static String BASE_URL = "http://honey.computing.dcu.ie/city/city.php?id=";
	ProgressDialog pd;

	ArrayList<String> allCities = new ArrayList<String>();
	ListView lv;
	ArrayAdapter<String> adapter;
	int counter = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.citylist);
		lv = (ListView) findViewById(R.id.listview);
		adapter = new ArrayAdapter<String>(CityList.this, android.R.layout.simple_list_item_1, allCities);
		generateEntries();
		
		//PAUSE PROGRAM UNTIL LIST IS LOADED

		
		lv.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
				if (firstVisibleItem + visibleItemCount == totalItemCount) {
					generateEntries();
				}
			}

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				// TODO Auto-generated method stub

				adapter.notifyDataSetChanged();
			}
		});
		
		lv.setOnItemClickListener(new OnItemClickListener() {
			
			public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
				// Toast.makeText(getApplicationContext(), "Click ListItem " + position, Toast.LENGTH_LONG).show();
				
				Class ourClass;
				try {
					ourClass = Class.forName("com.toyapp.sean.CityDescription");
					Intent ourIntent = new Intent(CityList.this, ourClass);
					String keyIdentifier = Integer.toString(position + 1);
					ourIntent.putExtra("CITY_CLICK_ID", keyIdentifier);
					startActivity(ourIntent);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
	}
	
	private void generateEntries() {
		// TODO Auto-generated method stub
		String cityID = "";

		for (int i = counter; i < counter + 10; i++) {
			JSONCityTask task = new JSONCityTask();
			cityID = Integer.toString(i+1);
//			String d = Integer.toString(i+1);
			task.execute(cityID);
//			allCities.add(d);
		}
		counter += 10;
	}

	private class JSONCityTask extends AsyncTask<String, Void, String> {
		
		@Override
		protected void onPreExecute() {
		}


		@Override
		protected String doInBackground(String... citySelection) {
			HttpURLConnection connection = null ;
			InputStream input = null;

			try {
				connection = (HttpURLConnection)(new URL(BASE_URL + citySelection[0])).openConnection();
				connection.setRequestMethod("GET");
				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.connect();

				StringBuffer buffer = new StringBuffer();
				input = connection.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(input));
				
				String line = null;
				
				while ((line = br.readLine()) != null) {
					buffer.append(line + "\r\n");
				}
				
				input.close();
				connection.disconnect();
				
				return buffer.toString();
		    } catch (Exception e) {
				e.printStackTrace();
			} finally {
				try { 
					input.close(); 
					connection.disconnect();
				} catch(Exception e) {
					e.printStackTrace();
				}
			}

			return null;
		}
		
		@Override
		protected void onPostExecute(String data) {			
			super.onPostExecute(data);
			
			JSONObject parent;
			String cityName = "";
			try {
				parent = new JSONObject(data);
				cityName = parent.getJSONObject("result").getString("city"); 
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			allCities.add(cityName);		
			
			lv.setAdapter(adapter); 

		}
	}
}