/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.defaultfileupload;

import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.editors.impl.AbstractCockpitEditorRenderer;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.image.AImage;
import org.zkoss.image.Image;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Vbox;

/**
 * Editor to manage media content
 */
public class DefaultFileUploadEditor extends AbstractCockpitEditorRenderer<FileUploadResult>
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultFileUploadEditor.class);
    public static final String UPLOAD_TRUE = "true";
    public static final String IMAGE = "image";


    @Override
    public void render(final Component parent, final EditorContext<FileUploadResult> context,
                    final EditorListener<FileUploadResult> listener)
    {
        Validate.notNull("All parameters are mandatory", parent, context, listener);
        final Div uploadComponent = new Div();
        uploadComponent.setParent(parent);
        final Vbox vbox = new Vbox();
        uploadComponent.appendChild(vbox);
        final Div toolbar = new Div();
        vbox.appendChild(toolbar);
        final Button uploadButton = new Button(getL10nDecorator(context, "actionUpload", "action.upload"));
        uploadButton.setUpload(UPLOAD_TRUE);
        toolbar.appendChild(uploadButton);
        final Button resetButton = new Button(getL10nDecorator(context, "actionReset", "action.reset"));
        toolbar.appendChild(resetButton);
        final Div previewComponent = new Div();
        vbox.appendChild(previewComponent);
        final FileUploadResult initialValue = context.getInitialValue();
        generatePreview(initialValue, previewComponent);
        uploadButton.addEventListener(Events.ON_UPLOAD, new EventListener<UploadEvent>()
        {
            @Override
            public void onEvent(final UploadEvent event)
            {
                final Media media = event.getMedia();
                previewComponent.getChildren().clear();
                generatePreview(media, previewComponent);
                final FileUploadResult res = new FileUploadResult();
                res.setContentType(media.getContentType());
                res.setData(extractUploadedData(media));
                res.setFormat(media.getFormat());
                res.setName(media.getName());
                listener.onValueChanged(res);
            }
        });
        resetButton.addEventListener(Events.ON_CLICK, new EventListener<Event>()
        {
            @Override
            public void onEvent(final Event event) throws Exception
            {
                listener.onValueChanged(initialValue);
                previewComponent.getChildren().clear();
                generatePreview(initialValue, previewComponent);
            }
        });
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
                throw new IllegalStateException(ioException);
            }
        }
        else
        {
            data = media.getStringData() != null ? media.getStringData().getBytes(StandardCharsets.UTF_8) : null;
        }
        return data;
    }


    protected void generatePreview(final FileUploadResult value, final Div previewComponent)
    {
        if(value != null && value.getData() != null && value.getContentType() != null && value.getContentType().contains(IMAGE))
        {
            try
            {
                final Image img = new AImage(value.getName(), value.getData());
                generatePreview(img, previewComponent);
            }
            catch(final IOException e)
            {
                LOG.warn("Error while generating preview", e);
            }
        }
    }


    protected void generatePreview(final Media media, final Div previewComponent)
    {
        if(media instanceof org.zkoss.image.Image)
        {
            final org.zkoss.zul.Image image = new org.zkoss.zul.Image();
            image.setContent((Image)media);
            previewComponent.appendChild(image);
        }
    }
}
