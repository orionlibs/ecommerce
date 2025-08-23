package de.hybris.platform.campaigns.dao.impl;

import com.google.common.collect.ImmutableMap;
import de.hybris.platform.campaigns.dao.CampaignDao;
import de.hybris.platform.campaigns.model.CampaignModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class DefaultCampaignDao extends DefaultGenericDao<CampaignModel> implements CampaignDao
{
    private static final String CURRENT_DATETIME_QUERY_PARAM = "now";
    private static final String FIND_ACTIVE_CAMAPIGNS = "SELECT {Pk} FROM {Campaign} WHERE {enabled} = ?enabled AND ({startDate} IS NULL OR {startDate} <= ?now) AND ({endDate} IS NULL OR {endDate} >= ?now) ";


    public DefaultCampaignDao()
    {
        super("Campaign");
    }


    public List<CampaignModel> findActiveCampaigns(Date currentDateTime)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("currentDateTime", currentDateTime);
        FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {Pk} FROM {Campaign} WHERE {enabled} = ?enabled AND ({startDate} IS NULL OR {startDate} <= ?now) AND ({endDate} IS NULL OR {endDate} >= ?now) ");
        query.addQueryParameter("enabled", Boolean.TRUE);
        query.addQueryParameter("now", currentDateTime);
        return getFlexibleSearchService().search(query).getResult();
    }


    public CampaignModel findCampaignByCode(String code)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("code", code);
        ImmutableMap immutableMap = ImmutableMap.of("code", code);
        List<CampaignModel> campaigns = find((Map)immutableMap);
        ServicesUtil.validateIfSingleResult(campaigns, "No campaign with given code[" + code + "] was found", "More than one campaign with given code [" + code + "] was found");
        return campaigns.iterator().next();
    }


    public List<CampaignModel> findAllCampaigns()
    {
        return find();
    }
}
