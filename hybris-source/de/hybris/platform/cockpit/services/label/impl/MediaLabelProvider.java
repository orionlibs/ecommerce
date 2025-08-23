package de.hybris.platform.cockpit.services.label.impl;

import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.cockpit.services.label.CatalogAwareLabelProvider;
import de.hybris.platform.jalo.media.Media;

@Deprecated
public class MediaLabelProvider extends CatalogAwareLabelProvider<Media>
{
    protected String getItemLabel(Media media)
    {
        String fileName = media.getFileName();
        String altText = media.getAltText();
        String code = media.getCode();
        return ((fileName == null) ? "" : fileName) + ((fileName == null) ? "" : fileName) + (
                        (fileName != null && fileName.length() > 0 && altText != null && altText.length() > 0) ? " - " : "") + " [" + (
                        (altText == null) ? "" : altText) + "]";
    }


    protected String getItemLabel(Media media, String languageIso)
    {
        return getItemLabel(media);
    }


    protected CatalogVersion getCatalogVersion(Media media)
    {
        return CatalogManager.getInstance().getCatalogVersion(media);
    }


    protected String getIconPath(Media item)
    {
        return item.getURL2();
    }


    protected String getIconPath(Media item, String languageIso)
    {
        return item.getURL2();
    }


    protected String getItemDescription(Media item)
    {
        return getItemDescription(item, null);
    }


    protected String getItemDescription(Media item, String languageIso)
    {
        return item.getDescription();
    }
}
