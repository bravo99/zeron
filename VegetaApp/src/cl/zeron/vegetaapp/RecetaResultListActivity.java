package cl.zeron.vegetaapp;

import java.util.List;


import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RecetaResultListActivity extends ActionBarActivity{
	
	@SuppressWarnings("unused")
	private CharSequence mTitle;
	TextView titulo;
	TextView descripcion;
	TextView tiempo;
	TextView personas;
	TextView valoracion;
	TextView ingred;
	ParseImageView imagen;	
	Button verComentarios;
	Dialog dialogComentarios;
	Bundle bundle;
	Button verAlimentos;
	protected ProgressDialog proDialog;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.receta_result_list);
		
		 bundle = getIntent().getExtras();
		 ingred = (TextView)findViewById(R.id.textView3);
				
		
		startLoading();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Receta");
		query.getInBackground(bundle.getString("ID"), new GetCallback<ParseObject>() {
		  public void done(ParseObject object, ParseException e) {
			if (e == null) {
				
				//imagen
//		    	ParseFile imageFile = object.getParseFile("imagen");
//		    	if (imageFile != null) {
//					imagen.setParseFile(imageFile);
//					imagen.loadInBackground();								
//				}
		    	//ingredientes
				
				ParseQuery<ParseObject> query2 = ParseQuery.getQuery("AlimentoReceta");
				query2.whereEqualTo("receta", object);
				 
				query2.findInBackground(new FindCallback<ParseObject>() {
				  public void done(List<ParseObject> commentList, ParseException e) {
					  
					 
					  String a = "";
					  for(int i=0; i<commentList.size(); i++){
						 try {
							a +=  commentList.get(i).getString("cantidad") + " " +
								  commentList.get(i).getString("medida") + " " +
								  commentList.get(i).getParseObject("ingrediente").fetchIfNeeded()
							      .getString("nombre") + "\n";
						} catch (ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					  }
					  ingred.setText(a);
				      stopLoading();
					 
				  }

				
				});
				
				
		    } else {
		      // something went wrong
		    }
		  }

		
		});
		
		
		
		
		titulo = (TextView)findViewById(R.id.textView1);
		titulo.setText(bundle.getString("TITULO"));
		
		imagen = (ParseImageView)findViewById(R.id.iv_imag_receta);
		//imagen.setImageBitmap(bitmap)
		imagen.setImageBitmap(BitmapFactory.decodeFile(bundle.getString("IMAG")));
		
		descripcion = (TextView)findViewById(R.id.textView2);
		descripcion.setText(bundle.getString("PREPARACION"));
		
		tiempo = (TextView)findViewById(R.id.tiempo);
		tiempo.setText(bundle.getString("TIEMPO"));
		
		personas = (TextView)findViewById(R.id.personas);
		personas.setText(bundle.getString("PERSONAS")+"[pers]");
		
		valoracion = (TextView)findViewById(R.id.valoracion);
		valoracion.setText("5/"+bundle.getString("VALORACION"));
		
		
		verComentarios = (Button)findViewById(R.id.ver_comentarios);
		verComentarios.setOnClickListener(new OnClickListener() {
		
			
			@Override
			public void onClick(View v) {
				Bundle bl=new Bundle();
				bl.putString("ID",bundle.getString("ID"));
				bl.putString("RECETA",bundle.getString("TITULO") );
				Intent intent=new Intent(RecetaResultListActivity.this,ListComentarioActivity.class);
				
				intent.putExtras(bl);
				startActivity(intent);
				
			}
		});
		
		verAlimentos = (Button)findViewById(R.id.ver_alimento);
		verAlimentos.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bundle bl=new Bundle();
				bl.putString("ID",bundle.getString("ID"));
				Intent intent=new Intent(RecetaResultListActivity.this,ListAlimentoActivity.class);
				intent.putExtras(bl);
				startActivity(intent);
			}
		});
		mTitle = getTitle(); // Get current title
		
		
		
		


		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setTitle(bundle.getString("TITULO"));
	
		
	    

	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.iconos, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        
	        case R.id.menu2_favoritas:
	        ParseQuery<ParseObject> query = ParseQuery.getQuery("Receta");
	    	query.getInBackground(bundle.getString("ID"), new GetCallback<ParseObject>() {
	    	public void done(ParseObject object, ParseException e) {
	    		if (e == null) {
	    			object.put("ingredientes", ingred.getText().toString());
	    			object.saveInBackground();
	    			object.pinInBackground(new SaveCallback() {
						@Override
						public void done(ParseException arg0) {
							if (arg0== null){
								Toast.makeText(getApplicationContext(),
											"Nueva receta añadida a Favoritas", Toast.LENGTH_SHORT).show();
							}
								
						}
				    });
	    			
	    		} else {
	    			      // something went wrong
	    		}
	    	}

	    		
	    	});
	        	
	        return true;
	        
	        case R.id.menu2_calificar:
	        	Toast.makeText(getApplicationContext(), "CALIFICAR", Toast.LENGTH_SHORT).show();
	            return true;
	        case R.id.menu2_comentar:
	        	Toast.makeText(getApplicationContext(), "COMENTAR", Toast.LENGTH_SHORT).show();
	            return true;
	        case R.id.menu2_editar:
	        	Toast.makeText(getApplicationContext(), "EDITAR", Toast.LENGTH_SHORT).show();
	            return true;
	        case R.id.menu2_eliminar:
	        	Toast.makeText(getApplicationContext(), "ELIMINAR", Toast.LENGTH_SHORT).show();
	            return true;    
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	
	@Override
	public Intent getSupportParentActivityIntent() {
		onBackPressed();
//	    Intent parentIntent= getIntent();
//      String className = parentIntent.getStringExtra("ParentClassName"); //getting the parent class name

	    Intent newIntent=null;
//	    try {
//	         //you need to define the class with package name
//	         newIntent = new Intent(this,Class.forName(className));
//
//	    } catch (ClassNotFoundException e) {
//	        e.printStackTrace();
//	    }
	    return newIntent;
	}
	
	protected void startLoading() {
		proDialog = new ProgressDialog(this);
	    proDialog.setMessage("loading...");
	    proDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	    proDialog.setCancelable(true);
	    proDialog.show();
	}

	protected void stopLoading() {
	    proDialog.dismiss();
	    proDialog = null;
	}
	
}
