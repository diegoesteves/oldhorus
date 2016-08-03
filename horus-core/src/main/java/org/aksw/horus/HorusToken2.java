package org.aksw.horus;

import org.aksw.horus.search.HorusEvidence;

import java.util.ArrayList;

/**
 * Created by dnes on 03/08/16.
 */
public class HorusToken2 {

    private int       _index;
    private int       _position; //global index
    private String    _token;
    private String    _postagger;
    private int       _ref_next_token; //it links to the next associated term

    private ArrayList<Double> prob;
    private ArrayList<HorusEvidence> evidences;


}
