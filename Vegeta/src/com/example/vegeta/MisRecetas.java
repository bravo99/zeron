package com.example.vegeta;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class MisRecetas extends Activity {
	
	ListView lv2;
	String algo;
	private ArrayList<ParseObject> posts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.misrecetas);
        lv2 = (ListView)findViewById(R.id.listv2);
		posts = new ArrayList<ParseObject>();
		init(this);
		
	}
	
public void init(final Context ct){
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Receta");
		query.whereEqualTo("autor", "vegetatest");
		
		
		query.findInBackground( new FindCallback<ParseObject>() {
			
			@Override
			public void done(List<ParseObject> recetaList, ParseException e) {
		    	
		    	if (e == null) {
		        	  	
		            Log.d("score", "Retrieved " + recetaList.size() + " scores");
		            
		            posts.clear();
					for (ParseObject post : recetaList) {
						posts.add(post);
					}
										
					ItemQueryAdaptermr adapter = new ItemQueryAdaptermr((Activity) ct,posts);
					lv2.setAdapter(adapter);
		            
		        } else {
		        	
		            Log.d("score", "Error: " + e.getMessage());
		        }
		        
		    }
		});
		
		
		
		
		
		
	}

	

}
