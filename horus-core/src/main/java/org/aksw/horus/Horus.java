package org.aksw.horus;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import org.aksw.horus.algorithm.FaceDetectOpenCV;
import org.aksw.horus.core.util.Global;
import org.aksw.horus.core.util.TimeUtil;
import org.aksw.horus.search.HorusEvidence;
import org.aksw.horus.search.crawl.ResourceExtractor;
import org.aksw.horus.search.query.MetaQuery;
import org.aksw.horus.search.web.ISearchEngine;
import org.aksw.horus.search.web.WebImageVO;
import org.aksw.horus.search.web.bing.AzureBingSearchEngine;
import org.apache.lucene.util.packed.DirectMonotonicReader;
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

    private static       List<HorusContainer> horusContainers    = new ArrayList<>();
    public  static       HorusConfig          HORUS_CONFIG;
    private static final Logger               LOGGER             = LoggerFactory.getLogger(Horus.class);
    private static double                     PER_THRESHOLD;
    private static double                     LOC_THRESHOLD;
    private static double                     ORG_THRESHOLD;
    private static int                        PER_OFFSET;
    private static int                        LOC_OFFSET;
    private static int                        ORG_OFFSET;

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
                    //todo: check whether I've got a new linked term here to run a composed query (amod and compound) -> http://nlp.stanford.edu/software/dependencies_manual.pdf
                    int ref = 0;
                    c.addTerm(new HorusTerm(iTerm, word, pos, iPosition, ref));
                    iTerm++; iPosition++;
                }
                horusContainers.add(c);
                iSentence++; iTerm = 0;
            }

            LOGGER.debug("done!");

        }catch(Exception e){
            LOGGER.error(e.toString());
        }
    }

    private static void cacheTerm(String term) throws Exception {

        LOGGER.debug(":: caching [" + term);
        AzureBingSearchEngine engine = new AzureBingSearchEngine();
        //engine.downloadAndCacheImages();

    }

    // *************************************** public methods ***************************************

    public static List<HorusContainer> process(String text) throws Exception{
        LOGGER.info(":: Processing...");

        long start = System.currentTimeMillis();

        /* 1. Annotate text */
        annotateWithStanford(text);

        /* 2. Querying and Caching */
        List<MetaQuery> queries = new ArrayList<>();

        //filtering out and creating linked list of terms
        for ( HorusContainer container : horusContainers ) {
            container.getTerms().forEach(t -> {
                if (t.getPOS().equals("NN") || t.getPOS().equals("NNS") ||
                        t.getPOS().equals("NNP") || t.getPOS().equals("NNPS")){
                    queries.add(new MetaQuery(Global.NERType.LOC, t.getTerm(), ""));
                    queries.add(new MetaQuery(Global.NERType.PER, t.getTerm(), ""));
                    queries.add(new MetaQuery(Global.NERType.ORG, t.getTerm(), ""));
                    if (t.getRefNextTerm() != -1){
                        String finalTerm = t.getTerm();
                        HorusTerm nextT = container.getTerms().get(t.getIndex()+1);
                        finalTerm += " " + nextT.getTerm();
                        while(nextT.getRefNextTerm() != -1 ){
                            nextT = container.getTerms().get(t.getIndex()+1);
                            finalTerm += " " + nextT.getTerm();
                        }
                        queries.add(new MetaQuery(Global.NERType.LOC, finalTerm, ""));
                        queries.add(new MetaQuery(Global.NERType.PER, finalTerm, ""));
                        queries.add(new MetaQuery(Global.NERType.ORG, finalTerm, ""));
                    }
                }
            });
        }

        if ( queries.size() <= 0 ) {

            LOGGER.warn("-> none query has been generated for this input! no HORUS processing will happen ...");

        } else {

            LOGGER.info("-> preparing queries took " + TimeUtil.formatTime(System.currentTimeMillis() - start));
            ISearchEngine engine = new AzureBingSearchEngine();
            long startCrawl = System.currentTimeMillis();

            ResourceExtractor ext = new ResourceExtractor(queries);
            List<HorusEvidence> evidences = ext.extractAndCache(engine);
            LOGGER.info(" -> extracting evidences took " + TimeUtil.formatTime(System.currentTimeMillis() - startCrawl));

            /* 3. Running models */
            recognizeEntities(evidences);
§
            /* 4. based on indicators, make the decision */
            makeDecisionAmongAll();

        }

        /* 5. return the containers */
        return horusContainers;
    }
    /***
     * init method
     */
    public static void init(){

        try {

            if ( Horus.HORUS_CONFIG  == null )
                Horus.HORUS_CONFIG = new HorusConfig(new Ini(new File(Horus.class.getResource("/horus.ini").getFile())));

            PER_THRESHOLD = Horus.HORUS_CONFIG.getDoubleSetting("crawl", "PER_THRESHOLD");
            ORG_THRESHOLD = Horus.HORUS_CONFIG.getDoubleSetting("crawl", "ORG_THRESHOLD");
            LOC_THRESHOLD = Horus.HORUS_CONFIG.getDoubleSetting("crawl", "LOC_THRESHOLD");

            PER_OFFSET = Horus.HORUS_CONFIG.getIntegerSetting("crawl", "PER_OFFSET");
            ORG_OFFSET = Horus.HORUS_CONFIG.getIntegerSetting("crawl", "ORG_OFFSET");
            LOC_OFFSET = Horus.HORUS_CONFIG.getIntegerSetting("crawl", "LOC_OFFSET");


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
        LOGGER.info(":: Printing results...");

        for (HorusContainer h : horusContainers) {
            LOGGER.info(":: Sentence Index " + h.getSentenceIndex() + ": " + h.getSentence());
            for (HorusTerm t : h.getTerms()) {
                LOGGER.info("  -- index     : " + t.getIndex());
                LOGGER.info("  -- term      : " + t.getTerm());
                LOGGER.info("  -- tagger    : " + t.getPOS());
                LOGGER.info("  -- Prob(LOC)    : " + String.valueOf(t.getLocationProb()));
                LOGGER.info("  -- Prob(PER)    : " + String.valueOf(t.getPersonProb()));
                LOGGER.info("  -- Prob(ORG)    : " + String.valueOf(t.getOrganisationProb()));
                LOGGER.info("  -- NER Class : " + t.getNER());
            }
            LOGGER.info("");
        }
    }

    /***
     * exports the metadata of execution set to the MEX interchange file format
     * @param s1
     * @param s2
     * @param s3
     * @param s4
     */
    public static void exportToMEX(String s1, String s2, String s3, String s4){
        LOGGER.info(":: Exporting horus metadata (MEX)");

        //TODO: integrate LOG4MEX and convert container to mex
    }

    /***
     * exports the metadata of execution set to the MEX interchange file format
     * @param s1
     * @param s2
     * @param s3
     * @param s4
     */
    public static void exportToNIF(String s1, String s2, String s3, String s4){
        LOGGER.info(":: Exporting horus metadata (NIF)");

        //TODO: integrate ? and convert container to nif
    }


    private static void recognizeEntities(List<HorusEvidence> evidences) throws Exception{
        LOGGER.info(":: Recognizing Entities - start");

        for ( Map.Entry<MetaQuery, HorusEvidence> evidencesToPosition : evidences.entrySet()) {
            MetaQuery q = evidencesToPosition.getKey();
            HorusEvidence evidence : evidencesToPosition.getValue();

                getTermByPosition(position).

            }
        }

        for (HorusContainer h : horusContainers) {
            LOGGER.debug(":: Sentence Index " + h.getSentenceIndex() + ": " + h.getSentence());
            for (HorusTerm t : h.getTerms()) {
                LOGGER.debug(":: is person? " + t.getIndex() + ": " + t.getTerm());
                setPersonDetected(t.getPosition());
                LOGGER.debug(":: is organisation? " + t.getIndex() + ": " + t.getTerm());
                setOrganisationDetected(t.getPosition());
                LOGGER.debug(":: is location? " + t.getIndex() + ": " + t.getTerm());
                setLocationDetected(t.getPosition());
            }
        }
        LOGGER.info(":: Recognizing Entities - done");
    }

    //TODO: to train J48 here...
    private static void makeDecisionAmongAll() throws Exception{

    }

    //TODO: create a singleton later...
    private static void setPersonDetected(int position) throws Exception{
        HorusEvidence e = getTermByPosition(position).getEvidences(Global.NERType.PER);
        FaceDetectOpenCV fd = new FaceDetectOpenCV();
        for (WebImageVO img: e.getImages()){
            boolean ret =
                    fd.faceDetected(new File(img.getImageFilePath() + img.getImageFileName()));
            img.setPersonDetected(ret);
        }
    }
    private static void setLocationDetected(int position) throws Exception{
    }
    private static void setOrganisationDetected(int position) throws Exception{
    }

    private static HorusTerm getTermByPosition(int position) throws Exception{
        int aux = 0;
        for (HorusContainer h : horusContainers) {
            aux += h.getTerms().size();
            if (aux >= position){
                for (HorusTerm t :  h.getTerms()) {
                    if (t.getPosition() == position){
                        return t;
                    }
                }
            }
        }
        return null;
    }

}
