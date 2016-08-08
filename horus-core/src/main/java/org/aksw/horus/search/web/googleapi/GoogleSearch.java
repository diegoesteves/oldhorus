package org.aksw.horus.search.web.googleapi;

import org.aksw.horus.core.util.Global;
import org.aksw.horus.search.query.MetaQuery;
import org.aksw.horus.search.result.ISearchResult;
import org.aksw.horus.search.web.ISearchEngine;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author Ren&eacute; Speck <speck@informatik.uni-leipzig.de>
 *
 */
public class GoogleSearch implements ISearchEngine {

  // TODO: add to a config file
  String endpoint = "https://www.googleapis.com/customsearch/v1";
  String key = "AIzaSyB59Eh78kMVPZk_wmllS7yIhILe0TKCiWs";
  String cx = "006095080433644533118:hlzdv-8k5mu";
  String searchtype = "image";

  // search url
  String url;

  /**
   * Test.
   *
   * @param args
   */
  public static void main(final String[] args) {
    final GoogleSearch googleSearch = new GoogleSearch();
    googleSearch.query(new MetaQuery(Global.NERType.PER, "diego", "", 1));
  }

  /**
   *
   * Constructor.
   *
   */
  public GoogleSearch() {
    url = endpoint.concat("?key=").concat(key).concat("&cx=").concat(cx).concat("&searchtype=")
        .concat(searchtype).concat("&q=");
  }

  @Override
  public ISearchResult query(MetaQuery query) {
    final JSONObject results = httpGet(url.concat(query.toString()));
    System.out.println(results.toString(2));

    throw new UnsupportedOperationException();
  }

  @Override
  public ISearchResult getSearchResults(final MetaQuery query) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String generateQuery(final MetaQuery query) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Long getNumberOfResults(final MetaQuery query) {
    throw new UnsupportedOperationException();
  }

  /**
   * Sends a http get request and expects JSON object as response.
   *
   * @param request url
   * @return json object with results
   */
  private JSONObject httpGet(final String request) {

    try {
      final URL url = new URL(request);
      final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

      String line;
      final StringBuilder result = new StringBuilder();
      while ((line = rd.readLine()) != null) {
        result.append(line);
      }
      rd.close();

      return new JSONObject(result.toString());
    } catch (final Exception e) {
      System.out.println(e);
    }
    return new JSONObject();
  }
}
