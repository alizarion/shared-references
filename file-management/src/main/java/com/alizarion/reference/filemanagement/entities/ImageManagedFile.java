package com.alizarion.reference.filemanagement.entities;

import com.alizarion.reference.exception.ApplicationException;

import javax.persistence.*;

/**
 * Describe image files that are managed by the app.
 * @author selim@openlinux.fr.
 */
@Entity
@DiscriminatorValue(value = ImageManagedFile.TYPE)
public class ImageManagedFile extends ManagedFile implements ManagedFileVisitable {

    private static final long serialVersionUID = -7416614602874082506L;

    public final static String TYPE = "IMAGE";




    public ImageManagedFile() {
        super();
        super.setMetaData(new ImageFileMetaData());
    }


    public String getFormat() {
        return getImageMetaData().getFormat();
    }


    public ImageFileMetaData getImageMetaData() {
        return (ImageFileMetaData) getMetaData();
    }

    public void setImageMetaData(final ImageFileMetaData imageMetaData) {
        this.setMetaData(imageMetaData);
    }

    @Override
    public  <ReturnType,T extends ApplicationException>
    ReturnType accept(ManagedFileVisitor<ReturnType,T> managedFileVisitor)
            throws T {
        return managedFileVisitor.visit(this);
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ImageManagedFile)) return false;
        if (!super.equals(o)) return false;

        ImageManagedFile that = (ImageManagedFile) o;

        if (getImageMetaData() != null ?
                !getImageMetaData().equals(that.getImageMetaData()) :
                that.getImageMetaData() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getImageMetaData() != null
                ? getImageMetaData().hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        return "ImageManagedFile{" +
                "imageMetaData=" + getImageMetaData().toString() +
                '}';
    }
}
