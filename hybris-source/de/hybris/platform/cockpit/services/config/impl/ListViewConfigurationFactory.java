package de.hybris.platform.cockpit.services.config.impl;

import de.hybris.platform.cockpit.model.listview.CellRenderer;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.services.SystemService;
import de.hybris.platform.cockpit.services.config.BaseConfiguration;
import de.hybris.platform.cockpit.services.config.BaseFallbackEnabledUIConfigurationFactory;
import de.hybris.platform.cockpit.services.config.ColumnConfiguration;
import de.hybris.platform.cockpit.services.config.ColumnGroupConfiguration;
import de.hybris.platform.cockpit.services.config.ListViewConfiguration;
import de.hybris.platform.cockpit.services.config.UIComponentConfigurationContext;
import de.hybris.platform.cockpit.services.config.jaxb.listview.Custom;
import de.hybris.platform.cockpit.services.config.jaxb.listview.DynamicColumnProviders;
import de.hybris.platform.cockpit.services.config.jaxb.listview.Group;
import de.hybris.platform.cockpit.services.config.jaxb.listview.Language;
import de.hybris.platform.cockpit.services.config.jaxb.listview.ListView;
import de.hybris.platform.cockpit.services.config.jaxb.listview.Parameter;
import de.hybris.platform.cockpit.services.config.jaxb.listview.Property;
import de.hybris.platform.core.model.c2l.LanguageModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;

public class ListViewConfigurationFactory extends JAXBBasedUIComponentConfigurationFactory<ListViewConfiguration, ListView> implements BaseFallbackEnabledUIConfigurationFactory<ListViewConfiguration>
{
    private static final Logger LOG = LoggerFactory.getLogger(ListViewConfigurationFactory.class);
    private SystemService systemService;
    private static final String DEFAULT_MENU_POPUP_BUILDER_BEAN_ID = "listViewMenuPopupBuilder";


    public Class getComponentClass()
    {
        return ListViewConfiguration.class;
    }


    public ListViewConfiguration createDefault(ObjectTemplate objectTemplate)
    {
        DefaultColumnGroupConfiguration rootGroup = new DefaultColumnGroupConfiguration("General");
        rootGroup.setAllLabels(createLabelForAllLanguages("General", this.systemService.getAvailableLanguages()));
        Set<PropertyDescriptor> propertyDescriptors = objectTemplate.getPropertyDescriptors();
        for(PropertyDescriptor desc : propertyDescriptors)
        {
            rootGroup.addColumnConfiguration((ColumnConfiguration)new PropertyColumnConfiguration(desc));
        }
        DefaultListViewConfiguration defaultListViewConfiguration = new DefaultListViewConfiguration((ColumnGroupConfiguration)rootGroup);
        if(defaultListViewConfiguration.getHeaderPopupBean() == null)
        {
            defaultListViewConfiguration.setHeaderPopupBean("listViewMenuPopupBuilder");
        }
        return (ListViewConfiguration)defaultListViewConfiguration;
    }


    public ListViewConfiguration createDefault(ObjectTemplate objectTemplate, BaseConfiguration baseConfiguration)
    {
        DefaultColumnGroupConfiguration defaultColumnGroupConfiguration1;
        if(baseConfiguration == null)
        {
            return createDefault(objectTemplate);
        }
        DefaultListViewConfiguration config = new DefaultListViewConfiguration();
        Map<String, List<PropertyDescriptor>> propertyGroups = getDefaultPropertyGroups(objectTemplate, baseConfiguration);
        Set<PropertyDescriptor> usedProps = new HashSet<>();
        ColumnGroupConfiguration rootGroup = null;
        List<ColumnGroupConfiguration> subgroups = new ArrayList<>();
        int index = 0;
        for(Map.Entry<String, List<PropertyDescriptor>> entry : propertyGroups.entrySet())
        {
            DefaultColumnGroupConfiguration group = createGroupForProperties(entry.getValue(), entry.getKey(), (index == 0));
            if(index == 0)
            {
                defaultColumnGroupConfiguration1 = group;
                config.setRootColumnConfigurationGroup((ColumnGroupConfiguration)defaultColumnGroupConfiguration1);
            }
            else
            {
                subgroups.add(group);
            }
            usedProps.addAll(entry.getValue());
            index++;
        }
        List<PropertyDescriptor> otherProps = getOtherProperties(usedProps, objectTemplate);
        DefaultColumnGroupConfiguration otherGroup = createGroupForProperties(otherProps, "Other", false);
        subgroups.add(otherGroup);
        defaultColumnGroupConfiguration1.setColumnGroupConfigurations(subgroups);
        if(config.getHeaderPopupBean() == null)
        {
            config.setHeaderPopupBean("listViewMenuPopupBuilder");
        }
        return (ListViewConfiguration)config;
    }


    private DefaultColumnGroupConfiguration createGroupForProperties(List<PropertyDescriptor> props, String name, boolean visible)
    {
        DefaultColumnGroupConfiguration group = new DefaultColumnGroupConfiguration(name);
        group.setAllLabels(createLabelForAllLanguages(name, this.systemService.getAvailableLanguages()));
        for(PropertyDescriptor desc : props)
        {
            PropertyColumnConfiguration column = new PropertyColumnConfiguration(desc);
            column.setVisible(visible);
            group.addColumnConfiguration((ColumnConfiguration)column);
        }
        return group;
    }


    protected ListViewConfiguration createUIComponent(ObjectTemplate objectTemplate, ObjectTemplate originalObjectTemplate, ListView xmlListView)
    {
        JaxbBasedUIComponentConfigurationContext<ListView> context = new JaxbBasedUIComponentConfigurationContext(xmlListView);
        Map<String, PropertyDescriptor> propertyDescriptors = new HashMap<>();
        DefaultColumnGroupConfiguration rootGroup = createColumnGroupConfiguration(objectTemplate, originalObjectTemplate, xmlListView
                        .getGroup(), propertyDescriptors, context);
        if(!propertyDescriptors.isEmpty())
        {
            String unassignedGroupName = xmlListView.getUnassignedGroupName();
            if(unassignedGroupName != null)
            {
                Map<String, PropertyDescriptor> allDescriptors = mapPropertyDescriptors(objectTemplate.getPropertyDescriptors());
                DefaultColumnGroupConfiguration unassignedGroup = new DefaultColumnGroupConfiguration(unassignedGroupName);
                unassignedGroup.setAllLabels(createLabelForAllLanguages(unassignedGroupName, this.systemService.getAvailableLanguages()));
                boolean isConfigurationSet = false;
                for(PropertyDescriptor desc : allDescriptors.values())
                {
                    if(isNotAssignedDescriptor(propertyDescriptors, desc))
                    {
                        unassignedGroup.addColumnConfiguration((ColumnConfiguration)createPropertyColumnConfiguration(desc));
                        isConfigurationSet = true;
                    }
                }
                addColumnGroupConfiguration(rootGroup, unassignedGroup, isConfigurationSet);
            }
        }
        DefaultListViewConfiguration defaultListViewConfiguration = new DefaultListViewConfiguration((ColumnGroupConfiguration)rootGroup, xmlListView.isAllowCreateInlineItems());
        defaultListViewConfiguration.setContext((UIComponentConfigurationContext)context);
        if(xmlListView.getHeaderPopupBean() == null)
        {
            defaultListViewConfiguration.setHeaderPopupBean("listViewMenuPopupBuilder");
        }
        else
        {
            defaultListViewConfiguration.setHeaderPopupBean(xmlListView.getHeaderPopupBean());
        }
        DynamicColumnProviders dynamicColumnProviders = xmlListView.getDynamicColumnProviders();
        if(dynamicColumnProviders != null)
        {
            defaultListViewConfiguration.setDynamicColumnProvidersSpringBeans(dynamicColumnProviders.getSpringBeans());
        }
        return (ListViewConfiguration)defaultListViewConfiguration;
    }


    private boolean isNotAssignedDescriptor(Map<String, PropertyDescriptor> propertyDescriptors, PropertyDescriptor desc)
    {
        return !propertyDescriptors.containsValue(desc);
    }


    private PropertyColumnConfiguration createPropertyColumnConfiguration(PropertyDescriptor descriptor)
    {
        PropertyColumnConfiguration propertyColumnConfig = new PropertyColumnConfiguration(descriptor);
        propertyColumnConfig.setVisible(false);
        return propertyColumnConfig;
    }


    private void addColumnGroupConfiguration(DefaultColumnGroupConfiguration rootGroup, DefaultColumnGroupConfiguration groupToAdd, boolean shouldAdd)
    {
        if(shouldAdd)
        {
            rootGroup.addColumnGroupConfiguration((ColumnGroupConfiguration)groupToAdd);
        }
    }


    private Map<String, PropertyDescriptor> mapPropertyDescriptors(Set<PropertyDescriptor> propertyDescriptors)
    {
        Map<String, PropertyDescriptor> map = new HashMap<>();
        for(PropertyDescriptor desc : propertyDescriptors)
        {
            map.put(desc.getQualifier().toLowerCase(), desc);
        }
        return map;
    }


    private DefaultColumnGroupConfiguration createColumnGroupConfiguration(ObjectTemplate objectTemplate, ObjectTemplate originalObjectTemplate, Group xmlGroup, Map<String, PropertyDescriptor> propertyDescriptors, JaxbBasedUIComponentConfigurationContext context)
    {
        DefaultColumnGroupConfiguration config = new DefaultColumnGroupConfiguration(xmlGroup.getName());
        context.registerJaxbElement(config, xmlGroup);
        for(Object o : xmlGroup.getPropertyOrCustomOrGroup())
        {
            if(o instanceof Group)
            {
                config.addColumnGroupConfiguration((ColumnGroupConfiguration)createColumnGroupConfiguration(objectTemplate, originalObjectTemplate, (Group)o, propertyDescriptors, context));
                continue;
            }
            if(o instanceof Property)
            {
                PropertyColumnConfiguration columnConfig = createPropertyColumnConfiguration(objectTemplate, originalObjectTemplate, (Property)o, propertyDescriptors);
                if(columnConfig != null)
                {
                    context.registerJaxbElement(columnConfig, o);
                    config.addColumnConfiguration((ColumnConfiguration)columnConfig);
                }
                continue;
            }
            if(o instanceof Custom)
            {
                Custom xmlCustom = (Custom)o;
                ColumnConfiguration columnConfig = createCustomColumnConfiguration(xmlCustom);
                if(columnConfig != null)
                {
                    context.registerJaxbElement(columnConfig, o);
                    config.addColumnConfiguration(columnConfig);
                }
                continue;
            }
            LOG.warn("Unsupported XML configuration type: " + o.getClass().getName());
        }
        config.setAllLabels(createLabelMap(xmlGroup.getLabel(), this.systemService.getAvailableLanguages()));
        return config;
    }


    private ColumnConfiguration createCustomColumnConfiguration(Custom xmlCustom)
    {
        if(xmlCustom.getClazz() != null && xmlCustom.getSpringBean() != null)
        {
            LOG.warn("Custom configuration has both class and spring-bean attribute. class attribute takes precedence");
        }
        AbstractColumnConfiguration columnConfig = null;
        if(xmlCustom.getClazz() != null)
        {
            try
            {
                columnConfig = (AbstractColumnConfiguration)Class.forName(xmlCustom.getClazz()).newInstance();
            }
            catch(InstantiationException e)
            {
                LOG.error("Error creating column configuration with class " + xmlCustom.getClazz() + ". Exception raised: " + e
                                .getClass().getName() + " - " + e.getMessage());
            }
            catch(IllegalAccessException e)
            {
                LOG.error("Error creating column configuration with class " + xmlCustom.getClazz() + ". Exception raised: " + e
                                .getClass().getName() + " - " + e.getMessage());
            }
            catch(ClassNotFoundException e)
            {
                LOG.error("Error creating column configuration with class " + xmlCustom.getClazz() + ". Exception raised: " + e
                                .getClass().getName() + " - " + e.getMessage());
            }
        }
        else
        {
            try
            {
                columnConfig = (AbstractColumnConfiguration)this.beanFactory.getBean(xmlCustom.getSpringBean(), DefaultCustomColumnConfiguration.class);
            }
            catch(BeansException e)
            {
                LOG.error("Error creating column configuration with Spring Bean ID " + xmlCustom.getSpringBean() + ". Exception raised: " + e
                                .getClass().getName() + " - " + e.getMessage());
            }
        }
        if(columnConfig != null)
        {
            columnConfig.setName(xmlCustom.getName());
            columnConfig.setEditable(xmlCustom.isEditable());
            columnConfig.setSortable(xmlCustom.isSortable());
            columnConfig.setVisible(xmlCustom.isVisible());
            columnConfig.setSelectable(xmlCustom.isSelectable());
            columnConfig.setWidth(xmlCustom.getWidth());
            columnConfig.setPosition(xmlCustom.getPosition().intValue());
            if(columnConfig instanceof DefaultCustomColumnConfiguration)
            {
                ((DefaultCustomColumnConfiguration)columnConfig).setAllLabels(createLabelMap(xmlCustom.getLabel(), this.systemService
                                .getAvailableLanguages()));
            }
            createAndSetRenderer(columnConfig, xmlCustom.getRenderer());
        }
        return (ColumnConfiguration)columnConfig;
    }


    private PropertyColumnConfiguration createPropertyColumnConfiguration(ObjectTemplate objectTemplate, ObjectTemplate originalObjectTemplate, Property xmlProperty, Map<String, PropertyDescriptor> propertyDescriptors)
    {
        PropertyDescriptor desc = null;
        String qualifier = xmlProperty.getQualifier().toLowerCase();
        if(propertyDescriptors.containsKey(qualifier))
        {
            LOG.warn("Duplicate use of property descriptor with qualifier: " + qualifier);
        }
        else
        {
            if(originalObjectTemplate instanceof de.hybris.platform.cockpit.model.meta.BaseType)
            {
                desc = this.typeService.getPropertyDescriptor((ObjectType)originalObjectTemplate, xmlProperty.getQualifier());
            }
            else
            {
                desc = this.typeService.getPropertyDescriptor((ObjectType)originalObjectTemplate.getBaseType(), xmlProperty.getQualifier());
            }
            if(desc == null)
            {
                LOG.warn("No property descriptor with qualifier: " + xmlProperty.getQualifier());
            }
            else
            {
                propertyDescriptors.put(qualifier, desc);
            }
        }
        if(desc == null)
        {
            return null;
        }
        PropertyColumnConfiguration config = new PropertyColumnConfiguration(desc);
        String name = xmlProperty.getName();
        if(name != null)
        {
            config.setName(name);
        }
        config.setEditable(xmlProperty.isEditable());
        config.setSortable(xmlProperty.isSortable());
        config.setVisible(xmlProperty.isVisible());
        config.setSelectable(xmlProperty.isSelectable());
        config.setEditor(xmlProperty.getEditor());
        config.setWidth(xmlProperty.getWidth());
        config.setPosition(xmlProperty.getPosition().intValue());
        createAndSetRenderer((AbstractColumnConfiguration)config, xmlProperty.getRenderer());
        List<Language> xmlLanguages = xmlProperty.getLanguage();
        List<LanguageModel> uiLanguages = new ArrayList<>();
        if(desc.isLocalized())
        {
            for(Language xmlLanguage : xmlLanguages)
            {
                String isoCode = xmlLanguage.getIsocode();
                if(isoCode != null)
                {
                    LanguageModel lang = this.systemService.getLanguageForLocale(new Locale(isoCode));
                    uiLanguages.add(lang);
                    continue;
                }
                LOG.warn("Attribute isocode missing in <language> tag for property with qualifier " + xmlProperty.getQualifier());
            }
        }
        else if(!xmlLanguages.isEmpty())
        {
            LOG.warn("Ignoring language(s) for non localized attribute: " + xmlProperty.getQualifier());
        }
        config.setLanguages(uiLanguages);
        List<Parameter> xmlParams = xmlProperty.getParameter();
        for(Parameter parameter : xmlParams)
        {
            config.setParameter(parameter.getName(), parameter.getValue());
        }
        return config;
    }


    private void createAndSetRenderer(AbstractColumnConfiguration colConfig, CellRenderer xmlCellRenderer)
    {
        if(colConfig != null && xmlCellRenderer != null)
        {
            if(xmlCellRenderer.getClazz() != null && xmlCellRenderer.getSpringBean() != null)
            {
                LOG.warn("Custom configuration has both class and spring-bean attribute. class attribute takes precedence");
            }
            CellRenderer cellRenderer = null;
            if(xmlCellRenderer.getClazz() != null)
            {
                try
                {
                    cellRenderer = (CellRenderer)Class.forName(xmlCellRenderer.getClazz()).newInstance();
                }
                catch(InstantiationException e)
                {
                    LOG.error("Error creating column configuration with class " + xmlCellRenderer.getClazz() + ". Exception raised: " + e
                                    .getClass().getName() + " - " + e.getMessage());
                }
                catch(IllegalAccessException e)
                {
                    LOG.error("Error creating column configuration with class " + xmlCellRenderer.getClazz() + ". Exception raised: " + e
                                    .getClass().getName() + " - " + e.getMessage());
                }
                catch(ClassNotFoundException e)
                {
                    LOG.error("Error creating column configuration with class " + xmlCellRenderer.getClazz() + ". Exception raised: " + e
                                    .getClass().getName() + " - " + e.getMessage());
                }
            }
            else
            {
                try
                {
                    cellRenderer = (CellRenderer)this.beanFactory.getBean(xmlCellRenderer
                                    .getSpringBean(), CellRenderer.class);
                }
                catch(BeansException e)
                {
                    LOG.error("Error creating column configuration with Spring Bean ID " + xmlCellRenderer.getSpringBean() + ". Exception raised: " + e
                                    .getClass().getName() + " - " + e.getMessage());
                }
            }
            if(cellRenderer != null)
            {
                colConfig.setCellRenderer(cellRenderer);
            }
        }
    }


    public void setSystemService(SystemService systemService)
    {
        this.systemService = systemService;
    }
}
