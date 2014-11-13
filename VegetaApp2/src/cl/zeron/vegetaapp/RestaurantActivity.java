package cl.zeron.vegetaapp;


import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class RestaurantActivity extends Activity {
	private ParseObject editRestaurant;
	private String llave;
	private TextView tv_nombre, tv_descripcion, vegetariano, vegano;;
	private ParseImageView imagen;
	private ParseFile imageFile;
	private ParseUser currentUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_restaurant);
		llave = getIntent().getExtras().getString("clave");
		tv_nombre = (TextView) findViewById(R.id.tv_nombre);
		tv_descripcion = (TextView) findViewById(R.id.descripcion);
		vegetariano = (TextView) findViewById(R.id.vegetariano);
		vegano = (TextView) findViewById(R.id.vegano);
		imagen = (ParseImageView) findViewById(R.id.imagen);
		currentUser = ParseUser.getCurrentUser();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Restaurant");
		query.getInBackground(llave, new GetCallback<ParseObject>() {
			
			@Override
			public void done(ParseObject restaurant, ParseException e) {
				editRestaurant = restaurant;
				tv_nombre.setText(editRestaurant.getString("nombre"));
				tv_descripcion.setText(editRestaurant.getString("descripcion"));
				imageFile = restaurant.getParseFile("imagen");
				if(imageFile!=null){
					imagen.setParseFile(imageFile);
					imagen.loadInBackground();
				}
				
				if(restaurant.getBoolean("vegetariano")){
					vegetariano.setVisibility(1);
				}
				if(restaurant.getBoolean("vegano")){
					vegano.setVisibility(1);
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
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()){
		case R.id.action_edit:
			if(currentUser == null){
				Toast.makeText(RestaurantActivity.this, "Debe estar registrado para Editar", Toast.LENGTH_SHORT).show();
				return false;
			}
			else{
				Toast.makeText(RestaurantActivity.this, currentUser.getString("name"), Toast.LENGTH_SHORT).show();
			}
			Intent intent = new Intent(RestaurantActivity.this, EditarRestaurant.class);
			Bundle extras = new Bundle();
			extras.putString("nombre", editRestaurant.getString("nombre"));
			extras.putString("descripcion", editRestaurant.getString("descripcion"));
			extras.putString("id", editRestaurant.getObjectId());
			extras.putBoolean("vegetariano", editRestaurant.getBoolean("vegetariano"));
			extras.putBoolean("vegano", editRestaurant.getBoolean("vegano"));
			intent.putExtras(extras);
			startActivity(intent);
			return true;
		case R.id.action_remove:
			if(currentUser!=null){
				ParseUser creador = editRestaurant.getParseUser("creado_por");
				if(creador.getObjectId().equals(currentUser.getObjectId())){
					actionRemove();
					return true;
				}
				else{
					Toast.makeText(RestaurantActivity.this, "No es el creador, no puede Eliminar ", Toast.LENGTH_SHORT).show();
					
					return false;
				}
			}
			
			else{
				Toast.makeText(RestaurantActivity.this, "Debe estar registrado para Eliminar", Toast.LENGTH_SHORT).show();
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
		                        eliminarRestaurant(); // metodo que se debe implementar
		                    }
		                });
		AlertDialog alert = builder.create();
		alert.show();
	}

	protected void eliminarRestaurant() {
		editRestaurant.deleteInBackground();
		Intent intent = new Intent(RestaurantActivity.this, MainActivity.class);
		intent.putExtra("fragment", "mapa");
		startActivity(intent);
		
	}
}
