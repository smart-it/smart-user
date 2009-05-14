/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.ui;

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Iterator;
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
 * Action which shows AddUser component.
 */
public class AddUserAction extends AbstractAction {

    public AddUserAction() {
        super(NbBundle.getMessage(AddUserAction.class, "CTL_AddUserAction"));
        putValue(SMALL_ICON, new ImageIcon(Utilities.loadImage(AddUserTopComponent.ICON_PATH, true)));
        setEnabled(false);

        LoginNode ln = new LoginNode("", "");
        CentralLookup.getDefault().setLoginNode(ln);

        //final Lookup lookup = CentralLookup.getDefault();
        Lookup.Result<LoginNode> result = CentralLookup.getDefault().lookupResult(LoginNode.class);

        Collection<LoginNode> loginNodes = (Collection<LoginNode>) result.allInstances();

        Collection all =CentralLookup.getDefault().lookupAll(LoginNode.class);
        Logger.getLogger(AddUserAction.class.getName()).warning(
                "The size of the all lookup result:" + new Integer(all.size()).toString());
        if (all != null) {
            Iterator ia = all.iterator();
            int i=0;
            while (ia.hasNext()) {
                Logger.getLogger(AddUserAction.class.getName()).warning(
                        i++ + "--->" + ia.next());
            }
        }else{
            Logger.getLogger(AddUserAction.class.getName()).warning(
                "The all lookup returns null" );
        }

        Logger.getLogger(AddUserAction.class.getName()).warning(
                "The size of the lookup result:" + new Integer(loginNodes.size()).toString());
        Logger.getLogger(AddUserAction.class.getName()).warning(
                result.allInstances().toString());

        result.addLookupListener(new LookupListener() {

            public void resultChanged(LookupEvent e) {
                Lookup.Result r = (Lookup.Result) e.getSource();
                Collection c = r.allInstances();

                Logger.getLogger(AddUserAction.class.getName()).warning(
                        "The size of the lookup result from result change:" + new Integer(c.size()).toString());
                setEnabled(true);
                
            }
        });

    }

    public void actionPerformed(ActionEvent evt) {
        TopComponent win = AddUserTopComponent.findInstance();
        win.open();
        win.requestActive();
    }
}
