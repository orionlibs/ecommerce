package de.hybris.platform.cockpit.model.referenceeditor.impl;

import de.hybris.platform.cockpit.model.editor.AdditionalReferenceEditorListener;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.referenceeditor.AbstractReferenceSelectorModel;
import de.hybris.platform.cockpit.model.referenceeditor.ReferenceSelectorListener;
import de.hybris.platform.cockpit.model.referenceeditor.ReferenceSelectorModelListener;
import de.hybris.platform.cockpit.model.referenceeditor.SelectorModelListener;
import de.hybris.platform.cockpit.model.referenceeditor.UIReferenceSelector;

public class DefaultMediaReferenceSelectorController extends DefaultReferenceSelectorController
{
    private ReferenceSelectorListener referenceSelectorListener = null;
    private ReferenceSelectorModelListener referenceSelectorModelListener = null;


    public DefaultMediaReferenceSelectorController(DefaultReferenceSelectorModel model, UIReferenceSelector selector, EditorListener editorListener, AdditionalReferenceEditorListener additionalReferenceEditorListener)
    {
        super(model, selector, editorListener, additionalReferenceEditorListener);
    }


    public void initialize()
    {
        this.referenceSelectorListener = createReferenceSelectorListener(this.model);
        this.referenceSelectorModelListener = createReferenceSelectorModelListener((AbstractReferenceSelectorModel)this.model, this.selector);
        this.selector.addReferenceSelectorListener(this.referenceSelectorListener);
        this.model.addSelectorModelListener((SelectorModelListener)this.referenceSelectorModelListener);
    }


    protected ReferenceSelectorListener createReferenceSelectorListener(DefaultReferenceSelectorModel model)
    {
        return (ReferenceSelectorListener)new DefaultMediaReferenceSelectorListener(model, this.editorListener, this.additionalReferenceEditorListener);
    }
}
