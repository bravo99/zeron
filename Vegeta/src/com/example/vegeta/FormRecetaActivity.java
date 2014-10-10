package com.example.vegeta;

import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TimePicker;

public class FormRecetaActivity extends Activity{
	
	final String[] categorias = {"Bocadillo","Ensalada","Pastas","Postre","Principal","Sopa"};
	String[] info;
	EditText titulo;
	Spinner categ;
	TimePicker time;
	private ArrayList<ParseObject> posts;
	ListView lv;
	public List<ParseObject> listaing;
		
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_receta);
		init(this);	
		
	}

	
	private void init(Context c) {
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
				
				info = new String [5];
				titulo =(EditText)findViewById(R.id.nombre_receta);
				categ =(Spinner)findViewById(R.id.spinnercat);
				time =(TimePicker)findViewById(R.id.timePicker1);
				lv = (ListView)findViewById(R.id.lv3);
				posts = new ArrayList<ParseObject>();
				
				ArrayAdapter<String> adaptadorSpinner =new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, categorias);
				adaptadorSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				categ.setAdapter(adaptadorSpinner);
				time.setIs24HourView(true);
				time.setCurrentHour(0);
				time.setCurrentMinute(0);
				
				Bundle bundle = new Bundle();
				bundle.putString("NOMBRE", titulo.getText().toString());
				bundle.putString("CATEGORIA", categ.getSelectedItem().toString());
				bundle.putString("TIEMPO",time.getCurrentHour() + ":" + time.getCurrentMinute());
				
				
				asd(c);
		
	} 
	
	
	
	public void buscar(View view){
		System.out.println(listaing.size());
		System.out.println(999);
	}
	
	
	/***
	 * busqueda
	 */
	private void asd(final Context ct) {
		
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Ingrediente");
		query.whereEqualTo("autor", "vegetatest");
		
		
		query.findInBackground( new FindCallback<ParseObject>() {
			
			@Override
			public void done(List<ParseObject> recetaList, ParseException e) {
		    	
		    	if (e == null) {
		    		 Log.d("score", "Retrieved " + recetaList.size() + " scores");
			            listaing=recetaList;
			            posts.clear();
						for (ParseObject post : recetaList) {
							posts.add(post);
						}
											
						ItemIngredQueryAdapter adapter = new ItemIngredQueryAdapter((Activity) ct,posts,(Context)ct);
						lv.setAdapter(adapter);
		            
		        } else {
		        	
		            Log.d("score", "Error: " + e.getMessage());
		        }
		        
		    }
		});
	}
	

}
