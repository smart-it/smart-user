/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.organization.resource;


import java.util.Date;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import org.apache.abdera.Abdera;
import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Link;
import org.apache.abdera.model.Person;

/**
 *
 * @author russel
 */
public class AbstractResource {

    @Context
    protected UriInfo uriInfo;
    protected final Factory abderaFactory = Abdera.getNewFactory();

    protected Link getSelfLink(){

        Link selfLink = abderaFactory.newLink();
        selfLink.setHref(uriInfo.getRequestUri().toString());
        selfLink.setRel(Link.REL_SELF);
        return selfLink;
    }

    protected Feed getFeed(String title, Date updated){
        return getFeed(uriInfo.getRequestUri().toString(), title, updated);
    }

    protected Feed getFeed(String id, String title, Date updated){
        Feed feed = getFeed();
        feed.setId(id);
        feed.setTitle(title);
        feed.setUpdated(updated);

        return feed;
    }

    protected Feed getFeed(){
        Feed feed = abderaFactory.newFeed();
        feed.addLink(getSelfLink());
        feed.addAuthor(getDefaultAuthor());
        return feed;
    }

    protected Person getDefaultAuthor(){
        Person person = abderaFactory.newAuthor();
        person.setEmail("info@smartitengineering.com");
        person.setName("smart-user-api");
        person.setUri("http://smartitengineering.com");

        return person;
    }
}
