package cl.zeron.vegetaapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AlimentoResultListActivity extends ActionBarActivity{
	
	TextView tv_nombre;
	TextView info;
	ImageView imagen;
	Bundle bundle;
	private String id_alimento;
	private ParseFile imageFile;
	private LinearLayout mLinear;
	private ParseObject alimentoParse;
	private ArrayList<RecetaIngrediente> lista_recetas;
	private List<ParseObject> lista_receta;
	ParseUser currentUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alimento_result_list);
		
		currentUser = ParseUser.getCurrentUser();
		bundle= getIntent().getExtras();
		
		tv_nombre=(TextView)findViewById(R.id.nom_alimento);
		info=(TextView)findViewById(R.id.info_alimento);
		imagen=(ImageView)findViewById(R.id.image_alimento);
		mLinear = (LinearLayout) findViewById(R.id.linear_listview_recetas);
		if(getIntent().hasExtra("NOMBRE")){
			tv_nombre.setText(getIntent().getStringExtra("NOMBRE"));
		}
		if(getIntent().hasExtra("INFO")){
			info.setText(getIntent().getStringExtra("INFO"));
		}
		if(getIntent().hasExtra("IMAG")){
			imagen.setImageBitmap(BitmapFactory.decodeFile(getIntent().getStringExtra("IMAG")));
		}
		if(getIntent().hasExtra("ID_OCULTO")){
			id_alimento = getIntent().getStringExtra("ID_OCULTO");
		}
		
		if(getIntent().hasExtra("ID")){
			id_alimento = getIntent().getStringExtra("ID");
			ParseQuery<ParseObject> alimentoquery = ParseQuery.getQuery("Alimento");
			alimentoquery.getInBackground(id_alimento, new GetCallback<ParseObject>() {
				
				@Override
				public void done(ParseObject alimento, ParseException e) {
					if(e==null){
						alimentoParse = alimento;
						tv_nombre.setText(alimento.getString("nombre"));
						info.setText(alimento.getString("descripcion"));
						imageFile = (ParseFile) alimento.get("imagen");
						imageFile.getDataInBackground(new GetDataCallback() {
							
							@Override
							public void done(byte[] data, ParseException arg1) {
								Bitmap   bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
								imagen.setImageBitmap(bmp);
							}
						});
					}
				}
			});
		}
		
		ParseQuery<ParseObject> alimentoRecetaquery = ParseQuery.getQuery("AlimentoReceta");
		alimentoRecetaquery.whereEqualTo("ingrediente", ParseObject.createWithoutData("Alimento", id_alimento));
		alimentoRecetaquery.include("receta");
		alimentoRecetaquery.findInBackground(new FindCallback<ParseObject>() {
			
			@Override
			public void done(List<ParseObject> lista_alimentoReceta, ParseException e) {
				if(e==null){
					lista_recetas = new ArrayList<RecetaIngrediente>();
					lista_receta = new ArrayList<ParseObject>();
					for(ParseObject object : lista_alimentoReceta){
						ParseObject receta = object.getParseObject("receta");
						lista_receta.add(receta);
					}
					Comparator<ParseObject> comparador = new Comparator<ParseObject>() {
						
						@Override
						public int compare(ParseObject a, ParseObject b) {
							int resultado = b.getString("valoracion").compareTo(a.getString("valoracion"));
							if(resultado!=0){
								return resultado;
							}
							return resultado;
						}
					};
					Collections.sort(lista_receta, comparador);
					if(lista_receta.size() <= 10){
						for(ParseObject object : lista_receta){
							LayoutInflater inflater = null;
		                     inflater = (LayoutInflater) getApplicationContext()
		                                  .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		                     final View mLinearView = inflater.inflate(R.layout.urgent_item1,null);
		                     ParseImageView icon = (ParseImageView) mLinearView.findViewById(R.id.icon);
		                     TextView nombre = (TextView) mLinearView.findViewById(R.id.nombre);
		                     TextView person = (TextView) mLinearView.findViewById(R.id.person);
		                     TextView time = (TextView) mLinearView.findViewById(R.id.time);
		                     ParseFile image = object.getParseFile("imagen");
		                     if(image != null){
		                    	 icon.setParseFile(image);
		                    	 icon.loadInBackground();
		                     }
		                     nombre.setText(object.getString("nombre"));
		                     person.setText(object.getString("personas"));
		                     time.setText(object.getString("tiempo_preparacion"));
		                     mLinear.addView(mLinearView);
						}
					}
				}
			}
		});

		
		
		
		
		
		
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		getSupportActionBar().setHomeButtonEnabled(true);
		//getSupportActionBar().setTitle(bundle.getString("TITULO"));
		
		
	}
	
	@Override
	public Intent getSupportParentActivityIntent() {
		onBackPressed();

		Intent newIntent=null;
        return newIntent;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.icono_alim, menu);
		 
	    return super.onCreateOptionsMenu(menu);
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_edit:
			
			if(currentUser != null){
				ParseQuery<ParseObject> queryElimOnLine = ParseQuery.getQuery("Alimento");
    			queryElimOnLine.getInBackground(bundle.getString("ID_OCULTO"), new GetCallback<ParseObject>() {
    			public void done(final ParseObject object, ParseException e) {
    			     if (e == null) {
    			    	 
    			    	 //COMPROBAR SI ES EL CREADOR
    			    	 ParseUser creador = object.getParseUser("creado_por");
    			    	 if(creador.getObjectId().equals(currentUser.getObjectId())){
    			    		 Intent intent = new Intent(getApplicationContext(),AlimentoEditActivity.class);
    			    		 intent.putExtras(bundle);
    			    		 startActivity(intent);
    			    	 }
    			    	 else{
    			    		 Toast.makeText(getApplicationContext(), 
    			    				 "Solo el creador puede editar", Toast.LENGTH_SHORT).show();
    			    	 }
    			     }		
    			 }});
    			
			}
			return true;	
			
			
		case R.id.action_place:
			Intent intent = new Intent(AlimentoResultListActivity.this, MainActivity.class);
			intent.putExtra("fragment", "mapa");
			intent.putExtra("ID_INGREDIENTE_MAPA", id_alimento);
			startActivity(intent);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
		
	}
	
	private String guardarImagen (Context context, String nombre, Bitmap imagen){
	    ContextWrapper cw = new ContextWrapper(context);
	    File dirImages = cw.getDir("Imagenes", Context.MODE_PRIVATE);
	    
		File myPath = new File(dirImages, nombre + ".png");
	     
	    FileOutputStream fos = null;
	    try{
	        fos = new FileOutputStream(myPath);
	        imagen.compress(Bitmap.CompressFormat.JPEG, 100, fos);
	        fos.flush();
	    }catch (FileNotFoundException ex){
	        ex.printStackTrace();
	    }catch (IOException ex){
	        ex.printStackTrace();
	    }
	    return myPath.getAbsolutePath();
	}
	
}
