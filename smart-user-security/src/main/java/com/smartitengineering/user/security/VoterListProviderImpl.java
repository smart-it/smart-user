/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.security;

import com.smartitengineering.user.security.acl.impl.AclServiceImpl;
import com.smartitengineering.user.security.acl.impl.ConfigAttributeImpl;
import com.smartitengineering.user.security.acl.impl.SmartAclVoter;
import com.smartitengineering.user.security.acl.impl.SmartObjectIdentityRetrievalStrategyImpl;
import com.smartitengineering.user.security.acl.impl.SmartSidRetrievalStrategyImpl;
import com.smartitengineering.user.security.impl.SmartAclServiceImpl;
import com.smartitengineering.user.security.service.SmartAclService;
import java.io.InputStream;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.util.List;
import org.springframework.security.ConfigAttribute;
import org.springframework.security.acls.Permission;
import org.springframework.security.acls.domain.BasePermission;

/**
 *
 * @author modhu7
 */
public class VoterListProviderImpl {

    private List<SmartAclVoter> listVoter = new ArrayList<SmartAclVoter>();

    public VoterListProviderImpl(String xmlFileName) {
        try {
            InputStream votersStream = getClass().getClassLoader().getResourceAsStream(xmlFileName);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(votersStream);
            doc.getDocumentElement().normalize();
            NodeList nodeLst = doc.getElementsByTagName("voter");

            for (int s = 0; s < nodeLst.getLength(); s++) {

                Node voterNode = nodeLst.item(s);
                SmartAclVoter voter = new SmartAclVoter();
                SmartAclService smartAclService = new SmartAclServiceImpl();
                AclServiceImpl aclServiceImpl = new AclServiceImpl();
                aclServiceImpl.setSmartAclService(smartAclService);
                voter.setAclService(aclServiceImpl);
                voter.setObjectIdentityRetrievalStrategy(new SmartObjectIdentityRetrievalStrategyImpl());
                voter.setSidRetrievalStrategy(new SmartSidRetrievalStrategyImpl());

                if (voterNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element voterElement = (Element) voterNode;

                    NodeList permissionElementList = voterElement.getElementsByTagName("permission");
                    Element permissionElement = (Element) permissionElementList.item(0);
                    NodeList permissions = permissionElement.getChildNodes();
                    String permission = ((Node) permissions.item(0)).getNodeValue();
                    voter.setRequirePermission(new Permission[]{BasePermission.buildFromMask(Integer.parseInt(permission))});


                    NodeList roleList = voterElement.getElementsByTagName("role");
                    Element roleElement = (Element) roleList.item(0);
                    NodeList roles = roleElement.getChildNodes();
                    String role = ((Node) roles.item(0)).getNodeValue();
                    ConfigAttribute attribute = new ConfigAttributeImpl(role);
                    voter.setProcessConfigAttribute(attribute);

                    NodeList classList = voterElement.getElementsByTagName("class");
                    Element classElement = (Element) classList.item(0);
                    NodeList classes = classElement.getChildNodes();
                    String className = ((Node) classes.item(0)).getNodeValue();
                    Class domainClass = Class.forName(className);
                    voter.setProcessDomainObjectClass(domainClass);
                    System.out.println(domainClass.getName());

                    listVoter.add(voter);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<SmartAclVoter> getListVoter() {
        if (listVoter == null) {
            return new ArrayList<SmartAclVoter>();
        } else {
            return listVoter;
        }

    }

    public void setListVoter(List<SmartAclVoter> listVoter) {
        this.listVoter = listVoter;
    }
}
