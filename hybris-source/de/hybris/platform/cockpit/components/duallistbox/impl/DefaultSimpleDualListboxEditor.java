package de.hybris.platform.cockpit.components.duallistbox.impl;

import de.hybris.platform.cockpit.components.duallistbox.AbstractDualListboxEditor;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Label;

public abstract class DefaultSimpleDualListboxEditor<T> extends AbstractDualListboxEditor
{
    private static final String SEARCH_COLUMN_INFO_BOX = "Search for Columns";
    private final FilterColumnNameResolver filterColumnNameResolver = new FilterColumnNameResolver();
    private List<T> availableValues = null;


    public DefaultSimpleDualListboxEditor(List<T> assignedValuesList, List<T> availableValues)
    {
        super(assignedValuesList);
        this.availableValues = availableValues;
    }


    public EventListener getOnUserSearchListener()
    {
        return (EventListener)new Object(this);
    }


    protected void updateCollectionAllItems()
    {
        String text = this.inputComponentDiv.getSearchInputComponent().getText();
        if(StringUtils.isEmpty(text) || StringUtils.equals("Search for Columns", text))
        {
            doSearchWithSorting("");
        }
        else
        {
            doSearchWithSorting(text);
        }
    }


    protected void doSearchWithSorting(String searchTerm)
    {
        List<T> result = new ArrayList<>(search(searchTerm));
        List<T> filteredResult = removeDuplicatedColumns(result, getAssignedValuesList());
        setResultListData(this.collectionAllItems, filteredResult);
    }


    protected List<T> getAvailableValues()
    {
        return this.availableValues;
    }


    protected String getSearchInfoBox()
    {
        return "Search for Columns";
    }


    public void setAvailableValues(List<T> availableValues)
    {
        this.availableValues = availableValues;
    }


    protected List<T> search(String searchTerm)
    {
        List<T> filteredCollection = new ArrayList<>();
        for(T desc : this.availableValues)
        {
            if(StringUtils.containsIgnoreCase(desc.toString(), searchTerm))
            {
                filteredCollection.add(desc);
            }
        }
        return filteredCollection;
    }


    public List<T> removeDuplicatedColumns(List<T> searchResult, List<T> objectsToRemove)
    {
        List<T> noDuplicateSearchResult = new ArrayList<>();
        noDuplicateSearchResult.addAll(searchResult);
        noDuplicateSearchResult.removeAll(objectsToRemove);
        return noDuplicateSearchResult;
    }


    protected Label getAvailableValuesLabel()
    {
        return new Label(Labels.getLabel("duallistbox.columns.available"));
    }


    protected Label getAssignedValuesLabel()
    {
        return new Label(Labels.getLabel("duallistbox.columns.assigned"));
    }


    protected FilterColumnNameResolver getFilterStringResolver()
    {
        return this.filterColumnNameResolver;
    }
}
