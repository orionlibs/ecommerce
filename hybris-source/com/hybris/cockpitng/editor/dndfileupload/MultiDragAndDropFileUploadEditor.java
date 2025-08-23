/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.dndfileupload;

import com.hybris.cockpitng.editor.defaultfileupload.FileUploadResult;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.function.BiConsumer;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkmax.zul.Dropupload;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.event.ZulEvents;

/**
 * Multi file upload editor. Accepted params:
 * <ul>
 * <li>accept - specifies the MIME types of files that the server accepts e.g "audio/|video/|image/*"</li>
 * <li>maxsize - defines max file size, if not defined then default system value will be used (property
 * fileUpload.maxSize)</li>
 * <li>singleLineMode - when set to true upload content will be displayed in single line</li>
 * <li>selectedItemsMaxSize - defines number of uploaded elements when list scroll should be shown
 * {@link Listbox#setRows(int)}. Default value: {@value #DEFAULT_SELECTED_ITEMS_MAX_SIZE}.</li>
 * </ul>
 */
public class MultiDragAndDropFileUploadEditor extends AbstractDragAndDropFileUploadEditor<Collection<FileUploadResult>>
{
    public static final int DEFAULT_SELECTED_ITEMS_MAX_SIZE = 5;
    protected static final String EDITOR_PREFIX = "ye-dndfileupload";
    protected static final String EDITOR_CONTAINER_SCLASS = "ye-multidndfileupload";
    protected static final String PARAM_SELECTED_ITEMS_MAX_SIZE = "selectedItemsMaxSize";
    protected static final String LABEL_UNSUPPORTED_UPLOAD = "editor.unsupported.upload";
    protected static final String LABEL_EXCEEDED_MAX_FILES_COUNT = "editor.exceeded.max.files.count";
    protected static final String LABEL_EXCEEDED_MAX_UPLOAD_SIZE = "editor.exceeded.max.upload.size";
    protected static final String PARAM_MAX_FILE_COUNT = "maxfilecount";
    protected static final String PARAM_MAX_ALLOWED_TOTAL_UPLOAD_SIZE = "maxtotaluploadsize";
    protected static final int DEFAULT_MAX_FILE_COUNT = 20;
    protected static final String FILE_UPLOAD_MAX_FILE_COUNT = "fileUpload.maxfilecount";
    protected static final String LABEL_DROPCONTENT_TOOLTIP_MULTI = "editor.dropcontent.tooltip.multi";
    private static final Logger LOG = LoggerFactory.getLogger(MultiDragAndDropFileUploadEditor.class);
    private static final int DURATION_OF_CLIENTS_NOTIFICATION = 5000;
    private static final String POSITION_BEFORE_END = "before_end";


    @Override
    protected String getEditorSclass()
    {
        return EDITOR_PREFIX;
    }


    @Override
    public void render(final Component parent, final EditorContext<Collection<FileUploadResult>> context,
                    final EditorListener<Collection<FileUploadResult>> listener)
    {
        if(context == null || listener == null || parent == null)
        {
            return;
        }
        final Div editorContainer = new Div();
        editorContainer.setSclass(getEditorSclass() + " " + EDITOR_CONTAINER_SCLASS);
        parent.appendChild(editorContainer);
        final Div uploadedListContainer = new Div();
        uploadedListContainer.setSclass(EDITOR_PREFIX + "-preview-container");
        editorContainer.appendChild(uploadedListContainer);
        final Listbox uploadedList = createUploadedFilesList(context, listener);
        uploadedList.setParent(uploadedListContainer);
        uploadedList.getModel().addListDataListener(listDataEvent -> uploadedList.setVisible(uploadedList.getItemCount() > 0));
        uploadedList.setVisible(uploadedList.getItemCount() > 0);
        final Component uploadContainer = createUploadContainer(context,
                        (UploadEvent event) -> onUpload(uploadedList, event, context, listener),
                        (Event event) -> onMaxFileCountExceed(uploadedList, context));
        editorContainer.appendChild(uploadContainer);
    }


    protected void onUpload(final Listbox uploadedList, final UploadEvent event,
                    final EditorContext<Collection<FileUploadResult>> context, final EditorListener<Collection<FileUploadResult>> listener)
    {
        final Media[] medias = event.getMedias();
        if(event.getMedias() == null)
        {
            return;
        }
        boolean containsUnsupportedMedia = false;
        long totalSize = 0;
        final long maxAllowedTotalUploadSizeInBytes = getMaxAllowedTotalUploadSizeInKilobytes(context) * BYTES_IN_KILOBYTE;
        for(final Media media : medias)
        {
            totalSize += media.getByteData().length;
            if(totalSize > maxAllowedTotalUploadSizeInBytes)
            {
                showTotalAllowedUploadSizeExceededMessage(uploadedList.getParent(), context);
                return;
            }
        }
        final ListModelList model = (ListModelList)uploadedList.getModel();
        for(final Media media : medias)
        {
            if(isUploadedMediaAcceptable(media, context))
            {
                final Optional<FileUploadResult> fileUploadResult = toFileUploadResult(media);
                if(fileUploadResult.isPresent())
                {
                    model.add(fileUploadResult.get());
                    listener.onValueChanged(new ArrayList<>(model));
                }
                else
                {
                    containsUnsupportedMedia = true;
                }
            }
            else
            {
                containsUnsupportedMedia = true;
            }
        }
        if(containsUnsupportedMedia)
        {
            showUnsupportedMediaMessage(uploadedList.getParent(), context);
        }
    }


    protected void onMaxFileCountExceed(final Listbox uploadedList, final EditorContext<Collection<FileUploadResult>> context)
    {
        showMaxFileCountExceededMessage(uploadedList.getParent(), context);
    }


    protected Listbox createUploadedFilesList(final EditorContext<Collection<FileUploadResult>> context,
                    final EditorListener<Collection<FileUploadResult>> listener)
    {
        final Listbox listbox = new Listbox();
        listbox.setSclass(EDITOR_PREFIX + "-preview-list");
        final ListModelList<FileUploadResult> model = new ListModelList<>();
        listbox.setModel(model);
        final int selectedReferencesMaxSize = getSelectedReferencesMaxSize(context);
        listbox.addEventListener(ZulEvents.ON_AFTER_RENDER, event -> {
            final int size = model.size();
            final int rows = size > selectedReferencesMaxSize ? selectedReferencesMaxSize : size;
            listbox.setRows(rows);
        });
        listbox.setItemRenderer((Listitem item, FileUploadResult data, int index) -> renderUploadedFile(item, data, index, context,
                        (Integer idx, FileUploadResult removedFile) -> {
                            model.remove(idx.intValue());
                            listener.onValueChanged(new ArrayList<>(model));
                        }));
        model.addAll(CollectionUtils.emptyIfNull(context.getInitialValue()));
        return listbox;
    }


    protected int getSelectedReferencesMaxSize(final EditorContext<Collection<FileUploadResult>> context)
    {
        final Object selectedItemsMaxSize = context.getParameter(PARAM_SELECTED_ITEMS_MAX_SIZE);
        if(selectedItemsMaxSize != null)
        {
            try
            {
                return Integer.parseInt(selectedItemsMaxSize.toString());
            }
            catch(final NumberFormatException nfe)
            {
                LOG.warn(
                                String.format("Wrong value of params:%s given value:%s", PARAM_SELECTED_ITEMS_MAX_SIZE, selectedItemsMaxSize),
                                nfe);
            }
        }
        return DEFAULT_SELECTED_ITEMS_MAX_SIZE;
    }


    protected void renderUploadedFile(final Listitem item, final FileUploadResult data, final int index,
                    final EditorContext<Collection<FileUploadResult>> context, final BiConsumer<Integer, FileUploadResult> onRemoveReference)
    {
        final Listcell previewCell = new Listcell();
        item.setSclass(EDITOR_PREFIX + "-preview-listitem");
        item.appendChild(previewCell);
        final Image image = getContentImageWithZoom(previewCell, data).orElse(getContentTypeImage(data.getContentType(), false));
        image.setSclass(EDITOR_PREFIX + "-preview-image");
        previewCell.appendChild(image);
        final Label fileNameLabel = new Label(data.getName());
        fileNameLabel.setSclass(EDITOR_PREFIX + "-preview-filename");
        previewCell.appendChild(fileNameLabel);
        if(context.isEditable())
        {
            final Button removeButton = new Button();
            removeButton.setSclass(String.format("%s %s", EDITOR_PREFIX + "-preview-remove-btn", "ye-delete-btn"));
            previewCell.appendChild(removeButton);
            removeButton.addEventListener(Events.ON_CLICK, event -> onRemoveReference.accept(index, data));
        }
    }


    protected Optional<Image> getContentImageWithZoom(final Component container, final FileUploadResult fileUploadResult)
    {
        final Optional<Image> contentImage = getContentImage(fileUploadResult);
        contentImage.ifPresent(image -> addImageZoomPopup(container, image));
        return contentImage;
    }


    protected void showUnsupportedMediaMessage(final Component messageAnchor,
                    final EditorContext<Collection<FileUploadResult>> context)
    {
        Clients.showNotification(context.getLabel(LABEL_UNSUPPORTED_UPLOAD), Clients.NOTIFICATION_TYPE_WARNING, messageAnchor,
                        POSITION_BEFORE_END, DURATION_OF_CLIENTS_NOTIFICATION);
    }


    protected void showMaxFileCountExceededMessage(final Component messageAnchor,
                    final EditorContext<Collection<FileUploadResult>> context)
    {
        Clients.showNotification(context.getLabel(LABEL_EXCEEDED_MAX_FILES_COUNT), Clients.NOTIFICATION_TYPE_WARNING, messageAnchor,
                        POSITION_BEFORE_END, DURATION_OF_CLIENTS_NOTIFICATION);
    }


    protected void showTotalAllowedUploadSizeExceededMessage(final Component messageAnchor,
                    final EditorContext<Collection<FileUploadResult>> context)
    {
        Clients.showNotification(context.getLabel(LABEL_EXCEEDED_MAX_UPLOAD_SIZE), Clients.NOTIFICATION_TYPE_WARNING, messageAnchor,
                        POSITION_BEFORE_END, DURATION_OF_CLIENTS_NOTIFICATION);
    }


    @Override
    protected Component createDropUpload(final Component dropAnchor, final EditorContext<Collection<FileUploadResult>> context)
    {
        final Dropupload upload = (Dropupload)super.createDropUpload(dropAnchor, context);
        upload.setMaxFileCount(getMaxFileCount(context));
        return upload;
    }


    @Override
    protected String getDropAreaConstraintInfo(final EditorContext<Collection<FileUploadResult>> context)
    {
        return context.getLabel(LABEL_DROPCONTENT_TOOLTIP_MULTI, new Object[]
                        {getMaxFileCount(context), readableFileSize(getMaxFileSize(context)),
                                        readableFileSize(getMaxAllowedTotalUploadSizeInKilobytes(context))});
    }


    /**
     * @param context
     *           context object to read configuration
     * @return maximal number of uploaded files
     */
    protected int getMaxFileCount(final EditorContext<Collection<FileUploadResult>> context)
    {
        final Object parameterValue = context.getParameter(PARAM_MAX_FILE_COUNT);
        if(parameterValue != null)
        {
            try
            {
                final int countFromEditorParam = Integer.parseInt(parameterValue.toString());
                if(countFromEditorParam > 0)
                {
                    return countFromEditorParam;
                }
            }
            catch(final NumberFormatException e)
            {
                LOG.warn("Wrong maxfilecount param value, default config will be used", e);
            }
        }
        final int countFromSystemProperty = getCockpitProperties().getInteger(FILE_UPLOAD_MAX_FILE_COUNT);
        return countFromSystemProperty > 0 ? countFromSystemProperty : DEFAULT_MAX_FILE_COUNT;
    }


    /**
     * @param context
     *           context object to read configuration
     * @return maximal allowed upload size in kilobytes
     */
    protected long getMaxAllowedTotalUploadSizeInKilobytes(final EditorContext<Collection<FileUploadResult>> context)
    {
        final Object parameterValue = context.getParameter(PARAM_MAX_ALLOWED_TOTAL_UPLOAD_SIZE);
        if(parameterValue != null)
        {
            try
            {
                final long maxUploadSize = Long.parseLong(parameterValue.toString());
                if(maxUploadSize > 0)
                {
                    return maxUploadSize;
                }
            }
            catch(final NumberFormatException e)
            {
                LOG.warn("Wrong maxtotaluploadsize param value, default config will be used", e);
            }
        }
        return getMaxFileCount(context) * (long)getMaxFileSize(context);
    }


    @Override
    protected String getUpload(final EditorContext<Collection<FileUploadResult>> context)
    {
        return super.getUpload(context) + ",multiple=true";
    }
}
