/*import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Sgd;*/

public class Main
{
  public static void main(String[] args)
  {
    int a = 1;
    /*
    List<String> rows = Files.readAllLines(Paths.get("res/AAPL_BID_RES_1M_RANGE_1Y.csv"));
    List<Integer> close_prices = new ArrayList<>();
    for (String r : rows.subList(1, rows.size()))
    {
      close_prices.add((int) (Float.parseFloat(r.split(",")[4]) * 1000));
    }
    List<Float> price_variations = new ArrayList<>();
    for (int i = 1; i < close_prices.size(); i++)
    {
      price_variations.add((float) (close_prices.get(i) - close_prices.get(i - 1)));
    }
    List<Float[]> price_samples = new ArrayList<>();
    for (int i = 0; i < price_variations.size() - 65; i++)
    {
      price_samples.add((Float[]) price_variations.subList(i, i + 65).toArray());
    }

    int cursor = 0;
    int training_samples_num = (int) (price_samples.size() * 0.66);
    float[][] feature_data = new float[training_samples_num][0];
    float[][] label_data = new float[training_samples_num][0];
    for (int i = 0; i < training_samples_num; i++, cursor++)
    {
      float[] feature_data_vector = new float[price_samples.get(i).length - 5];
      float[] label_data_vector = new float[5];

      for (int j = 0; j < feature_data_vector.length; j++)
      {
        feature_data_vector[j] = price_samples.get(i)[j];
      }
      for (int j = 0; j < label_data_vector.length; j++)
      {
        label_data_vector[j] = price_samples.get(i)[feature_data_vector.length + j];
      }
      feature_data[i] = feature_data_vector;
      label_data[i] = label_data_vector;
    }
    INDArray features = Nd4j.create(feature_data);
    INDArray labels = Nd4j.create(label_data);

      /*
      DummyDataSetIterator dataSetIterator = new DummyDataSetIterator(
        price_samples, 100, (int)((float)price_samples.size() * 0.66));*/
    /*
    int num_epochs = 15;

    MultiLayerConfiguration nn_conf = new NeuralNetConfiguration.Builder()
      .weightInit(WeightInit.XAVIER)
      .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
      .updater(new Sgd(0.05))
      .list()
      .layer(0, new DenseLayer.Builder()
        .nIn(60) // last 60 price variations
        .nOut(34)
        .activation(Activation.SIGMOID)
        .build())
      .layer(1, new DenseLayer.Builder()
        .nIn(34)
        .nOut(5) // predict next five price variations
        .activation(Activation.SOFTMAX)
        .build())
      .pretrain(false)
      .backprop(true)
      .build();

    MultiLayerNetwork model = new MultiLayerNetwork(nn_conf);
    model.init();

    model.setListeners(new ScoreIterationListener(1));
    for (int i = 0; i < num_epochs; i++)
    {
      model.fit(features, labels);
    }
    */
  }
}
