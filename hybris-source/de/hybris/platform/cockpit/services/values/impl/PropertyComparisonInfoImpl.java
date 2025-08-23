package de.hybris.platform.cockpit.services.values.impl;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.services.values.PropertyComparisonInfo;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PropertyComparisonInfoImpl implements PropertyComparisonInfo
{
    private final PropertyDescriptor descriptor;
    private boolean diffFlag = false;
    private final Map<String, DiffValueEntry> diffEntries = new HashMap<>();


    public PropertyComparisonInfoImpl(PropertyDescriptor descriptor)
    {
        this.descriptor = descriptor;
    }


    public ObjectValueContainer.ObjectValueHolder getCompareValueHolder(TypedObject item, String langIso)
    {
        DiffValueEntry diffValueEntry = this.diffEntries.get(langIso);
        return (diffValueEntry == null) ? null : (ObjectValueContainer.ObjectValueHolder)diffValueEntry.compareHolderMap.get(item);
    }


    public PropertyDescriptor getPropertyDescriptor()
    {
        return this.descriptor;
    }


    public ObjectValueContainer.ObjectValueHolder getReferenceValueHolder(String langIso)
    {
        DiffValueEntry diffValueEntry = this.diffEntries.get(langIso);
        return (diffValueEntry == null) ? null : diffValueEntry.refValueHolder;
    }


    public boolean hasDifferences()
    {
        return this.diffFlag;
    }


    public void addValueHolderEntry(String langIso, ObjectValueContainer.ObjectValueHolder referenceValueHolder, TypedObject compareObject, ObjectValueContainer.ObjectValueHolder compareValueHolder, boolean different)
    {
        DiffValueEntry diffValueEntry = this.diffEntries.get(langIso);
        if(diffValueEntry == null)
        {
            diffValueEntry = new DiffValueEntry(this, referenceValueHolder);
            this.diffEntries.put(langIso, diffValueEntry);
        }
        diffValueEntry.compareHolderMap.put(compareObject, compareValueHolder);
        if(different)
        {
            this.diffFlag = true;
            diffValueEntry.diffObjectSet.add(compareObject);
        }
    }


    public Collection<TypedObject> getItemsWithDifferences(String langIso)
    {
        DiffValueEntry diffValueEntry = this.diffEntries.get(langIso);
        return (diffValueEntry == null) ? Collections.EMPTY_SET : diffValueEntry.diffObjectSet;
    }
}
