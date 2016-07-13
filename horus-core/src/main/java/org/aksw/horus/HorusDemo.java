package org.aksw.horus;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by dnes on 31/05/16.
 */
public class HorusDemo {

    public static Logger LOG       = LogManager.getLogger(HorusDemo.class);

    public static void main(String[] args) throws Exception {

        LOG.info("************************************************************************");
        LOG.info("*                          Starting HORUS                              *");
        LOG.info("************************************************************************");

        long startTime = System.currentTimeMillis();
        LOG.info(startTime);

        String text = "There you go diego! how's going leipzig? I'll go to Rio this weekend! Are you up for? Ahhh I've got a new dell laptop! Uhull! Cya!";
        String text2 = "Rio de Janeiro"; //stanford recognizes as Person
        //tag list -> https://www.ling.upenn.edu/courses/Fall_2003/ling001/penn_treebank_pos.html

        try{
            Horus.process(text);
            Horus.printResults();
            Horus.exportToMEX("path/to/save/the/file", "yournamespace", "filename", "file-format");
        }catch (Exception e){
            LOG.error(e.toString());
        }


        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        LOG.info("************************************************************************");
        LOG.info("*                         Process Finished                             *");
        LOG.info("************************************************************************");

        String out = String.format("Processing Time: %02d hour, %02d min, %02d sec",
                TimeUnit.MILLISECONDS.toHours(totalTime),
                TimeUnit.MILLISECONDS.toMinutes(totalTime),
                TimeUnit.MILLISECONDS.toSeconds(totalTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(totalTime))
        );
        LOG.info(out);

    }


}
