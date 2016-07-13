package org.aksw.horus.search.crawl;

import org.aksw.horus.core.util.Global;
import org.aksw.horus.search.result.ISearchResult;
import org.aksw.horus.search.web.ISearchEngine;
import org.aksw.horus.search.web.WebResourceVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Diego on 7/12/2016.
 */
public class ResourceCrawler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceCrawler.class);
    private Map<Global.NERType,String> queries;

    public ResourceCrawler(Map<Global.NERType, Integer> queries){

    }

    private Set<ISearchResult> crawl(ISearchEngine engine) {

        //query and resources returned
        Map<String, List<WebResourceVO>> cache;

        LOGGER.debug(" -> starting crawling: [SearchEngineClass: " + engine.getClass().toString() + "]");

        long start = System.currentTimeMillis();
        LOGGER.debug(" -> start getting search results");
        Set<ISearchResult> searchResults = this.generateSearchResultsInParallel(engine);
        LOGGER.debug(" -> finished getting search results in " + (System.currentTimeMillis() - start));

        return searchResults;
    }

    private Set<ISearchResult> generateSearchResultsInParallel(ISearchEngine engine) {

    }

