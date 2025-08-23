package de.hybris.platform.admincockpit.services.impl;

import de.hybris.platform.admincockpit.services.ResourceConfiguration;
import de.hybris.platform.admincockpit.services.TypeAwareResourceResolver;
import de.hybris.platform.cockpit.model.meta.BaseType;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.session.UISessionUtils;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class ImageUriTypeAwareResourceResolver implements TypeAwareResourceResolver<String>
{
    private Map<String, String> typeMapping = new LinkedHashMap<>();
    private String defaultResource = null;


    @Required
    public void setConfiguration(List<ResourceConfiguration<String, String>> configuration)
    {
        this.typeMapping = new LinkedHashMap<>();
        for(ResourceConfiguration<String, String> entry : configuration)
        {
            this.typeMapping.put((String)entry.getKey(), (String)entry.getResource());
        }
    }


    public String getResourceForType(BaseType baseType)
    {
        for(String type : this.typeMapping.keySet())
        {
            if(UISessionUtils.getCurrentSession().getTypeService().getBaseType(type).isAssignableFrom((ObjectType)baseType))
            {
                return this.typeMapping.get(type);
            }
        }
        return getDefaultResource();
    }


    public String getDefaultResource()
    {
        return this.defaultResource;
    }


    @Required
    public void setDefaultResource(String defaultResource)
    {
        this.defaultResource = defaultResource;
    }
}
