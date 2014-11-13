package cl.zeron.vegetaapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class RecetaResultListActivity extends ActionBarActivity{
	
	@SuppressWarnings("unused")
	private CharSequence mTitle;
	TextView titulo;
	TextView preparacion, descripcion;
	TextView tiempo;
	TextView personas;
	TextView valoracion;
	TextView categoria;
	private EditText et_comentario;
	private Button btn_comentar;
	ParseImageView imagen;
	Bundle bundle;
	protected ProgressDialog proDialog;
	private Dialog rankDialog;
	private Dialog ComentarioDialog;
	protected Dialog ElimDialog;
	private ParseUser currentUser;
	private LinearLayout listaIngredientes, listaComentarios;
	private ImageView flechaDescripcion, flechaIngredientes, flechaPreparacion, flechaComentarios;
	private String id_receta;
	

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.receta_result_list);
		
		bundle = getIntent().getExtras();
		
		et_comentario = (EditText) findViewById(R.id.et_comentario);
		btn_comentar = (Button) findViewById(R.id.btn_add);
		
		categoria = (TextView) findViewById(R.id.tv_categoria);
		categoria.setText(bundle.getString("CATEGORIA"));
		flechaDescripcion = (ImageView) findViewById(R.id.flecha_descripcion);
		flechaIngredientes = (ImageView) findViewById(R.id.flecha_ingrediente);
		flechaPreparacion = (ImageView) findViewById(R.id.flecha_preparacion);
		flechaComentarios = (ImageView) findViewById(R.id.flecha_comentarios);
		descripcion = (TextView) findViewById(R.id.tv_descripcion);
		descripcion.setText(bundle.getString("DESCRIPCION"));
		id_receta = bundle.getString("ID");
		listaIngredientes = (LinearLayout) findViewById(R.id.linear_listview_ingredientes);
		listaComentarios = (LinearLayout) findViewById(R.id.linear_listview_comentarios);
		
		cargarIngredientes(id_receta);
		cargarComentarios(id_receta);
		
		currentUser = ParseUser.getCurrentUser();
		if(currentUser == null){
			et_comentario.setVisibility(View.GONE);
			btn_comentar.setVisibility(View.GONE);
		}
		
		titulo = (TextView)findViewById(R.id.textView1);
		titulo.setText(bundle.getString("TITULO"));
		
		imagen = (ParseImageView)findViewById(R.id.iv_imag_receta);
		//imagen.setImageBitmap(bitmap)
		imagen.setImageBitmap(BitmapFactory.decodeFile(bundle.getString("IMAG")));
		
		preparacion = (TextView)findViewById(R.id.textView2);
		preparacion.setText(bundle.getString("PREPARACION"));
		
		tiempo = (TextView)findViewById(R.id.tiempo);
		tiempo.setText(bundle.getString("TIEMPO")+" [min]");
		
		personas = (TextView)findViewById(R.id.personas);
		personas.setText(bundle.getString("PERSONAS")+"[pers]");
		
		valoracion = (TextView)findViewById(R.id.valoracion);
		valoracion.setText(bundle.getString("VALORACION")+"/5");
		
		
		
		mTitle = getTitle(); // Get current title
		
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setTitle(bundle.getString("TITULO"));
	
	}
	
	public void cargarIngredientes(String id){
		ParseQuery<ParseObject> alimentoRecetaquery = ParseQuery.getQuery("AlimentoReceta");
		alimentoRecetaquery.include("ingrediente");
		alimentoRecetaquery.whereEqualTo("receta", ParseObject.createWithoutData("Receta", id));
		alimentoRecetaquery.findInBackground(new FindCallback<ParseObject>() {
			
			@Override
			public void done(List<ParseObject> lista_ingredientes, ParseException e) {
				ArrayList<IngredienteCantidad> lista = new ArrayList<IngredienteCantidad>();
				if(e==null){
					for(ParseObject object : lista_ingredientes){
						IngredienteCantidad ing = new IngredienteCantidad();
						ParseObject alimento = object.getParseObject("ingrediente");
						String id_alimento = alimento.getObjectId();
						ing.setId(id_alimento);
						ing.setNombre(alimento.getString("nombre"));
						ing.setCantidad(object.getString("cantidad"));
						ing.setMedida(object.getString("medida"));
						lista.add(ing);
					}
					Comparator<IngredienteCantidad> comparator = new Comparator<IngredienteCantidad>() {
						
						@Override
						public int compare(IngredienteCantidad a, IngredienteCantidad b) {
							int resultado = a.getNombre().compareTo(b.getNombre());
							if(resultado!=0){
								return resultado;
							}
							return resultado;
						}
					};
					Collections.sort(lista, comparator);
					for(IngredienteCantidad ingrediente: lista){
						
						LayoutInflater inflater = null;
	                     inflater = (LayoutInflater) getApplicationContext()
	                                  .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	                     
	                     final View mLinearView = inflater.inflate(R.layout.item_ingredientes,null);
	                     TextView mNombre = (TextView) mLinearView.findViewById(R.id.nombreIng);
	                     TextView mCantidad = (TextView) mLinearView.findViewById(R.id.cantidadIng);
	                     TextView mId = (TextView) mLinearView.findViewById(R.id.id);
	                     TextView mMedida = (TextView) mLinearView.findViewById(R.id.medidaIng);
	                     mId.setText(ingrediente.getId());
	                     final String id_alimento = mId.getText().toString();
	                     mNombre.setText(ingrediente.getNombre());
	                     mCantidad.setText(ingrediente.getCantidad());
	                     mMedida.setText(ingrediente.getMedida());
	                     listaIngredientes.addView(mLinearView);
	                     mLinearView.setOnClickListener(new OnClickListener() {

	                           @Override
	                           public void onClick(View v) {
	                        	   Intent intent = new Intent(RecetaResultListActivity.this, AlimentoResultListActivity.class);
	                        	   intent.putExtra("ID",id_alimento);
	                        	   startActivity(intent);
	                           }
	                     });
						
					}
					
					
				}
				
			}
		});
		
	}
	
	public void cargarComentarios(String id){
		ParseQuery<ParseObject> comentarioquery = ParseQuery.getQuery("ComentarioReceta");
		comentarioquery.include("usuario");
		comentarioquery.orderByDescending("updateAt");
		comentarioquery.whereEqualTo("receta", ParseObject.createWithoutData("Receta", id));
		comentarioquery.findInBackground(new FindCallback<ParseObject>() {
			
			@Override
			public void done(List<ParseObject> lista_comentarios, ParseException e) {
				if(e==null){
					for(ParseObject com : lista_comentarios){
						ParseUser user = com.getParseUser("usuario");
						LayoutInflater inflater = null;
	                     inflater = (LayoutInflater) getApplicationContext()
	                                  .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	                     
	                     final View mLinearView = inflater.inflate(R.layout.item_comentarios,null);
	                     TextView tv_user = (TextView) mLinearView.findViewById(R.id.user);
	                     TextView tv_comentario = (TextView) mLinearView.findViewById(R.id.comentario);
	                     tv_user.setText(user.getString("name"));
	                     tv_comentario.setText(com.getString("post"));
	                     listaComentarios.addView(mLinearView);
					}
				}
			}
		});
		
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
	    			object.pinInBackground(new SaveCallback() {
						
						@Override
						public void done(ParseException arg0) {
							Toast.makeText(getApplicationContext(),
									"Nueva receta añadida a Favoritas", Toast.LENGTH_SHORT).show();
							
					}});
	    			
	    			
	    		} 
	    	}});
	        	
	        return true;
	        
	        //CALIFICAR
	        case R.id.menu2_calificar:
	        	if(currentUser == null){
	        		Toast.makeText(RecetaResultListActivity.this, "Debe estar registrado para Calificar", Toast.LENGTH_SHORT).show();
	        		return false;
	        	}
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
	         	    			valoracion.setText( Integer.toString(valfin) +"/5");
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
	        	if(currentUser == null){
	        		Toast.makeText(RecetaResultListActivity.this, "Debe estar registrado para Comentar", Toast.LENGTH_SHORT).show();
	        		return false;
	        	}
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
		         	    			newComentario.put("usuario",currentUser);
    	         	    			newComentario.saveInBackground(new SaveCallback() {
    									
    									@Override
    									public void done(ParseException arg0) {
    										Toast.makeText(getApplicationContext(),
    												"Comentario añadido", Toast.LENGTH_SHORT).show();
    										
    									}});
		    	         	    	
		    	         	    	ComentarioDialog.dismiss();
	         	    			}
	         	    			
	         	    		}
	        	    	}});
						
					}});
	            ComentarioDialog.show();
	            
	            return true;
	            
	        //EDITAR    
	        case R.id.menu2_editar:
	        	if(currentUser == null){
	        		Toast.makeText(RecetaResultListActivity.this, "Debe estar registrado para Editar", Toast.LENGTH_SHORT).show();
	        		return false;
	        	}
	        	Intent intent = new Intent(RecetaResultListActivity.this, EditarReceta.class);
	        	intent.putExtras(bundle);
	        	startActivity(intent);
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
        			    	 object.unpinInBackground();
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
	        		if(currentUser == null){
		        		Toast.makeText(RecetaResultListActivity.this, "Debe estar registrado para Eliminar", Toast.LENGTH_SHORT).show();
		        		return false;
		        	}
	        		//COMPROBAR LOGIN
	        		if(currentUser != null){
	        			ParseQuery<ParseObject> queryElimOnLine = ParseQuery.getQuery("Receta");
	        			queryElimOnLine.getInBackground(bundle.getString("ID"), new GetCallback<ParseObject>() {
	        			public void done(final ParseObject object, ParseException e) {
	        			     if (e == null) {
	        			    	 
	        			    	 //COMPROBAR SI ES EL CREADOR
	        			    	 ParseUser creador = object.getParseUser("creado_por");
	        			    	 if(creador.getObjectId().equals(currentUser.getObjectId())){
	        			    		
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
	
	public void clickDescripcion(View v){
		int visibilidad = descripcion.getVisibility();
		
		switch (visibilidad) {
		case View.GONE:
			descripcion.setVisibility(View.VISIBLE);
			flechaDescripcion.setImageResource(R.drawable.flecha_abajo);
			
			break;
		case View.VISIBLE:
			descripcion.setVisibility(View.GONE);
			flechaDescripcion.setImageResource(R.drawable.flecha_lado);
			break;

		default:
			break;
		}
		
	}
	public void clickIngrediente(View v){
		int visibilidad = listaIngredientes.getVisibility();
		
		switch (visibilidad) {
		case View.GONE:
			listaIngredientes.setVisibility(View.VISIBLE);
			flechaIngredientes.setImageResource(R.drawable.flecha_abajo);
			
			break;
		case View.VISIBLE:
			listaIngredientes.setVisibility(View.GONE);
			flechaIngredientes.setImageResource(R.drawable.flecha_lado);
			break;

		default:
			break;
		}
		
	}
	
	public void clickPreparacion(View v){
		int visibilidad = preparacion.getVisibility();
		
		switch (visibilidad) {
		case View.GONE:
			preparacion.setVisibility(View.VISIBLE);
			flechaPreparacion.setImageResource(R.drawable.flecha_abajo);
			
			break;
		case View.VISIBLE:
			preparacion.setVisibility(View.GONE);
			flechaPreparacion.setImageResource(R.drawable.flecha_lado);
			break;

		default:
			break;
		}
		
	}
	
	public void clickComentarios(View v){
		int visibilidad = listaComentarios.getVisibility();
		
		switch (visibilidad) {
		case View.GONE:
			listaComentarios.setVisibility(View.VISIBLE);
			flechaComentarios.setImageResource(R.drawable.flecha_abajo);
			if(currentUser !=null){
				et_comentario.setVisibility(View.VISIBLE);
				btn_comentar.setVisibility(View.VISIBLE);
			}
			
			break;
		case View.VISIBLE:
			listaComentarios.setVisibility(View.GONE);
			flechaComentarios.setImageResource(R.drawable.flecha_lado);
			if(currentUser !=null){
				et_comentario.setVisibility(View.GONE);
				btn_comentar.setVisibility(View.GONE);
			}
			break;

		default:
			break;
		}
		
	}
	
	public void btnComentar(View view){
		final String comentario = et_comentario.getText().toString();
		if(comentario.isEmpty()){
			Toast.makeText(RecetaResultListActivity.this, "Ingrese un comentario", Toast.LENGTH_SHORT).show();
			return;
		}
		ParseObject comentarioReceta = new ParseObject("ComentarioReceta");
		comentarioReceta.put("receta", ParseObject.createWithoutData("Receta", id_receta));
		comentarioReceta.put("usuario", currentUser);
		comentarioReceta.put("post", comentario);
		comentarioReceta.saveInBackground(new SaveCallback() {
			
			@Override
			public void done(ParseException e) {
				if(e==null){
					LayoutInflater inflater = null;
                    inflater = (LayoutInflater) getApplicationContext()
                                 .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    
                    final View mLinearView = inflater.inflate(R.layout.item_comentarios,null);
                    TextView tv_user = (TextView) mLinearView.findViewById(R.id.user);
                    TextView tv_comentario = (TextView) mLinearView.findViewById(R.id.comentario);
                    tv_user.setText(currentUser.getString("name"));
                    tv_comentario.setText(comentario);
                    listaComentarios.addView(mLinearView);
                    et_comentario.setText("", TextView.BufferType.EDITABLE);
				}
			}
		});
		
	}
}
