package org.aksw.horus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dnes on 25/05/16.
 */
public class HorusContainer {

    private int             _sentenceId;
    private int             _index;
    private List<HorusTerm> _terms;

    public HorusContainer(int index){
        this._terms = new ArrayList<>();
        this._index = index;
    }

    public int getIndex() {
        return _index;
    }

    public void setIndex(int _index) {
        this._index = _index;
    }

    public List<HorusTerm> getTerms(){
        return this._terms;
    }

    public void addTerm(HorusTerm t){
        this._terms.add(t);
    }


}
