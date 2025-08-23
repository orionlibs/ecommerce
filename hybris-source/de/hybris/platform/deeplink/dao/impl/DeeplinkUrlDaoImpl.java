package de.hybris.platform.deeplink.dao.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.deeplink.dao.DeeplinkUrlDao;
import de.hybris.platform.deeplink.model.rules.DeeplinkUrlModel;
import de.hybris.platform.deeplink.model.rules.DeeplinkUrlRuleModel;
import de.hybris.platform.servicelayer.exceptions.ModelLoadingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class DeeplinkUrlDaoImpl implements DeeplinkUrlDao
{
    private static final Logger LOG = Logger.getLogger(DeeplinkUrlDaoImpl.class);
    private FlexibleSearchService searchService;
    private ModelService modelService;


    public DeeplinkUrlModel findDeeplinkUrlModel(String code)
    {
        DeeplinkUrlModel result = null;
        Map<Object, Object> params = new HashMap<>();
        params.put("code", code);
        String query = "SELECT {pk} FROM {DeeplinkUrl} WHERE {code} = ?code";
        SearchResult<DeeplinkUrlModel> searchResult = getSearchService().search("SELECT {pk} FROM {DeeplinkUrl} WHERE {code} = ?code", params);
        if(!searchResult.getResult().isEmpty())
        {
            result = searchResult.getResult().get(0);
        }
        else if(LOG.isDebugEnabled())
        {
            LOG.debug("No DeeplinkUrl item was found with search code: " + sanitize(code));
        }
        return result;
    }


    public List<DeeplinkUrlRuleModel> findDeeplinkUrlRules()
    {
        String query = "SELECT {pk} FROM {DeeplinkUrlRule} ORDER BY {priority}";
        SearchResult<DeeplinkUrlRuleModel> result = getSearchService().search("SELECT {pk} FROM {DeeplinkUrlRule} ORDER BY {priority}");
        return result.getResult();
    }


    public Object findObject(String pkString)
    {
        Object result = null;
        try
        {
            result = getModelService().get(PK.parse(pkString));
        }
        catch(ModelLoadingException e)
        {
            LOG.warn("Item with PK: " + sanitize(pkString) + " not found in the system" + e);
        }
        return result;
    }


    protected static String sanitize(String input)
    {
        String output = StringUtils.defaultString(input).trim();
        output = output.replaceAll("(\\r\\n|\\r|\\n)+", " ");
        output = StringEscapeUtils.escapeHtml(output);
        return output;
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    public FlexibleSearchService getSearchService()
    {
        return this.searchService;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public void setSearchService(FlexibleSearchService searchService)
    {
        this.searchService = searchService;
    }
}
