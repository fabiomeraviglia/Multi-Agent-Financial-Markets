import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class MarketHistoryTest {

    @org.junit.jupiter.api.Test
    void plotAskPrices() {
        MarketHistory history = new MarketHistory();
        double value = 0.0;
        for (int i = 0; i < 4000; i++) {
            value = value + Math.random( ) - 0.5;
            history.addAsk(new Integer((int) value * 1000));
        }
        history.plotAskPrices();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}