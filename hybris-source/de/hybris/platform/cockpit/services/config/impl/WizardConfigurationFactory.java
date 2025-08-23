package de.hybris.platform.cockpit.services.config.impl;

import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.services.SystemService;
import de.hybris.platform.cockpit.services.config.UIComponentConfiguration;
import de.hybris.platform.cockpit.services.config.WizardConfiguration;
import de.hybris.platform.cockpit.services.config.jaxb.wizard.Group;
import de.hybris.platform.cockpit.services.config.jaxb.wizard.Parameter;
import de.hybris.platform.cockpit.services.config.jaxb.wizard.Property;
import de.hybris.platform.cockpit.services.config.jaxb.wizard.WizardConfig;
import de.hybris.platform.cockpit.services.config.jaxb.wizard.WizardPropertyList;
import de.hybris.platform.cockpit.session.UISession;
import de.hybris.platform.cockpit.session.UISessionUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WizardConfigurationFactory extends JAXBBasedUIComponentConfigurationFactory<WizardConfiguration, WizardConfig>
{
    private static final Logger LOG = LoggerFactory.getLogger(WizardConfigurationFactory.class);
    private SystemService systemService;


    public SystemService getSystemService()
    {
        return this.systemService;
    }


    public void setSystemService(SystemService systemService)
    {
        this.systemService = systemService;
    }


    protected WizardConfiguration createUIComponent(ObjectTemplate objectTemplate, ObjectTemplate originalObjectTemplate, WizardConfig xmlContentEditor)
    {
        DefaultWizardConfiguration conf = null;
        if(validateType(objectTemplate))
        {
            Map<String, String> allVisible = new LinkedHashMap<>();
            Map<String, String> allHidden = new LinkedHashMap<>();
            getQualifiers(xmlContentEditor, allVisible, allHidden);
            Map<String, Map<String, String>> paramMap = createParameterMap(xmlContentEditor);
            conf = new DefaultWizardConfiguration(allVisible, allHidden, paramMap, xmlContentEditor.isShowPrefilledValues(), xmlContentEditor.isSelectMode(), xmlContentEditor.isCreateMode(), xmlContentEditor.isDisplaySubtypes());
            conf.setGroups(getGroups(xmlContentEditor, true));
            conf.setUnboundProperties(getUnboundProperties(xmlContentEditor));
            conf.setActivateAfterCreate(xmlContentEditor.getActivateAfterCreate());
            conf.setCreateWithinEditorArea(xmlContentEditor.getCreateWithinEditor());
            conf.setCreateWithinPopup(xmlContentEditor.getCreateWithinPopup());
            conf.setWizardScript(xmlContentEditor.getWizardScript());
            conf.setValidationInfoIgnored(xmlContentEditor.isValidationInfoIgnored());
        }
        return (WizardConfiguration)conf;
    }


    public UIComponentConfiguration createDefault(ObjectTemplate objectTemplate)
    {
        DefaultWizardConfiguration defConf = null;
        if(validateType(objectTemplate))
        {
            defConf = new DefaultWizardConfiguration(Collections.EMPTY_MAP, Collections.EMPTY_MAP, Collections.EMPTY_MAP, false, true, false, true);
        }
        return (UIComponentConfiguration)defConf;
    }


    public Class getComponentClass()
    {
        return WizardConfiguration.class;
    }


    private Map<String, String> getUnboundProperties(WizardConfig xmlContentEditor)
    {
        Map<String, String> unboundProperties = null;
        if(xmlContentEditor != null)
        {
            unboundProperties = new LinkedHashMap<>();
            WizardPropertyList wizardPropertyList = xmlContentEditor.getWizardPropertyList();
            if(wizardPropertyList != null)
            {
                List<Property> properties = wizardPropertyList.getProperty();
                if(properties != null)
                {
                    for(Property prop : properties)
                    {
                        if(prop.isVisible())
                        {
                            unboundProperties.put(prop.getQualifier(), prop.getEditorCode());
                        }
                    }
                }
            }
        }
        else
        {
            LOG.warn("No content editor object available.");
        }
        return unboundProperties;
    }


    private Map<WizardGroupConfiguration, List<String>> getGroups(WizardConfig xmlContentEditor, boolean visible)
    {
        Map<WizardGroupConfiguration, List<String>> propertiesForGroups = null;
        if(xmlContentEditor != null)
        {
            propertiesForGroups = new LinkedHashMap<>();
            WizardPropertyList wizardPropertyList = xmlContentEditor.getWizardPropertyList();
            if(wizardPropertyList != null)
            {
                List<Group> configuredGroups = wizardPropertyList.getGroups();
                if(configuredGroups != null)
                {
                    for(Group group : configuredGroups)
                    {
                        if(group.isVisible() == visible)
                        {
                            List<String> propertyQualifiers = new ArrayList<>();
                            if(group.getProperties() != null && CollectionUtils.isNotEmpty(group.getProperties()))
                            {
                                for(Property property : group.getProperties())
                                {
                                    propertyQualifiers.add(property.getQualifier());
                                }
                                WizardGroupConfiguration wzc = new WizardGroupConfiguration(this);
                                wzc.setQualifier(group.getQualifier());
                                wzc.setAllLabels(createLabelMap(group.getLabel(), this.systemService.getAvailableLanguages()));
                                wzc.setInitiallyOpened(group.isInitiallyOpened());
                                propertiesForGroups.put(wzc, propertyQualifiers);
                            }
                        }
                    }
                }
            }
        }
        else
        {
            LOG.warn("No content editor object available.");
        }
        return propertiesForGroups;
    }


    private void getQualifiers(WizardConfig xmlContentEditor, Map<String, String> allVisible, Map<String, String> allHidden)
    {
        if(xmlContentEditor != null)
        {
            WizardPropertyList wizardPropertyList = xmlContentEditor.getWizardPropertyList();
            if(wizardPropertyList != null)
            {
                getPropertyQualifiers(wizardPropertyList.getProperty(), allVisible, allHidden);
                List<Group> groups = wizardPropertyList.getGroups();
                if(CollectionUtils.isNotEmpty(groups))
                {
                    for(Group group : groups)
                    {
                        getPropertyQualifiers(group.getProperties(), allVisible, allHidden);
                    }
                }
            }
        }
        else
        {
            LOG.warn("No content editor object available.");
        }
    }


    private void getPropertyQualifiers(List<Property> properties, Map<String, String> allVisible, Map<String, String> allHidden)
    {
        if(properties != null)
        {
            for(Property prop : properties)
            {
                if(prop.isVisible())
                {
                    allVisible.put(prop.getQualifier(), prop.getEditorCode());
                    continue;
                }
                allHidden.put(prop.getQualifier(), prop.getEditorCode());
            }
        }
    }


    private Map<String, Map<String, String>> createParameterMap(WizardConfig xmlContentEditor)
    {
        Map<String, Map<String, String>> paramMap = null;
        if(xmlContentEditor != null)
        {
            paramMap = new LinkedHashMap<>();
            WizardPropertyList propertyList = xmlContentEditor.getWizardPropertyList();
            if(propertyList != null)
            {
                List<Property> propertyEntries = propertyList.getProperty();
                UISession currentSession = UISessionUtils.getCurrentSession();
                Locale currentLocale = currentSession.getLocale();
                if(propertyEntries != null)
                {
                    for(Property entry : propertyEntries)
                    {
                        if(entry != null)
                        {
                            List<Parameter> parameters = entry.getParameter();
                            if(parameters != null && !parameters.isEmpty())
                            {
                                Map<String, String> editorParams = new LinkedHashMap<>();
                                paramMap.put(currentSession.getTypeService().getPropertyDescriptor(entry.getQualifier()).getQualifier()
                                                .toLowerCase(currentLocale), editorParams);
                                for(Parameter param : parameters)
                                {
                                    editorParams.put(param.getName(), param.getValue());
                                }
                            }
                        }
                    }
                }
                List<Group> groupEntries = propertyList.getGroups();
                if(groupEntries != null)
                {
                    for(Group group : groupEntries)
                    {
                        if(group != null)
                        {
                            List<Property> properties = group.getProperties();
                            if(properties != null && !properties.isEmpty())
                            {
                                for(Property property : properties)
                                {
                                    Map<String, String> params = new LinkedHashMap<>();
                                    paramMap.put(currentSession.getTypeService().getPropertyDescriptor(property.getQualifier())
                                                    .getQualifier().toLowerCase(currentLocale), params);
                                    for(Parameter param : property.getParameter())
                                    {
                                        params.put(param.getName(), param.getValue());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        else
        {
            LOG.warn("Could not create parameter map. Reason: No content editor object available.");
        }
        return paramMap;
    }


    private boolean validateType(ObjectTemplate type)
    {
        boolean isOK = false;
        try
        {
            if(type == null)
            {
                LOG.warn("Can not create default configuration. Reason: No Object template specified.");
            }
            else
            {
                isOK = true;
            }
        }
        catch(Exception e)
        {
            LOG.error("Could not validate type.", e);
            isOK = false;
        }
        return isOK;
    }
}
