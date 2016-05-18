package org.aksw.horus.core.util;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

/**
 * Created by dnes on 18/05/16.
 */
public class Global {

    private static Global singleton = null;

    protected Global() {
        // Exists only to defeat instantiation.
    }

    private static Logger LOGGER =  Logger.getLogger(Global.class);

    public static Global getInstance() {
        if(singleton == null) {
            singleton = new Global();
        }
        return singleton;
    }


    /**
     * serializes an object into a specific path
     * @param e
     * @param filePath
     * @param fileName the default file type is ".ser", no need to explicitly define it
     * @throws Exception
     */
    public static boolean serializeObject(Object e, String filePath, String fileName) throws Exception{

        try
        {
            String fullName = filePath + "/" + fileName + ".ser";
            File f = new File(fullName);
            if(!f.exists()) {
                f.createNewFile();}

            FileOutputStream fileOut =
                    new FileOutputStream(fullName, false);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(e);
            out.close();
            fileOut.close();
            LOGGER.debug("Serialized data is saved in " + filePath + fileName);
            return true;
        }catch(Exception ex)
        {
            LOGGER.error(ex.getMessage());
            return false;
        }


    }

}
