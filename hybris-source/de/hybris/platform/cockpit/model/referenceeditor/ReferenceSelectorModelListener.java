package de.hybris.platform.cockpit.model.referenceeditor;

public interface ReferenceSelectorModelListener extends SelectorModelListener
{
    void autoCompleteResultChanged();


    void searchResultChanged();


    void temporaryResultChanged();


    void itemsNotConfirmedChanged();


    void rootTypeChanged();


    void rootSearchTypeChanged();


    void itemActivated();
}
