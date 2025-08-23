package de.hybris.platform.cmscockpit.services.label.impl;

import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cockpit.services.label.AbstractModelLabelProvider;
import org.apache.commons.lang.StringUtils;

public class CmsLinkComponentModelLabelProvider extends AbstractModelLabelProvider<CMSLinkComponentModel>
{
    protected static final String CMS_LINK_COMPONENT_ICON_PATH = "cmscockpit/images/ContentElementLinkInternalSmall.gif";


    protected String getItemLabel(CMSLinkComponentModel item)
    {
        String name = (item.getLinkName() != null) ? item.getLinkName().trim() : null;
        if(StringUtils.isEmpty(name))
        {
            name = item.getName();
        }
        String mnemonic = item.getCatalogVersion().getMnemonic();
        return name + " - " + name;
    }


    protected String getItemLabel(CMSLinkComponentModel item, String languageIso)
    {
        return getItemLabel(item);
    }


    protected String getIconPath(CMSLinkComponentModel item)
    {
        return "cmscockpit/images/ContentElementLinkInternalSmall.gif";
    }


    protected String getIconPath(CMSLinkComponentModel item, String languageIso)
    {
        return "cmscockpit/images/ContentElementLinkInternalSmall.gif";
    }


    protected String getItemDescription(CMSLinkComponentModel item)
    {
        return "";
    }


    protected String getItemDescription(CMSLinkComponentModel item, String languageIso)
    {
        return "";
    }
}
