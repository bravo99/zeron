package cl.zeron.vegetaapp;

import java.io.ByteArrayOutputStream;
import java.io.File;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

public class EditarRestaurant extends Activity {
	private String nombre, descripcion, id;
	private Boolean vegetariano, vegano;
	private CheckBox cb_vegano, cb_vegetariano;
	private MenuItem item_camara;
	private File imageFile;
	private Bitmap mBitmap;
	private EditText et_nombre, et_descripcion;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_editar_restaurant);
		Bundle extras = getIntent().getExtras();
		nombre= extras.getString("nombre");
		descripcion = extras.getString("descripcion");
		id = extras.getString("id");
		vegetariano = extras.getBoolean("vegetariano");
		vegano = extras.getBoolean("vegano");
		et_nombre = (EditText) findViewById(R.id.et_nombre);
		et_descripcion = (EditText) findViewById(R.id.et_descripcion);
		cb_vegetariano = (CheckBox) findViewById(R.id.cb_vegetariano);
		cb_vegano = (CheckBox) findViewById(R.id.cb_vegano);
		et_nombre.setText(nombre, TextView.BufferType.EDITABLE);
		et_descripcion.setText(descripcion, TextView.BufferType.EDITABLE);
		if(vegetariano){
			cb_vegetariano.setChecked(true);
		}
		if(vegano){
			cb_vegano.setChecked(true);
		}
		cb_vegetariano.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(!isChecked){
					if(!cb_vegano.isChecked()){
						cb_vegano.setChecked(true);
					}
				}
			}
		});
		cb_vegano.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(!isChecked){
					if(!cb_vegetariano.isChecked()){
						cb_vegetariano.setChecked(true);
					}
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
		final String nombre_nuevo = et_nombre.getText().toString();
		final String descripcion_nuevo = et_descripcion.getText().toString();
		if(nombre_nuevo.equals("") || descripcion_nuevo.equals("")){
			if(nombre_nuevo.equals("")){
				Toast.makeText(EditarRestaurant.this, "Por favor ingrese el nombre.", Toast.LENGTH_SHORT).show();
				return;
			}
			else{
				Toast.makeText(EditarRestaurant.this, "Por favor ingrese alguna descripcion.", Toast.LENGTH_SHORT).show();
				return;
			}
		}
		
		ParseQuery<ParseObject> restaurantquery = ParseQuery.getQuery("Restaurant");
		
		restaurantquery.getInBackground(id, new GetCallback<ParseObject>() {
			
			@Override
			public void done(ParseObject restaurant, ParseException e) {
				if(e==null){
					if(nombre_nuevo != nombre){
						restaurant.put("nombre", nombre_nuevo);
					}
					if(descripcion_nuevo != descripcion){
						restaurant.put("descripcion", descripcion_nuevo);
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
						restaurant.put("imagen",iFile);
					}
					if(cb_vegetariano.isChecked() != vegetariano){
						restaurant.put("vegetariano", cb_vegetariano.isChecked());
					}
					if(cb_vegano.isChecked() != vegano){
						restaurant.put("vegano", cb_vegano.isChecked());
					}
					
					restaurant.saveInBackground(new SaveCallback() {
						
						@Override
						public void done(ParseException e) {
							if(e==null){
								Intent intent = new Intent(EditarRestaurant.this, RestaurantActivity.class);
								intent.putExtra("clave", id);
								startActivity(intent);
							}
							
						}
					});
					
					
					
				}
			}
		});
		
		
	}
	
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
