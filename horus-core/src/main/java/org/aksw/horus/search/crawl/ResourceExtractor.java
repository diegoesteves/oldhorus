package org.aksw.horus.search.crawl;

import org.aksw.horus.Horus;
import org.aksw.horus.core.util.Global;
import org.aksw.horus.search.HorusEvidence;
import org.aksw.horus.search.query.MetaQuery;
import org.aksw.horus.search.result.ISearchResult;
import org.aksw.horus.search.web.ISearchEngine;
import org.aksw.horus.search.web.WebResourceVO;
import org.apache.lucene.util.packed.DirectMonotonicReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Diego on 7/12/2016.
 */
public class ResourceExtractor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceExtractor.class);
    private List<MetaQuery> queries;

    public ResourceExtractor(List<MetaQuery> queries) {
        this.queries = queries;
    }

    public List<HorusEvidence> extract(ISearchEngine engine) {

        //query and resources returned
        Map<String, List<WebResourceVO>> cache;


        if (!cache.containsKey(this.model) ) {

            LOGGER.debug(" -> starting crawling: [SearchEngineClass: " + engine.getClass().toString() + "]");

            long start = System.currentTimeMillis();
            LOGGER.debug(" -> start getting search results");
            Set<ISearchResult> searchResults = this.generateSearchResultsInParallel(engine);
            LOGGER.debug(" -> finished getting search results in " + (System.currentTimeMillis() - start));

        }

        return searchResults;
    }

    private Set<ISearchResult> generateSearchResultsInParallel(ISearchEngine engine) {
        Set<ISearchResult> results = new HashSet<>();
        Set<SearchResultCallable> searchResultCallables = new HashSet<>();

        for ( MetaQuery q : this.queries) {
            searchResultCallables.add(new SearchResultCallable(q, engine));
        }

        LOGGER.debug(" -> Starting to crawl/get from cache " + searchResultCallables.size() + " search results with " +
                Horus.HORUS_CONFIG.getIntegerSetting("crawl", "NUMBER_OF_SEARCH_RESULTS_THREADS") + " threads.");

        try {

            ExecutorService executor = Executors.newFixedThreadPool(Horus.HORUS_CONFIG.getIntegerSetting("crawl", "NUMBER_OF_SEARCH_RESULTS_THREADS"));
            int i=1;
            for ( Future<ISearchResult> result : executor.invokeAll(searchResultCallables)) {
                results.add(result.get());
                LOGGER.debug(" query " + i + ": " + result.get().getQuery().toString() + ": nr. websites = " + result.get().getWebResources().size());
                i++;
            }
            executor.shutdownNow();
        }
        catch (Exception e) {

            e.printStackTrace();
        }

        return results;
    }
}

