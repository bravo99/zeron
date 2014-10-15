package com.vegeta.vegetaappmaps;

public class ItemObject{
	
	private String titulo;
	private int icon;
	
	public ItemObject(String tit, int ic){
		this.titulo=tit;
		this.icon=ic;
	}
		
	public String getTitulo(){
		return titulo;
	}
	public void setTitulo(String titulo){
		this.titulo=titulo;
	}
	public int getIcono(){
		return icon;
	}
	public void setIcon(int icono ){
		this.icon=icono;
	}
}
