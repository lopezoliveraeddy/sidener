package electorum.sidener.service.dto;

public class WinnersDTO {
	private String partido;
	private Long cantidad;
	public String getPartido() {
		return partido;
	}
	public void setPartido(String partido) {
		this.partido = partido;
	}
	public Long getCantidad() {
		return cantidad;
	}
	public void setCantidad(Long cantidad) {
		this.cantidad = cantidad;
	}
	
	@Override
	public String toString() {
		return "winners [partido=" + partido + ", cantidad=" + cantidad + "]";
	}
	
	
	

}
