package com.alizarion.reference.filemanagement.entities;

import com.alizarion.reference.exception.ApplicationException;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Describe image files that are managed by the app.
 * @author selim@openlinux.fr.
 */
@Entity
@DiscriminatorValue(value = ImageManagedFile.TYPE)
public class ImageManagedFile extends ManagedFile implements ManagedFileVisitable {

    private static final long serialVersionUID = -7416614602874082506L;

    public final static String TYPE = "IMAGE";

    /**
     * Image height.
     */
    @Column(name = "managed_image_height")
    private Integer height;

    /**
     * Image Width.
     */
    @Column(name = "managed_image_width")
    private Integer width;

    /**
     * Image color spaces
     */
    @Column(name = "managed_image_color_space",length = 5)
    private Integer colorSpace;

    /**
     * Source image Format
     */
    @Column(name = "managed_image_format")
    private String format;

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getColorSpace() {
        return colorSpace;
    }

    public void setColorSpace(Integer colorSpace) {
        this.colorSpace = colorSpace;
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

        return !(colorSpace != null ? !colorSpace.equals(that.colorSpace) :
                that.colorSpace != null) &&
                !(height != null ? !height.equals(that.height) :
                        that.height != null) &&
                !(width != null ? !width.equals(that.width) :
                        that.width != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (height != null ?
                height.hashCode() : 0);
        result = 31 * result + (width != null ?
                width.hashCode() : 0);
        result = 31 * result + (colorSpace != null ?
                colorSpace.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {

        return super.toString() +
                "ImageManagedFile{" +
                "height=" + height +
                ", width=" + width +
                ", colorSpace=" + colorSpace +
                ", format='" + format + '\'' +
                '}'+ '\''+
                '}';
    }
}
