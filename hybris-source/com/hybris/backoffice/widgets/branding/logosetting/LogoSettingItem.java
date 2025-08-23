/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.branding.logosetting;

import com.hybris.backoffice.media.BackofficeMediaConstants;
import de.hybris.platform.core.model.media.MediaModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;

public class LogoSettingItem
{
    private static final Logger LOG = LoggerFactory.getLogger(LogoSettingItem.class);
    private static final String SCLASS_LOGO_UPLOAD_CONTAINER = "logo-upload-container";
    private static final String SCLASS_LOGO_BTNS_CONTAINER = "logo-btns-container";
    private static final String SCLASS_LOGO_UPLOAD_LABEL = "logo-upload-label";
    private static final String SCLASS_LOGO_UPLOAD_BTN = "logo-upload-btn y-btn-transparent";
    private static final String SCLASS_LOGO_RESET_BTN = "logo-reset-btn y-btn-transparent";
    private static final String SCLASS_LOGO_PREVIEW_CONTAINER = "logo-preview-container";
    private String defaultLogoUrl;
    private String logoCode;
    private String info;
    private Image logoImage;
    protected Button resetBtn;
    protected boolean isDataChanged = false;
    protected LogoSettingController controller;


    protected LogoSettingItem(final Component parent, final String logoCode, final String defaultLogoUrl, final String info,
                    final LogoSettingController controller)
    {
        this.logoCode = logoCode;
        this.defaultLogoUrl = defaultLogoUrl;
        this.controller = controller;
        this.info = info;
        render(parent);
    }


    private void render(final Component parent)
    {
        final var uploadContainer = new Div();
        uploadContainer.setSclass(SCLASS_LOGO_UPLOAD_CONTAINER);
        parent.appendChild(uploadContainer);
        final var btnContainer = new Div();
        btnContainer.setSclass(SCLASS_LOGO_BTNS_CONTAINER);
        btnContainer.setParent(uploadContainer);
        renderLabelAndBtns(btnContainer);
        renderLogoPreview(uploadContainer);
    }


    private void renderLabelAndBtns(final Component parent)
    {
        final var label = new Label();
        label.setSclass(SCLASS_LOGO_UPLOAD_LABEL);
        label.setValue(info);
        label.setParent(parent);
        final var uploadBtn = new Button();
        uploadBtn.setSclass(SCLASS_LOGO_UPLOAD_BTN);
        uploadBtn.setTooltiptext(controller.getLabel("logosetting.upload.btn.tooltip"));
        uploadBtn.setUpload(String.format("true,maxsize=%d,accept=%s", BackofficeMediaConstants.FILE_UPLOAD_MAX_SIZE,
                        BackofficeMediaConstants.FILE_UPLOAD_LOGO_ACCEPT_TYPE));
        uploadBtn.addEventListener(Events.ON_UPLOAD, this::onFileUpload);
        uploadBtn.setParent(parent);
        resetBtn = new Button();
        resetBtn.setSclass(SCLASS_LOGO_RESET_BTN);
        resetBtn.setTooltiptext(controller.getLabel("logosetting.reset.btn.tooltip"));
        resetBtn.setParent(parent);
        resetBtn.addEventListener(Events.ON_CLICK, this::onResetToDefault);
    }


    private void renderLogoPreview(final Component parent)
    {
        final var previewContainer = new Div();
        previewContainer.setSclass(SCLASS_LOGO_PREVIEW_CONTAINER);
        previewContainer.setParent(parent);
        logoImage = new Image();
        logoImage.setParent(previewContainer);
        reloadLogo();
    }


    //Click reset to default button
    protected void onResetToDefault(final Event event)
    {
        logoImage.setSrc(defaultLogoUrl);
        isDataChanged = true;
        resetBtn.setDisabled(true);
        controller.enableSave(true);
    }


    protected void onFileUpload(final Event event)
    {
        if(event instanceof UploadEvent)
        {
            final var media = ((UploadEvent)event).getMedia();
            if(media instanceof org.zkoss.image.Image)
            {
                logoImage.setContent((org.zkoss.image.Image)media);
                isDataChanged = true;
                resetBtn.setDisabled(false);
                controller.enableSave(true);
            }
            else
            {
                Messagebox.show(controller.getLabel("logosetting.upload.file.type.not.supported"));
            }
        }
    }


    protected boolean save()
    {
        if(!isDataChanged)
        {
            return true;
        }
        try
        {
            final boolean isResetToDefault = resetBtn.isDisabled();
            final var modelOpt = controller.getBackofficeMediaUtil().getMedia(logoCode);
            if(modelOpt.isPresent())
            {
                final var mediaModel = modelOpt.get();
                if(isResetToDefault)
                {
                    controller.getBackofficeMediaUtil().deleteMedia(mediaModel);
                    controller.onLogoSaved(logoCode, null);
                }
                else
                {
                    controller.getBackofficeMediaUtil().updateMedia(mediaModel, logoImage.getContent());
                    controller.onLogoSaved(logoCode, mediaModel);
                }
            }
            else if(!isResetToDefault)
            {
                final var mediaModel = controller.getBackofficeMediaUtil().createMedia(logoCode,
                                BackofficeMediaConstants.BACKOFFICE_LOGO_MEDIA_FOLDER, MediaModel._TYPECODE, logoImage.getContent());
                controller.onLogoSaved(logoCode, mediaModel);
            }
            isDataChanged = false;
            return true;
        }
        catch(final RuntimeException ex)
        {
            LOG.error(String.format("Save logo %s failed", logoCode), ex);
            return false;
        }
    }


    protected void reloadLogo()
    {
        controller.getBackofficeMediaUtil().getMedia(logoCode).ifPresentOrElse(mediaModel -> {
            logoImage.setSrc(mediaModel.getURL());
            resetBtn.setDisabled(false);
        }, () -> {
            logoImage.setSrc(defaultLogoUrl);
            resetBtn.setDisabled(true);
        });
    }


    protected void reset()
    {
        isDataChanged = false;
        reloadLogo();
    }


    protected boolean isDataChanged()
    {
        return isDataChanged;
    }


    public String getDefaultLogoUrl()
    {
        return defaultLogoUrl;
    }


    public String getLogoCode()
    {
        return logoCode;
    }


    public Image getLogoImage()
    {
        return logoImage;
    }


    public LogoSettingController getController()
    {
        return controller;
    }
}
