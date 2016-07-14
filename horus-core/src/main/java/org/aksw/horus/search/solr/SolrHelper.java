package org.aksw.horus.search.solr;

import org.aksw.horus.Horus;
import org.aksw.horus.core.util.Global;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;

/**
 * Created by dnes on 15/04/16.
 */
public class SolrHelper {


    private SolrClient solr;

    private static String SOLR_SERVER;
    private static Logger LOGGER =  Logger.getLogger(SolrHelper.class);

    public SolrHelper(){
        try{
            if ( Horus.HORUS_CONFIG != null ) {
                SOLR_SERVER = Horus.HORUS_CONFIG.getStringSetting("solr", "SERVER");
            }
            LOGGER.debug("Solr server: " + SOLR_SERVER);

            solr = new HttpSolrClient(SOLR_SERVER);
        }catch (Exception e){
            LOGGER.error(e.toString());
        }
    }

    public void saveDocumentPersonImage(String pTerm, String pAdditional,
                                        Global.NERType pNER, String pSearchEngineFeature,
                                        int pTotalHit, String pResourceTitle,
                                        String pResourceURL, String pResourceSearchRank,
                                        String pResourceLanguage, String pResourcePageRank,
                                        String pImageFileName, String pImageFilePath,
                                        String pSiteText) throws Exception {

        SolrInputDocument document = new SolrInputDocument();
        document.addField("query_term", pTerm);
        document.addField("query_additional_content", pAdditional);
        document.addField("query_ner_type", pNER.toString());
        document.addField("query_search_engine_feature", pSearchEngineFeature);
        document.addField("query_total_hit", pTotalHit);
        document.addField("resource_title", pResourceTitle);
        document.addField("resource_url", pResourceURL);
        document.addField("resource_search_rank", pResourceSearchRank);
        document.addField("resource_language", pResourceLanguage);
        document.addField("resource_page_rank", pResourcePageRank);
        document.addField("webimage_filename", pImageFileName);
        document.addField("webimage_filepath", pImageFilePath);
        document.addField("website_text", pSiteText);
        UpdateResponse response = solr.add(document);
        solr.commit();

    }
}
