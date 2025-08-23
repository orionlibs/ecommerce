package de.hybris.platform.ruleengineservices.rao.providers.impl;

import de.hybris.platform.campaigns.model.CampaignModel;
import de.hybris.platform.campaigns.service.CampaignService;
import de.hybris.platform.ruleengineservices.rao.CampaignRAO;
import de.hybris.platform.ruleengineservices.rao.providers.RAOProvider;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCampaignRAOProvider implements RAOProvider
{
    private CampaignService campaignService;
    private Converter<CampaignModel, CampaignRAO> campaignRaoConverter;


    public Set<?> expandFactModel(Object modelFact)
    {
        return (Set)getCampaignService().getActiveCampaigns().stream().map(cm -> (CampaignRAO)getCampaignRaoConverter().convert(cm))
                        .collect(Collectors.toSet());
    }


    protected CampaignService getCampaignService()
    {
        return this.campaignService;
    }


    @Required
    public void setCampaignService(CampaignService campaignService)
    {
        this.campaignService = campaignService;
    }


    protected Converter<CampaignModel, CampaignRAO> getCampaignRaoConverter()
    {
        return this.campaignRaoConverter;
    }


    @Required
    public void setCampaignRaoConverter(Converter<CampaignModel, CampaignRAO> campaignRaoConverter)
    {
        this.campaignRaoConverter = campaignRaoConverter;
    }
}
