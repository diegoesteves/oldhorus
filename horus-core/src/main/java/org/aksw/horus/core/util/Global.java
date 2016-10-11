package org.aksw.horus.core.util;

import mpicbg.imagefeatures.Feature;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by dnes on 18/05/16.
 */
public class Global {

    private static Global singleton = null;

    protected Global() {
        // Exists only to defeat instantiation.
    }

    public enum NERType {
        PER(1), LOC(2), ORG(3);
        private final int value;

        private NERType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum NLPToolkit {
        STANFORD(1);
        private final int value;

        private NLPToolkit(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }


    private static Logger LOGGER = Logger.getLogger(Global.class);

    public static Global getInstance() {
        if (singleton == null) {
            singleton = new Global();
        }
        return singleton;
    }


    public static boolean serializeSIFTFeatures(ArrayList<Feature> features, String filePath, String fileName) throws Exception{

        String fullName = new StringBuilder()
                .append(filePath)
                .append("/")
                .append(fileName)
                .append(".sift.csv").toString();

        File f = new File(fullName);
        if (!f.exists()) {
            f.createNewFile();
        }


        PrintWriter out = new PrintWriter(fullName);

        String header = "scale,orientation,location_0,location_1,";
        for (int i=0; i<features.get(0).descriptor.length; i++) {
            header+= "des_" + i + ",";
        }
        out.println(header.substring(0, header.length()-1));

        for (Feature feat: features) {
            String line = feat.scale + "," +
                    feat.orientation + "," +
                    feat.location[0] + "," +
                    feat.location[1] + ",";


            for (int i=0; i<feat.descriptor.length; i++) {
                line+= feat.descriptor[i] + ",";
            }
            System.out.println(line);

            out.println(line.substring(0,line.length()-1));
        }
        out.close();
        LOGGER.debug("Serialized sift features is saved in " + filePath + fileName);
        return true;

    }


    /**
     * serializes an object into a specific path
     *
     * @param e
     * @param filePath
     * @param fileName the default file type is ".ser", no need to explicitly define it
     * @throws Exception
     */
    public static boolean serializeObject(Object e, String filePath, String fileName) throws Exception {

        try {

            String fullName = new StringBuilder()
                    .append(filePath)
                    .append("/")
                    .append(fileName)
                    .append(".ser").toString();

            File f = new File(fullName);
            if (!f.exists()) {
                f.createNewFile();
            }

            FileOutputStream fileOut =
                    new FileOutputStream(fullName, false);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(e);
            out.close();
            fileOut.close();
            LOGGER.debug("Serialized data is saved in " + filePath + fileName);
            return true;
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            return false;
        }
    }

    public static ArrayList<Feature> deserializeFeatures(File file) throws Exception {

        try {

            ArrayList<Feature> features = null;
            FileInputStream inputFileStream = new FileInputStream(file.getAbsoluteFile());
            ObjectInputStream objectInputStream = new ObjectInputStream(inputFileStream);
            features = (ArrayList<Feature>) objectInputStream.readObject();
            objectInputStream.close();
            inputFileStream.close();
            return features;

        }catch (Exception e){
            LOGGER.error(e.toString());
            return null;
        }
    }



}
