package de.hybris.platform.cockpit.wizards.generic;

import de.hybris.platform.cockpit.services.media.MediaInfoService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.cockpit.wizards.WizardPageController;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.media.MediaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

public class UploadMediaPage extends AbstractGenericItemPage
{
    private static final Logger LOG = LoggerFactory.getLogger(UploadMediaPage.class);
    private MediaService mediaService;
    private MediaInfoService mediaInfoService;
    private Media media = null;


    protected void updateMediaContent(Image img, Media media)
    {
        if(media instanceof org.zkoss.image.AImage)
        {
            img.setContent((Image)media);
        }
        else
        {
            img.setSrc(getMediaInfoService().getFallbackIcon());
        }
    }


    public Component createRepresentationItself()
    {
        Image previewImg = new Image();
        if(getMedia() != null)
        {
            updateMediaContent(previewImg, getMedia());
        }
        Div content = new Div();
        content.setSclass("wizardMediaUploadPage");
        content.setStyle("padding: 10px;");
        Component rowComp = createRowComponent();
        rowComp.appendChild((Component)new Label(Labels.getLabel("cockpit.wizard.createwidget.uploadpage.attribute.file")));
        Hbox uploadEditorHbox = new Hbox();
        uploadEditorHbox.setWidths("none,100px");
        uploadEditorHbox.setWidth("100%");
        uploadEditorHbox.setStyle("table-layout:fixed;");
        rowComp.appendChild((Component)uploadEditorHbox);
        Textbox uploadTextbox = new Textbox();
        uploadTextbox.setDisabled(true);
        uploadTextbox.setText((this.media == null) ? null : this.media.getName());
        uploadEditorHbox.appendChild((Component)uploadTextbox);
        Button uploadButton = new Button(Labels.getLabel("mediaeditor.button.uploaddialog"));
        uploadButton.setSclass("btnblue");
        uploadButton.addEventListener("onClick", (EventListener)new Object(this, uploadTextbox, previewImg));
        uploadEditorHbox.appendChild((Component)uploadButton);
        Div rowCmpCnt = new Div();
        rowCmpCnt.setStyle("margin: 5px; margin-top: 15px;");
        rowCmpCnt.setSclass("rowCnt");
        rowCmpCnt.appendChild(rowComp);
        content.appendChild((Component)rowCmpCnt);
        Div previewCnt = new Div();
        previewCnt.setSclass("wizardMediaPreview");
        previewCnt.setStyle("max-height: 400px; max-width: 600px; overflow: auto; border: 1px solid #CCCCCC; margin-top:20px; padding: 10px;");
        previewCnt.appendChild((Component)previewImg);
        content.appendChild((Component)previewCnt);
        return (Component)content;
    }


    public void setMedia(Media media)
    {
        this.media = media;
    }


    public Media getMedia()
    {
        return this.media;
    }


    protected Component createRowComponent()
    {
        Hbox hbox = new Hbox();
        hbox.setWidth("100%");
        hbox.setStyle("table-layout:fixed;");
        hbox.setWidths("80px,none");
        return (Component)hbox;
    }


    public WizardPageController getController()
    {
        if(super.getController() == null)
        {
            setController((WizardPageController)new Object(this));
        }
        return super.getController();
    }


    protected void updateMediaModel(MediaModel currentMediaModel)
    {
        UITools.updateMediaModel(currentMediaModel, getMedia(), getMediaService());
        UISessionUtils.getCurrentSession().getModelService().save(currentMediaModel);
    }


    @Required
    public void setMediaService(MediaService mediaService)
    {
        this.mediaService = mediaService;
    }


    public MediaService getMediaService()
    {
        return this.mediaService;
    }


    @Required
    public void setMediaInfoService(MediaInfoService mediaInfoService)
    {
        this.mediaInfoService = mediaInfoService;
    }


    public MediaInfoService getMediaInfoService()
    {
        return this.mediaInfoService;
    }
}
