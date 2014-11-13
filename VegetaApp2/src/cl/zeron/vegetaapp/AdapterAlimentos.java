package cl.zeron.vegetaapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class AdapterAlimentos extends ParseQueryAdapter<ParseObject> {
	Context context;
	ArrayList<ProductoPrecio> lista = new ArrayList<ProductoPrecio>();
	Dialog dialogPrecio; 
	Dialog dialogLocal, dialogAlimento;
	private GlobalClass globalClass;
	private LinearLayout mLinearListView;
	
	
	public AdapterAlimentos(Context context, LinearLayout mLinearListView,
			Dialog dialogAlimento) {
		
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
		this.mLinearListView = mLinearListView;
		globalClass = (GlobalClass) context.getApplicationContext();
		
	}
	
	
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
			alimentoImage.setImageResource(R.drawable.ic_launcher3);
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
							Toast.makeText(context,object.getString("nombre")+" ya est� en la lista", Toast.LENGTH_SHORT).show();
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
						
						globalClass.setLista(lista);
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
						mLinearListView.removeAllViews();
						for(ProductoPrecio prod_al: lista){
							LayoutInflater inflater = null;
		                    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		                     View mLinearView = inflater.inflate(R.layout.item_producto_precio, null);
		                     final TextView mName = (TextView) mLinearView.findViewById(R.id.textViewName);
		                     TextView mPrecio = (TextView) mLinearView.findViewById(R.id.textViewPrecio);
		                     TextView mId = (TextView) mLinearView.findViewById(R.id.id);
		                     ImageButton btnEliminar = (ImageButton) mLinearView.findViewById(R.id.ib_delete);
		                     mId.setText(prod_al.getId());
		                     mName.setText(prod_al.getNombre());
		                     mPrecio.setText(""+prod_al.getPrecio());
		                     mLinearListView.addView(mLinearView);
		                     final String nombre = mName.getText().toString();
		                     final String precio_valor = mPrecio.getText().toString();
		                     final String id_alimento = mId.getText().toString();
		                     btnEliminar.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									btnEliminarAlimento(id_alimento);
									
								}
							});
		                     mLinearView.setOnClickListener(new OnClickListener() {

		                           @Override
		                           public void onClick(View v) {
		                        	   mostrarDialogPrecio(id_alimento, precio_valor, nombre );
		                           }
		                     });
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
	public void btnEliminarAlimento(String id){
		ArrayList<ProductoPrecio> lista = globalClass.getLista();
		ArrayList<ProductoPrecio> lista_nueva = new ArrayList<ProductoPrecio>();
		if(lista != null){
			for(ProductoPrecio prod : lista){
				if(!id.equals(prod.getId())){
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
			mLinearListView.removeAllViews();
			for(ProductoPrecio prod : lista_nueva){
				LayoutInflater inflater = null;
                 inflater = (LayoutInflater) context
                              .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                 final View mLinearView = inflater.inflate(R.layout.item_producto_precio,null);
                 TextView mFirstName = (TextView) mLinearView.findViewById(R.id.textViewName);
                 TextView mPrecio = (TextView) mLinearView.findViewById(R.id.textViewPrecio);
                 TextView mId = (TextView) mLinearView.findViewById(R.id.id);
                 ImageButton btnEliminar = (ImageButton) mLinearView.findViewById(R.id.ib_delete);
                 mId.setText(prod.getId());
                 final String id_alimento = mId.getText().toString();
                 mFirstName.setText(prod.getNombre());
                 mPrecio.setText(""+prod.getPrecio());
                 final String precio_nuevo = mPrecio.getText().toString();
                 final String nombre = mFirstName.getText().toString();
                 mLinearListView.addView(mLinearView);
                 btnEliminar.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						btnEliminarAlimento(id_alimento);
					}
				});
                 mLinearView.setOnClickListener(new OnClickListener() {

                       @Override
                       public void onClick(View v) {
                    	   mostrarDialogPrecio(id_alimento, precio_nuevo, nombre );
                       }
                 });
			}
		}
	}
	
	public void mostrarDialogPrecio(final String id, final String precio, final String nombre) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Alimento");
		query.getInBackground(id, new GetCallback<ParseObject>() {
			
			@Override
			public void done(ParseObject object, ParseException e) {
				if(e==null){
					dialogPrecio = new Dialog(context, R.style.Theme_Dialog_Translucent);
					dialogPrecio.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialogPrecio.setContentView(R.layout.formulario_precio);
					dialogPrecio.setCanceledOnTouchOutside(false);
					EditText et_precio = (EditText) dialogPrecio.findViewById(R.id.et_precio);
					et_precio.setText(precio, TextView.BufferType.EDITABLE);
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
								Toast.makeText(context , "Por favor ingrese el precio", Toast.LENGTH_SHORT).show();
								return;
							}
							ArrayList<ProductoPrecio> lista = globalClass.getLista();
							ArrayList<ProductoPrecio> lista_nueva = new ArrayList<ProductoPrecio>();
							int precio = Integer.parseInt(precio_input);
							ProductoPrecio producto = new ProductoPrecio();
							producto.setId(id);
							producto.setNombre(nombre);
							producto.setPrecio(precio);
							lista_nueva.add(producto);
							for(ProductoPrecio prod : lista){
								if(!prod.getId().equals(id)){
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
							mLinearListView.removeAllViews();
							for(ProductoPrecio prod : lista_nueva){
								LayoutInflater inflater = null;
			                     inflater = (LayoutInflater) context
			                                  .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			                     final View mLinearView = inflater.inflate(R.layout.item_producto_precio,null);
			                     TextView mFirstName = (TextView) mLinearView.findViewById(R.id.textViewName);
			                     TextView mPrecio = (TextView) mLinearView.findViewById(R.id.textViewPrecio);
			                     TextView mId = (TextView) mLinearView.findViewById(R.id.id);
			                     ImageButton btnEliminar = (ImageButton) mLinearView.findViewById(R.id.ib_delete);
			                     mId.setText(prod.getId());
			                     final String id_alimento = mId.getText().toString();
			                     mFirstName.setText(prod.getNombre());
			                     mPrecio.setText(""+prod.getPrecio());
			                     final String precio_nuevo = mPrecio.getText().toString();
			                     final String nombre = mFirstName.getText().toString();
			                     mLinearListView.addView(mLinearView);
			                     btnEliminar.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										btnEliminarAlimento(id_alimento);
										
									}
								});
			                     mLinearView.setOnClickListener(new OnClickListener() {

			                           @Override
			                           public void onClick(View v) {
			                        	   mostrarDialogPrecio(id_alimento, precio_nuevo, nombre );
			                           }
			                     });
							}
							
							dialogPrecio.dismiss();
						}
					});
				}
			}
		});
		
	}

}