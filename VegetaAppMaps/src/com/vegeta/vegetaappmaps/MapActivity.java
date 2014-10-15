package com.vegeta.vegetaappmaps;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import android.support.v4.app.FragmentActivity;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MapActivity extends FragmentActivity implements LocationListener,
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener, 
OnCameraChangeListener, 
com.google.android.gms.location.LocationListener{
	private static final int GPS_ERRORDIALOG_REQUEST = 9001;
	
	GoogleMap mMap;
	MarkerOptions marcador;
	LocationClient mLocationClient;
	LatLng ll;
	private Location lastLocation;
	private Location currentLocation;
	private LocationRequest locationRequest;
	private LocationClient locationClient;
	private Dialog dialogMarker=null, dialogRestaurant=null, dialogLocal=null, dialogAlimento=null;
	private File imageFile;
	private Boolean dRestaurant=false, dLocal=false;
	private Bitmap mBitmap = null;
	private ProgressDialog mProgressDialog=null;
	private CustomAdapterAlimentos alimentosAdapter;
	private ListView listView;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Parse.initialize(this, "u9VdDovJEB22hYLgjjGm6hjaKH08dzuwhPq4dWGc", "gtDf7cQQEoGJrp89xgZfazpgYfnAyq2OJFBBuumK");
        if(services0K()){
        	setContentView(R.layout.activity_map);
        	
        	if(initMap()){
        		locationRequest = LocationRequest.create();
        		locationRequest.setInterval(5000);
        		locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        		locationRequest.setFastestInterval(1000);
        		locationClient = new LocationClient(this,this,this);
        		mMap.setMyLocationEnabled(true);
        		mMap.setOnCameraChangeListener(this);
        		
        		
        		
        		
        		
        	}
        	else{

        		Toast.makeText(this, "Not Ready", Toast.LENGTH_SHORT).show();
        	}
        }
        else{
        	setContentView(R.layout.activity_main);
        }
        
    }

    @Override
    protected void onStart() {
    	super.onStart();
    	locationClient.connect();
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	if(locationClient.isConnected()){
    		stopPeriodicUpdates();
    	}
    	locationClient.disconnect();
    	MapStateManager mgr = new MapStateManager(this);
    	mgr.savedMapState(mMap);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	MapStateManager mgr = new MapStateManager(this);
    	CameraPosition position = mgr.getSavedCameraPosition();  
    	if(position !=null){
    		CameraUpdate update = CameraUpdateFactory.newCameraPosition(position);
    		mMap.moveCamera(update);
    	}
    }
     
    
    public void botonFuncion(View view){
    	switch(view.getId()){
    	case R.id.ib_restaurant:
    		break;
    	case R.id.ib_local:
    		break;
    	case R.id.ib_geolocalize:
    		Location myLoc = (currentLocation == null) ? lastLocation : currentLocation;
        	if(myLoc == null){
        		Toast.makeText(this, "Por favor intentelo nuevamente despu�s que su posici�n aparezca",Toast.LENGTH_SHORT).show();
        		return;
        	}
        	dialogMarker = new Dialog(this, R.style.Theme_Dialog_Translucent);
	        dialogMarker.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialogMarker.setContentView(R.layout.dialog_marcador);
			dialogMarker.show();
			dialogMarker.setOnCancelListener(new OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface dialog) {
					dialogMarker=null;
				}
			});
	        ((Button) dialogMarker.findViewById(R.id.cancelar)).setOnClickListener(new View.OnClickListener() {
	             
	            @Override
	            public void onClick(View view)
	            {
	            	dialogMarker.cancel();
	                 
	            }
	        });
	        
	        ((ImageButton) dialogMarker.findViewById(R.id.pin_restaurant)).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dialogRestaurant = new Dialog(MapActivity.this, R.style.Theme_Dialog_Translucent);
	            	dialogRestaurant.requestWindowFeature(Window.FEATURE_NO_TITLE);
	            	dialogRestaurant.setContentView(R.layout.formulario_restaurant);
	            	dialogRestaurant.setCanceledOnTouchOutside(false);
	            	dialogRestaurant.show();
	            	dialogRestaurant.setOnCancelListener(new OnCancelListener() {
						
						@Override
						public void onCancel(DialogInterface arg0) {
							imageFile=null;
							dialogRestaurant=null;
							mBitmap=null;
							
						}
					});
	            	
	            	((Button) dialogRestaurant.findViewById(R.id.cancelar)).setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							dialogRestaurant.cancel();
						}
					});
	            	
	            	((Button) dialogRestaurant.findViewById(R.id.aceptar)).setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							String nombre_restaurant ="", descripcion="";
							EditText et_restaurant=null, et_descripcion=null;
							CheckBox ch1 = (CheckBox) dialogRestaurant.findViewById(R.id.check1);
							CheckBox ch2 = (CheckBox) dialogRestaurant.findViewById(R.id.check2);
							et_restaurant = (EditText) dialogRestaurant.findViewById(R.id.et_nombreRestaurant);
							et_descripcion = (EditText) dialogRestaurant.findViewById(R.id.et_descripcionRestaurant);
							nombre_restaurant = et_restaurant.getText().toString();
							descripcion = et_descripcion.getText().toString();
							if(nombre_restaurant.equals("") || descripcion.equals("") ){
								if(nombre_restaurant.equals("")){
									Toast.makeText(MapActivity.this, "Ingrese el nombre del Restaurant", Toast.LENGTH_SHORT).show();
									return;
								}
								else{
									Toast.makeText(MapActivity.this, "Ingrese alguna descripcion", Toast.LENGTH_SHORT).show();
									return;
								}
							}
							// Si los campos no estan vacios, se guarda el registro en la base de datos.
							Location myLoc = (currentLocation == null) ? lastLocation : currentLocation;
				        	if(myLoc == null){
				        		Toast.makeText(MapActivity.this, "Problemas con su ubicaci�n, intenelo de nuevo",Toast.LENGTH_SHORT).show();
				        		return;
				        	}
				        	ParseGeoPoint point = new ParseGeoPoint(myLoc.getLatitude(),myLoc.getLongitude());
				        	ParseObject po_marcador = new ParseObject("Restaurant");
						    po_marcador.put("nombre", nombre_restaurant);
						    po_marcador.put("descripcion", descripcion );
						    po_marcador.put("location", point);
						    po_marcador.put("vegetariano", ch1.isChecked());
						    po_marcador.put("vegano",ch2.isChecked());
				        	if(mBitmap!=null){
				        		Bitmap mBitmapScaled = Bitmap.createScaledBitmap(mBitmap, 500, 500 * mBitmap.getHeight() / mBitmap.getWidth(), false);
				        		ByteArrayOutputStream stream = new ByteArrayOutputStream();
				        		//rotatedScaledMealImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
				        		mBitmapScaled.compress(Bitmap.CompressFormat.JPEG, 100, stream);
								
				        		//mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
								byte[] data = stream.toByteArray();    
								ParseFile iFile = new ParseFile("image.jpg", data);
								iFile.saveInBackground();
								po_marcador.put("imagen",iFile);
				        	}
				        	//po_marcador.put("creado_por", "GzI2BheHjA" );
				        	mProgressDialog = new ProgressDialog(MapActivity.this);
				        	mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				        	mProgressDialog.setCanceledOnTouchOutside(false);
				        	mProgressDialog.setMessage("Guardando datos...");
				        	mProgressDialog.setIndeterminate(false);
				        	mProgressDialog.setCancelable(false);
							// Show progressdialog
							mProgressDialog.show();
							po_marcador.saveInBackground(new SaveCallback() {
								
								@Override
								public void done(ParseException e) {
									if(e==null){
										mProgressDialog.dismiss();
										Toast.makeText(MapActivity.this, "Restaurant Agregado", Toast.LENGTH_SHORT).show();
									}
									else{
										mProgressDialog.dismiss();
										Toast.makeText(MapActivity.this, "Error al guardar, intentelo nuevamente", Toast.LENGTH_SHORT).show();
									}
								}
							});
							dialogRestaurant.cancel();
							dialogMarker.cancel();
							Toast.makeText(MapActivity.this,"Se ha ingresado el marcador al mapa", Toast.LENGTH_SHORT).show();
						}
					});
	            	
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
	            	
	            	
	            	
	            	
				} // fin del boton pin_restaurant
			});
	        
	        ((ImageButton) dialogMarker.findViewById(R.id.pin_carrito)).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					dialogLocal = new Dialog(MapActivity.this, R.style.Theme_Dialog_Translucent);
					dialogLocal.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialogLocal.setContentView(R.layout.formulario_producto);
					dialogLocal.setCanceledOnTouchOutside(false);
					dialogLocal.show();
					dialogLocal.setOnCancelListener(new OnCancelListener() {
						
						@Override
						public void onCancel(DialogInterface arg0) {
							imageFile=null;
							dialogLocal=null;
							mBitmap=null;
							
						}
					});
					((Button) dialogLocal.findViewById(R.id.cancelar)).setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							dialogLocal.cancel();
						}
					});
					((ImageButton) dialogLocal.findViewById(R.id.camara)).setOnClickListener(new View.OnClickListener() {
						
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
					((ImageButton) dialogLocal.findViewById(R.id.ib_agregar)).setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							dialogAlimento = new Dialog(MapActivity.this, R.style.Theme_Dialog_Translucent);
							dialogAlimento.requestWindowFeature(Window.FEATURE_NO_TITLE);
							dialogAlimento.setContentView(R.layout.dialog_lista_alimentos);
							dialogAlimento.setCanceledOnTouchOutside(false);
							dialogAlimento.show();
							listView = (ListView) dialogAlimento.findViewById(R.id.lv_alimentos);
							ArrayList<String> producto = new ArrayList<String>();
							producto.add("hola");
							producto.add("precio");
							ArrayList<ArrayList<String>> hola = new ArrayList<ArrayList<String>>();
							hola.add(producto);
							String name = hola.get(0).get(0).toString();
							Toast.makeText(MapActivity.this, name, Toast.LENGTH_SHORT).show();
							if(alimentosAdapter==null){
								alimentosAdapter = new CustomAdapterAlimentos(MapActivity.this);
							}
							
							listView.setAdapter(alimentosAdapter);
							alimentosAdapter.loadObjects();
							
							
						}
					});
					
					
				}
			});
	        
	        
        	
    		break;
    	}
    	
    	
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
        case R.id.action_search_city:
        	Toast.makeText(this, "Buscar ciudad", Toast.LENGTH_SHORT).show();
        	break;
        case R.id.action_search_restaurant:
        	Toast.makeText(this, "Buscar restaurant", Toast.LENGTH_SHORT).show();
        	break;
        case R.id.action_search_local:
        	Toast.makeText(this, "Buscar tienda", Toast.LENGTH_SHORT).show();
        	break;
        case R.id.action_search_producto:
        	Toast.makeText(this, "Buscar producto", Toast.LENGTH_SHORT).show();
        	break;
        
        }
        return super.onOptionsItemSelected(item);
    }
    
     public boolean services0K(){
    	int isAvailable;
    	isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
    	
    	if(isAvailable == ConnectionResult.SUCCESS){
    		return true;
    	}
    	else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)){
    		Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isAvailable, this, GPS_ERRORDIALOG_REQUEST);
    		dialog.show();
    	}
    	else{
    		Toast.makeText(this, "No se puede conectar con Google Play Services...", Toast.LENGTH_SHORT).show();
    	}
    	return false;
     }
     
     private boolean initMap(){
    	 if(mMap==null){
    		 SupportMapFragment mapFrag =
    				 (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
    		 mMap = mapFrag.getMap();
    	 }
    	 return(mMap!=null);
     }

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onConnected(Bundle connectionHint) {
		currentLocation = getLocation();
		startPeriodicUpdates();
		Toast.makeText(this, "Conectado al servicion de localizacion", Toast.LENGTH_SHORT).show();
		
	}


	private void startPeriodicUpdates() {
	    locationClient.requestLocationUpdates(locationRequest, this);
		
	}
	
	private void stopPeriodicUpdates() {
		locationClient.removeLocationUpdates(this);
		
	}


	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		
	}


	@Override
	public void onLocationChanged(Location location) {
		currentLocation = location;
		// Hacer un if para que no cambie si uno ha cambiado sus par de metros
		lastLocation = location;
		LatLng myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
		// Hacer una comprobacion del zoom que tiene puesto el Cliente, para que no cambie cada vez
		CameraUpdateFactory.newLatLngZoom(myLatLng,10);
		//doMapQuery();
		
	}


	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onCameraChange(CameraPosition position) {
		doMapQuery();
		
	}


	private void doMapQuery() {
		// TODO Auto-generated method stub
		
	}
	 private Location getLocation() {
		    // If Google Play Services is available
		    if (services0K()) {
		      // Get the current location
		      return locationClient.getLastLocation();
		    } else {
		      return null;
		    }
	 }
	 
	 @Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		 if(requestCode == 0 ){
			 switch(resultCode){
			 case Activity.RESULT_OK:
				 if(imageFile.exists()){
					 ImageButton imgbtn;
					 TextView tv_foto;
					 if(dRestaurant){
						 Toast.makeText(this, "Imagen guardada en: "+imageFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
						 imgbtn = (ImageButton) dialogRestaurant.findViewById(R.id.camara);
						 imgbtn.setImageResource(R.drawable.adjunto);
						 tv_foto = (TextView) dialogRestaurant.findViewById(R.id.tv_camara);
						 tv_foto.setText("Imagen Adjunta");
						 mBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
						 dRestaurant=false;
						 imageFile=null;
					 }
					 
					 if(dLocal){
						 Toast.makeText(this, "Imagen guardada en: "+imageFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
						 imgbtn = (ImageButton) dialogLocal.findViewById(R.id.camara);
						 imgbtn.setImageResource(R.drawable.adjunto);
						 tv_foto = (TextView) dialogLocal.findViewById(R.id.tv_camara);
						 tv_foto.setText("Imagen Adjunta");
						 mBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
						 dLocal=false;
						 imageFile=null;
					 }
					 					 
				 }
				 break;
			 case Activity.RESULT_CANCELED:
				 ImageButton imgbtn;
				 TextView tv_foto;
				 if(dRestaurant){
					 imgbtn = (ImageButton) dialogRestaurant.findViewById(R.id.camara);
					 imgbtn.setImageResource(R.drawable.camara);
					 tv_foto = (TextView) dialogRestaurant.findViewById(R.id.tv_camara);
					 tv_foto.setText("Foto");
					 dRestaurant=false;
					 imageFile=null;
				 }
				 if(dLocal){
					 imgbtn = (ImageButton) dialogLocal.findViewById(R.id.camara);
					 imgbtn.setImageResource(R.drawable.camara);
					 tv_foto = (TextView) dialogLocal.findViewById(R.id.tv_camara);
					 tv_foto.setText("Foto");
					 dLocal=false;
					 imageFile=null;
				 }
				 
				 break;
				 
			 }
		}
	 }
     
}
