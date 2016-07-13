package org.aksw.horus.search.result;

import org.aksw.horus.core.util.Global;
import org.aksw.horus.search.web.WebResourceVO;

import java.util.List;

/**
 * Created by dnes on 01/06/16.
 */
public class DefaultSearchResult implements ISearchResult {

    private Long                 totalHitCount = 0L;
    private String               query;
    private String               language;
    private boolean              cached = false;
    private List<WebResourceVO>  webresources;
    private Global.NERType       type;

    public DefaultSearchResult(List<WebResourceVO> resources, Long totalHitCount, String query, boolean cached, String ln, Global.NERType type) {
        this.webresources      = resources;
        this.totalHitCount     = totalHitCount;
        this.query             = query;
        this.cached            = cached;
        this.language          = ln;
        this.type              = type;
    }

    @Override
    public Global.NERType getNERType(){return this.type;}

    @Override
    public Long getTotalHitCount() {
        return this.totalHitCount != null ? this.totalHitCount : 0L;
    }

    @Override
    public List<WebResourceVO> getWebResources() {
        return this.webresources;
    }

    public String getLanguage() {
        return this.language;
    }

    public String getQuery() {
        return query;
    }

    @Override
    public boolean isCached() {
        // TODO Auto-generated method stub
        return this.cached;
    }

}
