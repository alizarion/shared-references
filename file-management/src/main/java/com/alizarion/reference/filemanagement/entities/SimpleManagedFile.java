package com.alizarion.reference.filemanagement.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@DiscriminatorValue(value = SimpleManagedFile.TYPE)
public class SimpleManagedFile extends ManagedFile {

    public final static String TYPE =  "simple";


    @Override
    public String getType() {
        return TYPE;
    }
}
