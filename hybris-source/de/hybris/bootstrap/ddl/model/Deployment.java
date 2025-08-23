package de.hybris.bootstrap.ddl.model;

public class Deployment
{
    private String tableName;
    private String typecode;
    private String packageName;
    private String propsTableName;
    private String name;
    private String extensionName;


    public String getTableName()
    {
        return this.tableName;
    }


    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }


    public String getTypecode()
    {
        return this.typecode;
    }


    public void setTypecode(String typecode)
    {
        this.typecode = typecode;
    }


    public String getPackageName()
    {
        return this.packageName;
    }


    public void setPackageName(String packageName)
    {
        this.packageName = packageName;
    }


    public String getPropsTableName()
    {
        return this.propsTableName;
    }


    public void setPropsTableName(String propsTableName)
    {
        this.propsTableName = propsTableName;
    }


    public String getName()
    {
        return this.name;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getExtensionName()
    {
        return this.extensionName;
    }


    public void setExtensionName(String extensionName)
    {
        this.extensionName = extensionName;
    }
}
