package de.hybris.platform.cockpit.model.referenceeditor.simple;

import de.hybris.platform.cockpit.model.advancedsearch.AdvancedSearchModel;
import de.hybris.platform.cockpit.model.advancedsearch.AdvancedSearchParameterContainer;
import de.hybris.platform.cockpit.model.advancedsearch.impl.DefaultAdvancedSearchModel;
import de.hybris.platform.cockpit.model.listview.MutableTableModel;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import java.util.List;

public interface SimpleReferenceSelectorModel extends SimpleSelectorModel
{
    List<? extends Object> getAutoCompleteResult();


    List<? extends Object> getSearchResult();


    int getMaxAutoCompleteResultSize();


    int getMinAutoCompleteTextLength();


    int getPageSize();


    void cancel();


    int getTotalSize();


    ObjectType getRootType();


    ObjectType getRootSearchType();


    MutableTableModel getTableModel();


    MutableTableModel createDefaultTableModel();


    DefaultAdvancedSearchModel createAdvancedTableModel();


    AdvancedSearchModel getAdvancedSearchModel();


    void doSearch(ObjectTemplate paramObjectTemplate, AdvancedSearchParameterContainer paramAdvancedSearchParameterContainer, int paramInt);
}
