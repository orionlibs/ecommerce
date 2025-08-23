package de.hybris.platform.cockpit.services.config.impl;

import de.hybris.platform.cockpit.model.advancedsearch.config.EditorConditionEntry;
import de.hybris.platform.cockpit.model.advancedsearch.config.impl.DefaultShortcutConditionEntry;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.services.config.AdvancedSearchConfiguration;
import de.hybris.platform.cockpit.services.config.SearchFieldConfiguration;
import de.hybris.platform.cockpit.services.config.SearchFieldGroupConfiguration;
import de.hybris.platform.cockpit.services.config.UIComponentConfiguration;
import de.hybris.platform.cockpit.services.config.jaxb.advancedsearch.AdvancedSearch;
import de.hybris.platform.cockpit.services.config.jaxb.advancedsearch.Condition;
import de.hybris.platform.cockpit.services.config.jaxb.advancedsearch.ConditionList;
import de.hybris.platform.cockpit.services.config.jaxb.advancedsearch.Group;
import de.hybris.platform.cockpit.services.config.jaxb.advancedsearch.Label;
import de.hybris.platform.cockpit.services.config.jaxb.advancedsearch.Mode;
import de.hybris.platform.cockpit.services.config.jaxb.advancedsearch.Parameter;
import de.hybris.platform.cockpit.services.config.jaxb.advancedsearch.Property;
import de.hybris.platform.cockpit.services.config.jaxb.advancedsearch.RelatedTypes;
import de.hybris.platform.cockpit.services.config.jaxb.advancedsearch.RootGroup;
import de.hybris.platform.cockpit.services.config.jaxb.advancedsearch.ShortcutValue;
import de.hybris.platform.cockpit.services.config.jaxb.advancedsearch.ShortcutValueList;
import de.hybris.platform.cockpit.services.config.jaxb.advancedsearch.Type;
import de.hybris.platform.core.model.c2l.LanguageModel;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdvancedSearchConfigurationPersistingStrategy extends DefaultConfigurationPersistingStrategy<AdvancedSearchConfiguration, AdvancedSearch>
{
    private static final String ADVANCED_SEARCH_CONFIGURATION_FACTORY = "advancedSearchConfigurationFactory";
    private static final Logger LOG = LoggerFactory.getLogger(AdvancedSearchConfigurationPersistingStrategy.class);


    public AdvancedSearch updateJaxb(AdvancedSearchConfiguration configuration)
    {
        AdvancedSearch root = null;
        if(configuration instanceof DefaultAdvancedSearchConfiguration)
        {
            DefaultAdvancedSearchConfiguration config = (DefaultAdvancedSearchConfiguration)configuration;
            root = (AdvancedSearch)getRootJaxbElement((UIComponentConfiguration)config);
            if(root == null)
            {
                root = createRootElement(config);
            }
            List<ObjectTemplate> configTypes = config.getRelatedTypes();
            if(configTypes == null || configTypes.isEmpty())
            {
                root.setRelatedTypes(null);
            }
            else
            {
                RelatedTypes relatedTypes = root.getRelatedTypes();
                if(relatedTypes == null)
                {
                    relatedTypes = new RelatedTypes();
                    root.setRelatedTypes(relatedTypes);
                }
                List<Type> types = relatedTypes.getType();
                types.clear();
                for(ObjectTemplate type : configTypes)
                {
                    Type objectTemplateType = new Type();
                    objectTemplateType.setCode(type.getCode());
                    types.add(objectTemplateType);
                }
                root.getRelatedTypes().setIncludeSubTypes(Boolean.valueOf(config.isIncludeSubTypesForRelatedTypes()));
            }
            SearchFieldGroupConfiguration rootGroupConfig = config.getRootGroupConfiguration();
            if(rootGroupConfig != null)
            {
                Group rootGroup = updateJaxb(config, rootGroupConfig);
                if(rootGroup instanceof RootGroup)
                {
                    root.setGroup((RootGroup)rootGroup);
                }
            }
        }
        else
        {
            LOG.debug("Only " + DefaultAdvancedSearchConfiguration.class + "can be persisted by this strategy.");
        }
        return root;
    }


    protected Group updateJaxb(DefaultAdvancedSearchConfiguration config, SearchFieldGroupConfiguration group)
    {
        Group jaxbGroup = (Group)getJaxbElement((UIComponentConfiguration)config, group);
        if(jaxbGroup == null)
        {
            jaxbGroup = (group.getParentSearchFieldGroupConfiguration() == null) ? (Group)new RootGroup() : new Group();
        }
        if(jaxbGroup instanceof RootGroup)
        {
            ((RootGroup)jaxbGroup).setType(config.getRootType().getCode());
            ((RootGroup)jaxbGroup).setExcludeRootType(Boolean.valueOf(config.isExcludeRootType()));
            ((RootGroup)jaxbGroup).setIncludeSubTypes(Boolean.valueOf(config.isIncludeSubTypes()));
        }
        jaxbGroup.setName(group.getName());
        jaxbGroup.setVisible(Boolean.valueOf(group.isVisible()));
        Map<LanguageModel, String> labels = group.getAllLabels();
        if(labels != null)
        {
            List<Label> jaxbLabels = jaxbGroup.getLabel();
            jaxbLabels.clear();
            for(Map.Entry<LanguageModel, String> entry : labels.entrySet())
            {
                Label label = new Label();
                label.setLang(((LanguageModel)entry.getKey()).getIsocode());
                label.setValue(entry.getValue());
                jaxbLabels.add(label);
            }
        }
        List<SearchFieldConfiguration> fields = group.getSearchFieldConfigurations();
        List<Property> properties = jaxbGroup.getProperty();
        properties.clear();
        if(fields != null)
        {
            for(SearchFieldConfiguration field : fields)
            {
                Property property = (Property)getJaxbElement((UIComponentConfiguration)config, field);
                if(property == null)
                {
                    property = new Property();
                }
                property.setEditor(field.getEditor());
                property.setQualifier(field.getPropertyDescriptor().getQualifier());
                property.setSortEnabled(Boolean.valueOf(!field.isSortDisabled()));
                property.setVisible(Boolean.valueOf(field.isVisible()));
                updateJaxb(property.getParameter(), field.getParameters());
                List<EditorConditionEntry> conditionEntries = field.getConditionEntries();
                if(conditionEntries == null || conditionEntries.isEmpty())
                {
                    property.setConditions(null);
                }
                else
                {
                    ConditionList conditions = new ConditionList();
                    updateJaxb(conditionEntries, conditions);
                    property.setConditions(conditions);
                    setMode(field, conditions);
                }
                properties.add(property);
            }
        }
        List<SearchFieldGroupConfiguration> groups = group.getSearchFieldGroupConfigurations();
        List<Group> jaxbGroups = jaxbGroup.getGroup();
        jaxbGroups.clear();
        if(groups != null)
        {
            for(SearchFieldGroupConfiguration subgroup : groups)
            {
                Group jaxbSubgroup = updateJaxb(config, subgroup);
                if(jaxbSubgroup != null)
                {
                    jaxbGroups.add(jaxbSubgroup);
                }
            }
        }
        return jaxbGroup;
    }


    private void setMode(SearchFieldConfiguration field, ConditionList conditions)
    {
        SearchFieldConfiguration.EntryListMode mode = field.getEntryListMode();
        if(SearchFieldConfiguration.EntryListMode.APPEND.equals(mode))
        {
            conditions.setMode(Mode.APPEND);
        }
        else if(SearchFieldConfiguration.EntryListMode.EXCLUDE.equals(mode))
        {
            conditions.setMode(Mode.EXCLUDE);
        }
        else if(SearchFieldConfiguration.EntryListMode.REPLACE.equals(mode))
        {
            conditions.setMode(Mode.REPLACE);
        }
        else
        {
            LOG.warn("Mode '" + mode + "' not supported");
        }
    }


    protected void updateJaxb(List<EditorConditionEntry> conditionEntries, ConditionList conditions)
    {
        List<Condition> conditionList = conditions.getCondition();
        conditionList.clear();
        for(EditorConditionEntry entry : conditionEntries)
        {
            if(entry instanceof de.hybris.platform.cockpit.model.editor.search.impl.AbstractExtensibleConditionUIEditor.ClearConditionEntry)
            {
                continue;
            }
            Condition condition = new Condition();
            condition.setIndex(BigInteger.valueOf(entry.getIndex()));
            condition.setOperator(entry.getOperator());
            if(entry instanceof DefaultShortcutConditionEntry)
            {
                ShortcutValueList jaxbValueList = new ShortcutValueList();
                List<ShortcutValue> jaxbValues = jaxbValueList.getValue();
                List<Object> values = ((DefaultShortcutConditionEntry)entry).getConditionValues();
                if(values != null)
                {
                    for(Object value : values)
                    {
                        ShortcutValue jaxbValue = new ShortcutValue();
                        jaxbValue.setType(value.getClass().getName());
                        jaxbValue.setValue(String.valueOf(value));
                        jaxbValues.add(jaxbValue);
                    }
                }
                condition.setValues(jaxbValueList);
                condition.getLabel();
                Map<String, String> labels = ((DefaultShortcutConditionEntry)entry).getAllLabels();
                if(labels != null)
                {
                    for(Map.Entry<String, String> langEntry : labels.entrySet())
                    {
                        Label label = new Label();
                        label.setLang(langEntry.getKey());
                        label.setValue(langEntry.getValue());
                    }
                }
            }
            else
            {
                condition.setValues(null);
            }
            conditionList.add(condition);
        }
    }


    protected void updateJaxb(List<Parameter> jaxbParameters, Map<String, String> parameters)
    {
        jaxbParameters.clear();
        if(parameters != null)
        {
            for(Map.Entry<String, String> entry : parameters.entrySet())
            {
                Parameter jaxbParameter = new Parameter();
                jaxbParameter.setName(entry.getKey());
                jaxbParameter.setValue(entry.getValue());
                jaxbParameters.add(jaxbParameter);
            }
        }
    }


    protected AdvancedSearch createRootElement(DefaultAdvancedSearchConfiguration config)
    {
        AdvancedSearch root = new AdvancedSearch();
        return root;
    }


    public Class getComponentClass()
    {
        return AdvancedSearchConfiguration.class;
    }


    public String getConfigurationFactory()
    {
        return "advancedSearchConfigurationFactory";
    }
}
