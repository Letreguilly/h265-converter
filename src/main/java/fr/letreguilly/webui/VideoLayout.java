package fr.letreguilly.webui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.*;
import fr.letreguilly.persistence.entities.Video;
import fr.letreguilly.persistence.repositories.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

public class VideoLayout extends VerticalLayout {

    private VideoRepository videoRepository;

    public VideoLayout(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
        this.init();
    }



    public void init() {
       // this.setStyleName("right-panel");

        Grid<Video> grid = new Grid();

        grid.addColumn(Video::getId).setWidth(150).setCaption("Id").setSortable(true);
        grid.addColumn(Video::getName).setMaximumWidth(400).setCaption("Name").setSortable(true);
        grid.addColumn(Video::getSize).setMaximumWidth(300).setCaption("Size").setSortable(true);
        grid.addColumn(Video::getPath).setMaximumWidth(500).setCaption("Path").setSortable(true);
        grid.addColumn(Video::getMd5).setMaximumWidth(250).setCaption("Md5").setSortable(true);
        grid.addColumn(Video::getExtension).setMinimumWidth(100).setCaption("Extension").setSortable(true);






        List<Video> videoList = new ArrayList();
        for(Video video : videoRepository.findAll()){
            videoList.add(video);
        }
        grid.setItems(videoList);

        grid.setHeightMode(HeightMode.ROW);

        grid.setResponsive(true);
        grid.setWidth(100, Unit.PERCENTAGE);
        grid.setHeight(Page.getCurrent().getBrowserWindowHeight() -70, Unit.PIXELS);

        this.addComponent(grid);
    }

}
