package electorum.sidener.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import electorum.sidener.domain.enumeration.ElectionAmbit;

/**
 * Entidad Tipo de Elección (Gobernador, Diputado)
 */
@ApiModel(description = "Entidad Tipo de Elección (Gobernador, Diputado)")
@Entity
@Table(name = "election_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "electiontype")
public class ElectionType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre del Tipo de Elección
     */
    @ApiModelProperty(value = "Nombre del Tipo de Elección")
    @Column(name = "name")
    private String name;

    /**
     * Ámbito de la Elección
     */
    @ApiModelProperty(value = "Ámbito de la Elección")
    @Enumerated(EnumType.STRING)
    @Column(name = "election_ambit")
    private ElectionAmbit electionAmbit;

    @Column(name = "published")
    private Boolean published;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "updated_date")
    private ZonedDateTime updatedDate;

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

    public ElectionType name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ElectionAmbit getElectionAmbit() {
        return electionAmbit;
    }

    public ElectionType electionAmbit(ElectionAmbit electionAmbit) {
        this.electionAmbit = electionAmbit;
        return this;
    }

    public void setElectionAmbit(ElectionAmbit electionAmbit) {
        this.electionAmbit = electionAmbit;
    }

    public Boolean isPublished() {
        return published;
    }

    public ElectionType published(Boolean published) {
        this.published = published;
        return this;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public ElectionType createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getUpdatedDate() {
        return updatedDate;
    }

    public ElectionType updatedDate(ZonedDateTime updatedDate) {
        this.updatedDate = updatedDate;
        return this;
    }

    public void setUpdatedDate(ZonedDateTime updatedDate) {
        this.updatedDate = updatedDate;
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
        ElectionType electionType = (ElectionType) o;
        if (electionType.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), electionType.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ElectionType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", electionAmbit='" + getElectionAmbit() + "'" +
            ", published='" + isPublished() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", updatedDate='" + getUpdatedDate() + "'" +
            "}";
    }
}
