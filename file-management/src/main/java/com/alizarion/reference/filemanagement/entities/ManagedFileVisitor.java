package com.alizarion.reference.filemanagement.entities;

import com.alizarion.reference.exception.ApplicationException;

/**
 * This interface contains the methods a visitor of the AST must implement.
 * @author selim@openlinux.fr
 */
public interface ManagedFileVisitor <ReturnType, T extends  ApplicationException> {

    public ReturnType visit(ImageManagedFile imageManagedFile) throws T;

    public ReturnType visit(SimpleManagedFile simpleManagedFile) throws T;
}
