package de.hybris.platform.cockpit.model.meta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class LookupEditorFactory implements EditorFactory, ApplicationContextAware
{
    private ApplicationContext applicationContext;
    private DefaultEditorFactory editorFactory;


    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        this.applicationContext = applicationContext;
    }


    public void init()
    {
        List<PropertyEditorDescriptor> parentDescriptors = new ArrayList<>(this.editorFactory.getAllEditorDescriptors());
        Map<String, PropertyEditorDescriptor> parentDescriptorMapping = new HashMap<>();
        for(PropertyEditorDescriptor desc : parentDescriptors)
        {
            parentDescriptorMapping.put(desc.getEditorType(), desc);
        }
        Map<String, PropertyEditorDescriptor> beanMap = BeanFactoryUtils.beansOfTypeIncludingAncestors((ListableBeanFactory)this.applicationContext, PropertyEditorDescriptor.class);
        Map<String, PropertyEditorDescriptor> descriptorMapping = new HashMap<>();
        for(PropertyEditorDescriptor desc : beanMap.values())
        {
            String editorType = desc.getEditorType();
            if(parentDescriptorMapping.containsKey(editorType))
            {
                PropertyEditorDescriptor parentDesc = parentDescriptorMapping.get(editorType);
                if(parentDesc != null)
                {
                    parentDescriptorMapping.remove(editorType);
                    descriptorMapping.put(editorType, merge(parentDesc, desc));
                }
                continue;
            }
            if(descriptorMapping.containsKey(editorType))
            {
                PropertyEditorDescriptor master = descriptorMapping.get(editorType);
                descriptorMapping.put(editorType, merge(master, desc));
                continue;
            }
            descriptorMapping.put(editorType, desc);
        }
        for(Map.Entry<String, PropertyEditorDescriptor> entry : parentDescriptorMapping.entrySet())
        {
            descriptorMapping.put(entry.getKey(), entry.getValue());
        }
        this.editorFactory.setEditorDescriptors(new ArrayList(descriptorMapping.values()));
    }


    protected PropertyEditorDescriptor merge(PropertyEditorDescriptor master, PropertyEditorDescriptor update)
    {
        if(master instanceof DefaultPropertyEditorDescriptor && update instanceof DefaultPropertyEditorDescriptor)
        {
            DefaultPropertyEditorDescriptor masterDesc = (DefaultPropertyEditorDescriptor)master;
            DefaultPropertyEditorDescriptor updateDesc = (DefaultPropertyEditorDescriptor)update;
            if(!StringUtils.isEmpty(updateDesc.getDefaultEditor()))
            {
                masterDesc.setDefaultEditor(updateDesc.getDefaultEditor());
            }
            if(!StringUtils.isEmpty(updateDesc.getDefaultMode()))
            {
                masterDesc.setDefaultMode(updateDesc.getDefaultMode());
            }
            if(!StringUtils.isEmpty(updateDesc.getDefaultSearchMode()))
            {
                masterDesc.setDefaultSearchMode(updateDesc.getDefaultSearchMode());
            }
            if(!StringUtils.isEmpty(updateDesc.getLabel()))
            {
                masterDesc.setLabel(updateDesc.getLabel());
            }
            if(!StringUtils.isEmpty(updateDesc.getPreviewImage()))
            {
                masterDesc.setPreviewImage(updateDesc.getPreviewImage());
            }
            Map<String, String> editors = updateDesc.getEditors();
            if(editors != null)
            {
                Map<String, String> newEditors = new HashMap<>(masterDesc.getEditors());
                for(Map.Entry<String, String> entry : editors.entrySet())
                {
                    newEditors.put(entry.getKey(), entry.getValue());
                }
                masterDesc.setEditors(newEditors);
            }
            return master;
        }
        return update;
    }


    public Collection<PropertyEditorDescriptor> getMatchingEditorDescriptors(String editorType)
    {
        return this.editorFactory.getMatchingEditorDescriptors(editorType);
    }


    public Collection<PropertyEditorDescriptor> getAllEditorDescriptors()
    {
        return this.editorFactory.getAllEditorDescriptors();
    }


    public void setEditorFactory(DefaultEditorFactory editorFactory)
    {
        this.editorFactory = editorFactory;
    }
}
