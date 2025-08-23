package de.hybris.bootstrap.ddl.tools.persistenceinfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class DeploymentRow
{
    private final Map<String, Object> rowValues;


    public DeploymentRow(Map<String, Object> rowValues)
    {
        this.rowValues = Objects.<Map<String, Object>>requireNonNull(rowValues);
    }


    public Object getColumnValue(String columnName)
    {
        return this.rowValues.get(Objects.requireNonNull(columnName));
    }


    public Set<String> getColumns()
    {
        return this.rowValues.keySet();
    }


    public DeploymentRow withChangedTypeSystem(String typeSystemName)
    {
        Map<String, Object> newValues = new HashMap<>(this.rowValues);
        newValues.put("typesystemname", typeSystemName);
        return new DeploymentRow(newValues);
    }


    public DeploymentRow withChangedTableName(String tableName)
    {
        Map<String, Object> newValues = new HashMap<>(this.rowValues);
        newValues.put("tablename", tableName);
        return new DeploymentRow(newValues);
    }


    public DeploymentRow withChangedPropsTableName(String propsTableName)
    {
        Map<String, Object> newValues = new HashMap<>(this.rowValues);
        newValues.put("propstablename", propsTableName);
        return new DeploymentRow(newValues);
    }


    public boolean isTypeSystemRelated()
    {
        return PersistenceInformation.isTypeSystemRelatedDeployment((String)getColumnValue("name"));
    }


    public String getTableName()
    {
        return (String)getColumnValue("tablename");
    }


    public String getPropsTableName()
    {
        return (String)getColumnValue("propstablename");
    }
}
