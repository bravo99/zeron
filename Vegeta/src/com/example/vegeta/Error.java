package com.example.vegeta;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;

public class Error extends Activity {
	int valor = -1;
	public static final String DEBUG_TAG = "GesturesActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.error_conexion);
				       
	}
	
	@Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
 
        int action = MotionEventCompat.getActionMasked(event);
 
        switch (action) {
        case (MotionEvent.ACTION_DOWN):
        	Intent i = new Intent(this, FullscreenActivity.class);
        	startActivity(i);
        	this.finish();
            Log.d(DEBUG_TAG, "La accion ha sido ABAJO");
            return true;
        case (MotionEvent.ACTION_MOVE):
            Log.d(DEBUG_TAG, "La acción ha sido MOVER");
            return true;
        case (MotionEvent.ACTION_UP):
            Log.d(DEBUG_TAG, "La acción ha sido ARRIBA");
            return true;
        case (MotionEvent.ACTION_CANCEL):
            Log.d(DEBUG_TAG, "La accion ha sido CANCEL");
            return true;
        case (MotionEvent.ACTION_OUTSIDE):
            Log.d(DEBUG_TAG,
                    "La accion ha sido fuera del elemento de la pantalla");
            return true;
        default:
            return super.onTouchEvent(event);
        }
    }
	
	
	@Override
	public void onBackPressed() {
	}

	
	
	
	}
