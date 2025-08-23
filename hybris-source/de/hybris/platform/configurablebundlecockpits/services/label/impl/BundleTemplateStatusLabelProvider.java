package de.hybris.platform.configurablebundlecockpits.services.label.impl;

import de.hybris.platform.cockpit.services.label.AbstractModelLabelProvider;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateStatusModel;
import de.hybris.platform.servicelayer.type.TypeService;
import org.springframework.beans.factory.annotation.Required;

public class BundleTemplateStatusLabelProvider extends AbstractModelLabelProvider<BundleTemplateStatusModel>
{
    private TypeService typeService;


    protected String getItemLabel(BundleTemplateStatusModel bundleTemplateStatus)
    {
        if(bundleTemplateStatus.getStatus() != null)
        {
            String label = getTypeService().getEnumerationValue("BundleTemplateStatusEnum", bundleTemplateStatus.getStatus().getCode()).getName();
            if(label == null)
            {
                label = bundleTemplateStatus.getStatus().getCode();
            }
            return label;
        }
        return bundleTemplateStatus.getId();
    }


    protected String getItemLabel(BundleTemplateStatusModel bundleTemplateStatus, String languageIso)
    {
        return getItemLabel(bundleTemplateStatus);
    }


    protected String getIconPath(BundleTemplateStatusModel arg0)
    {
        return null;
    }


    protected String getIconPath(BundleTemplateStatusModel arg0, String arg1)
    {
        return null;
    }


    protected String getItemDescription(BundleTemplateStatusModel arg0)
    {
        return "";
    }


    protected String getItemDescription(BundleTemplateStatusModel arg0, String arg1)
    {
        return "";
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    protected TypeService getTypeService()
    {
        return this.typeService;
    }
}
