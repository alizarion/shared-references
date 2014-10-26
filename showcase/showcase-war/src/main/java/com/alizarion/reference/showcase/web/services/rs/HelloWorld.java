package com.alizarion.reference.showcase.web.services.rs;

import com.alizarion.reference.person.entities.PhysicalPerson;
import com.alizarion.reference.person.entities.Title;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

/**
 * @author selim@openlinux.fr.
 */
@Path("/person")
@RolesAllowed("profile")
public class HelloWorld {


    @Context
    SecurityContext securityContext;

    @GET
    @Path("/")
    @RolesAllowed("profile")
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
