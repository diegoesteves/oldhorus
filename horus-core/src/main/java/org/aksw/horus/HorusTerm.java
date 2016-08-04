package org.aksw.horus;

import org.aksw.horus.search.HorusEvidence;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dnes on 03/08/16.
 */
public class HorusTerm {

    private List<HorusToken> _tokens;
    private ArrayList<Double> prob;
    private ArrayList<HorusEvidence> evidences;
    private Integer _idTerm;

    public HorusTerm(Integer id){
        this._tokens = new ArrayList<>();
        this.prob = new ArrayList<>();
        this.evidences = new ArrayList<>();
        this._idTerm = id;
    }

    public Integer getId(){
        return this._idTerm;
    }

    public void addToken(HorusToken t){
        this._tokens.add(t);
    }

    public String getTokensValues(){
        String ret = "";
        for (HorusToken t: this._tokens)
            ret += t.getTokenValue() + " ";
        return ret;
    }

    public HorusToken getToken(){
        if (this._tokens.size() >= 1)
            return this._tokens.get(0);
        return null;
    }

    public List<HorusToken> getTokens(){
        return this._tokens;
    }

    public boolean isComposedTerm(){
        return ((this._tokens.size() > 1) ? true : false);
    }

}
