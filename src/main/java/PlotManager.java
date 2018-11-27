import javafx.util.Pair;
import org.jfree.ui.RefineryUtilities;

import java.awt.*;
import java.util.*;
import java.util.List;

public class PlotManager
{
    public static final List<Observable> watchedObservables;
    private static final Map<Observable, String> observableLabels;
    private static final Map<Observable, Integer[]> observablePlotParams;
    static
    {
        // Set Observables To be plotted
        List<Observable> tmpWatched = new ArrayList<>();
        tmpWatched.add(Observable.BID_PRICE_HISTORY);
        tmpWatched.add(Observable.ASK_PRICE_HISTORY);
        tmpWatched.add(Observable.MARKET_DEPTH);
        tmpWatched.add(Observable.LOG_RETURNS);
        tmpWatched.add(Observable.LOG_SPREAD);

        // Set plot label for each observable
        Map<Observable, String> tmpLabelMap = new HashMap<>();
        tmpLabelMap.put(Observable.BID_PRICE_HISTORY, "Bid Price");
        tmpLabelMap.put(Observable.ASK_PRICE_HISTORY, "Ask Price");
        tmpLabelMap.put(Observable.MARKET_DEPTH, "Market Depth");
        tmpLabelMap.put(Observable.LOG_RETURNS, "Logaritmic Returns");
        tmpLabelMap.put(Observable.LOG_SPREAD, "Spread");

        // Set plot parameters for each observable
        Map<Observable, Integer[]> tmpParamMap = new HashMap<>();
        tmpParamMap.put(Observable.BID_PRICE_HISTORY, new Integer[]{600, 300, 10000, 1, 1});
        tmpParamMap.put(Observable.ASK_PRICE_HISTORY, new Integer[]{600, 300, 10000, 1, 1});
        tmpParamMap.put(Observable.MARKET_DEPTH, new Integer[]{600, 300, 5000, 0, 0});
        tmpParamMap.put(Observable.LOG_RETURNS, new Integer[]{600, 300, 10000, 1, 1});
        tmpParamMap.put(Observable.LOG_SPREAD, new Integer[]{600, 300, 10000, 1, 1});

        watchedObservables = Collections.unmodifiableList(tmpWatched);
        observableLabels = Collections.unmodifiableMap(tmpLabelMap);
        observablePlotParams = Collections.unmodifiableMap(tmpParamMap);
    }

    private final Map<Observable, ObservablePlot> plots;

    public PlotManager()
    {
        plots = new HashMap<>();
        for(Observable o : watchedObservables)
        {
            Integer[] p = observablePlotParams.get(o);
            String l = observableLabels.get(o);
            plots.put(o, new ObservablePlot(l,"", new Dimension(p[0], p[1]), p[2], p[3] != 0, p[4] != 0));
            plots.get(o).pack();
            RefineryUtilities.positionFrameRandomly(plots.get(o));
            plots.get(o).setVisible(true);
        }
    }

    public void updatePlot(Observable o, List<Pair<Integer,Double>> newData)
    {
        plots.get(o).updateChart(newData);
    }
}
