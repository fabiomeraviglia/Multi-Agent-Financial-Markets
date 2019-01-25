package Gui;

import javafx.util.Pair;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ObservablePlot extends ApplicationFrame {

    private String title;
    private String label;

    private int resolution;
    private JFreeChart chart;
    private boolean crop;
    private boolean pad;

    public ObservablePlot(
            String title,
            String label,
            java.awt.Dimension dims,
            int resolution,
            boolean crop,
            boolean pad)
    {
        super(title);

        this.title = title;
        this.label = label;
        this.resolution = resolution;
        this.crop = crop;
        this.pad = pad;

        List<Pair<Long, Double>> emptyData = new ArrayList<>();
        chart = createChart(createDataset(emptyData));
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(dims);
        chartPanel.setMouseZoomable(true , false);
        setContentPane(chartPanel);
    }

    private XYDataset createDataset(List<Pair<Long, Double>> data)
    {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series = new XYSeries(title);
        List<Pair<Double, Double>> convertedData = data.stream()
                .map(d -> new Pair<>((double)d.getKey(), d.getValue()))
                .collect(Collectors.toList());

        if(crop && convertedData.size() > resolution)
        {
            convertedData = convertedData.subList(data.size() - resolution, data.size());
        }
        else if(pad && data.size() < resolution)
        {
            List<Pair<Double,Double>> padding = new ArrayList<>();
            if(data.size() == 0) {
                padding.add(new Pair<>(0.0, 0.0));
            } else {
                padding.add(new Pair<>(convertedData.get(data.size() - 1).getKey() + 1.0, 0.0));
            }

            for(int i = 0; i < resolution - data.size() - 1; i++)
            {
                padding.add(new Pair<>(padding.get(padding.size() - 1).getKey() + 1.0, 0.0));
            }
            convertedData.addAll(padding);
        }

        for(Pair<Double, Double> p : convertedData)
        {
            series.add(p.getKey(), p.getValue());
        }

        dataset.addSeries(series);
        return dataset;

    }

    private JFreeChart createChart( final XYDataset dataset )
    {
        return ChartFactory.createXYLineChart(
                title,
                "",
                "",
                dataset
        );
    }

    public void updateChart(List<Pair<Long, Double>> newData)
    {
        XYDataset newDataset = createDataset(newData);
        chart.getXYPlot().setDataset(newDataset);
    }
}   