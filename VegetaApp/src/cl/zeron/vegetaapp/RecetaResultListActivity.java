package cl.zeron.vegetaapp;

import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
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
	private Dialog rankDialog;
	private Dialog ComentarioDialog;
	protected Dialog ElimDialog;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.receta_result_list);
		
		bundle = getIntent().getExtras();
		 
		ingred = (TextView)findViewById(R.id.textView3);
		ingred.setText(bundle.getString("INGREDIENTES"));

		
		
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
		if(bundle.getString("CLAVE").equals("fav")){
			getMenuInflater().inflate(R.menu.iconosfav, menu);
		 } else{
			 getMenuInflater().inflate(R.menu.iconos, menu);
		}
	    return super.onCreateOptionsMenu(menu);
	}
	
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    final RatingBar ratingBar;
		switch (item.getItemId()) {
	        
		    //AGREGAR A FAVORITAS
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
								
					}});
	    			
	    		} 
	    	}});
	        	
	        return true;
	        
	        //CALIFICAR
	        case R.id.menu2_calificar:
	        	
	        	rankDialog = new Dialog(this,R.style.FullHeightDialog);
	            rankDialog.setContentView(R.layout.rank_dialog);
	            rankDialog.setCancelable(true);
	            ratingBar = (RatingBar)rankDialog.findViewById(R.id.dialog_ratingbar);
	            ratingBar.setRating(5);
	            
	            TextView text = (TextView) rankDialog.findViewById(R.id.rank_dialog_text1);
	            text.setText(bundle.getString("TITULO"));
	     
	            Button updateButton = (Button) rankDialog.findViewById(R.id.rank_dialog_button);
	            updateButton.setOnClickListener(new View.OnClickListener() {
	            	
	                @Override
	                public void onClick(View v) {
	                	System.out.println(ratingBar.getRating()); 
	                	
	                	ParseQuery<ParseObject> query = ParseQuery.getQuery("Receta");
	         	    	query.getInBackground(bundle.getString("ID"), new GetCallback<ParseObject>() {
	         	    	public void done(ParseObject object, ParseException e) {
	         	    		if (e == null) {
	         	    			int vcs = object.getInt("vecesCalif");
	         	    			String val =  object.getString("valoracion");
	         	    			int valora = Integer.parseInt(val);
	         	    			int valfin = (int) Math.round((valora + (int)ratingBar.getRating())/2); 
	         	    			object.put("valoracion", Integer.toString(valfin));
	         	    			object.put("vecesCalif", vcs+1);
	         	    			object.saveInBackground(new SaveCallback() {
									
									@Override
									public void done(ParseException arg0) {
										Toast.makeText(getApplicationContext(),
												"Calificacion exitosa", Toast.LENGTH_SHORT).show();
										
									}
								});
	         	    			
	         	    		}else {
	  	    			     
	        	    		}
	        	    	}

	        	    		
	        	    	});
	         	    		
	                    rankDialog.dismiss();
	                }
	            });
	            //now that the dialog is set up, it's time to show it    
	            rankDialog.show();                
	           
	            return true;
	            
	        //COMENTAR    
	        case R.id.menu2_comentar:
	        	
	        	ComentarioDialog = new Dialog(this,R.style.FullHeightDialog);
	            ComentarioDialog.setContentView(R.layout.comentario_dialog);
	            ComentarioDialog.setCancelable(true);
	            	            
	            TextView textcoment = (TextView) ComentarioDialog.findViewById(R.id.comentario_dialog);
	            textcoment.setText(bundle.getString("TITULO"));
	            
	            final EditText editComent = (EditText)ComentarioDialog.findViewById(R.id.coment_dialog_edit);
	            
	            Button botonComent = (Button) ComentarioDialog.findViewById(R.id.comentario_dialog_button);
	            botonComent.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						ParseQuery<ParseObject> query = ParseQuery.getQuery("Receta");
	         	    	query.getInBackground(bundle.getString("ID"), new GetCallback<ParseObject>() {
	         	    	@TargetApi(Build.VERSION_CODES.GINGERBREAD) public void done(ParseObject object, ParseException e) {
	         	    		if (e == null) {
	         	    			
	         	    			if(editComent.getText().toString().isEmpty() ){
	         	    				Toast.makeText(getApplicationContext(),
											"Rellene el campo", Toast.LENGTH_SHORT).show();
	         	    			}	
	         	    			
	         	    			else{
	         	    				final ParseObject newComentario = new ParseObject("ComentarioReceta");
		         	    			newComentario.put("receta",object);
		         	    			newComentario.put("post",editComent.getText().toString());
		         	    			
		         	    			//AGARRAR UN USUARIO MIENTRAS
		         	    			ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
		    	         	    	query.getInBackground("rENqlW6Atd", new GetCallback<ParseObject>() {
		    	         	    	public void done(ParseObject object, ParseException e) {
		    	         	    		if (e == null) {
		    	         	    			newComentario.put("usuario",object);
		    	         	    			newComentario.saveInBackground(new SaveCallback() {
		    									
		    									@Override
		    									public void done(ParseException arg0) {
		    										Toast.makeText(getApplicationContext(),
		    												"Comentario añadido", Toast.LENGTH_SHORT).show();
		    										
		    									}});
		    	         	    		}
		    	        	    	}});
		    	         	    	
		    	         	    	ComentarioDialog.dismiss();
	         	    			}
	         	    			
	         	    		}
	        	    	}});
						
					}});
	            ComentarioDialog.show();
	            
	        	Toast.makeText(getApplicationContext(), "COMENTAR", Toast.LENGTH_SHORT).show();
	            return true;
	            
	        //EDITAR    
	        case R.id.menu2_editar:
	        	Toast.makeText(getApplicationContext(), "EDITAR", Toast.LENGTH_SHORT).show();
	            return true;
	            
	        //ELIMINAR    
	        case R.id.menu2_eliminar:
	        	 ElimDialog = new Dialog(this,R.style.FullHeightDialog);
		         ElimDialog.setContentView(R.layout.eliminar_dialog);
		         ElimDialog.setCancelable(true);
	        	
	        	
	        	//ELIMINAR DESDE FAVORITAS
	        	if(bundle.getString("CLAVE").equals("fav")){
	        		
	        		ParseQuery<ParseObject> queryElim = ParseQuery.getQuery("Receta");
	        		queryElim.fromLocalDatastore();
        			queryElim.getInBackground(bundle.getString("ID"), new GetCallback<ParseObject>() {
        			public void done(ParseObject object, ParseException e) {
        			     if (e == null) {
        			    	 object.unpinInBackground(null);
        			    	 Toast.makeText(getApplicationContext(), bundle.getString("TITULO")+ 
        			    			 " ya no \n pertence a Favoritas" ,Toast.LENGTH_SHORT).show();
        			    	 onBackPressed();
        			    	 Intent volver = new Intent(getApplicationContext(),MainActivity.class);
        			    	 startActivity(volver);
    				     } 
        			}});
	        				
	        	}
	        	//ELIMINAR DESDE RECETA
	        	else{
	        		
	        		final boolean login = true;
	        		//COMPROBAR LOGIN
	        		if(login){
	        			ParseQuery<ParseObject> queryElimOnLine = ParseQuery.getQuery("Receta");
	        			queryElimOnLine.getInBackground(bundle.getString("ID"), new GetCallback<ParseObject>() {
	        			public void done(final ParseObject object, ParseException e) {
	        			     if (e == null) {
	        			    	 
	        			    	 //COMPROBAR SI ES EL CREADOR
	        			    	 if(!login){
	        			    		
        					         ElimDialog.show();
        					         
        					         Button aceptar = (Button)ElimDialog.findViewById(R.id.Elim_aceptar);
        					         aceptar.setOnClickListener(new OnClickListener() {
										
										@Override
										public void onClick(View v) {
											object.deleteInBackground(new DeleteCallback() {
												
												@Override
												public void done(ParseException arg0) {
													Toast.makeText(getApplicationContext(),
															"Eliminando \n"+bundle.getString("TITULO"), Toast.LENGTH_SHORT).show();
													
										}});
											
		        			    		Intent volver = new Intent(getApplicationContext(),MainActivity.class);
			        			    	startActivity(volver);
											
									 }});
        					         
        					         Button cancelar = (Button)ElimDialog.findViewById(R.id.Elim_cancelar);
        					         cancelar.setOnClickListener(new OnClickListener() {
										
										@Override
										public void onClick(View v) {
											ElimDialog.dismiss();
											
									 }});
	        			    		
		        			    	 
	     	        			}else{
	     	        				int denuncias = object.getInt("denuncias");
	     	        				if(denuncias == 9){
	     	        					object.deleteInBackground(new DeleteCallback() {
											
											@Override
											public void done(ParseException arg0) {
												Toast.makeText(getApplicationContext(),
														"Eliminando \n"+bundle.getString("TITULO"), Toast.LENGTH_SHORT).show();
												
										}});
	     	        					
	     	        					Intent volver = new Intent(getApplicationContext(),MainActivity.class);
			        			    	startActivity(volver);
			        			    	
	     	        				}else{
	     	        					object.put("denuncias", denuncias + 1);
	     	        					object.saveInBackground(new SaveCallback() {
											
											@Override
											public void done(ParseException arg0) {
												Toast.makeText(getApplicationContext(),
														"Ha denunciado a \n" + bundle.getString("TITULO"), Toast.LENGTH_SHORT).show();
												
										}});
	     	        				}
	     	        			}
	        			    	 
	    				     } 
	        			}});
	        			
	        			
	        		}else{
	        			Toast.makeText(getApplicationContext(),
	        					"Debe iniciar sesión", Toast.LENGTH_SHORT).show();
	        		}
	        	}
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
