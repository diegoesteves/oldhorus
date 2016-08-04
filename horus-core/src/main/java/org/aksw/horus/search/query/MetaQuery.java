package org.aksw.horus.search.query;

import org.aksw.horus.Horus;
import org.aksw.horus.core.util.Global;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Diego Esteves on 7/13/2016.
 */
public class MetaQuery {

    private Global.NERType type;
    //represents
    private String text;
    private boolean isComposedTerm;
    private String additionalContent;
    private String searchEngineFeature;

    public MetaQuery(String metaQueryStr){
        String[] qsplited = metaQueryStr.split("\\|-\\|");
        this.text = qsplited[0];
        this.additionalContent = qsplited[1];
        this.type = Global.NERType.valueOf(qsplited[2]);
        this.searchEngineFeature = qsplited[3];
    }


    public MetaQuery(Global.NERType type, String text, String additionalContent){
        this.type = type;
        this.text = text;
        this.additionalContent = additionalContent;
        setSearchEngineFeature();
    }

    /**
     * has the Term been composed by a sequential structure?
     * @return true or false
     */
    public boolean isComposedTerm(){
        if (StringUtils.isNotEmpty(this.text))
            return (this.text.split(" ").length > 1);
        return false;
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
        return String.format("%s|-|%s|-|%s|-|%s", this.text, this.additionalContent, this.type.toString(), this.searchEngineFeature);
    }



}
