package de.hybris.platform.cmscockpit.components.editor.impl;

import de.hybris.platform.cmscockpit.components.editor.MediaReferencePreviewListener;
import de.hybris.platform.cmscockpit.components.editor.MediaReferencePreviewModel;
import de.hybris.platform.cmscockpit.components.editor.MediaReferencePreviewModelListener;
import de.hybris.platform.cmscockpit.components.editor.UIMediaReferencePreview;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.misc.ComponentController;
import org.apache.log4j.Logger;

public class DefaultMediaReferencePreviewController implements ComponentController
{
    private static final Logger log = Logger.getLogger(DefaultMediaReferencePreviewController.class);
    protected transient UIMediaReferencePreview previewSelector;
    protected final MediaReferencePreviewModel model;
    private MediaReferencePreviewListener mediaReferencePreviewListener;
    private MediaReferencePreviewModelListener mediaReferencePreviewModelListener;
    private final EditorListener editorListener;


    public DefaultMediaReferencePreviewController(MediaReferencePreviewModel model, UIMediaReferencePreview previewSelector, EditorListener editorListener)
    {
        this.model = model;
        this.previewSelector = previewSelector;
        this.editorListener = editorListener;
    }


    public void initialize()
    {
        this.mediaReferencePreviewListener = createMediaReferencePreviewListener(this.model);
        this.mediaReferencePreviewModelListener = createMediaReferencePreviewModelListener();
        this.previewSelector.addMediaReferencePreviewListener(this.mediaReferencePreviewListener);
        this.model.addMediaReferencePreviewModelListener(this.mediaReferencePreviewModelListener);
    }


    protected MediaReferencePreviewListener createMediaReferencePreviewListener(MediaReferencePreviewModel model)
    {
        return (MediaReferencePreviewListener)new DefaultMediaReferencePreviewListener(model);
    }


    protected MediaReferencePreviewModelListener createMediaReferencePreviewModelListener()
    {
        return (MediaReferencePreviewModelListener)new DefaultMediaReferencePreviewModelListener(this.editorListener);
    }


    public void unregisterListeners()
    {
        log.debug("Desktop removed - Unregistering all listeners...");
        if(this.model != null)
            ;
        if(this.previewSelector != null)
        {
            this.previewSelector.removeMediaReferencePreviewListener(this.mediaReferencePreviewListener);
        }
    }
}
