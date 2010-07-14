package com.smartitengineering.user.parser;

import com.smartitengineering.user.domain.BasicIdentity;
import com.smartitengineering.user.domain.Name;
import com.smartitengineering.user.domain.Person;
import com.smartitengineering.user.domain.Privilege;
import com.smartitengineering.user.domain.Role;
import com.smartitengineering.user.domain.User;
import com.smartitengineering.user.domain.UserPerson;
import com.smartitengineering.user.service.PrivilegeService;
import com.smartitengineering.user.service.RoleService;
import com.smartitengineering.user.service.UserPersonService;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class InitialRoleFactory {

    private PrivilegeService privilegeService;
    private RoleService roleService;
    private UserPersonService userPersonService;

    public UserPersonService getUserPersonService() {
        return userPersonService;
    }

    public void setUserPersonService(UserPersonService userPersonService) {
        this.userPersonService = userPersonService;
    }

    public RoleService getRoleService() {
        return roleService;
    }

    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    public PrivilegeService getPrivilegeService() {
        return privilegeService;
    }

    public void setPrivilegeService(PrivilegeService privilegeService) {
        this.privilegeService = privilegeService;
    }

    public void initializeInformation() {
        initializePrivileges();
        initializeRoles();
        initializeUsers();
    }

    public void initializePrivileges() {

        try {
            InputStream privilegesStream = getClass().getClassLoader().getResourceAsStream("privileges.xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(privilegesStream);
            doc.getDocumentElement().normalize();
            System.out.println("Root element " + doc.getDocumentElement().getNodeName());
            NodeList nodeLst = doc.getElementsByTagName("privilege");
            System.out.println("Information of all Priveleges");
            Privilege privilege = new Privilege();

            for (int s = 0; s < nodeLst.getLength(); s++) {

                Node privilegeNode = nodeLst.item(s);
                System.out.println(nodeLst.getLength());

                if (privilegeNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element privilegeElement = (Element) privilegeNode;

                    NodeList nameElementList = privilegeElement.getElementsByTagName("name");
                    Element nameElement = (Element) nameElementList.item(0);
                    NodeList names = nameElement.getChildNodes();
                    String name = ((Node) names.item(0)).getNodeValue();
                    System.out.println("Name : " + name);
                    privilege.setName(name);

                    NodeList displayNameList = privilegeElement.getElementsByTagName("displayName");
                    Element displayNameElement = (Element) displayNameList.item(0);
                    NodeList displayNames = displayNameElement.getChildNodes();
                    String displayName = ((Node) displayNames.item(0)).getNodeValue();
                    System.out.println("Display Name : " + displayName);
                    //privilege.setDisplayName(displayName);

                    NodeList shortDescriptionList = privilegeElement.getElementsByTagName("shortDescription");
                    Element shortDescriptionElement = (Element) shortDescriptionList.item(0);
                    NodeList shortDescriptions = shortDescriptionElement.getChildNodes();
                    String description = ((Node) shortDescriptions.item(0)).getNodeValue();
                    System.out.println("Description: " + description);
                    //privilege.setShortDescription(description);
                    privilegeService.create(privilege);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initializeRoles() {
        try {
            InputStream rolesStream = getClass().getClassLoader().getResourceAsStream("roles.xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(rolesStream);
            doc.getDocumentElement().normalize();
            System.out.println("Root element " + doc.getDocumentElement().getNodeName());
            NodeList nodeLst = doc.getElementsByTagName("role");
            System.out.println("Information of all Roles");
            Role role = new Role();

            for (int s = 0; s < nodeLst.getLength(); s++) {

                Node roleNode = nodeLst.item(s);

                if (roleNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element roleElement = (Element) roleNode;

                    NodeList nameElementList = roleElement.getElementsByTagName("name");
                    Element nameElement = (Element) nameElementList.item(0);
                    NodeList names = nameElement.getChildNodes();
                    String name = ((Node) names.item(0)).getNodeValue();
                    System.out.println("Name : " + name);
                    role.setName(name);

                    NodeList displayNameList = roleElement.getElementsByTagName("displayName");
                    Element displayNameElement = (Element) displayNameList.item(0);
                    NodeList displayNames = displayNameElement.getChildNodes();
                    String displayName = ((Node) displayNames.item(0)).getNodeValue();
                    System.out.println("Display Name : " + displayName);
                    role.setDisplayName(displayName);

                    NodeList shortDescriptionList = roleElement.getElementsByTagName("shortDescription");
                    Element shortDescriptionElement = (Element) shortDescriptionList.item(0);
                    NodeList shortDescriptions = shortDescriptionElement.getChildNodes();
                    String description = ((Node) shortDescriptions.item(0)).getNodeValue();
                    System.out.println("Description: " + description);
                    role.setShortDescription(description);

                    NodeList privilegesList = roleElement.getElementsByTagName("privileges");
                    System.out.println(privilegesList.getLength());
                    Element privelegesElement = (Element) privilegesList.item(0);
                    System.out.println(privelegesElement.toString());
                    NodeList privilegeElementList = privelegesElement.getElementsByTagName("privilege");
                    System.out.println(privilegeElementList.getLength());
                    Set<Privilege> privileges = new HashSet<Privilege>();

                    for (int p = 0; p < privilegeElementList.getLength(); p++) {
                        NodeList privilegeNodeList = privilegeElementList.item(p).getChildNodes();
                        String privilegeName = ((Node) privilegeNodeList.item(0)).getNodeValue();
                        System.out.println(privilegeName);
                        Privilege privilege = privilegeService.getPrivilegesByObjectID(privilegeName);
                        System.out.print(privilege.getName());
                        privileges.add(privilege);
                    }
                    role.setPrivileges(privileges);
                    roleService.create(role);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initializeUsers() {

        try {

            InputStream rolesStream = getClass().getClassLoader().getResourceAsStream("users.xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(rolesStream);
            doc.getDocumentElement().normalize();
            System.out.println("Root element " + doc.getDocumentElement().getNodeName());
            NodeList nodeLst = doc.getElementsByTagName("user");
            System.out.println("Information of all Users");


            for (int s = 0; s < nodeLst.getLength(); s++) {

                Person person = new Person();
                User user = new User();
                BasicIdentity basicIdentity = new BasicIdentity();
                UserPerson userPerson = new UserPerson();
                Name name = new Name();

                Node userNode = nodeLst.item(s);

                if (userNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element userElement = (Element) userNode;

                    //Parse the name

                    NodeList nameElementList = userElement.getElementsByTagName("name");

                    NodeList firstNameList = userElement.getElementsByTagName("firstName");
                    Element firstNameElement = (Element) firstNameList.item(0);
                    NodeList firstNames = firstNameElement.getChildNodes();
                    String firstName = ((Node) firstNames.item(0)).getNodeValue();
                    name.setFirstName(firstName);

                    NodeList lastNameList = userElement.getElementsByTagName("lastName");
                    Element lastNameElement = (Element) lastNameList.item(0);
                    NodeList lastNames = lastNameElement.getChildNodes();
                    String lastName = ((Node) lastNames.item(0)).getNodeValue();
                    name.setLastName(lastName);

                    NodeList middleInitialList = userElement.getElementsByTagName("middleInitial");
                    String middleInitial = "";
                    if (middleInitialList.getLength() > 0) {
                        Element middleInitialElement = (Element) middleInitialList.item(0);
                        NodeList middleInitials = middleInitialElement.getChildNodes();
                        if (middleInitials.getLength() > 0) {
                            middleInitial = ((Node) middleInitials.item(0)).getNodeValue();
                            name.setMiddleInitial(middleInitial);
                        }
                    }

                    System.out.println("Name : " + firstName + " " + middleInitial + " " + lastName);

                    NodeList nidElementList = userElement.getElementsByTagName("nationalid");
                    Element nidElement = (Element) nidElementList.item(0);
                    NodeList nids = nidElement.getChildNodes();
                    String nid = ((Node) nids.item(0)).getNodeValue();
                    System.out.println("National ID : " + nid);

                    basicIdentity.setName(name);
                    basicIdentity.setNationalID(nid);
                    person.setSelf(basicIdentity);

                    NodeList emailElementList = userElement.getElementsByTagName("email");
                    Element emailElement = (Element) emailElementList.item(0);
                    NodeList emails = emailElement.getChildNodes();
                    String emailAddress = ((Node) emails.item(0)).getNodeValue();
                    System.out.println("Email : " + emailAddress);
                    person.setPrimaryEmail(emailAddress);

                    NodeList displayNameList = userElement.getElementsByTagName("username");
                    Element displayNameElement = (Element) displayNameList.item(0);
                    NodeList usernames = displayNameElement.getChildNodes();
                    String username = ((Node) usernames.item(0)).getNodeValue();
                    System.out.println("Username : " + username);
                    user.setUsername(username);

                    NodeList shortDescriptionList = userElement.getElementsByTagName("password");
                    Element shortDescriptionElement = (Element) shortDescriptionList.item(0);
                    NodeList passwordNodes = shortDescriptionElement.getChildNodes();
                    String password = ((Node) passwordNodes.item(0)).getNodeValue();
                    System.out.println("Password: " + password);
                    user.setPassword(password);

                    NodeList rolesList = userElement.getElementsByTagName("roles");
                    Element rolesElement = (Element) rolesList.item(0);
                    NodeList roleElementList = rolesElement.getElementsByTagName("role");
                    Set<Role> roles = new HashSet<Role>();

                    for (int p = 0; p < roleElementList.getLength(); p++) {
                        NodeList roleNodeList = roleElementList.item(p).getChildNodes();
                        String roleName = ((Node) roleNodeList.item(0)).getNodeValue();
                        System.out.println(roleName);
                        Role role = roleService.getRoleByUserID(roleName);
                        System.out.print(role.getDisplayName());
                        roles.add(role);
                    }
                    user.setRoles(roles);
                    userPerson.setUser(user);
                    System.out.println("Person ID: " + person.getId());
                    userPerson.setPerson(person);
                    System.out.println("Person ID from UserPerson: " + userPerson.getPerson().getId());
                    userPersonService.create(userPerson);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}