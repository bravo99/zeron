package cl.zeron.vegetaapp;

import java.util.Timer;
import java.util.TimerTask;

import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class InitActivity extends Activity {
	private static final int LOGIN_REQUEST = 0;
	private static final long SPLASH_SCREEN_DELAY = 3000;
	private ParseUser currentUser;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_init);
		currentUser = ParseUser.getCurrentUser();
		TimerTask task = new TimerTask() {
            @Override
            public void run() {
            	if(currentUser==null){
            		ParseLoginBuilder loginBuilder = new ParseLoginBuilder(InitActivity.this);
      	          	startActivityForResult(loginBuilder.build(), LOGIN_REQUEST);
            	}
            	else{
            		Intent intent = new Intent(InitActivity.this, MainActivity.class);
            		startActivity(intent);
            		finish();
            	}
            	
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);
		
	}
	public void btnLogin(View view){
		ParseLoginBuilder loginBuilder = new ParseLoginBuilder(InitActivity.this);
	          startActivityForResult(loginBuilder.build(), LOGIN_REQUEST);
	}
	
	public void btnOmitir(View view){
		Intent intent = new Intent(InitActivity.this, MainActivity.class);
		startActivity(intent);
	}
	
	public void btnLogout(View view){
		ParseUser.logOut();
		Toast.makeText(InitActivity.this, "LogOut", Toast.LENGTH_SHORT).show();
		return;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == LOGIN_REQUEST){
			switch(resultCode){
				case Activity.RESULT_OK:
					ParseUser currentUser = ParseUser.getCurrentUser();
		        	if(currentUser != null){
		        		Toast.makeText(InitActivity.this, "Registrado como: " + currentUser.getString("name"), Toast.LENGTH_SHORT).show();
		        	}
		        	else{
		        		Toast.makeText(InitActivity.this, "No está registrado", Toast.LENGTH_SHORT).show();
		        	}
					Intent intent = new Intent(InitActivity.this, MainActivity.class);
					startActivity(intent);
					finish();
					break;
				case Activity.RESULT_CANCELED:
					Toast.makeText(InitActivity.this, "Cancelaste el Login", Toast.LENGTH_SHORT).show();
					break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
