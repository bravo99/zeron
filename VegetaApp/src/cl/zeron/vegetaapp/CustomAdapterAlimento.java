package cl.zeron.vegetaapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class CustomAdapterAlimento extends ParseQueryAdapter<ParseObject> {
	TextView titleTextView ;  
	Activity act;
		public CustomAdapterAlimento(Activity activity,final ParseObject receta) {
			
			// Use the QueryFactory to construct a PQA that will only show
			// Todos marked as high-pri
			super( activity, new ParseQueryAdapter.QueryFactory<ParseObject>() {
				@SuppressWarnings({ "unchecked", "rawtypes" })
				public ParseQuery create() {
					ParseQuery query = new ParseQuery("AlimentoReceta");
					query.whereEqualTo("receta",receta);
									
					return query;
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
			ParseFile imageFile = null;
			try {
				imageFile = object.getParseObject("ingrediente").fetchIfNeeded().getParseFile("imagen");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
					
			if (imageFile != null) {
				alimentoImage.setParseFile(imageFile);
				alimentoImage.loadInBackground();
				alimentoImage.setImageResource(R.drawable.ic_launcher3);
			}
	        
			// Add the title view
			titleTextView= (TextView) v.findViewById(R.id.nombre_ali);
			try {
				titleTextView.setText(object.getParseObject("ingrediente").fetchIfNeeded()
					      .getString("nombre"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			v.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					Bitmap imBitmap = ((BitmapDrawable) alimentoImage.getDrawable()).getBitmap();
					
					String ruta = guardarImagen( getContext(), "imagen", imBitmap);
					Bundle bundle = new Bundle();
					try {
						bundle.putString("NOMBRE",object.getParseObject("ingrediente").fetchIfNeeded()
							      .getString("nombre"));
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					bundle.putString("IMAG", ruta);
					try {
						bundle.putString("INFO",object.getParseObject("ingrediente").fetchIfNeeded()
								.getString("descripcion"));
					} catch (ParseException e) {
					
						e.printStackTrace();
					}
					bundle.putString("ID",object.getParseObject("ingrediente").getObjectId());
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
