package de.hybris.bootstrap.ddl.dbtypesystem.impl;

import de.hybris.bootstrap.util.LocaleHelper;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class DefaultSQLRowsProvider implements RowsProvider
{
    private static final Logger LOG = Logger.getLogger(DefaultSQLRowsProvider.class.getName());
    private static final String ATOMIC_TYPE = "AtomicType";
    private static final String ATTRIBUTE_DESCRIPTOR = "AttributeDescriptor";
    private static final String COLLECTION_TYPE = "CollectionType";
    private static final String COMPOSED_TYPE = "ComposedType";
    private static final String ENUMERATION_VALUE = "EnumerationValue";
    private static final String MAP_TYPE = "MapType";
    private static final String TYPE_SYSTEM_PROPS = "TypeSystemProps";
    private static final String QUERY_FOR_DEPLOYMENTS_TEMPLATE = "select * from %sydeployments where typesystemname=?";
    private static final String QUERY_FOR_ALL_ROWS_TEMPLATE = "select * from %s";
    private static final String QUERY_FOR_NUMBERSERIES_VALUES_TEMPLATE = "select * from %snumberseries where serieskey like ?";
    private static final String QUERY_FOR_PROPS_FALLBACK_TEMPLATE = "select * from %s where itemtypepk in(select pk from %scomposedtypes where itemJNDIName IN ('de.hybris.platform.persistence.type.ComposedType', 'de.hybris.platform.persistence.type.AtomicType','de.hybris.platform.persistence.type.CollectionType', 'de.hybris.platform.persistence.type.MapType','de.hybris.platform.persistence.type.AttributeDescriptor', 'de.hybris.platform.persistence.enumeration.EnumerationValue'))";
    private final JdbcTemplate jdbcTemplate;
    private final String tablePrefix;
    private final String typeSystemName;
    private final Map<String, String> deploymentToTable = new HashMap<>();


    public DefaultSQLRowsProvider(JdbcTemplate jdbcTemplate, String tablePrefix, String typeSystemName)
    {
        this.jdbcTemplate = Objects.<JdbcTemplate>requireNonNull(jdbcTemplate);
        this.tablePrefix = Objects.<String>requireNonNull(tablePrefix);
        this.typeSystemName = Objects.<String>requireNonNull(typeSystemName);
    }


    private static Boolean asBoolean(ResultSet rs, String field) throws SQLException
    {
        int i = rs.getInt(field);
        return rs.wasNull() ? Boolean.FALSE : Boolean.valueOf((i != 0));
    }


    private static Boolean asBooleanWithDefault(ResultSet rs, String field, Boolean defaultValue)
    {
        try
        {
            return asBoolean(rs, field);
        }
        catch(SQLException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug(e);
            }
            return defaultValue;
        }
    }


    private static Long asLongWithDefault(ResultSet rs, String field, Long defaultValue) throws SQLException
    {
        long i = rs.getLong(field);
        return rs.wasNull() ? defaultValue : Long.valueOf(i);
    }


    private static Integer asInteger(ResultSet rs, String field) throws SQLException
    {
        int i = rs.getInt(field);
        return rs.wasNull() ? Integer.valueOf(0) : Integer.valueOf(i);
    }


    private static Long asLong(ResultSet rs, String field) throws SQLException
    {
        return asLongWithDefault(rs, field, Long.valueOf(0L));
    }


    private static String asString(ResultSet rs, String field) throws SQLException
    {
        return rs.getString(field);
    }


    private static boolean hasColumn(ResultSet rs, String columnName) throws SQLException
    {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columns = rsmd.getColumnCount();
        for(int x = 1; x <= columns; x++)
        {
            if(columnName.equalsIgnoreCase(rsmd.getColumnName(x)))
            {
                return true;
            }
        }
        return false;
    }


    public Iterable<DeploymentRow> getDeploymentRows()
    {
        return this.jdbcTemplate.query(String.format("select * from %sydeployments where typesystemname=?", new Object[] {this.tablePrefix}), new Object[] {this.typeSystemName}, (RowMapper)
                        getDeploymentRowMapper());
    }


    public Iterable<TypeRow> getTypeRows()
    {
        String query = String.format("select * from %s", new Object[] {getTableForDeployment("ComposedType")});
        return this.jdbcTemplate.query(query, (RowMapper)getTypeRowMapper());
    }


    public Iterable<AttributeRow> getAttributeRows()
    {
        String query = String.format("select * from %s", new Object[] {getTableForDeployment("AttributeDescriptor")});
        return this.jdbcTemplate.query(query, (RowMapper)getAttributeRowMapper());
    }


    public Iterable<AtomicTypeRow> getAtomicTypeRows()
    {
        String query = String.format("select * from %s", new Object[] {getTableForDeployment("AtomicType")});
        return this.jdbcTemplate.query(query, (RowMapper)getAtomicRowMapper());
    }


    public Iterable<CollectionTypeRow> getCollectionTypeRow()
    {
        String query = String.format("select * from %s", new Object[] {getTableForDeployment("CollectionType")});
        return this.jdbcTemplate.query(query, (RowMapper)getCollectionRowMapper());
    }


    public Iterable<MapTypeRow> getMapTypeRows()
    {
        String query = String.format("select * from %s", new Object[] {getTableForDeployment("MapType")});
        return this.jdbcTemplate.query(query, (RowMapper)getMapRowMapper());
    }


    public Iterable<EnumerationValueRow> getEnumerationValueRows()
    {
        String query = String.format("select * from %s", new Object[] {getTableForDeployment("EnumerationValue")});
        return this.jdbcTemplate.query(query, (RowMapper)getEnumRowMapper());
    }


    public Iterable<NumberSeriesRow> getNumberSeriesRows()
    {
        return this.jdbcTemplate.query(String.format("select * from %snumberseries where serieskey like ?", new Object[] {this.tablePrefix}), new Object[] {"pk_%"}, (RowMapper)
                        getNumberSeriesRowMapper());
    }


    public Iterable<PropsRow> getPropsRows()
    {
        String query, typeSystemPropsTable = getTableForDeployment("TypeSystemProps");
        if(typeSystemPropsTable.toLowerCase(LocaleHelper.getPersistenceLocale()).contains("typesystemprops"))
        {
            query = String.format("select * from %s", new Object[] {typeSystemPropsTable});
        }
        else
        {
            LOG.warn("Unexpected type system props table '" + typeSystemPropsTable + "' - using fallback query for filtering non-type system properties!");
            query = String.format(
                            "select * from %s where itemtypepk in(select pk from %scomposedtypes where itemJNDIName IN ('de.hybris.platform.persistence.type.ComposedType', 'de.hybris.platform.persistence.type.AtomicType','de.hybris.platform.persistence.type.CollectionType', 'de.hybris.platform.persistence.type.MapType','de.hybris.platform.persistence.type.AttributeDescriptor', 'de.hybris.platform.persistence.enumeration.EnumerationValue'))",
                            new Object[] {typeSystemPropsTable, this.tablePrefix});
        }
        return this.jdbcTemplate.query(query, (RowMapper)getPropsRowMapper());
    }


    protected TypeRowMapper getTypeRowMapper()
    {
        return new TypeRowMapper();
    }


    protected DeploymentRowMapper getDeploymentRowMapper()
    {
        return new DeploymentRowMapper();
    }


    protected AttributeRowMapper getAttributeRowMapper()
    {
        return new AttributeRowMapper();
    }


    protected AtomicTypeRowMapper getAtomicRowMapper()
    {
        return new AtomicTypeRowMapper();
    }


    protected CollectionTypeRowMapper getCollectionRowMapper()
    {
        return new CollectionTypeRowMapper();
    }


    protected MapTypeRowMapper getMapRowMapper()
    {
        return new MapTypeRowMapper();
    }


    protected EnumerationValueRowMapper getEnumRowMapper()
    {
        return new EnumerationValueRowMapper();
    }


    protected NumberSeriesValueRowMapper getNumberSeriesRowMapper()
    {
        return new NumberSeriesValueRowMapper();
    }


    protected PropsRowMapper getPropsRowMapper()
    {
        return new PropsRowMapper();
    }


    private String getTableForDeployment(String deploymentName)
    {
        assureMappingOfDeploymentToTable();
        return this.deploymentToTable.get(deploymentName);
    }


    private void assureMappingOfDeploymentToTable()
    {
        if(!this.deploymentToTable.isEmpty())
        {
            return;
        }
        Map<String, String> mappings = new HashMap<>();
        for(DeploymentRow deployment : getDeploymentRows())
        {
            mappings.put(deployment.getName(), this.tablePrefix + this.tablePrefix);
            if("ComposedType".equals(deployment.getName()))
            {
                mappings.put("TypeSystemProps", this.tablePrefix + this.tablePrefix);
            }
        }
        this.deploymentToTable.putAll(mappings);
    }
}
