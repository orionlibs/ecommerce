package de.hybris.platform.hac.data.dto;

import java.util.List;

public class DeploymentData
{
    private final int firstFreeTypeCode;
    private final List<TypeCodeTableEntry> typesWithDeployment;
    private final List<TypeCodeTableEntry> typesWithoutDeployment;
    private final List<TypeCodeTableEntry> deploymentsWithoutTypeCodes;


    private DeploymentData(Builder builder)
    {
        this.firstFreeTypeCode = builder.firstFreeTypeCode;
        this.typesWithDeployment = builder.typesWithDeployment;
        this.typesWithoutDeployment = builder.typesWithoutDeployment;
        this.deploymentsWithoutTypeCodes = builder.deploymentsWithoutTypeCodes;
    }


    public int getFirstFreeTypeCode()
    {
        return this.firstFreeTypeCode;
    }


    public List<TypeCodeTableEntry> getTypesWithDeployment()
    {
        return this.typesWithDeployment;
    }


    public List<TypeCodeTableEntry> getTypesWithoutDeployment()
    {
        return this.typesWithoutDeployment;
    }


    public List<TypeCodeTableEntry> getDeploymentsWithoutTypeCodes()
    {
        return this.deploymentsWithoutTypeCodes;
    }
}
