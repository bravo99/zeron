package cl.zeron.vegetaapp;

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
import com.parse.ParseUser;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LocalActivity extends Activity {
	private ParseImageView imagen;
	private TextView tv_nombre, tv_descripcion;
	private String llave, nombre, descripcion;
	protected ArrayList<ProductoPrecio> lista_producto;
	private ProductoPrecio producto_alimento;
	private ParseFile imageFile;
	private ParseObject editLocal;
	private ParseUser currentUser;
	private List<ParseObject> lista_productosLocal;
	private LinearLayout mLinearListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_local);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		currentUser = ParseUser.getCurrentUser();
		tv_nombre = (TextView) findViewById(R.id.tv_nombre);
		lista_producto = new ArrayList<ProductoPrecio>();
		tv_descripcion = (TextView) findViewById(R.id.descripcion);
		imagen = (ParseImageView) findViewById(R.id.imagen);
		llave = getIntent().getExtras().getString("clave");
		mLinearListView = (LinearLayout) findViewById(R.id.linear_listview);
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Local");
		query.getInBackground(llave, new GetCallback<ParseObject>() {
			
			@Override
			public void done(ParseObject local, ParseException e) {
				if(e==null){
					editLocal = local;
					imageFile = local.getParseFile("imagen");
					if(imageFile!=null){
						imagen.setParseFile(imageFile);
						imagen.loadInBackground();
					}
					nombre = local.getString("nombre");
					descripcion = local.getString("descripcion");
					tv_nombre.setText(nombre);
					tv_descripcion.setText(descripcion);
					
					ParseQuery<ParseObject> query = ParseQuery.getQuery("AlimentoLocal");
					query.whereEqualTo("local", local);
					query.include("alimento");
					query.findInBackground(new FindCallback<ParseObject>(
							) {
						
						@Override
						public void done(List<ParseObject> alimentoLocal, ParseException e) {
							if(e==null){
								lista_productosLocal = alimentoLocal;
								for(ParseObject producto : alimentoLocal){
									ParseObject alimento = producto.getParseObject("alimento");
									producto_alimento = new ProductoPrecio();
									producto_alimento.setNombre(alimento.getString("nombre"));
									producto_alimento.setPrecio(producto.getInt("precio"));
									producto_alimento.setId(alimento.getObjectId());
									lista_producto.add(producto_alimento);
								}
								
								
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
								Collections.sort(lista_producto, comparador);
								for(ProductoPrecio prod : lista_producto){
									LayoutInflater inflater = null;
				                     inflater = (LayoutInflater) getApplicationContext()
				                                  .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				                     final View mLinearView = inflater.inflate(R.layout.item_producto_precio,null);
				                     TextView mFirstName = (TextView) mLinearView.findViewById(R.id.textViewName);
				                     TextView mPrecio = (TextView) mLinearView.findViewById(R.id.textViewPrecio);
				                     TextView mId = (TextView) mLinearView.findViewById(R.id.id);
				                     ImageButton btnEliminar = (ImageButton) mLinearView.findViewById(R.id.ib_delete);
				                     btnEliminar.setVisibility(View.INVISIBLE);
				                     mId.setText(prod.getId());
				                     final String id_alimento = mId.getText().toString();
				                     mFirstName.setText(prod.getNombre());
				                     mPrecio.setText(""+prod.getPrecio());
				                     mLinearListView.addView(mLinearView);
				                     mLinearView.setOnClickListener(new OnClickListener() {

				                           @Override
				                           public void onClick(View v) {
				                        	   mostrarIngrediente(id_alimento);
				                           }
				                     });
								}
							}
						}
					});
					
					
					
				}
				else{
					Toast.makeText(LocalActivity.this, "Problemas al encontrar la Tienda", Toast.LENGTH_SHORT).show();
					return;
				}
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.local_activity_action, menu);
		return super.onCreateOptionsMenu(menu);
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.action_edit:
			if(currentUser == null){
				Toast.makeText(LocalActivity.this, "Debe estar registrado para Editar", Toast.LENGTH_SHORT).show();
				return false;
			}
			else{
				Toast.makeText(LocalActivity.this, currentUser.getString("name"), Toast.LENGTH_SHORT).show();
			}
			Intent intent = new Intent(LocalActivity.this,EditarLocal.class);
			Bundle extras = new Bundle();
			extras.putString("nombre", editLocal.getString("nombre"));
			extras.putString("descripcion", editLocal.getString("descripcion"));
			extras.putString("id", editLocal.getObjectId());
			intent.putExtras(extras);
			startActivity(intent);
			return true;
		case R.id.action_remove:
			if(currentUser!=null){
				if(editLocal.getParseUser("creado_por") == currentUser){
					actionRemove();
					return true;
				}
				else{
					Toast.makeText(LocalActivity.this, "No es el creador, no puede Eliminar", Toast.LENGTH_SHORT).show();
					return false;
				}
			}
			
			else{
				Toast.makeText(LocalActivity.this, "Debe estar registrado para Eliminar", Toast.LENGTH_SHORT).show();
				return false;
			}
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void actionRemove() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("¿Desea eliminar la Tienda?")
		        .setTitle("Advertencia")
		        .setCancelable(false)
		        .setNegativeButton("Cancelar",
		                new DialogInterface.OnClickListener() {
		                    public void onClick(DialogInterface dialog, int id) {
		                        dialog.cancel();
		                    }
		                })
		        .setPositiveButton("Aceptar",
		                new DialogInterface.OnClickListener() {
		                    public void onClick(DialogInterface dialog, int id) {
		                        eliminarLocal(); // metodo que se debe implementar
		                    }
		                });
		AlertDialog alert = builder.create();
		alert.show();
	}

	protected void eliminarLocal() {
		if(lista_productosLocal != null){
			for(ParseObject prod : lista_productosLocal){
				prod.deleteInBackground();
			}
		}
		editLocal.deleteInBackground();
		Intent intent = new Intent(LocalActivity.this, MainActivity.class);
		intent.putExtra("fragment", "mapa");
		startActivity(intent);
	}
	
	public void mostrarIngrediente(String id_alimento){
		Intent intent = new Intent(LocalActivity.this, AlimentoResultListActivity.class);
		intent.putExtra("ID", id_alimento);
		startActivity(intent);
	}
	
}
