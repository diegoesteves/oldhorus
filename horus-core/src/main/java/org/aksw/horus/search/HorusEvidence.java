package org.aksw.horus.search;

import org.aksw.horus.search.query.MetaQuery;
import org.aksw.horus.search.web.WebImageVO;
import org.aksw.horus.search.web.WebResourceVO;

import java.util.List;

/**
 * Created by Diego Esteves on 7/13/2016.
 */
public class  HorusEvidence {

    private MetaQuery query;
    private List<WebResourceVO> resources;

    public HorusEvidence(MetaQuery query, List<WebResourceVO> resources){
        this.resources = resources;
        this.query = query;
    }

    public MetaQuery getQuery(){
        return this.query;
    }

    public List<WebResourceVO> getResources(){
        return this.resources;
    }

    public void addImage(WebImageVO img) {
        this.resources.add(img);
    }



}
