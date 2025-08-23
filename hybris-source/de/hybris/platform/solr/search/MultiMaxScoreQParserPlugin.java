package de.hybris.platform.solr.search;

import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.QParser;
import org.apache.solr.search.QParserPlugin;

public class MultiMaxScoreQParserPlugin extends QParserPlugin
{
    public QParser createParser(String qstr, SolrParams localParams, SolrParams params, SolrQueryRequest req)
    {
        return (QParser)new MultiMaxScoreQParser(qstr, localParams, params, req);
    }
}
