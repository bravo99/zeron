package com.example.vegeta;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.vegeta.util.SystemUiHider;

import android.app.Activity;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

import android.widget.Toast;


import com.parse.ParseUser;


public class Inicioconlogin extends Activity implements OnTouchListener, OnClickListener {
	
	private static final boolean AUTO_HIDE = true;

	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
	
	private static final boolean TOGGLE_ON_CLICK = true;

	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	private SystemUiHider mSystemUiHider;
	
	Button btn3;
	Button btn2;
	Button btn1;
	Button logout;
	Intent i;
	
	final static int cons=0;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from activity_fullscreen.xml
        setContentView(R.layout.inicioconlogin);
        
        init();
        
      //Retrieve current user from Parse.com
        ParseUser currentUser = ParseUser.getCurrentUser();

        //Convert currentUser into String
        String struser = currentUser.getUsername().toString();

        //Locate TextView in welcome.xml
        TextView txtuser = (TextView) findViewById(R.id.txtuser);

        //Set the currentUser String into TextView
        txtuser.setText(struser);

	final View controlsView = findViewById(R.id.fullscreen_content_controls);
	final View contentView = findViewById(R.id.fullscreen_content);

	// Set up an instance of SystemUiHider to control the system UI for
	// this activity.
	mSystemUiHider = SystemUiHider.getInstance(this, contentView,HIDER_FLAGS);
	mSystemUiHider.setup();
	mSystemUiHider.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
				// Cached values.
	int mControlsHeight;
	int mShortAnimTime;

	@Override
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public void onVisibilityChange(boolean visible) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			// If the ViewPropertyAnimator API is available
			// (Honeycomb MR2 and later), use it to animate the
	    	// in-layout UI controls at the bottom of the
		    // screen.
			if (mControlsHeight == 0) {
				mControlsHeight = controlsView.getHeight();
			}
			if (mShortAnimTime == 0) {
				mShortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
			}
			controlsView.animate().translationY(visible ? 0 : mControlsHeight).setDuration(mShortAnimTime);
		} 
		else {
			// If the ViewPropertyAnimator APIs aren't
		    // available, simply show or hide the in-layout UI
			// controls.
			controlsView.setVisibility(visible ? View.VISIBLE: View.GONE);
		}

		if (visible && AUTO_HIDE) {
			// Schedule a hide().
			delayedHide(AUTO_HIDE_DELAY_MILLIS);
		}
	}
	});

	// Set up the user interaction to manually show or hide the system UI.
	contentView.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			if (TOGGLE_ON_CLICK) {
				mSystemUiHider.toggle();
			} else {
				mSystemUiHider.show();
			}
		}
	});

	// Upon interacting with UI controls, delay any scheduled hide()
	// operations to prevent the jarring behavior of controls going away
	// while interacting with the UI.

		//findViewById(R.id.dummy_button).setOnTouchListener(
		//	mDelayHideTouchListener);

}



@Override
protected void onPostCreate(Bundle savedInstanceState) {
super.onPostCreate(savedInstanceState);

// Trigger the initial hide() shortly after the activity has been
// created, to briefly hint to the user that UI controls
// are available.
 
delayedHide(100);


}

/**
* Touch listener to use for in-layout UI controls to delay hiding the
* system UI. This is to prevent the jarring behavior of controls going away
* while interacting with activity UI.
*/
@SuppressLint("ClickableViewAccessibility")
View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
@Override
public boolean onTouch(View view, MotionEvent motionEvent) {
	if (AUTO_HIDE) {
		delayedHide(AUTO_HIDE_DELAY_MILLIS);
	}
	return false;
}
};




Handler mHideHandler = new Handler();
Runnable mHideRunnable = new Runnable() {
@Override
public void run() {
	mSystemUiHider.hide();
}
};
/**
* Schedules a call to hide() in [delay] milliseconds, canceling any
* previously scheduled calls.
*/
private void delayedHide(int delayMillis) {
mHideHandler.removeCallbacks(mHideRunnable);
mHideHandler.postDelayed(mHideRunnable, delayMillis);
}


//Instanciar
public void init(){


btn3 = (Button)findViewById(R.id.button3);
btn3.setOnClickListener(this);
btn2 = (Button)findViewById(R.id.button2);
btn2.setOnClickListener(this);
btn1 = (Button)findViewById(R.id.button1);
btn1.setOnClickListener(this);
logout = (Button) findViewById(R.id.logout);//Locate Button in inicioconlogin.xml
logout.setOnClickListener(this); // Logout Button Click Listener

}

@Override
public void onClick(View v) {
int id;
id= v.getId();
switch (id){
	case R.id.button2: //lanzar formulario
 		i = new  Intent(this,Recetas.class);//Recetas
 		startActivity(i);
 		break;
	case R.id.button3:
		i = new Intent(this,MainActivity.class);
		startActivity(i);
		break;
	case R.id.button1:
		i = new  Intent(this,Ingrediente.class);
 		startActivity(i);
		break;
	case R.id.logout:
		// Logout current user
		ParseUser.logOut();
        finish();
    }
}





public static boolean verificaConexion(Context ctx) {
boolean bConectado = false;
ConnectivityManager connec = (ConnectivityManager) ctx
        .getSystemService(Context.CONNECTIVITY_SERVICE);
// No solo wifi, tambien GPS
NetworkInfo[] redes = connec.getAllNetworkInfo();
// este bucle deberia no ser tan ñapa
for (int i = 0; i < 2; i++) {
    // �Tenemos conexion? ponemos a true
    if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
        bConectado = true;
    }
}
return bConectado;
}



@SuppressLint("ClickableViewAccessibility")
@Override
public boolean onTouch(View v, MotionEvent event) {
// TODO Auto-generated method stub
return false;
}	

}