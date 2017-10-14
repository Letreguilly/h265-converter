package fr.letreguilly.webui;

import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import fr.letreguilly.persistence.repositories.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.teemusa.sidemenu.SideMenu;

@SpringUI
@Theme("mytheme")
public class VaadinUI extends UI {

    @Autowired
    private VideoRepository videoRepository;

    @Override
    protected void init(VaadinRequest request) {
        SideMenu sideMenu = this.initSideMenu();
        this.setContent(sideMenu);
    }

    private SideMenu initSideMenu() {
        SideMenu sideMenu = new SideMenu();
        sideMenu.setWidth(100, Unit.PERCENTAGE);
        sideMenu.setHeight(100, Unit.PERCENTAGE);

        sideMenu.addUserMenuItem("other", () -> System.out.println("click"));

        sideMenu.addMenuItem("videos", VaadinIcons.MOVIE, () -> sideMenu.setContent(new VideoLayout(videoRepository)));
        sideMenu.addMenuItem("logs", VaadinIcons.ARCHIVE, () -> sideMenu.setContent(new LogsLayout()));
        sideMenu.addMenuItem("monitoring", VaadinIcons.LINE_BAR_CHART, () -> sideMenu.setContent(new MonitoringLayout()));
        sideMenu.setMenuCaption("Video converter");

        this.setUser(sideMenu, "Sylvain", VaadinIcons.USER);

        return sideMenu;
    }

    private void setUser(SideMenu sideMenu, String name, VaadinIcons icon) {
        sideMenu.setUserName(name);
        sideMenu.setUserIcon(icon);

        sideMenu.clearUserMenu();

        sideMenu.addUserMenuItem("Settings", VaadinIcons.WRENCH, () -> {
            Notification.show("Showing settings", Notification.Type.TRAY_NOTIFICATION);
        });
        sideMenu.addUserMenuItem("Sign out", VaadinIcons.USER, () -> {
            Notification.show("Logging out..", Notification.Type.TRAY_NOTIFICATION);
        });


    }

}
