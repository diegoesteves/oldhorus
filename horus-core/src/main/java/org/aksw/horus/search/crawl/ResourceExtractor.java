package org.aksw.horus.search.crawl;

import org.aksw.horus.core.util.Global;
import org.aksw.horus.search.HorusEvidence;
import org.aksw.horus.search.query.MetaQuery;
import org.aksw.horus.search.result.ISearchResult;
import org.aksw.horus.search.web.ISearchEngine;
import org.aksw.horus.search.web.WebResourceVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by Diego on 7/12/2016.
 */
public class ResourceExtractor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceExtractor.class);
    private Map<Global.NERType, String> queries;

    public ResourceExtractor(List<MetaQuery> queries) {

    }

    public List<HorusEvidence> extract(ISearchEngine engine) {

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
        return null;
    }
}

