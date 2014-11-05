package cl.zeron.vegetaapp;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class CustomAdapterComentario extends ParseQueryAdapter<ParseObject> {
	  
	    
		public CustomAdapterComentario(final Activity a,final Context context,final ParseObject receta) {
			
			// Use the QueryFactory to construct a PQA that will only show
			// Todos marked as high-pri
			super( context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
				@SuppressWarnings({ "unchecked", "rawtypes" })
				public ParseQuery create() {
					ParseQuery query = new ParseQuery("ComentarioReceta");
					query.whereEqualTo("receta",receta);
					try {
						if(query.count() == 0){
							
                            Toast.makeText(context, "Aun no existen comentarios ", Toast.LENGTH_SHORT).show();
                            a.onBackPressed();
						}
						
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
					query.orderByDescending("updatedAt");
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
				nombre_user = object.getParseObject("usuario").fetchIfNeeded().getString("name");
				
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
