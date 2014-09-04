package com.example.vegeta;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.parse.ParseObject;

public class ItemIngredQueryAdapter extends BaseAdapter{
	protected Activity activity;
	protected ArrayList<ParseObject> items;
	String[] info;
	public final static String ACT_INFO = "com.example.vegeta.Result1Ingrediente";
	
	public ItemIngredQueryAdapter(Activity activity , ArrayList<ParseObject> items) {
	    //this.pso = findCallback;
	    this.activity= activity;
	    this.items = items;
	    
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

	@Override
	public View getView(final int position, View contentView, ViewGroup parent) {
		View vi=contentView;
		
	       
	    if(contentView == null) {
	      LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	      vi = inflater.inflate(R.layout.list_ingre_item,null);
	    }
	             
	    ParseObject item= items.get(position);
	    
	         
	   	         
	    TextView nombre = (TextView) vi.findViewById(R.id.tv_ingred_ver);
	    nombre.setText(item.getString("nombre"));
	    
	   // ImageView imagenview = (ImageView) vi.findViewById(R.id.imageView1);
	    
	    
		Button ver =(Button) vi.findViewById(R.id.button1);

		ver.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int potition = position;
				ParseObject obj = items.get(potition);
				info = new String[4];
				info[0]=obj.getString("nombre");
				info[1]=obj.getString("descripcion");
				info[2]=obj.getObjectId();
				info[3]=obj.getString("uniIngrediente");
				
				lanzar(info);
				
			}

			

				 
		});
		
	   
	    return vi;
	    //obj.deleteInBackground();
	  }

	private void lanzar(String[] algo) {
		
		Intent a = new Intent(activity,ResultIngrediente1.class );
		a.putExtra(ACT_INFO, algo);
		activity.startActivity(a);
		
	}


}
