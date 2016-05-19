package org.aksw.horus.sift;

import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;
import mpicbg.ij.SIFT;
import mpicbg.imagefeatures.Feature;
import mpicbg.imagefeatures.FloatArray2DSIFT;
import org.aksw.horus.core.util.Global;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.util.ArrayList;


/**
 * Created by dnes on 17/05/16.
 */
public class FeatureExtraction {


    private  FloatArray2DSIFT.Param param = null;
    private  String filename;

    private static final Logger LOGGER = LoggerFactory.getLogger(FeatureExtraction.class);

    public FeatureExtraction(){

    }

    public static void main(String[] args){

        try{


            FloatArray2DSIFT.Param p = new FloatArray2DSIFT.Param();
            String ext = FilenameUtils.getExtension("/path/to/file/foo.txt");

            //extracting all features for PERSON
            File dir = new File("/Users/dnes/Github/Horus/horus-core/src/main/resources/person/");
            File[] directoryListing = dir.listFiles();
            if (directoryListing != null) {
                for (File d : directoryListing) {
                    String mimetype = new MimetypesFileTypeMap().getContentType(d.getName());
                    String type = mimetype.split("/")[0];
                    if(type.equals("image")){

                        FeatureExtraction fe = new FeatureExtraction(p, d.getAbsolutePath());
                        ArrayList<Feature> features = fe.extractFeatures();

                        Global.getInstance().serializeObject(features, "/Users/dnes/Github/Horus/features/person/", d.getName());

                        //for (Feature f: features){

                        //}
                    }


                }
            }







        }catch (Exception e){

            LOGGER.error(e.toString());

        }


    }


    public FeatureExtraction(FloatArray2DSIFT.Param p, String filename)
    {
        this.param = p;
        this.filename  = filename;

    }

    public ArrayList<Feature> extractFeatures() throws Exception {
        ImagePlus im = IJ.openImage(filename);
        ImageProcessor ip = im.getProcessor();
        ArrayList<Feature> feat = new ArrayList<>();
        SIFT sift = new SIFT(new FloatArray2DSIFT(param));
        sift.extractFeatures(ip, feat);
        return feat;
    }





}
