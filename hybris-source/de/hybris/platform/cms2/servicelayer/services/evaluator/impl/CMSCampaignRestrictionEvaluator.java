package de.hybris.platform.cms2.servicelayer.services.evaluator.impl;

import de.hybris.platform.campaigns.model.CampaignModel;
import de.hybris.platform.campaigns.service.CampaignService;
import de.hybris.platform.cms2.model.restrictions.CMSCampaignRestrictionModel;
import de.hybris.platform.cms2.servicelayer.data.RestrictionData;
import de.hybris.platform.cms2.servicelayer.services.evaluator.CMSRestrictionEvaluator;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Collection;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class CMSCampaignRestrictionEvaluator implements CMSRestrictionEvaluator<CMSCampaignRestrictionModel>
{
    private CampaignService campaignService;


    public boolean evaluate(CMSCampaignRestrictionModel cmsCampaignRestriction, RestrictionData context)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("cmsCampaignRestriction", cmsCampaignRestriction);
        Collection<CampaignModel> activeCampaigns = getCampaignService().getActiveCampaigns();
        Collection<CampaignModel> restrictedToCampaigns = cmsCampaignRestriction.getCampaigns();
        if(CollectionUtils.isEmpty(activeCampaigns) || CollectionUtils.isEmpty(restrictedToCampaigns))
        {
            return false;
        }
        return CollectionUtils.containsAny(restrictedToCampaigns, activeCampaigns);
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
}
