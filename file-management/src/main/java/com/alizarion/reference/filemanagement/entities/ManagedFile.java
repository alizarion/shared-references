package com.alizarion.reference.filemanagement.entities;

import org.apache.commons.io.FilenameUtils;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * All Files that are managed by the application, images, videos, resources, .
 * @author selim@openlinux.fr.
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@Table(name = "file_management_managed_file")
public abstract class ManagedFile  implements Serializable {

    private static final long serialVersionUID = -5029119787687345370L;
    @Id
    @TableGenerator(name = "managed_file_SEQ",
            pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT",
            table = "sequence")
    @GeneratedValue(strategy = GenerationType.TABLE,
            generator = "managed_file_SEQ")
    private Long id;

    /**
     * used to manage directories structures
     */
    @Column(name = "creation_date" ,nullable = false)
    private Date creationDate;

    @Column(name = "file_name",length = 255, nullable = false)
    private String fileName;


    @OneToOne(fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            optional = false)
    private ManagedFileMetaData  metaData;

    /**
     * type of the downcast child
     */
    @Column(insertable = false,updatable = false )
    private String type;

    /**
     * File Weight in kilo bytes.
     */
    @Column(name = "weight_kb")
    private Long weight;

    /**
     * File name extension.
     */
    @Column(name = "extension")
    private String extension;

    /**
     * Managed file state
     */
    @Enumerated(EnumType.STRING)
    private ManagedFileState managedFileState;

    /**
     * Managed File unique complex unique id
     */
    @GeneratedValue(generator = "managed-file-uuid")
    @GenericGenerator(name="managed-file-uuid",
            strategy = "uuid2")
    @Column(name = "uuid",unique = true)
    private String UUID;

    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE},
            fetch = FetchType.LAZY)
    @LazyCollection(value = LazyCollectionOption.EXTRA)
    private Set<GenericFileMetaData>  genericFileMetaData =
            new HashSet<>();

    protected ManagedFile() {
        this.managedFileState = ManagedFileState.US;
        this.creationDate = new Date();
    }

    public ManagedFileMetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(ManagedFileMetaData metaData) {
        this.metaData = metaData;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setManagedFileState(ManagedFileState managedFileState) {
        this.managedFileState = managedFileState;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public Long getId() {
        return id;
    }

    public void addGenericMetaData(final GenericFileMetaData genericMetaData){
        this.genericFileMetaData.add(genericMetaData);
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
        this.extension = FilenameUtils.getExtension(this.fileName);
    }

    public Set<GenericFileMetaData> getGenericFileMetaData() {
        return genericFileMetaData;
    }

    public void setGenericFileMetaData(Set<GenericFileMetaData> genericFileMetaData) {
        this.genericFileMetaData = genericFileMetaData;
    }

    public abstract String getType();

    public void setType(String type) {
        this.type = type;
    }

    public Long getWeight() {
        return weight;
    }

    public void setWeight(Long weight) {
        this.weight = weight;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Enum getManagedFileState() {
        return managedFileState;
    }



    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ManagedFile))
            return false;

        ManagedFile that = (ManagedFile) o;

        return !(managedFileState != null ?
                !managedFileState.equals(that.managedFileState) :
                that.managedFileState != null) &&
                !(creationDate != null ? !creationDate.equals(that.creationDate) :
                        that.creationDate != null) &&
                !(extension != null ? !extension.equals(that.extension) :
                        that.extension != null) &&
                !(fileName != null ? !fileName.equals(that.fileName) :
                        that.fileName != null) &&
                !(id != null ? !id.equals(that.id) : that.id != null) &&
                !(type != null ? !type.equals(that.type) : that.type != null) &&
                !(weight != null ? !weight.equals(that.weight) : that.weight != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (creationDate != null ?
                creationDate.hashCode() : 0);
        result = 31 * result + (fileName != null ?
                fileName.hashCode() : 0);
        result = 31 * result + (type != null ?
                type.hashCode() : 0);
        result = 31 * result + (weight != null ?
                weight.hashCode() : 0);
        result = 31 * result + (extension != null ?
                extension.hashCode() : 0);
        result = 31 * result + (managedFileState != null ?
                managedFileState.hashCode() : 0);
        return result;
    }

    /**
     * Method that generate the uuid before persisting entity
     */
    @PrePersist
    public void generateUUID(){
        this.UUID = java.util.UUID.randomUUID().toString();
    }

    @Override
    public String toString() {
        return "ManagedFile{" +
                "id=" + id +
                ", creationDate=" + creationDate +
                ", fileName='" + fileName + '\'' +
                ", type='" + type + '\'' +
                ", weight=" + weight +
                ", extension='" + extension + '\'' +
                ", managedFileState=" + managedFileState +
                ", UUID='" + UUID + '\''
                ;
    }
}
