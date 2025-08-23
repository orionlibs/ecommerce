package de.hybris.platform.cmscockpit.services.label.impl;

import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cockpit.services.label.AbstractModelLabelProvider;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.core.model.media.MediaModel;

public class ContentPageModelLabelProvider extends AbstractModelLabelProvider<ContentPageModel>
{
    protected static final String CONTENT_PAGE_ICON_PATH = "cmscockpit/images/ContentPageModel_small.gif";


    protected String getItemLabel(ContentPageModel item)
    {
        String name = item.getName();
        String mnemonic = item.getCatalogVersion().getMnemonic();
        return name + " - " + name;
    }


    protected String getItemLabel(ContentPageModel item, String languageIso)
    {
        return getItemLabel(item);
    }


    protected String getIconPath(ContentPageModel item)
    {
        MediaModel preview = item.getPreviewImage();
        if(preview != null && preview.getURL2() != null)
        {
            return UITools.getAdjustedUrl(preview.getURL2());
        }
        return "cmscockpit/images/ContentPageModel_small.gif";
    }


    protected String getIconPath(ContentPageModel item, String languageIso)
    {
        return getIconPath(item);
    }


    protected String getItemDescription(ContentPageModel item)
    {
        return "";
    }


    protected String getItemDescription(ContentPageModel item, String languageIso)
    {
        return "";
    }
}
