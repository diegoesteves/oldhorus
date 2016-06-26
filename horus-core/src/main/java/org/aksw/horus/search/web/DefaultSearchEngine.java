package org.aksw.horus.search.web;

import org.aksw.horus.Horus;
import org.aksw.horus.search.cache.ICache;
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

    @Override
    public ISearchResult getSearchResults(String query) {

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

        if (searchResultsCache instanceof PersonCache) {
            for (WebImageVO img : result.getWebImages()) {
                System.out.println("title: " + img.getTitle());
                System.out.println("url: " + img.getUrl());
            }
        }else{
            for (WebSiteVO site : result.getWebSites()) {
                System.out.println("title: " + site.getTitle());
                System.out.println("text" + site.getText());
            }
        }

    }
}
