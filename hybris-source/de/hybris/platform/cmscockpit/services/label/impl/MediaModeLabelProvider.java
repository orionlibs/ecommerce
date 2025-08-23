package de.hybris.platform.cmscockpit.services.label.impl;

import de.hybris.platform.cockpit.services.label.AbstractModelLabelProvider;
import de.hybris.platform.cockpit.services.media.MediaInfoService;
import de.hybris.platform.core.model.media.MediaModel;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class MediaModeLabelProvider extends AbstractModelLabelProvider<MediaModel>
{
    private static final Logger LOG = Logger.getLogger(MediaModeLabelProvider.class);
    private static final String DEFAULT_MEDIA_PREVIEW_ICON = "cmscockpit/images/navigation_resource_Media.gif";
    private MediaInfoService mediaInfoService;


    protected String getItemLabel(MediaModel item)
    {
        return item.getCode();
    }


    protected String getItemLabel(MediaModel item, String languageIso)
    {
        return getItemLabel(item);
    }


    protected String getIconPath(MediaModel item)
    {
        return retriveImageUrl(item.getDownloadURL(), item);
    }


    protected String getIconPath(MediaModel item, String languageIso)
    {
        return retriveImageUrl(item.getDownloadURL(), item);
    }


    protected String getItemDescription(MediaModel item)
    {
        return "";
    }


    protected String getItemDescription(MediaModel item, String languageIso)
    {
        return "";
    }


    protected String retriveImageUrl(String imageUrl, MediaModel item)
    {
        String displayImgUrl = "cmscockpit/images/navigation_resource_Media.gif";
        if(StringUtils.isNotBlank(imageUrl) && this.mediaInfoService.isWebMedia(item).booleanValue())
        {
            boolean absolute = false;
            try
            {
                URI uri = new URI(imageUrl);
                absolute = uri.isAbsolute();
                displayImgUrl = absolute ? imageUrl : ("~" + imageUrl);
            }
            catch(URISyntaxException e)
            {
                LOG.info(e);
            }
        }
        return displayImgUrl;
    }


    public MediaInfoService getMediaInfoService()
    {
        return this.mediaInfoService;
    }


    public void setMediaInfoService(MediaInfoService mediaInfoService)
    {
        this.mediaInfoService = mediaInfoService;
    }
}
