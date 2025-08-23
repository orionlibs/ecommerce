/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.facades.compare;

import com.hybris.cockpitng.compare.impl.DefaultItemComparisonFacade;
import com.hybris.cockpitng.compare.model.CompareAttributeDescriptor;
import com.hybris.cockpitng.compare.model.ObjectAttributesValueContainer;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Required;

/**
 * Comparison facade that takes permissions into consideration - all values that current user has no rights to are
 * considered equal to avoid providing any information that user is not allowed.
 */
public class PermissionsAwareItemComparisonFacade extends DefaultItemComparisonFacade
{
    private PermissionFacade permissionFacade;


    @Override
    protected void compareReferenceValueWithCompareObject(final Object referenceAttrValue,
                    final ObjectAttributesValueContainer compareObjectContainer, final CompareAttributeDescriptor compareAttrDescriptor,
                    final Map<ObjectAttributesValueContainer, Set<CompareAttributeDescriptor>> differences)
    {
        if(getPermissionFacade().canReadInstanceProperty(getObjectById(compareObjectContainer.getObjectId()).orElse(null),
                        compareAttrDescriptor.getQualifier()))
        {
            super.compareReferenceValueWithCompareObject(referenceAttrValue, compareObjectContainer, compareAttrDescriptor,
                            differences);
        }
    }


    @Override
    protected void updateObjectAttributeValue(final ObjectAttributesValueContainer valueContainer, final Object object,
                    final CompareAttributeDescriptor attributeDescriptor)
    {
        if(getPermissionFacade().canReadInstanceProperty(object, attributeDescriptor.getQualifier()))
        {
            super.updateObjectAttributeValue(valueContainer, object, attributeDescriptor);
        }
    }


    protected PermissionFacade getPermissionFacade()
    {
        return permissionFacade;
    }


    @Required
    public void setPermissionFacade(final PermissionFacade permissionFacade)
    {
        this.permissionFacade = permissionFacade;
    }
}
