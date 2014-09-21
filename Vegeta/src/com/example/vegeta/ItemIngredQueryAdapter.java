package com.example.vegeta;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

public class ItemIngredQueryAdapter extends BaseAdapter{
	protected Activity activity;
	protected ArrayList<ParseObject> items;
	Context ct;
	String[] info;
	
	byte[] img;
	public final static String ACT_INFO = "com.example.vegeta.Result1Ingrediente";
	private static final String Imagenes = "Imagenes";
	
	public ItemIngredQueryAdapter(Activity activity , ArrayList<ParseObject> items, Context context) {
	    //this.pso = findCallback;
	    this.activity= activity;
	    this.items = items;
	        
	    this.ct = context;
	    
	   
	    
	    
	    
	  }
	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int arg0) {
		return items.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		
		return 0;
	}

	
	@SuppressLint("InflateParams") @Override
	public View getView(final int position, View contentView, ViewGroup parent) {
		
		View vi=contentView;
		
	       
	    if(contentView == null) {
	      LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	      vi = inflater.inflate(R.layout.list_ingre_item,null);
	    }
	             
	    ParseObject item= items.get(position);
	    final ImageView imagenview = (ImageView) vi.findViewById(R.id.imageView1);
	         
	    ParseFile applicantResume = (ParseFile)item.get("imagen");
	    applicantResume.getDataInBackground(new GetDataCallback() {
	    @Override	
	      public void done(byte[] data, ParseException e) {
	        if (e == null) {
	         
	          imagenview.setImageBitmap(BitmapFactory.decodeByteArray(data,0,data.length));
	         
	        } else {
	          //poner imagen por defecto
	        }
	      }

		
	    });
	   
	   	         
	    TextView nombre = (TextView) vi.findViewById(R.id.tv_ingred_ver);
	    nombre.setText(item.getString("nombre"));
	    
	 
	   
	    
	    
		Button ver =(Button) vi.findViewById(R.id.button1);

		ver.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Drawable drawable = imagenview.getDrawable();
				Bitmap bitmap = (Bitmap)((BitmapDrawable) drawable).getBitmap();
				
				int potition = position;
				ParseObject obj = items.get(potition);
				info = new String[5];
				info[0]=obj.getString("nombre");
				info[1]=obj.getString("descripcion");
				info[2]=obj.getObjectId();
				info[3]=obj.getString("uniIngrediente");
				
				String ruta = guardarImagen( ct, "imagen", bitmap);
				System.out.println(ruta); 
			    info[4]=ruta;				
				lanzar(info);
				
			}

			
			

				 
		});
		
	   
	    return vi;
	    //obj.deleteInBackground();
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
	
	private void lanzar(String[] algo) {
		
		Intent a = new Intent(activity,ResultIngrediente1.class );
		a.putExtra(ACT_INFO, algo);
		
		activity.startActivity(a);
		
	}


}
