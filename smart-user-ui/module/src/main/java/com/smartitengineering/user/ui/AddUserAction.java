/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.ui;

import com.smartitengineering.user.ui.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
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
 * Action which shows AddUser component.
 */
public class AddUserAction extends AbstractAction {

    Lookup.Result<LoginNode> result = CentralLookup.getDefault().lookupResult(LoginNode.class);

    public AddUserAction() {
        super(NbBundle.getMessage(AddUserAction.class, "CTL_AddUserAction"));
        putValue(SMALL_ICON, new ImageIcon(Utilities.loadImage(AddUserTopComponent.ICON_PATH, true)));
        setEnabled(false);


        result.addLookupListener(new LookupListener() {

            public void resultChanged(LookupEvent e) {
                Lookup.Result r = (Lookup.Result) e.getSource();
                Collection c = r.allInstances();

                Logger.getLogger(AddUserAction.class.getName()).warning(
                        "The size of the lookup result from result change:" + new Integer(c.size()).toString());
                List<LoginNode> listLoginNode = new ArrayList<LoginNode>(c);

                if (listLoginNode!=null && listLoginNode.size()>0) {
                    if (listLoginNode.get(0).getUsername().equalsIgnoreCase("")) {
                        setEnabled(false);
                    } else {
                        setEnabled(true);
                    }
                }

            }
        });

    }

    public void actionPerformed(ActionEvent evt) {
        TopComponent win = AddUserTopComponent.findInstance();
        win.open();
        win.requestActive();
    }
}
