package de.hybris.platform.hac.data.dto;

public class TypeCodeTableEntry
{
    private Integer typecode;
    private String type;
    private String typePK;
    private String table;
    private String propsTable;
    private String extension;
    private Boolean persistent;
    private String deploymentName;
    private String superDeploymentName;
    private Boolean abstractValue;
    private Boolean finalValue;


    public String getDeploymentName()
    {
        return this.deploymentName;
    }


    public void setDeploymentName(String deploymentName)
    {
        this.deploymentName = deploymentName;
    }


    public String getSuperDeploymentName()
    {
        return this.superDeploymentName;
    }


    public void setSuperDeploymentName(String superDeploymentName)
    {
        this.superDeploymentName = superDeploymentName;
    }


    public Boolean getAbstractValue()
    {
        return this.abstractValue;
    }


    public void setAbstractValue(Boolean isAbstract)
    {
        this.abstractValue = isAbstract;
    }


    public Boolean getFinalValue()
    {
        return this.finalValue;
    }


    public void setFinalValue(Boolean isFinal)
    {
        this.finalValue = isFinal;
    }


    public Integer getTypecode()
    {
        return this.typecode;
    }


    public void setTypecode(Integer typecode)
    {
        this.typecode = typecode;
    }


    public String getType()
    {
        return this.type;
    }


    public void setType(String type)
    {
        this.type = type;
    }


    public String getTable()
    {
        return this.table;
    }


    public void setTable(String table)
    {
        this.table = table;
    }


    public String getPropsTable()
    {
        return this.propsTable;
    }


    public void setPropsTable(String propsTable)
    {
        this.propsTable = propsTable;
    }


    public String getExtension()
    {
        return this.extension;
    }


    public void setExtension(String extension)
    {
        this.extension = extension;
    }


    public Boolean getPersistent()
    {
        return this.persistent;
    }


    public void setPersistent(Boolean persisted)
    {
        this.persistent = persisted;
    }


    public String getTypePK()
    {
        return this.typePK;
    }


    public void setTypePK(String typePK)
    {
        this.typePK = typePK;
    }


    public String toString()
    {
        return "TypeCodeTableEntry{abstractValue=" + this.abstractValue + ", typecode=" + this.typecode + ", type='" + this.type + "', typePK='" + this.typePK + "', table='" + this.table + "', propsTable='" + this.propsTable + "', extension='" + this.extension + "', persistent=" + this.persistent
                        + ", deploymentName='" + this.deploymentName + "', superDeploymentName='" + this.superDeploymentName + "', finalValue=" + this.finalValue + "}";
    }
}
