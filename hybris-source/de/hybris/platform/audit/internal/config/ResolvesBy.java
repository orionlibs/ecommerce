package de.hybris.platform.audit.internal.config;

import de.hybris.platform.audit.provider.internal.resolver.ReferencesResolver;
import de.hybris.platform.core.Registry;
import javax.xml.bind.annotation.XmlAttribute;
import org.apache.commons.lang.builder.ToStringBuilder;

public class ResolvesBy
{
    @XmlAttribute
    private String resolverBeanId;
    @XmlAttribute
    private String expression;


    private ResolvesBy()
    {
    }


    private ResolvesBy(Builder builder)
    {
        this.resolverBeanId = builder.resolverBeanId;
        this.expression = builder.expression;
    }


    public static Builder builder()
    {
        return new Builder();
    }


    public static Builder basedOn(ResolvesBy resolvesBy)
    {
        if(resolvesBy == null)
        {
            return new Builder();
        }
        return new Builder(resolvesBy.resolverBeanId, resolvesBy.expression);
    }


    public String getExpression()
    {
        return this.expression;
    }


    public ReferencesResolver getResolverBean()
    {
        return (ReferencesResolver)Registry.getApplicationContext().getBean(this.resolverBeanId, ReferencesResolver.class);
    }


    public String toString()
    {
        return (new ToStringBuilder(this)).append("resolverBeanId", this.resolverBeanId).append("expression", this.expression).toString();
    }
}
