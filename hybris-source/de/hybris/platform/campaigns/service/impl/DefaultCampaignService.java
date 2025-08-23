package de.hybris.platform.campaigns.service.impl;

import de.hybris.platform.campaigns.dao.CampaignDao;
import de.hybris.platform.campaigns.model.CampaignModel;
import de.hybris.platform.campaigns.service.CampaignService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCampaignService implements CampaignService
{
    private CampaignDao campaignDao;
    private TimeService timeService;


    public List<CampaignModel> getAllCampaigns()
    {
        return getCampaignDao().findAllCampaigns();
    }


    public List<CampaignModel> getActiveCampaigns()
    {
        return getCampaignDao().findActiveCampaigns(getTimeService().getCurrentTime());
    }


    public CampaignModel getCampaignByCode(String code)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("code", code);
        return getCampaignDao().findCampaignByCode(code);
    }


    protected CampaignDao getCampaignDao()
    {
        return this.campaignDao;
    }


    @Required
    public void setCampaignDao(CampaignDao campaignDao)
    {
        this.campaignDao = campaignDao;
    }


    protected TimeService getTimeService()
    {
        return this.timeService;
    }


    @Required
    public void setTimeService(TimeService timeService)
    {
        this.timeService = timeService;
    }
}
