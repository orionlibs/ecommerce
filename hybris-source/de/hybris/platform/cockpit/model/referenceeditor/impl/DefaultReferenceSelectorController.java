package de.hybris.platform.cockpit.model.referenceeditor.impl;

import de.hybris.platform.cockpit.model.editor.AdditionalReferenceEditorListener;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.misc.ComponentController;
import de.hybris.platform.cockpit.model.referenceeditor.AbstractReferenceSelectorModel;
import de.hybris.platform.cockpit.model.referenceeditor.ReferenceSelectorListener;
import de.hybris.platform.cockpit.model.referenceeditor.ReferenceSelectorModelListener;
import de.hybris.platform.cockpit.model.referenceeditor.SelectorModelListener;
import de.hybris.platform.cockpit.model.referenceeditor.UIReferenceSelector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultReferenceSelectorController implements ComponentController
{
    private static final Logger log = LoggerFactory.getLogger(DefaultReferenceSelectorController.class);
    protected transient UIReferenceSelector selector;
    protected final DefaultReferenceSelectorModel model;
    private ReferenceSelectorListener referenceSelectorListener = null;
    private ReferenceSelectorModelListener referenceSelectorModelListener = null;
    protected final EditorListener editorListener;
    protected final AdditionalReferenceEditorListener additionalReferenceEditorListener;


    public DefaultReferenceSelectorController(DefaultReferenceSelectorModel model, UIReferenceSelector selector, EditorListener editorListener, AdditionalReferenceEditorListener additionalListener)
    {
        this.model = model;
        this.selector = selector;
        this.editorListener = editorListener;
        this.additionalReferenceEditorListener = additionalListener;
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
        return (ReferenceSelectorListener)new DefaultReferenceSelectorListener(model, this.editorListener, this.additionalReferenceEditorListener);
    }


    protected ReferenceSelectorModelListener createReferenceSelectorModelListener(AbstractReferenceSelectorModel model, UIReferenceSelector selector)
    {
        return (ReferenceSelectorModelListener)new DefaultReferenceSelectorModelListener(model, selector, this.editorListener);
    }


    public void unregisterListeners()
    {
        log.debug("Desktop removed - Unregistering all listeners...");
        if(this.model != null)
        {
            this.model.removeSelectorModelListener((SelectorModelListener)this.referenceSelectorModelListener);
        }
        if(this.selector != null)
        {
            this.selector.removeReferenceSelectorListener(this.referenceSelectorListener);
        }
    }
}
