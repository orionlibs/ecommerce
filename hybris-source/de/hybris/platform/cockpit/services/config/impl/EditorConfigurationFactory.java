package de.hybris.platform.cockpit.services.config.impl;

import de.hybris.platform.cockpit.model.meta.BaseType;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.services.SystemService;
import de.hybris.platform.cockpit.services.config.BaseConfiguration;
import de.hybris.platform.cockpit.services.config.BaseFallbackEnabledUIConfigurationFactory;
import de.hybris.platform.cockpit.services.config.EditorConfiguration;
import de.hybris.platform.cockpit.services.config.EditorRowConfiguration;
import de.hybris.platform.cockpit.services.config.EditorSectionConfiguration;
import de.hybris.platform.cockpit.services.config.UIComponentConfigurationContext;
import de.hybris.platform.cockpit.services.config.UIConfigurationException;
import de.hybris.platform.cockpit.services.config.jaxb.editor.CustomGroup;
import de.hybris.platform.cockpit.services.config.jaxb.editor.Editor;
import de.hybris.platform.cockpit.services.config.jaxb.editor.Group;
import de.hybris.platform.cockpit.services.config.jaxb.editor.Parameter;
import de.hybris.platform.cockpit.services.config.jaxb.editor.Property;
import de.hybris.platform.cockpit.services.security.UIAccessRightService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Required;

public class EditorConfigurationFactory extends JAXBBasedUIComponentConfigurationFactory<EditorConfiguration, Editor> implements BaseFallbackEnabledUIConfigurationFactory<EditorConfiguration>
{
    private static final Logger LOG = LoggerFactory.getLogger(EditorConfigurationFactory.class);
    private SystemService systemService;
    private UIAccessRightService accessService;


    public Class getComponentClass()
    {
        return EditorConfiguration.class;
    }


    public EditorConfiguration createDefault(ObjectTemplate objectTemplate, BaseConfiguration baseConfiguration)
    {
        if(baseConfiguration == null)
        {
            return createDefault(objectTemplate);
        }
        DefaultEditorConfiguration config = new DefaultEditorConfiguration();
        List<EditorSectionConfiguration> sections = new ArrayList<>();
        List<EditorSectionConfiguration> groups = new ArrayList<>();
        Map<String, List<PropertyDescriptor>> propertyGroups = getDefaultPropertyGroups(objectTemplate, baseConfiguration);
        int index = 0;
        for(Map.Entry<String, List<PropertyDescriptor>> entry : propertyGroups.entrySet())
        {
            EditorSectionConfiguration group = createGroupForProperties(entry.getValue(), entry.getKey());
            groups.add(group);
            if(index++ > 0)
            {
                group.setVisible(true);
                group.setInitiallyOpened(false);
            }
            else
            {
                group.setVisible(true);
                group.setInitiallyOpened(true);
            }
            sections.add(group);
        }
        EditorSectionConfiguration othersGroup = createOthersGroup("Other");
        othersGroup.setVisible(true);
        othersGroup.setInitiallyOpened(false);
        registerCustomSection(othersGroup, config);
        sections.add(othersGroup);
        config.setSections(sections);
        return (EditorConfiguration)config;
    }


    protected EditorSectionConfiguration createOthersGroup(String name)
    {
        UnassignedEditorSectionConfiguration unassignedEditorSectionConfiguration = new UnassignedEditorSectionConfiguration();
        unassignedEditorSectionConfiguration.setQualifier("fallback_unassigned");
        unassignedEditorSectionConfiguration.setAllLabel(createLabelForAllLanguages(name, this.systemService.getAvailableLanguages()));
        return (EditorSectionConfiguration)unassignedEditorSectionConfiguration;
    }


    protected void registerCustomSection(EditorSectionConfiguration sectionConfig, DefaultEditorConfiguration editorConfig)
    {
        JaxbBasedUIComponentConfigurationContext<Editor> context = new JaxbBasedUIComponentConfigurationContext(new Editor());
        editorConfig.setContext((UIComponentConfigurationContext)context);
        CustomGroup cgroup = new CustomGroup();
        cgroup.setClazz(sectionConfig.getClass().getName());
        context.registerJaxbElement(sectionConfig, cgroup);
    }


    public EditorConfiguration createDefault(ObjectTemplate objectTemplate)
    {
        Set<PropertyDescriptor> propertyDescs = objectTemplate.getDeclaredPropertyDescriptors();
        DefaultEditorConfiguration config = new DefaultEditorConfiguration();
        config.setSections(Collections.singletonList(createGroupForProperties(new ArrayList<>(propertyDescs), "General")));
        return (EditorConfiguration)config;
    }


    protected EditorSectionConfiguration createGroupForProperties(List<PropertyDescriptor> properties, String label)
    {
        List<EditorRowConfiguration> rows = new ArrayList<>();
        for(PropertyDescriptor desc : properties)
        {
            PropertyEditorRowConfiguration row = new PropertyEditorRowConfiguration(desc, true, true);
            rows.add(row);
        }
        DefaultEditorSectionConfiguration section = new DefaultEditorSectionConfiguration();
        section.setSectionRows(rows);
        section.setAllLabel(createLabelForAllLanguages(label, this.systemService.getAvailableLanguages()));
        section.setQualifier("fallback_" + label);
        return (EditorSectionConfiguration)section;
    }


    protected EditorConfiguration createUIComponent(ObjectTemplate objectTemplate, ObjectTemplate originalObjectTemplate, Editor xmlEditor)
    {
        JaxbBasedUIComponentConfigurationContext<Editor> context = new JaxbBasedUIComponentConfigurationContext(xmlEditor);
        List<Object> xmlGroups = xmlEditor.getGroupOrCustomGroup();
        List<EditorSectionConfiguration> sections = new ArrayList<>(xmlGroups.size());
        for(Object xmlGroup : xmlGroups)
        {
            EditorSectionConfiguration sectionConfig = null;
            if(xmlGroup instanceof Group)
            {
                sectionConfig = createSection((Group)xmlGroup, originalObjectTemplate.getBaseType(), context);
            }
            else if(xmlGroup instanceof CustomGroup)
            {
                sectionConfig = createCustomSection((CustomGroup)xmlGroup, context);
            }
            else
            {
                throw new UIConfigurationException("Unexpected JAXB for editor group: " + xmlGroup.getClass().getName());
            }
            if(sectionConfig != null)
            {
                sections.add(sectionConfig);
            }
        }
        DefaultEditorConfiguration editorConfig = new DefaultEditorConfiguration();
        editorConfig.setContext((UIComponentConfigurationContext)context);
        editorConfig.setSections(sections);
        return (EditorConfiguration)editorConfig;
    }


    private EditorSectionConfiguration createSection(Group xmlGroup, BaseType type, JaxbBasedUIComponentConfigurationContext context)
    {
        DefaultEditorSectionConfiguration sectionConfig = new DefaultEditorSectionConfiguration();
        context.registerJaxbElement(sectionConfig, xmlGroup);
        sectionConfig.setInitiallyOpened(xmlGroup.isInitiallyOpened());
        sectionConfig.setPosition(xmlGroup.getPosition().intValue());
        sectionConfig.setQualifier(xmlGroup.getQualifier());
        sectionConfig.setShowIfEmpty(xmlGroup.isShowIfEmpty());
        sectionConfig.setShowInCreateMode(xmlGroup.isShowInCreateMode());
        sectionConfig.setTabbed(xmlGroup.isTabbed());
        sectionConfig.setVisible(xmlGroup.isVisible());
        sectionConfig.setPrintable(xmlGroup.isPrintable());
        sectionConfig.setXmlDataProvider(xmlGroup.getXmlDataProvider());
        sectionConfig.setAllLabel(createLabelMap(xmlGroup.getLabel(), this.systemService.getAvailableLanguages()));
        List<Property> xmProperties = xmlGroup.getProperty();
        List<EditorRowConfiguration> rows = new ArrayList<>();
        for(Property xmlProperty : xmProperties)
        {
            addRow(rows, xmlProperty, (ObjectType)type, context);
        }
        sectionConfig.setSectionRows(rows);
        return (EditorSectionConfiguration)sectionConfig;
    }


    private void addRow(List<EditorRowConfiguration> rows, Property xmlProperty, ObjectType type, JaxbBasedUIComponentConfigurationContext context)
    {
        ObjectType objectType = this.typeService.getObjectType(this.typeService.getTypeCodeFromPropertyQualifier(xmlProperty
                        .getQualifier()));
        PropertyDescriptor propertyDescriptor = null;
        if(objectType instanceof BaseType)
        {
            propertyDescriptor = this.typeService.getPropertyDescriptor(type, xmlProperty.getQualifier());
        }
        else
        {
            propertyDescriptor = this.typeService.getPropertyDescriptor(objectType, xmlProperty.getQualifier());
        }
        if(propertyDescriptor == null)
        {
            LOG.error("No such descriptor for property: " + xmlProperty.getQualifier() + ". Ignoring editor property.");
        }
        else
        {
            PropertyEditorRowConfiguration row = new PropertyEditorRowConfiguration(propertyDescriptor, xmlProperty.isVisible(), true);
            context.registerJaxbElement(row, xmlProperty);
            row.setEditor(xmlProperty.getEditor());
            row.setPrintoutAs(xmlProperty.getPrintoutas());
            row.setXmlDataProvider(xmlProperty.getXmlDataProvider());
            List<Parameter> params = xmlProperty.getParameter();
            for(Parameter param : params)
            {
                row.setParameter(param.getName(), param.getValue());
            }
            rows.add(row);
        }
    }


    private EditorSectionConfiguration createCustomSection(CustomGroup xmlSection, JaxbBasedUIComponentConfigurationContext context)
    {
        if(xmlSection.getClazz() != null && xmlSection.getSpringBean() != null)
        {
            LOG.warn("Custom section has both class and spring-bean attribute. class attribute takes precedence");
        }
        EditorSectionConfiguration sectionConfig = null;
        if(xmlSection.getClazz() != null)
        {
            try
            {
                sectionConfig = (EditorSectionConfiguration)Class.forName(xmlSection.getClazz()).newInstance();
            }
            catch(InstantiationException e)
            {
                LOG.error("Error creating custom section with class " + xmlSection.getClazz() + ". Exception raised: " + e
                                .getClass().getName() + " - " + e.getMessage());
            }
            catch(IllegalAccessException e)
            {
                LOG.error("Error creating custom section with class " + xmlSection.getClazz() + ". Exception raised: " + e
                                .getClass().getName() + " - " + e.getMessage());
            }
            catch(ClassNotFoundException e)
            {
                LOG.error("Error creating custom section with class " + xmlSection.getClazz() + ". Exception raised: " + e
                                .getClass().getName() + " - " + e.getMessage());
            }
        }
        else if(xmlSection.getSpringBean() != null)
        {
            try
            {
                sectionConfig = (EditorSectionConfiguration)this.beanFactory.getBean(xmlSection.getSpringBean(), EditorSectionConfiguration.class);
            }
            catch(BeansException e)
            {
                LOG.error("Error creating custom section with Spring bean ID " + xmlSection.getSpringBean() + ". Exception raised: " + e
                                .getClass().getName() + " - " + e.getMessage());
            }
        }
        else
        {
            LOG.error("Neither class nor spring-bean specified for a custom-section. Ignoring.");
        }
        if(sectionConfig != null)
        {
            context.registerJaxbElement(sectionConfig, xmlSection);
            sectionConfig.setQualifier(xmlSection.getQualifier());
            sectionConfig.setInitiallyOpened(xmlSection.isInitiallyOpened());
            sectionConfig.setShowIfEmpty(xmlSection.isShowIfEmpty());
            sectionConfig.setVisible(xmlSection.isVisible());
            sectionConfig.setPrintable(xmlSection.isPrintable());
            sectionConfig.setXmlDataProvider(xmlSection.getXmlDataProvider());
            sectionConfig.setAllLabel(createLabelMap(xmlSection.getLabel(), this.systemService.getAvailableLanguages()));
        }
        return sectionConfig;
    }


    public void setBeanFactory(BeanFactory beanFactory) throws BeansException
    {
        this.beanFactory = beanFactory;
    }


    public void setSystemService(SystemService systemService)
    {
        this.systemService = systemService;
    }


    @Required
    public void setUiAccessRightService(UIAccessRightService accessService)
    {
        this.accessService = accessService;
    }


    public UIAccessRightService getUiAccessRightService()
    {
        return this.accessService;
    }
}
