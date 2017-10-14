package fr.letreguilly.webui;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class LogsLayout extends VerticalLayout {
    public LogsLayout() {
        Label label = new Label("LogsLayout");
        this.addComponent(label);
    }
}
