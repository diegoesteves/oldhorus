package org.aksw.horus;

/**
 * Created by dnes on 04/08/16.
 */

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.*;

import java.io.StringReader;
import java.util.Collection;
import java.util.List;

class Parser {

    private final static String PCG_MODEL = "/Users/dnes/Github/Horus/horus-core/src/main/resources/models/stanford/englishPCFG.ser.gz";

    private final TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "invertible=true");

    //private final static LexicalizedParser parser = LexicalizedParser.loadModel(PCG_MODEL);
    private final static LexicalizedParser lp     = LexicalizedParser.loadModel(PCG_MODEL);

    public Tree parse(String str) {
        List<CoreLabel> tokens = tokenize(str);
        Tree tree = lp.apply(tokens);
        return tree;
    }

    private List<CoreLabel> tokenize(String str) {
        Tokenizer<CoreLabel> tokenizer =
                tokenizerFactory.getTokenizer(
                        new StringReader(str));
        return tokenizer.tokenize();
    }

    public static void main(String[] args) {
        String str = "My dog also likes eating sausage.";
        Parser parser = new Parser();
        Tree tree = parser.parse(str);

        List<Tree> leaves = tree.getLeaves();
        // Print words and Pos Tags
        for (Tree leaf : leaves) {
            Tree parent = leaf.parent(tree);

            TreebankLanguagePack tlp = new PennTreebankLanguagePack();
            GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
            GrammaticalStructure gs = gsf.newGrammaticalStructure(parent);
            Collection<TypedDependency> td = gs.typedDependenciesCollapsed();
            System.out.println(td);

            leaf.pennPrint();

            Object[] list = td.toArray();
            System.out.println(list.length);
            TypedDependency typedDependency;
            for (Object object : list) {
                typedDependency = (TypedDependency) object;
                System.out.println("Depdency Name: " + typedDependency.dep() + " :: " + "Node" + typedDependency.reln());
                if (typedDependency.reln().getShortName().equals("something")) {
                    //your code
                }
            }

           // System.out.print(leaf.label().value() + "-" + parent.label().value() + " ");
        }

        System.out.println();

//        LexicalizedParser lp = LexicalizedParser.loadModel(PCG_MODEL);
        String sent = "This is one last test!";

        TreebankLanguagePack tlp = lp.getOp().langpack();
        Tokenizer<? extends HasWord> toke = tlp.getTokenizerFactory().getTokenizer(new StringReader(sent));
        List<? extends HasWord> sentence = toke.tokenize();
        lp.apply(sentence);

    }
}