/**
 *
 */
package org.aksw.horus.search.crawl;

import java.util.concurrent.Callable;
import org.aksw.horus.core.util.Global;
import org.aksw.horus.search.result.ISearchResult;
import org.aksw.horus.search.web.ISearchEngine;

public class SearchResultCallable implements Callable<ISearchResult> {

    private String query;
    private Global.NERType type;
    private ISearchEngine engine;

    public SearchResultCallable(String query, Global.NERType type, ISearchEngine engine) {

        this.query      = query;
        this.type       = type;
        this.engine     = engine;
    }

    @Override
    public ISearchResult call() throws Exception {
        return this.engine.getSearchResults(this.query, this.type);
    }

}
