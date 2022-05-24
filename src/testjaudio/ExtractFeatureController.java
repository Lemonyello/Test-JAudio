/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package testjaudio;

import jAudioFeatureExtractor.ACE.DataTypes.Batch;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JOptionPane;
import static testjaudio.TestJAudio.fe;
 import weka.core.Instance;
 import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;
 import weka.core.Attribute;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.lazy.IBk;

/**
 *
 * @author Admin
 */
public class ExtractFeatureController {
    
    void AddClassToARFF() throws IOException {
        String filePath = "file\\extracted_val.arff";
        ArffLoader loader = new ArffLoader();
        loader.setSource(new File(
                filePath));
        Instances data = loader.getDataSet();//get instances object
        List<String> val = Arrays.asList("classical", "hiphop", "pop", "rock", "reggae", "jazz");
        data.insertAttributeAt(new Attribute("class", val), data.numAttributes());     
        
        String fileSave = "file\\music.arff";

    // save ARFF
    ArffSaver saverARFF = new ArffSaver();
    saverARFF.setInstances(data);
    saverARFF.setFile(new File(fileSave));
    saverARFF.writeBatch();
    }
    
    public void MFCCjAudio(File input) throws Exception {
        File[] files = new File[1];
        files[0] = input;
        
        String desVal = "file\\extracted_val.arff";
        String desDef = "file\\extracted_def.xml";
        String featureFile = "file\\features.xml";

        Batch batch = new Batch();
        batch.setRecordings(files);
        batch.setWindowSize(2048);
        batch.setWindowOverlap(512);
        batch.setSamplingRate(22050);
        batch.setNormalise(false);
        batch.setPerWindow(false);
        batch.setOverall(true);
        batch.setDestinationFK(desDef);
        batch.setDestinationFV(desVal);
        batch.setOutputType(1);
        batch.setSettings(2048, 0.25, 22050, false, false, true, 1);
        jAudioFeatureExtractor.DataModel dm = new jAudioFeatureExtractor.DataModel(featureFile, null);
            OutputStream valsavepath = new FileOutputStream(desVal);
            OutputStream defsavepath = new FileOutputStream(desDef);
            dm.featureKey = defsavepath;
            dm.featureValue = valsavepath;
        batch.setDataModel(dm);
        HashMap<String,Boolean> active = new HashMap<>();
        HashMap<String,String[]> attributes = new HashMap<>();
        LinkedList<String> tmpAttributes = new LinkedList<>();
        for (int i = 0; i < dm.features.length; ++i) {
            String name = dm.features[i].getFeatureDefinition().name;
            active.put(name, false);
            int count = dm.features[i].getFeatureDefinition().attributes.length;
            for (int j = 0; j < count; ++j) {
                    try {
                            tmpAttributes.add(dm.features[i].getElement(j));
                    } catch (Exception e1) {

                    }
            }
            attributes.put(name,tmpAttributes.toArray(new String[] {}));
            tmpAttributes.clear();
        }
        active.replace("MFCC", true);
        batch.setFeatures(active, attributes);
        dm.aggregators = new jAudioFeatureExtractor.Aggregators.Aggregator[] {
        (jAudioFeatureExtractor.Aggregators.Aggregator) (dm.aggregatorMap
                    .get("Standard Deviation").clone()),
        (jAudioFeatureExtractor.Aggregators.Aggregator) (dm.aggregatorMap.get("Mean").clone()) };
        jAudioFeatureExtractor.Aggregators.Aggregator[] aggs = dm.aggregators;
        String[] names = new String[aggs.length];
        String[][] features = new String[aggs.length][];
        String[][] parameters = new String[aggs.length][];
        for(int i=0;i<aggs.length;++i){
                names[i] = aggs[i].getAggregatorDefinition().name;
                features[i] = aggs[i].getFeaturesToApply();
                parameters[i] = aggs[i].getParamaters();
        }
        batch.setAggregators(names, features, parameters);
        batch.execute();
    }
    
    void saveLoad() throws Exception {
        IBk smo2 = (IBk) weka.core.SerializationHelper
                .read("file\\music_genres_random_6.model");

        //load new dataset
        DataSource source1 = new DataSource(
                 "file\\music.arff");   
        Instances testDataset = source1.getDataSet();
        //set class index to the last attribute
        testDataset.setClassIndex(testDataset.numAttributes() - 1);
        int num = 350;
        int trueNum = 0;
        int falseNum = 0;

        for(int i=0; i<testDataset.numInstances(); i++) {
            num = i;
            //get class double value for first instance
            double actualValue = testDataset.instance(num).classValue();
            //get Instance object of first instance
            Instance newInst = testDataset.instance(num);
            //call classifyInstance, which returns a double value for the class
            double predSMO = smo2.classifyInstance(newInst);
            
            if(actualValue == predSMO)
                trueNum++;
            else
                falseNum++;

            System.out.println(i + ". Actual: " + actualValue + "----------- Predict: " + testDataset.classAttribute().value((int) predSMO));
            JOptionPane.showMessageDialog(fe.f, "Nhạc thuộc thể loại: " + testDataset.classAttribute().value((int) predSMO) + ".");
        }
    }
    
    public void Classify() throws IOException, Exception {
        AddClassToARFF();
        saveLoad();
    }
    
//    public void MFCCjAudio() throws Exception {
//        File[] files = new File[1];
//        String fileName = "E:\\Dokumente\\Study\\Semester 2 year 4\\Khai phá\\archive (1)\\Data\\genres_original\\classical\\classical.00000.wav";
//        String desVal = "E:\\Dokumente\\Study\\Semester 2 year 4\\Khai phá\\archive (1)\\Data\\extracted_val.arff";
//        String desDef = "E:\\Dokumente\\Study\\Semester 2 year 4\\Khai phá\\archive (1)\\Data\\extracted_def.xml";
//        String featureFile = "E:\\features.xml";
////        for(int i = 0; i<5; i++) {
////            files[i] = new File(fileName);
////        }
//        files[0] = new File(fileName);
//        Batch batch = new Batch();
//        batch.setRecordings(files);
//        batch.setWindowSize(2048);
//        batch.setWindowOverlap(512);
//        batch.setSamplingRate(22050);
//        batch.setNormalise(false);
//        batch.setPerWindow(false);
//        batch.setOverall(true);
//        batch.setDestinationFK(desDef);
//        batch.setDestinationFV(desVal);
//        batch.setOutputType(1);
//        batch.setSettings(2048, 0.25, 22050, false, false, true, 1);
//        DataModel dm = new DataModel(featureFile, null);
//            OutputStream valsavepath = new FileOutputStream(desVal);
//            OutputStream defsavepath = new FileOutputStream(desDef);
//            dm.featureKey = defsavepath;
//            dm.featureValue = valsavepath;
//        batch.setDataModel(dm);
//        HashMap<String,Boolean> active = new HashMap<>();
//        HashMap<String,String[]> attributes = new HashMap<>();
//        LinkedList<String> tmpAttributes = new LinkedList<>();
//        for (int i = 0; i < dm.features.length; ++i) {
//            String name = dm.features[i].getFeatureDefinition().name;
//            active.put(name, false);
//            int count = dm.features[i].getFeatureDefinition().attributes.length;
//            for (int j = 0; j < count; ++j) {
//                    try {
//                            tmpAttributes.add(dm.features[i].getElement(j));
//                    } catch (Exception e1) {
//
//                    }
//            }
//            attributes.put(name,tmpAttributes.toArray(new String[] {}));
//            tmpAttributes.clear();
//        }
//        active.replace("MFCC", true);
//        batch.setFeatures(active, attributes);
//        dm.aggregators = new Aggregator[] {
//        (Aggregator) (dm.aggregatorMap
//                    .get("Standard Deviation").clone()),
//        (Aggregator) (dm.aggregatorMap.get("Mean").clone()) };
//        Aggregator[] aggs = dm.aggregators;
//        String[] names = new String[aggs.length];
//        String[][] features = new String[aggs.length][];
//        String[][] parameters = new String[aggs.length][];
//        for(int i=0;i<aggs.length;++i){
//                names[i] = aggs[i].getAggregatorDefinition().name;
//                features[i] = aggs[i].getFeaturesToApply();
//                parameters[i] = aggs[i].getParamaters();
//        }
//        batch.setAggregators(names, features, parameters);
//        batch.execute();
//    }
}
