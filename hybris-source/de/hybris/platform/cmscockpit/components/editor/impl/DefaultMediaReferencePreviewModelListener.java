package de.hybris.platform.cmscockpit.components.editor.impl;

import de.hybris.platform.cmscockpit.components.editor.MediaReferencePreviewModelListener;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.meta.TypedObject;

public class DefaultMediaReferencePreviewModelListener implements MediaReferencePreviewModelListener
{
    private final EditorListener editorListener;


    public DefaultMediaReferencePreviewModelListener(EditorListener editorListener)
    {
        this.editorListener = editorListener;
    }


    public void valueChanged(TypedObject typedObject)
    {
        this.editorListener.valueChanged(typedObject);
        this.editorListener.actionPerformed("force_save_clicked");
    }
}
