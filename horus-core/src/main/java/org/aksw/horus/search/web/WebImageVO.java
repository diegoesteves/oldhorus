package org.aksw.horus.search.web;

/**
 * Created by dnes on 12/04/16.
 */
public class WebImageVO extends WebResourceVO {

    private String    fileName; //photo_name
    private String    filePath;  //photo_dir
    private WebSiteVO site; //photo_site_url


    public WebImageVO(String queryString, String imgURL, String websiteURL) {
        this.setQuery(queryString);
        this.setUrl(imgURL);
        this.site = new WebSiteVO(websiteURL);
    }

    public void setImageFilePath(String value){
        this.filePath = value;
    }
    public String getImageFilePath(){
        return this.filePath;
    }
    public void setImageFileName(String value){
        this.fileName = value;
    }
    public String getImageFileName(){
        return this.fileName;
    }
    public WebSiteVO getWebSite(){
        return this.site;
    }


}
