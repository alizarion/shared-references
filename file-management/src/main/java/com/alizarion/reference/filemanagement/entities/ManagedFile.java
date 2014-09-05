package com.alizarion.reference.filemanagement.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@Table(name = "file_management_managed_file")
public abstract class ManagedFile  implements Serializable{

    @Id
    @TableGenerator(name = "managed_file_SEQ",
            pkColumnName = "SEQ_NAME",
            pkColumnValue = "SEQ_VALUE",
            table = "sequence")
    @GeneratedValue(strategy = GenerationType.TABLE,
            generator = "managed_file_SEQ")
    private Long id;


}
