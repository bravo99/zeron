package com.example.vegeta;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TextView;

public class ResultRecetaActivity extends Activity{
	TextView resultado1;
	TextView resultado2;
	String[] info;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resultreceta);
		
		//Resources res = getResources();
		TabHost tabs = (TabHost) findViewById(android.R.id.tabhost);
		tabs.setup();
		TabHost.TabSpec spec = tabs.newTabSpec("Pestana1");
		spec.setContent(R.id.tab1);
		spec.setIndicator("Descripcion");
		tabs.addTab(spec);
		
		TabHost.TabSpec spec2 = tabs.newTabSpec("Pestana2");
		spec2.setContent(R.id.tab2);
		spec2.setIndicator("Ingredientes");
		tabs.addTab(spec2);
		
		TabHost.TabSpec spec3 = tabs.newTabSpec("Pestana3");
		spec3.setContent(R.id.tab3);
		spec3.setIndicator("Preparacion");
		tabs.addTab(spec3);
		
		
		
		Intent men = getIntent();
		info = men.getStringArrayExtra(ItemQueryAdapter.ACT_INFO1);
		
		resultado1 = (TextView)findViewById(R.id.tvpreparacion);
		resultado1.setText(info[1]);
		resultado2 = (TextView)findViewById(R.id.tvtit);
		resultado2.setText(info[0]);
		
	} 
}
