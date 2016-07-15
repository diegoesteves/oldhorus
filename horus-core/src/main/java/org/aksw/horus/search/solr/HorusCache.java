package org.aksw.horus.search.solr;

import org.aksw.horus.Horus;
import org.aksw.horus.core.util.Constants;
import org.aksw.horus.core.util.Global;
import org.aksw.horus.search.cache.ICache;
import org.aksw.horus.search.query.MetaQuery;
import org.aksw.horus.search.result.DefaultSearchResult;
import org.aksw.horus.search.result.ISearchResult;
import org.aksw.horus.search.web.WebImageVO;
import org.aksw.horus.search.web.WebResourceVO;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.aksw.horus.core.util.Constants.*;

/**
 * Created by dnes on 01/06/16.
 */
public class HorusCache implements ICache<ISearchResult> {

    private SolrClient server;
    private static String SOLR_SERVER;
    private static final Logger LOGGER = LoggerFactory.getLogger(HorusCache.class);

    public HorusCache(){
        try{
            if ( Horus.HORUS_CONFIG != null ) {
                SOLR_SERVER = Horus.HORUS_CONFIG.getStringSetting("solr", "SERVER");
            }
            LOGGER.debug("Solr server: " + SOLR_SERVER);
            server = new HttpSolrClient(SOLR_SERVER);
        }catch (Exception e){
            LOGGER.error(e.toString());
        }
    }

    @Override
    public boolean contains(String identifier) {
        SolrDocumentList docList = null;
        try{
            SolrQuery query = new SolrQuery(Constants.LUCENE_SEARCH_RESULT_QUERY_FIELD + ":\"" + identifier + "\"").setRows(1);
            QueryResponse response = this.querySolrServer(query);
            docList = response.getResults();
        }catch (Exception e){
            LOGGER.error(e.toString());
        }
        return docList == null ? false : docList.size() > 0 ? true : false;
    }

    private QueryResponse querySolrServer(SolrQuery query) throws Exception {
        return this.server.query(query);
    }


    @Override
    public ISearchResult getEntry(String identifier) {

        List<WebResourceVO> resources = new ArrayList<WebResourceVO>();
        MetaQuery metaQuery = null;
        Long hitCount = 0L;

        try {

            /*
            SolrQuery query = new SolrQuery(
                    Constants.LUCENE_SEARCH_RESULT_QUERY_FIELD + ":\"" + identifier + "\"" + " AND " +
                    Constants.LUCENE_SEARCH_RESULT_QUERY_FIELD + ":\"" + identifier + "\"" + " AND " +
                    Constants.LUCENE_SEARCH_RESULT_QUERY_FIELD + ":\"" + identifier + "\"" + " AND " +
                    Constants.LUCENE_SEARCH_RESULT_QUERY_FIELD + ":\"" + identifier + "\""
            ).setRows(50);
            */
            SolrQuery query = new SolrQuery(
                    LUCENE_SEARCH_RESULT_FIELD_QUERY + ":\"" + identifier + "\"").setRows(50);

            query.addField(LUCENE_SEARCH_RESULT_FIELD_ID);
            query.addField(LUCENE_SEARCH_RESULT_FIELD_QUERY);
            query.addField(LUCENE_SEARCH_RESULT_FIELD_CREATED);
            query.addField(LUCENE_SEARCH_RESULT_FIELD_QUERY_TERM);
            query.addField(LUCENE_SEARCH_RESULT_FIELD_QUERY_ADDITIONAL_CONTENT);
            query.addField(LUCENE_SEARCH_RESULT_FIELD_QUERY_NER_TYPE);
            query.addField(LUCENE_SEARCH_RESULT_FIELD_QUERY_SEARCH_ENGINE_FEATURE);
            query.addField(LUCENE_SEARCH_RESULT_FIELD_QUERY_HIT_COUNT);
            query.addField(LUCENE_SEARCH_RESULT_FIELD_RESOURCE_TITLE);
            query.addField(LUCENE_SEARCH_RESULT_FIELD_RESOURCE_URL);
            query.addField(LUCENE_SEARCH_RESULT_FIELD_RESOURCE_SEARCH_RANK);
            query.addField(LUCENE_SEARCH_RESULT_FIELD_RESOURCE_LANGUAGE);
            query.addField(LUCENE_SEARCH_RESULT_FIELD_IMAGE_NAME);
            query.addField(LUCENE_SEARCH_RESULT_FIELD_IMAGE_PATH);
            query.addField(LUCENE_SEARCH_RESULT_FIELD_SITE_CONTENT);
            query.addField(LUCENE_SEARCH_RESULT_FIELD_SITE_PAGE_RANK);

            QueryResponse response = this.querySolrServer(query);

            if (response.getResults().size() > 0){

            }

            for (SolrDocument doc : response.getResults()) {

                String query_meta = (String) doc.get(LUCENE_SEARCH_RESULT_FIELD_QUERY);
                metaQuery = new MetaQuery(query_meta);
                hitCount = (Long) doc.get(Constants.LUCENE_SEARCH_RESULT_FIELD_QUERY_HIT_COUNT);

                if (!((String) doc.get(LUCENE_SEARCH_RESULT_FIELD_RESOURCE_URL)).isEmpty()) {

                    WebImageVO img = new WebImageVO();

                    img.setCachedID((String) doc.get(LUCENE_SEARCH_RESULT_FIELD_ID));

                    img.setTitle((String) doc.get(LUCENE_SEARCH_RESULT_FIELD_RESOURCE_TITLE));
                    img.setUrl((String) doc.get(LUCENE_SEARCH_RESULT_FIELD_RESOURCE_URL));
                    img.setSearchRank(((Long) doc.get(LUCENE_SEARCH_RESULT_FIELD_RESOURCE_SEARCH_RANK)).intValue());
                    img.setCached(true);
                    img.setQuery(metaQuery);
                    img.setLanguage((String) doc.get(LUCENE_SEARCH_RESULT_FIELD_RESOURCE_LANGUAGE));
                    img.setTotalHitCount(((Long) doc.get(LUCENE_SEARCH_RESULT_FIELD_QUERY_HIT_COUNT)).intValue());

                    img.setImageFileName((String)doc.get(LUCENE_SEARCH_RESULT_FIELD_IMAGE_NAME));
                    img.setImageFilePath((String)doc.get(LUCENE_SEARCH_RESULT_FIELD_IMAGE_PATH));
                    img.setWebSite((String)doc.get(LUCENE_SEARCH_RESULT_URL_FIELD));

                    resources.add(img);
                }
            }

        } catch (Exception e) {
            LOGGER.error(e.toString());
        }

        //we do not define language now, because we rely only on resultset from search engines
        return new DefaultSearchResult(resources, hitCount, metaQuery, true);

    }

    private List<SolrInputDocument> searchResultToDocument(ISearchResult entry) {

        List<SolrInputDocument> documents = new ArrayList<SolrInputDocument>();

        if (entry.getWebResources().isEmpty()) {

            SolrInputDocument solrDocument = new SolrInputDocument();

            solrDocument.addField(LUCENE_SEARCH_RESULT_FIELD_QUERY, entry.getQuery().toString());
            solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_FIELD_CREATED, new Date().getTime());

            solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_FIELD_QUERY_TERM, entry.getQuery().getTerm());
            solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_FIELD_QUERY_ADDITIONAL_CONTENT, entry.getQuery().getAdditionalContent() );
            solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_FIELD_QUERY_NER_TYPE, entry.getQuery().getType().toString());
            solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_FIELD_QUERY_SEARCH_ENGINE_FEATURE,  entry.getQuery().getSearchEngineFeature());
            solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_FIELD_QUERY_HIT_COUNT, entry.getTotalHitCount());

            solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_FIELD_RESOURCE_TITLE, "");
            solrDocument.addField(LUCENE_SEARCH_RESULT_FIELD_RESOURCE_URL, "");
            solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_FIELD_RESOURCE_SEARCH_RANK, -1);
            solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_FIELD_RESOURCE_LANGUAGE, "");

            solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_FIELD_IMAGE_NAME, "");
            solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_FIELD_IMAGE_PATH, "");

            solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_FIELD_SITE_CONTENT, "");
            solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_FIELD_SITE_PAGE_RANK, -1);


            documents.add(solrDocument);
        }
        else {

            for (WebResourceVO resource : entry.getWebResources()) {

                WebImageVO image = (WebImageVO) resource;

                SolrInputDocument solrDocument = new SolrInputDocument();

                solrDocument.addField(LUCENE_SEARCH_RESULT_FIELD_QUERY, entry.getQuery().toString());
                solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_FIELD_CREATED, new Date().getTime());

                solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_FIELD_QUERY_TERM, entry.getQuery().getTerm());
                solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_FIELD_QUERY_ADDITIONAL_CONTENT, entry.getQuery().getAdditionalContent() );
                solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_FIELD_QUERY_NER_TYPE, entry.getQuery().getType().toString());
                solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_FIELD_QUERY_SEARCH_ENGINE_FEATURE,  entry.getQuery().getSearchEngineFeature());
                solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_FIELD_QUERY_HIT_COUNT, entry.getTotalHitCount());

                solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_FIELD_RESOURCE_TITLE, image.getTitle());
                solrDocument.addField(LUCENE_SEARCH_RESULT_FIELD_RESOURCE_URL, image.getUrl());
                solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_FIELD_RESOURCE_SEARCH_RANK, image.getSearchRank());
                solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_FIELD_RESOURCE_LANGUAGE, image.getLanguage());

                solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_FIELD_IMAGE_NAME, image.getImageFileName());
                solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_FIELD_IMAGE_PATH, image.getImageFilePath());

                solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_FIELD_SITE_CONTENT, image.getWebSite().getText());
                solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_FIELD_SITE_PAGE_RANK, image.getWebSite().getPageRank());


                documents.add(solrDocument);
            }
        }

        return documents;
    }

    @Override
    public ISearchResult add(ISearchResult entry) {

        try {
            if (!entry.isCached()) {
                this.server.add(searchResultToDocument(entry));
                LOGGER.info(String.format("Query: '%s' was not found in the cache, starting to query!", entry.getQuery()));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return entry;
    }

    @Override
    public List<ISearchResult> addAll(List<ISearchResult> listToAdd) {
        for (ISearchResult result : listToAdd ) this.add(result);
        try {
            this.server.commit();
        }
        catch (java.io.CharConversionException e ) {
            e.printStackTrace();
        }
        catch (SolrServerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return listToAdd;
    }

    @Override
    public ISearchResult removeEntryByPrimaryKey(String primaryKey) {
        throw new RuntimeException("not yet implemented");
    }

    @Override
    public boolean updateEntry(ISearchResult object) {
        throw new RuntimeException("not yet implemented");
    }
}
