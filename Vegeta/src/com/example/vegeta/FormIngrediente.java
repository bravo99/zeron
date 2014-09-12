package com.example.vegeta;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseFile;
import com.parse.ParseObject;

@SuppressLint("DefaultLocale")
public class FormIngrediente extends Activity implements OnClickListener{
	Bitmap im;
	Button agregar;
	Intent a;
	EditText nIngrediente,descripcion;
	Spinner unidad,origen;
	String[] info;
	Toast toast1; 
	ArrayList<Bitmap> foto;
	final String[] origenes = {"Camara","Galeria"};	
	final String[] unidades = {"A gusto","Unidad","Taza","Gotas","Gramos","Cucharadita"};
	ArrayList<ParseObject> posts;
	 final static String ACT_INFO = "com.example.vegeta.ResultIngrediente";
	private static int TAKE_PICTURE = 1;
	private static int SELECT_PICTURE = 2;
	private String name = "";
	ImageView iv;
	
	/*
	 * buscar como autocompletar con registros d la bd
	 */
	
	@Override
	protected void onCreate(Bundle savedInstanceState)  {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.form_ingrediente);
		init( this);
		
	}
	
	@SuppressLint("ShowToast")
	public void init(final Context ct){
		iv = (ImageView)findViewById(R.id.imgView);
		agregar=(Button)findViewById(R.id.button1);
		agregar.setOnClickListener(this);
		nIngrediente=(EditText)findViewById(R.id.editText1);
		descripcion =(EditText)findViewById(R.id.editText2);
		unidad=(Spinner)findViewById(R.id.spinner1);
		origen=(Spinner)findViewById(R.id.spinner2);
		info = new String[4];
		toast1 =  Toast.makeText(getApplicationContext(),"Debes rellenar todos los campos", Toast.LENGTH_SHORT);
		posts = new ArrayList<ParseObject>();
		
		ArrayAdapter<String> adaptadorSpinner2 =new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, origenes);
		adaptadorSpinner2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		origen.setAdapter(adaptadorSpinner2);
		
		ArrayAdapter<String> adaptadorSpinner1 =new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, unidades);
		adaptadorSpinner1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		unidad.setAdapter(adaptadorSpinner1);
		
		
			
        
        /***
         * codigo para la imagen
         */
        name = Environment.getExternalStorageDirectory() + "/test.jpg";

        Button btnAction = (Button)findViewById(R.id.btnPic);
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
	
	/**
     * Funci�n que se ejecuta cuando concluye el intent en el que se solicita una imagen
     * ya sea de la c�mara o de la galer�a
     */
    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	/**
    	 * Se revisa si la imagen viene de la c�mara (TAKE_PICTURE) o de la galer�a (SELECT_PICTURE)
    	 */
    	if (requestCode == TAKE_PICTURE) {
    		/**
    		 * Si se reciben datos en el intent tenemos una vista previa (thumbnail)
    		 */
    		if (data != null) {
    			/**
    			 * En el caso de una vista previa, obtenemos el extra �data� del intent y 
    			 * lo mostramos en el ImageView
    			 */
    			if (data.hasExtra("data")) { 
    				//ImageView iv = (ImageView)findViewById(R.id.imgView);
    				
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
    	/**
    	 * Recibimos el URI de la imagen y construimos un Bitmap a partir de un stream de Bytes
    	 */
    	} else if (requestCode == SELECT_PICTURE){
    		Uri selectedImage = data.getData();
    		InputStream is;
    		try {
    			is = getContentResolver().openInputStream(selectedImage);
    	    	BufferedInputStream bis = new BufferedInputStream(is);
    	    	Bitmap bitmap = BitmapFactory.decodeStream(bis); 
    	    	
    	    	iv.setImageBitmap(bitmap);	
 
    		} catch (FileNotFoundException e) {}
    	}
    }
	
    /***
	 * Guardamos los datos en la BD al hacer click en el boton correspondiente
	 */	
	@Override	
	public void onClick(View v) {
		
		ParseObject ingrediente = new ParseObject("Ingrediente");
		
		
		
		// Save image file
		Drawable drawable = iv.getDrawable();
		Bitmap bitmap = (Bitmap)((BitmapDrawable) drawable).getBitmap();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		byte[] data = stream.toByteArray();                
		ParseFile imageFile = new ParseFile("image.jpg", data);
		imageFile.saveInBackground();
		
		
			
		
		if(nIngrediente.getText().toString() != null && (!nIngrediente.getText().toString().equals("")) && 
		  descripcion.getText().toString() != null &&(!descripcion.getText().toString().equals(""))
          
		  )
		  { 
			info[0]= nIngrediente.getText().toString();
			info[1]=descripcion.getText().toString();
			info[2]="Form";
			info[3] = name;
			
			String pos_0= info[0].substring(0, 1).toUpperCase();
			System.out.println(pos_0);
			String pos_1_n = info[0].substring(1, info[0].length()).toLowerCase();
			System.out.println(pos_1_n);
            info[0]= pos_0.concat(pos_1_n);			
            
			ingrediente.put("nombre", info[0]);
			ingrediente.put("descripcion", descripcion.getText().toString() );
			ingrediente.put("uniIngrediente", unidad.getSelectedItem().toString());
			ingrediente.put("imagen",imageFile);
			//siempre autor sera vegetatest para este entregable
			
			ingrediente.put("autor", "vegetatest" );
			
			ingrediente.saveInBackground();
			
			
			a = new Intent(this, ResultIngrediente.class);
			a.putExtra(ACT_INFO, info);
			
			startActivity(a);
		}	
		else 
			 
	        toast1.show();
		
		
	}
	
	


}
