package cl.zeron.vegetaapp;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.GINGERBREAD) public class ActivityNuevaReceta extends ActionBarActivity{
	EditText titulo;
	ImageView iv;
	Spinner sp_personas;
	Spinner sp_categorias;
	EditText tiempo;
	EditText preparacion;
	ImageButton addIng;
	Button btnAction;
	Button agregar;
	private ParseUser currentUser;
	private Dialog dialogAlimento;
	private String name = "";
	private Spinner origen;
	private LinearLayout mLinearView;
	private static int TAKE_PICTURE = 1;
	private static int SELECT_PICTURE = 2;
	final String[] origenes = {"Camara","Galeria"};	
	
	final String[] categorias = {"Aperitivo","Ensalada","Pastas","Postres","Principal","Sopa","Salsas" };
	final String[] personas = {"1","2","3","4","5","6","7","8"};
	ArrayAdapter<String> adapSpinnerCat;
	ArrayAdapter<String> adapSpinnerPer;
	private GlobalClass globalClass;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nueva_receta);
		
		//Adpatadores
		globalClass = (GlobalClass) getApplicationContext();
		adapSpinnerCat =new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, categorias);
		adapSpinnerCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		adapSpinnerPer =new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, personas);
		adapSpinnerPer.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		//instanciar
		
		currentUser=ParseUser.getCurrentUser();
		mLinearView = (LinearLayout) findViewById(R.id.linear_listview);
		titulo = (EditText)findViewById(R.id.tit_receta);
		tiempo = (EditText)findViewById(R.id.tiempo_receta);
		preparacion = (EditText)findViewById(R.id.prep_receta);
		
		sp_personas = (Spinner)findViewById(R.id.personas_receta);
		sp_personas.setAdapter(adapSpinnerPer);
		
		sp_categorias = (Spinner)findViewById(R.id.categoria_receta);
		sp_categorias.setAdapter(adapSpinnerCat);
		
		
		
		
		addIng = (ImageButton)findViewById(R.id.add_Ingrediente);
		addIng.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				agregarIngrediente();
				
		}});
		
		iv = (ImageView)findViewById(R.id.image_receta);
		
		
		
		
		origen=(Spinner)findViewById(R.id.sp_orig_cam);
		ArrayAdapter<String> adaptadorSpinner2 =new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, origenes);
		adaptadorSpinner2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		origen.setAdapter(adaptadorSpinner2);
		
		name = Environment.getExternalStorageDirectory() + "/test.jpg";
		btnAction=(Button)findViewById(R.id.b_receta);
		
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
		
		
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setTitle("Nueva Receta" );
		
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
    	    				//ImageView iv = (ImageView)findViewById(R.id.ima_ali);
    	    				
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
    				iv.setImageResource(R.drawable.ic_launcher);
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
		    		break;
		    		
				case Activity.RESULT_CANCELED:
					iv.setImageResource(R.drawable.ic_launcher);
					break;
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
		
		
		
		
		if(titulo.getText().toString().isEmpty() || tiempo.getText().toString().isEmpty() 
				|| preparacion.getText().toString().isEmpty()){
			Toast.makeText(this, "Complete todos los campos" , Toast.LENGTH_SHORT).show();
		
		}else{
			String nom_rec = (titulo.getText().toString().substring(0, 1).toUpperCase()).concat(
					(titulo.getText().toString().substring(1, titulo.getText().length())).toLowerCase());
			
			final ParseObject receta = new ParseObject("Receta");
			receta.put("nombre", nom_rec);
			int position = sp_categorias.getSelectedItemPosition();
			receta.put("categoria", categorias[position]);
			receta.put("personas", sp_personas.getSelectedItem().toString());
			receta.put("vecesCalif", 0);
			receta.put("denuncias", 0);
			receta.put("valoracion", "5");
			receta.put("tiempo_preparacion", tiempo.getText().toString());
			receta.put("preparacion", preparacion.getText().toString());
			receta.put("creado_por", currentUser);
			
			 receta.put("imagen",imageFile);
					
			receta.saveInBackground(new SaveCallback() {
				
				@Override
				public void done(ParseException e) {
					if(e==null){
						ArrayList<IngredienteCantidad> lista_final = globalClass.getListaIngrediente();
						for(IngredienteCantidad ingrediente : lista_final){
							ParseObject ingredienteReceta = new ParseObject("AlimentoReceta");
							ingredienteReceta.put("ingrediente", ParseObject.createWithoutData("Alimento", ingrediente.getId()));
							ingredienteReceta.put("receta", receta);
							ingredienteReceta.put("medida", ingrediente.getMedida());
							ingredienteReceta.put("cantidad", ingrediente.getCantidad());
							ingredienteReceta.saveInBackground();
						}
						Toast.makeText(getApplicationContext(), 
								"Nueva receta disponible", Toast.LENGTH_SHORT).show();	
						Intent i = new Intent(getApplicationContext(),MainActivity.class);
						startActivity(i);
					}
					
				}
			});
					
			
			
			
		}
		
	}

	

	protected void agregarIngrediente() {
		dialogAlimento = new Dialog(ActivityNuevaReceta.this, R.style.Theme_Dialog_Translucent);
		dialogAlimento.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogAlimento.setContentView(R.layout.dialog_lista_alimentos);
		dialogAlimento.setCanceledOnTouchOutside(false);
		dialogAlimento.show();
		ListView listView = (ListView) dialogAlimento.findViewById(R.id.lv_alimentos);
		AdapterAlimentoIngrediente adapter = new AdapterAlimentoIngrediente(ActivityNuevaReceta.this, mLinearView);
		listView.setAdapter(adapter);
		adapter.loadObjects();
	}

	@Override
	public Intent getSupportParentActivityIntent() {
		onBackPressed();

	    Intent newIntent=null;
        return newIntent;
	}
	
	
	public void btnGuardar(View view){
		guardar();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
        case R.id.menu_guardar:
        	
        	guardar();
        	
            return true;
            
        
            
        default:
            return super.onOptionsItemSelected(item);
    }
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
      getMenuInflater().inflate(R.menu.menu_guardar, menu);
        
	  return super.onCreateOptionsMenu(menu);
		
	}


}
