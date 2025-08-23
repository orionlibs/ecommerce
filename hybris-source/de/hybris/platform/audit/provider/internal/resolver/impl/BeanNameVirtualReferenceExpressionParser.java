package de.hybris.platform.audit.provider.internal.resolver.impl;

import de.hybris.platform.audit.provider.internal.resolver.VirtualReferenceExpressionParser;
import de.hybris.platform.audit.provider.internal.resolver.VirtualReferenceValuesExtractor;
import de.hybris.platform.core.Registry;
import org.apache.commons.lang.StringUtils;

public class BeanNameVirtualReferenceExpressionParser implements VirtualReferenceExpressionParser
{
    private final VirtualReferenceExpressionParser next;
    private static final String REGEX = ".*=beanName(.*)";
    private static final String BEAN_BEGIN_CHAR = "(";
    private static final String BEAN_END_CHAR = ")";
    private static final String QUALIFIER_END_CHAR = "=";


    public BeanNameVirtualReferenceExpressionParser(VirtualReferenceExpressionParser next)
    {
        this.next = next;
    }


    public VirtualReferenceValuesExtractor getResolver(String expression)
    {
        if(expression.matches(".*=beanName(.*)"))
        {
            String beanName = StringUtils.substringBetween(expression, "(", ")");
            return getExtractorBean(beanName);
        }
        return this.next.getResolver(expression);
    }


    protected VirtualReferenceValuesExtractor getExtractorBean(String beanName)
    {
        return (VirtualReferenceValuesExtractor)Registry.getCoreApplicationContext().getBean(beanName, VirtualReferenceValuesExtractor.class);
    }


    public String getQualifier(String expression)
    {
        if(expression.matches(".*=beanName(.*)"))
        {
            return StringUtils.substringBefore(expression, "=");
        }
        return this.next.getQualifier(expression);
    }
}
