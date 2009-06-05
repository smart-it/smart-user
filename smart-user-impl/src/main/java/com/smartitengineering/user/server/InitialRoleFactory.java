package com.smartitengineering.user.server;


import com.smartitengineering.user.domain.Privilege;
import com.smartitengineering.user.service.PrivilegeService;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class InitialRoleFactory {

   private PrivilegeService privilegeService;

    public PrivilegeService getPrivilegeService() {
        return privilegeService;
    }

    public void setPrivilegeService(PrivilegeService privilegeService) {
        this.privilegeService = privilegeService;
    }
   

    public void intialPrivileges() {
        
        try {
            InputStream privilegesStream = getClass().getClassLoader()
                .getResourceAsStream("Privileges.xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(privilegesStream);
            doc.getDocumentElement().normalize();
            System.out.println("Root element " + doc.getDocumentElement().getNodeName());
            NodeList nodeLst = doc.getElementsByTagName("Privilege");
            System.out.println("Information of all Priveleges");
            Privilege privilege = new Privilege();

            for (int s = 0; s < nodeLst.getLength(); s++) {

                Node privilegeNode = nodeLst.item(s);

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
                    privilege.setDisplayName(displayName);

                    NodeList shortDescriptionList = privilegeElement.getElementsByTagName("shortDescription");
                    Element shortDescriptionElement = (Element) shortDescriptionList.item(0);
                    NodeList shortDescriptions = shortDescriptionElement.getChildNodes();
                    String description = ((Node) shortDescriptions.item(0)).getNodeValue();
                    System.out.println("Description: " + description);
                    privilege.setShortDescription(description);
                    privilegeService.create(privilege);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}