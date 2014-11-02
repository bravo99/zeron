package cl.zeron.vegetaapp;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;
import android.widget.TextView;

public class ListComentarioActivity extends ActionBarActivity{
	Bundle bundle;
	ListView lv_comentario;
	TextView algo;
	
	CustomAdapterComentario adapter;
	
	protected ProgressDialog proDialog;
		
    
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		bundle=getIntent().getExtras();
		lv_comentario = (ListView)findViewById(R.id.listcomentario);
		
		
		startLoading();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Receta");
		query.getInBackground(bundle.getString("ID"), new GetCallback<ParseObject>() {
		  public void done(ParseObject object, ParseException e) {
			if (e == null) {
			 
 
				adapter = new CustomAdapterComentario(getApplicationContext(),object); 
				
				lv_comentario.setAdapter(adapter);	 	
			     if (lv_comentario != null){
			    	 stopLoading();
			     }	
				
			} else {
		    	stopLoading();
		    }
			
		  }
		  
		
		});
		
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setTitle("Comentarios" );
		getSupportActionBar().setSubtitle( bundle.getString("RECETA"));
	}
	
	@Override
	public Intent getSupportParentActivityIntent() {
		onBackPressed();

	    Intent newIntent=null;
        return newIntent;
	}
	
	protected void startLoading() {
		proDialog = new ProgressDialog(this);
	    proDialog.setMessage("loading...");
	    proDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	    proDialog.setCancelable(true);
	    proDialog.show();
	}

	protected void stopLoading() {
	    proDialog.dismiss();
	    proDialog = null;
	}
}
