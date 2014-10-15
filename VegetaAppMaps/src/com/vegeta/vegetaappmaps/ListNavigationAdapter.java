package com.vegeta.vegetaappmaps;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListNavigationAdapter extends BaseAdapter{
	private Activity activity;
	ArrayList<ItemObject> items;
    public ListNavigationAdapter(Activity activity , ArrayList<ItemObject> items){
    	this.activity=activity;
    	this.items=items;
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
		return position;
	}
	
	public static class Fila{
		TextView titulo_itm;
		ImageView icon;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Fila view;
		LayoutInflater inflator= activity.getLayoutInflater();
		
		if(convertView==null){
			view = new Fila();
			ItemObject itm = items.get(position);
			convertView = inflator.inflate(R.layout.navigation_adapter,null);
			view.titulo_itm=(TextView)convertView.findViewById(R.id.text_navigation);
			view.titulo_itm.setText(itm.getTitulo());
			view.icon=(ImageView)convertView.findViewById(R.id.ic_navigation);
			view.icon.setImageResource(itm.getIcono());
			convertView.setTag(view);
		}else{
			view=(Fila) convertView.getTag();
		}
			
		return convertView;
	}

}
