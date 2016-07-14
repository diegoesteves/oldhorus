package org.aksw.horus.core;


import mpicbg.imagefeatures.Feature;
import org.aksw.horus.core.util.Global;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by dnes on 20/05/16.
 */
public class HorusModel {

    private static final Logger LOGGER = LoggerFactory.getLogger(HorusModel.class);

    public HorusModel(){

    }

    public static void main(String[] args) {


        PrintWriter writer = null;


        try {

            writer = new PrintWriter("sift_person.txt", "UTF-8");
            writer.println("@relation sift-person");
            writer.println("");
            writer.println("@attribute ");


            writer.println("");

            //creating dataset for PERSON
            File dir = new File("/Users/dnes/Github/Horus/features/person/");
            File[] directoryListing = dir.listFiles();
            if (directoryListing != null) {
                writer.println("@data");
                for (File d : directoryListing) {
                    String ext = FilenameUtils.getExtension(d.getAbsolutePath());
                    if (ext.equals("ser")) {

                        ArrayList<Feature> features = Global.getInstance().deserializeFeatures(d);
                        for (Feature f: features){



                        }
                    }
                }
            }


        } catch (Exception e) {

        } finally {
            if (writer != null)
                writer.close();
        }

    }


}
