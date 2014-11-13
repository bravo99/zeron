package cl.zeron.vegetaapp;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AlimentoResultListActivity extends ActionBarActivity{
	
	TextView nombre;
	TextView info;
	ImageView imagen;
	Bundle bundle;
	ParseUser currentUser;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alimento_result_list);
		
		currentUser = ParseUser.getCurrentUser();
		bundle= getIntent().getExtras();
		
		nombre=(TextView)findViewById(R.id.nom_alimento);
		nombre.setText(bundle.getString("NOMBRE"));
		
		info=(TextView)findViewById(R.id.info_alimento);
		info.setText(bundle.getString("INFO"));
		
		imagen=(ImageView)findViewById(R.id.image_alimento);
		imagen.setImageBitmap(BitmapFactory.decodeFile(bundle.getString("IMAG")));
		
		
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setTitle(bundle.getString("TITULO"));
		
		
	}
	
	
public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.icono_alim, menu);
		 
	    return super.onCreateOptionsMenu(menu);
	}
	
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    
		switch (item.getItemId()) {
			
		case R.id.menu_lugar:
			Intent intent = new Intent(getApplicationContext(),MainActivity.class);
   		    intent.putExtra("fragment","mapa");
   		    startActivity(intent);
			Toast.makeText(getApplicationContext(), "lugar", Toast.LENGTH_SHORT).show();
		return true;
			
		case R.id.menu_editar:
			
			if(currentUser != null){
				ParseQuery<ParseObject> queryElimOnLine = ParseQuery.getQuery("Alimento");
    			queryElimOnLine.getInBackground(bundle.getString("ID"), new GetCallback<ParseObject>() {
    			public void done(final ParseObject object, ParseException e) {
    			     if (e == null) {
    			    	 
    			    	 //COMPROBAR SI ES EL CREADOR
    			    	 ParseUser creador = object.getParseUser("creado_por");
    			    	 if(creador.getObjectId().equals(currentUser.getObjectId())){
    			    		 Intent intent = new Intent(getApplicationContext(),AlimentoEditActivity.class);
    			    		 intent.putExtras(bundle);
    			    		 startActivity(intent);
    			    	 }
    			    	 else{
    			    		 Toast.makeText(getApplicationContext(), 
    			    				 "Solo el creador puede editar", Toast.LENGTH_SHORT).show();
    			    	 }
    			     }		
    			 }});
    			
			}
			return true;	
								
			default:
	            return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public Intent getSupportParentActivityIntent() {
		onBackPressed();

		Intent newIntent=null;
        return newIntent;
	}
	
	
	        
}
