package org.aksw.horus.core.util;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

import java.io.File;

public class CSV2Arff {
    /**
     * takes 2 arguments:
     * - CSV input file
     * - ARFF output file
     */
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("\nUsage: CSV2Arff <input.csv> <output.arff>\n");
        //    System.exit(1);
        }
        //convert(args[0], args[1]);
        //convert("/Users/dnes/Github/Horus/horus-core/src/main/resources/training-data/pos/LOGO_alfaromeo.sift.csv", "/Users/dnes/Downloads/out.arff");
    }

    public static void convert(String in, String out) throws Exception{

        System.out.println("converting from CSV to ARFF...");
        System.out.println("in: " + in);
        System.out.println("out: " + out);
        // load CSV
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File(in));
        Instances data = loader.getDataSet();

        // save ARFF
        ArffSaver saver = new ArffSaver();
        saver.setInstances(data);
        saver.setFile(new File(out));
        saver.setDestination(new File(out));
        saver.writeBatch();
    }
}