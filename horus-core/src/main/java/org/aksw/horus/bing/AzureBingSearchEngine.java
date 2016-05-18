package org.aksw.horus.bing;

import net.billylieurance.azuresearch.AbstractAzureSearchQuery.AZURESEARCH_QUERYTYPE;
import net.billylieurance.azuresearch.AbstractAzureSearchResult;
import net.billylieurance.azuresearch.AzureSearchCompositeQuery;
import net.billylieurance.azuresearch.AzureSearchImageResult;
import net.billylieurance.azuresearch.AzureSearchResultSet;
import org.aksw.horus.Horus;
import org.aksw.horus.core.util.ImageManipulation;
import org.aksw.horus.index.SolrHelper;
import org.apache.log4j.Logger;

/**
 * Created by dnes on 12/04/16.
 */
public class AzureBingSearchEngine {

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


    public void downloadAndCacheImages(){

        try{


            ImageManipulation imgHelp = new ImageManipulation();

            //images already cached
            if (imgHelp.directoryExists(IMG_ROOT_DIR + this.query.hashCode()) &&
                    !imgHelp.directoryIsEmpty(IMG_ROOT_DIR + this.query.hashCode())){
                LOGGER.info("This query is cached, there is no need to search it again now...");
                return;
            }

            //query has not been executed yet, starting cache
            imgHelp.createDirectory(IMG_ROOT_DIR + this.query.hashCode());


            AzureSearchCompositeQuery aq = new AzureSearchCompositeQuery();
            aq.setSources(new AZURESEARCH_QUERYTYPE[] {
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

            int i = 1;
            int totalSites = ars.getASRs().size();

            LOGGER.info("Total de websites: " + totalSites);

            for (AbstractAzureSearchResult result : ars) {

                if (i > Integer.valueOf(NUMBER_OF_SEARCH_RESULTS)) break;

                //if ( ((AzureSearchWebResult) result).getUrl().startsWith("http://images.webgiftr.com/")
                //        || ((AzureSearchWebResult) result).getUrl().startsWith("http://www.calza.com/")) continue;

                String image_url = ((AzureSearchImageResult) result).getMediaUrl();
                String page_title = result.getTitle();
                String image_id = ((AzureSearchImageResult) result).getId();
                String image_type = ((AzureSearchImageResult) result).getContentType();
                String image_name = image_id+"."+image_type.substring(image_type.lastIndexOf("/")+1);

                /*if (!image_name.contains(".jpg") &&
                        !image_name.contains(".jpeg") &&
                        !image_name.contains(".bmp") &&
                        !image_name.contains(".gif") &&
                        !image_name.contains(".png") &&
                        !image_name.contains(".tiff")){
                    image_name+="."+image_type.substring(image_type.lastIndexOf("/")+1);
                }*/

                LOGGER.debug("image_url: " + image_url);
                LOGGER.debug("page title: " + page_title);

                try {

                    imgHelp.saveImage(image_url, IMG_ROOT_DIR + this.query.hashCode() + "/" + image_name);


                    SolrHelper solrh = new SolrHelper();
                    solrh.saveDocumentPersonImage(image_id,
                            image_name, page_title, IMG_ROOT_DIR + this.query.hashCode() + "/" ,
                            strQuery, String.valueOf(strQuery.hashCode()), totalSites, i);

                    LOGGER.debug("Document has been saved: " + image_id);
                    i++;

                }catch (Exception e){
                    LOGGER.error(e.toString());
                }

            }


        }catch (Exception e){
            LOGGER.error(e.toString());
        }

        LOGGER.debug("process finished");

    }

    public static void main(String[] args) {

        try{

            Horus.init();

            AzureBingSearchEngine engine = new AzureBingSearchEngine("Jens");
            engine.downloadAndCacheImages();


        }catch (Exception e){

        }



    }

}
