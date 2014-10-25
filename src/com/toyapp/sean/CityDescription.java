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
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CityDescription extends Activity implements OnClickListener {
	
	private static String BASE_URL = "http://honey.computing.dcu.ie/city/city.php?id=";
	TextView id, country, region, city, latitude, longitude, comment;
	Button mapButton;
	double [] coordinates = new double[2];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.citydescription);
		
		mapButton = (Button) findViewById(R.id.bLocate);
		id = (TextView) findViewById(R.id.tvID);
		country = (TextView) findViewById(R.id.tvCountry);
		region = (TextView) findViewById(R.id.tvRegion);
		city = (TextView) findViewById(R.id.tvCity);
		latitude = (TextView) findViewById(R.id.tvLatitude);
		longitude  = (TextView) findViewById(R.id.tvLongitude);
		comment = (TextView) findViewById(R.id.tvComment);
		
		mapButton.setOnClickListener(this);
		
		String cityID = getCityId(savedInstanceState);
		id.setText(cityID);
		
		JSONCityDescription task = new JSONCityDescription();
		task.execute(cityID);
		// Toast.makeText(getApplicationContext(), cityID, Toast.LENGTH_LONG).show();
	}

	private String getCityId(Bundle savedState) {
		// TODO Auto-generated method stub
		String id = null;
		if (savedState == null) {
			Bundle extras = getIntent().getExtras();
			
			if (extras == null) {
				id = null;
			} else {
				id = extras.getString("CITY_CLICK_ID");
			}
		} else {
			id = (String) savedState.getSerializable("CITY_CLICK_ID");
		}
		return id;
	}
	
	private class JSONCityDescription extends AsyncTask<String, Void, String> {
		
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
			String countryName;
			String regionName;
			String cityName;
			double latitudeValue;
			double longitudeValue;
			String commentPhrase;
			
			countryName = regionName = cityName = commentPhrase = null;
			latitudeValue = longitudeValue = 0;
			
			try {
				parent = new JSONObject(data);
				countryName = parent.getJSONObject("result").getString("country"); 
				regionName = parent.getJSONObject("result").getString("region"); 
				cityName = parent.getJSONObject("result").getString("city"); 
				latitudeValue = parent.getJSONObject("result").getDouble("latitude"); 
				longitudeValue = parent.getJSONObject("result").getDouble("longitude"); 
				commentPhrase = parent.getJSONObject("result").getString("comment"); 
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			country.setText(countryName);
			region.setText(regionName);
			city.setText(cityName);
			latitude.setText(Double.toString(latitudeValue));
			longitude.setText(Double.toString(longitudeValue));
			comment.setText(commentPhrase);
			mapButton.setText("Locate " + cityName + " on Google Maps");
			
			coordinates[0] = latitudeValue;
			coordinates[1] = longitudeValue;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.bLocate:
				try {
					String location = "geo:" + coordinates[0] + "," + coordinates[1] + "?q=" + coordinates[0] + "," + coordinates[1];
					Intent searchIntent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(location));
					startActivity(searchIntent);
				} catch (Exception e) {
					String error = e.toString();
					
					Dialog dialog = new Dialog(this);
					dialog.setTitle("Error");
					
					TextView text = new TextView(this);
					text.setText(error);
					
					dialog.setContentView(text);
					dialog.show();
				}
				break;
		}
	}
}

