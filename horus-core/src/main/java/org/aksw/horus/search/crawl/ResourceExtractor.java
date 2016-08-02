package org.aksw.horus.search.crawl;

import org.aksw.horus.Horus;
import org.aksw.horus.search.HorusEvidence;
import org.aksw.horus.search.cache.ICache;
import org.aksw.horus.search.query.MetaQuery;
import org.aksw.horus.search.result.ISearchResult;
import org.aksw.horus.search.solr.HorusCache;
import org.aksw.horus.search.web.ISearchEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Diego Esteves on 7/12/2016.
 */
public class ResourceExtractor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceExtractor.class);
    private List<MetaQuery>  queries;

    public ResourceExtractor(List<MetaQuery> queries) {
        this.queries = queries;
    }

    public List<HorusEvidence> extractAndCache(ISearchEngine engine) {

        //Map<MetaQuery, HorusEvidence> ret = new HashMap<>();
        List<HorusEvidence> ret = new ArrayList<>();

        Map<String, MetaQuery> cache = new HashMap<>();
        HorusCache solrCache = new HorusCache();
        Set<ISearchResult> searchResultsCached = new HashSet<>();

        LOGGER.debug(" -> starting extracting: [SearchEngineClass: " + engine.getClass().toString() + "]");
        long start = System.currentTimeMillis();
        LOGGER.debug(" -> filtering out the queries: " + String.valueOf(this.queries.size()));
        for (MetaQuery query: this.queries) {
            if (!cache.containsKey(query.toString())) {
                if (solrCache.contains(query.toString())){
                    searchResultsCached.add(solrCache.getEntry(query.toString()));
                }else{
                    cache.put(query.toString(), query);
                }
            }
        }
        Set<ISearchResult> searchResults = this.generateSearchResultsInParallel(engine, cache);
        LOGGER.debug(" -> start caching extracted " + (System.currentTimeMillis() - start));
        cacheSearchResults(searchResults);
        LOGGER.debug(" -> merging of cached and new results done");
        searchResults.addAll(searchResultsCached);
        LOGGER.debug(" -> adding evidences x query");
        for (ISearchResult result: searchResults){
            ret.put(result.getQuery(), new HorusEvidence(result.getQuery(), result.getWebResources()));
        }

       return ret;
    }

    private Set<ISearchResult> generateSearchResultsInParallel(ISearchEngine engine, Map<String, MetaQuery> finalQueries) {

        Set<ISearchResult> results = new HashSet<>();
        Set<SearchResultCallable> searchResultCallables = new HashSet<>();

        for (Map.Entry<String, MetaQuery> q : finalQueries.entrySet())
            searchResultCallables.add(new SearchResultCallable(q.getValue(), engine));

            LOGGER.debug(" -> Starting to crawl/get from cache " + searchResultCallables.size() + " search results with " +
                    Horus.HORUS_CONFIG.getIntegerSetting("crawl", "NUMBER_OF_SEARCH_RESULTS_THREADS") + " threads.");

            try {

                ExecutorService executor = Executors.newFixedThreadPool(Horus.HORUS_CONFIG.getIntegerSetting("crawl", "NUMBER_OF_SEARCH_RESULTS_THREADS"));
                int i = 1;
                for (Future<ISearchResult> result : executor.invokeAll(searchResultCallables)) {
                    results.add(result.get());
                    LOGGER.debug(" query " + i + ": " + result.get().getQuery().toString() + ": nr. websites = " + result.get().getWebResources().size());
                    i++;
                }
                executor.shutdownNow();
            } catch (Exception e) {

                e.printStackTrace();
            }

        return results;
    }

    private void cacheSearchResults(Set<ISearchResult> searchResults) {

        long start = System.currentTimeMillis();
        ICache<ISearchResult> cache = new HorusCache();
        for ( ISearchResult result : searchResults )
            cache.add(result);
        LOGGER.debug(String.format("  -> Caching took %sms", System.currentTimeMillis()-start));
    }
}

