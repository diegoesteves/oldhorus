package org.aksw.horus.search.web;

import org.aksw.horus.search.query.MetaQuery;

/**
 * Created by dnes on 12/04/16.
 */
public class WebSiteVO extends WebResourceVO {

    private String  text                     = "";
    private int     pagerank                 = 0;

    public WebSiteVO(MetaQuery query, String url) {
        this.setQuery(query);
        this.setUrl(url);
    }

    public WebSiteVO(){
    }

    public WebSiteVO(String url) {
        this.setUrl(url);
    }

    public int getPageRank() {
        return this.pagerank;
    }

    public void setPageRank(int pagerank) {
        this.pagerank = pagerank;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }



    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.getUrl() == null) ? 0 : this.getUrl().hashCode());
        return result;
    }


    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();
        builder.append("WebSiteVO [text.length=");
        builder.append(text.length());
        builder.append(", url=");
        builder.append(this.getUrl());
        builder.append(", pagerank=");
        builder.append(pagerank);
        builder.append("]");
        return builder.toString();
    }


}
