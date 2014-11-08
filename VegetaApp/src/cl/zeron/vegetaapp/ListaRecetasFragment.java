package cl.zeron.vegetaapp;


import com.parse.ParseUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.MenuItemCompat.OnActionExpandListener;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class ListaRecetasFragment extends Fragment implements OnQueryTextListener, OnActionExpandListener {
	
	final String[] categorias = {"Todas","Aperitivo","Ensalada","Pastas","Postres","Principal","Sopa","Salsas" };
	private Spinner sp_categoria;
	private ListView lv_recetas;
	Activity act;
	private CustomAdapterRecetas recetasAdapter;
	ArrayAdapter<String> adaptadorSpinner;
	@SuppressWarnings("unused")
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
	  
		setHasOptionsMenu(true);
    }
    
  
   
    //El Fragment va a cargar su layout, el cual debemos especificar
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    	View v = inflater.inflate(R.layout.lista_receta_fragment, container, false);
    	
         if(v != null){
    		sp_categoria = (Spinner)v.findViewById(R.id.spinner_categoria_receta);
    		lv_recetas = (ListView)v.findViewById(R.id.lv_receta);
    		lv_recetas.setEmptyView(v.findViewById(R.id.listvacia));
    		sp_categoria.setAdapter(adaptadorSpinner);
    		    		
    		   		
			sp_categoria.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					if(categorias[position]=="Todas"){
						recetasAdapter = new CustomAdapterRecetas(act);
						lv_recetas.setAdapter(recetasAdapter);
						recetasAdapter.loadObjects();
					}else{
						recetasAdapter = new CustomAdapterRecetas(act,categorias[position]);
						lv_recetas.setAdapter(recetasAdapter);
						recetasAdapter.loadObjects();
					}								
				}
								
				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					
					
				}
    			
			});
     	}
        
    	return v;
    }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Pass the event to ActionBarDrawerToggle, if it returns
		// true, then it has handled the app icon touch event
    	switch (item.getItemId()) {
	        case R.id.menu3_add:
	        	if(ParseUser.getCurrentUser() == null){
		        	Toast.makeText(getActivity(), "Debes iniciar sesion para agregar Recetas", Toast.LENGTH_SHORT).show();
		        	return false;

	        	}
	        	Intent intent = new Intent(act.getApplicationContext(), RecetaActivity.class);
	        	startActivity(intent);
	        	//}else{Toast.makeText(this, "Debes iniciar sesión", Toast.LENGTH_SHORT);act.onBackPressed();}
	        	
	            return true;
	            
	        
	            
	        default:
	            return super.onOptionsItemSelected(item);
        }
	}
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
                
    	act.getMenuInflater().inflate(R.menu.vistabusqueda, menu);
    	 MenuItem searchItem = menu.findItem(R.id.menu3_buscar);
    	
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
	    searchView.setOnQueryTextListener(this);
    
	    MenuItemCompat.setOnActionExpandListener(searchItem, this);
	    super.onCreateOptionsMenu(menu, inflater);
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

    public boolean onMenuItemActionCollapse(MenuItem arg0) {
		
		return true;
	}
	
	public boolean onMenuItemActionExpand(MenuItem arg0) {
		
		return true;
	}

	@Override
	public boolean onQueryTextChange(String arg0) {
		//Toast.makeText(act.getApplicationContext(), arg0, Toast.LENGTH_SHORT).show();
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String busqueda) {
		lv_recetas.setAdapter(null);
		String busquedaUC, busquedatodoLC;
		
		busquedaUC = (busqueda.substring(0, 1).toUpperCase()).concat(
				(busqueda.substring(1, busqueda.length())).toLowerCase());
		
		busquedatodoLC = (busqueda.substring(0,busqueda.length())).toLowerCase();
		
		Toast.makeText(act.getApplicationContext(), "Buscando: "+ busqueda, Toast.LENGTH_SHORT).show();
		recetasAdapter = new CustomAdapterRecetas(act,busqueda,busquedaUC,busquedatodoLC);
		lv_recetas.setAdapter(recetasAdapter);
		
		recetasAdapter.loadObjects();
		
		return false;
		
		
	}
    
    
   	
}
