package de.hybris.platform.cockpit.components.duallistbox.impl;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.comparators.UserNameComparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

public class CockpitUsersDualListboxEditor extends DefaultSimpleDualListboxEditor<TypedObject>
{
    public CockpitUsersDualListboxEditor(List<TypedObject> assignedValuesList, List<TypedObject> availableValues)
    {
        super(assignedValuesList, availableValues);
    }


    protected void parseParams(Map parameters)
    {
    }


    protected List<TypedObject> search(String searchTerm)
    {
        List<TypedObject> filteredCollection = new ArrayList<>();
        for(TypedObject desc : getAvailableValues())
        {
            String label = UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabelForTypedObject(desc);
            if(StringUtils.containsIgnoreCase(label, searchTerm))
            {
                filteredCollection.add(desc);
            }
        }
        return filteredCollection;
    }


    protected void doSearchWithSorting(String searchTerm)
    {
        List<TypedObject> result = new ArrayList<>(search(searchTerm));
        List<TypedObject> filteredResult = removeDuplicatedColumns(result, getAssignedValuesList());
        Collections.sort(filteredResult, (Comparator<? super TypedObject>)new UserNameComparator());
        setResultListData(this.collectionAllItems, filteredResult);
    }
}
