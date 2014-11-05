package cl.zeron.vegetaapp;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class AlimentoResultListActivity extends ActionBarActivity{
	
	TextView nombre;
	TextView info;
	ImageView imagen;
	Bundle bundle;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alimento_result_list);
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
	
	@Override
	public Intent getSupportParentActivityIntent() {
		onBackPressed();

		Intent newIntent=null;
        return newIntent;
	}
}
