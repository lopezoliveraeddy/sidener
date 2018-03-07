package electorum.sidener.domain;

import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import electorum.sidener.domain.enumeration.TypeCausal;

import electorum.sidener.domain.enumeration.SubTypeCausal;

/**
 * Entidad Causales
 */
@ApiModel(description = "Entidad Causales")
@Entity
@Table(name = "causal")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "causal")
public class Causal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_causal")
    private TypeCausal typeCausal;

    @Enumerated(EnumType.STRING)
    @Column(name = "sub_type_causal")
    private SubTypeCausal subTypeCausal;

    @Column(name = "color")
    private String color;

    @Column(name = "published")
    private Boolean published;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "updated_date")
    private ZonedDateTime updatedDate;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "causal_causal_description",
               joinColumns = @JoinColumn(name="causals_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="causal_descriptions_id", referencedColumnName="id"))
    private Set<CausalDescription> causalDescriptions = new HashSet<>();

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

    public Causal name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TypeCausal getTypeCausal() {
        return typeCausal;
    }

    public Causal typeCausal(TypeCausal typeCausal) {
        this.typeCausal = typeCausal;
        return this;
    }

    public void setTypeCausal(TypeCausal typeCausal) {
        this.typeCausal = typeCausal;
    }

    public SubTypeCausal getSubTypeCausal() {
        return subTypeCausal;
    }

    public Causal subTypeCausal(SubTypeCausal subTypeCausal) {
        this.subTypeCausal = subTypeCausal;
        return this;
    }

    public void setSubTypeCausal(SubTypeCausal subTypeCausal) {
        this.subTypeCausal = subTypeCausal;
    }

    public String getColor() {
        return color;
    }

    public Causal color(String color) {
        this.color = color;
        return this;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Boolean isPublished() {
        return published;
    }

    public Causal published(Boolean published) {
        this.published = published;
        return this;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public Causal createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getUpdatedDate() {
        return updatedDate;
    }

    public Causal updatedDate(ZonedDateTime updatedDate) {
        this.updatedDate = updatedDate;
        return this;
    }

    public void setUpdatedDate(ZonedDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Set<CausalDescription> getCausalDescriptions() {
        return causalDescriptions;
    }

    public Causal causalDescriptions(Set<CausalDescription> causalDescriptions) {
        this.causalDescriptions = causalDescriptions;
        return this;
    }

    public Causal addCausalDescription(CausalDescription causalDescription) {
        this.causalDescriptions.add(causalDescription);
        return this;
    }

    public Causal removeCausalDescription(CausalDescription causalDescription) {
        this.causalDescriptions.remove(causalDescription);
        return this;
    }

    public void setCausalDescriptions(Set<CausalDescription> causalDescriptions) {
        this.causalDescriptions = causalDescriptions;
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
        Causal causal = (Causal) o;
        if (causal.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), causal.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Causal{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", typeCausal='" + getTypeCausal() + "'" +
            ", subTypeCausal='" + getSubTypeCausal() + "'" +
            ", color='" + getColor() + "'" +
            ", published='" + isPublished() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", updatedDate='" + getUpdatedDate() + "'" +
            "}";
    }
}
