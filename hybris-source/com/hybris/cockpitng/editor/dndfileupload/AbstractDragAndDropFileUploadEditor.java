/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.dndfileupload;

import com.hybris.cockpitng.common.configuration.MenupopupPosition;
import com.hybris.cockpitng.core.context.impl.DefaultCockpitContext;
import com.hybris.cockpitng.core.util.CockpitProperties;
import com.hybris.cockpitng.editor.defaultfileupload.FileUploadResult;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.impl.AbstractCockpitEditorRenderer;
import com.hybris.cockpitng.services.media.MimeTypeChecker;
import com.hybris.cockpitng.services.media.MimeTypeResolver;
import com.hybris.cockpitng.services.media.ObjectPreview;
import com.hybris.cockpitng.services.media.ObjectPreviewService;
import com.hybris.cockpitng.testing.annotation.InextensibleMethod;
import com.hybris.cockpitng.util.UITools;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Optional;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.image.AImage;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkmax.zul.Dropupload;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Popup;

/**
 * Abstract file upload editor which allows to drag and drop content. Accepted params:
 * <ul>
 * <li>accept - specifies the MIME types of files that the server accepts e.g "audio/|video/|image/*"</li>
 * <li>maxsize - defines max file size, if not defined then default system value will be used (property
 * fileUpload.maxSize)</li>
 * <li>singleLineMode - param which defines single line mode. This param can be utilised in sub classes</li>
 * </ul>
 *
 * @param <T>
 *           type of uploaded reference
 */
public abstract class AbstractDragAndDropFileUploadEditor<T> extends AbstractCockpitEditorRenderer<T>
{
    protected static final int BYTES_IN_KILOBYTE = 1024;
    protected static final DecimalFormat ALLOWED_SIZE_DECIMAL_FORMAT = new DecimalFormat("#,##0.#");
    protected static final String PARAM_ACCEPT = "accept";
    protected static final String PARAM_MAX_SIZE = "maxsize";
    protected static final String PARAM_SINGLE_LINE_MODE = "singleLineMode";
    protected static final String LABEL_DROPCONTENT = "editor.dropcontent";
    protected static final String LABEL_UPLOAD = "editor.selectcontent";
    protected static final String FILE_UPLOAD_MAX_SIZE = "fileUpload.maxSize";
    protected static final int DEFAULT_MAX_FILE_SIZE = 10000;
    protected static final int UNLIMITED_FILE_SIZE = -1;
    protected static final String LABEL_UNSUPPORTED_FILETYPE = "editor.unsupported.filetype";
    protected static final String LABEL_DROPCONTENT_TOOLTIP = "editor.dropcontent.tooltip";
    protected static final String ON_MAX_FILE_COUNT_EXCEED = "onMaxFileCountExceed";
    private static final Logger LOG = LoggerFactory.getLogger(AbstractDragAndDropFileUploadEditor.class);
    @Resource
    private ObjectPreviewService objectPreviewService;
    @Resource
    private CockpitProperties cockpitProperties;
    @Resource
    private MimeTypeResolver mimeTypeResolver;
    @Resource
    private MimeTypeChecker mimeTypeChecker;


    protected abstract String getEditorSclass();


    protected void showUnsupportedMediaMessage(final Component parent, final EditorContext<T> context, final Media media)
    {
        final String fileName = media != null ? StringEscapeUtils.escapeHtml(media.getName()) : StringUtils.EMPTY;
        final String label = context.getLabel(LABEL_UNSUPPORTED_FILETYPE, new String[]
                        {fileName});
        showUnsupportedMediaMessage(parent, label);
    }


    @InextensibleMethod
    void showUnsupportedMediaMessage(final Component parent, final String label)
    {
        Clients.showNotification(label, Clients.NOTIFICATION_TYPE_WARNING, parent, MenupopupPosition.BEFORE_END.getName(), 5000);
    }


    protected boolean isUploadedMediaAcceptable(final Media media, final EditorContext<T> ctx)
    {
        final String acceptParam = ctx.getParameterAs(PARAM_ACCEPT);
        return mimeTypeChecker.isMediaAcceptable(media, acceptParam);
    }


    /**
     * @deprecated since 2005. Not used anymore
     */
    @Deprecated(since = "2005", forRemoval = true)
    protected boolean matchContentType(final String accept, final String contentType)
    {
        if(StringUtils.isEmpty(accept) || StringUtils.isEmpty(contentType))
        {
            return false;
        }
        final String regexp = StringUtils.replace(accept, "*", ".*");
        return Pattern.matches(regexp, contentType);
    }


    protected Component createUploadContainer(final EditorContext<T> context, final EventListener<UploadEvent> onUpload,
                    final EventListener<Event> onMaxFileCountExceed)
    {
        final Div uploadContainer = new Div();
        uploadContainer.setSclass(getEditorSclass() + "-upload-container");
        final Div dropAnchor = new Div();
        dropAnchor.setSclass(getEditorSclass() + "-drop-anchor");
        final Button fileUpload = createFileUpload(context);
        dropAnchor.appendChild(fileUpload);
        final Label label = new Label(context.getLabel(LABEL_DROPCONTENT));
        dropAnchor.appendChild(label);
        uploadContainer.appendChild(dropAnchor);
        if(context.isEditable())
        {
            final Component dropUpload = createDropUpload(dropAnchor, context);
            uploadContainer.appendChild(dropUpload);
            final Label dropPanelLabel = new Label(getDropAreaConstraintInfo(context));
            dropPanelLabel.setSclass(getEditorSclass() + "-drop-upload-size-label");
            uploadContainer.appendChild(dropPanelLabel);
            fileUpload.addEventListener(Events.ON_UPLOAD, onUpload);
            dropUpload.addEventListener(Events.ON_UPLOAD, onUpload);
            dropUpload.addEventListener(ON_MAX_FILE_COUNT_EXCEED, onMaxFileCountExceed);
        }
        return uploadContainer;
    }


    protected Component createUploadContainer(final EditorContext<T> context, final EventListener<UploadEvent> onUpload)
    {
        return createUploadContainer(context, onUpload, event -> {
        });
    }


    /**
     * @param context
     *           to allow to read l10n data
     * @return localized information on allowed file size and number of allowed files (if applicable)
     */
    protected String getDropAreaConstraintInfo(final EditorContext<T> context)
    {
        return context.getLabel(LABEL_DROPCONTENT_TOOLTIP, new Object[]
                        {readableFileSize(getMaxFileSize(context))});
    }


    /**
     * As described at <a href="https://stackoverflow.com/a/5599842/7814797">StackOverflow</a>
     *
     * @param sizeInKB
     *           size in kilo bytes
     */
    protected String readableFileSize(final long sizeInKB)
    {
        final long sizeInBytes = sizeInKB * BYTES_IN_KILOBYTE;
        if(sizeInBytes <= 0)
        {
            return "0 MB";
        }
        final String[] units = new String[]
                        {"B", "kB", "MB", "GB", "TB"};
        final int digitGroups = (int)(Math.log10(sizeInBytes) / Math.log10(BYTES_IN_KILOBYTE));
        final String formattedSize = ALLOWED_SIZE_DECIMAL_FORMAT.format(sizeInBytes / Math.pow(BYTES_IN_KILOBYTE, digitGroups));
        return String.format("%s %s", formattedSize, units[digitGroups]);
    }


    protected Button createFileUpload(final EditorContext<T> context)
    {
        final Button fileUpload = new Button(context.getLabel(LABEL_UPLOAD));
        fileUpload.setUpload(getUpload(context));
        fileUpload.setDisabled(!context.isEditable());
        fileUpload.setSclass(getEditorSclass() + "-fileupload" + " " + getEditorSclass() + "-component ye-text-button");
        return fileUpload;
    }


    protected String getUpload(final EditorContext<T> context)
    {
        final String acceptParam = context.getParameterAs(PARAM_ACCEPT);
        final String accept = StringUtils.isNotEmpty(acceptParam) ? String.format(",accept=%s", acceptParam) : StringUtils.EMPTY;
        return String.format("true,maxsize=%d%s", getMaxFileSize(context), accept);
    }


    protected Component createDropUpload(final Component dropAnchor, final EditorContext<T> context)
    {
        final Dropupload dropUpload = new Dropupload();
        dropUpload.setAnchor(dropAnchor);
        dropUpload.setContent(context.getLabel(LABEL_DROPCONTENT));
        dropUpload.setSclass(getEditorSclass() + "-drop-upload");
        dropUpload.setMaxsize(getMaxFileSize(context));
        dropUpload.setNative(true);
        return dropUpload;
    }


    protected Optional<FileUploadResult> toFileUploadResult(final Media media)
    {
        final byte[] data = extractUploadedData(media);
        if(data != null)
        {
            final FileUploadResult fileUploadResult = new FileUploadResult();
            fileUploadResult.setData(data);
            fileUploadResult.setFormat(media.getFormat());
            fileUploadResult.setContentType(mimeTypeResolver.getMimeFromFirstBytes(data, media.getContentType()));
            fileUploadResult.setName(media.getName());
            return Optional.of(fileUploadResult);
        }
        return Optional.empty();
    }


    protected byte[] extractUploadedData(final Media media)
    {
        final byte[] data;
        if(media.isBinary())
        {
            try
            {
                data = media.getStreamData().readAllBytes();
            }
            catch(final IOException ioException)
            {
                LOG.warn("Could not read stream", ioException);
                throw new IllegalStateException(ioException);
            }
        }
        else
        {
            data = media.getStringData() != null ? media.getStringData().getBytes() : null;
        }
        return data;
    }


    /**
     * @param context
     *           context object to read configuration
     * @return maximal file size in kilobytes
     */
    protected int getMaxFileSize(final EditorContext<T> context)
    {
        final Object parameterValue = context.getParameter(PARAM_MAX_SIZE);
        if(parameterValue != null)
        {
            try
            {
                final int sizeFromEditorParam = Integer.parseInt(parameterValue.toString());
                if(isCorrectFileSize(sizeFromEditorParam))
                {
                    return sizeFromEditorParam;
                }
            }
            catch(final NumberFormatException e)
            {
                LOG.warn("Wrong maxsize param value, default config will be used", e);
            }
        }
        final int sizeFromSystemProperty = getCockpitProperties().getInteger(FILE_UPLOAD_MAX_SIZE);
        return isCorrectFileSize(sizeFromSystemProperty) ? sizeFromSystemProperty : DEFAULT_MAX_FILE_SIZE;
    }


    protected boolean isCorrectFileSize(final int sizeFromEditorParam)
    {
        return sizeFromEditorParam > 0 || sizeFromEditorParam == UNLIMITED_FILE_SIZE;
    }


    protected boolean isSingleLineModeEnabled(final EditorContext<T> context)
    {
        return context.getParameterAsBoolean(PARAM_SINGLE_LINE_MODE, false);
    }


    protected Optional<Image> getContentImage(final FileUploadResult uploadResult)
    {
        if(StringUtils.containsIgnoreCase(uploadResult.getContentType(), "image"))
        {
            try
            {
                final Image image = new Image();
                image.setContent(new AImage(uploadResult.getName(), uploadResult.getData()));
                return Optional.of(image);
            }
            catch(final IOException e)
            {
                LOG.debug("Cannot render image preview", e);
            }
        }
        return Optional.empty();
    }


    protected Image getContentTypeImage(final String contentType, final boolean gridPreview)
    {
        final DefaultCockpitContext ctx = new DefaultCockpitContext();
        if(gridPreview)
        {
            ctx.setParameter(ObjectPreviewService.PREFERRED_PREVIEW_SUFFIX, "grid");
        }
        final ObjectPreview objectPreview = getObjectPreviewService().getPreview(contentType, ctx);
        final Image image = new Image();
        image.setSrc(objectPreview.getUrl());
        return image;
    }


    protected void addImageZoomPopup(final Component parent, final Image target)
    {
        if(target.getContent() == null)
        {
            return;
        }
        final Popup zoomPopup = new Popup();
        final Image popupImage = new Image();
        popupImage.setContent(target.getContent());
        UITools.addSClass(popupImage, "ye-listview-preview-popup-image");
        zoomPopup.appendChild(popupImage);
        parent.appendChild(zoomPopup);
        target.addEventListener(Events.ON_MOUSE_OVER, event -> zoomPopup.open(target, MenupopupPosition.BEFORE_START.getName()));
        target.addEventListener(Events.ON_MOUSE_OUT, event -> zoomPopup.close());
    }


    public ObjectPreviewService getObjectPreviewService()
    {
        return objectPreviewService;
    }


    public CockpitProperties getCockpitProperties()
    {
        return cockpitProperties;
    }
}
