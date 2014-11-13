package cl.zeron.vegetaapp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentMapa extends Fragment implements LocationListener,
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener, 
OnCameraChangeListener, 
com.google.android.gms.location.LocationListener {
	
	
	
	private static final int GPS_ERRORDIALOG_REQUEST = 9001;
	private MapView mMapView;
	private GoogleMap mMap;
	private ImageButton ib_restaurant, ib_local, ib_geolocalize;
	private Bundle mBundle;
	private File imageFile;
	private Bitmap mBitmap;
	LocationClient mLocationClient;
	private ProgressDialog mProgressDialog=null;
	private ListView listView;
	LatLng ll;
	private Location lastLocation;
	private Location currentLocation;
	private LocationRequest locationRequest;
	private LocationClient locationClient;
	private Dialog dialogMarker=null, dialogRestaurant=null, dialogLocal=null, dialogAlimento=null;
	private boolean dRestaurant=false, dLocal=false;
	private CustomAdapterAlimentos alimentosAdapter;
	private GlobalClass globalClass;
	private HashMap<Marker,String> hashRestaurant, hashNombreRestaurant;
	private HashMap<Marker,String> hashLocal, hashNombreLocal;
	private double maxDistanciaBusqueda=15;
	private boolean restaurantPress = true, localPress = true;
	private ParseObject po_marcador;
	private ArrayList<ProductoPrecio> lista_prueba;
	private ParseUser currentUser;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		globalClass = (GlobalClass) getActivity().getApplicationContext();
		View inflatedView = inflater.inflate(R.layout.mapa, container, false);
		mMapView = (MapView) inflatedView.findViewById(R.id.map);
		mMapView.onCreate(mBundle);	
		if(services0K()){
			MapsInitializer.initialize(getActivity());
			if(setUpMapIfNeeded(inflatedView)){
				locationRequest = LocationRequest.create();
        		//locationRequest.setInterval(5000);
        		locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        		//locationRequest.setFastestInterval(1000);
        		locationClient = new LocationClient(getActivity(),this,this);
        		locationClient.connect();
        		ib_restaurant = (ImageButton) inflatedView.findViewById(R.id.ib_restaurant);
        		ib_local = (ImageButton) inflatedView.findViewById(R.id.ib_local);
        		ib_geolocalize = (ImageButton) inflatedView.findViewById(R.id.ib_geolocalize);
        		mMap.setMyLocationEnabled(true);
        		mMap.setOnCameraChangeListener(this);
        		ib_restaurant.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// Boton para mostrar los marcadores de tipo Restaurant
						botonRestaurant();						
					}
				});
        		ib_local.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// Boton para mostrar los marcadores de tipo Local
						botonLocal();
					}
				});
        		ib_geolocalize.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// Boton para agregar un marcador de geolocalizacion.
						botonGeolocalize();
					}
				});
			}
			else{
				Toast.makeText(getActivity(), "Problema al cargar el Mapa", Toast.LENGTH_SHORT).show();
			}
		}
		else{
			Toast.makeText(getActivity(), "Los servicios De Google Play Services no est�n Listos", Toast.LENGTH_SHORT).show();
		}
		
		

		
		

		return inflatedView;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBundle = savedInstanceState;
	}

	private boolean setUpMapIfNeeded(View inflatedView) {
		if (mMap == null) {
			mMap = ((MapView) inflatedView.findViewById(R.id.map)).getMap();
			mMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter());
			mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
				
				@Override
				public void onInfoWindowClick(Marker marker) {
					InfoWindowClick(marker);
				}
			});
		}
		return(mMap!=null);
	}


	protected void InfoWindowClick(Marker marker) {
		if(hashLocal.containsKey(marker)){
			Intent intent = new Intent(getActivity(), LocalActivity.class);
			intent.putExtra("clave", hashLocal.get(marker));
			startActivity(intent);
		}
		else if(hashRestaurant.containsKey(marker)){
			Intent intent = new Intent(getActivity(), RestaurantActivity.class);
			intent.putExtra("clave", hashRestaurant.get(marker));
			startActivity(intent);
		}
		else{
			Toast.makeText(getActivity(), "Error al buscar el Marcador seleccionado!", Toast.LENGTH_SHORT).show();
		}
		
	}

	@Override
	public void onResume() {
		super.onResume();
		mMapView.onResume();
	}
	

	@Override
	public void onPause() {
		super.onPause();
		mMapView.onPause();
	}

	@Override
	public void onDestroy() {
		mMapView.onDestroy();
		if(locationClient.isConnected()){
    		stopPeriodicUpdates();
    	}
    	locationClient.disconnect();
		super.onDestroy();
	}

	private void stopPeriodicUpdates() {
		locationClient.removeLocationUpdates(this);
		
	}

	@Override
	public void onCameraChange(CameraPosition arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		currentLocation = getLocation();
		startPeriodicUpdates();		
	}

	private void startPeriodicUpdates() {
		locationClient.requestLocationUpdates(locationRequest, this);
		
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
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLocationChanged(Location location) {
		currentLocation = location;
		// Hacer un if para que no cambie si uno ha cambiado sus par de metros
		lastLocation = location;
		LatLng myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
		// Hacer una comprobacion del zoom que tiene puesto el Cliente, para que no cambie cada vez
		mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 13));
		iniciarMarkerRestaurant();
		iniciarMarkerLocal();
		
		
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
	
	public boolean services0K(){
    	int isAvailable;
    	isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
    	
    	if(isAvailable == ConnectionResult.SUCCESS){
    		return true;
    	}
    	else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)){
    		Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isAvailable, getActivity(), GPS_ERRORDIALOG_REQUEST);
    		dialog.show();
    	}
    	else{
    		Toast.makeText(getActivity(), "No se puede conectar con Google Play Services...", Toast.LENGTH_SHORT).show();
    	}
    	return false;
     }
	
	protected void botonRestaurant() {
		if(restaurantPress){
    		ib_restaurant.setBackgroundResource(R.drawable.dialog_button_selector);
			ocultarMarkerRestaurant();
			restaurantPress=false;    			
		}
		else{
			ib_restaurant.setBackgroundResource(R.drawable.imagebutton_selector);
			mostrarMarkerRestaurant();
			restaurantPress = true;
		}
	}
	protected void botonLocal() {
		if(localPress){
			ib_local.setBackgroundResource(R.drawable.dialog_button_selector);
			ocultarMarkerLocal();
			localPress=false;
		}
		else{
			ib_local.setBackgroundResource(R.drawable.imagebutton_selector);
			mostrarMarkerLocal();
			localPress = true;
		}
	}
	protected void botonGeolocalize() {
		currentUser = ParseUser.getCurrentUser();
		if(currentUser == null){
			Toast.makeText(getActivity(), "Debe estar registrado para poder ingresar un Marcador al Mapa", Toast.LENGTH_SHORT).show();
			return;
		}
		Location myLoc = (currentLocation == null) ? lastLocation : currentLocation;
    	if(myLoc == null){
    		Toast.makeText(getActivity(), "Por favor intentelo nuevamente despu�s que su posici�n aparezca",Toast.LENGTH_SHORT).show();
    		return;
    	}
    	mostrarDialogMarker();
	}

	private void mostrarDialogMarker() {
		dialogMarker = new Dialog(getActivity(), R.style.Theme_Dialog_Translucent);
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
			public void onClick(View view) {
				mostrarDialogRestaurant();
			}
		});
        ((ImageButton) dialogMarker.findViewById(R.id.pin_carrito)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				mostrarDialogLocal();
			}
		});
		
	}
	protected void mostrarDialogRestaurant() {
		dialogRestaurant = new Dialog(getActivity(), R.style.Theme_Dialog_Translucent);
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
						Toast.makeText(getActivity(), "Ingrese el nombre del Restaurant", Toast.LENGTH_SHORT).show();
						return;
					}
					else{
						Toast.makeText(getActivity(), "Ingrese alguna descripcion", Toast.LENGTH_SHORT).show();
						return;
					}
				}
				// Si los campos no estan vacios, se guarda el registro en la base de datos.
				Location myLoc = (currentLocation == null) ? lastLocation : currentLocation;
	        	if(myLoc == null){
	        		Toast.makeText(getActivity(), "Problemas con su ubicación, intenelo de nuevo",Toast.LENGTH_SHORT).show();
	        		return;
	        	}
	        	ParseGeoPoint point = new ParseGeoPoint(myLoc.getLatitude(),myLoc.getLongitude());
	        	ParseObject po_marcador = new ParseObject("Restaurant");
			    po_marcador.put("nombre", nombre_restaurant);
			    po_marcador.put("descripcion", descripcion );
			    po_marcador.put("location", point);
			    po_marcador.put("vegetariano", ch1.isChecked());
			    po_marcador.put("vegano",ch2.isChecked());
			    po_marcador.put("creado_por", ParseUser.getCurrentUser());
	        	if(mBitmap!=null){
	        		Bitmap mBitmapScaled = Bitmap.createScaledBitmap(mBitmap, 200 , 300, false);
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
	        	mProgressDialog = new ProgressDialog(getActivity());
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
							mProgressDialog=null;
							Toast.makeText(getActivity(), "Restaurant Agregado", Toast.LENGTH_SHORT).show();
							mMap.clear();
							iniciarMarkerRestaurant();
							iniciarMarkerLocal();
							
						}
						else{
							mProgressDialog.dismiss();
							mProgressDialog=null;
							Toast.makeText(getActivity(), "Error al guardar, intentelo nuevamente", Toast.LENGTH_SHORT).show();
						}
					}
				});
				dialogRestaurant.cancel();
				dialogMarker.cancel();
			}
		});
		
	}
	protected void mostrarDialogLocal() {
		dialogLocal = new Dialog(getActivity(), R.style.Theme_Dialog_Translucent);
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
				globalClass.setLista(null);
				
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
				mostrarDialogAlimento();
			}
		});
		((Button) dialogLocal.findViewById(R.id.aceptar)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EditText et_nombre = (EditText) dialogLocal.findViewById(R.id.et_nombre);
				EditText et_descripcion = (EditText) dialogLocal.findViewById(R.id.et_descripcion);
				final String nombre = et_nombre.getText().toString();
				final String descripcion = et_descripcion.getText().toString();
				if(nombre.equals("") || descripcion.equals("")){
					if(nombre.equals("")){
						Toast.makeText(getActivity(), "Por favor ingrese el nombre.", Toast.LENGTH_SHORT).show();
						return;
					}
					else{
						Toast.makeText(getActivity(), "Por favor ingrese alguna descripcion.", Toast.LENGTH_SHORT).show();
						return;
					}
				}
				lista_prueba = globalClass.getLista();
				
				Location myLoc = (currentLocation == null) ? lastLocation : currentLocation;
	        	if(myLoc == null){
	        		Toast.makeText(getActivity(), "Problemas con su ubicaci�n, intenelo de nuevo",Toast.LENGTH_SHORT).show();
	        		return;
	        	}
	        	final ParseGeoPoint point = new ParseGeoPoint(myLoc.getLatitude(),myLoc.getLongitude());
	        	po_marcador = new ParseObject("Local");
			    po_marcador.put("nombre", nombre);
			    po_marcador.put("descripcion", descripcion );
			    po_marcador.put("location", point);
			    po_marcador.put("creado_por", ParseUser.getCurrentUser());
	        	if(mBitmap!=null){
	        		//* mBitmap.getHeight() / mBitmap.getWidth()
	        		Bitmap mBitmapScaled = Bitmap.createScaledBitmap(mBitmap, 200, 300 , false);
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
	        	mProgressDialog = new ProgressDialog(getActivity());
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
							if(lista_prueba!=null){
								for(ProductoPrecio objeto : lista_prueba){
									ParseObject saveAlimentoLocal = new ParseObject("AlimentoLocal");
									saveAlimentoLocal.put("local", po_marcador);
									saveAlimentoLocal.put("alimento", ParseObject.createWithoutData("Alimento", objeto.getId()));
									saveAlimentoLocal.put("precio",objeto.getPrecio());
									Toast.makeText(getActivity(), objeto.getNombre(), Toast.LENGTH_SHORT).show();
									saveAlimentoLocal.saveInBackground(new SaveCallback() {
										
										@Override
										public void done(ParseException e) {
											if(e==null){
												Toast.makeText(getActivity(), "Se guard� la tienda correctamente", Toast.LENGTH_SHORT ).show();
												mProgressDialog.dismiss();
											}
											else{
												mProgressDialog.dismiss();
												Toast.makeText(getActivity(), "Error al guardar los productos", Toast.LENGTH_SHORT ).show();
											}
											
										}
									});
								}
							}
							mMap.clear();
							iniciarMarkerRestaurant();
							iniciarMarkerLocal();
						}
						else{
							mProgressDialog.dismiss();
							Toast.makeText(getActivity(), "Error al guardar la Tienda, por favor intentelo nuevamente.", Toast.LENGTH_SHORT ).show();
						}
						
					}
				});
				dialogLocal.cancel();
				dialogMarker.cancel();
			}
		});
	}
	
	protected void mostrarDialogAlimento() {
		dialogAlimento = new Dialog(getActivity(), R.style.Theme_Dialog_Translucent);
		dialogAlimento.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogAlimento.setContentView(R.layout.dialog_lista_alimentos);
		dialogAlimento.setCanceledOnTouchOutside(false);
		dialogAlimento.show();
		listView = (ListView) dialogAlimento.findViewById(R.id.lv_alimentos);
		alimentosAdapter = new CustomAdapterAlimentos(getActivity(), dialogLocal, dialogAlimento);
		
		
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

	 @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		 if(requestCode == 0 ){
			 switch(resultCode){
			 case Activity.RESULT_OK:
				 if(imageFile.exists()){
					 ImageButton imgbtn;
					 TextView tv_foto;
					 if(dRestaurant){
						 imgbtn = (ImageButton) dialogRestaurant.findViewById(R.id.camara);
						 imgbtn.setImageResource(R.drawable.adjunto);
						 tv_foto = (TextView) dialogRestaurant.findViewById(R.id.tv_camara);
						 tv_foto.setText("Imagen Adjunta");
						 mBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
						 dRestaurant=false;
						 imageFile=null;
					 }
					 
					 if(dLocal){
						 imgbtn = (ImageButton) dialogLocal.findViewById(R.id.camara);
						 imgbtn.setImageResource(R.drawable.ic_action_attachment);
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
					 mBitmap=null;
				 }
				 if(dLocal){
					 imgbtn = (ImageButton) dialogLocal.findViewById(R.id.camara);
					 imgbtn.setImageResource(R.drawable.ic_action_camera);
					 dLocal=false;
					 imageFile=null;
					 mBitmap=null;
				 }
				 
				 break;
				 
			 }
		}
	 }
	
	 
	private void ocultarMarkerRestaurant(){
	    	@SuppressWarnings("rawtypes")
			Iterator mIterator = hashRestaurant.keySet().iterator();
	    	while(mIterator.hasNext()){
	    		Marker marker = (Marker) mIterator.next();
	    		@SuppressWarnings("unused")
				String idRes = (String) hashRestaurant.get(marker);
	    		marker.setVisible(false);
	    		
	    	}
	    }
	private void mostrarMarkerRestaurant(){
	    	@SuppressWarnings("rawtypes")
			Iterator mIterator = hashRestaurant.keySet().iterator();
	    	while(mIterator.hasNext()){
	    		Marker marker = (Marker) mIterator.next();
	    		@SuppressWarnings("unused")
				String idRes = (String) hashRestaurant.get(marker);
	    		marker.setVisible(true);
	    	}
	    }
	private void ocultarMarkerLocal(){
	    	@SuppressWarnings("rawtypes")
			Iterator mIterator = hashLocal.keySet().iterator();
	    	while(mIterator.hasNext()){
	    		Marker marker = (Marker) mIterator.next();
	    		@SuppressWarnings("unused")
				String idRes = (String) hashLocal.get(marker);
	    		marker.setVisible(false);
	    		
	    	}
	    }
	@SuppressWarnings("rawtypes")
	private void mostrarMarkerLocal(){
	    	Iterator mIterator = hashLocal.keySet().iterator();
	    	while(mIterator.hasNext()){
	    		Marker marker = (Marker) mIterator.next();
	    		@SuppressWarnings("unused")
				String idRes = (String) hashLocal.get(marker);
	    		marker.setVisible(true);
	    	}
	    }
	private void iniciarMarkerRestaurant() {
			hashNombreRestaurant = null;
			hashRestaurant = null;
			hashNombreRestaurant = new HashMap<Marker, String>();
			hashRestaurant = new HashMap<Marker, String>();
			ParseQuery<ParseObject> query = ParseQuery.getQuery("Restaurant");
			Location myLoc = (currentLocation == null) ? lastLocation : currentLocation;
	    	if(myLoc == null){
	    		Toast.makeText(getActivity(), "Problemas con su ubicaci�n, intenelo de nuevo",Toast.LENGTH_SHORT).show();
	    		return;
	    	}
	    	ParseGeoPoint point = new ParseGeoPoint(myLoc.getLatitude(),myLoc.getLongitude());
	    	query.whereWithinKilometers("location", point, maxDistanciaBusqueda);
	    	query.findInBackground(new FindCallback<ParseObject>() {
				
				@Override
				public void done(List<ParseObject> list, ParseException e) {
					if(e==null){
						Boolean visible = true;
						if(!restaurantPress){
							visible=false;
						}
						for(ParseObject restaurant: list){
							LatLng position = new LatLng(restaurant.getParseGeoPoint("location").getLatitude(), restaurant.getParseGeoPoint("location").getLongitude());
							MarkerOptions markerOption = new MarkerOptions();
							markerOption.position(position);
							markerOption.visible(visible);
							markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.point_restaurant));
							Marker marker = mMap.addMarker(markerOption);
							hashRestaurant.put(marker, restaurant.getObjectId());
							hashNombreRestaurant.put(marker,restaurant.getString("nombre"));
						}
					}
					
				}
			});
			
			
		}
	private void iniciarMarkerLocal() {
			hashNombreLocal=null;
			hashLocal = null;
			hashNombreLocal = new HashMap<Marker, String>();
			hashLocal = new HashMap<Marker, String>();
			ParseQuery<ParseObject> query = ParseQuery.getQuery("Local");
			Location myLoc = (currentLocation == null) ? lastLocation : currentLocation;
	    	if(myLoc == null){
	    		Toast.makeText(getActivity(), "Problemas con su ubicaci�n, intenelo de nuevo",Toast.LENGTH_SHORT).show();
	    		return;
	    	}
	    	ParseGeoPoint point = new ParseGeoPoint(myLoc.getLatitude(),myLoc.getLongitude());
	    	query.whereWithinKilometers("location", point, maxDistanciaBusqueda);
	    	query.findInBackground(new FindCallback<ParseObject>() {
				
				@Override
				public void done(List<ParseObject> list, ParseException e) {
					if(e==null){
						Boolean visible = true;
						if(!localPress){
							visible = false;
						}
						for(ParseObject local: list){
							LatLng position = new LatLng(local.getParseGeoPoint("location").getLatitude(), local.getParseGeoPoint("location").getLongitude());
							MarkerOptions markerOption = new MarkerOptions();
							markerOption.position(position);
							markerOption.visible(visible);
							markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.point_marker));
							Marker marker = mMap.addMarker(markerOption);
							hashLocal.put(marker, local.getObjectId());
							hashNombreLocal.put(marker, local.getString("nombre"));
						}
					}
					
				}
			});
			
			
		}
	public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter{
		 
		 public MarkerInfoWindowAdapter(){
	        }
		 
		@Override
		public View getInfoContents(Marker marker) {
			View v;
			LayoutInflater inflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.infowindow_layout, null);
			String nombre, snnipet;
			ImageView icon = (ImageView) v.findViewById(R.id.icon);
			if(hashRestaurant.containsKey(marker)){
				nombre = hashNombreRestaurant.get(marker);
				snnipet= "Restaurant";
				icon.setImageResource(R.drawable.restaurant);
			}
			else if(hashLocal.containsKey(marker)){
				nombre = hashNombreLocal.get(marker);
				snnipet = "Tienda";
				icon.setImageResource(R.drawable.local);
			}
			else{
				Toast.makeText(getActivity(), "No se encontró el Marcador en los registros (?)", Toast.LENGTH_SHORT).show();
				return null;
			}
			
			TextView tv_titulo = (TextView) v.findViewById(R.id.tv_titulo);
			TextView tv_snnipet = (TextView) v.findViewById(R.id.tv_snnipet);
			tv_titulo.setText(nombre);
			tv_snnipet.setText(snnipet);
			return v;
		}

		@Override
		public View getInfoWindow(Marker marker) {
			return null;
		}
		 
	 }

}
