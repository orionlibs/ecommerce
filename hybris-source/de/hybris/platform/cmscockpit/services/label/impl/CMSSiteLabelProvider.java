package de.hybris.platform.cmscockpit.services.label.impl;

import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cockpit.services.label.AbstractModelLabelProvider;

public class CMSSiteLabelProvider extends AbstractModelLabelProvider<CMSSiteModel>
{
    protected String getItemLabel(CMSSiteModel item)
    {
        return (item.getName() != null) ? item.getName() : item.getUid();
    }


    protected String getItemLabel(CMSSiteModel item, String languageIso)
    {
        return null;
    }


    protected String getItemDescription(CMSSiteModel item)
    {
        return null;
    }


    protected String getItemDescription(CMSSiteModel item, String languageIso)
    {
        return null;
    }


    protected String getIconPath(CMSSiteModel item)
    {
        return null;
    }


    protected String getIconPath(CMSSiteModel item, String languageIso)
    {
        return null;
    }
}
