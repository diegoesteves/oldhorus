package org.aksw.horus;

/**
 * Created by dnes on 12/04/16.
 */
public class WebImage {

    private String url                      = "";
    private String queryString                    = "";


    public WebImage(String queryString, String url) {
        this.queryString = queryString;
        this.url = url;
    }
}
