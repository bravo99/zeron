package cl.zeron.vegetaapp;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ProductoPrecioAdapter extends BaseAdapter{
	protected Activity activity;
	protected ArrayList<ProductoPrecio> items;
	
	public ProductoPrecioAdapter(Activity activity, ArrayList<ProductoPrecio> items){
		this.activity = activity;
		this.items = items;
		// Tirar un intent para rescatar la lista en MapActivity
		
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
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi=convertView;
		
        if(convertView == null) {
        	LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	vi = inflater.inflate(R.layout.list_item_layout, null);
        }
            
        ProductoPrecio item = items.get(position);
        
        TextView nombre = (TextView) vi.findViewById(R.id.nombre);
        nombre.setText(item.getNombre());
        
        TextView tipo = (TextView) vi.findViewById(R.id.tipo);
        tipo.setText(""+item.getPrecio());

        return vi;
	}

}
