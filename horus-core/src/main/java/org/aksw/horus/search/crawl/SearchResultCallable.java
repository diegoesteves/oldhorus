/**
 *
 */
package org.aksw.horus.search.crawl;

import java.util.concurrent.Callable;
import org.aksw.horus.core.util.Global;
import org.aksw.horus.search.query.MetaQuery;
import org.aksw.horus.search.result.ISearchResult;
import org.aksw.horus.search.web.ISearchEngine;

public class SearchResultCallable implements Callable<ISearchResult> {

    private MetaQuery query;
    private ISearchEngine engine;

    public SearchResultCallable(MetaQuery query, ISearchEngine engine) {
        this.query      = query;
        this.engine     = engine;
    }

    @Override
    public ISearchResult call() throws Exception {
        return this.engine.getSearchResults(this.query);
    }

}
