package com.platform.gen.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.platform.gen.dialog.AutoCodeDialog;

public class AutoCodeAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Application application = ApplicationManager.getApplication();
        AutoCodeDialog dialog = new AutoCodeDialog();
        dialog.setVisible(true);
    }

}
