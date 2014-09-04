package com.example.vegeta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class Resbusqueda extends Activity{
	
	TextView resultado1;
    TextView resultado2;
    Toast toast1; 
    
   
    String[] info;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resultingrediente);
		
		init(this);
		
	}
	
	public void init(final Context ct){
		resultado1= (TextView)findViewById(R.id.textView2);
		resultado2= (TextView)findViewById(R.id.textView4);
		
		
		toast1 =  Toast.makeText(getApplicationContext(),"Nuevo ingrediente disponible ", Toast.LENGTH_SHORT);
		Intent men = getIntent();
		info = men.getStringArrayExtra(Buscar.ACT_INFO);
		resultado1.setText(info[0]);
		resultado2.setText(info[1]);
		
		
		
		
		
		
	}
		

}
