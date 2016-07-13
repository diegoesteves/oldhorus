package org.aksw.horus;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import org.aksw.horus.core.util.Global;
import org.aksw.horus.core.util.TimeUtil;
import org.aksw.horus.search.HorusEvidence;
import org.aksw.horus.search.crawl.ResourceExtractor;
import org.aksw.horus.search.query.MetaQuery;
import org.aksw.horus.search.web.ISearchEngine;
import org.aksw.horus.search.web.bing.AzureBingSearchEngine;
import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by dnes on 12/04/16.
 */
public abstract class Horus {

    private static       List<HorusContainer> _lstContainer    = new ArrayList<>();
    public  static       HorusConfig          HORUS_CONFIG;
    private static final Logger               LOGGER           = LoggerFactory.getLogger(Horus.class);
    private static final double               PERSON_THRESHOLD = 0.8;
    private static final int                  PERSON_MAX_ITENS = 50;


    // *************************************** private methods ***************************************

    /**
     * compute the probabilities for each term in a text
     * @param text
     * @throws Exception
     */
    private void computeProbabilities(String text) throws Exception {

        try{

        }catch (Exception e){

        }

    }

    /***
     * annotate a given input text with Stanford POS tagger
     * @param text
     * @throws Exception
     */
    private static void annotateWithStanford(String text) throws Exception{

        LOGGER.debug("starting annotation with Stanford POS");

        try{

            int iSentence = 0;
            int iTerm = 0;
            int iPosition = 0;

            Properties props = new Properties();
            props.setProperty("annotators","tokenize, ssplit, pos");

            StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
            Annotation annotation = new Annotation(text);
            pipeline.annotate(annotation);
            List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
            for (CoreMap sentence : sentences) {
                HorusContainer c = new HorusContainer(iSentence, sentence.toString());
                for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                    String word = token.get(CoreAnnotations.TextAnnotation.class);
                    String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                    c.addTerm(new HorusTerm(iTerm, word, pos, iPosition));
                    iTerm++; iPosition++;
                }
                _lstContainer.add(c);
                iSentence++; iTerm = 0;
            }

            LOGGER.debug("done!");

        }catch(Exception e){
            LOGGER.error(e.toString());
        }
    }

    private static boolean isTermCached(String term){
        return false;
    }

    private static void cacheTerm(String term) throws Exception {

        LOGGER.debug(":: caching [" + term);
        AzureBingSearchEngine engine = new AzureBingSearchEngine();
        //engine.downloadAndCacheImages();

    }

    private static void processPerson() throws Exception{

        for (HorusContainer h : _lstContainer) {
            for (HorusTerm t : h.getTerms()) {
                if (t.getPOS().equals("NN") || t.getPOS().equals("NNP")) {
                    //TODO: check here the POS TAG list

                    LOGGER.debug(":: checking if [" + t.getTerm() + "] is cached");
                    if (!isTermCached(t.getTerm())){
                        cacheTerm(t.getTerm());
                    }

                    LOGGER.debug(":: returning the cache for [" + t.getTerm() + "]");





                    LOGGER.debug(":: checking if [" + t.getTerm() + "] is a [PERSON]");





                }


            }
            LOGGER.info("");
        }

    }

    // *************************************** public methods ***************************************

    public static List<HorusContainer> process(String text) throws Exception{

        long start = System.currentTimeMillis();

        /* 1. Annotate text */
        List<HorusContainer> horusContainers = annotate(text);

        /* 2. Querying and Caching */
        List<MetaQuery> queries = new ArrayList<>();

        //filtering out
        for ( HorusContainer container : horusContainers ) {
            container.getTerms().forEach(t -> {
                if (t.getPOS().equals("NN") || t.getPOS().equals("NNS") ||
                        t.getPOS().equals("NNP") || t.getPOS().equals("NNPS")){
                    queries.add(new MetaQuery(Global.NERType.LOC, t.getTerm(), t.getPosition()));
                    queries.add(new MetaQuery(Global.NERType.PER, t.getTerm(), t.getPosition()));
                    queries.add(new MetaQuery(Global.NERType.ORG, t.getTerm(), t.getPosition()));
                }
            });
        }
        if ( queries.size() <= 0 ) {
            LOGGER.debug("none query has been generated for this text!");
            return new ArrayList<>();
        }
        LOGGER.debug("-> Preparing queries took " +
                TimeUtil.formatTime(System.currentTimeMillis() - start));

        ISearchEngine engine = new AzureBingSearchEngine();
        long startCrawl = System.currentTimeMillis();
        ResourceExtractor ext = new ResourceExtractor(queries);
        List<HorusEvidence> evidences = ext.extract(engine);

        LOGGER.debug(" -> extracting evidences took " +
                TimeUtil.formatTime(System.currentTimeMillis() - startCrawl));


        /* 3. Running PER model */


        /* 4. Running LOC model */


        /* 5. Running ORG model */

        return horusContainers;
    }
    /***
     * init method
     */
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

    /***
     * print the results (stdout)
     */
    public static void printResults(){

        for (HorusContainer h : _lstContainer) {
            LOGGER.info(":: Sentence Index " + h.getSentenceIndex() + ": " + h.getSentence());
            for (HorusTerm t : h.getTerms()) {
                LOGGER.info("  -- index     : " + t.getIndex());
                LOGGER.info("  -- term      : " + t.getTerm());
                LOGGER.info("  -- tagger    : " + t.getPOS());
                LOGGER.info("  -- P(LOC)    : " + String.valueOf(t.getLocationProb()));
                LOGGER.info("  -- P(PER)    : " + String.valueOf(t.getPersonProb()));
                LOGGER.info("  -- P(ORG)    : " + String.valueOf(t.getOrganisationProb()));
                LOGGER.info("  -- NER Class : " + t.getNER());
            }
            LOGGER.info("");
        }


    }

    /***
     * run HORUS
     * @param inputText
     * @return
     * @throws Exception
     */
    private static List<HorusContainer> annotate(String inputText) throws Exception{

        LOGGER.info(":: annotating the input text...");

        try{

            init();

            LOGGER.debug(":: stanford annotations");
            annotateWithStanford(inputText);

            LOGGER.debug(":: checking PER");
            processPerson();

        }catch (Exception e){
            LOGGER.error(e.toString());
        }

        LOGGER.info(":: done");

        return _lstContainer;

    }

    /***
     * exports the metadata of execution set to the MEX interchange file format
     * @param s1
     * @param s2
     * @param s3
     * @param s4
     */
    public static void exportToMEX(String s1, String s2, String s3, String s4){
        //TODO: integrate LOG4MEX and convert container to mex
    }

}
