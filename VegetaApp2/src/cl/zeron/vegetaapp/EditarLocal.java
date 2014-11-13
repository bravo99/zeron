package cl.zeron.vegetaapp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class EditarLocal extends Activity {
	private String nombre, descripcion, id;
	private ArrayList<ProductoPrecio> lista_prod;
	
	private EditText tv_titulo, tv_descripcion;
	private ParseObject editLocal;
	private Bitmap mBitmap;
	private GlobalClass globalClass;
	private List<ParseObject> lista_original;
	private Dialog dialogAlimento;
	private File imageFile;
	private MenuItem item_camara;
	private LinearLayout mLinearListView;
	private Dialog dialogPrecio;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_editar_local);
		Bundle extras = getIntent().getExtras();
		nombre= extras.getString("nombre");
		descripcion = extras.getString("descripcion");
		id = extras.getString("id");
		mLinearListView = (LinearLayout) findViewById(R.id.linear_listview);
		globalClass = (GlobalClass) getApplicationContext();
		tv_titulo = (EditText) findViewById(R.id.et_nombre);
		tv_descripcion = (EditText) findViewById(R.id.et_descripcion);
		ImageButton ib_add = (ImageButton) findViewById(R.id.ib_agregar);
		ib_add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				mostrarAlimentos();				
			}
		});
		tv_titulo.setText(nombre, TextView.BufferType.EDITABLE);
		tv_descripcion.setText(descripcion, TextView.BufferType.EDITABLE);
		lista_prod = new ArrayList<ProductoPrecio>();
		ParseQuery<ParseObject> localquery = ParseQuery.getQuery("Local");
		localquery.getInBackground(id, new GetCallback<ParseObject>() {
			
			@Override
			public void done(ParseObject objeto, ParseException e) {
				if(e==null){
					editLocal = objeto;
					cargarProductos(editLocal);
				}
			}
		});
	}
	
	protected void mostrarAlimentos() {
		dialogAlimento = new Dialog(EditarLocal.this, R.style.Theme_Dialog_Translucent);
		dialogAlimento.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogAlimento.setContentView(R.layout.dialog_lista_alimentos);
		dialogAlimento.setCanceledOnTouchOutside(false);
		dialogAlimento.show();
		
		ListView listView = (ListView) dialogAlimento.findViewById(R.id.lv_alimentos);
		AdapterAlimentos alimentosAdapter = new AdapterAlimentos(EditarLocal.this, mLinearListView, dialogAlimento);
		
		listView.setAdapter(alimentosAdapter);
		alimentosAdapter.loadObjects();
		dialogAlimento.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface arg0) {
			}
		});
		((Button) dialogAlimento.findViewById(R.id.listo)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				dialogAlimento.dismiss();
				
			}
		});
		
	}

	protected void cargarProductos(ParseObject objeto) {
		
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("AlimentoLocal");
		query.whereEqualTo("local", objeto);
		query.include("alimento");
		query.findInBackground(new FindCallback<ParseObject>() {
			
			@Override
			public void done(List<ParseObject> lista, ParseException e) {
				if(e==null){
					lista_original = lista;
					for(ParseObject objeto : lista){
						
						 /**
	                      * inflate items/ add items in linear layout instead of listview
	                      */
						ParseObject alimento = objeto.getParseObject("alimento");
						ProductoPrecio prod = new ProductoPrecio();
						prod.setId(alimento.getObjectId());
						prod.setNombre(alimento.getString("nombre"));
						prod.setPrecio(objeto.getInt("precio"));
						lista_prod.add(prod);
						
					}
					globalClass.setLista(lista_prod);
					Comparator<ProductoPrecio> comparador = new Comparator<ProductoPrecio>() {
						
						@Override
						public int compare(ProductoPrecio a, ProductoPrecio b) {
							int resultado = a.getNombre().compareTo(b.getNombre());
							if(resultado!=0){
								return resultado;
							}
							return resultado;
						}
					};
					Collections.sort(lista_prod, comparador);
					for(ProductoPrecio prod : lista_prod){
						LayoutInflater inflater = null;
	                     inflater = (LayoutInflater) getApplicationContext()
	                                  .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	                     final View mLinearView = inflater.inflate(R.layout.item_producto_precio,null);
	                     TextView mFirstName = (TextView) mLinearView.findViewById(R.id.textViewName);
	                     TextView mPrecio = (TextView) mLinearView.findViewById(R.id.textViewPrecio);
	                     TextView mId = (TextView) mLinearView.findViewById(R.id.id);
	                     ImageButton btnEliminar = (ImageButton) mLinearView.findViewById(R.id.ib_delete);
	                     
	                     mId.setText(prod.getId());
	                     final String id_alimento = mId.getText().toString();
	                     mFirstName.setText(prod.getNombre());
	                     mPrecio.setText(""+prod.getPrecio());
	                     final String precio = mPrecio.getText().toString();
	                     final String nombre = mFirstName.getText().toString();
	                     mLinearListView.addView(mLinearView);
	                     btnEliminar.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									btnEliminarAlimento(id_alimento);
									
								}
							});
	                     mLinearView.setOnClickListener(new OnClickListener() {

	                           @Override
	                           public void onClick(View v) {
	                        	   mostrarDialogPrecio(id_alimento, precio, nombre );
	                           }
	                     });
					}
					
				}
			}
		});
		
	}

	public void mostrarDialogPrecio(final String id, final String precio, final String nombre) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Alimento");
		query.getInBackground(id, new GetCallback<ParseObject>() {
			
			@Override
			public void done(ParseObject object, ParseException e) {
				if(e==null){
					dialogPrecio = new Dialog(EditarLocal.this, R.style.Theme_Dialog_Translucent);
					dialogPrecio.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialogPrecio.setContentView(R.layout.formulario_precio);
					dialogPrecio.setCanceledOnTouchOutside(false);
					EditText et_precio = (EditText) dialogPrecio.findViewById(R.id.et_precio);
					et_precio.setText(precio, TextView.BufferType.EDITABLE);
					dialogPrecio.show();
					ParseFile imageFile = object.getParseFile("imagen");
					ParseImageView alimentoImage = (ParseImageView) dialogPrecio.findViewById(R.id.icon);
					if(imageFile!=null){
						alimentoImage.setParseFile(imageFile);
						alimentoImage.loadInBackground();
					}
					TextView titleTextView = (TextView) dialogPrecio.findViewById(R.id.nombre);
					titleTextView.setText(object.getString("nombre"));
					((Button) dialogPrecio.findViewById(R.id.cancelar)).setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							dialogPrecio.cancel();
						}
					});
					((Button) dialogPrecio.findViewById(R.id.aceptar)).setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							String precio_input = "";
							EditText et_precio = (EditText) dialogPrecio.findViewById(R.id.et_precio);
							precio_input = et_precio.getText().toString();
							if(precio_input.equals("")){
								Toast.makeText(EditarLocal.this , "Por favor ingrese el precio", Toast.LENGTH_SHORT).show();
								return;
							}
							ArrayList<ProductoPrecio> lista = globalClass.getLista();
							ArrayList<ProductoPrecio> lista_nueva = new ArrayList<ProductoPrecio>();
							int precio = Integer.parseInt(precio_input);
							ProductoPrecio producto = new ProductoPrecio();
							producto.setId(id);
							producto.setNombre(nombre);
							producto.setPrecio(precio);
							lista_nueva.add(producto);
							for(ProductoPrecio prod : lista){
								if(!prod.getId().equals(id)){
									lista_nueva.add(prod);
								}
							}
							globalClass.setLista(lista_nueva);
							Comparator<ProductoPrecio> comparador = new Comparator<ProductoPrecio>() {
								
								@Override
								public int compare(ProductoPrecio a, ProductoPrecio b) {
									int resultado = a.getNombre().compareTo(b.getNombre());
									if(resultado!=0){
										return resultado;
									}
									return resultado;
								}
							};
							Collections.sort(lista_nueva, comparador);
							mLinearListView.removeAllViews();
							for(ProductoPrecio prod : lista_nueva){
								LayoutInflater inflater = null;
			                     inflater = (LayoutInflater) getApplicationContext()
			                                  .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			                     final View mLinearView = inflater.inflate(R.layout.item_producto_precio,null);
			                     TextView mFirstName = (TextView) mLinearView.findViewById(R.id.textViewName);
			                     TextView mPrecio = (TextView) mLinearView.findViewById(R.id.textViewPrecio);
			                     TextView mId = (TextView) mLinearView.findViewById(R.id.id);
			                     ImageButton btnEliminar = (ImageButton) mLinearView.findViewById(R.id.ib_delete);
			                     mId.setText(prod.getId());
			                     final String id_alimento = mId.getText().toString();
			                     mFirstName.setText(prod.getNombre());
			                     mPrecio.setText(""+prod.getPrecio());
			                     final String precio_nuevo = mPrecio.getText().toString();
			                     final String nombre = mFirstName.getText().toString();
			                     mLinearListView.addView(mLinearView);
			                     btnEliminar.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										btnEliminarAlimento(id_alimento);
										
									}
								});
			                     mLinearView.setOnClickListener(new OnClickListener() {

			                           @Override
			                           public void onClick(View v) {
			                        	   mostrarDialogPrecio(id_alimento, precio_nuevo, nombre );
			                           }
			                     });
							}
							
							dialogPrecio.dismiss();
						}
					});
				}
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
		case R.id.camera:
			item_camara = item;
			camaraEdicion();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
		
		
	}

	private void camaraEdicion() {
		imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"local.jpg");
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		Uri tempuri = Uri.fromFile(imageFile);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, tempuri);
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		startActivityForResult(intent, 0);
		
	}

	private void aceptarEdicion() {
		if(editLocal==null){
			return;
		}
		final String nombre_edit = tv_titulo.getText().toString();
		final String descripcion_edit = tv_descripcion.getText().toString();
		if(nombre_edit.equals("") || descripcion_edit.equals("")){
			if(nombre_edit.equals("")){
				Toast.makeText(EditarLocal.this, "Por favor ingrese el nombre.", Toast.LENGTH_SHORT).show();
				return;
			}
			else{
				Toast.makeText(EditarLocal.this, "Por favor ingrese alguna descripcion.", Toast.LENGTH_SHORT).show();
				return;
			}
		}
		if(nombre_edit != nombre){
			editLocal.put("nombre", nombre_edit);
		}
		if(descripcion_edit != descripcion){
			editLocal.put("descripcion", descripcion_edit);
		}
		if(mBitmap !=null){
			Bitmap mBitmapScaled = Bitmap.createScaledBitmap(mBitmap, 200, 300 , false);
    		ByteArrayOutputStream stream = new ByteArrayOutputStream();
    		//rotatedScaledMealImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
    		mBitmapScaled.compress(Bitmap.CompressFormat.JPEG, 100, stream);
			
    		//mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
			byte[] data = stream.toByteArray();    
			ParseFile iFile = new ParseFile("image.jpg", data);
			iFile.saveInBackground();
			editLocal.put("imagen",iFile);
		}
		
		editLocal.saveInBackground(new SaveCallback() {
			
			@Override
			public void done(ParseException e) {
				if(e==null){
					ArrayList<ProductoPrecio> lista_final = globalClass.getLista();
					if(lista_final != null){
						for(ProductoPrecio alim_final : lista_final){
							String id_alim_final = alim_final.getId();
							Boolean isIn = false;
							for(ParseObject alimentoLocal : lista_original){
								ParseObject alimento = alimentoLocal.getParseObject("alimento");
								if(id_alim_final.equals(alimento.getObjectId())){
									isIn = true;
									if(alim_final.getPrecio() != alimentoLocal.getInt("precio")){
										alimentoLocal.put("precio", alim_final.getPrecio());
										alimentoLocal.saveInBackground();
									}
									break;
								}
							}
							if(!isIn){
								ParseObject alimentoLocal = new ParseObject("AlimentoLocal");
								alimentoLocal.put("local", editLocal);
								alimentoLocal.put("alimento", ParseObject.createWithoutData("Alimento", alim_final.getId()));
								alimentoLocal.put("precio", alim_final.getPrecio());
								alimentoLocal.saveInBackground(new SaveCallback() {
									
									@Override
									public void done(ParseException arg0) {
									}
								});
							}
						}
						for(ParseObject alimentoLocal : lista_original){
							ParseObject alimento = alimentoLocal.getParseObject("alimento");
							String id_alimento = alimento.getObjectId();
							Boolean isIn =false;
							for(ProductoPrecio alim_final : lista_final){
								if(alim_final.getId().equals(id_alimento)){
									isIn =true;
									break;
								}
							}
							if(!isIn){
								alimentoLocal.deleteInBackground();
							}
						}
						
					}
					Intent intent = new Intent(EditarLocal.this, LocalActivity.class);
					intent.putExtra("clave", editLocal.getObjectId());
					mBitmap=null;
					startActivity(intent);
				}
			}
		});
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 0){
			switch(resultCode){
			case Activity.RESULT_OK:
				if(imageFile.exists()){
					mBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
					imageFile=null;
					item_camara.setIcon(R.drawable.ic_action_attachment);
				}
				break;
			case Activity.RESULT_CANCELED:
				item_camara.setIcon(R.drawable.ic_action_camera);
				imageFile=null;
				mBitmap=null;
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
		
	}
	
	public void btnEliminarAlimento(String id){
		ArrayList<ProductoPrecio> lista = globalClass.getLista();
		ArrayList<ProductoPrecio> lista_nueva = new ArrayList<ProductoPrecio>();
		if(lista != null){
			for(ProductoPrecio prod : lista){
				if(!id.equals(prod.getId())){
					lista_nueva.add(prod);
				}
			}
			globalClass.setLista(lista_nueva);
			Comparator<ProductoPrecio> comparador = new Comparator<ProductoPrecio>() {
				
				@Override
				public int compare(ProductoPrecio a, ProductoPrecio b) {
					int resultado = a.getNombre().compareTo(b.getNombre());
					if(resultado!=0){
						return resultado;
					}
					return resultado;
				}
			};
			Collections.sort(lista_nueva, comparador);
			mLinearListView.removeAllViews();
			for(ProductoPrecio prod : lista_nueva){
				LayoutInflater inflater = null;
                 inflater = (LayoutInflater) getApplicationContext()
                              .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                 final View mLinearView = inflater.inflate(R.layout.item_producto_precio,null);
                 TextView mFirstName = (TextView) mLinearView.findViewById(R.id.textViewName);
                 TextView mPrecio = (TextView) mLinearView.findViewById(R.id.textViewPrecio);
                 TextView mId = (TextView) mLinearView.findViewById(R.id.id);
                 ImageButton btnEliminar = (ImageButton) mLinearView.findViewById(R.id.ib_delete);
                 mId.setText(prod.getId());
                 final String id_alimento = mId.getText().toString();
                 mFirstName.setText(prod.getNombre());
                 mPrecio.setText(""+prod.getPrecio());
                 final String precio_nuevo = mPrecio.getText().toString();
                 final String nombre = mFirstName.getText().toString();
                 mLinearListView.addView(mLinearView);
                 btnEliminar.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						btnEliminarAlimento(id_alimento);
					}
				});
                 mLinearView.setOnClickListener(new OnClickListener() {

                       @Override
                       public void onClick(View v) {
                    	   mostrarDialogPrecio(id_alimento, precio_nuevo, nombre );
                       }
                 });
			}
		}
		
    }

}
