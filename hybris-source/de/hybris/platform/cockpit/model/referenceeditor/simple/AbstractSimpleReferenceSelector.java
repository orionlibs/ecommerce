package de.hybris.platform.cockpit.model.referenceeditor.simple;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.impl.CreateContext;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zul.Div;

public abstract class AbstractSimpleReferenceSelector extends Div implements UISimpleReferenceSelector
{
    private final List<SimpleReferenceSelectorListener> listeners = new ArrayList<>();


    public void fireCancel()
    {
        for(SimpleReferenceSelectorListener listener : this.listeners)
        {
            listener.cancel();
        }
    }


    public void fireAbortAndCloseAdvancedMode()
    {
        for(SimpleReferenceSelectorListener listener : this.listeners)
        {
            listener.abortAndCloseAdvancedMode();
        }
    }


    protected void fireAutoCompleteSearch(String searchTerm)
    {
        for(SimpleReferenceSelectorListener listener : this.listeners)
        {
            listener.triggerAutoCompleteSearch(searchTerm);
        }
    }


    protected void fireSelectorAdvancedMode()
    {
        for(SimpleReferenceSelectorListener listener : this.listeners)
        {
            listener.selectorAdvancedMode();
        }
    }


    protected void fireSelectorNormalMode()
    {
        for(SimpleReferenceSelectorListener listener : this.listeners)
        {
            listener.selectorNormaMode();
        }
    }


    public void fireOpenReferencedItem(TypedObject item)
    {
        for(SimpleReferenceSelectorListener listener : this.listeners)
        {
            listener.doOpenReferencedItem(item);
        }
    }


    protected void fireShowItemEditorPopup(CreateContext context)
    {
        for(SimpleReferenceSelectorListener listener : this.listeners)
        {
            listener.showAddItemPopupEditor(context);
        }
    }


    protected void fireSaveActualItem(Object currentValue)
    {
        for(SimpleReferenceSelectorListener listener : this.listeners)
        {
            listener.saveActualItem(currentValue);
        }
    }


    public void addReferenceSelectorListener(SimpleReferenceSelectorListener referenceSelectorListener)
    {
        if(!this.listeners.contains(referenceSelectorListener))
        {
            this.listeners.add(referenceSelectorListener);
        }
    }


    public void removeReferenceSelectorListener(SimpleReferenceSelectorListener referenceSelectorListener)
    {
        if(this.listeners.contains(referenceSelectorListener))
        {
            this.listeners.remove(referenceSelectorListener);
        }
    }
}
