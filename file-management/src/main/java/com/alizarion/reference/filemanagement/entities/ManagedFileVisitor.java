package com.alizarion.reference.filemanagement.entities;

/**
 * Created by sphinx on 05/09/14.
 * @author selim@openlinux.fr
 */
public interface ManagedFileVisitor {

    public void visit(ImageManagedFile imageManagedFile);

    public void visit(SimpleManagedFile simpleManagedFile);
}
