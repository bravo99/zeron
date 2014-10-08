package com.example.vegeta;

import com.parse.ParseAnonymousUtils;
import com.parse.ParseUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Ingrediente extends Activity implements OnClickListener {
	
	Button btn8;
	Button btn9;
	Button btn10;
	Intent i;
	ParseUser pu;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ingrediente);
		init();
		pu = ParseUser.getCurrentUser();
		
	}
	public void init(){
		
		btn8 = (Button)findViewById(R.id.button1);
		btn8.setOnClickListener(this);
		btn9 = (Button)findViewById(R.id.button2);
		btn9.setOnClickListener(this);
		btn10 = (Button)findViewById(R.id.button3);
		btn10.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		
	          
		int id;
		id= v.getId();
		switch (id){
		
		 	case R.id.button1: //lanzar formulario
		 		if (pu.equals(null)){
		 			Toast.makeText(getApplicationContext(),"Inicie sesion para acceder a esta funcionalidad",Toast.LENGTH_LONG).
		 			show();
		 		}
		 		else{
		 		i = new  Intent(this,FormIngrediente.class);
		 		startActivity(i);}
		 		break;
			case R.id.button2:
				if (false){
		 			Toast.makeText(getApplicationContext(),"Inicie sesion para acceder a esta funcionalidad",Toast.LENGTH_LONG).
		 			show();
		 		}else{
				i = new Intent(this,MisIngredientes.class);
				startActivity(i);}
				break;
			case R.id.button3:
				i = new Intent(this, BuscarIngredienteActivity.class);
				startActivity(i);
				break;
		}
		
	}

}
