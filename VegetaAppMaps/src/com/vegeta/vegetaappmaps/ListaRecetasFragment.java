package com.vegeta.vegetaappmaps;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

public class ListaRecetasFragment extends Fragment {
	
	final String[] categorias = {"Bocadillo","Ensalada","Pastas","Postre","Principal","Sopa"};
	private Spinner sp_categoria;
	private ListView lv_recetas;
	Activity act;
	private CustomAdapterRecetas recetasAdapter;
	ArrayAdapter<String> adaptadorSpinner;
	private FragmentIterationListener mCallback = null;
    public interface FragmentIterationListener{
        public void onFragmentIteration(Bundle parameters);
    }
	
	
	

	//El fragment se ha adjuntado al Activity
    @Override
    public void onAttach(Activity activity) {
    	super.onAttach(activity);
    	this.act=activity;
        try{
            mCallback = (FragmentIterationListener) activity;
        }catch(ClassCastException ex){
            Log.e("ExampleFragment", "El Activity debe implementar la interfaz FragmentIterationListener");
        }
    }
     
    //El Fragment ha sido creado        
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        adaptadorSpinner =new ArrayAdapter<String>(act,android.R.layout.simple_spinner_item, categorias);
		adaptadorSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			
		
    }
     
    //El Fragment va a cargar su layout, el cual debemos especificar
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    	View v = inflater.inflate(R.layout.lista_receta_fragment, container, false);
    	if(v != null){
    		sp_categoria = (Spinner)v.findViewById(R.id.spinner_categoria_receta);
    		lv_recetas = (ListView)v.findViewById(R.id.lv_receta);
    		sp_categoria.setAdapter(adaptadorSpinner);
    		if(recetasAdapter==null){
				recetasAdapter = new CustomAdapterRecetas(act,sp_categoria.getSelectedItem().toString());
			}
    		
    		lv_recetas.setAdapter(recetasAdapter);
			recetasAdapter.loadObjects();
			
			sp_categoria.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					recetasAdapter = new CustomAdapterRecetas(act,categorias[position]);
					lv_recetas.setAdapter(recetasAdapter);
					recetasAdapter.loadObjects();
					
										
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub
					
				}
    			
			});
    		
    		
    	}
    	return v;
    }
     
    //La vista de layout ha sido creada y ya está disponible
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        
        
        
    }
     
    //La vista ha sido creada y cualquier configuración guardada está cargada
    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }
     
    //El Activity que contiene el Fragment ha terminado su creación
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
     
    //El Fragment ha sido quitado de su Activity y ya no está disponible
    @Override
    public void onDetach() {
        super.onDetach();
    }
    
    
   	
}
