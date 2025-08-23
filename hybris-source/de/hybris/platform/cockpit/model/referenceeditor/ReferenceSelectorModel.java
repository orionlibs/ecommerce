package de.hybris.platform.cockpit.model.referenceeditor;

import de.hybris.platform.cockpit.model.advancedsearch.AdvancedSearchModel;
import de.hybris.platform.cockpit.model.advancedsearch.AdvancedSearchParameterContainer;
import de.hybris.platform.cockpit.model.advancedsearch.impl.DefaultAdvancedSearchModel;
import de.hybris.platform.cockpit.model.listview.MutableTableModel;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import java.util.Collection;
import java.util.List;

public interface ReferenceSelectorModel extends SelectorModel
{
    List<Object> getActualSelectedTempItems();


    List<Object> getActualSelectedItems();


    List<Object> getNotConfirmedItems();


    List<? extends Object> getAutoCompleteResult();


    List<? extends Object> getSearchResult();


    List<Object> getTemporaryItems();


    int getMaxAutoCompleteResultSize();


    int getMinAutoCompleteTextLength();


    int getPageSize();


    void reset();


    void cancel();


    int getTotalSize();


    ObjectType getRootType();


    ObjectType getRootSearchType();


    MutableTableModel getTableModel();


    MutableTableModel createDefaultTableModel();


    DefaultAdvancedSearchModel createAdvancedTableModel();


    AdvancedSearchModel getAdvancedSearchModel();


    void doSearch(ObjectTemplate paramObjectTemplate, AdvancedSearchParameterContainer paramAdvancedSearchParameterContainer, int paramInt);


    void setActiveItems(Collection<TypedObject> paramCollection);
}
