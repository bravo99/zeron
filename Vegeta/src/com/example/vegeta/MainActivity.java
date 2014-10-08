package com.example.vegeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


	public class MainActivity extends FragmentActivity implements OnMapClickListener, OnMyLocationChangeListener, OnMapLongClickListener {
	private GoogleMap mMap;
	private File imageFile;
	Location myLocation;
	ArrayList<ParseObject> posts;
	Dialog dialogMarker = null, dialogRestaurant=null, dialogLocal=null, dialogIngredient=null;
	String city = "";
	String city2 ="";
	ListView lv_ingredientes; // ListView donde poner todos los ingredientes de parse! 
	
	//Para lo del ListView
	ListView listview;
	List<ParseObject> ob;
	ProgressDialog mProgressDialog;
	ListViewAdapter adapter;
	private List<ListIngredient> listingredient = null;
	
	private int pin_activo=0;
	private boolean dRestaurant=false, dLocal=false;
	private boolean restaurant_activado=true;
	private boolean carrito_activado=true;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
       
       //Antes que nada, vamos a agregar un ingrediente sin imagen a la base de datos para ver si funciona
       // Codigo de abajo funciona perfectamente!
       /*
       ParseObject ingrediente = new ParseObject("Ingrediente");
       ingrediente.put("nombre", "nombre prueba");
       ingrediente.put("descripcion", "Descripcion de prueba" );
       ingrediente.put("uniIngrediente", "unidad de prueba");
		//siempre autor sera vegetatest para este entregable
		
		ingrediente.put("autor", "vegetatest" );
		
		ingrediente.saveInBackground();*/
       
       
       
       // Preparar y mostrar mapa
        SupportMapFragment mapFrag =
				(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		mMap = mapFrag.getMap();
		// Poner latitud y longitud de Valparaiso
		LatLng valparaiso = new LatLng(-33.063056, -71.639444);
		// Muevo la camara hacia Valparaiso
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(valparaiso, 13));
		// Hasta aqui todo bien!
		
		// A�adir un marcador (En este caso de prueba puse a Valparaiso)
		MarkerOptions marcador = new MarkerOptions();
		marcador.title("Valparaiso");
		marcador.snippet("Quinta regi�n de Chile");
		marcador.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
		marcador.position(valparaiso);
		
		// A�adir el marcador al mapa mMap
		mMap.addMarker(marcador);
		// Hacer click en parte del mapa (Listener) implementar en MainActivity y crear la funcion onMapClick()
		mMap.setOnMapClickListener(this);
		mMap.setOnMapLongClickListener(this);
		// Poner el boton mi ubicacion al mapa
		mMap.setMyLocationEnabled(true);
		// cuando cambia mi posicion
		mMap.setOnMyLocationChangeListener(this);
		
        }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
      //  getMenuInflater().inflate(R.menu.main, menu);
    	
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        
        return super.onOptionsItemSelected(item);
    }


	@Override
	public void onMapClick(LatLng point) {
		if(pin_activo==0){
			return;
		}
		else{
			//setContentView(R.layout.activity_markerinfo);
			
			//Geocoder para ver la ciudad o localidad de los puntos geograficos
			Geocoder geocoder;
			List<Address> addresses;
			geocoder = new Geocoder(this,Locale.getDefault());
			try {
				addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);
				city = addresses.get(0).getLocality();
				city2= addresses.get(0).getCountryName();
			} catch(IOException e) {
				e.printStackTrace();			
			}
			switch(pin_activo){
			case 1:
				Toast.makeText(this, "restaurant "+ city + " - " + city2, Toast.LENGTH_SHORT).show();
				break;
			case 2:
				Toast.makeText(this, "carrito " + city + " - " + city2, Toast.LENGTH_SHORT).show();
				break;
			}
			MarkerOptions marcador = new MarkerOptions();
			marcador.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
			marcador.position(point);
			marcador.title("Coordenadas");
			marcador.snippet("Latitud: "+ point.latitude+" - Longitud: "+ point.longitude);
			mMap.addMarker(marcador);
			pin_activo=0;
			return;
		}
		}


	@Override
	public void onMyLocationChange(Location loc) {
		LatLng posicion = new LatLng(loc.getLatitude(), loc.getLongitude());
		// Muevo la camara hacia Valparaiso
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posicion, 15));
		mMap.setOnMyLocationChangeListener(null);
		myLocation=loc;
		Geocoder mygeo;
		List<Address> addresses;
		mygeo= new Geocoder(MainActivity.this,Locale.getDefault());
		try {
			addresses = mygeo.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
			city = addresses.get(0).getLocality();
			city2= addresses.get(0).getCountryName();
		} catch(IOException e) {
			e.printStackTrace();			
		}
		
	}


	@Override
	public void onMapLongClick(LatLng point) {
		
	}
	
	public void BotonFuncion(View v){
		 int id = v.getId();
		 switch(id){
		 
		 	case R.id.restaurant:
		 		if(restaurant_activado){
		 			v.setBackgroundColor(0x80FFFFFF);
		 			restaurant_activado=false;
		 			Toast.makeText(this, "Restaurant desactivado", Toast.LENGTH_SHORT).show();
		 		}
		 		else{
		 			v.setBackgroundColor(0x8000FF00);
		 			restaurant_activado=true;
		 			Toast.makeText(this, "Restaurant activado", Toast.LENGTH_SHORT).show();
		 		}
		 		break;
		 	case R.id.carrito:
		 		if(carrito_activado){
		 			v.setBackgroundColor(0x80FFFFFF);
		 			carrito_activado=false;
		 			Toast.makeText(this, "Carrito desactivado", Toast.LENGTH_SHORT).show();
		 		}
		 		else{
		 			v.setBackgroundColor(0x8000FF00);
		 			carrito_activado=true;
		 			Toast.makeText(this, "Carrito activado", Toast.LENGTH_SHORT).show();
		 		}
		 		break;
		case R.id.pin:
			// con este tema personalizado evitamos los bordes por defecto
			dialogMarker = new Dialog(this, R.style.Theme_Dialog_Translucent);
			//deshabilitamos el t�tulo por defecto
	        dialogMarker.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialogMarker.setContentView(R.layout.dialog_marcador);
			// Comando para definir el tama�o de la ventana del dialogo
			//dialogMarker.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			
	        ((Button) dialogMarker.findViewById(R.id.cancelar)).setOnClickListener(new View.OnClickListener() {
	             
	            @Override
	            public void onClick(View view)
	            {
	            	dialogMarker.dismiss();
	            	Toast.makeText(MainActivity.this, "holi shet", Toast.LENGTH_SHORT).show();
	                 
	            }
	        });
			
			((ImageButton) dialogMarker.findViewById(R.id.pin_restaurant)).setOnClickListener(new View.OnClickListener(){
	             
	            @Override
	            public void onClick(View view)
	            {
	            	dialogRestaurant = new Dialog(MainActivity.this, R.style.Theme_Dialog_Translucent);
	            	dialogRestaurant.requestWindowFeature(Window.FEATURE_NO_TITLE);
	            	dialogRestaurant.setContentView(R.layout.formulario_restaurant);
	            	dialogRestaurant.setCanceledOnTouchOutside(false);
	            	dialogRestaurant.setTitle("Agregar nuevo Restaurant");
	            	dialogRestaurant.show();
	            	
	            	((CheckBox) dialogRestaurant.findViewById(R.id.check1)).setOnCheckedChangeListener(new OnCheckedChangeListener() {
						
						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							if(!isChecked){
								CheckBox ch2 = (CheckBox) dialogRestaurant.findViewById(R.id.check2);
								if(!ch2.isChecked()){
									ch2.setChecked(true);
								}
							}
						}
					});
	            	((CheckBox) dialogRestaurant.findViewById(R.id.check2)).setOnCheckedChangeListener(new OnCheckedChangeListener() {
						
						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							if(!isChecked){
								CheckBox ch1 = (CheckBox) dialogRestaurant.findViewById(R.id.check1);
								if(!ch1.isChecked()){
									ch1.setChecked(true);
								}
							}
						}
					});
	            	
	            	
	            	((ImageButton) dialogRestaurant.findViewById(R.id.camara)).setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							dRestaurant=true;
							imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"vegeta.jpg");
							Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
							Uri tempuri = Uri.fromFile(imageFile);
							intent.putExtra(MediaStore.EXTRA_OUTPUT, tempuri);
							intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
							startActivityForResult(intent, 0);
							
						}
					});
	            	
	            	
	            	((Button) dialogRestaurant.findViewById(R.id.aceptar)).setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							String nombre_restaurant ="", descripcion="";
							EditText et_restaurant=null, et_descripcion=null;
							et_restaurant = (EditText) dialogRestaurant.findViewById(R.id.et_nombreRestaurant);
							et_descripcion = (EditText) dialogRestaurant.findViewById(R.id.et_descripcionRestaurant);
							nombre_restaurant = et_restaurant.getText().toString();
							descripcion = et_descripcion.getText().toString();
							if(nombre_restaurant.equals("") || descripcion.equals("") ){
								if(nombre_restaurant.equals("")){
									Toast.makeText(MainActivity.this, "Ingrese el nombre del Restaurant", Toast.LENGTH_SHORT).show();
									return;
								}
								else{
									Toast.makeText(MainActivity.this, "Ingrese alguna descripci�n", Toast.LENGTH_SHORT).show();
									return;
								}
							}
							// Si los campos est�n rellenos se procede a incluir el marcador en el mapa
							
							MarkerOptions marcador = new MarkerOptions();
							marcador.title(nombre_restaurant);
							marcador.snippet(descripcion);
							marcador.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_restaurant));
							LatLng latlong = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
							marcador.position(latlong);
							mMap.addMarker(marcador);
							dialogRestaurant.dismiss();
							dialogMarker.dismiss();
							Toast.makeText(MainActivity.this,"Se ha ingresado el marcador al mapa", Toast.LENGTH_SHORT).show();
						}
					});
	            	((Button) dialogRestaurant.findViewById(R.id.cancelar)).setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							dialogRestaurant.dismiss();
						}
					});
	            	
	            	
	                 
	            }
	        });
			
			((ImageButton) dialogMarker.findViewById(R.id.pin_carrito)).setOnClickListener(new View.OnClickListener(){
	             
	            @Override
	            public void onClick(View view)
	            {
	            	dialogLocal = new Dialog(MainActivity.this, R.style.Theme_Dialog_Translucent);
	            	dialogLocal.requestWindowFeature(Window.FEATURE_NO_TITLE);
	            	dialogLocal.setContentView(R.layout.formulario_producto);
	            	dialogLocal.setCanceledOnTouchOutside(false);
	            	dialogLocal.show();
	            	
	            	((Button) dialogLocal.findViewById(R.id.cancelar)).setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							dialogLocal.dismiss();
						}
					});
	            	
	            	((ImageButton) dialogLocal.findViewById(R.id.ib_camara)).setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							dLocal=true;
							imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"local.jpg");
							Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
							Uri tempuri = Uri.fromFile(imageFile);
							intent.putExtra(MediaStore.EXTRA_OUTPUT, tempuri);
							intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
							startActivityForResult(intent, 0);
							
						}
					});
	            	
	            	((ImageButton) dialogLocal.findViewById(R.id.ib_agregar)).setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							dialogIngredient = new Dialog(MainActivity.this, R.style.Theme_Dialog_Translucent);
							dialogIngredient.requestWindowFeature(Window.FEATURE_NO_TITLE);
							dialogIngredient.setContentView(R.layout.listview_main);
							dialogIngredient.setCanceledOnTouchOutside(false);
							dialogIngredient.show();
							
							new RemoteDataTask().execute();
						}
					});
	            	
	                 
	            }
	        });
			
			dialogMarker.show();
			break;
		
		}
		return;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 0 ){
			switch(resultCode){
			case Activity.RESULT_OK:
				if(imageFile.exists()){
					ImageButton imgbtn;
					if(dRestaurant){
						Toast.makeText(MainActivity.this, "La imagen se guardo en " + imageFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
						imgbtn= (ImageButton) dialogRestaurant.findViewById(R.id.camara);
						imgbtn.setImageResource(R.drawable.imagen_ready);
						TextView nombre_camara = (TextView) dialogRestaurant.findViewById(R.id.tv_camara_foto);
						nombre_camara.setText("Imagen adjunta");
						// Bitmap mBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
						
						//ImageView mImageview = (ImageView) dialogRestaurant.findViewById(R.id.iv_restaurant);
						//mImageview.setImageBitmap(mBitmap);
						//imgbtn.setImageBitmap(mBitmap);
						dRestaurant=false;
						imageFile=null;
					}
					if(dLocal){
						Toast.makeText(MainActivity.this, "La imagen se guardo en " + imageFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
						imgbtn= (ImageButton) dialogLocal.findViewById(R.id.ib_camara);
						//imgbtn.setBackgroundResource(R.drawable.imagen_ready);
						imgbtn.setImageResource(R.drawable.imagen_ready);
						TextView nombre_camara = (TextView) dialogLocal.findViewById(R.id.tv_camara);
						nombre_camara.setText("Imagen adjunta");
						// Bitmap mBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
						
						//ImageView mImageview = (ImageView) dialogRestaurant.findViewById(R.id.iv_restaurant);
						//mImageview.setImageBitmap(mBitmap);
						//imgbtn.setImageBitmap(mBitmap);
						dLocal=false;
						imageFile=null;
					}
					
				}
				else{
					Toast.makeText(MainActivity.this, "problem", Toast.LENGTH_SHORT).show();
				}
				break;
			case Activity.RESULT_CANCELED:
				ImageButton imgbtn;
				if(dRestaurant){
					Toast.makeText(this, "Sacar foto fue cancelado!", Toast.LENGTH_SHORT).show();
					imgbtn= (ImageButton) dialogRestaurant.findViewById(R.id.camara);
					imgbtn.setImageResource(R.drawable.camara);
					TextView nombre_camara = (TextView) dialogRestaurant.findViewById(R.id.tv_camara_foto);
					nombre_camara.setText("Foto");
				}
				if(dLocal){
					Toast.makeText(this, "Sacar foto fue cancelado!", Toast.LENGTH_SHORT).show();
					imgbtn= (ImageButton) dialogLocal.findViewById(R.id.ib_camara);
					imgbtn.setImageResource(R.drawable.camara);
					TextView nombre_camara = (TextView) dialogLocal.findViewById(R.id.tv_camara);
					nombre_camara.setText("Foto");
				}
				break;
			}
			
		}
	}
	
	private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Create a progressdialog
			mProgressDialog = new ProgressDialog(MainActivity.this);
			// Set progressdialog title
			mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			//mProgressDialog.setTitle("Parse.com Custom ListView Tutorial");
			// Set progressdialog message
			mProgressDialog.setMessage("Loading...");
			mProgressDialog.setIndeterminate(false);
			// Show progressdialog
			mProgressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// Create the array
			listingredient = new ArrayList<ListIngredient>();
			try {
				// Locate the class table named "Country" in Parse.com
				ParseQuery<ParseObject> query2 = new ParseQuery<ParseObject>("Ingrediente");
				// Locate the column named "ranknum" in Parse.com and order list
				// by ascending
				query2.orderByAscending("nombre");
				ob = query2.find();
				for (ParseObject ing : ob){
					ParseFile image2 = (ParseFile) ing.get("imagen");
					ListIngredient ingrediente = new ListIngredient();
					ingrediente.setNombre((String) ing.get("nombre"));
					ingrediente.setDescripcion((String) ing.get("descripcion"));
					ingrediente.setImagenIngrediente(image2.getUrl());
					listingredient.add(ingrediente);
				}
			} catch (ParseException e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// Locate the listview in listview_main.xml
			listview = (ListView) dialogIngredient.findViewById(R.id.listview);
			// Pass the results into ListViewAdapter.java
			//adapter = new ListViewAdapter(MainActivity.this,
				//	worldpopulationlist);
			adapter = new ListViewAdapter(MainActivity.this, listingredient, dialogIngredient);
			// Binds the Adapter to the ListView
			listview.setAdapter(adapter);
			// Close the progressdialog
			mProgressDialog.dismiss();
		}
	}
	
	
}
