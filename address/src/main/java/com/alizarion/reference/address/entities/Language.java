package com.alizarion.reference.address.entities;

import com.alizarion.reference.staticparams.StaticParam;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@Table(catalog = StaticParam.CATALOG,name = "language")
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
