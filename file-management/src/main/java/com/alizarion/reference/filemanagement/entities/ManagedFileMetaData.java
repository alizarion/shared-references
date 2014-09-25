package com.alizarion.reference.filemanagement.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class ManagedFileMetaData implements Serializable {

    private static final long serialVersionUID = 4425157392550967081L;

    @Id
    @TableGenerator(
            name = "file_management_meta_data_SEQ",
            pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT",
            table = "sequence")
    @GeneratedValue(strategy = GenerationType.TABLE,
            generator = "file_management_meta_data_SEQ")
    private Long id;

    protected ManagedFileMetaData() {
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ManagedFileMetaData)) return false;

        ManagedFileMetaData that = (ManagedFileMetaData) o;

        return !(id != null ? !id.equals(that.id) : that.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
