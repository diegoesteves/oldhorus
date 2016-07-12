package org.aksw.horus.search.solr;

import org.aksw.horus.Horus;
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

    public void saveDocumentPersonImage(String photoId, String photoName,
                                        String photoTitle, String photoDir,
                                        String query, String queryHash, int queryTotal, int rank) throws Exception{

        SolrInputDocument document = new SolrInputDocument();
        document.addField("photo_id", photoId);
        document.addField("photo_name", photoName);
        document.addField("photo_title",photoTitle);
        document.addField("photo_dir", photoDir);
        document.addField("query", query);
        document.addField("query_hash", queryHash);
        document.addField("query_total", queryTotal);
        document.addField("photo_rank", rank);
        UpdateResponse response = solr.add(document);
        solr.commit();


    }
}
