package com.example.vegeta;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TabHost;
import android.widget.TextView;

public class ResultReceta extends Activity{
	TextView resultado1;
	TextView resultado2;
	RatingBar rb;
	TextView crono;
	String[] info;
	ImageView iv;
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
		
		
		
		//Intent men = getIntent();
		//info = men.getStringArrayExtra(ItemQueryAdapter.ACT_INFO1);
		Bundle bundle = getIntent().getExtras();
		
		resultado1 = (TextView)findViewById(R.id.tvpreparacion);
		resultado1.setText(bundle.getString("PREPARACION"));
		resultado2 = (TextView)findViewById(R.id.tvtit);
		resultado2.setText(bundle.getString("NOMBRE"));
		rb=(RatingBar)findViewById(R.id.ratingBar1);
		//rb.setMax(5);
		rb.setNumStars(5);
		
		rb.setProgress(bundle.getInt("CALIFICACION")*2);
		rb.setEnabled(false);
		crono=(TextView)findViewById(R.id.time);
		crono.setText(bundle.getInt("TIEMPO")+ "minutos");
		iv=(ImageView)findViewById(R.id.ivimareceta);
		iv.setImageBitmap(BitmapFactory.decodeFile(bundle.getString("IMAG")));
	} 
}