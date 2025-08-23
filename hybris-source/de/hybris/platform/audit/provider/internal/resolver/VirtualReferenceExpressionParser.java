package de.hybris.platform.audit.provider.internal.resolver;

public interface VirtualReferenceExpressionParser
{
    VirtualReferenceValuesExtractor getResolver(String paramString);


    String getQualifier(String paramString);
}
