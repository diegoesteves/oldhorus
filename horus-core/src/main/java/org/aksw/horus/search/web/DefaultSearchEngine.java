package org.aksw.horus.search.web;

import org.aksw.horus.Horus;
import org.aksw.horus.core.util.Global;
import org.aksw.horus.core.util.ImageManipulation;
import org.aksw.horus.search.cache.ICache;
import org.aksw.horus.search.query.MetaQuery;
import org.aksw.horus.search.result.ISearchResult;
import org.aksw.horus.search.solr.PersonCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by dnes on 01/06/16.
 */
public abstract class DefaultSearchEngine implements ISearchEngine {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSearchEngine.class);
    protected ICache<ISearchResult> searchResultsCache = new PersonCache();
    private static String BING_API_KEY;
    private static String NUMBER_OF_SEARCH_RESULTS;
    private static String IMG_ROOT_DIR;

    public DefaultSearchEngine(){
        if ( Horus.HORUS_CONFIG != null ) {
            BING_API_KEY = Horus.HORUS_CONFIG.getStringSetting("crawl", "BING_API_KEY");
            NUMBER_OF_SEARCH_RESULTS = Horus.HORUS_CONFIG.getStringSetting("crawl", "NUMBER_OF_SEARCH_RESULTS");
            IMG_ROOT_DIR = Horus.HORUS_CONFIG.getStringSetting("image", "IMG_PERSON_ROOT_DIR");
        }
    }
    @Override
    public ISearchResult getSearchResults(MetaQuery query) {

        //type = PER, LOC, ORG
        if (searchResultsCache.contains(query.toString())) {
            LOGGER.info(String.format("Query: '%s' cached! Starting to get from cache!", query.toString()));
            ISearchResult result = searchResultsCache.getEntry(query.toString());
            return result;
        }
        return query(query);
    }

    public static void main(String[] args) {

        Horus.init();

        ICache<ISearchResult> searchResultsCache = new PersonCache();

        ISearchResult result = searchResultsCache.getEntry("diego");


            for (WebResourceVO r : result.getWebResources()) {

                if (searchResultsCache instanceof PersonCache) {
                    WebImageVO img = (WebImageVO)r;
                    System.out.println("title: " + img.getTitle());
                    System.out.println("url: " + img.getUrl());
                }else {
                    WebSiteVO site = (WebSiteVO)r;
                    System.out.println("title: " + site.getTitle());
                    System.out.println("text" + site.getText());
                }

            }
        }

    public static boolean resourcesCached(String query) throws Exception{

        ImageManipulation imgHelp = new ImageManipulation();

        //images already cached
        if (imgHelp.directoryExists(IMG_ROOT_DIR + query.hashCode()) &&
                !imgHelp.directoryIsEmpty(IMG_ROOT_DIR + query.hashCode())){
            LOGGER.info("This query is cached, there is no need to search it again now...");
            return true;
        }
        //query has not been executed yet, starting cache
        imgHelp.createDirectory(IMG_ROOT_DIR + query.hashCode());
        return false;
    }
}
