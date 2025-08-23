/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved
 */

package com.hybris.backoffice.widgets.imagecropper;

import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.util.CockpitComponentsUtils;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.notifications.DefaultNotificationService;
import com.hybris.cockpitng.util.notifications.event.NotificationEventTypes;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.image.AImage;
import org.zkoss.image.Image;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;

public class ImageCropperController extends DefaultWidgetController
{
    public static final String IMAGE_CROPPER_SCLASS = "yw_modal_image_cropper";
    public static final String SOCKET_INPUT_SHOW_CROPPER = "showCropper";
    public static final String SOCKET_OUTPUT_CLOSE = "close";
    public static final String SOCKET_OUTPUT_CROP_FINISHED = "cropFinished";
    public static final String ON_PREVIEW_IMAGE_LOADED = "onImageLoad";
    public static final String ON_USER_CONFIRMED = "onUserConfirmed";
    private static final String IMAGE_CROPPER_LABEL = "widget.image.cropper.title";
    private static final String CONFIRM_BUTTON_LABEL = "widget.image.cropper.btn.ok";
    private static final String CANCEL_BUTTON_LABEL = "widget.image.cropper.btn.cancel";
    private static final String NO_IMAGE_LOADED_WARNING_LABEL = "widget.image.cropper.no.image.warning";
    private static final String CLEAR_CROPPER_JS_FUNCTION = "clearCropper()";
    private static final int PREVIEW_MAX_HEIGHT = 300;
    private static final int PREVIEW_MAX_WIDTH = 600;
    private static final int DEFAULT_PREVIEW_SCALE = 1;
    private static final int DECIMAL_NUMBER = 4;
    private static final Logger LOG = LoggerFactory.getLogger(ImageCropperController.class);
    @Wire
    protected org.zkoss.zul.Image imagePreview;
    @Wire
    protected Button confirmBtn;
    @Wire
    protected Button cancelBtn;
    @Wire
    protected Label cropperWarningLabel;
    private DefaultNotificationService notificationService;


    @Override
    public void initialize(final Component comp)
    {
        super.initialize(comp);
        findCropperTemplateWindow().ifPresent(window -> {
            window.setClosable(false);
            if(window.getCaption() != null)
            {
                window.getCaption().setLabel(Labels.getLabel(IMAGE_CROPPER_LABEL));
            }
            window.addSclass(IMAGE_CROPPER_SCLASS);
        });
        confirmBtn.setLabel(Labels.getLabel(CONFIRM_BUTTON_LABEL));
        confirmBtn.setDisabled(true);
        confirmBtn.addEventListener(ON_USER_CONFIRMED, this::onUserConfirmed);
        cancelBtn.setLabel(Labels.getLabel(CANCEL_BUTTON_LABEL));
        cancelBtn.addEventListener(Events.ON_CLICK, event -> closeCropper());
        imagePreview.setVisible(false);
        imagePreview.addEventListener(ON_PREVIEW_IMAGE_LOADED, event -> onPreviewLoaded());
        cropperWarningLabel.setValue(Labels.getLabel(NO_IMAGE_LOADED_WARNING_LABEL));
    }


    @SocketEvent(socketId = SOCKET_INPUT_SHOW_CROPPER)
    public void showCropper(Image image)
    {
        if(Objects.nonNull(image))
        {
            cropperWarningLabel.setVisible(false);
            imagePreview.setVisible(true);
            imagePreview.setContent(image);
        }
    }


    protected void onPreviewLoaded()
    {
        if(Objects.nonNull(imagePreview) && Objects.nonNull(imagePreview.getContent()))
        {
            createImageCropper(imagePreview);
            confirmBtn.setDisabled(false);
        }
    }


    protected void closeCropper()
    {
        Clients.evalJavaScript(CLEAR_CROPPER_JS_FUNCTION);
        sendOutput(SOCKET_OUTPUT_CLOSE, null);
    }


    protected void onUserConfirmed(Event event)
    {
        try
        {
            final Image uploadedImage = imagePreview.getContent();
            final Map<String, Integer> cropperLocations = (Map<String, Integer>)event.getData();
            if(Objects.nonNull(uploadedImage) && Objects.nonNull(cropperLocations))
            {
                final Image croppedImage = getCroppedImage(uploadedImage, cropperLocations, getViewScale(uploadedImage));
                sendOutput(SOCKET_OUTPUT_CROP_FINISHED, croppedImage);
                closeCropper();
            }
            else
            {
                notificationService.notifyUser(getCurrentSource(), NotificationEventTypes.EVENT_TYPE_OBJECT_LOAD, NotificationEvent.Level.FAILURE);
            }
        }
        catch(final IOException | RasterFormatException e)
        {
            notificationService.notifyUser(getCurrentSource(), NotificationEventTypes.EVENT_TYPE_OBJECT_CREATION, NotificationEvent.Level.FAILURE);
            LOG.debug("Image crop failed due to {}", e.getMessage());
        }
    }


    /**
     * Function to crop image by BufferedImage.getSubimage(x, y, w, h)
     * @param image the origin org.zkoss.image.Image
     * @param locations target crop location post from front end
     * @param scale the scale of front end ui / origin
     */
    protected Image getCroppedImage(Image image, Map<String, Integer> locations, double scale) throws IOException, RasterFormatException
    {
        if(Objects.isNull(image))
        {
            return null;
        }
        final BufferedImage bufferedImage = ImageIO.read(image.getStreamData());
        final BufferedImage cropped = bufferedImage.getSubimage(
                        (int)(locations.get("x") * scale),
                        (int)(locations.get("y") * scale),
                        (int)(locations.get("w") * scale),
                        (int)(locations.get("h") * scale));
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ImageIO.write(cropped, getFormatContentType(image), stream);
        final byte[] croppedBytes = stream.toByteArray();
        stream.close();
        return new AImage(image.getName(), croppedBytes);
    }


    /**
     * Tries to find parent window for this template widget
     *
     * @return window object if the widget is a template, {@link Optional#empty()} otherwise
     */
    protected Optional<Window> findCropperTemplateWindow()
    {
        return CockpitComponentsUtils.findClosestComponent(getWidgetslot(), Window.class,
                        getWidgetSettings().getString(IMAGE_CROPPER_SCLASS));
    }


    protected void setNotificationService(DefaultNotificationService notificationService)
    {
        this.notificationService = notificationService;
    }


    protected DefaultNotificationService getNotificationService()
    {
        return this.notificationService;
    }


    private void createImageCropper(org.zkoss.zul.Image imageUi)
    {
        final Image content = imageUi.getContent();
        final int squareLength = Math.min(content.getHeight(), content.getWidth());
        final String createCropperScripts = String.format("attachCropperToImage('%s',%d)", imageUi.getUuid(), squareLength);
        Clients.evalJavaScript(createCropperScripts);
    }


    private double getViewScale(Image image)
    {
        double scale = DEFAULT_PREVIEW_SCALE;
        final int orgHeight = image.getHeight();
        final int orgWidth = image.getWidth();
        if(orgHeight > PREVIEW_MAX_HEIGHT && orgWidth > PREVIEW_MAX_WIDTH)
        {
            double scaleH = (double)orgHeight / PREVIEW_MAX_HEIGHT;
            double scaleW = (double)orgWidth / PREVIEW_MAX_WIDTH;
            scale = Math.max(scaleH, scaleW);
        }
        if(orgHeight > PREVIEW_MAX_HEIGHT && orgWidth <= PREVIEW_MAX_WIDTH)
        {
            scale = (double)orgHeight / PREVIEW_MAX_HEIGHT;
        }
        if(orgHeight <= PREVIEW_MAX_HEIGHT && orgWidth > PREVIEW_MAX_WIDTH)
        {
            scale = (double)orgWidth / PREVIEW_MAX_WIDTH;
        }
        return getFormattedScale(scale);
    }


    private String getFormatContentType(Image image)
    {
        final String[] typeArr = image.getContentType().split("/");
        return typeArr[typeArr.length - 1].toUpperCase(Locale.ENGLISH);
    }


    private String getCurrentSource()
    {
        final WidgetInstance instance = getWidgetInstanceManager().getWidgetslot().getWidgetInstance();
        if(Objects.nonNull(instance))
        {
            return instance.getId();
        }
        return NotificationEvent.EVENT_SOURCE_UNKNOWN;
    }


    private double getFormattedScale(double scale)
    {
        return BigDecimal.valueOf(scale).setScale(DECIMAL_NUMBER, RoundingMode.DOWN).doubleValue();
    }
}
