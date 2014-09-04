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
import android.widget.ListView;

public class Ver extends Activity{
		
	ListView lv2;
	String algo;
	private ArrayList<ParseObject> posts;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ver);
		lv2 = (ListView)findViewById(R.id.listView1);
		
		posts = new ArrayList<ParseObject>();
		init(this);
		
	}
	
	public void init(final Context ct){
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Receta");
		query.whereEqualTo("estado", true);
		
		
		query.findInBackground( new FindCallback<ParseObject>() {
			
			@Override
			public void done(List<ParseObject> scoreList, ParseException e) {
		    	System.out.println(e);
		    	if (e == null) {
		        	  	
		            Log.d("score", "Retrieved " + scoreList.size() + " scores");
		            
		            posts.clear();
					for (ParseObject post : scoreList) {
						posts.add(post);
					}
										
					ItemQueryAdapter adapter = new ItemQueryAdapter((Activity) ct,posts);
					lv2.setAdapter(adapter);
		            
		        } else {
		        	
		            Log.d("score", "Error: " + e.getMessage());
		        }
		        
		    }
		});
		
		
		
		
		
		
	}
		
	

	

}
