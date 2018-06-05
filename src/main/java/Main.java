import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.GravesLSTM;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.conf.layers.RnnOutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.RmsProp;
import org.nd4j.linalg.learning.config.Sgd;
import org.nd4j.linalg.lossfunctions.LossFunctions;

public class Main
{
  private static List<String> readAPPLData()
  {
    List<String> rows;
    try
    {
      rows = Files.readAllLines(Paths.get("res/AAPL_BID_RES_1M_RANGE_1Y.csv"));
    } catch (Exception e)
    {
      System.out.println("Error reading file");
      return null;
    }
    return rows;
  }

  private static List<Integer> extract_close_prices_from_csv_rows(
    List<String> csv_rows,
    int position,
    boolean header)
  {
    List<Integer> close_prices = new ArrayList<>();
    csv_rows = header ? csv_rows.subList(1, csv_rows.size()) : csv_rows;
    for (String r : csv_rows)
    {
      close_prices.add((int) (Float.parseFloat(r.split(",")[position]) * 1000));
    }

    return  close_prices;
  }

  private static List<Float> convert_to_price_variations(List<Integer> close_prices)
  {
    List<Float> price_variations = new ArrayList<>();
    for (int i = 1; i < close_prices.size(); i++)
    {
      price_variations.add((float) (close_prices.get(i) - close_prices.get(i - 1)));
    }

    return price_variations;
  }

  private static INDArray[] prepare_price_variations_features_labels(
    List<Float> price_variations,
    float training_percentage,
    int price_variation_in_input,
    int price_variation_in_output
  )
  {
    int price_block_size = price_variation_in_input + price_variation_in_output;
    List<Float[]> price_samples = new ArrayList<>();
    for (int i = 0; i < price_variations.size() - price_block_size; i++)
    {
      price_samples.add(new Float[price_block_size]);
      for (int j = 0; j < price_block_size; j++)
      {
        //price_samples.get(i)[j] = price_variations.get(i + j);
        price_samples.get(i)[j] = 500.0f;
      }
    }

    //int training_samples_num = (int) (price_samples.size() * training_percentage);
    float[][] feature_data = new float[price_samples.size()][0];
    float[][] label_data = new float[price_samples.size()][0];
    for (int i = 0; i < price_samples.size(); i++)
    {
      float[] feature_data_vector = new float[price_samples.get(i).length - price_variation_in_output];
      float[] label_data_vector = new float[price_variation_in_output];

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

    int training_samples = (int) (feature_data.length * training_percentage);
    float[][] training_feature_data = new float[training_samples][0];
    float[][] training_label_data = new float[training_samples][0];
    float[][] testing_feature_data = new float[feature_data.length - training_samples][0];
    float[][] testing_label_data = new float[feature_data.length - training_samples][0];
    for (int i = 0; i < training_samples; i++)
    {
      training_feature_data[i] = feature_data[i];
      training_label_data[i] = label_data[i];
    }
    for (int i = 0; i < feature_data.length - training_samples; i++)
    {
      testing_feature_data[i] = feature_data[i+training_samples];
      testing_label_data[i] = label_data[i+training_samples];
    }

    INDArray training_features = Nd4j.create(training_feature_data);
    INDArray training_labels = Nd4j.create(training_label_data);
    INDArray testing_features = Nd4j.create(testing_feature_data);
    INDArray testing_labels = Nd4j.create(testing_label_data);

    return new INDArray[]{
      training_features,
      training_labels,
      testing_features,
      testing_labels};
  }

  public static void main(String[] args)
  {
    float TRAINING_PERCENTAGE = 0.66f;
    int NUM_EPOCHS = 40;

    List<String> appl_csv_rows = readAPPLData();

    List<Integer> close_prices = extract_close_prices_from_csv_rows(
      appl_csv_rows,
      4, true);

    List<Float> price_variations = convert_to_price_variations(close_prices);

    INDArray[] features_and_labels = prepare_price_variations_features_labels(
      price_variations, 0.66f, 100, 5);

    INDArray training_features = features_and_labels[0];
    INDArray training_labels = features_and_labels[1];
    INDArray testing_features = features_and_labels[2];
    INDArray testing_labels = features_and_labels[3];


    /*MultiLayerConfiguration nn_conf = new NeuralNetConfiguration.Builder()
      .weightInit(WeightInit.XAVIER)
      .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
      .updater(new Sgd(0.05))
      .list()
      .layer(0, new DenseLayer.Builder()
        .nIn(60) // last 60 price variations
        .nOut(120)
        .activation(Activation.SIGMOID)
        .build())

      .layer(2, new OutputLayer.Builder(LossFunctions.LossFunction.MSE)
        .nIn(34)
        .nOut(5) // predict next five price variations
        .activation(Activation.IDENTITY)
        .build())
      .pretrain(false)
      .backprop(true)
      .build();*/

    MultiLayerConfiguration nn_conf = new NeuralNetConfiguration.Builder()
      .weightInit(WeightInit.XAVIER)
      .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
      .updater(new Sgd(0.05))
      .list()
      .layer(0, new GravesLSTM.Builder()
        .nIn(100)
        .nOut(120)
        .activation(Activation.TANH)
        .build())
      .layer(1, new GravesLSTM.Builder()
        .nIn(120)
        .nOut(30)
        .activation(Activation.TANH)
        .build())
      .layer(2, new RnnOutputLayer.Builder(LossFunctions.LossFunction.MSE)
        .nIn(30)
        .nOut(5) // predict next five price variations
        .activation(Activation.IDENTITY)
        .build())
      .pretrain(false)
      .backprop(true)
      .build();

    MultiLayerNetwork model = new MultiLayerNetwork(nn_conf);
    model.init();

    model.setListeners(new ScoreIterationListener(1));
    for (int i = 0; i < NUM_EPOCHS; i++)
    {
      model.fit(training_features, training_labels);
    }

    // modello, traingin + testing features -> predizione su tutti i traingin e testing -> array di predizioni, ovvero list<int>[5]
    INDArray training_output = model.output(training_features);
    INDArray testing_output = model.output(testing_features);





  }
}
