package de.hybris.platform.audit.provider.internal.resolver.impl;

import de.hybris.platform.audit.provider.internal.resolver.VirtualReferenceExpressionParser;
import de.hybris.platform.audit.provider.internal.resolver.VirtualReferenceValuesExtractor;

public class DefaultVirtualReferenceExpressionParser implements VirtualReferenceExpressionParser
{
    private final VirtualReferenceValuesExtractor resolver;


    public DefaultVirtualReferenceExpressionParser(VirtualReferenceValuesExtractor resolver)
    {
        this.resolver = resolver;
    }


    public VirtualReferenceValuesExtractor getResolver(String expression)
    {
        return this.resolver;
    }


    public String getQualifier(String expression)
    {
        return expression;
    }
}
