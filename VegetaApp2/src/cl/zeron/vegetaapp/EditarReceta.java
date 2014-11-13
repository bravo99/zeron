package cl.zeron.vegetaapp;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EditarReceta extends Activity {
	private Bundle bundle;
	private ImageButton addIng;
	final String[] categorias_medida = {"Taza", "Cucharada", "Cucharadita", "Unidad", "Gramos", "Otros" };
	final String[] categorias = {"Aperitivo","Ensalada","Pastas","Postres","Principal","Sopa","Salsas" };
	final String[] personas = {"1","2","3","4","5","6","7","8"};
	ArrayAdapter<String> adapSpinnerCat;
	ArrayAdapter<String> adapSpinnerPer;
	private LinearLayout mLinear;
	private Spinner origen;
	private String name = "";
	ImageView iv;
	Button btnAction;
	private ArrayList<IngredienteCantidad> lista;
	private static int TAKE_PICTURE = 1;
	private static int SELECT_PICTURE = 2;
	final String[] origenes = {"Camara","Galeria"};
	private GlobalClass globalClass;
	private ParseObject editReceta;
	private ArrayList<IngredienteCantidad> lista_ingrediente;
	List<ParseObject> lista_original;
	private Dialog dialogIngrediente, dialogAlimento;
	private EditText et_titulo, et_tiempo, et_preparacion;
	private Spinner sp_personas,sp_categoria;
	private String titulo_original, preparacion_original, categoria_original, tiempo_original, personas_original;
	private Bitmap mBitmap, bitmapImagen;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.nueva_receta);
		globalClass = (GlobalClass) getApplicationContext();
		bundle = getIntent().getExtras();
		titulo_original = bundle.getString("TITULO");
		preparacion_original = bundle.getString("PREPARACION");
		personas_original = bundle.getString("PERSONAS");
		tiempo_original = bundle.getString("TIEMPO");
		categoria_original = bundle.getString("CATEGORIA");
		//categoria_original = bundle.getString("CATEGORIA");
		final String recetaId = bundle.getString("ID");
		iv = (ImageView)findViewById(R.id.image_receta);
		iv.setImageBitmap(BitmapFactory.decodeFile(bundle.getString("IMAG")));
		mBitmap = BitmapFactory.decodeFile(bundle.getString("IMAG"));
		mLinear = (LinearLayout) findViewById(R.id.linear_listview);
		et_titulo = (EditText) findViewById(R.id.tit_receta);
		et_titulo.setText(bundle.getString("TITULO"), TextView.BufferType.EDITABLE);
		et_tiempo = (EditText) findViewById(R.id.tiempo_receta);
		et_tiempo.setText(bundle.getString("TIEMPO"), TextView.BufferType.EDITABLE);
		String cant_personas = bundle.getString("PERSONAS");
		et_preparacion = (EditText) findViewById(R.id.prep_receta);
		et_preparacion.setText(bundle.getString("PREPARACION"), TextView.BufferType.EDITABLE);
		sp_personas = (Spinner) findViewById(R.id.personas_receta);
		
		adapSpinnerCat = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, categorias);
		adapSpinnerCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		adapSpinnerPer = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, personas);
		adapSpinnerPer.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_personas.setAdapter(adapSpinnerPer);
		sp_personas.setSelection(adapSpinnerPer.getPosition(cant_personas));
		sp_categoria = (Spinner) findViewById(R.id.categoria_receta);
		sp_categoria.setAdapter(adapSpinnerCat);
		sp_categoria.setSelection(adapSpinnerCat.getPosition(categoria_original));
		
		addIng = (ImageButton)findViewById(R.id.add_Ingrediente);
		addIng.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				agregarIngrediente();
				
		}});
		origen=(Spinner)findViewById(R.id.sp_orig_cam);
		ArrayAdapter<String> adaptadorSpinner2 =new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, origenes);
		adaptadorSpinner2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		origen.setAdapter(adaptadorSpinner2);
		
		name = Environment.getExternalStorageDirectory() + "/test.jpg";
		btnAction=(Button)findViewById(R.id.b_receta);
		
		ParseQuery<ParseObject> recetaquery = ParseQuery.getQuery("Receta");
		recetaquery.getInBackground(recetaId, new GetCallback<ParseObject>() {
			
			@Override
			public void done(ParseObject receta, ParseException e) {
				editReceta = receta;
				lista_ingrediente = new ArrayList<IngredienteCantidad>();
				if(e==null){
					ParseQuery<ParseObject> alimentoRecetaquery = ParseQuery.getQuery("AlimentoReceta");
					alimentoRecetaquery.include("ingrediente");
					alimentoRecetaquery.whereEqualTo("receta", editReceta);
					alimentoRecetaquery.findInBackground(new FindCallback<ParseObject>() {
						
						@Override
						public void done(List<ParseObject> ingredienteList, ParseException e) {
							lista_original = ingredienteList;
							ParseObject alimento;
							for(ParseObject ing : ingredienteList){
								alimento = ing.getParseObject("ingrediente");
								IngredienteCantidad ingCant = new IngredienteCantidad();
								ingCant.setId(alimento.getObjectId());
								ingCant.setNombre(alimento.getString("nombre"));
								ingCant.setCantidad(ing.getString("cantidad"));
								ingCant.setMedida(ing.getString("medida"));
								lista_ingrediente.add(ingCant);
							}
							Comparator<IngredienteCantidad> comparator =  new Comparator<IngredienteCantidad>() {
								
								@Override
								public int compare(IngredienteCantidad a, IngredienteCantidad b) {
									int resultado = a.getNombre().compareTo(b.getNombre());
									if(resultado!=0){
										return resultado;
									}
									return resultado;
								}
							};
							
							Collections.sort(lista_ingrediente, comparator);
							globalClass.setListaIngrediente(lista_ingrediente);
							for(IngredienteCantidad ingrediente: lista_ingrediente){
								
								LayoutInflater inflater = null;
			                     inflater = (LayoutInflater) getApplicationContext()
			                                  .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			                     final View mLinearView = inflater.inflate(R.layout.item_alimento_ingrediente,null);
			                     TextView mNombre = (TextView) mLinearView.findViewById(R.id.nombreIng);
			                     TextView mCantidad = (TextView) mLinearView.findViewById(R.id.cantidadIng);
			                     TextView mId = (TextView) mLinearView.findViewById(R.id.id);
			                     TextView mMedida = (TextView) mLinearView.findViewById(R.id.medidaIng);
			                     ImageButton btnEliminar = (ImageButton) mLinearView.findViewById(R.id.ib_delete);
			                     mId.setText(ingrediente.getId());
			                     final String id_alimento = mId.getText().toString();
			                     mNombre.setText(ingrediente.getNombre());
			                     mCantidad.setText(ingrediente.getCantidad());
			                     mMedida.setText(ingrediente.getMedida());
			                     final String cantidad = mCantidad.getText().toString();
			                     final String medida = mMedida.getText().toString();
			                     final String nombre = mNombre.getText().toString();
			                     mLinear.addView(mLinearView);
			                     btnEliminar.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										btnEliminarAlimento(id_alimento);
										
									}
								});
			                     mLinearView.setOnClickListener(new OnClickListener() {

			                           @Override
			                           public void onClick(View v) {
			                        	   mostrarIngrediente(id_alimento, nombre, cantidad, medida);
			                           }
			                     });
								
							}
							
						}
					});
				}
			}
		});
		
		btnAction.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/**
       			 * Obtenemos los botones de imagen completa y de galer�a para revisar su estatus
       			 * m�s adelante
       			 */
       			//RadioButton rbtnFull = (RadioButton)findViewById(R.id.radbtnFull);
       			
       		//	RadioButton rbtnGallery = (RadioButton)findViewById(R.id.radbtnGall);
       			
       			/**
       			 * Para todos los casos es necesario un intent, si accesamos la c�mara con la acci�n
       			 * ACTION_IMAGE_CAPTURE, si accesamos la galer�a con la acci�n ACTION_PICK. 
       			 * En el caso de la vista previa (thumbnail) no se necesita m�s que el intent, 
       			 * el c�digo e iniciar la actividad. Por eso inicializamos las variables intent y
       			 * code con los valores necesarios para el caso del thumbnail, as� si ese es el
       			 * bot�n seleccionado no validamos nada en un if. 
       			 */
       			Intent intent =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
       			int code = TAKE_PICTURE;
       			
       			/**
       			 * Si la opci�n seleccionada es fotograf�a completa, necesitamos un archivo donde
       			 * guardarla
       			 */
       			if (origen.getSelectedItem().equals(origenes[0])) {					
       				Uri output = Uri.fromFile(new File(name));
       		    	intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
       			/**
       			 * Si la opci�n seleccionada es ir a la galer�a, el intent es diferente y el c�digo
       			 * tambi�n, en la consecuencia de que est� chequeado el bot�n de la galer�a se hacen
       			 * esas asignaciones
       			 */       		    	
       			} else if (origen.getSelectedItem().equals(origenes[1])){       				
       				intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
       				code = SELECT_PICTURE;
       			}
       			
       			/**
       			 * Luego, con todo preparado iniciamos la actividad correspondiente.
       			 */
       			startActivityForResult(intent, code);
				
			}
		});	
		
		
		
	}

	protected void agregarIngrediente() {
		dialogAlimento = new Dialog(EditarReceta.this, R.style.Theme_Dialog_Translucent);
		dialogAlimento.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogAlimento.setContentView(R.layout.dialog_lista_alimentos);
		dialogAlimento.setCanceledOnTouchOutside(false);
		dialogAlimento.show();
		ListView listView = (ListView) dialogAlimento.findViewById(R.id.lv_alimentos);
		AdapterAlimentoIngrediente adapter = new AdapterAlimentoIngrediente(EditarReceta.this, mLinear);
		listView.setAdapter(adapter);
		adapter.loadObjects();
	}
	public void btnEliminarAlimento(String id){
		ArrayList<IngredienteCantidad> lista = globalClass.getListaIngrediente();
		ArrayList<IngredienteCantidad> lista_nueva = new ArrayList<IngredienteCantidad>();
		if(lista != null){
			for(IngredienteCantidad prod : lista){
				if(!id.equals(prod.getId())){
					lista_nueva.add(prod);
				}
			}
			globalClass.setListaIngrediente(lista_nueva);
			Comparator<IngredienteCantidad> comparador = new Comparator<IngredienteCantidad>() {
				
				@Override
				public int compare(IngredienteCantidad a, IngredienteCantidad b) {
					int resultado = a.getNombre().compareTo(b.getNombre());
					if(resultado!=0){
						return resultado;
					}
					return resultado;
				}
			};
			Collections.sort(lista_nueva, comparador);
			mLinear.removeAllViews();
			for(IngredienteCantidad ingrediente: lista_nueva){
				
				LayoutInflater inflater = null;
                 inflater = (LayoutInflater) getApplicationContext()
                              .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                 final View mLinearView = inflater.inflate(R.layout.item_alimento_ingrediente,null);
                 TextView mNombre = (TextView) mLinearView.findViewById(R.id.nombreIng);
                 TextView mCantidad = (TextView) mLinearView.findViewById(R.id.cantidadIng);
                 TextView mId = (TextView) mLinearView.findViewById(R.id.id);
                 TextView mMedida = (TextView) mLinearView.findViewById(R.id.medidaIng);
                 ImageButton btnEliminar = (ImageButton) mLinearView.findViewById(R.id.ib_delete);
                 mId.setText(ingrediente.getId());
                 final String id_alimento = mId.getText().toString();
                 mNombre.setText(ingrediente.getNombre());
                 mCantidad.setText(ingrediente.getCantidad());
                 mMedida.setText(ingrediente.getMedida());
                 final String cantidad = mCantidad.getText().toString();
                 final String medida = mMedida.getText().toString();
                 final String nombre = mNombre.getText().toString();
                 mLinear.addView(mLinearView);
                 btnEliminar.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						btnEliminarAlimento(id_alimento);
						
					}
				});
                 mLinearView.setOnClickListener(new OnClickListener() {

                       @Override
                       public void onClick(View v) {
                    	   mostrarIngrediente(id_alimento, nombre, cantidad, medida);
                       }
                 });
				
			}
			
			
		}
	}
	
	public void mostrarIngrediente(final String id_alimento, final String nombre, String cantidad, String medida){
		dialogIngrediente = new Dialog(EditarReceta.this, R.style.Theme_Dialog_Translucent);
		dialogIngrediente.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogIngrediente.setContentView(R.layout.ingredientereceta);
		dialogIngrediente.setCanceledOnTouchOutside(false);
		dialogIngrediente.show();
		lista = new ArrayList<IngredienteCantidad>();
		TextView tv_nombre = (TextView) dialogIngrediente.findViewById(R.id.nomIngred);
		tv_nombre.setText(nombre);
		final EditText et_cantidad = (EditText) dialogIngrediente.findViewById(R.id.etCantidad);
		et_cantidad.setText(cantidad, TextView.BufferType.EDITABLE);
		final Spinner sp_medida = (Spinner) dialogIngrediente.findViewById(R.id.spMedida);
		adapSpinnerCat =new ArrayAdapter<String>(EditarReceta.this ,android.R.layout.simple_spinner_item, categorias_medida);
		adapSpinnerCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_medida.setAdapter(adapSpinnerCat);
		sp_medida.setSelection(adapSpinnerCat.getPosition(medida));
		((Button) dialogIngrediente.findViewById(R.id.aceptar)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(et_cantidad.getText().toString().equals("")){
					Toast.makeText(EditarReceta.this, "Por favor ingrese la cantidad", Toast.LENGTH_SHORT).show();
					return;
				}
				String cantidad_nueva = et_cantidad.getText().toString(); 
				ArrayList<IngredienteCantidad> nueva_lista = new ArrayList<IngredienteCantidad>();
				lista = globalClass.getListaIngrediente();
				int position = sp_medida.getSelectedItemPosition();
				String medida_nueva = categorias_medida[position];
				for(IngredienteCantidad ingrediente : lista){
					if(ingrediente.getId().equals(id_alimento)){
						ingrediente.setCantidad(cantidad_nueva);
						ingrediente.setMedida(medida_nueva);
						nueva_lista.add(ingrediente);
					}
					else{
						nueva_lista.add(ingrediente);
					}
				}
				globalClass.setListaIngrediente(nueva_lista);
				mLinear.removeAllViews();
				for(IngredienteCantidad ing : nueva_lista){
					LayoutInflater inflater = null;
	                 inflater = (LayoutInflater) getApplicationContext()
	                              .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	                 final View mLinearView = inflater.inflate(R.layout.item_alimento_ingrediente,null);
	                 TextView mNombre = (TextView) mLinearView.findViewById(R.id.nombreIng);
	                 TextView mCantidad = (TextView) mLinearView.findViewById(R.id.cantidadIng);
	                 TextView mId = (TextView) mLinearView.findViewById(R.id.id);
	                 TextView mMedida = (TextView) mLinearView.findViewById(R.id.medidaIng);
	                 ImageButton btnEliminar = (ImageButton) mLinearView.findViewById(R.id.ib_delete);
	                 mId.setText(ing.getId());
	                 final String id_alimento = mId.getText().toString();
	                 mNombre.setText(ing.getNombre());
	                 mCantidad.setText(ing.getCantidad());
	                 mMedida.setText(ing.getMedida());
	                 final String cantidad = mCantidad.getText().toString();
	                 final String medida = mMedida.getText().toString();
	                 final String nombre = mNombre.getText().toString();
	                 mLinear.addView(mLinearView);
	                 btnEliminar.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							btnEliminarAlimento(id_alimento);
							
						}
					});
	                 mLinearView.setOnClickListener(new OnClickListener() {

	                       @Override
	                       public void onClick(View v) {
	                    	   mostrarIngrediente(id_alimento, nombre, cantidad, medida);
	                       }
	                 });
				}
				
				
				dialogIngrediente.dismiss();
			}
		});
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.editar_local_activity_action, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.accept:
			aceptarEdicion();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	public void aceptarEdicion(){
		String titulo, preparacion, personas, tiempo, categoria;
		final ArrayList<IngredienteCantidad> lista_final = globalClass.getListaIngrediente();
		titulo = et_titulo.getText().toString();
		preparacion = et_preparacion.getText().toString();
		tiempo = et_tiempo.getText().toString();
		categoria = sp_categoria.getSelectedItem().toString();
		personas = sp_personas.getSelectedItem().toString();
		if(titulo.isEmpty() || preparacion.isEmpty() || tiempo.isEmpty()){
			Toast.makeText(EditarReceta.this, "Complete todos los campos por favor", Toast.LENGTH_LONG).show();
			return;
		}
		if(bitmapImagen != null){
			Bitmap mBitmapScaled = Bitmap.createScaledBitmap(bitmapImagen, 500, 500 * bitmapImagen.getHeight() / bitmapImagen.getWidth(), false);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			//rotatedScaledMealImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
			mBitmapScaled.compress(Bitmap.CompressFormat.JPEG, 100, stream);
			
			//mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
			byte[] data = stream.toByteArray();    
			ParseFile imageFile = new ParseFile("image.jpg", data);
			imageFile.saveInBackground();
			editReceta.put("imagen", imageFile);
		}
		
		if(!titulo.equals(titulo_original)){
			editReceta.put("nombre", titulo);
		}
		if(!personas.equals(personas_original)){
			editReceta.put("personas", personas);
		}
		if(!preparacion.equals(preparacion_original)){
			editReceta.put("preparacion", preparacion);
		}
		if(!tiempo.equals(tiempo_original)){
			editReceta.put("tiempo_preparacion", tiempo);
		}
		editReceta.put("categoria", categoria);
		
		
		
		editReceta.saveInBackground(new SaveCallback() {
			
			@Override
			public void done(ParseException e) {
				if(e==null){
					for(IngredienteCantidad ing : lista_final){
						boolean isIn = false;
						boolean isEdited = false;
						for(ParseObject object : lista_original){
							ParseObject alimento = object.getParseObject("ingrediente");
							if(alimento.getObjectId().equals(ing.getId())){
								if(!ing.getCantidad().equals(object.getString("cantidad"))){
									object.put("cantidad", ing.getCantidad());
									isEdited=true;
								}
								if(!ing.getMedida().equals(object.getString("medida"))){
									object.put("medida", ing.getMedida());
									isEdited=true;
								}
								isIn = true;
								if(isEdited){
									object.saveInBackground();
								}
								break;
							}
						}
						if(!isIn){
							ParseObject nuevo_ingrediente = new ParseObject("AlimentoReceta");
							nuevo_ingrediente.put("receta", editReceta);
							nuevo_ingrediente.put("ingrediente", ParseObject.createWithoutData("Alimento", ing.getId()));
							nuevo_ingrediente.put("medida", ing.getMedida());
							nuevo_ingrediente.put("cantidad", ing.getCantidad());
							nuevo_ingrediente.saveInBackground();
						}
					}
					
					for(ParseObject object : lista_original){
						ParseObject alimento = object.getParseObject("ingrediente");
						String id_alimento = alimento.getObjectId();
						Boolean exist = false;
						for(IngredienteCantidad ing_delete : lista_final){
							if(ing_delete.getId().equals(id_alimento)){
								exist = true;
								break;
							}
						}
						if(!exist){
							object.deleteInBackground();
						}
					}
					Intent intent = new Intent(EditarReceta.this, MainActivity.class);
					startActivity(intent);
					
				}
			}
		});
		
	}
	
	 @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    	if (requestCode == TAKE_PICTURE) {
	    		switch(resultCode){
	    			case Activity.RESULT_OK:
	    	    		if (data != null) {
	    	    			if (data.hasExtra("data")) {
	    	    				bitmapImagen = (Bitmap) data.getParcelableExtra("data");
	    	    				iv.setImageBitmap(bitmapImagen);
	    	    				
	    	    			}
	    	    		/**
	    	    		 * De lo contrario es una imagen completa
	    	    		 */    			
	    	    		} else {
	    	    			bitmapImagen = BitmapFactory.decodeFile(name);
	    	    			iv.setImageBitmap(bitmapImagen);
	    	    			/**
	    	    			 * Para guardar la imagen en la galer�a, utilizamos una conexi�n a un MediaScanner
	    	    			 */
	    	    			new MediaScannerConnectionClient() {
	    	    				private MediaScannerConnection msc = null; {
	    	    					msc = new MediaScannerConnection(getApplicationContext(), this); msc.connect();
	    	    				}
	    	    				public void onMediaScannerConnected() { 
	    	    					msc.scanFile(name, null);
	    	    				}
	    	    				public void onScanCompleted(String path, Uri uri) { 
	    	    					msc.disconnect();
	    	    				} 
	    	    			};				
	    	    		}
	    				break;
	    				
	    			case Activity.RESULT_CANCELED:
	    				bitmapImagen = null;
	    				if(mBitmap != null){
	    					iv.setImageBitmap(mBitmap);
	    				}
	    				else{
	    					iv.setImageResource(R.drawable.ic_launcher);
	    				}
	    				
	    				break;
	    		}
	    		
	    	/**
	    	 * Recibimos el URI de la imagen y construimos un Bitmap a partir de un stream de Bytes
	    	 */
	    	} else if (requestCode == SELECT_PICTURE){
	    		switch(resultCode){
					case Activity.RESULT_OK:
			    		Uri selectedImage = data.getData();
			    		InputStream is;
			    		try {
			    			is = getContentResolver().openInputStream(selectedImage);
			    	    	BufferedInputStream bis = new BufferedInputStream(is);
			    	    	Bitmap bitmap = BitmapFactory.decodeStream(bis); 
			    	    	
			    	    	iv.setImageBitmap(bitmap);	
			 
			    		} catch (FileNotFoundException e) {}
					case Activity.RESULT_CANCELED:
						bitmapImagen = null;
						if(mBitmap != null){
							iv.setImageBitmap(mBitmap);
						}
						else{
							iv.setImageResource(R.drawable.ic_launcher);
						}
	    		}
	    	}
	    }
	
}
