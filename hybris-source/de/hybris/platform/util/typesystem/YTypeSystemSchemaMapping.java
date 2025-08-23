package de.hybris.platform.util.typesystem;

import de.hybris.bootstrap.typesystem.YAttributeDeployment;
import de.hybris.bootstrap.typesystem.YAttributeDescriptor;
import de.hybris.bootstrap.typesystem.YDeployment;
import de.hybris.bootstrap.typesystem.YTypeSystem;
import de.hybris.platform.util.jdbc.DBColumn;
import de.hybris.platform.util.jdbc.DBSchema;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class YTypeSystemSchemaMapping
{
    private final YTypeSystem system;
    private final DBSchema schema;
    private final Map<YDeployment, Map<YAttributeDeployment, DBColumn>> attributeColumns;


    public YTypeSystemSchemaMapping(YTypeSystem system, DBSchema schema)
    {
        if(system == null)
        {
            throw new NullPointerException("system was null");
        }
        if(schema == null)
        {
            throw new NullPointerException("schema was null");
        }
        this.system = system;
        this.schema = schema;
        this.attributeColumns = new HashMap<>();
    }


    public YTypeSystem getSystem()
    {
        return this.system;
    }


    public DBSchema getSchema()
    {
        return this.schema;
    }


    protected void assertSchema(DBColumn col)
    {
        if(!this.schema.equals(col.getTable().getSchema()))
        {
            throw new IllegalArgumentException("column " + col + " doesnt belong to mapped schema " + getSchema() + " but " + col
                            .getTable().getSchema());
        }
    }


    protected void assertSystem(YAttributeDeployment depl)
    {
        if(!this.system.equals(depl.getTypeSystem()))
        {
            throw new IllegalArgumentException("attribute deployment " + depl + " doesnt belong to mapped system " + getSystem() + " but " + depl
                            .getTypeSystem());
        }
    }


    protected void assertSystem(YDeployment depl)
    {
        if(!this.system.equals(depl.getTypeSystem()))
        {
            throw new IllegalArgumentException("deployment " + depl + " doesnt belong to mapped system " + getSystem() + " but " + depl
                            .getTypeSystem());
        }
    }


    public void assignColumn(YDeployment deployment, YAttributeDeployment ad, DBColumn column)
    {
        assertSystem(deployment);
        assertSystem(ad);
        assertSchema(column);
        if(deployment.isAbstract())
        {
            throw new IllegalArgumentException("deployment " + deployment + " is abstract - cannot map columns for it");
        }
        Map<YAttributeDeployment, DBColumn> mappedColumns = this.attributeColumns.get(deployment);
        if(mappedColumns == null)
        {
            this.attributeColumns.put(deployment, mappedColumns = new HashMap<>());
        }
        DBColumn prev = mappedColumns.get(ad);
        if(prev != null && !prev.equals(column))
        {
            throw new IllegalArgumentException("attribute deployment " + ad + " had been previously assigned to column " + prev + " - cannot change into " + column);
        }
        mappedColumns.put(ad, column);
    }


    public DBColumn getColumn(YAttributeDescriptor ad)
    {
        YDeployment depl = ad.getEnclosingType().getDeployment();
        YAttributeDeployment aDepl = ad.getAttributeDeploymentOrFail();
        return (depl != null && aDepl != null) ? getColumn(depl, aDepl) : null;
    }


    public DBColumn getColumn(YDeployment deployment, YAttributeDeployment ad)
    {
        assertSystem(deployment);
        assertSystem(ad);
        Map<YAttributeDeployment, DBColumn> mappedColumns = this.attributeColumns.get(deployment);
        return (mappedColumns != null) ? mappedColumns.get(ad) : null;
    }


    public Map<YAttributeDeployment, DBColumn> getColumns(YDeployment depl)
    {
        Map<YAttributeDeployment, DBColumn> ret = this.attributeColumns.get(depl);
        return (ret != null) ? Collections.<YAttributeDeployment, DBColumn>unmodifiableMap(ret) : Collections.EMPTY_MAP;
    }
}
