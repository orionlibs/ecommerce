package de.hybris.platform.servicelayer.internal.converter.impl;

import de.hybris.platform.jalo.type.AttributeDescriptor;

public interface AttributePrefetchMode
{
    boolean isPrefetched(AttributeDescriptor paramAttributeDescriptor);
}
