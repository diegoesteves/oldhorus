package org.aksw.horus.search.query;

import org.aksw.horus.core.util.Global;
import org.apache.lucene.util.packed.DirectMonotonicReader;

/**
 * Created by Diego on 7/13/2016.
 */
public class MetaQuery {

    private Global.NERType type;
    //represents
    private String term;
    private String additionalContent;
    private int position;

    public MetaQuery(Global.NERType type, String term, String additionalContent, int position){
        this.type = type;
        this.position = position;
        this.term = term;
        this.additionalContent = additionalContent;
    }
    public MetaQuery(){

    }
    public MetaQuery(Global.NERType type, String term, String additionalContent){
        this.type = type;
        this.term = term;
        this.additionalContent = additionalContent;
    }

    public Global.NERType getType(){
        return this.type;
    }

    public String getTerm(){
        return this.term;
    }

    public String getAdditionalContent(){
        return this.additionalContent;
    }

    public int getPosition(){
        return this.position;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("%s|-|%s|-|%s|-|%s", term, additionalContent, type.toString(), String.valueOf(position));
    }



}
