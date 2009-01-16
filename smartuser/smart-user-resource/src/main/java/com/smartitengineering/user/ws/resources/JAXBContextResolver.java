/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.ws.resources;


import com.smartitengineering.user.ws.element.PersonElement;
import com.smartitengineering.user.ws.element.PersonElements;
import com.smartitengineering.user.ws.element.PersonFilterElement;
import com.smartitengineering.user.ws.element.UserElement;
import com.smartitengineering.user.ws.element.UserElements;
import com.smartitengineering.user.ws.element.UserFilterElement;
import com.smartitengineering.user.ws.element.UserPersonElement;
import com.smartitengineering.user.ws.element.UserPersonElements;
import com.smartitengineering.user.ws.element.UserPersonFilterElement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;

/**
 *
 * @author imyousuf
 */
@Provider
public class JAXBContextResolver implements ContextResolver<JAXBContext> {

    private final JAXBContext context;
    private final Set<Class> types;
    private final Class[] cTypes = {PersonElement.class, PersonElements.class, PersonFilterElement.class,
        UserElement.class, UserElements.class, UserFilterElement.class,
        UserPersonElement.class, UserPersonElements.class, UserPersonFilterElement.class    
    };

    public JAXBContextResolver() throws Exception {
        this.types = new HashSet(Arrays.asList(cTypes));
        this.context = JAXBContext.newInstance(cTypes);
    }

    public JAXBContext getContext(Class<?> objectType) {
        return (types.contains(objectType)) ? context : null;
    }
}
