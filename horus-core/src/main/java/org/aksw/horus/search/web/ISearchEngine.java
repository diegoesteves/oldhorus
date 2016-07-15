package org.aksw.horus.search.web;

import org.aksw.horus.core.util.Global;
import org.aksw.horus.search.query.MetaQuery;
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
    public ISearchResult getSearchResults(MetaQuery query);


    /**
     *
     * @param query
     * @return
     */
    public ISearchResult query(MetaQuery query);

    /**
     *
     * @param query
     * @return
     */
    public String generateQuery(MetaQuery query);

    /**
     *
     * @param query
     * @return
     */
    public Long getNumberOfResults(MetaQuery query);
}
