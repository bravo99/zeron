package cl.zeron.vegetaapp;

import java.util.ArrayList;

import com.parse.Parse;

import android.app.Application;

public class GlobalClass extends Application {
	private ArrayList<ProductoPrecio> lista;
	
	@Override
	public void onCreate() {
		Parse.initialize(this, "u9VdDovJEB22hYLgjjGm6hjaKH08dzuwhPq4dWGc", "gtDf7cQQEoGJrp89xgZfazpgYfnAyq2OJFBBuumK");
		//ParseFacebookUtils.initialize("729168253804672");
		
		super.onCreate();
	}
	
	public GlobalClass(){
		this.lista=null;
	}
	
	public ArrayList<ProductoPrecio> getLista(){
		return this.lista;
	}
	public void setLista(ArrayList<ProductoPrecio> value){
		this.lista = value;
	}

}
