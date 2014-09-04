package com.example.vegeta;
import java.util.List;

import com.example.vegeta.FormIngrediente;
import com.example.vegeta.ItemIngredQueryAdapter;
import com.example.vegeta.ItemQueryAdaptermr;
import com.example.vegeta.MisRecetas;
import com.example.vegeta.Modificar;
import com.example.vegeta.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class ResultIngrediente1 extends Activity{
	
	TextView resultado1;
    TextView resultado2;
    TextView resultado3;
   
    Button bt1;
    Button bt2;
    String[] info,inf1;
    String objid;
    final static String ACT_INFO = "com.example.vegeta.ModificarIngrediente";
    final static String ACT_INFO1= "com.example.vegeta.Ingrediente";
    Toast toast1; 
    
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resultingrediente1);
		init();
	}
	public void init(){
		toast1 =  Toast.makeText(getApplicationContext(),"Eliminando Ingrediente...", Toast.LENGTH_SHORT);
		resultado1= (TextView)findViewById(R.id.textView2);
		resultado2= (TextView)findViewById(R.id.textView4);
				
		bt1 = (Button)findViewById(R.id.button2) ;
		bt2 = (Button)findViewById(R.id.button1) ;
		Intent men = getIntent();
		info = men.getStringArrayExtra(ItemIngredQueryAdapter.ACT_INFO);
		
		resultado1.setText(info[0]);
		resultado2.setText(info[1]);
				
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
				ParseQuery<ParseObject> query = ParseQuery.getQuery("Ingrediente");
				query.whereEqualTo("objectId", info[2]);
				toast1.show();
	        	lanzar2();
				
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
		
		Intent a = new Intent(this,ModificarIngrediente.class );
		a.putExtra(ACT_INFO, algo);
		startActivity(a);
		
	}
private void lanzar2() {
	Intent a = new Intent(this,Ingrediente.class );
	a.putExtra(ACT_INFO1, false);
	startActivity(a);
	
}	


}
