package org.aksw.horus.algorithm;


import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.Transforms;
import org.openimaj.image.processing.face.detection.DetectedFace;
import org.openimaj.image.processing.face.detection.FaceDetector;
import org.openimaj.image.processing.face.detection.HaarCascadeDetector;

import java.io.FileInputStream;
import java.util.List;


/**
 * Created by dnes on 10/04/16.
 */
public class FaceDetectorIMAJ {

    public static void main(String[] args) {

        String imgToBeChecked = "/Users/dnes/Github/Horus/horus-core/src/main/resources/person/200x200/";
        //String imgToBeChecked = "/Users/dnes/Github/Horus/horus-core/src/main/resources/location/brazil/rj/1-praia.jpeg";

        String temp;

        for (int i=1;i<=10;i++){

            temp = imgToBeChecked;
            temp += String.valueOf(i) + ".png";

            if (existsFace(temp)){
                System.out.println(temp + ": HUMAN...");
            }else{
                System.out.println(temp + ": NOT HUMAN!");
            }


        }


    }

    public static boolean existsFace(String img){

        try{
            MBFImage image = ImageUtilities.readMBF(new FileInputStream(img));
            FaceDetector<DetectedFace,FImage> fd = new HaarCascadeDetector(40);
            List<DetectedFace> faces = fd.detectFaces (Transforms.calculateIntensity(image));
            return (faces.size() > 0);
        }catch (Exception e){
            return false;
        }

    }

}
