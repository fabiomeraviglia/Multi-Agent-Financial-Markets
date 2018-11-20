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
        if(inputs.size() < 1) { return ExperimentConfiguration.INITIAL_PRICE; }
        return inputs.get(inputs.size()-1);
    }
    public Integer getPrediction(Integer input)
    {

        return input;
    }
}
