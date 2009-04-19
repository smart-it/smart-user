/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.ui;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
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
    }

    public void actionPerformed(ActionEvent evt) {
        TopComponent win = AddUserTopComponent.findInstance();
        win.open();
        win.requestActive();
    }
}
