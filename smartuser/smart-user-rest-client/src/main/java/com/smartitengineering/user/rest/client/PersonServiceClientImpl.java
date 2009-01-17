/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.rest.client;

import com.smartitengineering.user.domain.Person;
import com.smartitengineering.user.filter.PersonFilter;
import com.smartitengineering.user.service.PersonService;
import com.smartitengineering.user.ws.element.PersonElement;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import java.util.Collection;

/**
 *
 * @author modhu7
 */
public class PersonServiceClientImpl extends AbstractClientImpl implements PersonService{

    public void create(Person person) {
        PersonElement personElement = new PersonElement();
        personElement.setPerson(person);
        final Builder type = getWebResource().path("person").type("application/xml");
        type.post(personElement);
    }

    public void update(Person person) {
        PersonElement personElement = new PersonElement();
        personElement.setPerson(person);
        final Builder type = getWebResource().path("person").type("application/xml");
        type.put(personElement);
    }

    public void delete(Person person) {
        PersonElement personElement = new PersonElement();
        personElement.setPerson(person);
        final Builder type = getWebResource().path("person").type("application/xml");
        type.post();
    }

    public Collection<Person> search(PersonFilter filter) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Person getPersonByID(Integer id) {
        WebResource resource = getWebResource().path("person/" +
                id);
        final PersonElement element = resource.get(PersonElement.class);
        final Person person = element.getPerson();
        return person;
    }

}
