/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.ui;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;

public final class LogoutAction extends AbstractAction {

    static final String ICON_PATH = "com/smartitengineering/user/ui/logout.gif";
    Lookup.Result<LoginNode> result = CentralLookup.getDefault().lookupResult(LoginNode.class);

    public LogoutAction() {
        super(NbBundle.getMessage(LoginWindowAction.class, "CTL_LogoutAction"));
        putValue(SMALL_ICON, new ImageIcon(Utilities.loadImage(ICON_PATH, true)));

        setEnabled(false);              

        result.addLookupListener(new LookupListener() {

            public void resultChanged(LookupEvent e) {
                //Logger.getLogger(LogoutAction.class.getName()).warning("result changed");
                Lookup.Result r = (Lookup.Result) e.getSource();
                List<LoginNode> listLoginNode = new ArrayList(r.allInstances());
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

    public void actionPerformed(ActionEvent e) {
        CentralLookup.getDefault().setLoginNode(new LoginNode("", ""));
    }

}
