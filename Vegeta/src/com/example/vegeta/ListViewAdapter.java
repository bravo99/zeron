package com.example.vegeta;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ListViewAdapter extends BaseAdapter {

	// Declare Variables
	Context context;
	LayoutInflater inflater;
	ImageLoader imageLoader;
	Dialog dialogIngrediente;
	private List<ListIngredient> listaingredients = null;
	private ArrayList<ListIngredient> arraylist2;

	public ListViewAdapter(Context context,
			List<ListIngredient> listaingredientes, Dialog mDialog) {
		this.dialogIngrediente = mDialog;
		this.context = context;
		this.listaingredients = listaingredientes;
		inflater = LayoutInflater.from(context);
		this.arraylist2 = new ArrayList<ListIngredient>();
		this.arraylist2.addAll(listaingredientes);
		imageLoader = new ImageLoader(context);
	}

	public class ViewHolder {
		TextView nombre;
		TextView descripcion;
		ImageView imagen;
	}

	@Override
	public int getCount() {
		return listaingredients.size();
	}

	@Override
	public Object getItem(int position) {
		return listaingredients.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.listview_item, null);
			// Locate the TextViews in listview_item.xml
			
			// Rank, country y flag estan mal puestas
			
			holder.nombre = (TextView) view.findViewById(R.id.rank);
			holder.descripcion = (TextView) view.findViewById(R.id.country);
			// Locate the ImageView in listview_item.xml
			holder.imagen = (ImageView) view.findViewById(R.id.flag);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		// Set the results into TextViews
		holder.nombre.setText(listaingredients.get(position).getNombre());
		holder.descripcion.setText(listaingredients.get(position).getDescripcion());
		// Set the results into ImageView
		imageLoader.DisplayImage(listaingredients.get(position).getImagenIngrediente(),
				holder.imagen);
		
		Toast.makeText(context, "Solo falta hacer clikc", Toast.LENGTH_SHORT).show();
		// Listen for ListView Item Click
		
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				Toast.makeText(context, listaingredients.get(position).getNombre(), Toast.LENGTH_SHORT).show();
				// Send single item click data to SingleItemView Class
				/*Intent intent = new Intent(context, SingleItemView.class);
				// Pass all data rank
				intent.putExtra("rank",
						(listaingredients.get(position).getNombre()));
				// Pass all data country
				intent.putExtra("country",
						(listaingredients.get(position).getDescripcion()));
				// Pass all data flag
				intent.putExtra("flag",
						(listaingredients.get(position).getImagenIngrediente()));
				// Start SingleItemView Class
				context.startActivity(intent);*/
			}
		});
		return view;
	}

}
