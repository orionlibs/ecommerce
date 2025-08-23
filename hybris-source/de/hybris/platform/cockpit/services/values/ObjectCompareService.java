package de.hybris.platform.cockpit.services.values;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import java.util.List;
import java.util.Map;

public interface ObjectCompareService
{
    Map<PropertyDescriptor, PropertyComparisonInfo> getComparedAttributes(TypedObject paramTypedObject, List<TypedObject> paramList);


    Map<PropertyDescriptor, PropertyComparisonInfo> getComparedAttributes(TypedObject paramTypedObject, List<TypedObject> paramList, ObjectAttributeComparator paramObjectAttributeComparator);


    Map<PropertyDescriptor, PropertyComparisonInfo> getComparedAttributes(ObjectValueContainer paramObjectValueContainer, List<ObjectValueContainer> paramList);


    Map<PropertyDescriptor, PropertyComparisonInfo> getComparedAttributes(ObjectValueContainer paramObjectValueContainer, List<ObjectValueContainer> paramList, ObjectAttributeComparator paramObjectAttributeComparator);
}
