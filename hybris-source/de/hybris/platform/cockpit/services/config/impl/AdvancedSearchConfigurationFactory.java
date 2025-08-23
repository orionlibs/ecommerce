package de.hybris.platform.cockpit.services.config.impl;

import de.hybris.platform.cockpit.model.advancedsearch.config.EditorConditionEntry;
import de.hybris.platform.cockpit.model.advancedsearch.config.impl.DefaultEditorConditionEntry;
import de.hybris.platform.cockpit.model.advancedsearch.config.impl.DefaultShortcutConditionEntry;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.services.SystemService;
import de.hybris.platform.cockpit.services.config.AdvancedSearchConfiguration;
import de.hybris.platform.cockpit.services.config.BaseConfiguration;
import de.hybris.platform.cockpit.services.config.BaseFallbackEnabledUIConfigurationFactory;
import de.hybris.platform.cockpit.services.config.SearchFieldConfiguration;
import de.hybris.platform.cockpit.services.config.SearchFieldGroupConfiguration;
import de.hybris.platform.cockpit.services.config.UIComponentConfigurationContext;
import de.hybris.platform.cockpit.services.config.jaxb.advancedsearch.AdvancedSearch;
import de.hybris.platform.cockpit.services.config.jaxb.advancedsearch.Condition;
import de.hybris.platform.cockpit.services.config.jaxb.advancedsearch.ConditionList;
import de.hybris.platform.cockpit.services.config.jaxb.advancedsearch.Group;
import de.hybris.platform.cockpit.services.config.jaxb.advancedsearch.Mode;
import de.hybris.platform.cockpit.services.config.jaxb.advancedsearch.Parameter;
import de.hybris.platform.cockpit.services.config.jaxb.advancedsearch.Property;
import de.hybris.platform.cockpit.services.config.jaxb.advancedsearch.RootGroup;
import de.hybris.platform.cockpit.services.config.jaxb.advancedsearch.ShortcutValue;
import de.hybris.platform.cockpit.services.config.jaxb.advancedsearch.ShortcutValueList;
import de.hybris.platform.cockpit.services.config.jaxb.advancedsearch.Type;
import de.hybris.platform.core.model.c2l.LanguageModel;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class AdvancedSearchConfigurationFactory extends JAXBBasedUIComponentConfigurationFactory<AdvancedSearchConfiguration, AdvancedSearch> implements BaseFallbackEnabledUIConfigurationFactory<AdvancedSearchConfiguration>
{
    private static final Logger LOG = LoggerFactory.getLogger(AdvancedSearchConfigurationFactory.class);
    private SystemService systemService;


    public Class getComponentClass()
    {
        return AdvancedSearchConfiguration.class;
    }


    public AdvancedSearchConfiguration createDefault(ObjectTemplate objectTemplate)
    {
        List<SearchFieldConfiguration> generalFields = new ArrayList<>();
        List<SearchFieldConfiguration> otherFields = new ArrayList<>();
        Set<PropertyDescriptor> propertyDescs = objectTemplate.getDeclaredPropertyDescriptors();
        for(PropertyDescriptor pd : propertyDescs)
        {
            PropertySearchFieldConfiguration field = new PropertySearchFieldConfiguration((PropertyDescriptor)this.searchService.getSearchDescriptor(pd));
            field.setSortDisabled(false);
            String attributeCode = this.typeService.getAttributeCodeFromPropertyQualifier(pd.getQualifier());
            if("pk".equalsIgnoreCase(attributeCode) || "code".equalsIgnoreCase(attributeCode) || "name"
                            .equalsIgnoreCase(attributeCode) || "description".equalsIgnoreCase(attributeCode))
            {
                field.setVisible(true);
                generalFields.add(field);
                continue;
            }
            field.setVisible(false);
            otherFields.add(field);
        }
        DefaultSearchFieldGroupConfiguration generalGroup = new DefaultSearchFieldGroupConfiguration("General");
        generalGroup.setAllLabels(createLabelForAllLanguages("General", this.systemService.getAvailableLanguages()));
        generalGroup.setVisible(true);
        generalGroup.setSearchFieldConfigurations(generalFields);
        DefaultSearchFieldGroupConfiguration otherGroup = new DefaultSearchFieldGroupConfiguration("Other");
        otherGroup.setAllLabels(createLabelForAllLanguages("Other", this.systemService.getAvailableLanguages()));
        otherGroup.setVisible(false);
        otherGroup.setSearchFieldConfigurations(otherFields);
        DefaultSearchFieldGroupConfiguration rootConfig = new DefaultSearchFieldGroupConfiguration("root");
        rootConfig.setAllLabels(createLabelForAllLanguages("All", this.systemService.getAvailableLanguages()));
        rootConfig.setSearchFieldGroupConfigurations(Arrays.asList(new DefaultSearchFieldGroupConfiguration[] {generalGroup, otherGroup}));
        DefaultAdvancedSearchConfiguration config = new DefaultAdvancedSearchConfiguration(objectTemplate);
        config.setRootGroupConfiguration((SearchFieldGroupConfiguration)rootConfig);
        return (AdvancedSearchConfiguration)config;
    }


    public AdvancedSearchConfiguration createDefault(ObjectTemplate objectTemplate, BaseConfiguration baseConfiguration)
    {
        if(baseConfiguration == null)
        {
            return createDefault(objectTemplate);
        }
        DefaultAdvancedSearchConfiguration config = new DefaultAdvancedSearchConfiguration(objectTemplate);
        List<SearchFieldGroupConfiguration> groups = new ArrayList<>();
        Map<String, List<PropertyDescriptor>> propertyGroups = getDefaultPropertyGroups(objectTemplate, baseConfiguration);
        Set<PropertyDescriptor> usedProps = new HashSet<>();
        int index = 0;
        for(Map.Entry<String, List<PropertyDescriptor>> entry : propertyGroups.entrySet())
        {
            DefaultSearchFieldGroupConfiguration group = createGroupForProperties(entry.getValue(), entry.getKey(),
                            (index++ == 0));
            groups.add(group);
            usedProps.addAll(entry.getValue());
        }
        List<PropertyDescriptor> otherProps = getOtherProperties(usedProps, objectTemplate);
        DefaultSearchFieldGroupConfiguration otherGroup = createGroupForProperties(otherProps, "Other", false);
        groups.add(otherGroup);
        DefaultSearchFieldGroupConfiguration rootConfig = new DefaultSearchFieldGroupConfiguration("root");
        rootConfig.setAllLabels(createLabelForAllLanguages("All", this.systemService.getAvailableLanguages()));
        rootConfig.setSearchFieldGroupConfigurations(groups);
        config.setRootGroupConfiguration((SearchFieldGroupConfiguration)rootConfig);
        return (AdvancedSearchConfiguration)config;
    }


    private DefaultSearchFieldGroupConfiguration createGroupForProperties(List<PropertyDescriptor> props, String label, boolean visible)
    {
        List<SearchFieldConfiguration> fields = new ArrayList<>();
        for(PropertyDescriptor pd : props)
        {
            PropertySearchFieldConfiguration field = new PropertySearchFieldConfiguration((PropertyDescriptor)this.searchService.getSearchDescriptor(pd));
            field.setSortDisabled(false);
            field.setVisible(visible);
            fields.add(field);
        }
        DefaultSearchFieldGroupConfiguration group = new DefaultSearchFieldGroupConfiguration(label);
        group.setAllLabels(createLabelForAllLanguages(label, this.systemService.getAvailableLanguages()));
        group.setVisible(visible);
        group.setSearchFieldConfigurations(fields);
        return group;
    }


    protected AdvancedSearchConfiguration createUIComponent(ObjectTemplate objectTemplate, ObjectTemplate originalObjectTemplate, AdvancedSearch advancedSearch)
    {
        JaxbBasedUIComponentConfigurationContext<AdvancedSearch> context = new JaxbBasedUIComponentConfigurationContext(advancedSearch);
        DefaultAdvancedSearchConfiguration config = new DefaultAdvancedSearchConfiguration(originalObjectTemplate);
        config.setContext((UIComponentConfigurationContext)context);
        RootGroup rootGroup = advancedSearch.getGroup();
        SearchFieldGroupConfiguration group = createSearchFieldGroupConfiguration((Group)rootGroup, context);
        config.setRootGroupConfiguration(group);
        if(advancedSearch.getRelatedTypes() != null && advancedSearch.getRelatedTypes().getType() != null &&
                        !advancedSearch.getRelatedTypes().getType().isEmpty())
        {
            List<ObjectTemplate> types = new ArrayList<>();
            for(Type xmlType : advancedSearch.getRelatedTypes().getType())
            {
                if(!StringUtils.isBlank(xmlType.getCode()))
                {
                    ObjectTemplate type = this.typeService.getObjectTemplate(xmlType.getCode());
                    if(type != null)
                    {
                        types.add(type);
                        continue;
                    }
                    LOG.error("No such related type: " + xmlType.getCode());
                    continue;
                }
                LOG.error("Code of related type must not be empty");
            }
            config.setRelatedTypes(types);
            config.setIncludeSubTypesForRelatedTypes(advancedSearch.getRelatedTypes().isIncludeSubTypes());
            config.setIncludeSubTypes(advancedSearch.getGroup().isIncludeSubTypes());
            config.setExcludeRootType(advancedSearch.getGroup().isExcludeRootType());
        }
        return (AdvancedSearchConfiguration)config;
    }


    private SearchFieldGroupConfiguration createSearchFieldGroupConfiguration(Group group, JaxbBasedUIComponentConfigurationContext context)
    {
        DefaultSearchFieldGroupConfiguration groupConfig = new DefaultSearchFieldGroupConfiguration(group.getName());
        context.registerJaxbElement(groupConfig, group);
        groupConfig.setVisible(group.isVisible());
        groupConfig.setAllLabels(createLabelMap(group.getLabel(), this.systemService.getAvailableLanguages()));
        groupConfig.setSearchFieldConfigurations(createFields(group.getProperty(), context));
        List<SearchFieldGroupConfiguration> groups = new ArrayList<>();
        List<Group> xmlGroups = group.getGroup();
        for(Group xmlGroup : xmlGroups)
        {
            groups.add(createSearchFieldGroupConfiguration(xmlGroup, context));
        }
        groupConfig.setSearchFieldGroupConfigurations(groups);
        return (SearchFieldGroupConfiguration)groupConfig;
    }


    private List<SearchFieldConfiguration> createFields(List<Property> xmlProperties, JaxbBasedUIComponentConfigurationContext context)
    {
        List<SearchFieldConfiguration> fields = new ArrayList<>(xmlProperties.size());
        for(Property xmlProperty : xmlProperties)
        {
            if(xmlProperty.getQualifier() != null)
            {
                PropertyDescriptor propertyDesc = this.typeService.getPropertyDescriptor(xmlProperty.getQualifier());
                if(propertyDesc != null)
                {
                    PropertySearchFieldConfiguration field = new PropertySearchFieldConfiguration((PropertyDescriptor)this.searchService.getSearchDescriptor(propertyDesc));
                    context.registerJaxbElement(field, xmlProperty);
                    field.setVisible(xmlProperty.isVisible());
                    field.setSortDisabled(!xmlProperty.isSortEnabled());
                    field.setEditor(xmlProperty.getEditor());
                    List<Parameter> parameters = xmlProperty.getParameter();
                    if(parameters != null)
                    {
                        Map<String, String> paramMap = new HashMap<>();
                        for(Parameter parameter : parameters)
                        {
                            paramMap.put(parameter.getName(), parameter.getValue());
                        }
                        field.setParameters(paramMap);
                    }
                    ConditionList conditions = xmlProperty.getConditions();
                    if(conditions != null)
                    {
                        Mode mode = conditions.getMode();
                        if(Mode.APPEND.equals(mode))
                        {
                            field.setEntryListMode(SearchFieldConfiguration.EntryListMode.APPEND);
                        }
                        else if(Mode.EXCLUDE.equals(mode))
                        {
                            field.setEntryListMode(SearchFieldConfiguration.EntryListMode.EXCLUDE);
                        }
                        else
                        {
                            if(!Mode.REPLACE.equals(mode))
                            {
                                LOG.warn("Mode '" + mode + "' not supported, using 'replace' as fallback");
                            }
                            field.setEntryListMode(SearchFieldConfiguration.EntryListMode.REPLACE);
                        }
                        List<Condition> list = conditions.getCondition();
                        if(list != null)
                        {
                            List<EditorConditionEntry> entries = new ArrayList<>();
                            for(Condition condition : list)
                            {
                                DefaultEditorConditionEntry entry = null;
                                ShortcutValueList values = condition.getValues();
                                if(values != null)
                                {
                                    DefaultShortcutConditionEntry defaultShortcutConditionEntry = new DefaultShortcutConditionEntry();
                                    DefaultShortcutConditionEntry defaultShortcutConditionEntry1 = defaultShortcutConditionEntry;
                                    List<ShortcutValue> shortCutValues = values.getValue();
                                    if(shortCutValues != null)
                                    {
                                        List<Object> conditionValues = new ArrayList();
                                        for(ShortcutValue shortcutValue : shortCutValues)
                                        {
                                            String type = shortcutValue.getType();
                                            String value = shortcutValue.getValue();
                                            Object wrappedValue = null;
                                            try
                                            {
                                                Class<?> valueClass = Class.forName(type);
                                                wrappedValue = wrapValue(valueClass, value);
                                            }
                                            catch(Exception e)
                                            {
                                                LOG.warn("Could not convert value '" + value + "' into type '" + type + "', reason: ", e);
                                            }
                                            if(wrappedValue != null)
                                            {
                                                conditionValues.add(wrappedValue);
                                            }
                                        }
                                        defaultShortcutConditionEntry.setConditionValues(conditionValues);
                                    }
                                    Map<LanguageModel, String> allLabels = createLabelMap(condition.getLabel(), this.systemService
                                                    .getAvailableLanguages());
                                    for(Map.Entry<LanguageModel, String> labelEntry : allLabels.entrySet())
                                    {
                                        String lang = ((LanguageModel)labelEntry.getKey()).getIsocode();
                                        String value = labelEntry.getValue();
                                        defaultShortcutConditionEntry.setLabel(lang, value);
                                    }
                                }
                                else
                                {
                                    entry = new DefaultEditorConditionEntry();
                                }
                                entry.setOperator(condition.getOperator());
                                if(condition.getIndex() != null)
                                {
                                    entry.setIndex(condition.getIndex().intValue());
                                }
                                context.registerJaxbElement(entry, condition);
                                entries.add(entry);
                            }
                            field.setConditionEntries(entries);
                        }
                    }
                    fields.add(field);
                    continue;
                }
                LOG.error("Cannot find property descriptor for " + xmlProperty.getQualifier() + ". Ignoring.");
                continue;
            }
            LOG.error("Skipping advanced search field configuration without qualifier");
        }
        return fields;
    }


    private <T> T wrapValue(Class clazz, String stringRep) throws ParseException
    {
        if(String.class.equals(clazz))
        {
            return (T)stringRep;
        }
        if(Integer.class.equals(clazz))
        {
            return (T)Integer.valueOf(Integer.parseInt(stringRep));
        }
        if(Long.class.equals(clazz))
        {
            return (T)Long.valueOf(Long.parseLong(stringRep));
        }
        if(Short.class.equals(clazz))
        {
            return (T)Short.valueOf(Short.parseShort(stringRep));
        }
        if(Byte.class.equals(clazz))
        {
            return (T)Byte.valueOf(Byte.parseByte(stringRep));
        }
        if(Float.class.equals(clazz))
        {
            return (T)Float.valueOf(Float.parseFloat(stringRep));
        }
        if(Double.class.equals(clazz))
        {
            return (T)Double.valueOf(Double.parseDouble(stringRep));
        }
        if(Date.class.equals(clazz))
        {
            return (T)DateFormat.getInstance().parse(stringRep);
        }
        if(Boolean.class.equals(clazz))
        {
            return (T)Boolean.valueOf(stringRep);
        }
        throw new ParseException("No wrapper defined", 0);
    }


    @Required
    public void setSystemService(SystemService systemService)
    {
        this.systemService = systemService;
    }
}
