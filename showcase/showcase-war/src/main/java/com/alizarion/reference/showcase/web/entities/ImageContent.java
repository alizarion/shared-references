package com.alizarion.reference.showcase.web.entities;

import com.alizarion.reference.filemanagement.entities.ImageManagedFile;
import com.alizarion.reference.social.entities.notification.Notification;
import com.alizarion.reference.social.entities.notification.Subject;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@Table(name = "showcase_image_content")
public class ImageContent extends Subject {

    private static final long serialVersionUID = 5782065141973723980L;

    @OneToOne
    private ImageManagedFile managedFile;

    @Override
    public void notifyObservers(Notification notification) {

    }

    @Override
    public void notifyOwner(Notification notification) {

    }

    @Override
    public String getSubjectType() {
        return null;
    }
}
