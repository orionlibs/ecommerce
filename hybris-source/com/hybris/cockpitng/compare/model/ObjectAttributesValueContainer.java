/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.compare.model;

import java.util.HashMap;
import java.util.Map;

public class ObjectAttributesValueContainer
{
    /**
     * Object identifier for which attributes are stored
     */
    private final Object objectId;
    /**
     * Object attributes Key = CompareAttributeDescriptor, Object = Attribute value
     */
    private final Map<CompareAttributeDescriptor, Object> attributeValues;


    public ObjectAttributesValueContainer(final Object objectId)
    {
        this.objectId = objectId;
        this.attributeValues = new HashMap<>();
    }


    public Object getObjectId()
    {
        return objectId;
    }


    public Map<CompareAttributeDescriptor, Object> getAttributeValues()
    {
        return attributeValues;
    }
}
