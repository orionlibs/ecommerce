package de.hybris.platform.platformbackoffice.renderers;

import com.hybris.cockpitng.core.config.impl.jaxb.listview.ListColumn;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.common.AbstractWidgetComponentRenderer;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.media.MediaService;
import java.io.InputStream;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Div;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Listcell;

public class DownloadMediaCellRenderer extends AbstractWidgetComponentRenderer<Listcell, ListColumn, MediaModel>
{
    public static final String SCLASS_YA_DOWNLOAD_MEDIA_INLINE = "ya-download-media-inline";
    private MediaService mediaService;


    public void render(Listcell listcell, ListColumn configuration, MediaModel mediaModel, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        Div download = new Div();
        download.setSclass("ya-download-media-inline");
        listcell.appendChild((Component)download);
        download.addEventListener("onClick", click -> {
            InputStream streamFromMedia = getMediaService().getStreamFromMedia(mediaModel);
            String mime = mediaModel.getMime();
            String fileName = extractFileName(mediaModel);
            Filedownload.save(streamFromMedia, mime, fileName);
        });
    }


    protected String extractFileName(MediaModel mediaModel)
    {
        return (String)StringUtils.defaultIfBlank(mediaModel.getRealFileName(), createFallbackFileName(mediaModel));
    }


    protected String createFallbackFileName(MediaModel mediaModel)
    {
        if(mediaModel.getPk() != null)
        {
            return mediaModel.getPk().toString();
        }
        return String.valueOf(System.currentTimeMillis());
    }


    public MediaService getMediaService()
    {
        return this.mediaService;
    }


    @Required
    public void setMediaService(MediaService mediaService)
    {
        this.mediaService = mediaService;
    }
}
