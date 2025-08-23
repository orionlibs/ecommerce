package de.hybris.platform.cockpit.model.referenceeditor.simple.impl;

import de.hybris.platform.cockpit.model.editor.AdditionalReferenceEditorListener;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.misc.ComponentController;
import de.hybris.platform.cockpit.model.referenceeditor.simple.AbstractSimpleReferenceSelectorModel;
import de.hybris.platform.cockpit.model.referenceeditor.simple.SimpleReferenceSelectorListener;
import de.hybris.platform.cockpit.model.referenceeditor.simple.SimpleReferenceSelectorModelListener;
import de.hybris.platform.cockpit.model.referenceeditor.simple.SimpleSelectorModelListener;
import de.hybris.platform.cockpit.model.referenceeditor.simple.UISimpleReferenceSelector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultSimpleReferenceSelectorController implements ComponentController
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultSimpleReferenceSelectorController.class);
    protected transient UISimpleReferenceSelector selector;
    protected final DefaultSimpleReferenceSelectorModel model;
    protected final EditorListener editorListener;
    protected final AdditionalReferenceEditorListener referenceEditorListener;
    private SimpleReferenceSelectorListener selectorListener = null;
    private SimpleReferenceSelectorModelListener selectorModelListener = null;


    public DefaultSimpleReferenceSelectorController(DefaultSimpleReferenceSelectorModel model, UISimpleReferenceSelector selector, EditorListener editorListener, AdditionalReferenceEditorListener additionalListener)
    {
        this.model = model;
        this.selector = selector;
        this.editorListener = editorListener;
        this.referenceEditorListener = additionalListener;
    }


    public void initialize()
    {
        this.selectorListener = createSimpleReferenceSelectorListener(this.model);
        this.selectorModelListener = createSimpleReferenceSelectorModelListener((AbstractSimpleReferenceSelectorModel)this.model, this.selector);
        this.selector.addReferenceSelectorListener(this.selectorListener);
        this.model.addSelectorModelListener((SimpleSelectorModelListener)this.selectorModelListener);
    }


    protected SimpleReferenceSelectorListener createSimpleReferenceSelectorListener(DefaultSimpleReferenceSelectorModel model)
    {
        return (SimpleReferenceSelectorListener)new DefaultSimpleReferenceSelectorListener(model, this.referenceEditorListener);
    }


    protected SimpleReferenceSelectorModelListener createSimpleReferenceSelectorModelListener(AbstractSimpleReferenceSelectorModel model, UISimpleReferenceSelector selector)
    {
        return (SimpleReferenceSelectorModelListener)new DefaultSimpleReferenceSelectorModelListener(model, selector, this.editorListener);
    }


    public void unregisterListeners()
    {
        LOG.debug("Desktop removed - Unregistering all listeners...");
        if(this.model != null)
        {
            this.model.removeSelectorModelListener((SimpleSelectorModelListener)this.selectorModelListener);
        }
        if(this.selector != null)
        {
            this.selector.removeReferenceSelectorListener(this.selectorListener);
        }
    }
}
