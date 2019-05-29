package org.micro.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.micro.plugin.view.AutoCodeDialog;

/**
 * Auto Code Main Action
 *
 * @author lry
 */
public class MainAction extends AnAction {

    public static final String DISPLAY_NAME = "Micro Service";

    @Override
    public void actionPerformed(AnActionEvent e) {
        AutoCodeDialog dialog = new AutoCodeDialog();
        dialog.setVisible(true);
    }

}
