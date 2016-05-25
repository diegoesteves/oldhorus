package org.aksw.horus;

/**
 * Created by dnes on 25/05/16.
 */
public class HorusContainer {

    private int       _index;
    private String    _term;
    private String    _postagger;
    private boolean   _person;
    private double    _personProb;
    private boolean   _location;
    private double    _locationProb;
    private boolean   _organisation;
    private double    _organisationProb;

    public HorusContainer(){

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
