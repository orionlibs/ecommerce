package de.hybris.platform.cockpit.components.duallistbox.impl;

import de.hybris.platform.cockpit.model.listview.ColumnDescriptor;
import de.hybris.platform.cockpit.util.comparators.ColumnNameComparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

public class ColumnDescriptorDualListboxEditor extends DefaultSimpleDualListboxEditor<ColumnDescriptor>
{
    public ColumnDescriptorDualListboxEditor(List<ColumnDescriptor> assignedValuesList, List<ColumnDescriptor> availableValues)
    {
        super(assignedValuesList, availableValues);
    }


    protected void parseParams(Map parameters)
    {
    }


    protected List<ColumnDescriptor> search(String searchTerm)
    {
        List<ColumnDescriptor> filteredCollection = new ArrayList<>();
        for(ColumnDescriptor desc : getAvailableValues())
        {
            if(StringUtils.containsIgnoreCase(desc.getName(), searchTerm))
            {
                filteredCollection.add(desc);
            }
        }
        return filteredCollection;
    }


    protected void doSearchWithSorting(String searchTerm)
    {
        List<ColumnDescriptor> result = new ArrayList<>(search(searchTerm));
        List<ColumnDescriptor> filteredResult = removeDuplicatedColumns(result, getAssignedValuesList());
        Collections.sort(filteredResult, (Comparator<? super ColumnDescriptor>)new ColumnNameComparator());
        setResultListData(this.collectionAllItems, filteredResult);
    }
}
