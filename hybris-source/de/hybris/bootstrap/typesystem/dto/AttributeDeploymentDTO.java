package de.hybris.bootstrap.typesystem.dto;

import de.hybris.bootstrap.typesystem.xml.BeanTagListener;
import java.util.Map;

public class AttributeDeploymentDTO
{
    private final String extensionName;
    private final String beanName;
    private final String qualifier;
    private final String type;
    private final boolean PK;
    private final Map<String, BeanTagListener.DeploymentAttributeMapping> columnMappings;


    public AttributeDeploymentDTO(String extensionName, String beanName, String qualifier, String type, boolean PK, Map<String, BeanTagListener.DeploymentAttributeMapping> columnMappings)
    {
        this.extensionName = extensionName;
        this.beanName = beanName;
        this.qualifier = qualifier;
        this.type = type;
        this.PK = PK;
        this.columnMappings = columnMappings;
    }


    public String getExtensionName()
    {
        return this.extensionName;
    }


    public String getBeanName()
    {
        return this.beanName;
    }


    public String getQualifier()
    {
        return this.qualifier;
    }


    public String getType()
    {
        return this.type;
    }


    public boolean isPK()
    {
        return this.PK;
    }


    public Map<String, BeanTagListener.DeploymentAttributeMapping> getColumnMappings()
    {
        return this.columnMappings;
    }
}
