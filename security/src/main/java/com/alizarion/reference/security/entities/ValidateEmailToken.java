package com.alizarion.reference.security.entities;

import com.alizarion.reference.location.entities.ElectronicAddress;
import com.alizarion.reference.person.entities.Person;
import com.alizarion.reference.security.tools.SecurityHelper;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@Table(name = "security_token_validate_electronic_address")
public class ValidateEmailToken implements Serializable {


    @Id
    @TableGenerator(
            name = "security_token_validate_electronic_address_SEQ",
            pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT",
            table = "sequence")
    @GeneratedValue(strategy = GenerationType.TABLE,
            generator = "security_token_validate_electronic_address_SEQ")
    private Long id;

    public static final String TYPE="Validate-email-token";

    @Embedded
    private SecurityToken validationToken;

    @OneToOne(optional = false)
    @JoinColumn(name = "email_id")
    private ElectronicAddress electronicAddress;

    @ManyToOne(optional = false)
    @JoinColumn(name = "person_id")
    private Person person;

    private static final long serialVersionUID = -5115069743327104540L;

    public ValidateEmailToken(final ElectronicAddress electronicAddress,
                              final Person person,
                              final long duration) {
        this.validationToken =  new SecurityToken(duration,
                SecurityHelper.getRandomAlphaNumericString(130));
        this.electronicAddress = electronicAddress;
        this.person =  person;

    }

    public ValidateEmailToken() {
    }

    public ElectronicAddress getElectronicAddress() {
        return electronicAddress;
    }

    public void setElectronicAddress(
            final ElectronicAddress electronicAddress) {
        this.electronicAddress = electronicAddress;
    }

    public Long getId() {
        return id;
    }

    public Person getPerson() {
        return person;
    }

    public Person updatePersonWithAddress(){
        this.person.
                setPrimaryElectronicAddress(this.electronicAddress);
        return person;
    }

    public SecurityToken getValidationToken() {
        return validationToken;
    }

    public void setPerson(Person person) {
        this.person = person;
    }


    @Override
    public String toString() {
        return "ValidateEmailToken{" +
                "electronicAddress=" + electronicAddress +
                ", person=" + person +
                '}';
    }
}