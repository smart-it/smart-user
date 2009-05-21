/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.ui;

import com.smartitengineering.user.rest.client.login.LoginCenter;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;

/**
 * Action which shows LoginWindow component.
 */
public class LoginWindowAction extends AbstractAction {

    public LoginWindowAction() {
        super(NbBundle.getMessage(LoginWindowAction.class, "CTL_LoginWindowAction"));
        putValue(SMALL_ICON, new ImageIcon(Utilities.loadImage(LoginWindowTopComponent.ICON_PATH, true)));        
    }

    public void actionPerformed(ActionEvent evt) {
        TopComponent win = LoginWindowTopComponent.findInstance();
        win.open();
        win.requestActive();
    }

    private static class PropertyChangeListenerImpl implements PropertyChangeListener {

        public PropertyChangeListenerImpl() {
        }

        public void propertyChange(PropertyChangeEvent evt) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
