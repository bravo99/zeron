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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class AdapterAlimentoIngrediente extends ParseQueryAdapter<ParseObject> {
	Context context;
	ArrayList<IngredienteCantidad> lista;
	Dialog dialogIngrediente;
	private GlobalClass globalClass;
	final String[] categorias = {"Taza", "Cucharada", "Cucharadita", "Unidad", "Gramos", "Otros" };
	ArrayAdapter<String> adapSpinnerCat;
	private LinearLayout mLinear;
	
	public AdapterAlimentoIngrediente(Context context, LinearLayout mLinear) {
		super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public ParseQuery create() {
				ParseQuery query = new ParseQuery("Alimento");
				query.orderByAscending("nombre");
				return query;
			}
		});
		this.context = context;
		this.mLinear = mLinear;
		globalClass = (GlobalClass) context.getApplicationContext();
		lista = new ArrayList<IngredienteCantidad>();
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
			alimentoImage.setImageResource(R.drawable.ic_launcher3);
		}

		// Add the title view
		TextView titleTextView = (TextView) v.findViewById(R.id.nombre);
		titleTextView.setText(object.getString("nombre"));
		
		
		v.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final IngredienteCantidad ing = new IngredienteCantidad();
				if(globalClass.getListaIngrediente() != null){
					for(IngredienteCantidad ingrediente_cantidad : globalClass.getListaIngrediente()){
						if(ingrediente_cantidad.getId().equals(object.getObjectId())){
							Toast.makeText(context,object.getString("nombre")+" ya est� en la lista", Toast.LENGTH_SHORT).show();
							return;
						}
					}
				}
				ing.setNombre(object.getString("nombre"));
				ing.setId(object.getObjectId());
				
				dialogIngrediente = new Dialog((Activity) context, R.style.Theme_Dialog_Translucent);
				dialogIngrediente.requestWindowFeature(Window.FEATURE_NO_TITLE);
				//Cambiar Layout
				dialogIngrediente.setContentView(R.layout.ingredientereceta);
				dialogIngrediente.setCanceledOnTouchOutside(false);
				dialogIngrediente.show();
				TextView tv_nombre = (TextView) dialogIngrediente.findViewById(R.id.nomIngred);
				tv_nombre.setText(object.getString("nombre"));
				Spinner spMedida = (Spinner) dialogIngrediente.findViewById(R.id.spMedida);
				adapSpinnerCat =new ArrayAdapter<String>(context ,android.R.layout.simple_spinner_item, categorias);
				adapSpinnerCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spMedida.setAdapter(adapSpinnerCat);
				((Button) dialogIngrediente.findViewById(R.id.aceptar)).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						String cantidad_input = "";
						EditText et_cantidad = (EditText) dialogIngrediente.findViewById(R.id.etCantidad);
						cantidad_input = et_cantidad.getText().toString();
						if(cantidad_input.equals("")){
							Toast.makeText((Activity) context, "Por favor ingrese la Cantidad", Toast.LENGTH_SHORT).show();
							return;
						}
						Spinner sp_medida = (Spinner) dialogIngrediente.findViewById(R.id.spMedida);
						String medida_input = "";
						medida_input = sp_medida.getSelectedItem().toString();
						ing.setCantidad(cantidad_input);
						ing.setMedida(medida_input);
						if(globalClass.getListaIngrediente() !=null){
							lista = globalClass.getListaIngrediente();
						}
						lista.add(ing);
						
						globalClass.setListaIngrediente(lista);
						Comparator<IngredienteCantidad> comparator =  new Comparator<IngredienteCantidad>() {
							
							@Override
							public int compare(IngredienteCantidad a, IngredienteCantidad b) {
								int resultado = a.getNombre().compareTo(b.getNombre());
								if(resultado!=0){
									return resultado;
								}
								return resultado;
							}
						};
						
						Collections.sort(lista, comparator);
						mLinear.removeAllViews();
						for(IngredienteCantidad ingrediente: lista){
							
							LayoutInflater inflater = null;
		                     inflater = (LayoutInflater) context
		                                  .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		                     final View mLinearView = inflater.inflate(R.layout.item_alimento_ingrediente,null);
		                     TextView mNombre = (TextView) mLinearView.findViewById(R.id.nombreIng);
		                     TextView mCantidad = (TextView) mLinearView.findViewById(R.id.cantidadIng);
		                     TextView mId = (TextView) mLinearView.findViewById(R.id.id);
		                     TextView mMedida = (TextView) mLinearView.findViewById(R.id.medidaIng);
		                     ImageButton btnEliminar = (ImageButton) mLinearView.findViewById(R.id.ib_delete);
		                     mId.setText(ingrediente.getId());
		                     final String id_alimento = mId.getText().toString();
		                     mNombre.setText(ingrediente.getNombre());
		                     mCantidad.setText(ingrediente.getCantidad());
		                     mMedida.setText(ingrediente.getMedida());
		                     final String cantidad = mCantidad.getText().toString();
		                     final String medida = mMedida.getText().toString();
		                     final String nombre = mNombre.getText().toString();
		                     mLinear.addView(mLinearView);
		                     btnEliminar.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									btnEliminarAlimento(id_alimento);
									
								}
							});
		                     mLinearView.setOnClickListener(new OnClickListener() {

		                           @Override
		                           public void onClick(View v) {
		                        	   mostrarIngrediente(id_alimento, nombre, cantidad, medida);
		                           }
		                     });
							
						}
						Toast.makeText(context,"Agregado " + ing.getNombre() + " a la lista." , Toast.LENGTH_SHORT).show();
						dialogIngrediente.cancel();
						
					}
				});
				
				
				
				
				
			}
		});

		// Add a reminder of how long this item has been outstanding
		//TextView timestampView = (TextView) v.findViewById(R.id.descripcion);
		//timestampView.setText(object.getCreatedAt().toString());
		return v;
	}
	
	public void btnEliminarAlimento(String id){
		ArrayList<IngredienteCantidad> lista = globalClass.getListaIngrediente();
		ArrayList<IngredienteCantidad> lista_nueva = new ArrayList<IngredienteCantidad>();
		if(lista != null){
			for(IngredienteCantidad prod : lista){
				if(!id.equals(prod.getId())){
					lista_nueva.add(prod);
				}
			}
			globalClass.setListaIngrediente(lista_nueva);
			Comparator<IngredienteCantidad> comparador = new Comparator<IngredienteCantidad>() {
				
				@Override
				public int compare(IngredienteCantidad a, IngredienteCantidad b) {
					int resultado = a.getNombre().compareTo(b.getNombre());
					if(resultado!=0){
						return resultado;
					}
					return resultado;
				}
			};
			Collections.sort(lista_nueva, comparador);
			mLinear.removeAllViews();
			for(IngredienteCantidad ingrediente: lista_nueva){
				
				LayoutInflater inflater = null;
                 inflater = (LayoutInflater) context
                              .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                 final View mLinearView = inflater.inflate(R.layout.item_alimento_ingrediente,null);
                 TextView mNombre = (TextView) mLinearView.findViewById(R.id.nombreIng);
                 TextView mCantidad = (TextView) mLinearView.findViewById(R.id.cantidadIng);
                 TextView mId = (TextView) mLinearView.findViewById(R.id.id);
                 TextView mMedida = (TextView) mLinearView.findViewById(R.id.medidaIng);
                 ImageButton btnEliminar = (ImageButton) mLinearView.findViewById(R.id.ib_delete);
                 mId.setText(ingrediente.getId());
                 final String id_alimento = mId.getText().toString();
                 mNombre.setText(ingrediente.getNombre());
                 mCantidad.setText(ingrediente.getCantidad());
                 mMedida.setText(ingrediente.getMedida());
                 final String cantidad = mCantidad.getText().toString();
                 final String medida = mMedida.getText().toString();
                 final String nombre = mNombre.getText().toString();
                 mLinear.addView(mLinearView);
                 btnEliminar.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						btnEliminarAlimento(id_alimento);
						
					}
				});
                 mLinearView.setOnClickListener(new OnClickListener() {

                       @Override
                       public void onClick(View v) {
                    	   mostrarIngrediente(id_alimento, nombre, cantidad, medida);
                       }
                 });
				
			}
			
			
		}
	}
	
	public void mostrarIngrediente(final String id_alimento, final String nombre, String cantidad, String medida){
		dialogIngrediente = new Dialog((Activity) context, R.style.Theme_Dialog_Translucent);
		dialogIngrediente.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogIngrediente.setContentView(R.layout.ingredientereceta);
		dialogIngrediente.setCanceledOnTouchOutside(false);
		dialogIngrediente.show();
		TextView tv_nombre = (TextView) dialogIngrediente.findViewById(R.id.nomIngred);
		tv_nombre.setText(nombre);
		final EditText et_cantidad = (EditText) dialogIngrediente.findViewById(R.id.etCantidad);
		et_cantidad.setText(cantidad, TextView.BufferType.EDITABLE);
		final Spinner sp_medida = (Spinner) dialogIngrediente.findViewById(R.id.spMedida);
		adapSpinnerCat =new ArrayAdapter<String>(context ,android.R.layout.simple_spinner_item, categorias);
		adapSpinnerCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_medida.setAdapter(adapSpinnerCat);
		sp_medida.setSelection(adapSpinnerCat.getPosition(medida));
		((Button) dialogIngrediente.findViewById(R.id.aceptar)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(et_cantidad.getText().toString().equals("")){
					Toast.makeText(getContext(), "Por favor ingrese la cantidad", Toast.LENGTH_SHORT).show();
					return;
				}
				String cantidad_nueva = et_cantidad.getText().toString(); 
				ArrayList<IngredienteCantidad> nueva_lista = new ArrayList<IngredienteCantidad>();
				lista = globalClass.getListaIngrediente();
				int position = sp_medida.getSelectedItemPosition();
				String medida_nueva = categorias[position];
				for(IngredienteCantidad ingrediente : lista){
					if(ingrediente.getId().equals(id_alimento)){
						ingrediente.setCantidad(cantidad_nueva);
						ingrediente.setMedida(medida_nueva);
						nueva_lista.add(ingrediente);
					}
					else{
						nueva_lista.add(ingrediente);
					}
				}
				globalClass.setListaIngrediente(nueva_lista);
				mLinear.removeAllViews();
				for(IngredienteCantidad ing : nueva_lista){
					LayoutInflater inflater = null;
	                 inflater = (LayoutInflater) context
	                              .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	                 final View mLinearView = inflater.inflate(R.layout.item_alimento_ingrediente,null);
	                 TextView mNombre = (TextView) mLinearView.findViewById(R.id.nombreIng);
	                 TextView mCantidad = (TextView) mLinearView.findViewById(R.id.cantidadIng);
	                 TextView mId = (TextView) mLinearView.findViewById(R.id.id);
	                 TextView mMedida = (TextView) mLinearView.findViewById(R.id.medidaIng);
	                 ImageButton btnEliminar = (ImageButton) mLinearView.findViewById(R.id.ib_delete);
	                 mId.setText(ing.getId());
	                 final String id_alimento = mId.getText().toString();
	                 mNombre.setText(ing.getNombre());
	                 mCantidad.setText(ing.getCantidad());
	                 mMedida.setText(ing.getMedida());
	                 final String cantidad = mCantidad.getText().toString();
	                 final String medida = mMedida.getText().toString();
	                 final String nombre = mNombre.getText().toString();
	                 mLinear.addView(mLinearView);
	                 btnEliminar.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							btnEliminarAlimento(id_alimento);
							
						}
					});
	                 mLinearView.setOnClickListener(new OnClickListener() {

	                       @Override
	                       public void onClick(View v) {
	                    	   mostrarIngrediente(id_alimento, nombre, cantidad, medida);
	                       }
	                 });
				}
				
				
				dialogIngrediente.dismiss();
			}
		});
		
	}

}