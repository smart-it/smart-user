package com.smartitengineering.user.ui;



import java.net.URL;
import org.openide.filesystems.MultiFileSystem;
import org.openide.filesystems.XMLFileSystem;
import org.xml.sax.SAXException;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author modhu7
 */
public class LoginFileSystem extends MultiFileSystem {
    private static LoginFileSystem INSTANCE;
    public LoginFileSystem() {
        // let's create the filesystem empty, because the user
        // is not yet logged in
        INSTANCE = this;
    }
    public static void assignURL(URL u) throws SAXException {
        INSTANCE.setDelegates(new XMLFileSystem(u));
    }
}
