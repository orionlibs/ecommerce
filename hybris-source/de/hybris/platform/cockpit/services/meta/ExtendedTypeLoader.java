package de.hybris.platform.cockpit.services.meta;

import de.hybris.platform.cockpit.model.meta.ExtendedType;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.meta.impl.ItemType;
import de.hybris.platform.core.model.type.TypeModel;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface ExtendedTypeLoader
{
    ExtendedType loadType(String paramString, TypeService paramTypeService);


    TypeModel getValueType(ObjectType paramObjectType, PropertyDescriptor paramPropertyDescriptor, TypeService paramTypeService);


    List<Object> getAvailableValues(TypeModel paramTypeModel, PropertyDescriptor paramPropertyDescriptor, TypeService paramTypeService);


    Set<String> getExtendedTypeCodes(TypedObject paramTypedObject);


    String getAttributeCodeFromPropertyQualifier(String paramString);


    String getTypeCodeFromPropertyQualifier(String paramString);


    Collection<ExtendedType> getExtendedTypesForTemplate(ItemType paramItemType, String paramString, TypeService paramTypeService);
}
