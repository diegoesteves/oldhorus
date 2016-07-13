package org.aksw.horus.search.result;

import org.aksw.horus.core.util.Global;
import org.aksw.horus.search.query.MetaQuery;
import org.aksw.horus.search.web.WebResourceVO;

import java.util.List;

/**
 * Created by dnes on 01/06/16.
 */
public interface ISearchResult {

    public Long getTotalHitCount();

    public List<WebResourceVO> getWebResources();

    public MetaQuery getQuery() ;

    public String getLanguage();

    public boolean isCached();

}
