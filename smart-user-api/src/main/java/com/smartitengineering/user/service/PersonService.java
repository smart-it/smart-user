/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.service;

import com.smartitengineering.user.domain.Person;
import com.smartitengineering.user.filter.PersonFilter;
import java.util.Collection;

/**
 *
 * @author modhu7
 */
public interface PersonService {

    public void save(Person person);

    public void update(Person person);

    public void delete(Person person);

    public Collection<Person> search(PersonFilter filter);

    public Collection<Person> getAllPerson();

    public Person getPersonByEmail(String email);

    public void validatePerson(Person person);
}
