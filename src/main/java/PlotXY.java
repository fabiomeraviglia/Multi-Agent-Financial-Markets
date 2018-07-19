import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.SeriesException;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class PlotXY extends ApplicationFrame {

    private String title;
    private String label;
    private Double[] data;

    public PlotXY( final String title, final String label, Double[] data) {
        super(title);

        this.title = title;
        this.label = label;
        this.data = data;

        final XYDataset dataset = createDataset();
        final JFreeChart chart = createChart(dataset);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(560 , 370));
        chartPanel.setMouseZoomable(true , false);
        setContentPane(chartPanel);
    }

    private XYDataset createDataset( ) {
        final TimeSeries series = new TimeSeries(this.label);
        Minute current = new Minute();

        for (int i = 0; i < this.data.length; i++) {
            try {
                series.add(current, data[i]);
                current = (Minute) current.next();
            } catch ( SeriesException e ) {
                System.err.println("Error adding to series");
            }
        }

        return new TimeSeriesCollection(series);
    }

    private JFreeChart createChart( final XYDataset dataset ) {
        return ChartFactory.createTimeSeriesChart(
                this.title,
                "Minutes",
                "Value",
                dataset,
                false,
                false,
                false);
    }
}   