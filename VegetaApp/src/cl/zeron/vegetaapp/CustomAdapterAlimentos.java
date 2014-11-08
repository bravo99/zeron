package cl.zeron.vegetaapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class CustomAdapterAlimentos extends ParseQueryAdapter<ParseObject> {
	Context context;
	ArrayList<ProductoPrecio> lista = new ArrayList<ProductoPrecio>();
	Dialog dialogPrecio; 
	Dialog dialogLocal, dialogAlimento;
	private GlobalClass globalClass;
	private ListView lv_productos;
	
	public CustomAdapterAlimentos(Context context, ListView listView, Dialog dialogAlimento) {
		super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public ParseQuery create() {
				ParseQuery query = new ParseQuery("Alimento");
				query.orderByAscending("nombre");
				return query;
			}
		});
		this.context = context;
		this.dialogAlimento = dialogAlimento;
		this.lv_productos = listView;
		globalClass = (GlobalClass) context.getApplicationContext();
	}
	public CustomAdapterAlimentos(Context context, Dialog dialogLocal, Dialog dialogAlimento) {
		super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public ParseQuery create() {
				ParseQuery query = new ParseQuery("Alimento");
				query.orderByAscending("nombre");
				return query;
			}
		});
		this.context= context;
		this.dialogLocal = dialogLocal;
		this.dialogAlimento = dialogAlimento;
		globalClass= (GlobalClass) context.getApplicationContext();
	}
	

	// Customize the layout by overriding getItemView

	
	


	@Override
	public View getItemView(final ParseObject object, View v, ViewGroup parent) {
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
		
		
		v.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final ProductoPrecio prod = new ProductoPrecio();
				if(globalClass.getLista() != null){
					for(ProductoPrecio producto_alimento : globalClass.getLista()){
						if(producto_alimento.getId().equals(object.getObjectId())){
							Toast.makeText(context,object.getString("nombre")+" ya está en la lista", Toast.LENGTH_SHORT).show();
							return;
						}
					}
				}
				prod.setNombre(object.getString("nombre"));
				prod.setId(object.getObjectId());
				dialogPrecio = new Dialog((Activity) context, R.style.Theme_Dialog_Translucent);
				dialogPrecio.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialogPrecio.setContentView(R.layout.formulario_precio);
				dialogPrecio.setCanceledOnTouchOutside(false);
				ParseFile imageFile = object.getParseFile("imagen");
				ParseImageView alimentoImage = (ParseImageView) dialogPrecio.findViewById(R.id.icon);
				if(imageFile!=null){
					alimentoImage.setParseFile(imageFile);
					alimentoImage.loadInBackground();
				}
				TextView titleTextView = (TextView) dialogPrecio.findViewById(R.id.nombre);
				titleTextView.setText(object.getString("nombre"));
				((Button) dialogPrecio.findViewById(R.id.cancelar)).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						dialogPrecio.cancel();
					}
				});
				((Button) dialogPrecio.findViewById(R.id.aceptar)).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						String precio_input = "";
						EditText et_precio = (EditText) dialogPrecio.findViewById(R.id.et_precio);
						precio_input = et_precio.getText().toString();
						if(precio_input.equals("")){
							Toast.makeText((Activity) context, "Por favor ingrese el precio", Toast.LENGTH_SHORT).show();
							return;
						}
						int precio = Integer.parseInt(precio_input);
						prod.setPrecio(precio);
						if(globalClass.getLista() !=null){
							lista = globalClass.getLista();
						}
						lista.add(prod);
						ListView lv_productoPrecio;
						
						globalClass.setLista(lista);
						if(lv_productos == null){
							lv_productoPrecio = (ListView) dialogLocal.findViewById(R.id.lv_producto);
							ProductoPrecioAdapter productoPrecioAdapter = new ProductoPrecioAdapter((Activity) context, lista, lv_productoPrecio);
							lv_productoPrecio.setAdapter(productoPrecioAdapter);
						}
						else{
							Comparator<ProductoPrecio> comparador = new Comparator<ProductoPrecio>() {
								
								@Override
								public int compare(ProductoPrecio a, ProductoPrecio b) {
									int resultado = a.getNombre().compareTo(b.getNombre());
									if(resultado!=0){
										return resultado;
									}
									return resultado;
								}
							};
							Collections.sort(lista, comparador);
							ProductoPrecioAdapter productoPrecioAdapter = new ProductoPrecioAdapter((Activity) context, lista, lv_productos);
							lv_productos.setAdapter(productoPrecioAdapter);
						}
						
						Toast.makeText(context,"Agregado " + prod.getNombre() + " a la lista." , Toast.LENGTH_SHORT).show();
						dialogPrecio.cancel();
						
					}
				});
				dialogPrecio.show();
				
				
				
				
				
			}
		});

		// Add a reminder of how long this item has been outstanding
		//TextView timestampView = (TextView) v.findViewById(R.id.descripcion);
		//timestampView.setText(object.getCreatedAt().toString());
		return v;
	}

}