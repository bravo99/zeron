package com.example.vegeta;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Result extends Activity {
    TextView resultado1;
    TextView resultado2;
    TextView resultado3;
    String[] info;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result);
		init();
	}
	public void init(){
		resultado1= (TextView)findViewById(R.id.r1);
		resultado2= (TextView)findViewById(R.id.r2);
		resultado3= (TextView)findViewById(R.id.r3);
		Intent men = getIntent();
		info = men.getStringArrayExtra(Form.ACT_INFO);
		resultado1.setText("Receta: "+info[0]);
		resultado2.setText("Descripcion: "+info[1]);
		resultado3.setText("Autor: "+info[2]);
		
	}

}
