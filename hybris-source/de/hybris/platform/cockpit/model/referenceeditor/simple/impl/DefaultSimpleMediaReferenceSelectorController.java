package de.hybris.platform.cockpit.model.referenceeditor.simple.impl;

import de.hybris.platform.cockpit.model.editor.AdditionalReferenceEditorListener;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.referenceeditor.simple.SimpleReferenceSelectorListener;
import de.hybris.platform.cockpit.model.referenceeditor.simple.UISimpleReferenceSelector;

public class DefaultSimpleMediaReferenceSelectorController extends DefaultSimpleReferenceSelectorController
{
    protected transient UISimpleReferenceSelector selector;


    public DefaultSimpleMediaReferenceSelectorController(DefaultSimpleReferenceSelectorModel model, UISimpleReferenceSelector selector, EditorListener editorListener, AdditionalReferenceEditorListener additionalListener)
    {
        super(model, selector, editorListener, additionalListener);
    }


    protected SimpleReferenceSelectorListener createSimpleReferenceSelectorListener(DefaultSimpleReferenceSelectorModel model)
    {
        return (SimpleReferenceSelectorListener)new DefaultSimpleMediaReferenceSelectorListener(model, this.referenceEditorListener);
    }
}
