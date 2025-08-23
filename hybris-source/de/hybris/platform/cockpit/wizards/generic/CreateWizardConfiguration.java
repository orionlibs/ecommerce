package de.hybris.platform.cockpit.wizards.generic;

import java.util.Collections;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;

public class CreateWizardConfiguration
{
    private String templateCode;
    private Set<String> restrictToPerspectives;
    private String wizardBeanId;
    private Set<String> restrictToProperties;
    private Set<String> ignoreAtProperties;


    public Set<String> getRestrictToProperties()
    {
        return this.restrictToProperties;
    }


    public void setRestrictToProperties(Set<String> restrictToProperties)
    {
        this.restrictToProperties = restrictToProperties;
    }


    public Set<String> getIgnoreAtProperties()
    {
        return this.ignoreAtProperties;
    }


    public void setIgnoreAtProperties(Set<String> ignoreAtProperties)
    {
        this.ignoreAtProperties = ignoreAtProperties;
    }


    public void setRestrictToPerspectives(Set<String> restrictToPerspectives)
    {
        this.restrictToPerspectives = restrictToPerspectives;
    }


    public Set<String> getRestrictToPerspectives()
    {
        return (this.restrictToPerspectives == null) ? Collections.EMPTY_SET : this.restrictToPerspectives;
    }


    public void setWizardBeanId(String wizardBeanId)
    {
        this.wizardBeanId = wizardBeanId;
    }


    public String getWizardBeanId()
    {
        return this.wizardBeanId;
    }


    public void setTemplateCode(String templateCode)
    {
        this.templateCode = templateCode;
    }


    public String getTemplateCode()
    {
        return this.templateCode;
    }


    public boolean hasRestrictions()
    {
        return (CollectionUtils.isNotEmpty(getIgnoreAtProperties()) || CollectionUtils.isNotEmpty(getRestrictToPerspectives()) ||
                        CollectionUtils.isNotEmpty(getRestrictToProperties()));
    }
}
