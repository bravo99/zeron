package com.example.vegeta;

import java.util.ArrayList;
import com.parse.ParseObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Form extends Activity implements OnClickListener{
	Button btsiguiente;
	Intent a;
	EditText titulo,descripcion,autor;
	String[] info;
	Toast toast1; 
	ArrayList<ParseObject> posts;
	final static String ACT_INFO = "com.example.vegeta.Result";
	
	@Override
	protected void onCreate(Bundle savedInstanceState)  {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form);
		init();
	}
	
	@SuppressLint("ShowToast")
	public void init(){
		btsiguiente=(Button)findViewById(R.id.button4);
		btsiguiente.setOnClickListener(this);
		titulo=(EditText)findViewById(R.id.ettitulo);
		descripcion =(EditText)findViewById(R.id.desc);
		autor=(EditText)findViewById(R.id.autor);
		info = new String[3];
		toast1 =  Toast.makeText(getApplicationContext(),"Debes rellenar todos los campos", Toast.LENGTH_SHORT);
			
		
		
	}
	
		
	@Override
	public void onClick(View v) {
		
		ParseObject receta = new ParseObject("Receta");
		
		info[0] = titulo.getText().toString();
		info[1] = descripcion.getText().toString();	
		info[2] = autor.getText().toString();
		
		if(titulo.getText().toString() != null && (!titulo.getText().toString().equals("")) && 
		  descripcion.getText().toString() != null &&(!descripcion.getText().toString().equals("")) &&
		 autor.getText().toString() != null && (!autor.getText().toString().equals(""))) {
			
			
			receta.put("nombre", titulo.getText().toString());
			receta.put("preparacion", descripcion.getText().toString() );
			//siempre autor sera vegetatest para este entregable
			
			receta.put("autor", "vegetatest" );
			receta.put("estado", false );
			receta.saveInBackground();
			
			
			a = new Intent(this, Result.class);
			a.putExtra(ACT_INFO, info);
			startActivity(a);
		}	
		else 
			 
	        toast1.show();
		
		
	}
	
	
}
