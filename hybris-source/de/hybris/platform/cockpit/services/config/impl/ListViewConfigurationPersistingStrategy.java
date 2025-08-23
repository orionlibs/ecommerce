package de.hybris.platform.cockpit.services.config.impl;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.services.config.ColumnConfiguration;
import de.hybris.platform.cockpit.services.config.ColumnGroupConfiguration;
import de.hybris.platform.cockpit.services.config.ListViewConfiguration;
import de.hybris.platform.cockpit.services.config.UIComponentConfiguration;
import de.hybris.platform.cockpit.services.config.jaxb.listview.DynamicColumnProviders;
import de.hybris.platform.cockpit.services.config.jaxb.listview.Group;
import de.hybris.platform.cockpit.services.config.jaxb.listview.Label;
import de.hybris.platform.cockpit.services.config.jaxb.listview.Language;
import de.hybris.platform.cockpit.services.config.jaxb.listview.ListView;
import de.hybris.platform.cockpit.services.config.jaxb.listview.Parameter;
import de.hybris.platform.cockpit.services.config.jaxb.listview.Property;
import de.hybris.platform.core.model.c2l.LanguageModel;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ListViewConfigurationPersistingStrategy extends DefaultConfigurationPersistingStrategy<ListViewConfiguration, ListView>
{
    private static final String LIST_VIEW_CONFIGURATION_FACTORY = "listViewConfigurationFactory";
    private static final Logger LOG = LoggerFactory.getLogger(ListViewConfigurationPersistingStrategy.class);


    public ListView updateJaxb(ListViewConfiguration configuration)
    {
        ListView root = null;
        if(configuration instanceof DefaultListViewConfiguration)
        {
            DefaultListViewConfiguration config = (DefaultListViewConfiguration)configuration;
            root = (ListView)getRootJaxbElement((UIComponentConfiguration)config);
            if(root == null)
            {
                root = createRootElement(config);
            }
            root.setAllowCreateInlineItems(config.isAllowCreateInlineItems());
            List<String> dynamicColumnProviders = config.getDynamicColumnProvidersSpringBeans();
            if(dynamicColumnProviders == null || dynamicColumnProviders.isEmpty())
            {
                root.setDynamicColumnProviders(null);
            }
            else
            {
                DynamicColumnProviders dcProviders = new DynamicColumnProviders();
                dcProviders.setSpringBeans(dynamicColumnProviders);
                root.setDynamicColumnProviders(dcProviders);
            }
            root.setHeaderPopupBean(config.getHeaderPopupBean());
            ColumnGroupConfiguration rootGroup = config.getRootColumnGroupConfiguration();
            Group jaxbGroup = updateJaxb(config, rootGroup);
            root.setGroup(jaxbGroup);
        }
        else
        {
            LOG.debug("Only " + DefaultListViewConfiguration.class + "can be persisted by this strategy.");
        }
        return root;
    }


    protected Group updateJaxb(DefaultListViewConfiguration config, ColumnGroupConfiguration group)
    {
        Group jaxbGroup = (Group)getJaxbElement((UIComponentConfiguration)config, group);
        if(jaxbGroup == null)
        {
            jaxbGroup = createJaxb(config, group);
        }
        jaxbGroup.setName(group.getName());
        List<Object> jaxbChildren = jaxbGroup.getPropertyOrCustomOrGroup();
        jaxbChildren.clear();
        List<? extends ColumnGroupConfiguration> groups = group.getColumnGroupConfigurations();
        if(groups != null)
        {
            for(ColumnGroupConfiguration subGroup : groups)
            {
                Group jaxbSubgroup = updateJaxb(config, subGroup);
                if(jaxbSubgroup != null)
                {
                    jaxbChildren.add(jaxbSubgroup);
                }
            }
        }
        List<? extends ColumnConfiguration> columns = group.getColumnConfigurations();
        if(columns != null)
        {
            for(ColumnConfiguration column : columns)
            {
                Object jaxbColumn = updateJaxb(config, column);
                if(jaxbColumn != null)
                {
                    jaxbChildren.add(jaxbColumn);
                }
            }
        }
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
        return jaxbGroup;
    }


    protected Object updateJaxb(DefaultListViewConfiguration config, ColumnConfiguration column)
    {
        ColumnConfiguration jaxbColumn = (ColumnConfiguration)getJaxbElement((UIComponentConfiguration)config, column);
        if(jaxbColumn == null)
        {
            jaxbColumn = createJaxb(config, column);
        }
        jaxbColumn.setName(column.getName());
        jaxbColumn.setSortable(Boolean.valueOf(column.isSortable()));
        jaxbColumn.setVisible(Boolean.valueOf(column.isVisible()));
        jaxbColumn.setWidth(column.getWidth());
        jaxbColumn.setPosition(BigInteger.valueOf(Math.max(1, column.getPosition())));
        if(column instanceof AbstractColumnConfiguration)
        {
            jaxbColumn.setEditable(Boolean.valueOf(((AbstractColumnConfiguration)column).isEditable()));
        }
        if(jaxbColumn instanceof Property)
        {
            Property jaxbProperty = (Property)jaxbColumn;
            jaxbProperty.setEditor(column.getEditor());
            jaxbProperty.setSelectable(Boolean.valueOf(column.isSelectable()));
            if(column instanceof PropertyColumnConfiguration)
            {
                PropertyDescriptor propertyDescriptor = ((PropertyColumnConfiguration)column).getPropertyDescriptor();
                jaxbProperty.setQualifier(propertyDescriptor.getQualifier());
            }
            List<Language> jaxbLanguages = jaxbProperty.getLanguage();
            jaxbLanguages.clear();
            List<LanguageModel> languages = column.getLanguages();
            if(languages != null)
            {
                for(LanguageModel lang : languages)
                {
                    Language jaxbLang = new Language();
                    jaxbLang.setIsocode(lang.getIsocode());
                    jaxbLanguages.add(jaxbLang);
                }
            }
            List<Parameter> jaxbParameters = jaxbProperty.getParameter();
            updateJaxb(jaxbParameters, column.getParameters());
        }
        return jaxbColumn;
    }


    protected ColumnConfiguration createJaxb(DefaultListViewConfiguration config, ColumnConfiguration column)
    {
        Property property = new Property();
        return (ColumnConfiguration)property;
    }


    protected Group createJaxb(DefaultListViewConfiguration config, ColumnGroupConfiguration section)
    {
        Group group = new Group();
        return group;
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


    protected ListView createRootElement(DefaultListViewConfiguration config)
    {
        ListView root = new ListView();
        return root;
    }


    public Class getComponentClass()
    {
        return ListViewConfiguration.class;
    }


    public String getConfigurationFactory()
    {
        return "listViewConfigurationFactory";
    }
}
