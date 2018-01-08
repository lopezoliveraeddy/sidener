package electorum.sidener.domain;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import electorum.sidener.domain.enumeration.ArchiveStatus;

/**
 * A Archive.
 */
@Entity
@Table(name = "archive")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "archive")
public class Archive implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre del Archivo
     */
    @ApiModelProperty(value = "Nombre del Archivo")
    @Column(name = "name")
    private String name;

    /**
     * Descripción del Archivo
     */
    @ApiModelProperty(value = "Descripción del Archivo")
    @Column(name = "description")
    private String description;

    /**
     * Tipo MIME del Archivo
     */
    @ApiModelProperty(value = "Tipo MIME del Archivo")
    @Column(name = "mime_type")
    private String mimeType;

    /**
     * Tamaño en Bytes del Archivo
     */
    @ApiModelProperty(value = "Tamaño en Bytes del Archivo")
    @Column(name = "size_length")
    private Long sizeLength;

    /**
     * Ruta donde fue almacenado el Archivo
     */
    @ApiModelProperty(value = "Ruta donde fue almacenado el Archivo")
    @Column(name = "path")
    private String path;

    /**
     * Estado del Archivo Almacenado
     */
    @ApiModelProperty(value = "Estado del Archivo Almacenado")
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ArchiveStatus status;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Archive name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Archive description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMimeType() {
        return mimeType;
    }

    public Archive mimeType(String mimeType) {
        this.mimeType = mimeType;
        return this;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Long getSizeLength() {
        return sizeLength;
    }

    public Archive sizeLength(Long sizeLength) {
        this.sizeLength = sizeLength;
        return this;
    }

    public void setSizeLength(Long sizeLength) {
        this.sizeLength = sizeLength;
    }

    public String getPath() {
        return path;
    }

    public Archive path(String path) {
        this.path = path;
        return this;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ArchiveStatus getStatus() {
        return status;
    }

    public Archive status(ArchiveStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(ArchiveStatus status) {
        this.status = status;
    }
    // jhipster-needle-entity-add-getters-setters - Jhipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Archive archive = (Archive) o;
        if (archive.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), archive.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Archive{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", mimeType='" + getMimeType() + "'" +
            ", sizeLength='" + getSizeLength() + "'" +
            ", path='" + getPath() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
