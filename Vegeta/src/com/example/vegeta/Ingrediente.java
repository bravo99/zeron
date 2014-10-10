package com.example.vegeta;

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
	boolean estado;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ingrediente);
		init();
	
		
	}
	public void init(){
		Bundle bundle = getIntent().getExtras();
		estado = bundle.getBoolean("ESTADO");
		
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
		 		if (estado==false){
		 			Toast.makeText(getApplicationContext(),"Solo puede acceder iniciando sesion",Toast.LENGTH_LONG).
		 			show();
		 		}
		 		else{
		 		i = new  Intent(this,FormIngrediente.class);
		 		startActivity(i);}
		 		break;
			case R.id.button2:
				if (estado==false){
		 			Toast.makeText(getApplicationContext(),"Solo puede acceder iniciando sesion",Toast.LENGTH_LONG).
		 			show();
		 		}else{
				i = new Intent(this,MisIngredientes.class);
				startActivity(i);}
				break;
			case R.id.button3:
				Bundle bundle2 = new Bundle();
				i = new Intent(this, BuscarIngredienteActivity.class);
				
				if(estado==false){
					bundle2.putBoolean("ESTADO", false);
				}
				else{
					bundle2.putBoolean("ESTADO", true);
				}
				
			    i.putExtras(bundle2);
			    startActivity(i);
				break;
		}
		
	}

}
