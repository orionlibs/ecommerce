package de.hybris.platform.cockpit.services.config.impl;

import de.hybris.platform.cockpit.services.config.WizardConfiguration;
import de.hybris.platform.cockpit.services.config.jaxb.wizard.AfterDoneWizardScript;
import de.hybris.platform.cockpit.session.UISessionUtils;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DefaultWizardConfiguration implements WizardConfiguration
{
    private final Map<String, String> displayedQuelifiers = new LinkedHashMap<>();
    private final Map<String, String> hiddenQuelifiers = new LinkedHashMap<>();
    private Map<String, Map<String, String>> params = new HashMap<>();
    private Map<WizardConfigurationFactory.WizardGroupConfiguration, List<String>> groups = new LinkedHashMap<>();
    private Map<String, String> unboundProperties = new LinkedHashMap<>();
    private boolean showPrefilledValues = false;
    private boolean createMode = false;
    private boolean selectMode = false;
    private boolean displaySubtypes = true;
    private boolean activateAfterCreate = true;
    private boolean createWithinPopup = false;
    private boolean createWithinEditorArea = false;
    private boolean validationInfoIgnored = false;
    private AfterDoneWizardScript wizardScript;


    public DefaultWizardConfiguration(Map<String, String> editorMap, Map<String, String> hiddenQualifiers, Map<String, Map<String, String>> params, boolean showPreffiledValues, boolean selectMode, boolean createMode, boolean displaySubtypes)
    {
        this.showPrefilledValues = showPreffiledValues;
        this.selectMode = selectMode;
        this.createMode = createMode;
        this.displaySubtypes = displaySubtypes;
        if(editorMap != null && !editorMap.isEmpty())
        {
            this.displayedQuelifiers.putAll(editorMap);
        }
        if(params != null && !params.isEmpty())
        {
            this.params.putAll(params);
        }
        if(hiddenQualifiers != null && !hiddenQualifiers.isEmpty())
        {
            this.hiddenQuelifiers.putAll(hiddenQualifiers);
        }
        if(this.groups != null && !this.groups.isEmpty())
        {
            this.groups.putAll(this.groups);
        }
    }


    public Map<WizardConfigurationFactory.WizardGroupConfiguration, List<String>> getGroups()
    {
        return this.groups;
    }


    public Map<String, String> getParameterMap(String propertyQualifier)
    {
        Map<String, String> paramMap = null;
        if(this.params != null && !this.params.isEmpty())
        {
            paramMap = this.params.get(propertyQualifier.toLowerCase(UISessionUtils.getCurrentSession().getLocale()));
        }
        return (paramMap == null) ? Collections.EMPTY_MAP : paramMap;
    }


    public Map<String, Map<String, String>> getParams()
    {
        return this.params;
    }


    public Map<String, String> getQualifiers()
    {
        Map<String, String> allQualifers = new HashMap<>();
        if(this.displayedQuelifiers != null && !this.displayedQuelifiers.isEmpty())
        {
            allQualifers.putAll(this.displayedQuelifiers);
        }
        if(this.hiddenQuelifiers != null && !this.hiddenQuelifiers.isEmpty())
        {
            allQualifers.putAll(this.hiddenQuelifiers);
        }
        return allQualifers;
    }


    public Map<String, String> getQualifiers(boolean visible)
    {
        if(visible)
        {
            return this.displayedQuelifiers;
        }
        return this.hiddenQuelifiers;
    }


    public Map<String, String> getUnboundProperties()
    {
        return this.unboundProperties;
    }


    public boolean isActivateAfterCreate()
    {
        return this.activateAfterCreate;
    }


    public boolean isCreateMode()
    {
        return this.createMode;
    }


    public boolean isDisplaySubtypes()
    {
        return this.displaySubtypes;
    }


    public boolean isSelectMode()
    {
        return this.selectMode;
    }


    public boolean isShowPrefilledValues()
    {
        return this.showPrefilledValues;
    }


    public void setActivateAfterCreate(boolean activateAfterCreate)
    {
        this.activateAfterCreate = activateAfterCreate;
    }


    public void setDisplaySubtypes(boolean displaySubtypes)
    {
        this.displaySubtypes = displaySubtypes;
    }


    public void setGroups(Map<WizardConfigurationFactory.WizardGroupConfiguration, List<String>> groups)
    {
        this.groups = groups;
    }


    public void setParams(Map<String, Map<String, String>> params)
    {
        this.params = params;
    }


    public void setUnboundProperties(Map<String, String> unboundProperties)
    {
        this.unboundProperties = unboundProperties;
    }


    public boolean isCreateWithinEditorArea()
    {
        return this.createWithinEditorArea;
    }


    public void setCreateWithinPopup(boolean createWithinPopup)
    {
        this.createWithinPopup = createWithinPopup;
    }


    public void setCreateWithinEditorArea(boolean createWithinEditorArea)
    {
        this.createWithinEditorArea = createWithinEditorArea;
    }


    public boolean isCreateWithinPopupEditorArea()
    {
        return this.createWithinPopup;
    }


    public void setValidationInfoIgnored(boolean validationInfoIgnored)
    {
        this.validationInfoIgnored = validationInfoIgnored;
    }


    public boolean isValidationInfoIgnored()
    {
        return this.validationInfoIgnored;
    }


    public AfterDoneWizardScript getWizardScript()
    {
        return this.wizardScript;
    }


    public void setWizardScript(AfterDoneWizardScript wizardScript)
    {
        this.wizardScript = wizardScript;
    }
}
