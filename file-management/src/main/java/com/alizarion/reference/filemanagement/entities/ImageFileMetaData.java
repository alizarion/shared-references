package com.alizarion.reference.filemanagement.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@Table(name = "file_management_image_file_meta_data")
public class ImageFileMetaData extends ManagedFileMetaData {

    private static final long serialVersionUID = -3408106740689115740L;


    protected ImageFileMetaData() {
    }

    protected ImageFileMetaData(ManagedFile managedFile) {
        this.setManagedFile(managedFile);
        }


    public ImageFileMetaData(final Integer width,
                             final Integer height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Image height.
     */
    @Column(name = "managed_image_height",nullable = false)
    private Integer height;

    /**
     * Image Width.
     */
    @Column(name = "managed_image_width",nullable = false)
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

    public Integer getColorSpace() {
        return colorSpace;
    }

    public void setColorSpace(final Integer colorSpace) {
        this.colorSpace = colorSpace;
    }

    public String getFormat() {
            return format;
        }

    public String getMimeType(){
        if (this.format != null){
            return "image/"+this.format.toLowerCase();
        }  else {
            return null;
        }
    }

    public void setFormat(final String format) {
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ImageFileMetaData)) return false;
        if (!super.equals(o)) return false;

        ImageFileMetaData that = (ImageFileMetaData) o;

        return !(colorSpace != null ?
                !colorSpace.equals(that.colorSpace) :
                that.colorSpace != null) &&
                !(format != null ? !format.equals(that.format) :
                        that.format != null) &&
                !(height != null ? !height.equals(that.height) :
                        that.height != null) &&
                !(width != null ? !width.equals(that.width) :
                        that.width != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (height != null ? height.hashCode() : 0);
        result = 31 * result + (width != null ? width.hashCode() : 0);
        result = 31 * result + (colorSpace != null ? colorSpace.hashCode() : 0);
        result = 31 * result + (format != null ? format.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ImageFileMetaData{" +
                "height=" + height +
                ", width=" + width +
                ", colorSpace=" + colorSpace +
                ", format='" + format + '\'' +
                '}';
    }
}
