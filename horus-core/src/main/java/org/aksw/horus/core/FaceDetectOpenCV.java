package org.aksw.horus.core;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by dnes on 20/05/16.
 */
public class FaceDetectOpenCV {


    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(FaceDetectOpenCV.class);
    private static CascadeClassifier classifier;


    public FaceDetectOpenCV(){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        //this.classifier = new CascadeClassifier("/usr/local/Cellar/opencv3/3.1.0_3/share/OpenCV/haarcascades/haarcascade_frontalface_alt.xml");
        //this.classifier = new CascadeClassifier("/usr/local/Cellar/opencv3/3.1.0_3/share/OpenCV/haarcascades/haarcascade_eye.xml");
        //this.classifier = new CascadeClassifier("/usr/local/Cellar/opencv3/3.1.0_3/share/OpenCV/haarcascades/haarcascade_frontalface_alt2.xml"); //better


        //this.classifier = new CascadeClassifier("/usr/local/Cellar/opencv3/3.1.0_3/share/OpenCV/haarcascades/haarcascade_frontalface_alt_tree.xml");
        //this.classifier = new CascadeClassifier("/usr/local/Cellar/opencv3/3.1.0_3/share/OpenCV/haarcascades/haarcascade_frontalface_default.xml"); //better
        this.classifier = new CascadeClassifier("C:\\DNE5\\libraries\\opencv\\3.1.0\\opencv\\sources\\data\\haarcascades_cuda\\haarcascade_frontalface_default.xml"); //better






    }

    public boolean faceDetected(File img){
        Mat image = Imgcodecs.imread(img.getAbsolutePath());
        MatOfRect faceDetections = new MatOfRect();
        this.classifier.detectMultiScale(image, faceDetections);
        return ((faceDetections.toArray().length >= 1) ? true : false);
    }

    private void detectFace(String imgPath) {

        File dir = new File(imgPath);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File d : directoryListing) {


                //String mimetype = new MimetypesFileTypeMap().getContentType(d.getName());
                //String type = mimetype.split("/")[0];
                //if (type.equals("image")) {
                if (d.getName().endsWith(".jpg") || d.getName().endsWith(".pgm") || d.getName().endsWith(".png")){

                    Mat image = Imgcodecs.imread(d.getAbsolutePath());
                    MatOfRect faceDetections = new MatOfRect();
                    this.classifier.detectMultiScale(image, faceDetections);
                    System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));

                }

            }
        }
    }


    public void run(String imgpath) {




      //  imgpath = "/Users/dnes/Github/Horus/horus-core/src/main/resources/person/1-Eggon-Silva-Luto-O-Correio-do-Povo-400x250.jpg";
        imgpath = "/Users/dnes/Github/Horus/horus-core/src/main/resources/location/brazil/rj/1-praia.jpeg";




        /*
        for (Rect rect : faceDetections.toArray()) {
            Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 255, 0));
        }

        // Change this path as appropriate for your system.
        String filename = "/Users/franco/ouput.png";
        System.out.println(String.format("Done. Writing %s", filename));
        Imgcodecs.imwrite(filename, image);
        */
    }

    public static void main (String[] args) {
        FaceDetectOpenCV fd = new FaceDetectOpenCV();
        //fd.detectFace("/Users/dnes/Github/Horus/horus-core/src/main/resources/person/"); //0.90
        fd.detectFace("C:\\DNE5\\github\\Horus\\horus-core\\src\\main\\resources\\person"); //0.90
    }

    public void test(String[] args) {


        try{

            LOGGER.debug("process started");

            System.out.println("Welcome to OpenCV " + Core.VERSION);
            System.out.println(System.getProperty("java.library.path"));
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            Mat m  = Mat.eye(3, 3, CvType.CV_8UC1);
            System.out.println("m = " + m.dump());

        }catch (Exception e){

        }

    }




}
