package cl.zeron.vegetaapp;

import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
 
public class FragmentEmpty extends Fragment {
 
    public final static String KEY_TEXT = "key_text";
    private static final int LOGIN_REQUEST = 0;
 
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	if(ParseUser.getCurrentUser() == null){
    		ParseLoginBuilder loginBuilder = new ParseLoginBuilder(getActivity());
	          	startActivityForResult(loginBuilder.build(), LOGIN_REQUEST);
    	}
    	else{
    		ParseUser.logOut();
            Toast.makeText(getActivity(), "LogOut", Toast.LENGTH_SHORT).show();
            ParseLoginBuilder loginBuilder = new ParseLoginBuilder(getActivity());
          	startActivityForResult(loginBuilder.build(), LOGIN_REQUEST);
    	}
    	
 
        return null;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(requestCode == LOGIN_REQUEST){
			switch(resultCode){
				case Activity.RESULT_OK:
					ParseUser currentUser = ParseUser.getCurrentUser();
		        	if(currentUser != null){
		        		Toast.makeText(getActivity(), "Registrado como: " + currentUser.getString("name"), Toast.LENGTH_SHORT).show();
		        	}
		        	else{
		        		Toast.makeText(getActivity(), "No está registrado", Toast.LENGTH_SHORT).show();
		        	}
					Intent intent = new Intent(getActivity(), MainActivity.class);
					startActivity(intent);
					break;
				case Activity.RESULT_CANCELED:
					Toast.makeText(getActivity(), "Cancelaste el Login", Toast.LENGTH_SHORT).show();
					break;
			}
		}
    	super.onActivityResult(requestCode, resultCode, data);
    }
}
