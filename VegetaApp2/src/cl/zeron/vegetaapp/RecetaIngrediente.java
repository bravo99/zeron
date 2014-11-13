package cl.zeron.vegetaapp;

public class RecetaIngrediente {
	protected String nombre;
	protected String valoracion;
	protected String idObject;
	protected String personas;
	protected String tiempo;
	protected String imagen;
	
	public RecetaIngrediente(){
		this.nombre="";
		this.idObject="";
		this.valoracion = "";
		this.personas = "";
		this.tiempo ="";
		this.imagen = "";
	}
	
	public String getNombre(){
		return this.nombre;
	}
	public void setNombre(String value){
		this.nombre = value;
	}
	public String getId(){
		return this.idObject;
	}
	public void setId(String value){
		this.idObject = value;
	}
	public String getValoracion(){
		return this.valoracion;
	}
	public void setValoracion(String value){
		this.valoracion = value;
	}
	public String getPersonas(){
		return this.personas;
	}
	public void setPersonas(String value){
		this.personas = value;
	}
	public String getTiempo(){
		return this.tiempo;
	}
	public void setTiempo(String value){
		this.tiempo = value;
	}
	public String getImagen(){
		return this.imagen;
	}
	public void setImagen(String value){
		this.imagen = value;
	}
}
