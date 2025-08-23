package de.hybris.platform.campaigns.service;

import de.hybris.platform.campaigns.model.CampaignModel;
import java.util.List;

public interface CampaignService
{
    List<CampaignModel> getAllCampaigns();


    List<CampaignModel> getActiveCampaigns();


    CampaignModel getCampaignByCode(String paramString);
}
