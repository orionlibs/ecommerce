package de.hybris.platform.cockpit.model.referenceeditor.collection.controller;

import de.hybris.platform.cockpit.model.editor.AdditionalReferenceEditorListener;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.misc.ComponentController;
import de.hybris.platform.cockpit.model.referenceeditor.collection.model.CollectionEditorModel;
import de.hybris.platform.cockpit.model.referenceeditor.simple.AbstractSimpleReferenceSelectorModel;
import de.hybris.platform.cockpit.model.referenceeditor.simple.SimpleReferenceSelectorListener;
import de.hybris.platform.cockpit.model.referenceeditor.simple.SimpleReferenceSelectorModelListener;
import de.hybris.platform.cockpit.model.referenceeditor.simple.SimpleSelectorModelListener;
import de.hybris.platform.cockpit.model.referenceeditor.simple.UISimpleReferenceSelector;
import de.hybris.platform.cockpit.model.referenceeditor.simple.impl.DefaultSimpleReferenceSelectorModel;
import de.hybris.platform.cockpit.model.referenceeditor.simple.impl.DefaultSimpleReferenceSelectorModelListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CollectionSelectorController implements ComponentController
{
    private static final Logger LOG = LoggerFactory.getLogger(CollectionSelectorController.class);
    protected transient UISimpleReferenceSelector selector;
    protected final DefaultSimpleReferenceSelectorModel model;
    protected final CollectionEditorModel parentModel;
    protected final EditorListener editorListener;
    protected final AdditionalReferenceEditorListener additionalReferenceEditorListener;
    private SimpleReferenceSelectorListener selectorListener = null;
    private SimpleReferenceSelectorModelListener selectorModelListener = null;


    public CollectionSelectorController(DefaultSimpleReferenceSelectorModel model, CollectionEditorModel parentModel, UISimpleReferenceSelector selector, EditorListener editorListener, AdditionalReferenceEditorListener additionalListener)
    {
        this.model = model;
        this.parentModel = parentModel;
        this.selector = selector;
        this.editorListener = editorListener;
        this.additionalReferenceEditorListener = additionalListener;
    }


    public void initialize()
    {
        this.selectorListener = createReferenceSelectorListener(this.model, this.parentModel, this.selector);
        this.selectorModelListener = createReferenceSelectorModelListener((AbstractSimpleReferenceSelectorModel)this.model, this.selector);
        this.selector.addReferenceSelectorListener(this.selectorListener);
        this.model.addSelectorModelListener((SimpleSelectorModelListener)this.selectorModelListener);
    }


    protected SimpleReferenceSelectorListener createReferenceSelectorListener(DefaultSimpleReferenceSelectorModel model, CollectionEditorModel parentModel, UISimpleReferenceSelector selector)
    {
        return (SimpleReferenceSelectorListener)new Object(this, model, this.additionalReferenceEditorListener, parentModel, selector);
    }


    protected SimpleReferenceSelectorModelListener createReferenceSelectorModelListener(AbstractSimpleReferenceSelectorModel model, UISimpleReferenceSelector selector)
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
