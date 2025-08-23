package de.hybris.platform.solrfacetsearch.interceptors;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.solrfacetsearch.enums.KeywordRedirectMatchType;
import de.hybris.platform.solrfacetsearch.model.redirect.SolrFacetSearchKeywordRedirectModel;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class SolrFacetSearchKeywordRedirectValidateInterceptor implements ValidateInterceptor
{
    public void onValidate(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(!(model instanceof SolrFacetSearchKeywordRedirectModel))
        {
            return;
        }
        SolrFacetSearchKeywordRedirectModel solrFacetSearchKeywordRedirectModel = (SolrFacetSearchKeywordRedirectModel)model;
        if(solrFacetSearchKeywordRedirectModel.getMatchType() == KeywordRedirectMatchType.REGEX)
        {
            String keyword = solrFacetSearchKeywordRedirectModel.getKeyword();
            try
            {
                Pattern.compile(keyword);
            }
            catch(PatternSyntaxException e)
            {
                throw new InterceptorException("Given pattern is not a valid regular expression: " + keyword, e);
            }
        }
    }
}
