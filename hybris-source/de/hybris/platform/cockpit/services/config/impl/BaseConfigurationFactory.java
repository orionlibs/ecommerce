package de.hybris.platform.cockpit.services.config.impl;

import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.impl.ItemType;
import de.hybris.platform.cockpit.model.search.SearchType;
import de.hybris.platform.cockpit.model.search.impl.ItemSearchType;
import de.hybris.platform.cockpit.services.config.BaseConfiguration;
import de.hybris.platform.cockpit.services.config.DefaultPropertySettings;
import de.hybris.platform.cockpit.services.config.InitialPropertyConfiguration;
import de.hybris.platform.cockpit.services.config.PropertyMappingConfiguration;
import de.hybris.platform.cockpit.services.config.UIComponentConfiguration;
import de.hybris.platform.cockpit.services.config.jaxb.base.Base;
import de.hybris.platform.cockpit.services.config.jaxb.base.DefaultProperty;
import de.hybris.platform.cockpit.services.config.jaxb.base.DefaultPropertyList;
import de.hybris.platform.cockpit.services.config.jaxb.base.InitialProperties;
import de.hybris.platform.cockpit.services.config.jaxb.base.Label;
import de.hybris.platform.cockpit.services.config.jaxb.base.Parameter;
import de.hybris.platform.cockpit.services.config.jaxb.base.Property;
import de.hybris.platform.cockpit.services.config.jaxb.base.PropertyList;
import de.hybris.platform.cockpit.services.config.jaxb.base.PropertyMapping;
import de.hybris.platform.cockpit.services.config.jaxb.base.Search;
import de.hybris.platform.cockpit.services.label.ObjectLabelProvider;
import de.hybris.platform.cockpit.services.label.impl.GenericLabelProvider;
import de.hybris.platform.cockpit.services.search.impl.ItemAttributeSearchDescriptor;
import de.hybris.platform.core.Constants;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;

public class BaseConfigurationFactory extends JAXBBasedUIComponentConfigurationFactory<BaseConfiguration, Base>
{
    private List<String> labelFallbackPropertyQualifiers = null;
    private static final Logger LOG = LoggerFactory.getLogger(BaseConfigurationFactory.class);


    public Class getComponentClass()
    {
        return BaseConfiguration.class;
    }


    public UIComponentConfiguration createDefault(ObjectTemplate objectTemplate)
    {
        ItemSearchType searchType = createDefaultSearchType(objectTemplate);
        ObjectLabelProvider objectLabelProvider = createDefaultObjectLabelProvider(objectTemplate);
        DefaultBaseConfiguration baseConfig = new DefaultBaseConfiguration();
        baseConfig.setSearchType((SearchType)searchType);
        baseConfig.setObjectLabelProvider(objectLabelProvider);
        return (UIComponentConfiguration)baseConfig;
    }


    protected BaseConfiguration createUIComponent(ObjectTemplate objectTemplate, ObjectTemplate originalObjectTemplate, Base xmlBase)
    {
        ItemSearchType searchType = createSearchType(originalObjectTemplate, xmlBase.getSearch());
        ObjectLabelProvider objectLabelProvider = createObjectLabelProvider(objectTemplate, xmlBase.getLabel());
        List<InitialPropertyConfiguration> initialPropertyConfigurations = createInitialPropertyConfiguration(xmlBase
                        .getInitialProperties());
        DefaultBaseConfiguration baseConfig = new DefaultBaseConfiguration();
        baseConfig.setSearchType((SearchType)searchType);
        baseConfig.setObjectLabelProvider(objectLabelProvider);
        baseConfig.setInitialPropertyConfigurations(initialPropertyConfigurations);
        DefaultPropertyList dpl = xmlBase.getDefaultPropertySettings();
        if(dpl != null && dpl.getProperty() != null && !dpl.getProperty().isEmpty())
        {
            List<DefaultPropertySettings> declaredPropertySettings = new ArrayList<>();
            for(DefaultProperty dp : dpl.getProperty())
            {
                String defaultEditorCode = dp.getDefaultEditor();
                Map<String, String> parameters = extractParameters(dp.getParameter());
                PropertyDescriptor propertyDescriptor = this.typeService.getPropertyDescriptor(dp.getQualifier());
                boolean baseProperty = dp.isBaseProperty();
                declaredPropertySettings.add(new DefaultPropertySettingsImpl(defaultEditorCode, parameters, propertyDescriptor, baseProperty));
            }
            baseConfig.setDeclaredPropertySettings(declaredPropertySettings);
        }
        return (BaseConfiguration)baseConfig;
    }


    private Map<String, String> extractParameters(List<Parameter> jaxbParameters)
    {
        Map<String, String> result = new HashMap<>();
        if(jaxbParameters != null)
        {
            for(Parameter p : jaxbParameters)
            {
                result.put(p.getName(), p.getValue());
            }
        }
        return result;
    }


    private ItemSearchType createSearchType(ObjectTemplate objectTemplate, Search xmlSearch)
    {
        ItemSearchType searchType = null;
        if(xmlSearch == null)
        {
            searchType = createDefaultSearchType(objectTemplate);
        }
        else
        {
            searchType = new ItemSearchType(((ItemType)objectTemplate.getBaseType()).getComposedType());
            PropertyList searchProperties = xmlSearch.getSearchProperties();
            if(searchProperties != null && searchProperties.getProperty() != null)
            {
                List<Property> xmlSearchProperties = searchProperties.getProperty();
                Set<PropertyDescriptor> propertyDescriptors = new HashSet<>(xmlSearchProperties.size());
                for(Property property : xmlSearchProperties)
                {
                    PropertyDescriptor propertyDescriptor = createSearchPropertyDescriptor(objectTemplate, property.getQualifier());
                    if(propertyDescriptor != null)
                    {
                        propertyDescriptors.add(propertyDescriptor);
                    }
                }
                searchType.setDeclaredPropertyDescriptors(propertyDescriptors);
            }
            PropertyList xmlSortPropertyList = xmlSearch.getSortProperties();
            if(xmlSortPropertyList != null && xmlSortPropertyList.getProperty() != null)
            {
                List<Property> xmlSortProperties = xmlSortPropertyList.getProperty();
                List<PropertyDescriptor> sortDescriptors = new ArrayList<>(xmlSortProperties.size());
                for(Property property : xmlSortProperties)
                {
                    String qualifier = property.getQualifier();
                    PropertyDescriptor propertyDescriptor = this.typeService.getPropertyDescriptor(qualifier);
                    sortDescriptors.add(propertyDescriptor);
                }
                searchType.setSortProperties(sortDescriptors);
            }
        }
        return searchType;
    }


    private ItemSearchType createDefaultSearchType(ObjectTemplate objectTemplate)
    {
        ItemSearchType searchType = new ItemSearchType(((ItemType)objectTemplate.getBaseType()).getComposedType());
        PropertyDescriptor propertyDescriptor = this.typeService.getPropertyDescriptor(Constants.TYPES.Item + "." + Constants.TYPES.Item);
        ItemAttributeSearchDescriptor searchDescriptor = (ItemAttributeSearchDescriptor)this.searchService.getSearchDescriptor(propertyDescriptor);
        searchDescriptor.setSimpleSearchProperty(false);
        Set<PropertyDescriptor> searchDescriptors = new HashSet<>();
        searchDescriptors.add(searchDescriptor);
        searchType.setDeclaredPropertyDescriptors(searchDescriptors);
        searchType.setSortProperties(Collections.singletonList(propertyDescriptor));
        return searchType;
    }


    private PropertyDescriptor createSearchPropertyDescriptor(ObjectTemplate objectTemplate, String qualifier)
    {
        PropertyDescriptor propertyDescriptor = this.typeService.getPropertyDescriptor((ObjectType)objectTemplate.getBaseType(), qualifier);
        return (PropertyDescriptor)this.searchService.getSearchDescriptor(propertyDescriptor, String.class.getName().equals(this.typeService.getValueTypeCode(propertyDescriptor)));
    }


    private ObjectLabelProvider createObjectLabelProvider(ObjectTemplate objectTemplate, Label xmlLabel)
    {
        GenericLabelProvider genericLabelProvider;
        ObjectLabelProvider objectLabelProvider = null;
        if(xmlLabel == null)
        {
            objectLabelProvider = createDefaultObjectLabelProvider(objectTemplate);
        }
        else if(xmlLabel.getSpringBean() != null)
        {
            if(xmlLabel.getClazz() != null)
            {
                LOG.error("Both spring-bean and class specified in label configuration for " + objectTemplate.getCode() + ". spring-bean takes precedece");
            }
            if(xmlLabel.getProperty() != null && !xmlLabel.getProperty().isEmpty())
            {
                LOG.error("Both spring-bean and properties in label configuration for " + objectTemplate.getCode() + ". spring-bean takes precedece");
            }
            try
            {
                objectLabelProvider = (ObjectLabelProvider)this.beanFactory.getBean(xmlLabel.getSpringBean(), ObjectLabelProvider.class);
            }
            catch(BeansException e)
            {
                LOG.error("Error creating object label provider  for " + objectTemplate.getCode() + " with Spring bean ID " + xmlLabel
                                .getSpringBean() + ". Exception raised: " + e.getClass().getName() + " - " + e.getMessage());
            }
        }
        else if(xmlLabel.getClazz() != null)
        {
            if(xmlLabel.getProperty() != null && !xmlLabel.getProperty().isEmpty())
            {
                LOG.error("Both class and properties in label configuration for " + objectTemplate.getCode() + ". class takes precedece");
            }
            try
            {
                objectLabelProvider = (ObjectLabelProvider)Class.forName(xmlLabel.getClazz()).newInstance();
            }
            catch(InstantiationException e)
            {
                LOG.error("Error creating object label provider  for " + objectTemplate.getCode() + " with class " + xmlLabel
                                .getClazz() + ". Exception raised: " + e.getClass().getName() + " - " + e.getMessage());
            }
            catch(IllegalAccessException e)
            {
                LOG.error("Error creating object label provider  for " + objectTemplate.getCode() + " with class " + xmlLabel
                                .getClazz() + ". Exception raised: " + e.getClass().getName() + " - " + e.getMessage());
            }
            catch(ClassNotFoundException e)
            {
                LOG.error("Error creating object label provider  for " + objectTemplate.getCode() + " with class " + xmlLabel
                                .getClazz() + ". Exception raised: " + e.getClass().getName() + " - " + e.getMessage());
            }
        }
        else if(xmlLabel.getProperty() != null && !xmlLabel.getProperty().isEmpty())
        {
            List<PropertyDescriptor> propertyDescriptors = new ArrayList<>(xmlLabel.getProperty().size());
            for(Property xmlProperty : xmlLabel.getProperty())
            {
                PropertyDescriptor propertyDescriptor = this.typeService.getPropertyDescriptor(xmlProperty.getQualifier());
                if(propertyDescriptor != null)
                {
                    propertyDescriptors.add(propertyDescriptor);
                    continue;
                }
                LOG.error("No such property: " + xmlProperty.getQualifier() + ". Ignoring for label for " + objectTemplate
                                .getCode());
            }
            if(!propertyDescriptors.isEmpty())
            {
                genericLabelProvider = new GenericLabelProvider(propertyDescriptors);
            }
        }
        else
        {
            LOG.error("Error creating object label provider  for " + objectTemplate.getCode() + ": Neither spring-bean nor class nor properties specified");
        }
        return (ObjectLabelProvider)genericLabelProvider;
    }


    private ObjectLabelProvider createDefaultObjectLabelProvider(ObjectTemplate objectTemplate)
    {
        PropertyDescriptor propertyDescriptor = null;
        String baseTypeCode = objectTemplate.getBaseType().getCode();
        if(CollectionUtils.isNotEmpty(this.labelFallbackPropertyQualifiers))
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Checking for fallback attributes...");
            }
            for(String fallbackProp : this.labelFallbackPropertyQualifiers)
            {
                try
                {
                    propertyDescriptor = this.typeService.getPropertyDescriptor(baseTypeCode + "." + baseTypeCode);
                }
                catch(UnknownIdentifierException e)
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("Could not find property '" + this.labelFallbackPropertyQualifiers + "' for type '" + baseTypeCode + "'.");
                    }
                }
                catch(Exception e)
                {
                    LOG.error("", e);
                }
                if(propertyDescriptor != null)
                {
                    break;
                }
            }
        }
        if(propertyDescriptor == null)
        {
            propertyDescriptor = this.typeService.getPropertyDescriptor(Constants.TYPES.Item + "." + Constants.TYPES.Item);
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Added falback labelProvider with property '" + propertyDescriptor.getQualifier() + "' for '" + baseTypeCode + "'.");
        }
        return (ObjectLabelProvider)new GenericLabelProvider(Collections.singletonList(propertyDescriptor));
    }


    private List<InitialPropertyConfiguration> createInitialPropertyConfiguration(List<InitialProperties> propertiesList)
    {
        if(propertiesList == null)
        {
            return null;
        }
        List<InitialPropertyConfiguration> configs = new ArrayList<>();
        for(InitialProperties properties : propertiesList)
        {
            String sourceObjectTemplateCode = properties.getSourceObjectTemplate();
            if(StringUtils.isBlank(sourceObjectTemplateCode))
            {
                LOG.error("No source object template code given");
                continue;
            }
            ObjectTemplate sourceObjectTemplate = null;
            try
            {
                sourceObjectTemplate = this.typeService.getObjectTemplate(sourceObjectTemplateCode);
            }
            catch(IllegalArgumentException e)
            {
                LOG.error("No such object template: " + sourceObjectTemplateCode);
                continue;
            }
            if(sourceObjectTemplate == null)
            {
                LOG.error("Cannot find source object template: " + sourceObjectTemplateCode);
                continue;
            }
            if(properties.getPropertyMapping() == null || properties.getPropertyMapping().isEmpty())
            {
                LOG.error("No property mappings given");
                continue;
            }
            Collection<PropertyMappingConfiguration> mappings = new ArrayList<>();
            for(PropertyMapping xmlMapping : properties.getPropertyMapping())
            {
                String source = xmlMapping.getSource();
                String target = xmlMapping.getTarget();
                if(!StringUtils.isBlank(source) && !StringUtils.isBlank(target))
                {
                    mappings.add(new DefaultPropertyMappingConfiguration(source, target));
                    continue;
                }
                LOG.error("Both source and target must be specified in initial property mapping");
            }
            configs.add(new DefaultInitialPropertyConfiguration(sourceObjectTemplate, mappings));
        }
        return configs;
    }


    public void setLabelFallbackPropertyQualifiers(List<String> labelFallbackPropertyQualifiers)
    {
        this.labelFallbackPropertyQualifiers = labelFallbackPropertyQualifiers;
    }
}
