package de.hybris.platform.platformbackoffice.widgets.impex;

import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.editors.impl.AbstractCockpitEditorRenderer;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.impex.model.ImpExMediaModel;
import de.hybris.platform.servicelayer.media.MediaService;
import java.io.InputStream;
import javax.annotation.Resource;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;
import org.zkoss.zul.Filedownload;

public class ImpExExportResultDownload extends AbstractCockpitEditorRenderer<Object>
{
    @Resource
    private MediaService mediaService;


    public void render(Component parent, EditorContext<Object> context, EditorListener<Object> listener)
    {
        Button exportDownloadBtn = new Button();
        exportDownloadBtn.setLabel(context.getLabel("download.button.label"));
        Object impexMedia = context.getInitialValue();
        if(impexMedia instanceof ImpExMediaModel)
        {
            exportDownloadBtn.addEventListener("onClick", e -> executeMediaDownload((ImpExMediaModel)impexMedia));
        }
        else
        {
            exportDownloadBtn.setDisabled(true);
        }
        exportDownloadBtn.setParent(parent);
    }


    protected void executeMediaDownload(ImpExMediaModel media)
    {
        InputStream mediaStream = getMediaService().getStreamFromMedia((MediaModel)media);
        String mime = media.getMime();
        String fileName = media.getCode();
        executeBrowserMediaDownload(mediaStream, mime, fileName);
    }


    protected void executeBrowserMediaDownload(InputStream mediaStream, String mime, String fileName)
    {
        Filedownload.save(mediaStream, mime, fileName);
    }


    public MediaService getMediaService()
    {
        return this.mediaService;
    }
}
