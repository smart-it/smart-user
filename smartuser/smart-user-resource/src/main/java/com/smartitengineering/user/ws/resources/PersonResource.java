/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.ws.resources;

import com.smartitengineering.user.domain.Name;
import com.smartitengineering.user.domain.Person;
import com.smartitengineering.user.filter.PersonFilter;
import com.smartitengineering.user.service.PersonService;
import com.smartitengineering.user.ws.element.ExceptionElement;
import com.smartitengineering.user.ws.element.PersonElement;
import com.smartitengineering.user.ws.element.PersonElements;
import com.smartitengineering.user.ws.element.PersonFilterElement;
import java.lang.reflect.Field;
import java.util.HashSet;
import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
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
    @Consumes("application/xml")
    public Response create(PersonElement personElement) {
        try {
            personService.create(personElement.getPerson());
            return Response.ok().build();
        } catch (RuntimeException e) {            
            String group = e.getMessage().split("-")[0];            
            String field = e.getMessage().split("-")[1];
            ExceptionElement exceptionElement = new ExceptionElement();
            exceptionElement.setGroup(group);
            exceptionElement.setFieldCausedBy(field);
            return Response.serverError().status(Status.INTERNAL_SERVER_ERROR).entity(exceptionElement).build();            
        }
    }

    @PUT
    @Consumes("application/xml")
    public Response updatePerson(PersonElement personElement) {
        try {
            personService.update(personElement.getPerson());
            return Response.ok().build();
        }catch (RuntimeException e) {            
            String group = e.getMessage().split("-")[0];            
            String field = e.getMessage().split("-")[1];
            ExceptionElement exceptionElement = new ExceptionElement();
            exceptionElement.setGroup(group);
            exceptionElement.setFieldCausedBy(field);
            return Response.serverError().status(Status.INTERNAL_SERVER_ERROR).entity(exceptionElement).build();            
        }
    }

    @DELETE
    @Path("{email}")
    @Consumes("application/xml")
    public void deletePerson(@PathParam("email") String email) {
        try {
            personService.delete(personService.getPersonByEmail(email));            
        } catch (Exception e) {            
        }
    }

    @POST
    @Path("search")
    @Consumes("application/xml")
    @Produces("application/xml")
    public PersonElements searchPersonByPost(
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
    @Path("{email}")
    @Produces("application/xml")
    public PersonElement getPersonByID(
            @PathParam("email") String email) {
        PersonElement personElement = new PersonElement();
        try {
            personElement.setPerson(personService.getPersonByEmail(email));
        } catch (Exception e) {
        }
        return personElement;
    }

    @GET
    @Produces("application/xml")
    public PersonElements searchPersonByGet(
            @DefaultValue(value = "NO EMAIL") @QueryParam(value = "email") final String email,
            @DefaultValue(value = "NO FIRSTNAME") @QueryParam(value = "firstName") final String firstName,
            @DefaultValue(value = "NO SECONDNAME") @QueryParam(value = "lastName") final String lastName,
            @DefaultValue(value = "NO MIDDLEINITIAL") @QueryParam(value = "middleInitial") final String middleInitial) {
        PersonFilter filter = new PersonFilter();
        Name name = new Name();
        name.setFirstName(firstName);
        name.setLastName(lastName);
        name.setMiddleInitial(middleInitial);
        filter.setEmail(email);
        filter.setName(name);
        PersonElements personElements = new PersonElements();
        try {
            personElements.setPersons(personService.search(filter));
        } catch (Exception e) {
        }
        return personElements;
    }

    @GET
    @Path("allperson")
    @Produces("application/xml")
    public PersonElements getAllPerson() {
        PersonElements personElements = new PersonElements();
        try {
            personElements.setPersons(new HashSet<Person>(personService.getAllPerson()));
            System.out.println(personElements.getPersons());
            for (Person person : personElements.getPersons()) {
                System.out.println(person);
                Field[] fields = person.getClass().getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    System.out.println(field.getName() + " " + field.get(person));
                }
            }
        } catch (Exception e) {
        }
        return personElements;
    }

    public PersonService getPersonService() {
        return personService;
    }

    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }
}
