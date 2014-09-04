package com.example.vegeta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ResultIngrediente extends Activity {
	TextView resultado1;
    TextView resultado2;
    Toast toast1; 
    
    ImageView im;
    String[] info;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resultingrediente);
		
		init(this);
		
	}
	
	public void init(final Context ct){
		resultado1= (TextView)findViewById(R.id.textView2);
		resultado2= (TextView)findViewById(R.id.textView4);
		im = (ImageView)findViewById(R.id.imageView1);
		
		toast1 =  Toast.makeText(getApplicationContext(),"Nuevo ingrediente disponible ", Toast.LENGTH_SHORT);
		Intent men = getIntent();
		info = men.getStringArrayExtra(FormIngrediente.ACT_INFO);
		resultado1.setText(info[0]);
		resultado2.setText(info[1]);
		
		im.setImageBitmap(BitmapFactory.decodeFile(info[3]));
		
		if(info[2].equals("Form")){
			 toast1.show();
		}
		
		
	}
		
	
}
