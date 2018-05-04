import com.opencsv.CSVReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        CSVReader reader = new CSVReader(new FileReader("res/AAPL_BID_RES_1M_RANGE_1Y.csv"), ',');
        List<String[]> fields = reader.readAll();
        fields.remove(0);
        List<Integer> close_prices = new ArrayList<Integer>();
        for (String[] f : fields) close_prices.add((int) (Float.parseFloat(f[4]) * 1000));
    }
}