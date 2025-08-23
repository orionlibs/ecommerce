package de.hybris.platform.cockpit.model.referenceeditor.simple;

import de.hybris.platform.cockpit.model.general.UIViewComponent;

public interface UISimpleReferenceSelector extends UIViewComponent
{
    void setModel(SimpleReferenceSelectorModel paramSimpleReferenceSelectorModel);


    SimpleReferenceSelectorModel getModel();


    void updateItems();


    void updateSearchResult();


    void updateAutoCompleteResult();


    void updateMode();


    void showAutoCompletePopup();


    void updateRootTypeChanged();


    void updateRootSearchTypeChanged();


    void addReferenceSelectorListener(SimpleReferenceSelectorListener paramSimpleReferenceSelectorListener);


    void removeReferenceSelectorListener(SimpleReferenceSelectorListener paramSimpleReferenceSelectorListener);
}
