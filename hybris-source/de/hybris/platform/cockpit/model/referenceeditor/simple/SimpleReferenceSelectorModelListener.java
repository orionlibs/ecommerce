package de.hybris.platform.cockpit.model.referenceeditor.simple;

public interface SimpleReferenceSelectorModelListener extends SimpleSelectorModelListener
{
    void autoCompleteResultChanged();


    void searchResultChanged();


    void rootTypeChanged();


    void rootSearchTypeChanged();
}
