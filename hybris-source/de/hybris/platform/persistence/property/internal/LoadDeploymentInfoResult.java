package de.hybris.platform.persistence.property.internal;

import de.hybris.platform.core.ItemDeployment;
import java.util.Map;

public class LoadDeploymentInfoResult
{
    private final Map<String, ItemDeployment> deploymentInfos;
    private final Map<Integer, String> typeCode2JndiMappings;
    private final Map<String, String> javaDefMapping;


    public LoadDeploymentInfoResult(Map<String, ItemDeployment> deploymentInfos, Map<Integer, String> typeCode2JndiMappings, Map<String, String> javaDefMapping)
    {
        this.deploymentInfos = deploymentInfos;
        this.typeCode2JndiMappings = typeCode2JndiMappings;
        this.javaDefMapping = javaDefMapping;
    }


    public Map<String, ItemDeployment> getDeploymentInfos()
    {
        return this.deploymentInfos;
    }


    public Map<Integer, String> getTypeCode2JndiMappings()
    {
        return this.typeCode2JndiMappings;
    }


    public Map<String, String> getJavaDefMapping()
    {
        return this.javaDefMapping;
    }
}
