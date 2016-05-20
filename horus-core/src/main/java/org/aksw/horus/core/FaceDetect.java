package org.aksw.horus.core;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.*;
import org.bytedeco.javacpp.opencv_face;
import org.bytedeco.javacpp.opencv_objdetect;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacpp.opencv_imgproc;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.IntBuffer;

import static org.bytedeco.javacpp.opencv_core.CV_32SC1;
import static org.bytedeco.javacpp.opencv_face.createFisherFaceRecognizer;
import static org.bytedeco.javacpp.opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;
import org.bytedeco.javacpp.opencv_imgcodecs;



/**
 * Created by dnes on 09/04/16.
 */
public class FaceDetect {

    // Create memory for calculations
    opencv_core.CvMemStorage storage = null;

    // Create a new Haar classifier
    opencv_objdetect.CvHaarClassifierCascade classifier = null;

    // List of classifiers
    String[] classifierName = {
            "./classifiers/haarcascade_frontalface_alt.xml",
            "./classifiers/haarcascade_frontalface_alt2.xml",
            "./classifiers/haarcascade_profileface.xml" };

    /*
    public FaceDetect() {
        // Allocate the memory storage
        storage = opencv_core.CvMemStorage.create();

        // Load the HaarClassifierCascade
        classifier = new opencv_objdetect.CvHaarClassifierCascade(cvLoad(classifierName[0]));

        // Make sure the cascade is loaded
        if (classifier.isNull()) {
            System.err.println("Error loading classifier file");
        }
    }
    */

    /*
    public boolean find (FlyCapture2.Image value ){
        // Clear the memory storage which was used before
        cvClearMemStorage(storage);

        if(!classifier.isNull()){
            // Detect the objects and store them in the sequence
            opencv_core.CvSeq faces = cvHaarDetectObjects(value.getImage(), classifier,
                    storage, 1.1, 3, CV_HAAR_DO_CANNY_PRUNING);

            // Get the number of faces found.
            int total = faces.total();
            if (total > 0) {
                return true;
            }
        }
        return false;
    }
    */

    private void classify(){


        opencv_objdetect.CascadeClassifier faceDetector = new opencv_objdetect.CascadeClassifier(this.getClass()
                .getResource("/lbpcascade_frontalface.xml").getPath());

        opencv_core.Mat image = opencv_imgcodecs.imread(getClass().getResource(
                "/AverageMaleFace.jpg").getPath());

        opencv_core.MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image, faceDetections);

        System.out.println(String.format("Detected %s faces",
                faceDetections.toArray().length));


    }


    public static void main(String[] args) {
        String trainingDir = "/Users/dnes/Github/Horus/horus-core/src/main/resources/person/200x200/";
        String imgToBeChecked = "/Users/dnes/Github/Horus/horus-core/src/main/resources/person/200x200/test/1-paula_amargo.png";

        opencv_core.Mat testImage = imread(imgToBeChecked, CV_LOAD_IMAGE_GRAYSCALE);

        File root = new File(trainingDir);

        FilenameFilter imgFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                name = name.toLowerCase();
                return name.endsWith(".jpg") || name.endsWith(".pgm") || name.endsWith(".png");
            }
        };

        File[] imageFiles = root.listFiles(imgFilter);

        opencv_core.MatVector images = new opencv_core.MatVector(imageFiles.length);

        opencv_core.Mat labels = new opencv_core.Mat(imageFiles.length, 1, CV_32SC1);
        IntBuffer labelsBuf = labels.getIntBuffer();

        int counter = 0;

        for (File image : imageFiles) {
            opencv_core.Mat img = imread(image.getAbsolutePath(), CV_LOAD_IMAGE_GRAYSCALE);

            int label = Integer.parseInt(image.getName().split("\\-")[0]);

            images.put(counter, img);

            labelsBuf.put(counter, label);

            counter++;
        }

        opencv_face.FaceRecognizer faceRecognizer = createFisherFaceRecognizer();
         //opencv_face.FaceRecognizer faceRecognizer2 = createEigenFaceRecognizer();
        // FaceRecognizer faceRecognizer = createLBPHFaceRecognizer()

        faceRecognizer.train(images, labels);

        int predictedLabel = faceRecognizer.predict(testImage);

        System.out.println("Predicted label: " + predictedLabel);
    }

}