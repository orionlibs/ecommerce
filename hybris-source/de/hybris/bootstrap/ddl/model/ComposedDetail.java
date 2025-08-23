package de.hybris.bootstrap.ddl.model;

import de.hybris.bootstrap.util.LocaleHelper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComposedDetail
{
    private Deployment deployment;
    private final Map<String, AttributeDescriptor> attributeDescriptorMap = new HashMap<>();
    private ComposedType composedType;


    public Deployment getDeployment()
    {
        return this.deployment;
    }


    public void setDeployment(Deployment deployment)
    {
        this.deployment = deployment;
    }


    public Map<String, AttributeDescriptor> getAttributeDescriptors()
    {
        return this.attributeDescriptorMap;
    }


    public void setAttributeDescriptors(List<AttributeDescriptor> attributeDescriptors)
    {
        for(AttributeDescriptor attributeDescriptor : attributeDescriptors)
        {
            this.attributeDescriptorMap.put(attributeDescriptor.getName().toLowerCase(LocaleHelper.getPersistenceLocale()), attributeDescriptor);
        }
    }


    public AttributeDescriptor getAttributedescriptor(String name)
    {
        return this.attributeDescriptorMap.get(name.toLowerCase(LocaleHelper.getPersistenceLocale()));
    }


    public ComposedType getComposedType()
    {
        return this.composedType;
    }


    public void setComposedType(ComposedType composedType)
    {
        this.composedType = composedType;
    }
}
