package de.hybris.platform.cockpit.services.label.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.services.label.CatalogAwareModelLabelProvider;
import de.hybris.platform.core.model.media.MediaModel;

public class MediaModelLabelProvider extends CatalogAwareModelLabelProvider<MediaModel>
{
    protected String getItemLabel(MediaModel media)
    {
        String fileName = media.getRealfilename();
        String altText = media.getAltText();
        String code = media.getCode();
        return ((fileName == null) ? "" : fileName) + ((fileName == null) ? "" : fileName) + (
                        (fileName != null && fileName.length() > 0 && altText != null && altText.length() > 0) ? " - " : "") + " [" + (
                        (altText == null) ? "" : altText) + "]";
    }


    protected String getItemLabel(MediaModel media, String languageIso)
    {
        return getItemLabel(media);
    }


    protected CatalogVersionModel getCatalogVersionModel(MediaModel itemModel)
    {
        return itemModel.getCatalogVersion();
    }


    protected String getIconPath(MediaModel item)
    {
        return item.getURL2();
    }


    protected String getIconPath(MediaModel item, String languageIso)
    {
        return item.getURL2();
    }


    protected String getItemDescription(MediaModel item)
    {
        return getItemDescription(item, null);
    }


    protected String getItemDescription(MediaModel item, String languageIso)
    {
        return item.getDescription();
    }
}
