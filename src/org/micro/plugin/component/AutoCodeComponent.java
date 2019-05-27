package org.micro.plugin.component;

import com.intellij.openapi.components.BaseComponent;
import org.jetbrains.annotations.NotNull;

public class AutoCodeComponent implements BaseComponent {

    @Override
    public void initComponent() {
    }

    @Override
    public void disposeComponent() {
    }

    @NotNull
    @Override
    public String getComponentName() {
        return "AutoCodeComponent";
    }

}
