package org.aksw.horus;

import org.aksw.horus.core.util.Global;
import org.aksw.horus.search.HorusEvidence;

/**
 * Created by dnes on 25/05/16.
 */
public class HorusTerm {

    private int       _index;
    private int       _position; //global index
    private String    _token;
    private int       _ref_next_term; //it links to the next associated term
    private String    _postagger;

    private boolean   _person;
    private double    _personProb;
    private boolean   _location;
    private double    _locationProb;
    private boolean   _organisation;
    private double    _organisationProb;

    private HorusEvidence _evidencePER;
    private HorusEvidence _evidenceLOC;
    private HorusEvidence _evidenceORG;


    public HorusTerm(int index, String token, String POS, int position, int ref_next_term){
        this._token = token;
        this._postagger = POS;
        this._index = index;
        this._position = position;
        this._ref_next_term = ref_next_term;
    }

    public HorusEvidence getEvidences(Global.NERType type){
        if (type.equals(Global.NERType.PER)){
            return this._evidencePER;
        }else if(type.equals(Global.NERType.LOC)){
            return this._evidenceLOC;
        }else{
            return this._evidenceORG;
        }
    }

    public int getIndex() {
        return _index;
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

    public double getPersonProb() {
        return _personProb;
    }

    public void setPersonProb(double _personProb) {
        this._personProb = _personProb;
    }

    public boolean isLocation() {
        return _location;
    }

    public void setLocation(boolean _location) {
        this._location = _location;
    }

    public double getLocationProb() {
        return _locationProb;
    }

    public int getPosition(){
        return this._position;
    }

    public String getNER(){

        if (this._locationProb ==0d && this._organisationProb ==0 && this._personProb ==0)
            return "-";

        Double maxval = findMax(this._locationProb, this._organisationProb, this._personProb);

        if (maxval.equals(this._locationProb))
                return "LOC";
        if (maxval.equals(this._personProb))
            return "PER";
        if (maxval.equals(this._organisationProb))
            return "ORG";

        return "ERR";

    }

    private Double findMax(double... vals) {
        double max = Double.NEGATIVE_INFINITY;

        for (double d : vals) {
            if (d > max) max = d;
        }

        return max;
    }

    public void setLocationProb(double _locationProb) {
        this._locationProb = _locationProb;
    }

    public boolean isOrganisation() {
        return _organisation;
    }

    public void setOrganisation(boolean _organisation) {
        this._organisation = _organisation;
    }

    public double getOrganisationProb() {
        return _organisationProb;
    }

    public void setOrganisationProb(double _organisationProb) {
        this._organisationProb = _organisationProb;
    }

    public int getRefNextTerm(){
        return this._ref_next_term;
    }

}
