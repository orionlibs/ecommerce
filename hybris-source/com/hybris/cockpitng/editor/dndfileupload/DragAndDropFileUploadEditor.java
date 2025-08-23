/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.dndfileupload;

import com.hybris.cockpitng.editor.defaultfileupload.FileUploadResult;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import java.util.Optional;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;

/**
 * Single file upload editor. Accepted params:
 * <ul>
 * <li>accept - specifies the MIME types of files that the server accepts e.g "audio/|video/|image/*"</li>
 * <li>maxsize - defines max file size, if not defined then default system value will be used (property
 * fileUpload.maxSize)</li>
 * <li>singleLineMode - when set to true upload content and preview will be displayed in single line</li>
 * </ul>
 */
public class DragAndDropFileUploadEditor extends AbstractDragAndDropFileUploadEditor<FileUploadResult>
{
    protected static final String EDITOR_PREFIX = "ye-dndfileupload";


    @Override
    protected String getEditorSclass()
    {
        return EDITOR_PREFIX;
    }


    @Override
    public void render(final Component parent, final EditorContext<FileUploadResult> context,
                    final EditorListener<FileUploadResult> listener)
    {
        if(context != null && listener != null && parent != null)
        {
            if(context.getInitialValue() == null)
            {
                renderUploadView(parent, context, listener);
            }
            else
            {
                renderPreview(parent, context.getInitialValue(), context, listener);
            }
        }
    }


    protected void renderUploadView(final Component parent, final EditorContext<FileUploadResult> context,
                    final EditorListener<FileUploadResult> listener)
    {
        final EventListener<UploadEvent> onFileUpload = (UploadEvent event) -> onFileUpload(parent, context, listener, event);
        parent.appendChild(createUploadContainer(context, onFileUpload));
    }


    protected void onFileUpload(final Component parent, final EditorContext<FileUploadResult> context,
                    final EditorListener<FileUploadResult> listener, final UploadEvent event)
    {
        if(isUploadedMediaAcceptable(event.getMedia(), context))
        {
            final Optional<FileUploadResult> fileUploadResult = toFileUploadResult(event.getMedia());
            if(fileUploadResult.isPresent())
            {
                listener.onValueChanged(fileUploadResult.get());
                parent.getChildren().clear();
                renderPreview(parent, fileUploadResult.get(), context, listener);
                return;
            }
        }
        showUnsupportedMediaMessage(parent, context, event.getMedia());
    }


    protected void renderPreview(final Component parent, final FileUploadResult uploadResult,
                    final EditorContext<FileUploadResult> context, final EditorListener<FileUploadResult> listener)
    {
        final Div container = new Div();
        container.setParent(parent);
        if(context.isEditable())
        {
            final Button removeBtn = new Button();
            removeBtn.setSclass(String.format("%s %s", EDITOR_PREFIX + "-preview-remove-btn", "ye-delete-btn"));
            removeBtn.setParent(container);
            removeBtn.addEventListener(Events.ON_CLICK, event -> {
                listener.onValueChanged(null);
                parent.getChildren().clear();
                renderUploadView(parent, context, listener);
            });
        }
        final Image image = getContentImage(uploadResult)
                        .orElse(getContentTypeImage(uploadResult.getContentType(), !isSingleLineModeEnabled(context)));
        image.setSclass(EDITOR_PREFIX + "-preview-image");
        final Label label = new Label(uploadResult.getName());
        label.setSclass(EDITOR_PREFIX + "-preview-filename");
        final Div imageContainer = new Div();
        imageContainer.setParent(container);
        imageContainer.setSclass(EDITOR_PREFIX + "-preview-container");
        imageContainer.appendChild(image);
        imageContainer.appendChild(label);
        if(isSingleLineModeEnabled(context))
        {
            container.setSclass(EDITOR_PREFIX + "-container " + EDITOR_PREFIX + "-single-line-mode");
            addImageZoomPopup(imageContainer, image);
        }
        else
        {
            container.setSclass(EDITOR_PREFIX + "-container");
        }
    }
}
