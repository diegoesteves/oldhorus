package org.aksw.horus.core.util;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by dnes on 13/04/16.
 */
public class ImageManipulation {

    /*    private static BufferedImage toBufferedImage(Image src) {
        int w = src.getWidth(null);
        int h = src.getHeight(null);
        int type = BufferedImage.TYPE_INT_RGB;  // other options
        BufferedImage dest = new BufferedImage(w, h, type);
        Graphics2D g2 = dest.createGraphics();
        g2.drawImage(src, 0, 0, null);
        g2.dispose();
        return dest;
    }
*/

  /*
   private static void save(BufferedImage image, String ext) {
        String fileName = "savingAnImage";
        File file = new File(fileName + "." + ext);
        try {
            ImageIO.write(image, ext, file);  // ignore returned boolean
        } catch(IOException e) {
            System.out.println("Write error for " + file.getPath() +
                    ": " + e.getMessage());
        }
    }
*/


    public ImageManipulation(){

    }


    public static void saveImage(String imageUrl, String path, String destinationFile) throws Exception {
        URL url = new URL(imageUrl);

        URLConnection uc;
        uc = url.openConnection();
        uc.connect();
        uc = url.openConnection();
        uc.addRequestProperty("User-Agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");

        //InputStream is = url.openStream();
        InputStream is = uc.getInputStream();

        File theDir = new File(path);
        if (!theDir.exists()) {
            theDir.mkdir();
        }

        OutputStream os = new FileOutputStream(path + destinationFile);

        byte[] b = new byte[2048];
        int length;

        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);
        }

        is.close();
        os.close();
    }


    public static boolean createDirectory(String directoryName) throws Exception{
        boolean result = false;
        File theDir = new File(directoryName);
        if (!theDir.exists()) {
            try{
                theDir.mkdir();
                result = true;
            }
            catch(SecurityException se){
                throw se;
            }
        }
        return result;
    }

    public static boolean directoryExists(String directory) throws Exception{
        File theDir = new File(directory);
        return (theDir.exists() && theDir.isDirectory());
    }

    public static boolean directoryIsEmpty(String directory) throws Exception{
        File theDir = new File(directory);
        if (theDir.exists() && theDir.isDirectory() && (theDir.list().length > 0)) {
            return false;
        }
        return true;
    }

}
