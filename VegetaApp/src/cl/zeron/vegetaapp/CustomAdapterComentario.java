package cl.zeron.vegetaapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class CustomAdapterComentario extends ParseQueryAdapter<ParseObject> {
	 
	   
		public CustomAdapterComentario(Context context,final ParseObject receta) {
			
			// Use the QueryFactory to construct a PQA that will only show
			// Todos marked as high-pri
			super( context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
				@SuppressWarnings({ "unchecked", "rawtypes" })
				public ParseQuery create() {
					ParseQuery query = new ParseQuery("ComentarioReceta");
					query.whereEqualTo("receta",receta);
					return query;
				}
			});
			
			
		}
		

		// Customize the layout by overriding getItemView

		
		@Override
		public View getItemView(final ParseObject object, View v, ViewGroup parent) {
			if (v == null) {
				v = View.inflate(getContext(), R.layout.urgent_item_comentario, null);
			}

			super.getItemView(object, v, parent);

			// Add the username
			TextView nombreTextView = (TextView) v.findViewById(R.id.user);
			String nombre_user;
			try {
				nombre_user = object.getParseObject("usuario").fetchIfNeeded().getString("username");
				nombreTextView.setText(nombre_user);
			} catch (ParseException e) {
				
				e.printStackTrace();
			}
			
			
			//add the post
			TextView postTextView = (TextView)v.findViewById(R.id.comentario);
			postTextView.setText(object.getString("post"));
			
					
			return v;
		}
		

}
