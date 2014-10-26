package com.vegeta.vegetaappmaps;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class CustomAdapterRecetas extends ParseQueryAdapter<ParseObject> {

	public CustomAdapterRecetas(Context context,final String cat) {
		// Use the QueryFactory to construct a PQA that will only show
		// Todos marked as high-pri
		super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
			public ParseQuery create() {
				ParseQuery query = new ParseQuery("Receta");
				query.whereEqualTo("categoria",cat);
				return query;
			}
		});
	}

	// Customize the layout by overriding getItemView

	
	@Override
	public View getItemView(ParseObject object, View v, ViewGroup parent) {
		if (v == null) {
			v = View.inflate(getContext(), R.layout.urgent_item, null);
		}

		super.getItemView(object, v, parent);

		// Add and download the image
		ParseImageView alimentoImage = (ParseImageView) v.findViewById(R.id.icon);
		ParseFile imageFile = object.getParseFile("imagen");
		if (imageFile != null) {
			alimentoImage.setParseFile(imageFile);
			alimentoImage.loadInBackground();
		}

		// Add the title view
		TextView titleTextView = (TextView) v.findViewById(R.id.nombre);
		titleTextView.setText(object.getString("nombre"));

		// Add a reminder of how long this item has been outstanding
		//TextView timestampView = (TextView) v.findViewById(R.id.descripcion);
		//timestampView.setText(object.getCreatedAt().toString());
		return v;
	}

}