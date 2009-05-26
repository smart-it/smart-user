/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.ui;

import java.util.Collection;
import java.util.Iterator;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author modhu7
 */
public class CentralLookup extends AbstractLookup {

    private static CentralLookup ctx = new CentralLookup();
    private InstanceContent content = null;
    private LoginNode loginNode = new LoginNode("", "");
    private CentralLookup() {
        this(new InstanceContent());
    }

    private CentralLookup(InstanceContent ic) {
        super(ic);
        this.content = ic;
        //add(loginNode);
    }
    public static CentralLookup getDefault(){
        return ctx;
    }

    public LoginNode getLoginNode() {
        return loginNode;
    }

    public void setLoginNode(LoginNode loginNode) {
        Collection all =
                lookupAll(LoginNode.class);
        if (all != null) {
            Iterator ia = all.iterator();
            while (ia.hasNext()) {
                this.
                remove(ia.next());
            }
        }
        add(loginNode);
        this.loginNode = loginNode;
    }
    public void add(Object instance) {
        content.add(instance);
    }

    public void remove(Object instance) {
        content.remove(instance);
    }

}
