package com.example.vegeta;

import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

public class Buscar extends Activity{
	
	AutoCompleteTextView tvac;
	Button bt;
	final static String ACT_INFO = "com.example.vegeta.Resbusqueda";
	String[] info;
	Toast toast1; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buscar);
		init();
	}
	
	private void init() {
		tvac=(AutoCompleteTextView)findViewById(R.id.autoCompleteTextView1);
		bt=(Button) findViewById(R.id.button1);
		toast1 =  Toast.makeText(getApplicationContext(),"Ingrese su busqueda", Toast.LENGTH_SHORT);
		info =new String[4];
		
		
		
		bt.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				if(!tvac.getText().toString().isEmpty()) {
					String bus = tvac.getText().toString();
					String pos0 = bus.substring(0, 1).toUpperCase();
					String pos1ton = bus.substring(1, bus.length());
					bus = pos0.concat(pos1ton);
				ParseQuery<ParseObject> query = ParseQuery.getQuery("Ingrediente");
				query.whereMatches("nombre", bus);
				
				query.findInBackground( new FindCallback<ParseObject>() {
					
					@Override
					public void done(List<ParseObject> recetaList, ParseException e) {
				    	
				    	if (e == null) {
				        	if(recetaList.size()!=0){
				        		System.out.println(recetaList.size());
				            Log.d("score", "Retrieved " + recetaList.size() + " scores");
				            info[0]=recetaList.get(0).getString("nombre");
				            info[1]=recetaList.get(0).getString("descripcion");
				            lanzar();
				            }
				        	else{
					    		 lanzar2();
					    	}
						  }
				    				    	        				    		
				            				            
				         else {
				        	
				            Log.d("score", "Error: " + e.getMessage());
				          }
				        
				    }

					
					
				});
			}else{
				toast1.show();
			}
			
		}
			} 
		
		);}
	
	/*
	 * existe algna coincidencia
	 */
	private void lanzar() {
		Intent a = new Intent(this, Resbusqueda.class);
		a.putExtra(ACT_INFO, info);
		
		startActivity(a);
		
	}
	
	/*
	 * no hay coincidencia
	 */
	private void lanzar2() {
		Intent a = new Intent(this, FormIngrediente.class);
		info[0]=tvac.getText().toString();
		
		startActivity(a);
	}		

	
}
