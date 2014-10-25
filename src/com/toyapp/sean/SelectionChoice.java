package com.toyapp.sean;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SelectionChoice extends Activity implements OnClickListener {

	Button searchCity, chooseCity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selectionchoice);
		
		initializeVariables();
	}

	private void initializeVariables() {
		
		searchCity = (Button) findViewById(R.id.bCitySearch);
		chooseCity = (Button) findViewById(R.id.bCityList);
		
		searchCity.setOnClickListener(this);
		chooseCity.setOnClickListener(this);
	}

	@Override
	public void onClick(View selection) {
		
		switch (selection.getId()) {
			case R.id.bCitySearch:
				try {
					Intent searchIntent = new Intent("com.toyapp.sean.CITYSEARCH");
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
				
			case R.id.bCityList:
				try {
					Intent listIntent = new Intent("com.toyapp.sean.CITYLIST");
					startActivity(listIntent);
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
