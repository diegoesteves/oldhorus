package org.aksw.horus.search.web;

/**
 * Created by dnes on 01/06/16.
 */
public abstract class WebResourceVO implements IWebResource {

    private String          title;
    private String          url;
    private int             searchrank = 0;
    private boolean         cached     = false;
    private String          query;
    private String          language;
    private int             hit = 0;

    public WebResourceVO(){

    }
    public WebResourceVO(String query, String url){
        this.query = query;
        this.url = url;
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
    public String getQuery() {
        return this.query;
    }
    public void setQuery(String query) {
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
