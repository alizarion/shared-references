package com.alizarion.reference.filemanagement.entities;

import com.alizarion.reference.exception.ApplicationException;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Class files without specific managed description.
 * @author selim@openlinux.fr.
 */
@Entity
@DiscriminatorValue(value = SimpleManagedFile.TYPE)
public class SimpleManagedFile extends ManagedFile<GenericFileMetaData> implements ManagedFileVisitable  {

    public final static String TYPE =  "simple";

    private static final long serialVersionUID = 2070467676114307695L;


    @Override
    public String getType() {
        return TYPE;
    }


    @Override
    public  <ReturnType,ExceptionType extends ApplicationException>
    ReturnType accept(ManagedFileVisitor<ReturnType,ExceptionType> managedFileVisitor)
            throws ExceptionType {
        return managedFileVisitor.visit(this);
    }
}
