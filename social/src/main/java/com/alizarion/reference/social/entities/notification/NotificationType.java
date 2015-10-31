package com.alizarion.reference.social.entities.notification;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@Table(name = "social_notification_type")
@Cacheable(true)
public class NotificationType implements Serializable{

    private static final long serialVersionUID = -3944404787546324529L;

    @Id
    @Column(name = "type")
    private String type;

    @Column(name = "notifiable")
    private Boolean notifiable;

    protected NotificationType() {
    }

    public NotificationType(String id) {
        this.type = id;
        this.notifiable = false;
    }

    public Boolean getNotifiable() {
        return notifiable;
    }

    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof NotificationType)) return false;

        NotificationType that = (NotificationType) o;

        return !(type != null ? !type.equals(that.type) : that.type != null);

    }

    @Override
    public int hashCode() {
        return type != null ? type.hashCode() : 0;
    }
}
