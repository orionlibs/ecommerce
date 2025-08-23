package de.hybris.platform.campaigns.dao;

import de.hybris.platform.campaigns.model.CampaignModel;
import de.hybris.platform.servicelayer.internal.dao.GenericDao;
import java.util.Date;
import java.util.List;

public interface CampaignDao extends GenericDao<CampaignModel>
{
    List<CampaignModel> findAllCampaigns();


    List<CampaignModel> findActiveCampaigns(Date paramDate);


    CampaignModel findCampaignByCode(String paramString);
}
