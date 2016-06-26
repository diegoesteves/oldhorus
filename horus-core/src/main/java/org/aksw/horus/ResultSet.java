package org.aksw.horus;

import java.net.URL;

/**
 * Created by dnes on 01/06/16.
 */
public abstract class ResultSet {

    private URL       _url;
    private String    _hash;
    private String    _filePath;

    public ResultSet(){

    }
    public ResultSet(URL url, String hash, String filePath){
        this._url = url;
        this._hash = hash;
        this._filePath = filePath;
    }

    public URL getURL(){
        return this._url;
    }

    public String getHash(){
        return this._hash;
    }

    public String getFilePath(){
        return this._filePath;
    }

    /**
     * is the current record classified as an instance of the current class?
     * @return
     */
    public abstract boolean isDetectedAs();

}
