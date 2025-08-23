package de.hybris.platform.basecommerce.site.dao.impl;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.basecommerce.site.dao.BaseSiteDao;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

public class BaseSiteDaoImpl extends AbstractItemDao implements BaseSiteDao
{
    private static final Logger LOG = Logger.getLogger(BaseSiteDaoImpl.class);


    public List<BaseSiteModel> findAllBaseSites()
    {
        List<BaseSiteModel> result = null;
        String query = "SELECT {pk} FROM {BaseSite}";
        SearchResult<BaseSiteModel> searchResult = search(new FlexibleSearchQuery("SELECT {pk} FROM {BaseSite}"));
        if(!searchResult.getResult().isEmpty())
        {
            result = searchResult.getResult();
        }
        else
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("No BaseSite item was found");
            }
            result = new ArrayList<>();
        }
        return result;
    }


    public BaseSiteModel findBaseSiteByUID(String siteUid)
    {
        BaseSiteModel result = null;
        Map<Object, Object> params = new HashMap<>();
        params.put("uid", siteUid);
        String query = "SELECT {pk} FROM {BaseSite} WHERE {uid} = ?uid";
        SearchResult<BaseSiteModel> searchResult = search("SELECT {pk} FROM {BaseSite} WHERE {uid} = ?uid", params);
        if(!searchResult.getResult().isEmpty())
        {
            result = searchResult.getResult().get(0);
        }
        else if(LOG.isDebugEnabled())
        {
            LOG.debug("No BaseSite item was found with search uid: " + siteUid);
        }
        return result;
    }
}
