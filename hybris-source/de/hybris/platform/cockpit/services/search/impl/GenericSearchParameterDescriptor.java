package de.hybris.platform.cockpit.services.search.impl;

import de.hybris.platform.cockpit.model.search.Operator;
import de.hybris.platform.cockpit.model.search.SearchParameterDescriptor;
import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.model.type.ComposedTypeModel;

public interface GenericSearchParameterDescriptor extends SearchParameterDescriptor
{
    boolean isSimpleSearchProperty();


    ComposedTypeModel getRequiredComposedType();


    GenericCondition createCondition(GenericQuery paramGenericQuery, Object paramObject, Operator paramOperator);


    String getAttributeQualifier();


    Operator getDefaultOperator();
}
