package com.alizarion.reference.resource.entities;

import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author selim@openlinux.fr.
 */

@Entity
@Table(name = "persistent_resources")
@NamedQueries({
        @NamedQuery(name = PersistentResource.GET_PERSISTENT_RESOURCE_VALUE_BY_KEY,
                query = "select pr from PersistentResource pr  where pr.category = :category "
                        + " and pr.key = :key")})
@Cacheable(true)
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class PersistentResource implements Serializable {

    public final static String GET_PERSISTENT_RESOURCE_VALUE_BY_KEY =
            "GET_PERSISTENT_RESOURCE_VALUE_BY_KEY";

    private static final long serialVersionUID = 4477468895307761998L;

    @Id
    @TableGenerator(name="persistent_resources_SEQ",
            table="sequence",
            pkColumnName="SEQ_NAME", valueColumnName="SEQ_COUNT")
    @GeneratedValue(strategy=GenerationType.TABLE,
            generator="persistent_resources_SEQ")
    @Column
    private Long id;

    @Column(name = "category",unique = false,length = 128)
    private String category;

    @Column(name = "unique_key",length = 50,unique = false)
    private String key;

    @Column(name = "value",length = 255,unique = false)
    private String value;


    @Column(name = "last_update")
    private Date lastUpdate;

    public PersistentResource() {
        this.lastUpdate = new Date();
    }

    public PersistentResource(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PersistentResource)) return false;

        PersistentResource that = (PersistentResource) o;

        if (category != null ? !category.equals(that.category)
                : that.category != null) return false;
        if (id != null ? !id.equals(that.id) :
                that.id != null) return false;
        if (key != null ? !key.equals(that.key) :
                that.key != null) return false;
        if (value != null ? !value.equals(that.value) :
                that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (key != null ? key.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
