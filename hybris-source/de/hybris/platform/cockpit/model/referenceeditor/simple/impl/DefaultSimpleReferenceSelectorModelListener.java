package de.hybris.platform.cockpit.model.referenceeditor.simple.impl;

import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.referenceeditor.simple.AbstractSimpleReferenceSelectorModel;
import de.hybris.platform.cockpit.model.referenceeditor.simple.SimpleReferenceSelectorModelListener;
import de.hybris.platform.cockpit.model.referenceeditor.simple.UISimpleReferenceSelector;

public class DefaultSimpleReferenceSelectorModelListener implements SimpleReferenceSelectorModelListener
{
    private final UISimpleReferenceSelector selector;
    private final EditorListener editorListener;
    private final AbstractSimpleReferenceSelectorModel model;


    public DefaultSimpleReferenceSelectorModelListener(AbstractSimpleReferenceSelectorModel model, UISimpleReferenceSelector selector, EditorListener editorListener)
    {
        if(model == null || selector == null)
        {
            throw new IllegalArgumentException("Model, view component and editor listener must be specified.");
        }
        this.selector = selector;
        this.editorListener = editorListener;
        this.model = model;
    }


    public void itemChanged()
    {
        if(this.editorListener != null)
        {
            this.editorListener.valueChanged(this.model.getValue());
            this.editorListener.actionPerformed("force_save_clicked");
        }
        this.selector.updateItems();
    }


    public void modeChanged()
    {
        this.selector.updateMode();
    }


    public void autoCompleteResultChanged()
    {
        this.selector.updateAutoCompleteResult();
        this.selector.showAutoCompletePopup();
    }


    public void rootSearchTypeChanged()
    {
        this.selector.updateRootSearchTypeChanged();
    }


    public void rootTypeChanged()
    {
        this.selector.updateRootTypeChanged();
    }


    public void searchResultChanged()
    {
        this.selector.updateSearchResult();
    }


    public void canceled()
    {
        if(this.editorListener != null)
        {
            this.editorListener.actionPerformed("escape_pressed");
        }
    }
}
