package org.aksw.horus.sift;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_features2d;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.features2d.*;
import org.opencv.highgui.Highgui;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

import static org.opencv.core.Core.KMEANS_PP_CENTERS;
import static org.opencv.core.TermCriteria.MAX_ITER;

/**
 * Created by dnes on 16/09/16.
 * Works with OpenCV Java wrapper 2.4.9
 * http://dummyscodes.blogspot.de/2015/12/using-siftsurf-for-object-recognition.html
 * https://github.com/kinathru/SURFDetector
 */
public class FeatureExtraction249 {

    public static void main(String[] args){
        try{
            extract_features_249();
        }catch (Exception e){
            System.out.print(e.toString());
        }

    }

    public FeatureExtraction249(){

    }

    private static void myExtractionTest_249(String pathTraining) throws Exception{


        //To store the SIFT descriptor of current image
        Mat descriptor;
        //To store all the descriptors that are extracted from all the images.
        Mat featuresUnclustered = new Mat();

        File dir = new File(pathTraining);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File d : directoryListing) {
                String mimetype = new MimetypesFileTypeMap().getContentType(d.getName());
                String type = mimetype.split("/")[0];
                if (type.equals("image")) {

                    Mat input = Highgui.imread(d.getAbsolutePath(), Highgui.CV_LOAD_IMAGE_GRAYSCALE);

                    //The SIFT feature extractor and descriptor
                    MatOfKeyPoint objectKeyPoints = new MatOfKeyPoint();
                    FeatureDetector featureDetector = FeatureDetector.create(FeatureDetector.SIFT);
                    System.out.println("Detecting key points...");
                    featureDetector.detect(input, objectKeyPoints);
                    KeyPoint[] keypoints = objectKeyPoints.toArray();
                    System.out.println(keypoints);

                    MatOfKeyPoint objectDescriptors = new MatOfKeyPoint();
                    DescriptorExtractor descriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.SIFT);
                    System.out.println("Computing descriptors...");
                    descriptorExtractor.compute(input, objectKeyPoints, objectDescriptors);

                    featuresUnclustered.push_back(objectDescriptors);

                }
            }
        }

        int dictionarySize=200;
        //define Term Criteria
        opencv_core.TermCriteria tc = new opencv_core.TermCriteria(MAX_ITER,100,0.001);
        //retries number
        int retries=1;
        //necessary flags
        int flags=KMEANS_PP_CENTERS;

        /**
         * essa parte do codigo (BOWKmeansTrainer) so no OpenCV 3
         */

        //Create the BoW (or BoF) trainer
        opencv_features2d.BOWKMeansTrainer bowTrainer =
                new opencv_features2d.BOWKMeansTrainer(dictionarySize,tc,retries,flags);

        /**
         * ai da problema de incompatibilidade, outras interfaces, como python, podem funcionar
            //cluster the feature vectors
            Mat dictionary = bowTrainer.cluster(featuresUnclustered);
         */



    }

    private static void extract_features_249() throws Exception{

        //ATTENTION: BOWKMeansTrainer and BOWTrainer are not available at Java wrapper 2.4.x

        String os = System.getProperty("os.name");
        String bitness = System.getProperty("sun.arch.data.model");

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);


        String bookObject = "/Users/dnes/Github/Horus/bookobject.jpg";
        String bookScene = "/Users/dnes/Github/Horus/bookscene.jpg";


        System.out.println("Started....");
        System.out.println("Loading images...");
        Mat objectImage = Highgui.imread(bookObject, Highgui.CV_LOAD_IMAGE_COLOR);
        Mat sceneImage = Highgui.imread(bookScene, Highgui.CV_LOAD_IMAGE_COLOR);

        MatOfKeyPoint objectKeyPoints = new MatOfKeyPoint();
        FeatureDetector featureDetector = FeatureDetector.create(FeatureDetector.SIFT);
        System.out.println("Detecting key points...");
        featureDetector.detect(objectImage, objectKeyPoints);
        KeyPoint[] keypoints = objectKeyPoints.toArray();
        System.out.println(keypoints);

        MatOfKeyPoint objectDescriptors = new MatOfKeyPoint();
        DescriptorExtractor descriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.SIFT);
        System.out.println("Computing descriptors...");
        descriptorExtractor.compute(objectImage, objectKeyPoints, objectDescriptors);

        // Create the matrix for output image.
        Mat outputImage = new Mat(objectImage.rows(), objectImage.cols(), Highgui.CV_LOAD_IMAGE_COLOR);
        Scalar newKeypointColor = new Scalar(255, 0, 0);

        System.out.println("Drawing key points on object image...");
        Features2d.drawKeypoints(objectImage, objectKeyPoints, outputImage, newKeypointColor, 0);

        // Match object image with the scene image
        MatOfKeyPoint sceneKeyPoints = new MatOfKeyPoint();
        MatOfKeyPoint sceneDescriptors = new MatOfKeyPoint();
        System.out.println("Detecting key points in background image...");
        featureDetector.detect(sceneImage, sceneKeyPoints);
        System.out.println("Computing descriptors in background image...");
        descriptorExtractor.compute(sceneImage, sceneKeyPoints, sceneDescriptors);

        Mat matchoutput = new Mat(sceneImage.rows() * 2, sceneImage.cols() * 2, Highgui.CV_LOAD_IMAGE_COLOR);
        Scalar matchestColor = new Scalar(0, 255, 0);

        List<MatOfDMatch> matches = new LinkedList<MatOfDMatch>();
        DescriptorMatcher descriptorMatcher = DescriptorMatcher.create(DescriptorMatcher.FLANNBASED);
        System.out.println("Matching object and scene images...");
        descriptorMatcher.knnMatch(objectDescriptors, sceneDescriptors, matches, 2);

        System.out.println("Calculating good match list...");
        LinkedList<DMatch> goodMatchesList = new LinkedList<DMatch>();

        float nndrRatio = 0.7f;

        for (int i = 0; i < matches.size(); i++) {
            MatOfDMatch matofDMatch = matches.get(i);
            DMatch[] dmatcharray = matofDMatch.toArray();
            DMatch m1 = dmatcharray[0];
            DMatch m2 = dmatcharray[1];

            if (m1.distance <= m2.distance * nndrRatio) {
                goodMatchesList.addLast(m1);

            }
        }

        System.out.println("matches = " + goodMatchesList.size());
        if (goodMatchesList.size() >= 6) {
            System.out.println("Object Found!!!");

            List<KeyPoint> objKeypointlist = objectKeyPoints.toList();
            List<KeyPoint> scnKeypointlist = sceneKeyPoints.toList();

            LinkedList<Point> objectPoints = new LinkedList<>();
            LinkedList<Point> scenePoints = new LinkedList<>();

            for (int i = 0; i < goodMatchesList.size(); i++) {
                objectPoints.addLast(objKeypointlist.get(goodMatchesList.get(i).queryIdx).pt);
                scenePoints.addLast(scnKeypointlist.get(goodMatchesList.get(i).trainIdx).pt);
            }

            MatOfPoint2f objMatOfPoint2f = new MatOfPoint2f();
            objMatOfPoint2f.fromList(objectPoints);
            MatOfPoint2f scnMatOfPoint2f = new MatOfPoint2f();
            scnMatOfPoint2f.fromList(scenePoints);

            Mat homography = Calib3d.findHomography(objMatOfPoint2f, scnMatOfPoint2f, Calib3d.RANSAC, 3);

            Mat obj_corners = new Mat(4, 1, CvType.CV_32FC2);
            Mat scene_corners = new Mat(4, 1, CvType.CV_32FC2);

            obj_corners.put(0, 0, new double[]{0, 0});
            obj_corners.put(1, 0, new double[]{objectImage.cols(), 0});
            obj_corners.put(2, 0, new double[]{objectImage.cols(), objectImage.rows()});
            obj_corners.put(3, 0, new double[]{0, objectImage.rows()});

            System.out.println("Transforming object corners to scene corners...");
            Core.perspectiveTransform(obj_corners, scene_corners, homography);

            Mat img = Highgui.imread(bookScene, Highgui.CV_LOAD_IMAGE_COLOR);

            Core.line(img, new Point(scene_corners.get(0, 0)), new Point(scene_corners.get(1, 0)), new Scalar(0, 255, 0), 4);
            Core.line(img, new Point(scene_corners.get(1, 0)), new Point(scene_corners.get(2, 0)), new Scalar(0, 255, 0), 4);
            Core.line(img, new Point(scene_corners.get(2, 0)), new Point(scene_corners.get(3, 0)), new Scalar(0, 255, 0), 4);
            Core.line(img, new Point(scene_corners.get(3, 0)), new Point(scene_corners.get(0, 0)), new Scalar(0, 255, 0), 4);

            System.out.println("Drawing matches image...");
            MatOfDMatch goodMatches = new MatOfDMatch();
            goodMatches.fromList(goodMatchesList);

            Features2d.drawMatches(objectImage, objectKeyPoints, sceneImage, sceneKeyPoints, goodMatches, matchoutput, matchestColor, newKeypointColor, new MatOfByte(), 2);

            Highgui.imwrite("outputImage.jpg", outputImage);
            Highgui.imwrite("matchoutput.jpg", matchoutput);
            Highgui.imwrite("img.jpg", img);
        } else {
            System.out.println("Object Not Found");
        }

        System.out.println("Ended....");

    }


    }

