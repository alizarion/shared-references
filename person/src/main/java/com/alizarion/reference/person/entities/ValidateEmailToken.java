package com.alizarion.reference.person.entities;

import com.alizarion.reference.location.entities.ElectronicAddress;
import com.alizarion.reference.security.entities.Token;
import com.alizarion.reference.security.tools.SecurityHelper;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@Table(name = "security_token_validate_electronic_address")
public class ValidateEmailToken extends Token {

    public static final String TYPE="Validate-email-token";

    public static final long VALID_DAYS = 15;

    @OneToOne(optional = false)
    private ElectronicAddress electronicAddress;

    @ManyToOne(optional = false)
    private Person person;

    private static final long serialVersionUID = -5115069743327104540L;

    public ValidateEmailToken(final ElectronicAddress electronicAddress,
                              final Person person) {
        super(SecurityHelper.getRandomAlphaNumericString(130));
        this.electronicAddress = electronicAddress;
        this.person =  person;

    }

    @Override
    public long getValid() {
        return VALID_DAYS * 24 * 3600;
    }

    public ElectronicAddress getElectronicAddress() {
        return electronicAddress;
    }

    public void setElectronicAddress(
            final ElectronicAddress electronicAddress) {
        this.electronicAddress = electronicAddress;
    }

    public Person getPerson() {
        return person;
    }

    public Person updatePersonWithAddress(){
        this.person.
                setPrimaryElectronicAddress(this.electronicAddress);
        return person;
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
