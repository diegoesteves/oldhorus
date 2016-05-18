package org.aksw.horus;

/**
 * Created by dnes on 12/04/16.
 */
public class WebSite {

    private String text                     = "";
    private String title                    = "";
    private String url                      = "";
    private String query                    = "";


    public WebSite(String query, String url) {
        this.query = query;
        this.url = url;
    }

}
