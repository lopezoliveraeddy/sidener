package electorum.sidener.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * Entidad Coalición
 */
@ApiModel(description = "Entidad Coalición")
@Entity
@Table(name = "coalition")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "coalition")
public class Coalition implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre de la Coalición
     */
    @ApiModelProperty(value = "Nombre de la Coalición")
    @Column(name = "name")
    private String name;

    /**
     * Iniciales
     */
    @ApiModelProperty(value = "Iniciales")
    @Column(name = "acronym")
    private String acronym;

    @Column(name = "published")
    private Boolean published;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "updated_date")
    private ZonedDateTime updatedDate;

    /**
     * Imagen Coalición
     */
    @ApiModelProperty(value = "Imagen Coalición")
    @OneToOne
    @JoinColumn(unique = true)
    private Archive image;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "coalition_political_parties",
               joinColumns = @JoinColumn(name="coalitions_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="political_parties_id", referencedColumnName="id"))
    private Set<PoliticalParty> politicalParties = new HashSet<>();

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

    public Coalition name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAcronym() {
        return acronym;
    }

    public Coalition acronym(String acronym) {
        this.acronym = acronym;
        return this;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public Boolean isPublished() {
        return published;
    }

    public Coalition published(Boolean published) {
        this.published = published;
        return this;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public Coalition createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getUpdatedDate() {
        return updatedDate;
    }

    public Coalition updatedDate(ZonedDateTime updatedDate) {
        this.updatedDate = updatedDate;
        return this;
    }

    public void setUpdatedDate(ZonedDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Archive getImage() {
        return image;
    }

    public Coalition image(Archive archive) {
        this.image = archive;
        return this;
    }

    public void setImage(Archive archive) {
        this.image = archive;
    }

    public Set<PoliticalParty> getPoliticalParties() {
        return politicalParties;
    }

    public Coalition politicalParties(Set<PoliticalParty> politicalParties) {
        this.politicalParties = politicalParties;
        return this;
    }

    public Coalition addPoliticalParties(PoliticalParty politicalParty) {
        this.politicalParties.add(politicalParty);
        return this;
    }

    public Coalition removePoliticalParties(PoliticalParty politicalParty) {
        this.politicalParties.remove(politicalParty);
        return this;
    }

    public void setPoliticalParties(Set<PoliticalParty> politicalParties) {
        this.politicalParties = politicalParties;
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
        Coalition coalition = (Coalition) o;
        if (coalition.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), coalition.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Coalition{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", acronym='" + getAcronym() + "'" +
            ", published='" + isPublished() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", updatedDate='" + getUpdatedDate() + "'" +
            "}";
    }
}
