package org.aksw.horus;

import org.aksw.horus.core.util.Global;
import org.apache.commons.lang.NotImplementedException;

/**
 * Created by dnes on 25/05/16.
 */
public class HorusToken {

    private int       _index;
    private int       _position; //global index
    private String    _tokenValue;
    private int       _ref_next_token; //it links to the next associated term (compound)
    private int       _ref_prev_token; //it links to the previous associated term (compound)
    private String    _postagger_stanford;
    private String    _ner_stanford;


    private boolean   _person;
    private boolean   _location;
    private boolean   _organisation;


    public HorusToken(int index, String tokenValue, String POS, Global.NLPToolkit tool, int position, String NER){
        this._tokenValue = tokenValue;
        this._index = index;
        this._position = position;
        //this.prob = new MultiValueMap();

        if (tool.equals(Global.NLPToolkit.STANFORD))
            this._postagger_stanford = POS;

        if (tool.equals(Global.NLPToolkit.STANFORD))
            this._ner_stanford = NER;

    }

    public boolean isComposed(){
        return ((_ref_next_token == 0 && _ref_prev_token == 0) ? false: true);
    }

    public Integer getIndex() {
        return _index;
    }

    public void setIndex(int _index) {
        this._index = _index;
    }

    public String getPOS(Global.NLPToolkit tool) {
        if (tool.equals(Global.NLPToolkit.STANFORD))
            return _postagger_stanford;
        else
            throw new NotImplementedException();
    }

    public String getNER(Global.NLPToolkit tool) {
        if (tool.equals(Global.NLPToolkit.STANFORD))
            return _ner_stanford;
        else
            throw new NotImplementedException();
    }

    public void setPOS(String _postagger, Global.NLPToolkit tool) {
        if (tool.equals(Global.NLPToolkit.STANFORD))
            this._postagger_stanford = _postagger;
        else throw new NotImplementedException();
    }

    public String getTokenValue() {
        return this._tokenValue;
    }

    public void setToken(String _tokenValue) {
        this._tokenValue = _tokenValue;
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


    public boolean isOrganisation() {
        return _organisation;
    }

    public void setOrganisation(boolean _organisation) {
        this._organisation = _organisation;
    }

    public int getRefNextToken(){
        return this._ref_next_token;
    }
    public int getRefPrevToken(){
        return this._ref_prev_token;
    }

    public void setRefNextToken(int ref){
        this._ref_next_token = ref;
    }
    public void setRefPrevToken(int ref){
        this._ref_prev_token = ref;
    }

}
