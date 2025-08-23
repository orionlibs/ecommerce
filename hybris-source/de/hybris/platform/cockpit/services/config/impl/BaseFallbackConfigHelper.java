package de.hybris.platform.cockpit.services.config.impl;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.services.config.BaseConfiguration;
import de.hybris.platform.cockpit.services.config.DefaultPropertySettings;
import de.hybris.platform.cockpit.services.label.ObjectLabelProvider;
import de.hybris.platform.cockpit.services.label.impl.GenericLabelProvider;
import de.hybris.platform.cockpit.services.meta.TypeService;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;

public class BaseFallbackConfigHelper
{
    public static Set<PropertyDescriptor> getBaseProperties(BaseConfiguration baseConfiguration)
    {
        Set<PropertyDescriptor> ret = new LinkedHashSet<>();
        for(DefaultPropertySettings propertySetting : baseConfiguration.getBaseProperties())
        {
            ret.add(propertySetting.getPropertyDescriptor());
        }
        return ret;
    }


    public static Set<PropertyDescriptor> getLabelProperties(BaseConfiguration baseConfiguration)
    {
        Set<PropertyDescriptor> ret = new LinkedHashSet<>();
        ObjectLabelProvider objectLabelProvider = baseConfiguration.getObjectLabelProvider();
        if(objectLabelProvider instanceof GenericLabelProvider)
        {
            List<PropertyDescriptor> propertyDescriptors = ((GenericLabelProvider)objectLabelProvider).getPropertyDescriptors();
            if(CollectionUtils.isNotEmpty(propertyDescriptors))
            {
                ret.addAll(propertyDescriptors);
            }
        }
        return ret;
    }


    public static Set<PropertyDescriptor> getSearchProperties(BaseConfiguration baseConfiguration, TypeService typeService)
    {
        Set<PropertyDescriptor> ret = new LinkedHashSet<>();
        for(PropertyDescriptor propertyDescriptor : baseConfiguration.getSearchType().getDeclaredPropertyDescriptors())
        {
            ret.add(typeService.getPropertyDescriptor(propertyDescriptor.getQualifier()));
        }
        return ret;
    }


    public static Set<PropertyDescriptor> getSortProperties(BaseConfiguration baseConfiguration, TypeService typeService)
    {
        Set<PropertyDescriptor> ret = new LinkedHashSet<>();
        for(PropertyDescriptor propertyDescriptor : baseConfiguration.getSearchType().getSortProperties())
        {
            ret.add(typeService.getPropertyDescriptor(propertyDescriptor.getQualifier()));
        }
        return ret;
    }
}
