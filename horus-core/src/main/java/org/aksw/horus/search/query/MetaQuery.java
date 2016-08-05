package org.aksw.horus.search.query;

import org.aksw.horus.Horus;
import org.aksw.horus.core.util.Constants;
import org.aksw.horus.core.util.Global;

/**
 * Created by Diego Esteves on 7/13/2016.
 */
public class MetaQuery {

    private Global.NERType type;
    private String text;
    private String additionalContent;
    private String searchEngineFeature;
    private Integer horusTermIdentifier;

    public MetaQuery(String metaQueryStr){
        String[] qsplited = metaQueryStr.split(Constants.METAQUERY_SEPARATOR);
        this.text = qsplited[0];
        this.additionalContent = qsplited[1];
        this.type = Global.NERType.valueOf(qsplited[2]);
        this.searchEngineFeature = qsplited[3];
        this.horusTermIdentifier = Integer.valueOf(qsplited[4]);
    }


    public MetaQuery(Global.NERType type, String text, String additionalContent, Integer sequential){
        this.type = type;
        this.text = text;
        this.additionalContent = additionalContent;
        setSearchEngineFeature();
        this.horusTermIdentifier = horusTermIdentifier;

    }

    public Integer getHorusTermIdentifier(){
        return  this.horusTermIdentifier;

    }

    private void setSearchEngineFeature(){
        if (type.equals(Global.NERType.PER)) {
            this.searchEngineFeature = Horus.HORUS_CONFIG.getStringSetting("[search_engine]", "SEARCH_ENGINE_FEATURES_PER");}
        else if (type.equals(Global.NERType.LOC)) {
            this.searchEngineFeature = Horus.HORUS_CONFIG.getStringSetting("[search_engine]", "SEARCH_ENGINE_FEATURES_LOC");}
        else {
            this.searchEngineFeature = Horus.HORUS_CONFIG.getStringSetting("[search_engine]", "SEARCH_ENGINE_FEATURES_ORG");}
    }

    public Global.NERType getType(){
        return this.type;
    }


    public String getText(){
        return this.text;
    }

    public String getAdditionalContent(){
        return this.additionalContent;
    }

    public String getSearchEngineFeature(){
        return this.searchEngineFeature;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("%s|-|%s|-|%s|-|%s|-|%s", this.text, this.additionalContent, this.type.toString(), this.searchEngineFeature, this.horusTermIdentifier.toString());
    }



}
