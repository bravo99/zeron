package com.example.vegeta;

import com.example.vegeta.util.SystemUiHider;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.Toast;
import com.parse.Parse;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseUser;


public class FullscreenActivity extends Activity implements OnTouchListener, OnClickListener {
	
	private static final boolean AUTO_HIDE = true;

	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
	
	private static final boolean TOGGLE_ON_CLICK = true;

	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	private SystemUiHider mSystemUiHider;
	
	Button btn3;
	Button btn2;
	Button btn1;
	Button login;
	Intent i;
	final static int cons=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		
		super.onCreate(savedInstanceState);
		
		if (!verificaConexion(this)) {
			 i = new  Intent(this,Error.class);
		 		startActivity(i);
		    Toast.makeText(getBaseContext(),
		            "Comprueba tu conexión a Internet. ", Toast.LENGTH_SHORT)
		            .show();
		   
		    
		    
		}
		
			Parse.initialize(this, "dWlZ6dRpL68VNQb1FXpkbtOHvDubsywsRWReOgP4", "RdHO2zjqPOhjlEI4Ywp8l1oX4vZq1cPvxzN7ohZi");
	       	setContentView(R.layout.iniciosinlogin);
	        init();
	       
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
		
			//	findViewById(R.id.dummy_button).setOnTouchListener(
			//		mDelayHideTouchListener);
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

	
	//Instaciar
	public void init(){
		btn3 = (Button)findViewById(R.id.button3);
		btn3.setOnClickListener(this);
		btn2 = (Button)findViewById(R.id.button2);
		btn2.setOnClickListener(this);
		btn1 = (Button)findViewById(R.id.button1);
		btn1.setOnClickListener(this);
		login = (Button)findViewById(R.id.login);
		login.setOnClickListener(this);
			
	}
	
	@Override
	public void onClick(View v) {
		
		Bundle bundle = new Bundle();
		bundle.putBoolean("ESTADO", false);
		
		int id;
		id= v.getId();
		switch (id){
		
		 	case R.id.button2: //lanzar formulario
		 		i = new  Intent(this,Recetas.class);//Recetas
		 		i.putExtras(bundle);
		 		startActivity(i);
		 		//no puede agregar receta;
		 		break;
			case R.id.button3:
				//no se pueden agregar lugares
				i = new Intent(this,MainActivity.class);
				i.putExtras(bundle);
				startActivity(i);
				break;
			case R.id.button1:
				//no se pueden administrar ingredientes
				i = new  Intent(this,Ingrediente.class);
				i.putExtras(bundle);
		 		startActivity(i);
				break;
			case R.id.login:
				i = new Intent(this,LoginSignUpActivity.class);
				startActivity(i);
				break;
		}
		
	}

	

	public static boolean verificaConexion(Context ctx) {
	    boolean bConectado = false;
	    ConnectivityManager connec = (ConnectivityManager) ctx
	            .getSystemService(Context.CONNECTIVITY_SERVICE);
	    // No sólo wifi, también GPRS
	    NetworkInfo[] redes = connec.getAllNetworkInfo();
	    // este bucle debería no ser tan ñapa
	    for (int i = 0; i < 2; i++) {
	        // ¿Tenemos conexión? ponemos a true
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
	
    
	public void login(){
		//ParseApplication papp = new ParseApplication();
		 // Determine whether the current user is an anonymous user
      if (ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
          // If user is anonymous, send the user to LoginSignupActivity.class
          Intent intent = new Intent(this,
                  LoginSignUpActivity.class);
          startActivity(intent);
          finish();
      } else {
           //If current user is NOT anonymous user
          // Get current user data from Parse.com
          ParseUser currentUser = ParseUser.getCurrentUser();
          if (currentUser != null) {
              // Send logged in users to Welcome.class
            Intent intent = new Intent(FullscreenActivity.this, Inicioconlogin.class);
            
              startActivity(intent);
             finish();
          } else {
              // Send user to LoginSignupActivity.class
              Intent intent = new Intent(FullscreenActivity.this,
                      LoginSignUpActivity.class);
              startActivity(intent);
              finish();
          }
      }
	
	}

	
	
}

