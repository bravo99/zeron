package cl.zeron.vegetaapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class CustomAdapterAlimento extends ParseQueryAdapter<ParseObject> {
	  	    
		public CustomAdapterAlimento(final Context context,final ParseObject receta) {
			
			// Use the QueryFactory to construct a PQA that will only show
			// Todos marked as high-pri
			super( context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
				@SuppressWarnings({ "unchecked", "rawtypes" })
				public ParseQuery create() {
					ParseQuery query = new ParseQuery("AlimentoReceta");
					query.whereEqualTo("receta",receta);
									
					return query;
				}
			});
			
			
			
		}
		
		
		// Customize the layout by overriding getItemView

		
		
		
		
		@Override
		public View getItemView(final ParseObject object, View v, ViewGroup parent) {
			
			
			if (v == null) {
				v = View.inflate(getContext(), R.layout.urgent_item_alimento, null);
			}

			super.getItemView(object, v, parent);
            
			
			
			// Add and download the image
			final ParseImageView recetaImage = (ParseImageView) v.findViewById(R.id.icon_ali);
			ParseFile imageFile = null;
			try {
				imageFile = object.getParseObject("ingrediente").fetchIfNeeded().getParseFile("imagen");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
					
			if (imageFile != null) {
				recetaImage.setParseFile(imageFile);
				recetaImage.loadInBackground();
			}
	        
			// Add the title view
			TextView titleTextView = (TextView) v.findViewById(R.id.nombre_ali);
			try {
				titleTextView.setText(object.getParseObject("ingrediente").fetchIfNeeded()
					      .getString("nombre"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return v;
		}

		


}
