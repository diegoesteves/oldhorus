package org.aksw.horus;

import org.aksw.horus.core.util.Global;
import org.aksw.horus.search.HorusEvidence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dnes on 03/08/16.
 */
public class HorusTerm {

    private List<HorusToken> _tokens;
    private HashMap<Global.NERType, Double> prob;
    private HashMap<Global.NERType, HorusEvidence> evidences;
    private Integer _idTerm;

    /* PER=0, LOC=1, ORG=2 */
    //private MultiValueMap prob;


    public HorusTerm(Integer id){
        this._tokens = new ArrayList<>();
        this.prob = new HashMap<>();
        this.evidences = new HashMap<>();
        this._idTerm = id;
    }

    public Integer getId(){
        return this._idTerm;
    }

    public List<HorusToken> getTokens(){
        return this._tokens;
    }

    public HorusToken getToken(Integer index){
        return this._tokens.get(index);
    }

    public HorusEvidence getEvidences(Global.NERType type){
        return this.evidences.get(type);
    }

    public void setEvidence(Global.NERType type, HorusEvidence e){
        this.evidences.put(type, e);
    }

    public void addToken(HorusToken token){
        this._tokens.add(token);
    }

    public String getTokensValue(){
        String ret = "";
        for (HorusToken t: this._tokens){
            ret += t.getTokenValue() + " ";
        }
        return ret.substring(0, ret.length() - 1);
    }

    public String getTokensPOS(Global.NLPToolkit tool){
        String ret = "";
        for (HorusToken t: this._tokens){
            ret += t.getPOS(tool) + " ";
        }
        return ret.substring(0, ret.length() - 1);
    }

    public boolean isComposedTerm(){
        return ((_tokens.size() > 1) ? true: false);
    }

    public Double getProbability(Global.NERType type) {
        return prob.get(type);
    }

    public void setProbability(Global.NERType type, double prob) {
        this.prob.putIfAbsent(type, prob);
    }

    public String getHorusNER(){

        String ner = "-";
        for(Map.Entry<Global.NERType, Double> entry : this.prob.entrySet())
            if (!entry.getValue().equals(0d)){ner = "?"; break;}

        if (ner.equals("-")) return ner;

        ArrayList<Global.NERType> argmax = getIndexOfMaxValues();

        return argmax.toString();

    }

    private ArrayList<Global.NERType> getIndexOfMaxValues() {
        double max = Double.NEGATIVE_INFINITY;

        ArrayList<Global.NERType> indexes = new ArrayList<>();

        for(Map.Entry<Global.NERType, Double> entry : this.prob.entrySet()){

            if (entry.getValue() >= max) {
                max = entry.getValue();
                indexes.add(entry.getKey());
            }
        }

        return indexes;
    }

}
