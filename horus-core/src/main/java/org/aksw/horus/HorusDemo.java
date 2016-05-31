package org.aksw.horus;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

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

        String sentence = "There you go diego! how's going leipzig? I'll go to Rio this weekend! Are you up for? Ahhh I've got a new dell laptop! Uhull! Cya!";

        Horus.annotate(sentence);

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

        Horus.printResults();

        Horus.exportToMEX("path/to/save/the/file", "yournamespace", "filename", "file-format");

    }


}
