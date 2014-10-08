package com.example.vegeta;

import java.util.ArrayList;

import com.parse.ParseObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class ItemQueryAdapter extends BaseAdapter{
	protected Activity activity;
	protected ArrayList<ParseObject> items;
	String[] info;
	final static String ACT_INFO1 = "com.example.vegeta.ResultRecetaActivity";
	
	
	
	public ItemQueryAdapter(Activity activity , ArrayList<ParseObject> items) {
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
	      vi = inflater.inflate(R.layout.list_item_layout, null);
	    }
	             
	    ParseObject item= items.get(position);
	    
	         
	   	         
	    TextView nombre = (TextView) vi.findViewById(R.id.tvnombrereceta);
	    nombre.setText(item.getString("nombre"));
	         
	    Button ver =(Button) vi.findViewById(R.id.botonverreceta);

		ver.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int potition = position;
				ParseObject obj = items.get(potition);
				info = new String[3];
				info[0]=obj.getString("nombre");
				info[1]=obj.getString("preparacion");
				info[2]=obj.getString("autor");
				lanzar(info);
				
			}

			

				 
		});
		
	   
	    return vi;
	    //obj.deleteInBackground();
	  }

	private void lanzar(String[] algo) {
		
		Intent a = new Intent(activity,ResultRecetaActivity.class );
		a.putExtra(ACT_INFO1, algo);
		activity.startActivity(a);
		
	}

	
	

}
