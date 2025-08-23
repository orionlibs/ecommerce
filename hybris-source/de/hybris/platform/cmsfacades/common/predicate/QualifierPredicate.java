/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.common.predicate;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import java.util.function.Predicate;
import org.springframework.beans.factory.annotation.Required;

/**
 * Predicate that returns true if the provided {@link AttributeDescriptorModel} qualifier match the given qualifier.
 */
public class QualifierPredicate implements Predicate<AttributeDescriptorModel>
{
    private String qualifier;


    @Override
    public boolean test(final AttributeDescriptorModel attributeDescriptor)
    {
        return attributeDescriptor.getQualifier().equals(this.getQualifier());
    }


    protected String getQualifier()
    {
        return qualifier;
    }


    @Required
    public void setQualifier(String qualifier)
    {
        this.qualifier = qualifier;
    }
}