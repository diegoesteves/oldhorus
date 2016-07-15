package org.aksw.horus.search.web;

import org.aksw.horus.search.query.MetaQuery;
import org.apache.lucene.util.packed.DirectMonotonicReader;

/**
 * Created by dnes on 01/06/16.
 */
public abstract class WebResourceVO implements IWebResource {

    private String          title;
    private String          url;
    private int             searchrank = 0;
    private boolean         cached     = false;
    private MetaQuery       query;
    private String          language;
    private int             hit = 0;
    private String          cachedID;

    public WebResourceVO(){

    }

    public WebResourceVO(MetaQuery query){
        this.query = query;
    }

    public void setCachedID(String value){
        this.cachedID = value;
    }
    public String getCachedID(){
        return this.cachedID;
    }
    public void setTotalHitCount(int value){
        this.hit = value;
    }
    public int getTotalHitCount(){
        return this.hit;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return this.title;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public MetaQuery getQuery() {
        return this.query;
    }
    public void setQuery(MetaQuery query) {
        this.query = query;
    }
    public void setSearchRank(int rank) {
        this.searchrank = rank;
    }
    public int getSearchRank() {
        return this.searchrank;
    }
    public void setLanguage(String language) {
        this.language  = language;
    }
    public String getLanguage() {
        return this.language;
    }
    public boolean isCached() {
        return cached;
    }
    public void setCached(boolean cached) {
        this.cached = cached;
    }


}
