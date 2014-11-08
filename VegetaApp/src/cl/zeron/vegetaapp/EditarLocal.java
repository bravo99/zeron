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
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class EditarLocal extends Activity {
	private String nombre, descripcion, id;
	private ArrayList<ProductoPrecio> lista_prod;
	private ListView lv_producto;
	private EditText tv_titulo, tv_descripcion;
	private ParseObject editLocal;
	private Bitmap mBitmap;
	private GlobalClass globalClass;
	private List<ParseObject> lista_original;
	private Dialog dialogAlimento;
	private File imageFile;
	private MenuItem item_camara;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_editar_local);
		Bundle extras = getIntent().getExtras();
		nombre= extras.getString("nombre");
		descripcion = extras.getString("descripcion");
		id = extras.getString("id");
		lv_producto = (ListView) findViewById(R.id.lv_productos);
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
		CustomAdapterAlimentos alimentosAdapter = new CustomAdapterAlimentos(EditarLocal.this, lv_producto, dialogAlimento);
		
		
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
					ProductoPrecioAdapter adapter = new ProductoPrecioAdapter(EditarLocal.this, lista_prod, lv_producto);
					lv_producto.setAdapter(adapter);
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

}
