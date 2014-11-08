package cl.zeron.vegetaapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ProductoPrecioAdapter extends BaseAdapter{
	protected Activity activity;
	protected ArrayList<ProductoPrecio> items;
	private CharSequence[] options = {"Editar", "Eliminar"};
	private GlobalClass globalClass;
	private Dialog dialogPrecio;
	private ListView lv_producto;
	
	public ProductoPrecioAdapter(Activity activity,
			ArrayList<ProductoPrecio> items, ListView lv_producto) {
		this.activity = activity;
		this.items = items;
		this.lv_producto = lv_producto;
		globalClass = (GlobalClass) activity.getApplicationContext();
		
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

	@SuppressLint("InflateParams") @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi=convertView;
		
        if(convertView == null) {
        	LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	vi = inflater.inflate(R.layout.list_item_layout, null);
        }
            
        final ProductoPrecio item = items.get(position);
        
        TextView nombre = (TextView) vi.findViewById(R.id.nombre);
        nombre.setText(item.getNombre());
        
        TextView tipo = (TextView) vi.findViewById(R.id.tipo);
        tipo.setText(""+item.getPrecio());
        
        vi.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View view) {
				AlertDialog.Builder builder = new AlertDialog.Builder(activity);
				builder.setItems(options, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch(which){
						case 0:
							editarProducto(item);
							break;
						case 1:
							eliminarProducto(item);
							break;
						}
						
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
				return false;
			}
		});
        
        
        return vi;
	}
	
	public void editarProducto(final ProductoPrecio item){
		
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Alimento");
		query.getInBackground(item.getId(), new GetCallback<ParseObject>() {
			
			@Override
			public void done(ParseObject object, ParseException e) {
				if(e==null){
					dialogPrecio = new Dialog(activity, R.style.Theme_Dialog_Translucent);
					dialogPrecio.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialogPrecio.setContentView(R.layout.formulario_precio);
					dialogPrecio.setCanceledOnTouchOutside(false);
					EditText et_precio = (EditText) dialogPrecio.findViewById(R.id.et_precio);
					et_precio.setText(""+item.getPrecio(), TextView.BufferType.EDITABLE);
					dialogPrecio.show();
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
								Toast.makeText(activity , "Por favor ingrese el precio", Toast.LENGTH_SHORT).show();
								return;
							}
							ArrayList<ProductoPrecio> lista = globalClass.getLista();
							ArrayList<ProductoPrecio> lista_nueva = new ArrayList<ProductoPrecio>();
							int precio = Integer.parseInt(precio_input);
							ProductoPrecio producto = new ProductoPrecio();
							producto.setId(item.getId());
							producto.setNombre(item.getNombre());
							producto.setPrecio(precio);
							lista_nueva.add(producto);
							for(ProductoPrecio prod : lista){
								if(!prod.getId().equals(item.getId())){
									lista_nueva.add(prod);
								}
							}
							globalClass.setLista(lista_nueva);
							if(lv_producto != null){
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
								Collections.sort(lista_nueva, comparador);
								ProductoPrecioAdapter adapter =  new ProductoPrecioAdapter(activity, lista_nueva, lv_producto);
								lv_producto.setAdapter(adapter);
							}
							dialogPrecio.dismiss();
						}
					});
				}
			}
		});
		
	}
	
	public void eliminarProducto(final ProductoPrecio item){
		ArrayList<ProductoPrecio> lista = globalClass.getLista();
		ArrayList<ProductoPrecio> lista_nueva = new ArrayList<ProductoPrecio>();
		if(lista != null){
			for(ProductoPrecio prod : lista){
				if(!item.getId().equals(prod.getId())){
					lista_nueva.add(prod);
				}
			}
			globalClass.setLista(lista_nueva);
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
			Collections.sort(lista_nueva, comparador);
			ProductoPrecioAdapter adapter = new ProductoPrecioAdapter(activity, lista_nueva, lv_producto);
			lv_producto.setAdapter(adapter);
		}
	}

}
