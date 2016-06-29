package org.aksw.horus;

import java.io.File;

/**
 * Created by Diego on 6/25/2016.
 */
public class Temp {


    public static void main(String[] args) {

        try {

            String prefix = "FLAG_";
            File dir = new File("C:\\DNE5\\github\\Horus\\horus-core\\src\\main\\resources\\training-data\\flags");
            File[] directoryListing = dir.listFiles();
            if (directoryListing != null) {
                for (File d : directoryListing) {
                    if (d.isFile()) {
                        d.renameTo(new File(d.getAbsolutePath() + prefix + d.getName()));
                    }
                }
            }



                } catch (Exception e) {
            System.out.print(e.toString());
        }

    }
}
