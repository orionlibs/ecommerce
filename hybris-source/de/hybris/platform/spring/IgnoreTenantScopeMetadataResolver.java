package de.hybris.platform.spring;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationScopeMetadataResolver;
import org.springframework.context.annotation.ScopeMetadata;

public class IgnoreTenantScopeMetadataResolver extends AnnotationScopeMetadataResolver
{
    private static final Logger LOG = Logger.getLogger(IgnoreTenantScopeMetadataResolver.class.getName());


    public ScopeMetadata resolveScopeMetadata(BeanDefinition definition)
    {
        ScopeMetadata parentMetaData = super.resolveScopeMetadata(definition);
        if("tenant".equalsIgnoreCase(parentMetaData.getScopeName()))
        {
            parentMetaData.setScopeName("");
            if(LOG.isInfoEnabled())
            {
                LOG.info("!!! Scope tenant removed from bean definition [" + definition.getBeanClassName() + "], please remove it manually to avoid any issues in future. !!!");
            }
        }
        return parentMetaData;
    }
}
