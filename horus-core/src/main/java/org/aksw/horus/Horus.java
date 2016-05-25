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
    public boolean isPerson(String sentence) throws Exception{

        boolean ret = false;

        try{
            String[] terms = sentence.split("\\s+");

        }catch(Exception e){

        }


        return ret;

    }

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

    public static void annotateWithStanford(String text) throws Exception{

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
                HorusContainer c = new HorusContainer(iSentence);
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

    private static void printResults(){

        for (HorusContainer h : _lstContainer) {
            LOGGER.info("Sentence: " + h.getIndex());
            for (HorusTerm t : h.getTerms()) {
                LOGGER.info("-- index: " + t.getIndex());
                LOGGER.info("-- term: " + t.getTerm());
                LOGGER.info("-- tagger: " + t.getPOS());
            }
            LOGGER.info("");
        }


    }

    public static void main(String[] args) {

        try{

            String text = "diego esteves. what's going on orlando?";

            annotateWithStanford(text);

            printResults();

        }catch (Exception e){
            LOGGER.error(e.toString());
        }


    }


}
