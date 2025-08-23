package de.hybris.bootstrap.typesystem.dto;

public class DeploymentDTO
{
    private final String extensionName;
    private final String packageName;
    private final String name;
    private final String superDeployment;
    private final int typeCode;
    private final boolean anAbstract;
    private final boolean generic;
    private final boolean aFinal;
    private final String tableName;
    private final String propsTableName;
    private final boolean nonItemDeployment;


    public DeploymentDTO(String extensionName, String packageName, String name, String superDeployment, int typeCode, boolean anAbstract, boolean generic, boolean aFinal, String tableName, String propsTableName, boolean nonItemDeployment)
    {
        this.extensionName = extensionName;
        this.packageName = packageName;
        this.name = name;
        this.superDeployment = superDeployment;
        this.typeCode = typeCode;
        this.anAbstract = anAbstract;
        this.generic = generic;
        this.aFinal = aFinal;
        this.tableName = tableName;
        this.propsTableName = propsTableName;
        this.nonItemDeployment = nonItemDeployment;
    }


    public String getExtensionName()
    {
        return this.extensionName;
    }


    public String getPackageName()
    {
        return this.packageName;
    }


    public String getName()
    {
        return this.name;
    }


    public String getSuperDeployment()
    {
        return this.superDeployment;
    }


    public int getTypeCode()
    {
        return this.typeCode;
    }


    public boolean isAbstract()
    {
        return this.anAbstract;
    }


    public boolean isGeneric()
    {
        return this.generic;
    }


    public boolean isFinal()
    {
        return this.aFinal;
    }


    public String getTableName()
    {
        return this.tableName;
    }


    public String getPropsTableName()
    {
        return this.propsTableName;
    }


    public boolean isNonItemDeployment()
    {
        return this.nonItemDeployment;
    }
}
