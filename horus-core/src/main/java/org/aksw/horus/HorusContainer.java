package org.aksw.horus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dnes on 25/05/16.
 * Represents a sentence. An input text may have more than 1 sentence, i.e., more than 1 HorusContainer
 */
public class HorusContainer {

    private int _sentenceIndex;
    private List<HorusTerm> _terms;
    private String _sentence;


    public HorusContainer(int sentenceId, String sentence) {
        this._terms = new ArrayList<>();
        this._sentenceIndex = sentenceId;
        this._sentence = sentence;
    }


    public List<HorusTerm> getTerms() {
        return this._terms;
    }

    public void addTerm(HorusTerm t) {
        this._terms.add(t);
    }

    public int getSentenceIndex() {
        return this._sentenceIndex;
    }

    public void setSentenceIndex(int sentenceIndex) {
        this._sentenceIndex = sentenceIndex;
    }

    public String getSentence() {
        return this._sentence;
    }

}
