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
import android.widget.Button;
import android.widget.TextView;


public class Result1 extends Activity {
	
	TextView resultado1;
    TextView resultado2;
    TextView resultado3;
   
    Button bt1;
    Button bt2;
    String[] info;
    String objid;
    final static String ACT_INFO = "com.example.vegeta.Modificar";
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result1);
		init();
	}
	public void init(){
		resultado1= (TextView)findViewById(R.id.tvr1);
		resultado2= (TextView)findViewById(R.id.tvr2);
		resultado3= (TextView)findViewById(R.id.tvr3);
		
		bt1 = (Button)findViewById(R.id.br1) ;
		bt2 = (Button)findViewById(R.id.br2) ;
		Intent men = getIntent();
		info = men.getStringArrayExtra(ItemQueryAdaptermr.ACT_INFO);
		resultado1.setText("Receta: "+info[0]);
		resultado2.setText("Descripcion: "+info[1]);
		resultado3.setText("Autor: "+info[2]);
		objid=info[3];
		
		bt2.setOnClickListener(new OnClickListener() {
			
			@Override
			
			public void onClick(View v) {
				System.out.println(info[0]);
				lanzar(info);
			}
		});
		
		bt1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ParseQuery<ParseObject> query = ParseQuery.getQuery("Receta");
				query.whereEqualTo("objectId", info[3]);
				
				
				query.findInBackground( new FindCallback<ParseObject>() {
					
					@Override
					public void done(List<ParseObject> recetaList, ParseException e) {
				    	
				    	if (e == null) {
				        	recetaList.get(0).deleteInBackground();  	
				            Log.d("score", "Retrieved " + recetaList.size() + " scores");
				            
				            				            
				        } else {
				        	
				            Log.d("score", "Error: " + e.getMessage());
				        }
				        
				    }
				});
				
				
				
				
			}
		});
	
		
	}
	
private void lanzar(String[] algo) {
		
		Intent a = new Intent(this,Modificar.class );
		a.putExtra(ACT_INFO, algo);
		startActivity(a);
		
	}
	

}
