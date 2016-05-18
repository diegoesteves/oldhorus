package org.aksw.horus.sift;

import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;
import mpicbg.ij.SIFT;
import mpicbg.imagefeatures.Feature;
import mpicbg.imagefeatures.FloatArray2DSIFT;
import org.aksw.horus.core.util.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

            FeatureExtraction fe = new FeatureExtraction(p, "/Users/dnes/Github/Horus/horus-core/src/main/resources/person/1-Eggon-Silva-Luto-O-Correio-do-Povo-400x250.jpg");
            ArrayList<Feature> features = fe.extractFeatures();

            for (Feature f: features){
                fe.serializeFeatures("horus","test.jpg",f);
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


    public void serializeFeatures(String prefix, String imageName, Feature f) throws Exception {

        final String name = prefix == null ? "features" : prefix + ".features";


        Global.getInstance().serializeObject(f, "\\Users\\dnes\\Github", "test");





        String ret = new StringBuilder()
                .append( "features.ser/" )
                .append(imageName)
                .append(name).toString();




    }



}
