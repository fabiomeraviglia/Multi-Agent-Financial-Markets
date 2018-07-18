import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

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
private static List<Float> KStandardDeviations = new ArrayList<Float>() {{
  add(-12.00f); add(- 6.00f); add(- 3.00f); add(- 2.00f); add(- 1.00f);
  add(- 0.80f); add(- 0.60f); add(- 0.40f); add(- 0.20f); add(- 0.10f);
  add(- 0.05f); add(  0.05f); add(  0.10f); add(  0.20f); add(  0.40f);
  add(  0.60f); add(  0.80f); add(  1.00f); add(  2.00f); add(  3.00f);
  add(  6.00f); add( 12.00f); }};

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

private static List<Integer> getClosePrices(
  List<String> csvRows,
  int position,
  boolean header)
{
  List<Integer> closePrices = new ArrayList<>();
  csvRows = header ? csvRows.subList(1, csvRows.size()) : csvRows;
  for (String r : csvRows)
  {
    closePrices.add((int) (Float.parseFloat(r.split(",")[position]) * 1000));
  }

  return  closePrices;
}

private static List<Float> convertToPriceVariations(
  List<Integer> closePrices)
{
  List<Float> priceVariations = new ArrayList<>();
  for (int i = 1; i < closePrices.size(); i++)
  {
    priceVariations.add((float) (closePrices.get(i) - closePrices.get(i - 1)));
  }

  return priceVariations;
}

private static float stdDev(
  float[] dataVector)
{
  float sum = 0.0f;
  for (int i = 0; i < dataVector.length; i++)
    sum += dataVector[i];
  float avg = sum/dataVector.length;

  float error2sum = 0.0f;
  for (int i = 0; i < dataVector.length; i++)
    error2sum += (float)Math.pow(dataVector[i] - avg, 2);

  return (float)Math.sqrt(error2sum/(dataVector.length - 1));
}

private static float[] classifyInStdDevIncrements(
  float priceVariation,
  float seriesStdDev)
{
  float[] ret = new float[KStandardDeviations.size()+1];
  for (int i = 0; i < ret.length; i++)
  {
    ret[i] = 0.0f;
  }
  if (priceVariation/seriesStdDev < KStandardDeviations.get(0))
  {
    ret[0] = 1.0f;
    return ret;
  }
  if (priceVariation/seriesStdDev > KStandardDeviations.get(KStandardDeviations.size()-1))
  {
    ret[KStandardDeviations.size()] = 1.0f;
    return ret;
  }
  for (int i=1; i < KStandardDeviations.size(); i++)
  {
    if(   priceVariation/seriesStdDev > KStandardDeviations.get(i-1)
      && priceVariation/seriesStdDev < KStandardDeviations.get(i))
    {
      ret[i] = 1.0f;
      return ret;
    }
  }
  return ret;
}

private static INDArray[] prepareFeaturesAndLables(
  List<Float> priceVariations,
  float trainingPercentage,
  int featuresSize
)
{
  float[] data = new float[priceVariations.size()];
  for (int i = 0; i < data.length; i++) data[i] = priceVariations.get(i);

  int trainingSize = (int)((data.length - featuresSize) * trainingPercentage);
  int testingSize = data.length - featuresSize - trainingSize;
  float[][] featuresDataTraining = new float[trainingSize][0];
  float[][] labelsDataTraining = new float[trainingSize][0];
  float[][] featuresDataTesting = new float[testingSize][0];
  float[][] labelsDataTesting = new float[testingSize][0];

  for (int i = 0; i < trainingSize + testingSize; i++)
  {
    float[] featuresVector = new float[featuresSize];
    for (int j = 0; j < featuresSize; j++) featuresVector[j] = data[i + j];

    float[] labelVector = classifyInStdDevIncrements(
      data[i + featuresSize],
      stdDev(featuresVector));

    if(i < trainingSize)
    {
      featuresDataTraining[i] = featuresVector;
      labelsDataTraining[i] = labelVector;
    }
    else
    {
      featuresDataTesting[i - trainingSize] = featuresVector;
      labelsDataTesting[i - trainingSize] = labelVector;
    }
  }

  INDArray trf = Nd4j.create(featuresDataTraining);
  INDArray trl = Nd4j.create(labelsDataTraining);
  INDArray tsf = Nd4j.create(featuresDataTesting);
  INDArray tsl = Nd4j.create(labelsDataTesting);
  return new INDArray[]{trf, trl, tsf, tsl};

}
public static void main(String[] args) {
  Simulation sim = new  Simulation(10);
  sim.initialize();

  for(int i = 0;i<20;i++) sim.nextTurn();
  sim.nextTurn();
  sim.nextTurn();

}
public static void main2(String[] args)
{


  float TRAINING_PERCENTAGE = 0.001f;
  int NUM_EPOCHS = 100000;
  int FEATURE_SIZE = 6;

  List<String> APPLCsvRows = readAPPLData();

  List<Integer> closePrices = getClosePrices(APPLCsvRows, 4, true);

  List<Float> priceVariations = convertToPriceVariations(closePrices);

  INDArray[] features_and_labels = prepareFeaturesAndLables(
    priceVariations, TRAINING_PERCENTAGE, FEATURE_SIZE);

  INDArray training_features = features_and_labels[0];
  INDArray training_labels = features_and_labels[1];
  INDArray testing_features = features_and_labels[2];
  INDArray testing_labels = features_and_labels[3];

  MultiLayerConfiguration nn_conf = new NeuralNetConfiguration.Builder()
    .weightInit(WeightInit.XAVIER)
    .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
    .updater(new Sgd(0.05))
    .list()
    .layer(0, new DenseLayer.Builder()
      .nIn(FEATURE_SIZE)
      .nOut((FEATURE_SIZE + KStandardDeviations.size() + 1)/2)
      .activation(Activation.SIGMOID)
      .build())
    /*.layer(1, new GravesLSTM.Builder()
      .nIn((FEATURE_SIZE + KStandardDeviations.size() + 1)/2)
      .nOut((int)((KStandardDeviations.size() + 1)*1.5))
      .activation(Activation.SIGMOID)
      .build())*/
    .layer(1, new OutputLayer.Builder(LossFunctions.LossFunction.RECONSTRUCTION_CROSSENTROPY)
      //.nIn((int)((KStandardDeviations.size() + 1)*1.5))
      .nIn((FEATURE_SIZE + KStandardDeviations.size() + 1)/2)
      .nOut(KStandardDeviations.size() + 1)
      .activation(Activation.SOFTMAX)
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

  INDArray training_output = model.output(training_features);
  INDArray testing_output = model.output(testing_features);





}
}