package electorum.sidener.service.dto;


import java.io.Serializable;
import java.util.Objects;
import electorum.sidener.domain.enumeration.ArchiveStatus;

/**
 * A DTO for the Archive entity.
 */
public class ArchiveDTO implements Serializable {

    private Long id;

    private String name;

    private String description;

    private String mimeType;

    private Long sizeLength;

    private String path;

    private ArchiveStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Long getSizeLength() {
        return sizeLength;
    }

    public void setSizeLength(Long sizeLength) {
        this.sizeLength = sizeLength;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ArchiveStatus getStatus() {
        return status;
    }

    public void setStatus(ArchiveStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ArchiveDTO archiveDTO = (ArchiveDTO) o;
        if(archiveDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), archiveDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ArchiveDTO{" +
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
