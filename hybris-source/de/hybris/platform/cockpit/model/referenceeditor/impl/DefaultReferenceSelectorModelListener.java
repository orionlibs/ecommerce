package de.hybris.platform.cockpit.model.referenceeditor.impl;

import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.referenceeditor.AbstractReferenceSelectorModel;
import de.hybris.platform.cockpit.model.referenceeditor.ReferenceSelectorModelListener;
import de.hybris.platform.cockpit.model.referenceeditor.UIReferenceSelector;

public class DefaultReferenceSelectorModelListener implements ReferenceSelectorModelListener
{
    private final UIReferenceSelector selector;
    private final EditorListener editorListener;
    private final AbstractReferenceSelectorModel model;


    public DefaultReferenceSelectorModelListener(AbstractReferenceSelectorModel model, UIReferenceSelector selector, EditorListener editorListener)
    {
        if(model == null || selector == null)
        {
            throw new IllegalArgumentException("Model, view component and editor listener must be specified.");
        }
        this.selector = selector;
        this.editorListener = editorListener;
        this.model = model;
    }


    public void itemsChanged()
    {
        this.selector.updateItems();
        if(this.editorListener != null)
        {
            this.editorListener.valueChanged(this.model.getValue());
            this.editorListener.actionPerformed("force_save_clicked");
        }
    }


    public void itemsNotConfirmedChanged()
    {
        this.selector.updateItemsNotConfirmed();
    }


    public void modeChanged()
    {
        this.selector.updateMode();
    }


    public void selectionModeChanged()
    {
    }


    public void changed()
    {
        this.selector.update();
    }


    public void autoCompleteResultChanged()
    {
        this.selector.updateAutoCompleteResult();
        this.selector.showAutoCompletePopup();
    }


    public void searchResultChanged()
    {
        this.selector.updateSearchResult();
    }


    public void temporaryResultChanged()
    {
        this.selector.updateTemporaryItems();
    }


    public void rootSearchTypeChanged()
    {
        this.selector.updateRootSearchTypeChanged();
    }


    public void rootTypeChanged()
    {
        this.selector.updateRootTypeChanged();
    }


    public void canceled()
    {
        if(this.editorListener != null)
        {
            this.editorListener.actionPerformed("escape_pressed");
        }
    }


    public void itemActivated()
    {
        if(this.selector instanceof ReferenceSelector)
        {
            ((ReferenceSelector)this.selector).doItemActivated();
        }
    }
}
