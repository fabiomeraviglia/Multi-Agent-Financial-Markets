import java.util.List;

public class PricePredictor {
    /**
     * Lo scopo è di restituire una predizione sul prezzo data la storia dei prezzi
     *
     * @param inputs
     * @return
     */
    public Integer getPrediction(List<Integer> inputs)
    {
        if(inputs.size()<5) return 100;
            //TODO
            return inputs.get(inputs.size()-1);
    }
}
