package org.aksw.horus.algorithm.language;

/**
 * Created by Diego Esteves on 7/13/2016.
 */
import java.util.ArrayList;
import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;
import com.cybozu.labs.langdetect.Language;
import org.apache.commons.codec.language.bm.Languages;

public class LangDetectSample {

    //https://code.google.com/archive/p/language-detection/
    //http://www.slideshare.net/shuyo/language-detection-library-for-java
    //http://tika.apache.org/1.5/detection.html#Language_Detection

    public void init(String profileDirectory) throws LangDetectException {
        DetectorFactory.loadProfile(profileDirectory);
    }

    public String detect(String text) throws LangDetectException {
        Detector detector = DetectorFactory.create();
        detector.append(text); return detector.detect();
    }

    public ArrayList detectLangs(String text) throws LangDetectException {
        Detector detector = DetectorFactory.create();
        detector.append(text);
        return detector.getProbabilities();
    }

    public void main(String[] args) throws Exception{
        ArrayList<Languages> langs = detectLangs("diego esteves");
       // for (Languages ln: langs){
        //    System.out.println(ln.getLanguages().);
       // }
    }
}