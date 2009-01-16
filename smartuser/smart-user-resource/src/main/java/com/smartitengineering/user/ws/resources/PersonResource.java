/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.ws.resources;

import com.smartitengineering.user.filter.PersonFilter;
import com.smartitengineering.user.service.PersonService;
import com.smartitengineering.user.ws.element.PersonElement;
import com.smartitengineering.user.ws.element.PersonElements;
import com.smartitengineering.user.ws.element.PersonFilterElement;
import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
/**
 *
 * @author modhu7
 */
@Path("person")
@Component
@Scope(value = "singleton")
public class PersonResource {
    @Resource(name = "personService")
    private PersonService personService;
    
    
    @POST    
    @Path("create")
    @Consumes("application/xml")
    public void create(PersonElement personElement) {
        try {
            personService.create(personElement.getPerson());
        } catch (Exception e) {
        }
    }

    @PUT    
    @Consumes("application/xml")
    public void updatePerson(PersonElement personElement) {
        try {
            personService.update(personElement.getPerson());
        } catch (Exception e) {
        }
    }

    @DELETE
    @Path("{id}")
    @Consumes("application/xml")
    public void deletePerson(@PathParam("id") Integer id) {
        try {
            personService.delete(personService.getPersonByID(id));
        } catch (Exception e) {
        }
    }
    
    @POST
    @Path("search/person")
    @Consumes("application/xml")
    @Produces("application/xml")
    public PersonElements searchPerson(
            PersonFilterElement personFilterElement) {
        PersonElements personElements = new PersonElements();
        PersonFilter personFilter;
        if (personFilterElement != null && personFilterElement.getPersonFilter() != null) {
            personFilter = personFilterElement.getPersonFilter();
        } else {
            personFilter = new PersonFilter();
        }        
        try {
            personElements.setPersons(personService.search(personFilter));
        } catch (Exception e) {
        }
        return personElements;
    }
    @GET
    @Path("{id}")
    @Produces("application/xml")
    public PersonElement getPersonByID(
            @PathParam("id") Integer id) {
        PersonElement personElement = new PersonElement();
        try {
            personElement.setPerson(personService.getPersonByID(id));
        } catch (Exception e) {
        }
        return personElement;
    }

    public PersonService getPersonService() {
        return personService;
    }

    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }
    
}
