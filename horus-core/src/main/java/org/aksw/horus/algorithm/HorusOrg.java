package org.aksw.horus.algorithm;

import org.aksw.horus.core.HorusModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by dnes on 24/06/16.
 */
public class HorusOrg {
    /*

    LOGO-based
fffdd
    - opencv_haartraining -> obsolete
    - opencv_traincascade (newer) supports both Haar [148] and [87] (LBP - Local Binary Patterns) features
    - LBP are faster


    - opencv_createsamples -> produces a dataset of positive examples (*.vec)
    - opencv_performance -> evaluate the quality of classifiers (opencv_haartraining)

    NEGATIVE EXAMPLES
    bg.txt (containing the file path)

    POSITIVE EXAMPLES
    may generate from command line, i.e. [opencv_createsamples] or using a database



     */

    private static final Logger LOGGER = LoggerFactory.getLogger(HorusModel.class);

    public HorusOrg() {

    }

    public static void main(String[] args) {


    }


}

