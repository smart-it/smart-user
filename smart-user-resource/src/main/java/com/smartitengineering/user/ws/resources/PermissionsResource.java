/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.ws.resources;

import com.smartitengineering.user.domain.Privilege;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Link;

/**
 *
 * @author russel
 */
@Path("/permissions")
public class PermissionsResource extends AbstractResource{

//   static final UriBuilder PERMISSIONS_URI_BUILDER;
//
//
//
//  static {
//    PERMISSIONS_URI_BUILDER = UriBuilder.fromResource(PermissionsResource.class);
//  }
//  @QueryParam("name")
//  private String privilegeName;
//  @QueryParam("count")
//  private Integer count;
//
//
//  @GET
//  @Produces(MediaType.APPLICATION_ATOM_XML)
//  public Response get() {
//    return get(null);
//  }
//
//  public Response get(String privilegeName) {
//    if (count == null) {
//      count = 10;
//    }
//    Response.ResponseBuilder responseBuilder = Response.ok();
//    Feed atomFeed = getFeed("Books", new Date());
//    Link booksLink = abderaFactory.newLink();
//    booksLink.setHref(UriBuilder.fromResource(RootResource.class).build().toString());
//    booksLink.setRel("root");
//    atomFeed.addLink(booksLink);
//    Collection<Privilege> privileges = Services.getInstance().getPrivilegeService().getPrivilegesByName(privilegeName);
//    if (privileges != null && !privileges.isEmpty()) {
//      MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
//      List<Privilege> bookList = new ArrayList<Privilege>(privileges);
//      Link nextLink = abderaFactory.newLink();
//      nextLink.setRel(Link.REL_PREVIOUS);
//      Book lastBook = bookList.get(0);
//
//
//      nextLink.setHref(nextUri.build(lastBook.getIsbn()).toString());
//      atomFeed.addLink(nextLink);
//      Link prevLink = abderaFactory.newLink();
//      prevLink.setRel(Link.REL_NEXT);
//      Book firstBook = bookList.get(books.size() - 1);
//      prevLink.setHref(prevUri.build(firstBook.getIsbn()).toString());
//      atomFeed.addLink(prevLink);
//      for (Book book : books) {
//        Entry bookEntry = abderaFactory.newEntry();
//        bookEntry.setId(book.getIsbn());
//        bookEntry.setTitle(book.getName());
//        bookEntry.setSummary(book.getName());
//        bookEntry.setUpdated(book.getLastModifiedDate());
//        Link bookLink = abderaFactory.newLink();
//        bookLink.setHref(BookResource.BOOK_URI_BUILDER.clone().build(book.getIsbn()).toString());
//        bookLink.setRel(Link.REL_ALTERNATE);
//        bookLink.setMimeType(MediaType.APPLICATION_ATOM_XML);
//        bookEntry.addLink(bookLink);
//        atomFeed.addEntry(bookEntry);
//      }
//    }
//    responseBuilder.entity(atomFeed);
//    return responseBuilder.build();
//
//  }
//
//  @POST
//  @Consumes(MediaType.APPLICATION_JSON)
//  public Response post(Book book) {
//    ResponseBuilder responseBuilder;
//    try {
//      Services.getInstance().getAuthorService().populateAuthor(book);
//      Services.getInstance().getBookService().save(book);
//      responseBuilder = Response.status(Status.CREATED);
//      responseBuilder.location(BookResource.BOOK_URI_BUILDER.clone().build(book.getIsbn()));
//    }
//    catch (AuthorNotFoundException ex) {
//      responseBuilder = Response.status(Status.BAD_REQUEST);
//    }
//    catch (Exception ex) {
//      responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
//      ex.printStackTrace();
//    }
//    return responseBuilder.build();
//  }

}
