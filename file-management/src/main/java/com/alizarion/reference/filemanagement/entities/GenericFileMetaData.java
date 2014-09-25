package com.alizarion.reference.filemanagement.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * @author selim@openlinux.fr.
 */
@Entity
@Table(name = "file_management_generic_meta_data")
public class GenericFileMetaData implements Serializable {

    private static final long serialVersionUID = 7350501425963771749L;

    @Id
    @TableGenerator(
            name ="file_management_generic_meta_data_SEQ",
            table = "sequence",
            pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT")
    @GeneratedValue(generator = "file_management_generic_meta_data_SEQ",
            strategy = GenerationType.TABLE)
    private Long id;

    @ManyToOne
    private ManagedFile managedFile;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "value", nullable = false)
    private String value;

    protected GenericFileMetaData() {
    }

    public GenericFileMetaData(final ManagedFile managedFile,
                               final String name,
                               final String value) {
        this.managedFile = managedFile;
        this.name = name;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof GenericFileMetaData)) return false;

        GenericFileMetaData that = (GenericFileMetaData) o;

        return !(id != null ?
                !id.equals(that.id) :
                that.id != null) &&
                !(managedFile != null ?
                        !managedFile.equals(that.managedFile) :
                        that.managedFile != null) &&
                !(name != null ? !name.equals(that.name) :
                        that.name != null) &&
                !(value != null ? !value.equals(that.value) :
                        that.value != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (managedFile != null ? managedFile.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
