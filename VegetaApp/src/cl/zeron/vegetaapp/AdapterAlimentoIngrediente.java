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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
	ArrayList<IngredienteCantidad> lista = new ArrayList<IngredienteCantidad>();
	Dialog dialogIngrediente; 
	Dialog dialogAlimento;
	private GlobalClass globalClass;
	private ListView lv_ingredientes;
	final String[] categorias = {"Taza", "Cucharada", "Cucharadita", "Unidad", "Gramos", "Otros" };
	ArrayAdapter<String> adapSpinnerCat;
	public AdapterAlimentoIngrediente(Context context, ListView listView, Dialog dialogAlimento) {
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
		this.lv_ingredientes = listView;
		globalClass = (GlobalClass) context.getApplicationContext();
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
				final IngredienteCantidad ing = new IngredienteCantidad();
				if(globalClass.getListaIngrediente() != null){
					for(IngredienteCantidad ingrediente_cantidad : globalClass.getListaIngrediente()){
						if(ingrediente_cantidad.getId().equals(object.getObjectId())){
							Toast.makeText(context,object.getString("nombre")+" ya está en la lista", Toast.LENGTH_SHORT).show();
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
						AdapterIngredienteCantidad adapter = new AdapterIngredienteCantidad((Activity) context, lista, lv_ingredientes);
						lv_ingredientes.setAdapter(adapter);
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

}