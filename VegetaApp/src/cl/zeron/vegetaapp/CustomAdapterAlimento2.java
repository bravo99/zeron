package cl.zeron.vegetaapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class CustomAdapterAlimento2 extends ParseQueryAdapter<ParseObject>{
	TextView titleTextView ;  
	Activity act;
		public CustomAdapterAlimento2(Activity activity) {
			
			// Use the QueryFactory to construct a PQA that will only show
			// Todos marked as high-pri
			super( activity, new ParseQueryAdapter.QueryFactory<ParseObject>() {
				@SuppressWarnings({ "unchecked", "rawtypes" })
				public ParseQuery create() {
					ParseQuery query = new ParseQuery("Alimento");
					query.orderByAscending("nombre");
					return query;
				}
			});
			
			this.act=activity;
			
		}
      
		public CustomAdapterAlimento2(final Activity activity, final String a, final String b, final String c) {
			
			// Use the QueryFactory to construct a PQA that will only show
			// Todos marked as high-pri
			super( activity, new ParseQueryAdapter.QueryFactory<ParseObject>() {
				@SuppressWarnings({ "unchecked", "rawtypes" })
				public ParseQuery create() {
					ParseQuery<ParseObject> nombreInicioUpper = ParseQuery.getQuery("Alimento");
					nombreInicioUpper.whereContains("nombre", a);
					
					ParseQuery<ParseObject> nombre_N = ParseQuery.getQuery("Alimento");
					nombre_N.whereContains("nombre", b);
					
					ParseQuery<ParseObject> nombreLower = ParseQuery.getQuery("Alimento");
					nombreLower.whereContains("nombre", c);
					
					List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
					queries.add(nombreInicioUpper);
					queries.add(nombre_N);
					queries.add(nombreLower);
					
					ParseQuery<ParseObject> mainquery = ParseQuery.or(queries);
						try {
							if( mainquery.count() == 0){
								Toast.makeText(activity.getApplicationContext(),
										"Busqueda no exitosa", Toast.LENGTH_SHORT).show();
							}
						} catch (ParseException e) {
							e.printStackTrace();
					     }
						
					return mainquery;
				}
			});
			this.act=activity;
		}
		// Customize the layout by overriding getItemView

		
		
		
		
		@Override
		public View getItemView(final ParseObject object, View v, ViewGroup parent) {
			
			
			if (v == null) {
				v = View.inflate(getContext(), R.layout.urgent_item_alimento, null);
			}

			super.getItemView(object, v, parent);
            
			
			
			// Add and download the image
			final ParseImageView alimentoImage = (ParseImageView) v.findViewById(R.id.icon_ali);
			
			ParseFile imageFile  = object.getParseFile("imagen");
			if (imageFile != null) {
				alimentoImage.setParseFile(imageFile);
				alimentoImage.loadInBackground();
			}
	        
			// Add the title view
			titleTextView= (TextView) v.findViewById(R.id.nombre_ali);
			
			titleTextView.setText(object.getString("nombre"));
			
			v.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					Bitmap imBitmap = ((BitmapDrawable) alimentoImage.getDrawable()).getBitmap();
					
					String ruta = guardarImagen( getContext(), "imagen", imBitmap);
					Bundle bundle = new Bundle();
					bundle.putString("NOMBRE",object.getString("nombre"));
					bundle.putString("IMAG", ruta);
					bundle.putString("INFO",object.getString("descripcion"));
					
					
					Intent i = new Intent(act, AlimentoResultListActivity.class);
					i.putExtras(bundle);
					act.startActivity(i);
				}
			});
			
			return v;
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
