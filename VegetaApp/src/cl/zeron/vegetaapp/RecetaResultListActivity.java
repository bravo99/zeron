package cl.zeron.vegetaapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class RecetaResultListActivity extends ActionBarActivity{
	
	@SuppressWarnings("unused")
	private CharSequence mTitle;
	TextView titulo;
	TextView descripcion;
	TextView tiempo;
	TextView personas;
	TextView valoracion;
		
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.receta_result_list);
		
		Bundle bundle = getIntent().getExtras();
		
		titulo = (TextView)findViewById(R.id.textView1);
		titulo.setText(bundle.getString("TITULO"));
		descripcion = (TextView)findViewById(R.id.textView2);
		descripcion.setText(bundle.getString("PREPARACION"));
		mTitle = getTitle(); // Get current title


		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		getSupportActionBar().setHomeButtonEnabled(true);
		
		
	    

	}

	
	@Override
	public Intent getSupportParentActivityIntent() {
	    Intent parentIntent= getIntent();
	    String className = parentIntent.getStringExtra("ParentClassName"); //getting the parent class name

	    Intent newIntent=null;
	    try {
	         //you need to define the class with package name
	         newIntent = new Intent(this,Class.forName(className));

	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    }
	    return newIntent;
	}
	
	
	
}
