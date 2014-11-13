package cl.zeron.vegetaapp;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class AlimentoActivity extends ActionBarActivity{
	ImageView iv;
	EditText nombre;
	EditText descripcion;
	Button btnAction;
	Button guardar;
	private String name = "";
	private Spinner origen;
	private static int TAKE_PICTURE = 1;
	private static int SELECT_PICTURE = 2;
	final String[] origenes = {"Camara","Galeria"};	
	private ParseUser currentUser;
		
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.nuevo_alim);
		currentUser = ParseUser.getCurrentUser();
		
		nombre = (EditText)findViewById(R.id.tit_alim);
		
		descripcion = (EditText)findViewById(R.id.desc_alim);
				
		iv = (ImageView)findViewById(R.id.ima_ali);
		
		origen=(Spinner)findViewById(R.id.sp_orig);
		ArrayAdapter<String> adaptadorSpinner2 =new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, origenes);
		adaptadorSpinner2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		origen.setAdapter(adaptadorSpinner2);
		
		name = Environment.getExternalStorageDirectory() + "/test.jpg";
		btnAction= (Button)findViewById(R.id.b_ali);
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
        
        guardar = (Button)findViewById(R.id.guardar_ali);
        guardar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				guardar();
				
			}
		});
		
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setTitle("Nuevo Alimento" );
		
	}
	
	
	/**
     * Funcion que se ejecuta cuando concluye el intent en el que se solicita una imagen
     * ya sea de la camara o de la galería
     */
    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	/**
    	 * Se revisa si la imagen viene de la camara (TAKE_PICTURE) o de la galería (SELECT_PICTURE)
    	 */
    	if (requestCode == TAKE_PICTURE) {
    		
    		switch(resultCode){
    			case Activity.RESULT_OK:
    				/**
    	    		 * Si se reciben datos en el intent tenemos una vista previa (thumbnail)
    	    		 */
    	    		if (data != null) {
    	    			/**
    	    			 * En el caso de una vista previa, obtenemos el extra �data� del intent y 
    	    			 * lo mostramos en el ImageView
    	    			 */
    	    			if (data.hasExtra("data")) { 
    	    				ImageView iv = (ImageView)findViewById(R.id.ima_ali);
    	    				
    	    				iv.setImageBitmap((Bitmap) data.getParcelableExtra("data"));
    	    				
    	    			}
    	    		/**
    	    		 * De lo contrario es una imagen completa
    	    		 */    			
    	    		} else {
    	    			/**
    	    			 * A partir del nombre del archivo ya definido lo buscamos y creamos el bitmap
    	    			 * para el ImageView
    	    			 */
    	    			//ImageView iv = (ImageView)findViewById(R.id.imgView);
    	    			iv.setImageBitmap(BitmapFactory.decodeFile(name));
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
					
    		}
    	}
    }
	
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD) protected void guardar() {
		
		Drawable drawable = iv.getDrawable();
		Bitmap bitmap = (Bitmap)((BitmapDrawable) drawable).getBitmap();
		Bitmap mBitmapScaled = Bitmap.createScaledBitmap(bitmap, 500, 500 * bitmap.getHeight() / bitmap.getWidth(), false);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		//rotatedScaledMealImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		mBitmapScaled.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		
		//mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		byte[] data = stream.toByteArray();    
		ParseFile imageFile = new ParseFile("image.jpg", data);
		imageFile.saveInBackground();
		
		
		
		
		if(nombre.getText().toString().isEmpty() || descripcion.getText().toString().isEmpty()){
			
			Toast.makeText(this, "Complete todos los campos" , Toast.LENGTH_SHORT).show();
			
		
		}else{
			String nom_rec = (nombre.getText().toString().substring(0, 1).toUpperCase()).concat(
					(nombre.getText().toString().substring(1, nombre.getText().length())).toLowerCase());
			
			final ParseObject alimento = new ParseObject("Alimento");
			alimento.put("nombre", nom_rec);
			alimento.put("descripcion", descripcion.getText().toString());
			alimento.put("imagen", imageFile);
			
			
			alimento.put("creado_por",currentUser );
			
			alimento.saveInBackground(new SaveCallback() {
				
				
				@Override
				public void done(ParseException arg0) {
					Toast.makeText(getApplicationContext(), "Nuevo alimento agregado", Toast.LENGTH_SHORT).show();
					Intent i = new Intent(getApplicationContext(),MainActivity.class);
					startActivity(i);
		            finish();
					
				}});		
	
	
		}
		
	}

	
	

	@Override
	public Intent getSupportParentActivityIntent() {
		onBackPressed();

	    Intent newIntent=null;
        return newIntent;
	}

}
