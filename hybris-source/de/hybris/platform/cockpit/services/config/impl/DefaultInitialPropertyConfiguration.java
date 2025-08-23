package de.hybris.platform.cockpit.services.config.impl;

import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.InitialPropertyConfiguration;
import de.hybris.platform.cockpit.services.config.PropertyMappingConfiguration;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultInitialPropertyConfiguration implements InitialPropertyConfiguration
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultInitialPropertyConfiguration.class);
    private final ObjectTemplate objectTemplate;
    private final Collection<PropertyMappingConfiguration> propertyMappingConfigurations;
    private final Map<String, Object> initialProperties = new HashMap<>();


    public DefaultInitialPropertyConfiguration(ObjectTemplate sourceObjectTemplate, Collection<PropertyMappingConfiguration> mappings)
    {
        this.objectTemplate = sourceObjectTemplate;
        this.propertyMappingConfigurations = mappings;
    }


    public ObjectTemplate getSourceObjectTemplate()
    {
        return this.objectTemplate;
    }


    public Collection<PropertyMappingConfiguration> getPropertyMappingConfigurations()
    {
        return Collections.unmodifiableCollection(this.propertyMappingConfigurations);
    }


    public Map<String, Object> getInitialProperties(TypedObject typedObject, TypeService typeService)
    {
        for(PropertyMappingConfiguration pmc : getPropertyMappingConfigurations())
        {
            Object value = null;
            String source = pmc.getSource();
            if("src".equals(source))
            {
                value = typedObject;
            }
            else
            {
                value = TypeTools.getObjectAttributeValue(typedObject, source.substring("src.".length()), typeService);
            }
            if(value != null)
            {
                PropertyDescriptor targetProp = UISessionUtils.getCurrentSession().getTypeService().getPropertyDescriptor(pmc.getTarget());
                if(targetProp != null)
                {
                    if(PropertyDescriptor.Multiplicity.LIST.equals(targetProp.getMultiplicity()) || PropertyDescriptor.Multiplicity.SET
                                    .equals(targetProp.getMultiplicity()))
                    {
                        if(!(value instanceof Collection))
                        {
                            if(PropertyDescriptor.Multiplicity.SET.equals(targetProp.getMultiplicity()))
                            {
                                value = Collections.singleton(value);
                            }
                            else
                            {
                                value = Collections.singletonList(value);
                            }
                        }
                    }
                    else if(PropertyDescriptor.Multiplicity.SINGLE.equals(targetProp.getMultiplicity()))
                    {
                        if(value instanceof Collection)
                        {
                            if(!((Collection)value).isEmpty())
                            {
                                value = ((Collection)value).iterator().next();
                            }
                        }
                    }
                    else
                    {
                        LOG.warn("Multiplicity " + targetProp.getMultiplicity() + " not supported in initial property configuration.");
                    }
                }
                this.initialProperties.put(pmc.getTarget(), value);
            }
        }
        return this.initialProperties;
    }
}
