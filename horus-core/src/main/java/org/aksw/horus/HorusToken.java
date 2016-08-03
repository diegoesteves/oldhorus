package org.aksw.horus;

import org.aksw.horus.core.util.Global;
import org.aksw.horus.search.HorusEvidence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dnes on 25/05/16.
 */
public class HorusToken {

    private int       _index;
    private int       _position; //global index
    private String    _token;
    private int       _ref_next_token; //it links to the next associated term
    private String    _postagger;

    private boolean   _person;
    private boolean   _location;
    private boolean   _organisation;

    /* PER=0, LOC=1, ORG=2 */
    //private MultiValueMap prob;
    private HashMap<Global.NERType, Double> prob;
    private HashMap<Global.NERType, HorusEvidence> evidences;


    public HorusToken(int index, String token, String POS, int position, int _ref_next_token){
        this._token = token;
        this._postagger = POS;
        this._index = index;
        this._position = position;
        this._ref_next_token = _ref_next_token;
        //this.prob = new MultiValueMap();
        this.prob = new HashMap<>();
        this.evidences = new HashMap<>();

    }

    public HorusEvidence getEvidences(Global.NERType type){
        return this.evidences.get(type);
    }

    public int getIndex() {
        return _index;
    }

    public Double getProbability(Global.NERType type) {
        return prob.get(type);
    }

    public void setProbability(Global.NERType type, double prob) {
        this.prob.putIfAbsent(type, prob);
    }

    public void setIndex(int _index) {
        this._index = _index;
    }

    public String getPOS() {
        return _postagger;
    }

    public void setPOS(String _postagger) {
        this._postagger = _postagger;
    }

    public String getToken() {
        return _token;
    }

    public void setToken(String _token) {
        this._token = _token;
    }

    public boolean isPerson() {
        return _person;
    }

    public void setPerson(boolean _person) {
        this._person = _person;
    }

    public boolean isLocation() {
        return _location;
    }

    public void setLocation(boolean _location) {
        this._location = _location;
    }

    public int getPosition(){
        return this._position;
    }

    public String getNER(){

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

    public boolean isOrganisation() {
        return _organisation;
    }

    public void setOrganisation(boolean _organisation) {
        this._organisation = _organisation;
    }

    public int getRefNextToken(){
        return this._ref_next_token;
    }

}
