package de.hybris.platform.cockpit.services.values;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import java.util.Collection;

public interface PropertyComparisonInfo
{
    PropertyDescriptor getPropertyDescriptor();


    boolean hasDifferences();


    Collection<TypedObject> getItemsWithDifferences(String paramString);


    ObjectValueContainer.ObjectValueHolder getReferenceValueHolder(String paramString);


    ObjectValueContainer.ObjectValueHolder getCompareValueHolder(TypedObject paramTypedObject, String paramString);
}
