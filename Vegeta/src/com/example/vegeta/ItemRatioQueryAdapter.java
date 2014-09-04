package com.example.vegeta;

import java.util.ArrayList;

import com.parse.ParseObject;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

public class ItemRatioQueryAdapter extends BaseAdapter{
	protected Activity activity;
	protected ArrayList<ParseObject> items;
	//String[] info=new String[3];
	String[] ingredId;
	int cont = 0;
	final static String ACT_INFO = "com.example.vegeta.Ingred";
	
		
	public ItemRatioQueryAdapter(Activity activity , ArrayList<ParseObject> items ){
	    
	    this.activity= activity;
	    this.items = items;
	    ingredId = new String[getCount()];
	   
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
	      vi = inflater.inflate(R.layout.item, null);
	    }
	             
	     ParseObject item= items.get(position);
	    
	         
	   	         
	    final CheckBox chb = (CheckBox) vi.findViewById(R.id.bbb1);
	    chb.setText(item.getString("nombre"));
	   
	    
	    chb.setOnClickListener(new CheckBox.OnClickListener(){
	    	
	    	int potition = position;
			ParseObject obj = items.get(potition);
            @Override
            public void onClick(View v) {
                if(chb.isChecked()){
                	ingredId[cont]= obj.getString("objectId");
                	cont = cont + 1;
                }
                    
                      
            }
            
        });
	 	 
		
		System.out.print(ingredId[0]);
	   
	    return vi;
	    
	  }
	



	//enviar objeto id a Ingred
		
		
		

	

}
