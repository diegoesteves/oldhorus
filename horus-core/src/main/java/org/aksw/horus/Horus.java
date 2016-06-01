package org.aksw.horus;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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
                    c.addTerm(new HorusTerm(iTerm, word, pos));
                    iTerm++;
                }
                _lstContainer.add(c);
                iSentence++; iTerm = 0;
            }

            LOGGER.debug("done!");

        }catch(Exception e){
            LOGGER.error(e.toString());
        }
    }


    private static void processPerson() throws Exception{

        for (HorusContainer h : _lstContainer) {
            for (HorusTerm t : h.getTerms()) {
                if (t.getPOS().equals("NN") || t.getPOS().equals("NNP")) {
                    //TODO: check here the POS TAG list
                    LOGGER.debug(":: checking if [" + t.getTerm() + "] is a [PERSON]");

                }


            }
            LOGGER.info("");
        }

    }

    // *************************************** public methods ***************************************


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
    public static List<HorusContainer> annotate(String inputText) throws Exception{

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
