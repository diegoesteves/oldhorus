package org.aksw.horus.search.query;

import org.aksw.horus.core.util.Global;
import org.apache.lucene.util.packed.DirectMonotonicReader;

/**
 * Created by Diego on 7/13/2016.
 */
public class MetaQuery {

    private Global.NERType type;
    private String term;
    private int position;

    public MetaQuery(Global.NERType type, String term, int position){

    }
    public MetaQuery(){

    }

    public Global.NERType getType(){
        return this.type;
    }

    public String getTerm(){
        return this.term;
    }

    public int getPosition(){
        return this.position;
    }





}
