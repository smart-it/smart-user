/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.ui;

import com.smartitengineering.user.ui.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;

/**
 * Action which shows LoginWindow component.
 */
public class LoginWindowAction extends AbstractAction {

    Lookup.Result<LoginNode> result = CentralLookup.getDefault().lookupResult(LoginNode.class);

    public LoginWindowAction() {
        super(NbBundle.getMessage(LoginWindowAction.class, "CTL_LoginWindowAction"));
        putValue(SMALL_ICON, new ImageIcon(Utilities.loadImage(LoginWindowTopComponent.ICON_PATH, true)));
        setEnabled(true);

        result.addLookupListener(new LookupListener() {

            public void resultChanged(LookupEvent e) {
                Logger.getLogger(LoginWindowAction.class.getName()).warning("result changed");
                Lookup.Result r = (Lookup.Result) e.getSource();
                List<LoginNode> listLoginNode = new ArrayList(r.allInstances());
                if (listLoginNode!=null && listLoginNode.size()>0) {
                    if (listLoginNode.get(0).getUsername().equalsIgnoreCase("")) {
                        setEnabled(true);
                    } else {
                        setEnabled(false);
                    }
                }

            }
        });
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
