package de.hybris.platform.cockpit.wizards.generic;

import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISessionUtils;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class CreateItemWizardRegistry implements ApplicationContextAware
{
    private ApplicationContext applicationContext;
    private final Map<String, Set<CreateWizardConfiguration>> typeWizardMapping = new HashMap<>();


    public void init()
    {
        Map<String, CreateWizardConfiguration> beansOfType = this.applicationContext.getBeansOfType(CreateWizardConfiguration.class);
        for(Map.Entry<String, CreateWizardConfiguration> bean : beansOfType.entrySet())
        {
            Set<CreateWizardConfiguration> set = this.typeWizardMapping.get(((CreateWizardConfiguration)bean.getValue()).getTemplateCode());
            if(set == null)
            {
                set = new HashSet<>();
                this.typeWizardMapping.put(((CreateWizardConfiguration)bean.getValue()).getTemplateCode(), set);
            }
            set.add(bean.getValue());
        }
    }


    public String getCustomWizardID(ObjectType type, UICockpitPerspective perspective, PropertyDescriptor propertyDescriptor)
    {
        return getCustomWizardID(type.getCode(), perspective, (propertyDescriptor == null) ? null : propertyDescriptor.getQualifier());
    }


    public String getCustomWizardID(String templateCode, UICockpitPerspective perspective, String propertyQualifier)
    {
        if(templateCode == null)
        {
            throw new IllegalArgumentException("TypeCode must not be null.");
        }
        PropertyDescriptor propertyDescriptor = null;
        if(propertyQualifier != null)
        {
            propertyDescriptor = UISessionUtils.getCurrentSession().getTypeService().getPropertyDescriptor(propertyQualifier);
        }
        String perspUid = null;
        if(perspective != null)
        {
            perspUid = perspective.getUid();
        }
        Set<CreateWizardConfiguration> configurations = this.typeWizardMapping.get(templateCode);
        if(configurations != null)
        {
            CreateWizardConfiguration fallback = null;
            for(CreateWizardConfiguration wizardConfiguration : configurations)
            {
                if(!wizardConfiguration.hasRestrictions())
                {
                    fallback = wizardConfiguration;
                    continue;
                }
                Set<String> restrictToPerspectives = wizardConfiguration.getRestrictToPerspectives();
                if(CollectionUtils.isNotEmpty(restrictToPerspectives) && perspUid != null &&
                                !wizardConfiguration.getRestrictToPerspectives().contains(perspUid))
                {
                    continue;
                }
                if(propertyDescriptor != null)
                {
                    Set<String> ignoreAtProperties = wizardConfiguration.getIgnoreAtProperties();
                    if(CollectionUtils.isNotEmpty(ignoreAtProperties) && ignoreAtProperties.contains(propertyQualifier))
                    {
                        continue;
                    }
                    Set<String> restrictToProperties = wizardConfiguration.getRestrictToProperties();
                    if(CollectionUtils.isNotEmpty(restrictToProperties) && !restrictToProperties.contains(propertyQualifier))
                    {
                        continue;
                    }
                }
                else if(CollectionUtils.isNotEmpty(wizardConfiguration.getRestrictToProperties()))
                {
                    continue;
                }
                return wizardConfiguration.getWizardBeanId();
            }
            if(fallback != null)
            {
                return fallback.getWizardBeanId();
            }
        }
        return null;
    }


    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        this.applicationContext = applicationContext;
    }
}
