package de.hybris.platform.cockpit.model.referenceeditor.simple;

public abstract class AbstractSimpleReferenceSelectorModel extends AbstractSimpleSelectorModel implements SimpleReferenceSelectorModel
{
    protected void fireAutoCompleteResultChanged()
    {
        for(SimpleSelectorModelListener listener : this.listeners)
        {
            ((SimpleReferenceSelectorModelListener)listener).autoCompleteResultChanged();
        }
    }


    protected void fireSearchResultChanged()
    {
        for(SimpleSelectorModelListener listener : this.listeners)
        {
            ((SimpleReferenceSelectorModelListener)listener).searchResultChanged();
        }
    }


    protected void fireRootTypeChanged()
    {
        for(SimpleSelectorModelListener listener : this.listeners)
        {
            ((SimpleReferenceSelectorModelListener)listener).rootTypeChanged();
        }
    }


    protected void fireRootSearchTypeChanged()
    {
        for(SimpleSelectorModelListener listener : this.listeners)
        {
            ((SimpleReferenceSelectorModelListener)listener).rootSearchTypeChanged();
        }
    }


    protected void fireCancel()
    {
        for(SimpleSelectorModelListener listener : this.listeners)
        {
            ((SimpleReferenceSelectorModelListener)listener).canceled();
        }
    }
}
