package com.example.vegeta;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemRecetaQueryAdapter extends BaseAdapter{
	private static final String Imagenes = null;
	protected Activity activity;
	protected ArrayList<ParseObject> items;
	
public  ItemRecetaQueryAdapter(Activity activity , ArrayList<ParseObject> items) {
	    
	    this.activity= activity;
	    this.items = items;
	    
	  }
	@Override
	public int getCount() {
		
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View contentView, ViewGroup parent) {
		View vi=contentView;
		
	       
	    if(contentView == null) {
	      LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	      vi = inflater.inflate(R.layout.list_item_layout, null);
	    }
	             
	    ParseObject item= items.get(position);
	    
	    final ImageView imagenview = new ImageView(activity);
		ParseFile applicantResume = (ParseFile)item.get("imagen");
	    applicantResume.getDataInBackground(new GetDataCallback() {
	    @Override	
	      public void done(byte[] data, ParseException e) {
	        if (e == null) {
	        	//bundle.putByteArray("IMAGEN", data);
	        	 imagenview.setImageBitmap(BitmapFactory.decodeByteArray(data,0,data.length));
	         
	        } else {
	          //poner imagen por defecto
	        }
	      }

		
	    });
	         
	   	         
	    TextView nombre = (TextView) vi.findViewById(R.id.tvnombrereceta);
	    nombre.setText(item.getString("nombre"));
	         
	    Button ver =(Button) vi.findViewById(R.id.botonverreceta);

		ver.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final Bundle bundle= new Bundle();
				
				//Drawable drawable = imagenview.getDrawable();
				//Bitmap bitmap = (Bitmap)((BitmapDrawable) drawable).getBitmap();
				
				int potition = position;
				ParseObject obj = items.get(potition);
				bundle.putString("NOMBRE", obj.getString("nombre"));
				
				//info[0]=obj.getString("nombre");
				bundle.putString("PREPARACION", obj.getString("preparacion"));
				//info[1]=obj.getString("descripcion");
				
				
			   
				Drawable drawable = imagenview.getDrawable();
				Bitmap bitmap = (Bitmap)((BitmapDrawable) drawable).getBitmap();
				String ruta = guardarImagen( activity, "imagen", bitmap);
				bundle.putString("IMAG", ruta);
				bundle.putInt("TIEMPO",obj.getInt("tiempo") );
				bundle.putInt("CALIFICACION",obj.getInt("calificacion") );
				
				//info[2]=obj.getObjectId();
				//info[3]=obj.getString("uniIngrediente");
				
				//String ruta = guardarImagen( ct, "imagen", bitmap);
				//System.out.println(ruta); 
			    //info[4]=ruta;				
				lanzar(bundle);
				
			}

			
			

				 
		});
		
	   
	    return vi;
	    
	}	
	public void lanzar(Bundle bdl){
		Intent intent=new Intent(activity,ResultReceta.class);
		intent.putExtras(bdl);
		activity.startActivity(intent);
	}
	
	private String guardarImagen (Context context, String nombre, Bitmap imagen){
	    ContextWrapper cw = new ContextWrapper(context);
	    File dirImages = cw.getDir(Imagenes, Context.MODE_PRIVATE);
	    
		File myPath = new File(dirImages, nombre + ".png");
	     
	    FileOutputStream fos = null;
	    try{
	        fos = new FileOutputStream(myPath);
	        imagen.compress(Bitmap.CompressFormat.JPEG, 10, fos);
	        fos.flush();
	    }catch (FileNotFoundException ex){
	        ex.printStackTrace();
	    }catch (IOException ex){
	        ex.printStackTrace();
	    }
	    return myPath.getAbsolutePath();
	}

}
