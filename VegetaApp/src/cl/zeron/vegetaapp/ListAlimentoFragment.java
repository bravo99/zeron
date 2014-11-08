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
import android.widget.ListView;
import android.widget.Toast;

public class ListAlimentoFragment extends Fragment implements OnQueryTextListener, OnActionExpandListener{

	Activity act;
	ListView listAlimentos;
	CustomAdapterAlimento2 adAlimento;
	
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
        
        
		
        	  
		setHasOptionsMenu(true);
    }
    
  
   
    //El Fragment va a cargar su layout, el cual debemos especificar
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    	View v = inflater.inflate(R.layout.list_alimento, container, false);
    	
         if(v != null){
        	 listAlimentos = (ListView)v.findViewById(R.id.listalim);
        	 listAlimentos.setEmptyView(v.findViewById(R.id.emptyListViewAli));
        	 adAlimento= new CustomAdapterAlimento2(act);
        	 listAlimentos.setAdapter(adAlimento);
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
	        		Toast.makeText(getActivity(), "Debes estar registrado para agregar un Alimento", Toast.LENGTH_SHORT).show();
	        		return false;
	        	}
	       	Intent intent = new Intent(act.getApplicationContext(), AlimentoActivity.class);
	       	startActivity(intent);
	        	
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
		listAlimentos.setAdapter(null);
		
		String busquedaUC, busquedatodoLC;
		
		busquedaUC = (busqueda.substring(0, 1).toUpperCase()).concat(
				(busqueda.substring(1, busqueda.length())).toLowerCase());
		
		busquedatodoLC = (busqueda.substring(0,busqueda.length())).toLowerCase();
		
		Toast.makeText(act.getApplicationContext(), "Buscando: "+ busqueda, Toast.LENGTH_SHORT).show();
		adAlimento= new CustomAdapterAlimento2(act,busqueda,busquedaUC,busquedatodoLC);
		listAlimentos.setAdapter(adAlimento);
		
		adAlimento.loadObjects();
		
		return false;
		
		
	}
	
}
