package com.alizarion.reference.location.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@Table(name = "location_device")
@DiscriminatorValue(value = Device.TYPE)
public class Device   extends Address implements Serializable {

    private static final long serialVersionUID = 578062672714930146L;

    public static final String TYPE = "device";

    @Column(name = "uuid",unique = true)
    private String UUID;

    @Column(name = "name")
    private String name;

    @Column(name = "model")
    private String model;

    @Column(name = "platform")
    private String platform;

    @Column(name = "version")
    private String version;

    @Column(name = "creation_date")
    private Date creationDate;

    @Column(name = "notifiable")
    private Boolean notifiable;

    @Column(name = "messaging_id")
    private String messagingId;

    public Device() {
    }

    @Override
    public String getType() {
        return TYPE;
    }

    public Device(String UUID) {
        this.UUID = UUID;
        this.creationDate =  new Date();
        this.notifiable =  false;

    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public String getPlatform() {
        return platform;
    }

    public Boolean getNotifiable() {
        return notifiable;
    }

    public void setNotifiable(Boolean notifiable) {
        this.notifiable = notifiable;
    }

    public String getMessagingId() {
        return messagingId;
    }

    public void setMessagingId(String messagingId) {
        this.notifiable = true;
        this.messagingId = messagingId;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Device)) return false;

        Device device = (Device) o;

        return !(UUID != null ? !UUID.equals(device.UUID)
                : device.UUID != null);

    }

    @Override
    public int hashCode() {
        return UUID != null ? UUID.hashCode() : 0;
    }
}
