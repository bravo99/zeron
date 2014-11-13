package cl.zeron.vegetaapp;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class ProductosLocalListaAdaptador extends ParseQueryAdapter<ParseObject> {
	Context context;
	ArrayList<ProductoPrecio> lista = new ArrayList<ProductoPrecio>();
	Dialog dialogPrecio; 
	Dialog dialogLocal, dialogAlimento;
	String id_alimento;
	
	public ProductosLocalListaAdaptador(Context context, Dialog dialogAlimento, final ParseObject local) {
		super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
			
			
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public ParseQuery create() {
				ParseQuery query = new ParseQuery("AlimentoLocal");
				query.include("alimento");
				query.whereEqualTo("local", local);
				return query;
			}
		});
		this.context= context;
		this.dialogAlimento = dialogAlimento;
	}
	

	// Customize the layout by overriding getItemView

	
	@Override
	public View getItemView(final ParseObject object, View v, ViewGroup parent) {
		if (v == null) {
			v = View.inflate(getContext(), R.layout.item_producto_precio_local, null);
		}

		super.getItemView(object, v, parent);

		// Add and download the image
		final ParseObject alimento = object.getParseObject("alimento");
		ParseImageView alimentoImage = (ParseImageView) v.findViewById(R.id.icon);
		ParseFile imageFile = alimento.getParseFile("imagen");
		if (imageFile != null) {
			alimentoImage.setParseFile(imageFile);
			alimentoImage.loadInBackground();
		}
		// Add the title view
		TextView titleTextView = (TextView) v.findViewById(R.id.nombre);
		titleTextView.setText(alimento.getString("nombre"));
		TextView precio = (TextView) v.findViewById(R.id.precio);
		precio.setText("$ " + object.getInt("precio"));
		
		
		v.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {				
				Toast.makeText(getContext(),alimento.getString("nombre") , Toast.LENGTH_SHORT).show();
			}
		});
		return v;
	}

}