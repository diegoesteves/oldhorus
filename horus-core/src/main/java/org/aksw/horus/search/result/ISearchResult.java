package org.aksw.horus.search.result;

import org.aksw.horus.search.web.WebResourceVO;

import java.util.List;

/**
 * Created by dnes on 01/06/16.
 */
public interface ISearchResult {

    public Long getTotalHitCount();

    public List<WebResourceVO> getWebResources();

    public String getQuery() ;

    public String getLanguage();

    public boolean isCached();

}
