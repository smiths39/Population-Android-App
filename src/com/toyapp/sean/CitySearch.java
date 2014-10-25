package com.toyapp.sean;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
import android.widget.EditText;
import android.widget.TextView;

public class CitySearch extends Activity implements OnClickListener {

	EditText idInput;
	Button search;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.citysearch);
		
		idInput = (EditText) findViewById(R.id.etCityID);
		search = (Button) findViewById(R.id.bSearch);
		
		search.setOnClickListener(this);
		
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.bSearch:
				
				try {
					String cityID = idInput.getText().toString();
				} catch (Exception e) {
					String error = e.toString();
					
					Dialog dialog = new Dialog(this);
					dialog.setTitle("Error");
					
					TextView text = new TextView(this);
					text.setText(error);
					
					dialog.setContentView(text);
					dialog.show();
				}
		}
}}