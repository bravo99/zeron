package com.example.vegeta;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Recetas extends Activity implements OnClickListener{
	Button btn7;
	Button btn6;
	Button btn5;
	Intent i;
	
	
	
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.receta);
		init();
		
	}
	
	//Instaciar
		public void init(){
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
		 		i = new  Intent(this,Form.class);
		 		startActivity(i);
		 		break;
			case R.id.button7:
				i = new Intent(this,MisRecetas.class);
				startActivity(i);
				break;
			case R.id.button5:
				i = new Intent(this, Ver.class);
				startActivity(i);
				break;
		}
		
	}
	

}
