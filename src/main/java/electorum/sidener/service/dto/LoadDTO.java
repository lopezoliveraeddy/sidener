package electorum.sidener.service.dto;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

public class LoadDTO  implements Serializable{

	private Long id;
    
	@Lob
    private byte[] dbFile;
    
	private String dbFileContentType;
    
	private Long eleccion;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public byte[] getDbFile() {
		return dbFile;
	}

	public void setDbFile(byte[] dbFile) {
		this.dbFile = dbFile;
	}

	public String getDbFileContentType() {
		return dbFileContentType;
	}

	public void setDbFileContentType(String dbFileContentType) {
		this.dbFileContentType = dbFileContentType;
	}

	public Long getEleccion() {
		return eleccion;
	}

	public void setEleccion(Long eleccion) {
		this.eleccion = eleccion;
	}
    
    

}
