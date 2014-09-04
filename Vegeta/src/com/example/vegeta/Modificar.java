package com.example.vegeta;
import java.util.List;

import com.example.vegeta.R;
import com.example.vegeta.Result1;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


public class Modificar extends Activity implements OnClickListener{
	
	EditText resultado1;
    EditText resultado2;
    EditText resultado3;
    Button guardar;
	String[] info;
	
protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modificar);
		init();
	}

private void init() {
	resultado1 = (EditText)findViewById(R.id.etm1);
	resultado2 = (EditText)findViewById(R.id.etm2);
	resultado3 = (EditText)findViewById(R.id.etm3);
	guardar = (Button)findViewById(R.id.bnm1);
	Intent men = getIntent();
	info = men.getStringArrayExtra(Result1.ACT_INFO);
	resultado1.setText(info[0]);
	resultado2.setText(info[1]);
}

@Override
public void onClick(View v) {
	
	ParseQuery<ParseObject> query = ParseQuery.getQuery("Receta");
	query.whereEqualTo("objectId", info[3]);
	
	
	query.findInBackground( new FindCallback<ParseObject>() {
		
		@Override
		public void done(List<ParseObject> recetaList, ParseException e) {
	    	
	    	if (e == null) {
	    		recetaList.get(0).put("nombre", resultado1.getText().toString());
	    		recetaList.get(0).put("preparacion", resultado2.getText().toString());
	    		recetaList.get(0).put("ingrediente", resultado3.getText().toString());
	        	recetaList.get(0).saveInBackground(); 	
	            Log.d("score", "Retrieved " + recetaList.size() + " scores");
	            
	            				            
	        } else {
	        	
	            Log.d("score", "Error: " + e.getMessage());
	        }
	        
	    }
	});
	
}

}
