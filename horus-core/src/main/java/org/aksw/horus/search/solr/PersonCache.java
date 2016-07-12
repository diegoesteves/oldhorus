package org.aksw.horus.search.solr;

import org.aksw.horus.Horus;
import org.aksw.horus.core.util.Constants;
import org.aksw.horus.core.util.Global;
import org.aksw.horus.search.cache.ICache;
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

/**
 * Created by dnes on 01/06/16.
 */
public class PersonCache implements ICache<ISearchResult> {

    private SolrClient server;
    private static String SOLR_SERVER;
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonCache.class);

    public PersonCache(){
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

    private QueryResponse querySolrServer(SolrQuery query) throws Exception {
        return this.server.query(query);
    }

    private List<SolrInputDocument> searchResultToDocument(ISearchResult entry) {

        List<SolrInputDocument> documents = new ArrayList<SolrInputDocument>();

        if (entry.getWebResources().isEmpty()) {

            SolrInputDocument solrDocument = new SolrInputDocument();
            //solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_ID_FIELD, String.valueOf(entry.getQuery().toString().hashCode()));

            //common fields
            solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_RANK_FIELD, -1);
            solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_PAGE_RANK_FIELD, -1);
            solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_QUERY_FIELD, entry.getQuery());
            solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_CREATED_FIELD, new Date().getTime());
            solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_LANGUAGE, entry.getLanguage());
            solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_TITLE_FIELD, "");
            solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_URL_FIELD, "");
            solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_QUERY_FIELD, entry.getQuery().toString());
            solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_HIT_COUNT_FIELD, entry.getTotalHitCount());

            //img fields
            solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_IMG_ID_FIELD, "");
            solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_IMG_NAME_FIELD, "");
            solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_IMG_DIR_FIELD, "");

            documents.add(solrDocument);
        }
        else {

            for (WebResourceVO resource : entry.getWebResources()) {

                WebImageVO image = (WebImageVO) resource;

                SolrInputDocument solrDocument = new SolrInputDocument();
                //solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_ID_FIELD, (entry.getQuery().toString() + "\t" + site.getUrl()).hashCode());
                solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_HIT_COUNT_FIELD, image.getTotalHitCount());
                solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_RANK_FIELD, image.getSearchRank());
                //solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_PAGE_RANK_FIELD, image.getSearchRank());
                solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_CREATED_FIELD, new Date().getTime());
                solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_URL_FIELD, image.getWebSite().getUrl());
                solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_TITLE_FIELD, image.getImageFileName());
                //solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_CONTENT_FIELD, site.getText());
                //solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_TAGGED_FIELD, site.getTaggedText() == null ? "" : site.getTaggedText());
                solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_QUERY_FIELD, entry.getQuery().toString());
                solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_LANGUAGE, entry.getLanguage());

                //img fields
                //solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_IMG_ID_FIELD, image.getN );
                solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_IMG_NAME_FIELD, image.getImageFileName());
                solrDocument.addField(Constants.LUCENE_SEARCH_RESULT_IMG_DIR_FIELD, image.getImageFilePath());

                documents.add(solrDocument);
            }
        }

        return documents;
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
    public ISearchResult getEntry(String _query) {

        List<WebResourceVO> resources = new ArrayList<WebResourceVO>();
        Long hitCount = 0L;

        try {

            SolrQuery query = new SolrQuery(Constants.LUCENE_SEARCH_RESULT_QUERY_FIELD + ":\"" + _query + "\"").setRows(50);
            query.addField(Constants.LUCENE_SEARCH_RESULT_QUERY_FIELD);
            query.addField(Constants.LUCENE_SEARCH_RESULT_HIT_COUNT_FIELD);
            query.addField(Constants.LUCENE_SEARCH_RESULT_URL_FIELD);
            query.addField(Constants.LUCENE_SEARCH_RESULT_RANK_FIELD);
            query.addField(Constants.LUCENE_SEARCH_RESULT_PAGE_RANK_FIELD);
            query.addField(Constants.LUCENE_SEARCH_RESULT_CONTENT_FIELD);
            query.addField(Constants.LUCENE_SEARCH_RESULT_TITLE_FIELD);
            query.addField(Constants.LUCENE_SEARCH_RESULT_TAGGED_FIELD);
            query.addField(Constants.LUCENE_SEARCH_RESULT_LANGUAGE);
            QueryResponse response = this.querySolrServer(query);

            for (SolrDocument doc : response.getResults()) {

                String q = (String) doc.get(Constants.LUCENE_SEARCH_RESULT_QUERY_FIELD);
                //the pattern should also be saved into the SOLR cache.

                hitCount = (Long) doc.get(Constants.LUCENE_SEARCH_RESULT_HIT_COUNT_FIELD);

                if (!((String) doc.get(Constants.LUCENE_SEARCH_RESULT_URL_FIELD)).isEmpty()) { // empty cache hits should not become a website

                    WebImageVO img = new WebImageVO();

                    img.setTitle((String) doc.get(Constants.LUCENE_SEARCH_RESULT_TITLE_FIELD));
                    img.setUrl((String) doc.get(Constants.LUCENE_SEARCH_RESULT_URL_FIELD));
                    img.setSearchRank(((Long) doc.get(Constants.LUCENE_SEARCH_RESULT_RANK_FIELD)).intValue());
                    img.setCached(true);
                    img.setQuery(_query);
                    img.setLanguage((String) doc.get(Constants.LUCENE_SEARCH_RESULT_LANGUAGE));
                    img.setTotalHitCount(((Long) doc.get(Constants.LUCENE_SEARCH_RESULT_HIT_COUNT_FIELD)).intValue());

                    img.setImageFileName((String)doc.get(Constants.LUCENE_SEARCH_RESULT_IMG_NAME_FIELD));
                    img.setImageFilePath((String)doc.get(Constants.LUCENE_SEARCH_RESULT_IMG_DIR_FIELD));
                    img.setWebSite((String)doc.get(Constants.LUCENE_SEARCH_RESULT_URL_FIELD));

                    resources.add(img);
                }
            }

        } catch (Exception e) {
            LOGGER.error(e.toString());
        }

        //we do not define language now, because we rely only on resultset from search engines
        return new DefaultSearchResult(resources, hitCount, _query, true, "", Global.NERType.PER);

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
