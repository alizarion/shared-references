package com.alizarion.reference.showcase.services.rs;

import com.alizarion.reference.person.entities.PhysicalPerson;
import com.alizarion.reference.person.entities.Title;

import javax.annotation.security.DenyAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

/**
 * @author selim@openlinux.fr.
 */
@Path("/person")
public class HelloWorld {


    @Context
    SecurityContext securityContext;

    @DenyAll
    @GET
    @Path("/")
    @Produces({ "application/json","application/xml" })
    public PhysicalPerson getPerson() {
        //SecurityAssociation
        PhysicalPerson person = new PhysicalPerson();
        person.setTitle(Title.MR);
        person.setFirstName("titi");
        person.setLastName("toto");
        return person;
    }

}
