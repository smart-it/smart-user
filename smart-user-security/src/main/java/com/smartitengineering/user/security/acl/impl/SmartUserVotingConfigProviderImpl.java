/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.security.acl.impl;

import com.smartitengineering.user.security.acl.impl.ConfigAttributeImpl;
import com.smartitengineering.user.security.VotingConfig;
import com.smartitengineering.user.security.VotingConfigProvider;
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
public class SmartUserVotingConfigProviderImpl implements VotingConfigProvider{

    private List<VotingConfig> listVotingConfig = new ArrayList<VotingConfig>();

    public SmartUserVotingConfigProviderImpl(String xmlFileName) {
        try {
            InputStream votersStream = getClass().getClassLoader().getResourceAsStream(xmlFileName);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(votersStream);
            doc.getDocumentElement().normalize();
            NodeList nodeLst = doc.getElementsByTagName("voter");

            for (int s = 0; s < nodeLst.getLength(); s++) {

                Node voterNode = nodeLst.item(s);
                VotingConfig votingConfig = new VotingConfig();

                if (voterNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element voterElement = (Element) voterNode;

                    NodeList permissionElementList = voterElement.getElementsByTagName("permission");
                    Element permissionElement = (Element) permissionElementList.item(0);
                    NodeList permissions = permissionElement.getChildNodes();
                    String permission = ((Node) permissions.item(0)).getNodeValue();                    
                    votingConfig.setRequirePermission(new Permission[]{BasePermission.buildFromMask(Integer.parseInt(permission))});

                    NodeList roleList = voterElement.getElementsByTagName("role");
                    Element roleElement = (Element) roleList.item(0);
                    NodeList roles = roleElement.getChildNodes();
                    String role = ((Node) roles.item(0)).getNodeValue();
                    ConfigAttribute attribute = new ConfigAttributeImpl(role);
                    votingConfig.setProcessConfigAttribute(attribute);

                    NodeList classList = voterElement.getElementsByTagName("class");
                    Element classElement = (Element) classList.item(0);
                    NodeList classes = classElement.getChildNodes();
                    String className = ((Node) classes.item(0)).getNodeValue();
                    Class domainClass = Class.forName(className);
                    votingConfig.setProcessDomainObjectClass(domainClass);

                    listVotingConfig.add(votingConfig);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<VotingConfig> getVotingConfigList() {
        if(listVotingConfig==null){
            return new ArrayList<VotingConfig>();
        }
        return listVotingConfig;
    }

   

    
}
