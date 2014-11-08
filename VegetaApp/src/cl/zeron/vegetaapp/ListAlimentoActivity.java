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

public class ListAlimentoActivity extends ActionBarActivity{
	
	ListView listAlimentos;
	CustomAdapterAlimento adapter;
	Bundle bundle;
	protected ProgressDialog proDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_alimento);
		bundle = getIntent().getExtras();
		listAlimentos = (ListView)findViewById(R.id.listalim);
		 
		startLoading();
		 
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Receta");
			query.getInBackground(bundle.getString("ID"), new GetCallback<ParseObject>() {
			  public void done(ParseObject object, ParseException e) {
				if (e == null) {
				 
	 
					adapter = new CustomAdapterAlimento(ListAlimentoActivity.this,object); 
					listAlimentos.setEmptyView(findViewById(R.id.listvacia));
					listAlimentos.setAdapter(adapter);	 	
				     if (listAlimentos != null){
				    	stopLoading();
				     }	
					
				} else {
			    	stopLoading();
			    }
				
			  }
			  
			
			});
			
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);		
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setTitle("Ingredientes" );
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
