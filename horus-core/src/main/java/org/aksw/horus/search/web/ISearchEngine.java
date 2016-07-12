package org.aksw.horus.search.web;

import org.aksw.horus.search.result.ISearchResult;

/**
 * Created by dnes on 01/06/16.
 */
public interface ISearchEngine {

    /**
     *
     * @param query
     * @return
     */
    public ISearchResult getSearchResults(String query);

    /**
     *
     */
    public ISearchResult query(String query);

    /**
     *
     * @param query
     * @return
     */
    public String generateQuery(String query);

    /**
     *
     * @param query
     * @return
     */
    public Long getNumberOfResults(String query);
}
