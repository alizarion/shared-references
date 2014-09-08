package com.alizarion.reference.filemanagement.entities;

/**
 * Created by sphinx on 05/09/14.
 */
public interface ManagedFileVisitable {

    public void accept(ManagedFileVisitor managedFileVisitor);

}
