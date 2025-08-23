package de.hybris.platform.cms2.model;

import de.hybris.platform.campaigns.model.CampaignModel;
import de.hybris.platform.cms2.model.restrictions.CMSCampaignRestrictionModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.util.localization.Localization;
import java.util.Collection;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;

public class CampaignRestrictionDescription implements DynamicAttributeHandler<String, CMSCampaignRestrictionModel>
{
    public String get(CMSCampaignRestrictionModel model)
    {
        Collection<CampaignModel> campaigns = model.getCampaigns();
        StringBuilder result = new StringBuilder();
        if(CollectionUtils.isNotEmpty(campaigns))
        {
            String localizedString = Localization.getLocalizedString("type.CMSCampaignRestriction.description.text");
            result.append((localizedString == null) ? "Display for campaigns:" : localizedString);
            result.append(" ");
            result.append(campaigns.stream().map(CampaignModel::getCode).collect(Collectors.joining(", ")));
        }
        return result.toString();
    }


    public void set(CMSCampaignRestrictionModel model, String value)
    {
        throw new UnsupportedOperationException();
    }
}
