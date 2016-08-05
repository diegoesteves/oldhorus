package org.aksw.horus.core.util;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * Created by dnes on 01/06/16.
 */
public class Constants {

    /* web resource */
    public static final String LUCENE_SEARCH_RESULT_FIELD_ID		                   = "id";
    public static final String LUCENE_SEARCH_RESULT_FIELD_QUERY_META                   = "query_meta";
    public static final String LUCENE_SEARCH_RESULT_FIELD_CREATED	                   = "created";

    public static final String LUCENE_SEARCH_RESULT_FIELD_QUERY_TEXT		           = "query_term";
    public static final String LUCENE_SEARCH_RESULT_FIELD_QUERY_ADDITIONAL_CONTENT     = "query_additional_content";
    public static final String LUCENE_SEARCH_RESULT_FIELD_QUERY_NER_TYPE		       = "query_ner_type";
    public static final String LUCENE_SEARCH_RESULT_FIELD_QUERY_SEARCH_ENGINE_FEATURE  = "query_search_engine_feature";
    public static final String LUCENE_SEARCH_RESULT_FIELD_QUERY_HIT_COUNT		       = "query_total_hit";

    public static final String LUCENE_SEARCH_RESULT_FIELD_RESOURCE_TITLE               = "resource_title";
    public static final String LUCENE_SEARCH_RESULT_FIELD_RESOURCE_URL		           = "resource_url";
    public static final String LUCENE_SEARCH_RESULT_FIELD_RESOURCE_SEARCH_RANK		   = "resource_search_rank";
    public static final String LUCENE_SEARCH_RESULT_FIELD_RESOURCE_LANGUAGE		       = "resource_language";
    /* web image */
    public static final String LUCENE_SEARCH_RESULT_FIELD_IMAGE_NAME                   = "webimage_filename";
    public static final String LUCENE_SEARCH_RESULT_FIELD_IMAGE_PATH                   = "webimage_filepath";
    /* web page */
    public static final String LUCENE_SEARCH_RESULT_FIELD_SITE_CONTENT	               = "website_text";
    public static final String LUCENE_SEARCH_RESULT_FIELD_SITE_PAGE_RANK	           = "website_page_rank";




    public static final String SEPARATOR = "\t";
    public static final String METAQUERY_SEPARATOR = "\\|-\\|";

    public static final String DBPEDIA_RESOURCE_NAMESPACE = "http://dbpedia.org/resource/";
    public static final String DE_DBPEDIA_RESOURCE_NAMESPACE = "http://de.dbpedia.org/resource/";
    public static final String FR_DBPEDIA_RESOURCE_NAMESPACE = "http://fr.dbpedia.org/resource/";
    public static final String FREEBASE_RESOURCE_NAMESPACE = "http://rdf.freebase.com/ns";
    public static final String DBPEDIA_ONTOLOGY_NAMESPACE = "http://dbpedia.org/ontology/";


    public static final Property HORUS_X = ResourceFactory.createProperty("http://horus.aksw.org/ontology/x");
    public static final Property RDFS_LABEL = ResourceFactory.createProperty("http://www.w3.org/2000/01/rdf-schema#label");
    public static final Property SKOS_ALT_LABEL = ResourceFactory.createProperty("http://www.w3.org/2004/02/skos/core#altLabel");
    public static final Property OWL_SAME_AS = ResourceFactory.createProperty("http://www.w3.org/2002/07/owl#sameAs");

    public static final String NO_LABEL = "no-label";
    public static final String NAMED_ENTITY_TAG_DELIMITER = "_";

    public static final Set<String> STOP_WORDS = new HashSet<String>();
    static {
        Collections.addAll(STOP_WORDS, "","''", ":", " ", "``", "`", "_NE_", "''", ",", "'", "'s", /*"-LRB-", "-RRB-",*/ ".", "-", "--", "i", "a", "about", "an", "and", "are", "as", "at", "be", "but", "by", "com", "for", "from",
                "how", "in", "is", "it", "of", "on", "or", "that", "the", "The", "was", "were", "à", "den",
                "under", "this", "to", "what", "when", "where", "who", "will", "with", "the", "www", "before", ",", "after", ";", "like", "and", "such", "-LRB-", "-RRB-", "-lrb-", "-rrb-", "aber", "als",
                "am", "an", "auch", "auf", "aus", "bei", "bin", "bis", "bist", "da", "dadurch", "daher", "darum", "das", "daß", "dass", "dein", "deine", "dem", "den", "der", "des", "dessen",
                "deshalb", "die", "dies", "dieser", "dieses", "doch", "dort", "du", "durch", "ein", "eine", "einem", "einen", "einer", "eines", "er", "es", "euer", "eure", "für", "hatte", "hatten",
                "hattest", "hattet", "hier", "hinter", "ich", "ihr", "ihre", "im", "in", "ist", "ja", "jede", "jedem", "jeden", "jeder", "jedes", "jener", "jenes", "jetzt", "kann", "kannst",
                "können", "könnt", "machen", "mein", "meine", "mit", "muß", "mußt", "musst", "müssen", "müßt", "nach", "nachdem", "nein", "nicht", "nun", "oder", "seid", "sein", "seine", "sich",
                "sie", "sind", "soll", "sollen", "sollst", "sollt", "sonst", "soweit", "sowie", "und", "unser", "unsere", "unter", "vom", "von", "vor", "wann", "warum", "weiter", "weitere", "wenn",
                "wer", "werde", "war", "wurde", "um", "werden", "werdet", "weshalb", "wie", "wieder", "wieso", "wir", "wird", "wirst", "wo", "woher", "wohin", "zu", "zum", "zur", "über",
                "alors", "au", "aucuns", "aussi", "autre", "avant", "avec", "avoir", "bon", "car", "ce", "cela", "ces", "ceux", "chaque", "ci", "comme", "comment",
                "dans", "des", "du", "dedans", "dehors", "depuis", "deux", "devrait", "doit", "donc", "dos", "droite", "début", "elle", "elles", "en", "encore",
                "essai", "est", "et", "eu", "fait", "faites", "fois", "font", "force", "haut", "hors", "ici", "il", "ils", "je", "juste", "la", "le", "les", "leur",
                "là", "ma", "maintenant", "mais", "mes", "mine", "moins", "mon", "mot", "même", "ni", "nommés", "notre", "nous", "nouveaux", "ou", "où", "par", "parce",
                "parole", "pas", "personnes", "peut", "peu", "pièce", "plupart", "pour", "pourquoi", "quand", "que", "quel", "quelle", "quelles", "quels", "qui", "sa",
                "sans", "ses", "seulement", "si", "sien", "son", "sont", "sous", "soyez", "sujet", "sur", "ta", "tandis", "tellement", "tels", "tes", "ton", "tous",
                "tout", "trop", "très", "tu", "valeur", "voie", "voient", "vont", "votre", "vous", "vu", "ça", "étaient", "état", "étions", "été", "être",
                "dean", "priscilla", "bates", "alan", "prinzessin", "bayern", "österreich", "daughter");
    }

    /**
     * Use this property to write new lines in files or stdouts
     */
    public static final String NEW_LINE_SEPARATOR	= System.getProperty("line.separator");

    public enum LANGUAGE {
        fr,
        en,
        de
    }
}
