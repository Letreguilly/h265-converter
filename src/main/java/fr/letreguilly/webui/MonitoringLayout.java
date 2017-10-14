package fr.letreguilly.webui;


import com.byteowls.vaadin.chartjs.ChartJs;
import com.byteowls.vaadin.chartjs.config.LineChartConfig;
import com.byteowls.vaadin.chartjs.data.Dataset;
import com.byteowls.vaadin.chartjs.data.LineDataset;
import com.byteowls.vaadin.chartjs.options.InteractionMode;
import com.byteowls.vaadin.chartjs.options.scale.Axis;
import com.byteowls.vaadin.chartjs.options.scale.CategoryScale;
import com.byteowls.vaadin.chartjs.options.scale.LinearScale;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

import java.util.ArrayList;
import java.util.List;

public class MonitoringLayout extends VerticalLayout {


    public static final int[] RGB_ARR_RED = {255, 99, 132};
    public static final int[] RGB_ARR_ORANGE = {255, 159, 64};
    public static final int[] RGB_ARR_YELLOW = {255, 205, 86};
    public static final int[] RGB_ARR_GREEN = {75, 192, 192};
    public static final int[] RGB_ARR_BLUE = {54, 162, 235};
    public static final int[] RGB_ARR_PURPLE = {153, 102, 255};
    public static final int[] RGB_ARR_GREY = {201, 203, 20};

    public static final String RGB_RED = "rgb(255, 99, 132)";
    public static final String RGB_ORANGE = "rgb(255, 159, 64)";
    public static final String RGB_YELLOW = "rgb(255, 205, 86)";
    public static final String RGB_GREEN = "rgb(75, 192, 192)";
    public static final String RGB_BLUE = "rgb(54, 162, 235)";
    public static final String RGB_PURPLE = "rgb(153, 102, 255)";
    public static final String RGB_GREY = "rgb(231,233,237)";
    public static final String RGB_BLACK = "rgb(0,0,0)";

    public MonitoringLayout() {
        LineChartConfig lineConfig = new LineChartConfig();
        lineConfig.data()
                .labels("January", "February", "March", "April", "May", "June", "July")
                .addDataset(new LineDataset().label("My First dataset").borderColor(RGB_RED).backgroundColor(RGB_RED))
                .addDataset(new LineDataset().label("My Second dataset").borderColor(RGB_BLUE).backgroundColor(RGB_BLUE))
                .addDataset(new LineDataset().label("My Third dataset").borderColor(RGB_GREEN).backgroundColor(RGB_GREEN))
                .addDataset(new LineDataset().label("My Third dataset").borderColor(RGB_YELLOW).backgroundColor(RGB_YELLOW))
                .and()
                .options()
                .responsive(true)
                .title()
                .display(true)
                .text("Chart.js Line Chart - Stacked Line")
                .and()
                .tooltips()
                .mode(InteractionMode.INDEX)
                .and()
                .hover()
                .mode(InteractionMode.INDEX)
                .and()
                .scales()
                .add(Axis.X, new CategoryScale()
                        .scaleLabel()
                        .display(true)
                        .labelString("Month")
                        .and())
                .add(Axis.Y, new LinearScale()
                        .stacked(true)
                        .scaleLabel()
                        .display(true)
                        .labelString("Value")
                        .and())
                .and()
                .done();

        // add random data for demo
        List<String> labels = lineConfig.data().getLabels();
        for (Dataset<?, ?> ds : lineConfig.data().getDatasets()) {
            LineDataset lds = (LineDataset) ds;
            List<Double> data = new ArrayList<>();
            for (int i = 0; i < labels.size(); i++) {
                data.add((double) (Math.random() > 0.5 ? 1.0 : -1.0) * Math.round(Math.random() * 100));
            }
            lds.dataAsList(data);
        }

        ChartJs chart = new ChartJs(lineConfig);
        chart.setJsLoggingEnabled(true);
        chart.addClickListener((a,b) -> MonitoringLayout.notification(a, b, lineConfig.data().getDatasets().get(a)));

        chart.setWidth(100, Unit.PERCENTAGE);
        chart.setHeight(100, Unit.PERCENTAGE);

        this.addComponent(chart);

    }

    public static void notification(int dataSetIdx, int dataIdx, Dataset<?, ?> dataset) {
        Notification.show("Dataset at Idx:" + dataSetIdx + "; Data at Idx: " + dataIdx + "; Value: " + dataset.getData().get(dataIdx), Notification.Type.TRAY_NOTIFICATION);
    }
}
