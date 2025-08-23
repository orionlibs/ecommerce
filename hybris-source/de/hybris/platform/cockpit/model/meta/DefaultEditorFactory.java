package de.hybris.platform.cockpit.model.meta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class DefaultEditorFactory implements EditorFactory
{
    private Map<String, List<PropertyEditorDescriptor>> editorDescriptors;


    @Required
    public void setEditorDescriptors(List<PropertyEditorDescriptor> editorDescriptors)
    {
        this.editorDescriptors = new LinkedHashMap<>();
        if(editorDescriptors != null)
        {
            for(PropertyEditorDescriptor d : editorDescriptors)
            {
                String editorType = d.getEditorType().toLowerCase();
                List<PropertyEditorDescriptor> propertyEditorDescriptorList = this.editorDescriptors.get(editorType);
                if(propertyEditorDescriptorList == null)
                {
                    this.editorDescriptors.put(editorType, propertyEditorDescriptorList = new ArrayList<>());
                }
                propertyEditorDescriptorList.add(d);
            }
        }
    }


    public Collection<PropertyEditorDescriptor> getAllEditorDescriptors()
    {
        Collection<PropertyEditorDescriptor> ret = null;
        if(this.editorDescriptors != null && !this.editorDescriptors.isEmpty())
        {
            ret = new ArrayList<>();
            for(Map.Entry<String, List<PropertyEditorDescriptor>> e : this.editorDescriptors.entrySet())
            {
                ret.addAll(e.getValue());
            }
        }
        return (ret != null) ? ret : Collections.EMPTY_LIST;
    }


    public Collection<PropertyEditorDescriptor> getMatchingEditorDescriptors(String editorType)
    {
        List<PropertyEditorDescriptor> ret = null;
        if(this.editorDescriptors != null && !this.editorDescriptors.isEmpty())
        {
            List<PropertyEditorDescriptor> propertyEditorDescriptorList = this.editorDescriptors.get(editorType.toLowerCase());
            if(propertyEditorDescriptorList != null && !propertyEditorDescriptorList.isEmpty())
            {
                ret = Collections.unmodifiableList(propertyEditorDescriptorList);
            }
        }
        return (ret != null) ? ret : Collections.EMPTY_LIST;
    }
}
