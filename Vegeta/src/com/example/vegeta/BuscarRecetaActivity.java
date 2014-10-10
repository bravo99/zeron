package com.example.vegeta;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class BuscarRecetaActivity extends ActionBarActivity{
	
	private static final String Imagenes = null;
	AutoCompleteTextView buscar;
	ListView lv;
	private ArrayList<ParseObject> posts;
	public List<ParseObject> listarec;
	boolean estado;
	ImageButton bt;
	Bundle bundle2;
	Toast toast1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buscarreceta);
		init();
	}
	
	private void init() {
		//Bundle bundle = getIntent().getExtras();
		bundle2=new Bundle();
		//estado = bundle.getBoolean("ESTADO");
		buscar=(AutoCompleteTextView)findViewById(R.id.busqreceta);
		bt=(ImageButton)findViewById(R.id.imageButton1);
		lv =(ListView)findViewById(R.id.listareceta);
		posts = new ArrayList<ParseObject>();
		toast1 =  Toast.makeText(getApplicationContext(),"Ingrese su busqueda", Toast.LENGTH_SHORT);
		
		
       
        bt.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				if(!buscar.getText().toString().isEmpty()) {
					String bus1 = buscar.getText().toString();
					String pos0 = bus1.substring(0, 1).toUpperCase();
					String pos1ton = bus1.substring(1, bus1.length());
					String bus = pos0.concat(pos1ton);
					
				ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Receta");
				query1.whereContains("nombre", bus1);
				
				ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Receta");
				query2.whereContains("nombre", bus);
				
				List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
				queries.add(query1);
				queries.add(query2);
				
				ParseQuery<ParseObject> querymain = ParseQuery.or(queries);
				querymain.findInBackground( new FindCallback<ParseObject>() {
					
					@Override
					public void done(List<ParseObject> recetaList, ParseException e) {
				    	
				    	if (e == null) {
				        	if(recetaList.size()!=0){
				        	System.out.println(recetaList.size());
				            Log.d("score", "Retrieved " + recetaList.size() + " scores");
				            bundle2.putString("NOMBRE", recetaList.get(0).getString("nombre"));
				            bundle2.putString("PREPARACION",recetaList.get(0).getString("preparacion"));
				            bundle2.putInt("TIEMPO",recetaList.get(0).getInt("tiempo"));
				            bundle2.putInt("CALIFICACION",recetaList.get(0).getInt("calificacion"));
				            
				            
				            
				            lanzar();
				            }
				        	else{
				        		
				        			Toast.makeText(BuscarRecetaActivity.this, "No se encontro la receta",
				        					Toast.LENGTH_LONG).show();
				        		
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
		
		);
llenarlista(BuscarRecetaActivity.this);
	}
		
	private void lanzar(){
		Intent intent=new Intent(this,ResultReceta.class);
		intent.putExtras(bundle2);
		startActivity(intent);
	}	
		
	private void llenarlista(final Context ct){
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Receta");
		query.whereGreaterThan("calificacion", 4);
		
		
		query.findInBackground( new FindCallback<ParseObject>() {
			
			

			@Override
			public void done(List<ParseObject> recetaList, ParseException e) {
		    	
		    	if (e == null) {
		    		 Log.d("score", "Retrieved " + recetaList.size() + " scores");
			            listarec=recetaList;
			            posts.clear();
						for (ParseObject post : recetaList) {
							posts.add(post);
						}
											
						ItemRecetaQueryAdapter adapter = new ItemRecetaQueryAdapter((Activity) ct,posts);
						lv.setAdapter(adapter);
		            
		        } else {
		        	
		            Log.d("score", "Error: " + e.getMessage());
		        }
		        
		    }
		});
	
		
		
		
	}

	private String guardarImagen (Context context, String nombre, Bitmap imagen){
	    ContextWrapper cw = new ContextWrapper(context);
	    File dirImages = cw.getDir(Imagenes, Context.MODE_PRIVATE);
	    
		File myPath = new File(dirImages, nombre + ".png");
	     
	    FileOutputStream fos = null;
	    try{
	        fos = new FileOutputStream(myPath);
	        imagen.compress(Bitmap.CompressFormat.JPEG, 10, fos);
	        fos.flush();
	    }catch (FileNotFoundException ex){
	        ex.printStackTrace();
	    }catch (IOException ex){
	        ex.printStackTrace();
	    }
	    return myPath.getAbsolutePath();}


}
