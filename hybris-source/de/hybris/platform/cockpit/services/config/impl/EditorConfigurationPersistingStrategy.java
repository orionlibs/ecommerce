package de.hybris.platform.cockpit.services.config.impl;

import de.hybris.platform.cockpit.services.config.EditorConfiguration;
import de.hybris.platform.cockpit.services.config.EditorRowConfiguration;
import de.hybris.platform.cockpit.services.config.EditorSectionConfiguration;
import de.hybris.platform.cockpit.services.config.UIComponentConfiguration;
import de.hybris.platform.cockpit.services.config.jaxb.editor.CustomGroup;
import de.hybris.platform.cockpit.services.config.jaxb.editor.Editor;
import de.hybris.platform.cockpit.services.config.jaxb.editor.Group;
import de.hybris.platform.cockpit.services.config.jaxb.editor.Label;
import de.hybris.platform.cockpit.services.config.jaxb.editor.Parameter;
import de.hybris.platform.cockpit.services.config.jaxb.editor.Property;
import de.hybris.platform.core.model.c2l.LanguageModel;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EditorConfigurationPersistingStrategy extends DefaultConfigurationPersistingStrategy<EditorConfiguration, Editor>
{
    private static final String EDITOR_CONFIGURATION_FACTORY = "editorConfigurationFactory";
    private static final Logger LOG = LoggerFactory.getLogger(EditorConfigurationPersistingStrategy.class);


    public Editor updateJaxb(EditorConfiguration configuration)
    {
        Editor root = null;
        if(configuration instanceof DefaultEditorConfiguration)
        {
            DefaultEditorConfiguration config = (DefaultEditorConfiguration)configuration;
            root = (Editor)getRootJaxbElement((UIComponentConfiguration)config);
            if(root == null)
            {
                root = createRootElement(config);
            }
            List<Object> groups = root.getGroupOrCustomGroup();
            groups.clear();
            List<EditorSectionConfiguration> sections = config.getSections();
            if(sections != null)
            {
                for(EditorSectionConfiguration section : sections)
                {
                    Object group = updateJaxb(config, section);
                    if(group != null)
                    {
                        groups.add(group);
                    }
                }
            }
        }
        else
        {
            LOG.debug("Only " + DefaultEditorConfiguration.class + "can be persisted by this strategy.");
        }
        return root;
    }


    protected Object updateJaxb(DefaultEditorConfiguration config, EditorSectionConfiguration section)
    {
        Object groupObject = getJaxbElement((UIComponentConfiguration)config, section);
        if(groupObject == null)
        {
            groupObject = createJaxb(config, section);
        }
        if(groupObject instanceof CustomGroup)
        {
            CustomGroup group = (CustomGroup)groupObject;
            group.setInitiallyOpened(Boolean.valueOf(section.isInitiallyOpened()));
            group.setPrintable(Boolean.valueOf(section.isPrintable()));
            group.setQualifier(section.getQualifier());
            group.setShowIfEmpty(Boolean.valueOf(section.showIfEmpty()));
            group.setVisible(Boolean.valueOf(section.isVisible()));
            Map<LanguageModel, String> labels = section.getAllLabel();
            if(labels != null)
            {
                List<Label> jaxbLabels = group.getLabel();
                jaxbLabels.clear();
                for(Map.Entry<LanguageModel, String> entry : labels.entrySet())
                {
                    Label label = new Label();
                    label.setLang(((LanguageModel)entry.getKey()).getIsocode());
                    label.setValue(entry.getValue());
                    jaxbLabels.add(label);
                }
            }
        }
        else if(groupObject instanceof Group)
        {
            Group group = (Group)groupObject;
            group.setInitiallyOpened(Boolean.valueOf(section.isInitiallyOpened()));
            group.setPosition(BigInteger.valueOf(Math.max(1, section.getPosition())));
            group.setPrintable(Boolean.valueOf(section.isPrintable()));
            group.setQualifier(section.getQualifier());
            group.setShowIfEmpty(Boolean.valueOf(section.showIfEmpty()));
            group.setShowInCreateMode(Boolean.valueOf(section.showInCreateMode()));
            group.setTabbed(Boolean.valueOf(section.isTabbed()));
            group.setVisible(Boolean.valueOf(section.isVisible()));
            Map<LanguageModel, String> labels = section.getAllLabel();
            if(labels != null)
            {
                List<Label> jaxbLabels = group.getLabel();
                jaxbLabels.clear();
                for(Map.Entry<LanguageModel, String> entry : labels.entrySet())
                {
                    Label label = new Label();
                    label.setLang(((LanguageModel)entry.getKey()).getIsocode());
                    label.setValue(entry.getValue());
                    jaxbLabels.add(label);
                }
            }
            List<EditorRowConfiguration> rows = section.getSectionRows();
            List<Property> properties = group.getProperty();
            properties.clear();
            if(rows != null)
            {
                for(EditorRowConfiguration row : rows)
                {
                    Property property = new Property();
                    property.setEditor(row.getEditor());
                    property.setPrintoutas(row.getPrintoutAs());
                    property.setQualifier(row.getPropertyDescriptor().getQualifier());
                    property.setVisible(Boolean.valueOf(row.isVisible()));
                    updateJaxb(property.getParameter(), row.getParameters());
                    properties.add(property);
                }
            }
        }
        return groupObject;
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


    protected Object createJaxb(DefaultEditorConfiguration config, EditorSectionConfiguration section)
    {
        if(section instanceof de.hybris.platform.cockpit.services.config.CustomEditorSectionConfiguration)
        {
            CustomGroup customGroup = new CustomGroup();
            return customGroup;
        }
        Group group = new Group();
        return group;
    }


    protected Editor createRootElement(DefaultEditorConfiguration config)
    {
        Editor editor = new Editor();
        return editor;
    }


    public Class getComponentClass()
    {
        return EditorConfiguration.class;
    }


    public String getConfigurationFactory()
    {
        return "editorConfigurationFactory";
    }
}
