package org.aksw.horus;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;



/**
 * Created by Diego Esteves on 6/25/2016.
 */
public class FileGenerator {


    private static void createNEGFile() throws Exception{

        File dir = new File("C:\\Users\\Diego\\Downloads\\opencv\\build\\x64\\vc12\\bin\\neg");
        FileWriter fw = new FileWriter("C:\\Users\\Diego\\Downloads\\opencv\\build\\x64\\vc12\\bin\\bg.txt", false);
        BufferedWriter out = new BufferedWriter(fw);

        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File d : directoryListing) {
                if (d.isFile()) {

                    Image image = ImageIO.read(d);
                    if (image != null) {
                        out.write("neg\\" + d.getName());
                        out.newLine();
                    }
                }
            }
        }
        out.flush();
        out.close();
        fw.close();


    }

    private static void createPOSFile() throws Exception{

        File dir = new File("C:\\Users\\Diego\\Downloads\\opencv\\build\\x64\\vc12\\bin\\pos");
        FileWriter fw = new FileWriter("C:\\Users\\Diego\\Downloads\\opencv\\build\\x64\\vc12\\bin\\posdata.info", false);
        BufferedWriter out = new BufferedWriter(fw);

        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File d : directoryListing) {
                if (d.isFile()) {
                    Image image = ImageIO.read(d);
                    if (image != null) {
                        BufferedImage bimg = (BufferedImage) image;
                        out.write("pos\\" + d.getName() + " 1 0 0 " + bimg.getWidth() + " " + bimg.getHeight());
                        out.newLine();
                    }
                }
            }
        }
        out.flush();
        out.close();
        fw.close();

    }

    private void renameFileAddingPrefix(){

        try {

            String prefix = "MAP_";
            File dir = new File("C:\\DNE5\\github\\Horus\\horus-core\\src\\main\\resources\\maps");
            File[] directoryListing = dir.listFiles();
            if (directoryListing != null) {
                for (File d : directoryListing) {
                    if (d.isFile()) {
                        d.renameTo(new File(d.getParent() + "\\" + prefix + d.getName()));
                    }
                }
            }

        } catch (Exception e) {
            System.out.print(e.toString());
        }


    }


    private static void readFileAndDownloadImage(){

        int ok = 0, nok = 0;
        try{

            BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Diego\\Downloads\\flickr_logos_27_dataset\\flickr_logos_27_dataset_distractor_set_urls.txt"));
            String line;
            while((line = br.readLine()) != null) {
                if (downloadImage(line, "C:\\Users\\Diego\\Downloads\\opencv\\build\\x64\\vc12\\bin\\datasets\\flickr27\\distractor\\" )) {
                    System.out.println("ok: " + line); ok++;
                }else{
                    System.out.println("nok: " + line); nok++;
                }

                if (line.equals("http://farm3.static.flickr.com/2019/1644282816_5098124616.jpg"))
                    System.out.print("d");

            }
        }catch (Exception e){

        }

        System.out.println("ok: " + ok);
        System.out.println("nok: " + nok);
    }

    private static boolean downloadImage(String purl, String destinationPath){
        BufferedImage image = null;

        try {
            URL url = new URL(purl);
            try{
                image = ImageIO.read(url);
            }catch (Exception e){
                System.out.print(e.toString() );
            }

            if (image != null){
                ImageIO.write(image, "jpg", new File(destinationPath + url.getFile().replace("/", "_")));
                return true;
            } else{
                return false;
            }
        } catch (IOException e) {
            System.out.println("error: " + purl + " - " + e.getMessage());
            return false;
        }
    }

    private static void convert(){

        //http://kovan.ceng.metu.edu.tr/LogoDataset/

        //  C:\Users\Diego\Downloads\UMD_Logo_Database.zip\Logos-UMD

        String[] readers = ImageIO.getReaderFormatNames();
        for ( String s : readers)
        {
            System.out.println(s);
        }

        /*
        String inFile = "";
        String otPath = "";

        SeekableStream s = new FileSeekableStream(inFile);
        TIFFDecodeParam param = null;
        ImageDecoder dec = ImageCodec.createImageDecoder("tiff", s, param);
        RenderedImage op = dec.decodeAsRenderedImage(0);

        FileOutputStream fos = new FileOutputStream(otPath);
        JPEGEncodeParam jpgparam = new JPEGEncodeParam();
        jpgparam.setQuality(67);
        ImageEncoder en = ImageCodec.createImageEncoder("jpeg", fos, jpgparam);
        en.encode(op);
        fos.flush();
        fos.close();
        */
    }

    public static void main(String[] args) throws Exception{
        createPOSFile();
        createNEGFile();
        //readFileAndDownloadImage();
        //convert();
    }
}
