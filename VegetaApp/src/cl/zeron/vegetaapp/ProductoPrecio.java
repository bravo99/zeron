package cl.zeron.vegetaapp;

public class ProductoPrecio {
	protected String nombre;
	protected int precio;
	protected String idObject;
	
	public ProductoPrecio(){
		this.nombre="";
		this.precio=0;
		this.idObject="";
	}
	public ProductoPrecio(String nombre, int precio){
		this.nombre = nombre;
		this.precio = precio;
	}
	public String getNombre(){
		return this.nombre;
	}
	public void setNombre(String value){
		this.nombre = value;
	}
	public int getPrecio(){
		return this.precio;
	}
	public void setPrecio(int value){
		this.precio=value;
	}
	public String getId(){
		return this.idObject;
	}
	public void setId(String value){
		this.idObject = value;
	}
	
}
