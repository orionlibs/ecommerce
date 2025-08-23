package de.hybris.platform.solrfacetsearch.config.impl;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.redirect.SolrFacetSearchKeywordRedirectModel;
import de.hybris.platform.solrfacetsearch.search.SolrFacetSearchKeywordDao;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

public class SolrKeywordRedirectValidateInterceptor implements ValidateInterceptor
{
    private SolrFacetSearchKeywordDao solrFacetSearchKeywordDao;


    public void onValidate(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof SolrFacetSearchKeywordRedirectModel)
        {
            SolrFacetSearchKeywordRedirectModel keyword = (SolrFacetSearchKeywordRedirectModel)model;
            SolrFacetSearchConfigModel config = keyword.getFacetSearchConfig();
            if(config == null)
            {
                throw new InterceptorException("Keyword redirects need to have facet search config.");
            }
            List<SolrFacetSearchKeywordRedirectModel> list = this.solrFacetSearchKeywordDao.findKeywords(keyword.getKeyword(), keyword
                            .getMatchType(), config.getName(), keyword.getLanguage().getIsocode());
            for(SolrFacetSearchKeywordRedirectModel ind : list)
            {
                if(!ind.equals(keyword))
                {
                    throw new InterceptorException("Keyword redirects with the same keyword and match type already exist.");
                }
            }
        }
    }


    @Required
    public void setSolrFacetSearchKeywordDao(SolrFacetSearchKeywordDao solrFacetSearchKeywordDao)
    {
        this.solrFacetSearchKeywordDao = solrFacetSearchKeywordDao;
    }
}
