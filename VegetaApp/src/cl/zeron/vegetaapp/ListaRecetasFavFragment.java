package cl.zeron.vegetaapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class ListaRecetasFavFragment extends Fragment{
	
	
	private ListView lv_recetas;
	Activity act;
	
	private CustomAdapterRecetasFav recetasAdapter;
	
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
        
    }
     
    //El Fragment va a cargar su layout, el cual debemos especificar
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    	View v = inflater.inflate(R.layout.list, container, false);
    	if(v != null){
    		
    		lv_recetas = (ListView)v.findViewById(R.id.listcomentario);
    	    lv_recetas.setEmptyView(v.findViewById(R.id.emptyListView));
    		recetasAdapter = new CustomAdapterRecetasFav(act);
    		lv_recetas.setAdapter(recetasAdapter);
    		recetasAdapter.loadObjects();
		
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
