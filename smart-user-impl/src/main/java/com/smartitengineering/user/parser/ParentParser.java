/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author modhu7
 */
public class ParentParser {
    
    private static Map<String, List<String>> mapParent;

    public ParentParser() {
        try {
            InputStream parentStream = getClass().getClassLoader().getResourceAsStream("parentfinder.xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(parentStream);
            doc.getDocumentElement().normalize();
            System.out.println("Root element " + doc.getDocumentElement().getNodeName());
            NodeList nodeLst = doc.getElementsByTagName("class");

            Map<String, List<String>> tempMapParent = new HashMap<String, List<String>>();


            for (int s = 0; s < nodeLst.getLength(); s++) {

                Node parentNode = nodeLst.item(s);

                if (parentNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element classElement = (Element) parentNode;

                    NodeList nameElementList = classElement.getElementsByTagName("name");
                    Element nameElement = (Element) nameElementList.item(0);
                    NodeList names = nameElement.getChildNodes();
                    String name = ((Node) names.item(0)).getNodeValue();

                    List<String> listParent = new ArrayList<String>();

                    NodeList parentList = nameElement.getElementsByTagName("parents");
                    Element parentsElement = (Element) parentList.item(0);
                    NodeList parentElementList = parentsElement.getElementsByTagName("parent");
                    
                    for (int p = 0; p < parentElementList.getLength(); p++) {
                        NodeList parentNodeList = parentElementList.item(p).getChildNodes();
                        String parent = ((Node) parentNodeList.item(0)).getNodeValue();
                        listParent.add(parent);
                    }
                    tempMapParent.put(name, listParent);
                }

            }
            mapParent = new HashMap<String, List<String>>(tempMapParent);
        } catch (Exception e) {
            mapParent = new HashMap<String, List<String>>();
            e.printStackTrace();
        }
    }



    public static List<String> getParentMethodName(String className){
        List<String> listParent = new ArrayList<String>();
        listParent = mapParent.get(className);
        if(listParent == null)
            return new ArrayList<String>();
        else
            return listParent;

    }

}
