package org.aksw.horus;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Created by dnes on 12/04/16.
 */
public class Horus {

    private static final Logger LOGGER = LoggerFactory.getLogger(Horus.class);
    public static HorusConfig HORUS_CONFIG;

    public static void init(){

        try {

            if ( Horus.HORUS_CONFIG  == null )
                Horus.HORUS_CONFIG = new HorusConfig(new Ini(new File(Horus.class.getResource("/horus.ini").getFile())));

        } catch (InvalidFileFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * detect whether a word is likely to be a PERSON's name
     * @param sentence a term that might represent a name or full name of a PERSON
     * @return
     */
    public boolean isPerson(String sentence){

        boolean ret = false;

        try{
            String[] terms = sentence.split("\\s+");

        }catch(Exception e){

        }


        return ret;

    }

}
