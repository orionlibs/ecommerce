package de.hybris.platform.cockpit.services.values;

import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import java.util.Set;

public interface ObjectValueHandler
{
    void loadValues(ObjectValueContainer paramObjectValueContainer, ObjectType paramObjectType, Object paramObject, Set<PropertyDescriptor> paramSet, Set<String> paramSet1) throws ValueHandlerException;


    void updateValues(ObjectValueContainer paramObjectValueContainer, Set<String> paramSet) throws ValueHandlerException;


    void updateValues(ObjectValueContainer paramObjectValueContainer, Set<String> paramSet, Set<PropertyDescriptor> paramSet1) throws ValueHandlerException;


    void storeValues(ObjectValueContainer paramObjectValueContainer) throws ValueHandlerException;


    void storeValues(ObjectValueContainer paramObjectValueContainer, boolean paramBoolean) throws ValueHandlerException;
}
