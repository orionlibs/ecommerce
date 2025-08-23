package de.hybris.bootstrap.typesystem;

import de.hybris.bootstrap.ddl.DatabaseSettings;
import java.util.Map;
import org.apache.commons.collections.map.CaseInsensitiveMap;

public class YAttributeDeployment extends YDeploymentElement
{
    private final String persistenceQualifier;
    private final YAttributeDescriptor attDesc;
    private final String javaType;
    private final TableType tableType;
    private boolean primaryKey = false;
    private Map<String, ColumnMapping> persistenceMappings;


    public YAttributeDeployment(YAttributeDescriptor attDesc)
    {
        this(attDesc.getNamespace(), attDesc.getEnclosingType().getDeployment().getName(), attDesc.getPersistenceQualifier(), attDesc
                        .getColumnType().getJavaClass().getName(), attDesc.isLocalized() ? TableType.LOC : TableType.CORE, attDesc);
    }


    public YAttributeDeployment(YNamespace container, String deploymentName, String persistenceQualifier, String javaType, TableType tableType)
    {
        this(container, deploymentName, persistenceQualifier, javaType, tableType, null);
    }


    protected YAttributeDeployment(YNamespace container, String deploymentName, String persistenceQualifier, String javaType, TableType tableType, YAttributeDescriptor attDesc)
    {
        super(container, deploymentName);
        this.persistenceQualifier = persistenceQualifier;
        this.javaType = javaType;
        this.tableType = tableType;
        this.attDesc = attDesc;
        if(attDesc != null)
        {
            for(Map.Entry<String, String> entry : (Iterable<Map.Entry<String, String>>)attDesc.getDbColumnDefinitions().entrySet())
            {
                addPersistenceMapping(entry.getKey(), attDesc
                                .getRealColumnName(), entry
                                .getValue(), attDesc
                                .isOptional());
            }
        }
    }


    public String toString()
    {
        return getPersistenceQualifier() + "->" + getPersistenceQualifier();
    }


    public YAttributeDescriptor getAttributeDescriptor()
    {
        return this.attDesc;
    }


    public boolean isPrimaryKey()
    {
        return this.primaryKey;
    }


    public void setPrimaryKey(boolean isPrimaryKey)
    {
        this.primaryKey = isPrimaryKey;
    }


    public String getJavaTypeName()
    {
        return this.javaType;
    }


    public Class getJavaType()
    {
        return getTypeSystem().resolveClass(this, YDeployment.convertNativeTypes(getJavaTypeName()));
    }


    public String getPersistenceQualifier()
    {
        return (this.persistenceQualifier != null) ? this.persistenceQualifier : ((this.attDesc != null) ?
                        this.attDesc.getPersistenceQualifier() : null);
    }


    public String getAttributeQualifier()
    {
        return (this.attDesc != null) ? this.attDesc.getQualifier() : this.persistenceQualifier;
    }


    protected String getMappedColumnName(String dbName)
    {
        ColumnMapping mapping = getPersistenceMapping(dbName);
        return (mapping != null) ? mapping.getColumnName() : null;
    }


    protected String getAttributeColumnName()
    {
        return (this.attDesc != null) ? this.attDesc.getRealColumnName() : null;
    }


    public String getProposedColumnName(String dbName)
    {
        return (this.attDesc != null) ? ("p_" + this.attDesc.getQualifier()) : getRealColumnName(dbName);
    }


    public String getRealColumnName(String dbName)
    {
        String col = getAttributeColumnName();
        if(col == null)
        {
            col = getMappedColumnName(dbName);
        }
        return col;
    }


    public String getSqlDefinition(String dbName)
    {
        ColumnMapping mapping = getPersistenceMapping(dbName);
        String def = (mapping != null) ? mapping.getSqlDefinition() : null;
        return (def != null) ? def : getDefaultSqlDefinition(dbName);
    }


    protected String getDefaultSqlDefinition(String dbName)
    {
        YDBTypeMapping dbMapping = getTypeSystem().getDBTypeMappings(dbName);
        return dbMapping.getTypeMapping(getJavaTypeName());
    }


    public boolean isNullable(String dbName)
    {
        ColumnMapping mapping = getPersistenceMapping(dbName);
        return (mapping == null || mapping.isNullable());
    }


    public ColumnMapping getPersistenceMapping(String dbName)
    {
        if(this.persistenceMappings == null)
        {
            return null;
        }
        ColumnMapping ret = this.persistenceMappings.get(dbName);
        return (ret != null) ? ret : this.persistenceMappings.get(null);
    }


    public final void addPersistenceMapping(String dbName, String columnName, String sqlType, boolean nullable)
    {
        if(this.persistenceMappings == null)
        {
            this.persistenceMappings = (Map<String, ColumnMapping>)new CaseInsensitiveMap();
        }
        this.persistenceMappings.put(dbName, new ColumnMapping(this, columnName, sqlType, nullable));
    }


    public TableType getTableType()
    {
        return this.tableType;
    }


    public String getColumnName(DatabaseSettings databaseSettings)
    {
        String columnName = getRealColumnName(databaseSettings.getDataBaseProvider().getDbName());
        if(columnName == null)
        {
            throw new IllegalStateException("found attribute deployment without column name : " + this);
        }
        return columnName;
    }
}
