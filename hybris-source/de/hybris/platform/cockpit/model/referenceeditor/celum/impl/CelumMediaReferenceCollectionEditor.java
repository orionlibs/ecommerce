package de.hybris.platform.cockpit.model.referenceeditor.celum.impl;

import de.hybris.platform.cockpit.model.editor.AdditionalReferenceEditorListener;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.referenceeditor.impl.MediaReferenceCollectionEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zul.ListitemRenderer;

@Deprecated
public class CelumMediaReferenceCollectionEditor extends MediaReferenceCollectionEditor
{
    private static final Logger LOG = LoggerFactory.getLogger(CelumMediaReferenceCollectionEditor.class);
    private CelumMediaEditorBase celumMediaEditorBase;


    public CelumMediaReferenceCollectionEditor(EditorListener editorListener, AdditionalReferenceEditorListener additionalReferenceEditorListener)
    {
        super(editorListener, additionalReferenceEditorListener);
    }


    public CelumMediaReferenceCollectionEditor(EditorListener editorListener, AdditionalReferenceEditorListener additionalReferenceEditorListener, CelumMediaEditorBase celumMediaEditorBase)
    {
        super(editorListener, additionalReferenceEditorListener);
        this.celumMediaEditorBase = celumMediaEditorBase;
    }


    protected ListitemRenderer createCollectionItemListRenderer()
    {
        return (ListitemRenderer)new Object(this);
    }


    protected CelumMediaEditorBase getCelumMediaEditorBase()
    {
        return this.celumMediaEditorBase;
    }
}
