package org.aksw.horus.search.web.googleapi;

import java.util.List;

import org.aksw.horus.search.result.ISearchResult;
import org.aksw.horus.search.web.WebResourceVO;

public class GoogleSearchResult implements ISearchResult {

  @Override
  public Long getTotalHitCount() {
    throw new UnsupportedOperationException();
  }

  @Override
  public List<WebResourceVO> getWebResources() {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getQuery() {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getLanguage() {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isCached() {
    throw new UnsupportedOperationException();
  }
}
