package org.aksw.horus;

/**
 * Created by dnes on 25/05/16.
 */
public class HorusTerm {

    private int       _index;
    private String    _term;
    private String    _postagger;

    private boolean   _person;
    private double    _personProb;
    private boolean   _location;
    private double    _locationProb;
    private boolean   _organisation;
    private double    _organisationProb;

    public HorusTerm(int index, String term, String POS){
        this._term = term;
        this._postagger = POS;
        this._index = index;
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

    public String getTerm() {
        return _term;
    }

    public void setTerm(String _term) {
        this._term = _term;
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


}
