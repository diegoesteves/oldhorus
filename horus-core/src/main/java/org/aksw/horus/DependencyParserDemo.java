package org.aksw.horus;

/**
 * Created by dnes on 05/08/16.
 */
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.trees.GrammaticalRelation;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.logging.Redwood;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.parser.nndep.DependencyParser;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.GrammaticalStructure;

import java.io.StringReader;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * Demonstrates how to first use the tagger, then use the NN dependency
 * parser. Note that the parser will not work on untagged text.
 *
 * @author Jon Gauthier
 */
public class DependencyParserDemo  {

    /** A logger for this class */
    private static Redwood.RedwoodChannels log = Redwood.channels(DependencyParserDemo.class);
    public static void main(String[] args) {
        String modelPath = DependencyParser.DEFAULT_MODEL;
        String taggerPath = "edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger";

        for (int argIndex = 0; argIndex < args.length; ) {
            switch (args[argIndex]) {
                case "-tagger":
                    taggerPath = args[argIndex + 1];
                    argIndex += 2;
                    break;
                case "-model":
                    modelPath = args[argIndex + 1];
                    argIndex += 2;
                    break;
                default:
                    throw new RuntimeException("Unknown argument " + args[argIndex]);
            }
        }

        String text = "James wood allen and garcia stella like each other, even more when are going to the Cine.";



        MaxentTagger tagger = new MaxentTagger(taggerPath);
        DependencyParser parser = DependencyParser.loadFromModelFile(modelPath);

        DocumentPreprocessor tokenizer = new DocumentPreprocessor(new StringReader(text));
        for (List<HasWord> sentence : tokenizer) {
            List<TaggedWord> tagged = tagger.tagSentence(sentence);
            GrammaticalStructure gs = parser.predict(tagged);

            // Print typed dependencies
            log.info(gs);
        }
    }

    public void iterationOver(String text){

        Properties props = new Properties();
        props.setProperty("annotators","tokenize, ssplit, pos, depparse");

        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        Annotation annotation = new Annotation(text);
        pipeline.annotate(annotation);
        List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {

            SemanticGraph dependencies =
                    sentence.get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);

                    /*
                    http://nlp.stanford.edu/nlp/javadoc/javanlp/edu/stanford/nlp/semgraph/SemanticGraphCoreAnnotations.html
                     */


            SemanticGraph dependencies1 =
                    sentence.get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);

            System.out.println(dependencies.toString().contains("compound"));
            System.out.println("----------");
            System.out.println(dependencies.toFormattedString().contains("compound"));
            System.out.println("----------");
            System.out.println(dependencies.toList().contains("compound"));
            System.out.println("----------");
            System.out.println(dependencies.toPOSList().contains("compound"));
            System.out.println("----------");


            System.out.println("SENTENCE: "+sentence.toString());
            System.out.println("DEPENDENCIES: "+dependencies1.toList());
            System.out.println("DEPENDENCIES SIZE: "+dependencies1.size());
            System.out.println("****************************************************");
            Iterable<SemanticGraphEdge> edge_set1 = dependencies1.edgeIterable();
            int j=0;

            for(SemanticGraphEdge edge : edge_set1){
                j++;
                System.out.println("------EDGE DEPENDENCY: "+j);
                Iterator<SemanticGraphEdge> it = edge_set1.iterator();
                IndexedWord dep = edge.getDependent();
                String dependent = dep.word();
                int dependent_index = dep.index();
                IndexedWord gov = edge.getGovernor();
                String governor = gov.word();
                int governor_index = gov.index();
                GrammaticalRelation relation = edge.getRelation();
                System.out.println("No:"+j+" Relation: "+relation.toString()+" Dependent ID: "+dependent_index+" Dependent: "+dependent.toString()+" Governor ID: "+governor_index+" Governor: "+governor.toString());
            }
            System.out.println("");
            System.out.println("");
        }

    }

}
