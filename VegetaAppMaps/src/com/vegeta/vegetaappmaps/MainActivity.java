package com.vegeta.vegetaappmaps;

import java.util.ArrayList;

import com.parse.Parse;

import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends ActionBarActivity {

	private DrawerLayout drawerLayout;
	private ListView navList;
	private CharSequence mTitle;
	
	private String[] titulos;
	private ArrayList<ItemObject> NavItms;
	private TypedArray NavIcons;
	ListNavigationAdapter navAdapter;
	

	private ActionBarDrawerToggle drawerToggle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity_drawer);
		 Parse.initialize(this, "u9VdDovJEB22hYLgjjGm6hjaKH08dzuwhPq4dWGc", "gtDf7cQQEoGJrp89xgZfazpgYfnAyq2OJFBBuumK");
		mTitle = getTitle(); // Get current title

		this.drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		this.navList = (ListView) findViewById(R.id.left_drawer);
		
		NavIcons = getResources().obtainTypedArray(R.array.ic_nav_option);
		titulos=getResources().getStringArray(R.array.nav_options);
		NavItms = new ArrayList<ItemObject>();
		NavItms.add(new ItemObject(titulos[0],NavIcons.getResourceId(0, -1) ) );
		NavItms.add(new ItemObject(titulos[1],NavIcons.getResourceId(1, -1) ) );
		NavItms.add(new ItemObject(titulos[2],NavIcons.getResourceId(2, -1) ) );
		NavItms.add(new ItemObject(titulos[3],NavIcons.getResourceId(3, -1) ) );
		
		navAdapter = new ListNavigationAdapter(this, NavItms);
		navList.setAdapter(navAdapter);
		// Load an array of options names
		//final String[] names = getResources().getStringArray(
			//	R.array.nav_options);

		// Set previous array as adapter of the list
		//ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
			//	android.R.layout.simple_list_item_1, names);
	  //	navList.setAdapter(adapter);
		navList.setOnItemClickListener(new DrawerItemClickListener());
		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
				R.drawable.ic_drawer, R.string.open_drawer,
				R.string.close_drawer) {

			/**
			 * Called when a drawer has settled in a completely closed state.
			 */
			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle(mTitle);
				// creates call to onPrepareOptionsMenu()
				supportInvalidateOptionsMenu();
			}

			/**
			 * Called when a drawer has settled in a completely open state.
			 */
			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle("Selecciona opción");
				// creates call to onPrepareOptionsMenu()
				supportInvalidateOptionsMenu();
			}
		};

		// Set the drawer toggle as the DrawerListener
		drawerLayout.setDrawerListener(drawerToggle);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		
		 Fragment fragment = new MyFragment();
		 Bundle args = new Bundle();
 		 args.putString(MyFragment.KEY_TEXT, mTitle.toString());
 		 fragment.setArguments(args);
 		 FragmentManager fragmentManager = getSupportFragmentManager();
		 fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		drawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// Called by the system when the device configuration changes while your
		// activity is running
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Pass the event to ActionBarDrawerToggle, if it returns
		// true, then it has handled the app icon touch event
		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle your other action bar items...
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main2, menu);
		return true;
	}

	/*
	 * Called whenever we call invalidateOptionsMenu()
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view
		boolean drawerOpen = drawerLayout.isDrawerOpen(navList);
		menu.findItem(R.id.action_search).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	/** Swaps fragments in the main content view 
	 * aqui modificar todo
	 * */
	
	
	private void selectItem(int position) {
		// Get text from resources
		mTitle = getResources().getStringArray(R.array.nav_options)[position];

		// Create a new fragment and specify the option to show based on
		// position
		//Fragment fragment = new MyFragment();
		

		
		Fragment fragment = null;
		Bundle args = new Bundle();
		
		switch (position) {

        case 0:
            fragment = new ListaRecetasFragment();
            
    		args.putString(MyFragment.KEY_TEXT, mTitle.toString());
    		fragment.setArguments(args);
            break;

        case 1:
        	
            fragment = new MyFragment();
            
    		args.putString(MyFragment.KEY_TEXT, mTitle.toString());
    		fragment.setArguments(args);
            break;

        case 2:

            fragment = new MyFragment();
            
    		args.putString(MyFragment.KEY_TEXT, mTitle.toString());
    		fragment.setArguments(args);
            break;

        case 3:

            fragment = new MyFragment();
            
    		args.putString(MyFragment.KEY_TEXT, mTitle.toString());
    		fragment.setArguments(args);
            break;

        default:
            break;
       
        }

		// Insert the fragment by replacing any existing fragment
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();

		// Highlight the selected item, update the title, and close the drawer
		navList.setItemChecked(position, true);
		getSupportActionBar().setTitle(mTitle);
		drawerLayout.closeDrawer(navList);
	}

}