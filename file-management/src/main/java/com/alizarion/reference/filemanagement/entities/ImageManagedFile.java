package com.alizarion.reference.filemanagement.entities;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@DiscriminatorValue(value = ImageManagedFile.TYPE)
public class ImageManagedFile extends ManagedFile implements ManagedFileVisitable {

    public final static String TYPE = "IMAGE";

    @Column(name = "managed_image_height")
    private Integer height;

    @Column(name = "managed_image_width")
    private Integer width;

    @Column(name = "managed_image_color_space",length = 5)
    private Integer colorSpace;

    @Column(name = "managed_image_format")
    private String format;



    @Override
    public void accept(ManagedFileVisitor managedFileVisitor) {
        managedFileVisitor.visit(this);
    }

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
    public String getType() {
        return TYPE;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof ImageManagedFile)) return false;
        if (!super.equals(o)) return false;

        ImageManagedFile that = (ImageManagedFile) o;

        if (colorSpace != null ? !colorSpace.equals(that.colorSpace) :
                that.colorSpace != null) return false;
        if (height != null ? !height.equals(that.height) :
                that.height != null) return false;
        if (width != null ? !width.equals(that.width) :
                that.width != null) return false;

        return true;
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
}
