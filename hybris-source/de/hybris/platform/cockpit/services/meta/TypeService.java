package de.hybris.platform.cockpit.services.meta;

import de.hybris.platform.cockpit.model.meta.BaseType;
import de.hybris.platform.cockpit.model.meta.ExtendedType;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.core.model.ItemModel;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface TypeService
{
    BaseType getBaseType(String paramString);


    ExtendedType getExtendedType(String paramString);


    ObjectTemplate getObjectTemplate(String paramString);


    Set<ObjectType> getAllSubtypes(ObjectType paramObjectType);


    List<ObjectType> getAllSupertypes(ObjectType paramObjectType);


    List<ObjectTemplate> getObjectTemplates(BaseType paramBaseType);


    PropertyDescriptor getPropertyDescriptor(ObjectType paramObjectType, String paramString);


    ObjectType getObjectType(String paramString);


    String getTypeCodeFromPropertyQualifier(String paramString);


    String getAttributeCodeFromPropertyQualifier(String paramString);


    ObjectType getObjectTypeFromPropertyQualifier(String paramString);


    PropertyDescriptor getPropertyDescriptor(String paramString);


    String getValueTypeCode(PropertyDescriptor paramPropertyDescriptor);


    List<Object> getAvailableValues(PropertyDescriptor paramPropertyDescriptor);


    List<Object> getAvailableValues(PropertyDescriptor paramPropertyDescriptor, TypedObject paramTypedObject);


    TypedObject wrapItem(Object paramObject);


    List<TypedObject> wrapItems(Collection<? extends Object> paramCollection);


    List<ItemModel> unwrapItems(Collection<TypedObject> paramCollection);


    ObjectTemplate getBestTemplate(TypedObject paramTypedObject);


    boolean checkItemAlive(TypedObject paramTypedObject);


    Collection<TypedObject> getAllInstancesOf(ObjectType paramObjectType);


    Collection<TypedObject> getAllInstancesOf(ObjectType paramObjectType, String paramString1, String paramString2);


    int countAllInstancesOf(ObjectType paramObjectType);


    PropertyDescriptor getSelectionOf(PropertyDescriptor paramPropertyDescriptor);


    Collection<PropertyDescriptor> getReverseSelectionOf(PropertyDescriptor paramPropertyDescriptor);
}
