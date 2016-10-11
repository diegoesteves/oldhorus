package org.aksw.horus.sift;

import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;
import mpicbg.ij.SIFT;
import mpicbg.imagefeatures.Feature;
import mpicbg.imagefeatures.FloatArray2DSIFT;
import org.aksw.horus.core.util.CSV2Arff;
import org.aksw.horus.core.util.Global;
import org.apache.commons.io.FilenameUtils;
import org.bytedeco.javacpp.opencv_features2d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.TextDirectoryLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.util.ArrayList;

import static java.lang.System.exit;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;

//import org.opencv.core.*;
//import org.opencv.features2d.DescriptorExtractor;
//import org.opencv.features2d.DescriptorMatcher;
//import org.opencv.features2d.FeatureDetector;
//import org.opencv.imgcodecs.Imgcodecs;
//import static org.opencv.core.Core.KMEANS_PP_CENTERS;


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

            //testWeka("/Users/dnes/Downloads/text_example/text_example/class1");


            //exit(0);

            extractFeatures2("/Users/dnes/Github/Horus/horus-core/src/main/resources/training-data/pos");

            exit(0);

            String filename1 = "~/Github/Horus/horus-core/src/main/resources/location/usa/beverly_hills/1-cidade.jpeg";
            String filename2 = "~/Github/Horus/horus-core/src/main/resources/location/usa/beverly_hills/2-casa.jpeg";

            int ret;
            ret = compareFeature(filename1, filename2);

            if (ret > 0) {
                System.out.println("Two images are same.");
            } else {
                System.out.println("Two images are different.");
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



    /**
     * Compare that two images is similar using feature mapping
     * @author minikim
     * @param filename1 - the first image
     * @param filename2 - the second image
     * @return integer - count that has the similarity within images
     */
    public static int compareFeature(String filename1, String filename2) {
    /*

        int retVal = 0;
        long startTime = System.currentTimeMillis();

        //System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // Load images to compare
        Mat img1 = imread(filename1, CV_LOAD_IMAGE_COLOR);
        Mat img2 = imread(filename2, CV_LOAD_IMAGE_COLOR);

        // Declare key point of images
        MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
        MatOfKeyPoint keypoints2 = new MatOfKeyPoint();
        Mat descriptors1 = new Mat();
        Mat descriptors2 = new Mat();

        // Definition of ORB key point detector and descriptor extractors
        FeatureDetector detector = FeatureDetector.create(FeatureDetector.ORB);
        DescriptorExtractor extractor = DescriptorExtractor.create(DescriptorExtractor.ORB);

        // Detect key points
        detector.detect(img1, keypoints1);
        detector.detect(img2, keypoints2);

        // Extract descriptors
        extractor.compute(img1, keypoints1, descriptors1);
        extractor.compute(img2, keypoints2, descriptors2);

        //img1 = drawKeypoints(img1,kp,flags=DRAW_MATCHES_FLAGS_DRAW_RICH_KEYPOINTS)
        //cv2.imwrite('sift_keypoints.jpg',img)

        // Definition of descriptor matcher
        DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);

        // Match points of two images
        MatOfDMatch matches = new MatOfDMatch();
//  System.out.println("Type of Image1= " + descriptors1.type() + ", Type of Image2= " + descriptors2.type());
//  System.out.println("Cols of Image1= " + descriptors1.cols() + ", Cols of Image2= " + descriptors2.cols());

        // Avoid to assertion failed
        // Assertion failed (type == src2.type() && src1.cols == src2.cols && (type == CV_32F || type == CV_8U)
        if (descriptors2.cols() == descriptors1.cols()) {
            System.out.println("cols size equal");
            matcher.match(descriptors1, descriptors2, matches);

            // Check matches of key points
            DMatch[] match = matches.toArray();
            double max_dist = 0; double min_dist = 100;

            for (int i = 0; i < descriptors1.rows(); i++) {
                double dist = match[i].distance;
                if( dist < min_dist ) min_dist = dist;
                if( dist > max_dist ) max_dist = dist;
            }
            System.out.println("max_dist=" + max_dist + ", min_dist=" + min_dist);

            // Extract good images (distances are under 10)
            for (int i = 0; i < descriptors1.rows(); i++) {
                if (match[i].distance <= 10) {
                    retVal++;
                }
            }
            System.out.println("matching count=" + retVal);
        }

        long estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println("estimatedTime=" + estimatedTime + "ms");
        System.out.println("return val=" + retVal);

        return retVal;


*/

    return 0;

    }


    /***
     * requires OpenCV 2.4.9 otherwise it does not work
     *
     * @param path
     * @throws Exception
     */
    private static void extractFeatures2(String path) throws Exception{

        FloatArray2DSIFT.Param p = new FloatArray2DSIFT.Param();
        String ext = FilenameUtils.getExtension("/path/to/file/foo.txt");

        CSV2Arff converter = new CSV2Arff();

        //extracting all features for PERSON
        File dir = new File(path);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File d : directoryListing) {
                String mimetype = new MimetypesFileTypeMap().getContentType(d.getName());
                String type = mimetype.split("/")[0];
                if(type.equals("image")){

                    FeatureExtraction fe = new FeatureExtraction(p, d.getAbsolutePath());
                    ArrayList<Feature> features = fe.extractFeatures();


                    //Global.getInstance().serializeObject(features, path , d.getName());
                    Global.getInstance().serializeSIFTFeatures(features, path , d.getName());
                    CSV2Arff.convert(path + "/" + d.getName() + ".sift.csv", path + "/" + d.getName() + ".arff");

                    //for (Feature f: features){

                    //}
                }


            }
        }

    }




    private static void testWeka(String path) throws Exception{
        // convert the directory into a dataset
        TextDirectoryLoader loader = new TextDirectoryLoader();
        loader.setDirectory(new File(path));
        Instances dataRaw = loader.getDataSet();
        //System.out.println("\n\nImported data:\n\n" + dataRaw);

        // apply the StringToWordVector
        // (see the source code of setOptions(String[]) method of the filter
        // if you want to know which command-line option corresponds to which
        // bean property)
        StringToWordVector filter = new StringToWordVector();
        filter.setInputFormat(dataRaw);
        Instances dataFiltered = Filter.useFilter(dataRaw, filter);
        //System.out.println("\n\nFiltered data:\n\n" + dataFiltered);

        // train J48 and output model
        J48 classifier = new J48();
        classifier.buildClassifier(dataFiltered);
        System.out.println("\n\nClassifier model:\n\n" + classifier);
    }


    private static void bof(String trainingDir) throws Exception {

        long startTime = System.currentTimeMillis();

        //System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        FloatArray2DSIFT.Param detector = new FloatArray2DSIFT.Param();

        /* to store the current input image */
        Mat input;

        /* To store the keypoints that will be extracted by SIFT */
        ArrayList<KeyPoint> keypoints;

        /* To store the SIFT descriptor of current image */
        Mat descriptor;

        /* To store all the descriptors that are extracted from all the images. */
        Mat featuresUnclustered;


        File dir = new File(trainingDir);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File d : directoryListing) {
                String mimetype = new MimetypesFileTypeMap().getContentType(d.getName());
                String type = mimetype.split("/")[0];
                if (type.equals("image")) {

                    input = imread(d.getAbsolutePath(), CV_LOAD_IMAGE_GRAYSCALE);

                    FeatureExtraction fe = new FeatureExtraction(detector, d.getAbsolutePath());
                    ArrayList<Feature> features = fe.extractFeatures();


                    /* put the all feature descriptors in a single Mat object */
                   // featuresUnclustered.push_back(descriptor);



                }
            }
        }



        int dictionarySize = 200;
        int retries = 1;
        int flags = KMEANS_PP_CENTERS;
        TermCriteria tc = new TermCriteria(TermCriteria.MAX_ITER, 100, 0.001);


        BOWKMeansTrainer bowkMeansTrainer =
                new BOWKMeansTrainer(dictionarySize, tc, retries, flags);

       // Mat dictionary = bowkMeansTrainer.cluster(featuresUnclustered);
        //opencv_core.FileStorage fs = new FileStorage("dictionary.yml", FileStorage.WRITE);
        //write(fs , "dictionary", dictionary);
//http://stackoverflow.com/questions/18877903/read-yml-file-with-opencv
        //fs.release();







    }


}
