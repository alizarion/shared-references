package com.alizarion.reference.location.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@Table(name = "location_language")
public class Language implements Serializable {

    @Column(name = "managed")
    private Boolean managed;

    @Id
    @Column(name = "lang_id")
    private String LangCode;

    public Language() {
        this.managed = false;
    }


}
