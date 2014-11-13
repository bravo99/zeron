package cl.zeron.vegetaapp;

import java.util.ArrayList;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseTwitterUtils;

import android.app.Application;

public class GlobalClass extends Application {
	private ArrayList<ProductoPrecio> lista;
	private ArrayList<IngredienteCantidad> lista_ingrediente;
	
	@Override
	public void onCreate() {
		Parse.initialize(this, "u9VdDovJEB22hYLgjjGm6hjaKH08dzuwhPq4dWGc", "gtDf7cQQEoGJrp89xgZfazpgYfnAyq2OJFBBuumK");
		ParseFacebookUtils.initialize("729168253804672");
		ParseTwitterUtils.initialize("9mtqJaVo5w70cU9CJ6p8ZGfRv", "AedvvxDkyt7Gl0HfrVTrZiGNbQScGUhqD2aPscN0gJGDI0aoWw");
		Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
		
		super.onCreate();
	}
	
	public GlobalClass(){
		this.lista=null;
		this.lista_ingrediente =null;
	}
	
	public ArrayList<ProductoPrecio> getLista(){
		return this.lista;
	}
	public void setLista(ArrayList<ProductoPrecio> value){
		this.lista = value;
	}
	public ArrayList<IngredienteCantidad> getListaIngrediente(){
		return this.lista_ingrediente;
	}
	public void setListaIngrediente(ArrayList<IngredienteCantidad> value){
		this.lista_ingrediente = value;
	}

}
