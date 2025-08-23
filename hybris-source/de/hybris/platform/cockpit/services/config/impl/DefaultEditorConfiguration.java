package de.hybris.platform.cockpit.services.config.impl;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.services.config.EditorConfiguration;
import de.hybris.platform.cockpit.services.config.EditorRowConfiguration;
import de.hybris.platform.cockpit.services.config.EditorSectionConfiguration;
import de.hybris.platform.cockpit.services.config.UIComponentConfigurationContext;
import java.util.ArrayList;
import java.util.List;

public class DefaultEditorConfiguration extends AbstractUIComponentConfiguration implements EditorConfiguration
{
    private List<EditorSectionConfiguration> sections;


    public List<EditorSectionConfiguration> getSections()
    {
        return this.sections;
    }


    public EditorConfiguration clone() throws CloneNotSupportedException
    {
        DefaultEditorConfiguration ret = (DefaultEditorConfiguration)super.clone();
        JaxbBasedUIComponentConfigurationContext context = null;
        JaxbBasedUIComponentConfigurationContext clonedContext = null;
        if(getContext() instanceof JaxbBasedUIComponentConfigurationContext)
        {
            context = (JaxbBasedUIComponentConfigurationContext)getContext();
            clonedContext = new JaxbBasedUIComponentConfigurationContext(context.getRootJaxbElement());
            ret.setContext((UIComponentConfigurationContext)clonedContext);
        }
        List<EditorSectionConfiguration> clonedSections = new ArrayList<>();
        for(EditorSectionConfiguration secConf : this.sections)
        {
            EditorSectionConfiguration clonedSec = secConf.clone();
            clonedSections.add(clonedSec);
            if(context != null)
            {
                clonedContext.registerJaxbElement(clonedSec, context.getJaxbElement(secConf));
            }
        }
        ret.setSections(clonedSections);
        return ret;
    }


    public void setSections(List<EditorSectionConfiguration> sections)
    {
        this.sections = sections;
    }


    public List<PropertyDescriptor> getAllPropertyDescriptors()
    {
        List<PropertyDescriptor> ret = new ArrayList<>();
        for(EditorSectionConfiguration sectionConfiguration : getSections())
        {
            for(EditorRowConfiguration rowConfiguration : sectionConfiguration.getSectionRows())
            {
                ret.add(rowConfiguration.getPropertyDescriptor());
            }
        }
        return ret;
    }
}
