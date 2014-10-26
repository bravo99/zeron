package cl.zeron.vegetaapp;

import java.util.List;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class RecetaResultListActivity extends ActionBarActivity{
	
	@SuppressWarnings("unused")
	private CharSequence mTitle;
	TextView titulo;
	TextView descripcion;
	TextView tiempo;
	TextView personas;
	TextView valoracion;
	TextView ingred;
	ParseImageView imagen;	
	Button verComentarios;
	Dialog dialogComentarios;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.receta_result_list);
		
		Bundle bundle = getIntent().getExtras();
		
		imagen = (ParseImageView)findViewById(R.id.iv_imag_receta);
		ingred = (TextView)findViewById(R.id.textView3);
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Receta");
		query.getInBackground(bundle.getString("ID"), new GetCallback<ParseObject>() {
		  public void done(ParseObject object, ParseException e) {
			if (e == null) {
				//imagen
		    	ParseFile imageFile = object.getParseFile("imagen");
		    	if (imageFile != null) {
					imagen.setParseFile(imageFile);
					imagen.loadInBackground();								
				}
		    	//ingredientes
				
				ParseQuery<ParseObject> query2 = ParseQuery.getQuery("AlimentoReceta");
				query2.whereEqualTo("receta", object);
				 
				query2.findInBackground(new FindCallback<ParseObject>() {
				  public void done(List<ParseObject> commentList, ParseException e) {
					  System.out.println(commentList.size());
					 
					  String a = "";
					  for(int i=0; i<commentList.size(); i++){
						 try {
							a +=  commentList.get(i).getString("cantidad") + " " +
								  commentList.get(i).getString("medida") + " " +
								  commentList.get(i).getParseObject("ingrediente").fetchIfNeeded()
							      .getString("nombre") + "\n";
						} catch (ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					  }
					  ingred.setText(a);
				      
					 
				  }

				
				});
				
				
		    } else {
		      // something went wrong
		    }
		  }

		
		});
		
		
		titulo = (TextView)findViewById(R.id.textView1);
		titulo.setText(bundle.getString("TITULO"));
		
		descripcion = (TextView)findViewById(R.id.textView2);
		descripcion.setText(bundle.getString("PREPARACION"));
		
		tiempo = (TextView)findViewById(R.id.tiempo);
		tiempo.setText(bundle.getString("TIEMPO"));
		
		personas = (TextView)findViewById(R.id.personas);
		personas.setText(bundle.getString("PERSONAS")+"[pers]");
		
		valoracion = (TextView)findViewById(R.id.valoracion);
		valoracion.setText("5/"+bundle.getString("VALORACION"));
		
		verComentarios = (Button)findViewById(R.id.ver_comentarios);
		verComentarios.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		
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
