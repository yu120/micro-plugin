package org.micro.plugin.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.micro.plugin.dialog.AutoCodeDialog;

public class AutoCodeAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        AutoCodeDialog dialog = new AutoCodeDialog();
        dialog.setVisible(true);
    }

}
