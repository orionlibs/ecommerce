package de.hybris.platform.cockpit.model.referenceeditor;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import java.util.Collection;

public abstract class AbstractReferenceSelectorModel extends AbstractSelectorModel implements ReferenceSelectorModel
{
    protected void fireAutoCompleteResultChanged()
    {
        for(SelectorModelListener listener : this.listeners)
        {
            ((ReferenceSelectorModelListener)listener).autoCompleteResultChanged();
        }
    }


    protected void fireSearchResultChanged()
    {
        for(SelectorModelListener listener : this.listeners)
        {
            ((ReferenceSelectorModelListener)listener).searchResultChanged();
        }
    }


    protected void fireTemporaryItemsChanged()
    {
        for(SelectorModelListener listener : this.listeners)
        {
            ((ReferenceSelectorModelListener)listener).temporaryResultChanged();
        }
    }


    protected void fireItemsNotConfirmedChanged()
    {
        for(SelectorModelListener listener : this.listeners)
        {
            ((ReferenceSelectorModelListener)listener).itemsNotConfirmedChanged();
        }
    }


    protected void fireRootTypeChanged()
    {
        for(SelectorModelListener listener : this.listeners)
        {
            ((ReferenceSelectorModelListener)listener).rootTypeChanged();
        }
    }


    protected void fireRootSearchTypeChanged()
    {
        for(SelectorModelListener listener : this.listeners)
        {
            ((ReferenceSelectorModelListener)listener).rootSearchTypeChanged();
        }
    }


    protected void fireCancel()
    {
        for(SelectorModelListener listener : this.listeners)
        {
            ((ReferenceSelectorModelListener)listener).canceled();
        }
    }


    protected void fireItemActivated()
    {
        for(SelectorModelListener listener : this.listeners)
        {
            ((ReferenceSelectorModelListener)listener).itemActivated();
        }
    }


    public void setActiveItems(Collection<TypedObject> items)
    {
        fireItemActivated();
    }
}
