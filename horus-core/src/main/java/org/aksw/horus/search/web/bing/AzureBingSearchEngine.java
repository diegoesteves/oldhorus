package org.aksw.horus.search.web.bing;

import net.billylieurance.azuresearch.*;
import net.billylieurance.azuresearch.AbstractAzureSearchQuery.AZURESEARCH_QUERYTYPE;
import org.aksw.horus.Horus;
import org.aksw.horus.core.util.Global;
import org.aksw.horus.core.util.ImageManipulation;
import org.aksw.horus.search.result.DefaultSearchResult;
import org.aksw.horus.search.result.ISearchResult;
import org.aksw.horus.search.solr.SolrHelper;
import org.aksw.horus.search.web.*;
import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dnes on 12/04/16.
 */
public class AzureBingSearchEngine extends DefaultSearchEngine {

    private static String BING_API_KEY;
    private static String NUMBER_OF_SEARCH_RESULTS;
    private static String IMG_ROOT_DIR;
    private static Logger LOGGER =  Logger.getLogger(AzureBingSearchEngine.class);
    private String query;

    public AzureBingSearchEngine(String query){
        if ( Horus.HORUS_CONFIG != null ) {
            BING_API_KEY = Horus.HORUS_CONFIG.getStringSetting("crawl", "BING_API_KEY");
            NUMBER_OF_SEARCH_RESULTS = Horus.HORUS_CONFIG.getStringSetting("crawl", "NUMBER_OF_SEARCH_RESULTS");
            IMG_ROOT_DIR = Horus.HORUS_CONFIG.getStringSetting("image", "IMG_PERSON_ROOT_DIR");
        }
        this.query = query;
    }

    public void setQuery(String query){
        this.query = query;
    }

    @Override
    public ISearchResult query(String query, Global.NERType type) {

        try {

            AzureSearchCompositeQuery aq = new AzureSearchCompositeQuery();
            aq.setSources(new AZURESEARCH_QUERYTYPE[]{
                    AZURESEARCH_QUERYTYPE.IMAGE
            });

            aq.setAppid(BING_API_KEY);
            aq.setLatitude("47.603450");
            aq.setLongitude("-122.329696"); //Seattle
            aq.setMarket("en-US");
            String strQuery = this.query;
            aq.setQuery(strQuery);
            aq.setImageFilters("Face:Face+Size:Small");
            //aq.setImageFilters("Size:Height:200+Size:Width:200+Face:Face");
            aq.setWebSearchOptions("DisableQueryAlterations");

            aq.doQuery();

            AzureSearchResultSet<AbstractAzureSearchResult> ars = aq.getQueryResult();   //https://msdn.microsoft.com/en-us/library/dd560913.aspx

            List<WebResourceVO> results = new ArrayList<>();

            int i = 0;
            int totalSites = ars.getASRs().size();

            LOGGER.info("Total de websites: " + totalSites);

            for (AbstractAzureSearchResult result : ars) {

                if (i > Integer.valueOf(NUMBER_OF_SEARCH_RESULTS)) break;

                //if ( ((AzureSearchWebResult) result).getUrl().startsWith("http://images.webgiftr.com/")
                //        || ((AzureSearchWebResult) result).getUrl().startsWith("http://www.calza.com/")) continue;

                WebSiteVO root = new WebSiteVO();
                //shared
                root.setTitle(result.getTitle());
                root.setUrl(((AzureSearchWebResult) result).getUrl());
                root.setSearchRank(i++);
                root.setCached(false);
                root.setQuery(query);
                root.setLanguage("");

                WebImageVO resource;
                if (type.equals(Global.NERType.PER) || type.equals(Global.NERType.LOC) || type.equals(Global.NERType.ORG)){
                    resource = new WebImageVO();

                    String image_id = ((AzureSearchImageResult) result).getId();
                    String image_type = ((AzureSearchImageResult) result).getContentType();
                    String image_url = ((AzureSearchImageResult) result).getMediaUrl();


                    resource.setImageFileName(image_id + "." + image_type.substring(image_type.lastIndexOf("/") + 1));
                    resource.setImageFilePath(IMG_ROOT_DIR + (this.query + "_" + type.toString()).hashCode() + "/");
                    resource.setWebSite(root);
                }else {
                   throw new NotImplementedException();
                }
                results.add(resource);


                /*if (!image_name.contains(".jpg") &&
                        !image_name.contains(".jpeg") &&
                        !image_name.contains(".bmp") &&
                        !image_name.contains(".gif") &&
                        !image_name.contains(".png") &&
                        !image_name.contains(".tiff")){
                    image_name+="."+image_type.substring(image_type.lastIndexOf("/")+1);
                }*/

                LOGGER.debug("image_url: " + resource.getUrl());
                LOGGER.debug("page title: " + resource.getWebSite().getTitle());

            }

            LOGGER.debug("process finished");

            return new DefaultSearchResult(results, ars.getWebTotal(), query, false, "", type);

        } catch (Exception e) {
            LOGGER.error(e.toString());
            return new DefaultSearchResult(new ArrayList<>(), 0L, query, false, "", type);

        }

    }

    public static void main(String[] args) {

        try{

            Horus.init();

            AzureBingSearchEngine engine = new AzureBingSearchEngine("Jens");
            engine.query("Jens", Global.NERType.PER);


            /*try {

                ImageManipulation imgHelp = new ImageManipulation();
                imgHelp.saveImage(resource.getUrl(), resource.getImageFilePath() + resource.getImageFileName());

                SolrHelper solrh = new SolrHelper();
                solrh.saveDocumentPersonImage(image_id,
                        image_name, page_title, IMG_ROOT_DIR + this.query.hashCode() + "/",
                        strQuery, String.valueOf(strQuery.hashCode()), totalSites, i);

                LOGGER.debug("Document has been saved: " + image_id);
                i++;

            } catch (Exception e) {
                LOGGER.error(e.toString());
            }
            */


        }catch (Exception e){

        }



    }

    @Override
    public String generateQuery(String query) {
        //return new BingQuery().generateQuery(query);
        return query;
    }
    @Override
    public Long getNumberOfResults(String query) {
        return 0L;
    }




}
