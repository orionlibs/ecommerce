package de.hybris.platform.cockpit.services.config.impl;

import de.hybris.platform.cockpit.components.sectionpanel.SectionPanelModel;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.MediaUpdateService;
import de.hybris.platform.cockpit.services.SystemService;
import de.hybris.platform.cockpit.services.config.CustomEditorSectionConfiguration;
import de.hybris.platform.cockpit.services.config.EditorConfiguration;
import de.hybris.platform.cockpit.services.config.EditorSectionConfiguration;
import de.hybris.platform.cockpit.services.media.MediaInfoService;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.ModelService;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Collections;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.image.AImage;
import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;

public class MediaEditorSectionConfiguration extends DefaultEditorSectionConfiguration implements CustomEditorSectionConfiguration
{
    private static final Logger LOG = LoggerFactory.getLogger(MediaEditorSectionConfiguration.class);
    private MediaUpdateService mediaUpdateService;
    private ModelService modelService;
    private MediaEditorSectionRenderer curstomRenderer;
    private MediaInfoService mediaInfoService;
    private SystemService systemService;


    public MediaEditorSectionConfiguration()
    {
    }


    public MediaEditorSectionConfiguration(String qualifier)
    {
        super(qualifier);
    }


    public void allInitialized(EditorConfiguration config, ObjectType objectType, TypedObject currentObject)
    {
    }


    public MediaEditorSectionConfiguration clone() throws CloneNotSupportedException
    {
        MediaEditorSectionConfiguration ret = (MediaEditorSectionConfiguration)super.clone();
        ret.mediaUpdateService = this.mediaUpdateService;
        ret.curstomRenderer = this.curstomRenderer;
        return ret;
    }


    protected Image convertByteStreamToImage(byte[] byteStream, String name) throws IOException
    {
        AImage imagePrototype = new AImage(name, byteStream);
        Image regularImage = new Image();
        regularImage.setContent((Image)imagePrototype);
        regularImage.setStyle("max-height:50px");
        return regularImage;
    }


    protected Image convertToImage(AImage imageContent)
    {
        Image regularImage = new Image();
        regularImage.setContent((Image)imageContent);
        regularImage.setStyle("max-height:50px");
        return regularImage;
    }


    protected Button createDownloadButton(TypedObject typedObject)
    {
        Button uploadButton = new Button(Labels.getLabel("mediaeditor.button.download"));
        uploadButton.setTooltiptext(Labels.getLabel("mediaeditor.button.download"));
        uploadButton.setSclass("btnDownload");
        if(typedObject.getObject() instanceof MediaModel)
        {
            uploadButton.setHref(UITools.getAdjustedUrl(((MediaModel)typedObject.getObject()).getDownloadURL()));
        }
        return uploadButton;
    }


    protected Button createPreviewButton(TypedObject typedObject)
    {
        Button uploadButton = new Button(Labels.getLabel("mediaeditor.button.preview"));
        uploadButton.setTooltiptext(Labels.getLabel("mediaeditor.button.preview"));
        uploadButton.setSclass("btnUploadPreview");
        uploadButton.setTarget("_blank");
        if(typedObject.getObject() instanceof MediaModel)
        {
            uploadButton.setHref(UITools.getAdjustedUrl(((MediaModel)typedObject.getObject()).getURL()));
        }
        return uploadButton;
    }


    protected byte[] extractMediaBytesFromMedia(Media uploadedMedia)
    {
        byte[] ret = null;
        if(uploadedMedia == null)
        {
            return ret;
        }
        InputStream anInputStream = null;
        Reader reader = null;
        try
        {
            if(uploadedMedia.isBinary())
            {
                anInputStream = uploadedMedia.getStreamData();
                if(anInputStream != null)
                {
                    ret = IOUtils.toByteArray(anInputStream);
                }
            }
            else
            {
                reader = uploadedMedia.getReaderData();
                if(reader != null)
                {
                    ret = IOUtils.toByteArray(reader);
                }
            }
        }
        catch(IOException e)
        {
            LOG.warn("An error ocured while extracting byte stream from " + Media.class.getName() + " object!");
        }
        finally
        {
            try
            {
                if(anInputStream != null)
                {
                    anInputStream.close();
                }
                if(reader != null)
                {
                    reader.close();
                }
            }
            catch(IOException e)
            {
                LOG.warn("An error ocured while extracting byte stream from " + Media.class
                                .getName() + " object!");
            }
        }
        return ret;
    }


    protected Button createUploadButton(TypedObject typedObject, Div previewContainer, SectionPanelModel sectionModel)
    {
        Button uploadButton = new Button(Labels.getLabel("mediaeditor.button.uploaddialog"));
        uploadButton.setTooltiptext(Labels.getLabel("mediaeditor.button.uploaddialog"));
        uploadButton.setSclass("btnUpload");
        uploadButton.setDisabled((typedObject == null ||
                        !getSystemService().checkPermissionOn(typedObject.getType().getCode(), "change")));
        uploadButton.addEventListener("onClick", (EventListener)new Object(this, typedObject, sectionModel, previewContainer));
        return uploadButton;
    }


    public List<EditorSectionConfiguration> getAdditionalSections()
    {
        return Collections.EMPTY_LIST;
    }


    public MediaEditorSectionRenderer getCustomRenderer()
    {
        return this.curstomRenderer;
    }


    protected MediaUpdateService getMediaUpdateService()
    {
        if(this.mediaUpdateService == null)
        {
            this.mediaUpdateService = (MediaUpdateService)SpringUtil.getBean("mediaUpdateService");
        }
        return this.mediaUpdateService;
    }


    public SystemService getSystemService()
    {
        if(this.systemService == null)
        {
            this.systemService = (SystemService)SpringUtil.getBean("systemService");
        }
        return this.systemService;
    }


    protected ModelService getModelService()
    {
        if(this.modelService == null)
        {
            this.modelService = (ModelService)SpringUtil.getBean("modelService");
        }
        return this.modelService;
    }


    protected MediaInfoService getMediaInfoService()
    {
        if(this.mediaInfoService == null)
        {
            this.mediaInfoService = (MediaInfoService)SpringUtil.getBean("mediaInfoService");
        }
        return this.mediaInfoService;
    }


    public void initialize(EditorConfiguration config, ObjectType objectType, TypedObject object)
    {
        this.curstomRenderer = new MediaEditorSectionRenderer(this, object);
    }


    public void loadValues(EditorConfiguration config, ObjectType type, TypedObject object, ObjectValueContainer objectValues)
    {
    }


    public void saveValues(EditorConfiguration config, ObjectType type, TypedObject object, ObjectValueContainer objectValues)
    {
    }


    protected Component wrapButton(Button aButton)
    {
        Div buttonContainer = new Div();
        buttonContainer.setStyle("padding:2px; text-align:center");
        buttonContainer.setSclass("imageEditorSclass");
        buttonContainer.appendChild((Component)aButton);
        return (Component)buttonContainer;
    }
}
