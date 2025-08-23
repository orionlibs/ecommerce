package de.hybris.platform.cockpit.model.referenceeditor;

import de.hybris.platform.cockpit.model.advancedsearch.UIAdvancedSearchView;
import de.hybris.platform.cockpit.model.general.UIViewComponent;

public interface UIReferenceSelector extends UIViewComponent
{
    void setModel(ReferenceSelectorModel paramReferenceSelectorModel);


    ReferenceSelectorModel getModel();


    void setAdvancedSearchComponent(UIAdvancedSearchView paramUIAdvancedSearchView);


    void updateItems();


    void updateSearchResult();


    void updateAutoCompleteResult();


    void updateItemsNotConfirmed();


    void updateTemporaryItems();


    void updateMode();


    void updateRootTypeChanged();


    void updateRootSearchTypeChanged();


    void showComponentPopup();


    void showAutoCompletePopup();


    void setFocus();


    void addReferenceSelectorListener(ReferenceSelectorListener paramReferenceSelectorListener);


    void removeReferenceSelectorListener(ReferenceSelectorListener paramReferenceSelectorListener);
}
