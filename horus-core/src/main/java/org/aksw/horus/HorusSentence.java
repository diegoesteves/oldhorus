package org.aksw.horus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dnes on 25/05/16.
 * Represents a sentence. An input text may have more than 1 sentence, i.e., more than 1 HorusSentence
 */
public class HorusSentence {

    private int             _sentenceIndex;
    private List<HorusToken> _tokens;
    private String          _sentenceText;

    public HorusSentence(int sentenceId, String text) {
        this._tokens = new ArrayList<>();
        this._sentenceIndex = sentenceId;
        this._sentenceText = text;
    }

    public List<HorusToken> getTokens() {
        return this._tokens;
    }

    public void addToken(HorusToken t) {
        this._tokens.add(t);
    }

    public int getSentenceIndex() {
        return this._sentenceIndex;
    }

    public String getSentenceText() {
        return this._sentenceText;
    }

    public HorusToken getToken(Integer index){
        return this._tokens.get(index);
    }

}
