package com.example.vegeta;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Recetas extends Activity implements OnClickListener{
	Button btn7;
	Button btn6;
	Button btn5;
	Intent i;
	boolean estado;
	
	
	
	
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.receta);
		init();
		
	}
	
	//Instaciar
		public void init(){
			Bundle bundle = getIntent().getExtras();
			estado = bundle.getBoolean("ESTADO");
			btn7 = (Button)findViewById(R.id.button7);
			btn7.setOnClickListener(this);
			btn6 = (Button)findViewById(R.id.button6);
			btn6.setOnClickListener(this);
			btn5 = (Button)findViewById(R.id.button5);
			btn5.setOnClickListener(this);
		}

	@Override
	public void onClick(View v) {
		int id;
		id= v.getId();
		switch (id){
		
		 	case R.id.button6: //lanzar formulario
		 		if(estado==false){
		 			Toast.makeText(getApplicationContext(),"Solo puede acceder iniciando sesion",Toast.LENGTH_LONG).
		 			show();
		 		}
		 		else{
		 		i = new  Intent(this,FormRecetaActivity.class);
		 		startActivity(i);}
		 		break;
			case R.id.button7:
				if(estado==false){
		 			Toast.makeText(getApplicationContext(),"Solo puede acceder iniciando sesion",Toast.LENGTH_LONG).
		 			show();}
				else{
				i = new Intent(this,MisRecetas.class);
				startActivity(i);}
				break;
			case R.id.button5:
				Bundle bundle2 = new Bundle();
				i = new Intent(this, BuscarRecetaActivity.class);
				
				if(estado==false){
					bundle2.putBoolean("ESTADO", false);
				}
				else{
					//para poder calificarla
					bundle2.putBoolean("ESTADO", true);
				}
				
			    i.putExtras(bundle2);
			    startActivity(i);
				break;
		 			
				
		}
		
	}
	

}
