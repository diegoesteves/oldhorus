package org.aksw.horus.search;

import org.aksw.horus.search.web.WebImageVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diego on 7/13/2016.
 */
public class HorusEvidence {

    private int position;
    private List<WebImageVO> images;

    public HorusEvidence(){
        this.images = new ArrayList<>();
    }

    public int getPosition(){
        return this.position;
    }

    public List<WebImageVO> getImages(){
        return this.images;
    }

    public void addImage(WebImageVO img) {
        this.images.add(img);
    }

}
