package com.example.vegeta;

import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class Ingred extends Activity{
	
	ArrayList<ParseObject> posts;
	ListView lv1;
	Button agregar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)  {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ingred);
		posts = new ArrayList<ParseObject>();
        init(this);
	}

	private void init(final Context ct) {
		lv1 =(ListView) findViewById(R.id.lvi1); 
		agregar =(Button) findViewById(R.id.bti1);
		agregar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//aki ya tengo info y debo mandar
				
			}
		});
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Ingrediente");
		
		query.findInBackground( new FindCallback<ParseObject>() {
			
			@Override
			public void done(List<ParseObject> itemList, ParseException e) {
		    	
		    	if (e == null) {
		        	  	
		            Log.d("score", "Retrieved " + itemList.size() + " scores");
		            
		            posts.clear();
					for (ParseObject post : itemList) {
						posts.add(post);
					}
										
					ItemRatioQueryAdapter adapter = new ItemRatioQueryAdapter((Activity) ct ,posts);
					lv1.setAdapter(adapter);
		            
		        } else {
		        	
		            Log.d("score", "Error: " + e.getMessage());
		        }
		        
		    }
		});
		
	}
}
