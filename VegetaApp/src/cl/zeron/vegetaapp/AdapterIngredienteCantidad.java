package cl.zeron.vegetaapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;



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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AdapterIngredienteCantidad extends BaseAdapter{
	protected Activity activity;
	protected ArrayList<IngredienteCantidad> items;
	private CharSequence[] options = {"Editar", "Eliminar"};
	private GlobalClass globalClass;
	private Dialog dialogIngrediente;
	private ListView lv_ingrediente;
	ArrayAdapter<String> adapSpinnerCat;
	
	public AdapterIngredienteCantidad(Activity activity,
			ArrayList<IngredienteCantidad> items, ListView lv_ingredientes) {
		this.activity = activity;
		this.items = items;
		this.lv_ingrediente = lv_ingredientes;
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
        	vi = inflater.inflate(R.layout.item_receta_ingrediente, null);
        }
            
        final IngredienteCantidad item = items.get(position);
        
        TextView nombre = (TextView) vi.findViewById(R.id.nombreIng);
        nombre.setText(item.getNombre());
        
        TextView cantidad = (TextView) vi.findViewById(R.id.cantidadIng);
        cantidad.setText(item.getCantidad());
        
        vi.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View view) {
				AlertDialog.Builder builder = new AlertDialog.Builder(activity);
				builder.setItems(options, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch(which){
						case 0:
							editarIngrediente(item);
							break;
						case 1:
							eliminarIngrediente(item);
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
	
	public void editarIngrediente(final IngredienteCantidad item){
		dialogIngrediente = new Dialog(activity, R.style.Theme_Dialog_Translucent);
		dialogIngrediente.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogIngrediente.setContentView(R.layout.ingredientereceta);
		dialogIngrediente.setCanceledOnTouchOutside(false);
		EditText et_cantidad = (EditText) dialogIngrediente.findViewById(R.id.etCantidad);
		et_cantidad.setText(item.getCantidad(), TextView.BufferType.EDITABLE);
		dialogIngrediente.show();
		TextView titleTextView = (TextView) dialogIngrediente.findViewById(R.id.nomIngred);
		titleTextView.setText(item.getNombre());
		final String[] categorias = {"Taza", "Cucharada", "Cucharadita", "Unidad", "Gramos", "Otros" };
		final Spinner spMedida = (Spinner) dialogIngrediente.findViewById(R.id.spMedida);
		adapSpinnerCat =new ArrayAdapter<String>(activity ,android.R.layout.simple_spinner_item, categorias);
		adapSpinnerCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		int position = 0;
		for(int i = 0; i<categorias.length ;i++){
			if(categorias[i].equals(item.getMedida())){
				position=i;
				break;
			}
		}
		spMedida.setAdapter(adapSpinnerCat);
		spMedida.setSelection(position);
		((Button) dialogIngrediente.findViewById(R.id.aceptar)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String cantidad_input = "";
				EditText et_cantidad = (EditText) dialogIngrediente.findViewById(R.id.etCantidad);
				cantidad_input = et_cantidad.getText().toString();
				if(cantidad_input.equals("")){
					Toast.makeText(activity , "Por favor ingrese la Cantidad", Toast.LENGTH_SHORT).show();
					return;
				}
				ArrayList<IngredienteCantidad> lista = globalClass.getListaIngrediente();
				ArrayList<IngredienteCantidad> lista_nueva = new ArrayList<IngredienteCantidad>();
				
				IngredienteCantidad ingrediente = new IngredienteCantidad();
				ingrediente.setId(item.getId());
				ingrediente.setNombre(item.getNombre());
				ingrediente.setCantidad(cantidad_input);
				ingrediente.setMedida(spMedida.getSelectedItem().toString());
				lista_nueva.add(ingrediente);
				for(IngredienteCantidad ing : lista){
					if(!ing.getId().equals(item.getId())){
						lista_nueva.add(ing);
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
				AdapterIngredienteCantidad adapter =  new AdapterIngredienteCantidad(activity, lista_nueva, lv_ingrediente);
				lv_ingrediente.setAdapter(adapter);
				dialogIngrediente.dismiss();
			}
		});
		
		
		
	}
	
	public void eliminarIngrediente(final IngredienteCantidad item){
		ArrayList<IngredienteCantidad> lista = globalClass.getListaIngrediente();
		ArrayList<IngredienteCantidad> lista_nueva = new ArrayList<IngredienteCantidad>();
		if(lista != null){
			for(IngredienteCantidad ing : lista){
				if(!item.getId().equals(ing.getId())){
					lista_nueva.add(ing);
				}
			}
			globalClass.setListaIngrediente(lista_nueva);
			Comparator<IngredienteCantidad> comparator = new Comparator<IngredienteCantidad>() {
				
				@Override
				public int compare(IngredienteCantidad a, IngredienteCantidad b) {
					int resultado = a.getNombre().compareTo(b.getNombre());
					if(resultado!=0){
						return resultado;
					}
					return resultado;
				}
			};
			
			Collections.sort(lista_nueva, comparator);
			AdapterIngredienteCantidad adapter = new AdapterIngredienteCantidad(activity, lista_nueva, lv_ingrediente);
			lv_ingrediente.setAdapter(adapter);
		}
	}

}
