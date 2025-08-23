package de.hybris.platform.cmscockpit.components.editor.impl;

import de.hybris.platform.cmscockpit.components.editor.AbstractMediaReferencePreview;
import de.hybris.platform.cmscockpit.components.editor.MediaReferencePreviewModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.impl.MediaEditorSectionConfiguration;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Toolbarbutton;

public class DefaultMediaReferencePreview extends AbstractMediaReferencePreview
{
    private static final Logger log = Logger.getLogger(DefaultMediaReferencePreview.class);
    private static final String EMPTY_IMAGE = "cockpit/images/stop_klein.jpg";
    private static final String MEDIA_REF_SELECTOR_CODE = "mediaReferenceSelector";
    private static final String MEDIA_REFERENCE_PREVIEW_SCLASS = "mediaReference";
    private boolean initialized = false;
    private MediaReferencePreviewModel model;
    private final String propertyQualifier;
    private final TypedObject parentObject;


    public DefaultMediaReferencePreview(String propertyQualifier, TypedObject typedObject)
    {
        this.propertyQualifier = propertyQualifier;
        this.parentObject = typedObject;
    }


    public boolean initialize()
    {
        this.initialized = false;
        Hbox mainHbox = new Hbox();
        mainHbox.setSclass("mediaReference");
        Div btnDiv = new Div();
        btnDiv.setSclass("z-combobox");
        btnDiv.setStyle("margin: 0 0 0 0;");
        Toolbarbutton uploadButton = null;
        Toolbarbutton clearButton = null;
        Toolbarbutton advancedEditButton = null;
        Image previewImage = new Image();
        if(getModel().getSource() != null)
        {
            previewImage = extractCurrentImage(previewImage);
            uploadButton = new Toolbarbutton("Upload");
            uploadButton.addEventListener("onClick", (EventListener)new Object(this));
            clearButton = new Toolbarbutton("Create");
            clearButton.addEventListener("onClick", (EventListener)new Object(this));
            advancedEditButton = new Toolbarbutton("AdvancedEdit");
            advancedEditButton.addEventListener("onClick", (EventListener)new Object(this));
        }
        else
        {
            previewImage.setSrc("cockpit/images/stop_klein.jpg");
        }
        mainHbox.appendChild((Component)previewImage);
        Toolbarbutton changeAnImage = new Toolbarbutton("Find");
        changeAnImage.addEventListener("onClick", (EventListener)new Object(this, mainHbox));
        mainHbox.appendChild((Component)changeAnImage);
        if(uploadButton != null)
            ;
        if(clearButton != null)
            ;
        if(advancedEditButton != null)
        {
            mainHbox.appendChild((Component)advancedEditButton);
        }
        appendChild((Component)mainHbox);
        return this.initialized;
    }


    public boolean update()
    {
        boolean success = false;
        if(this.initialized)
        {
            success = true;
        }
        else
        {
            success = initialize();
        }
        return success;
    }


    public void setModel(MediaReferencePreviewModel model)
    {
        if(this.model != model)
        {
            this.model = model;
            if(this.model != null)
            {
                initialize();
            }
        }
    }


    public MediaReferencePreviewModel getModel()
    {
        return this.model;
    }


    protected Image extractCurrentImage(Image currentImage)
    {
        MediaEditorSectionConfiguration.MediaContent anMediaContent = this.model.getBaseMediaProperties();
        Image anImageContent = null;
        try
        {
            if(anMediaContent.getData() != null && (anMediaContent.getData()).length != 0)
            {
                AImage aImage = new AImage(anMediaContent.getName(), anMediaContent.getData());
                currentImage.setContent((Image)aImage);
            }
            else
            {
                currentImage.setSrc("cockpit/images/stop_klein.jpg");
            }
        }
        catch(IOException e)
        {
            log.error("An error occured while creating na image!", e);
        }
        return currentImage;
    }
}
