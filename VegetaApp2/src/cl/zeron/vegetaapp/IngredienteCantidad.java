package cl.zeron.vegetaapp;

public class IngredienteCantidad {
	protected String nombre;
	protected String cantidad;
	protected String medida;
	protected String idObject;
	
	public IngredienteCantidad(){
		this.nombre="";
		this.cantidad="";
		this.medida="";
		this.idObject="";
	}
	
	public String getNombre(){
		return this.nombre;
	}
	public void setNombre(String value){
		this.nombre = value;
	}
	public String getCantidad(){
		return this.cantidad;
	}
	public void setCantidad(String value){
		this.cantidad=value;
	}
	public String getId(){
		return this.idObject;
	}
	public void setId(String value){
		this.idObject = value;
	}
	public String getMedida(){
		return this.medida;
	}
	public void setMedida(String value){
		this.medida=value;
	}
	
}
