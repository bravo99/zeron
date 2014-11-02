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
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class CustomAdapterRecetas extends ParseQueryAdapter<ParseObject> {
   Activity act;
  
	public CustomAdapterRecetas(Activity activity,final String cat) {
		
		// Use the QueryFactory to construct a PQA that will only show
		// Todos marked as high-pri
		super(activity, new ParseQueryAdapter.QueryFactory<ParseObject>() {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public ParseQuery create() {
				ParseQuery query = new ParseQuery("Receta");
				query.whereEqualTo("categoria",cat);
				return query;
			}
		});
		this.act=activity;
		
	}
	

	// Customize the layout by overriding getItemView

	
	@Override
	public View getItemView(final ParseObject object, View v, ViewGroup parent) {
		if (v == null) {
			v = View.inflate(getContext(), R.layout.urgent_item1, null);
		}

		super.getItemView(object, v, parent);

		// Add and download the image
		final ParseImageView recetaImage = (ParseImageView) v.findViewById(R.id.icon);
		ParseFile imageFile = object.getParseFile("imagen");
		
				
		if (imageFile != null) {
			recetaImage.setParseFile(imageFile);
			recetaImage.loadInBackground();
		}
		
		
		
        
		// Add the title view
		TextView titleTextView = (TextView) v.findViewById(R.id.nombre);
		titleTextView.setText(object.getString("nombre"));

		
		//add the time
		TextView timeTextView = (TextView)v.findViewById(R.id.time);
		timeTextView.setText(object.getString("tiempo_preparacion"));
		
		
		//add count person
		TextView personTextView = (TextView)v.findViewById(R.id.person);
		personTextView.setText(object.getString("personas"));
		// Add a reminder of how long this item has been outstanding
		//TextView timestampView = (TextView) v.findViewById(R.id.descripcion);
		//timestampView.setText(object.getCreatedAt().toString());
		
		v.setOnClickListener(new OnClickListener() {
			
			@Override  
			public void onClick(View v) {
				
				Bitmap imBitmap = ((BitmapDrawable) recetaImage.getDrawable()).getBitmap();
				
				String ruta = guardarImagen( act, "imagen", imBitmap);

				Bundle bundle = new Bundle();
				bundle.putString("ID",object.getObjectId());
				bundle.putString("TITULO", object.getString("nombre"));
				bundle.putString("PREPARACION", object.getString("preparacion"));
				bundle.putString("TIEMPO", object.getString("tiempo_preparacion"));
				bundle.putString("VALORACION",object.getString("valoracion"));
				bundle.putString("PERSONAS", object.getString("personas"));
				bundle.putString("IMAG", ruta);
				
				
				Intent i = new Intent(act, RecetaResultListActivity.class);
				i.putExtra("ParentClassName",act.getClass().getName());
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