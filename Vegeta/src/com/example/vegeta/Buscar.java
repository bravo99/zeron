package com.example.vegeta;

import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

public class Buscar extends Activity{
	
	AutoCompleteTextView tvac;
	Button bt;
	final static String ACT_INFO = "com.example.vegeta.Resbusqueda";
	String[] info;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buscar);
		init();
	}
	
	private void init() {
		tvac=(AutoCompleteTextView)findViewById(R.id.autoCompleteTextView1);
		bt=(Button) findViewById(R.id.button1);
		info =new String[4];
		
		bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ParseQuery<ParseObject> query = ParseQuery.getQuery("Ingrediente");
				query.whereEqualTo("nombre", tvac.getText().toString());
				
				query.findInBackground( new FindCallback<ParseObject>() {
					
					@Override
					public void done(List<ParseObject> recetaList, ParseException e) {
				    	
				    	if (e == null) {
				        	if(recetaList.size()!=0){
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
			}
			
		});}
	
	private void lanzar() {
		Intent a = new Intent(this, Resbusqueda.class);
		a.putExtra(ACT_INFO, info);
		
		startActivity(a);
		
	}
	private void lanzar2() {
		Intent a = new Intent(this, FormIngrediente.class);
		info[0]=tvac.getText().toString();
		
		startActivity(a);
	}		

	
}
