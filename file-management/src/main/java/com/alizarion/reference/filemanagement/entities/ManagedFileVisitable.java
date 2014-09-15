package com.alizarion.reference.filemanagement.entities;

import com.alizarion.reference.exception.ApplicationException;

/**
 * Defines an interface for classes that may be interpreted by a Visitor.
 * @author selim@openlinux.fr
 */
public interface ManagedFileVisitable   {

    public <ReturnType, T extends ApplicationException>
    ReturnType accept(ManagedFileVisitor<ReturnType, T > managedFileVisitor) throws T;

}
