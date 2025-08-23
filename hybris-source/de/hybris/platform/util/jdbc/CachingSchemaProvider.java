package de.hybris.platform.util.jdbc;

import de.hybris.platform.jalo.JaloSystemException;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.log4j.Logger;
import org.springframework.jdbc.support.JdbcUtils;

public class CachingSchemaProvider
{
    private static final Logger LOG = Logger.getLogger(SchemaAnalyzer.class);
    private static final String[] TABLE_TYPES = new String[] {"TABLE", "VIEW"};
    private final AtomicReference<List<TableSchemaData>> tableSchemas = new AtomicReference<>();
    private final AtomicReference<List<ColumnsData>> columnsData = new AtomicReference<>();


    public void invalidate()
    {
        this.tableSchemas.set(null);
        this.columnsData.set(null);
    }


    public List<TableSchemaData> getTables(DatabaseMetaData md, String schemaName)
    {
        assureInitialized(md, schemaName);
        return this.tableSchemas.get();
    }


    public List<ColumnsData> getColumns(DatabaseMetaData md, String schemaName)
    {
        assureInitialized(md, schemaName);
        return this.columnsData.get();
    }


    private void assureInitialized(DatabaseMetaData md, String schemaName)
    {
        assureTablesInitialized(md, schemaName);
        assureColumnsInitialized(md, schemaName);
    }


    private void assureColumnsInitialized(DatabaseMetaData md, String schemaName)
    {
        if(this.columnsData.get() == null)
        {
            ResultSet rs = null;
            try
            {
                rs = md.getColumns(null, schemaName, null, null);
                List<ColumnsData> computedColumnsData = new ArrayList<>();
                while(rs.next())
                {
                    ColumnsData cd = new ColumnsData();
                    cd.setTableName(rs.getString("TABLE_NAME"));
                    cd.setColumnName(rs.getString("COLUMN_NAME"));
                    cd.setDataType(Integer.valueOf(rs.getInt("DATA_TYPE")));
                    cd.setColumnSize(Integer.valueOf(rs.getInt("COLUMN_SIZE")));
                    cd.setDecimalDigits(Integer.valueOf(rs.getInt("DECIMAL_DIGITS")));
                    cd.setDefaultValue(rs.getString("COLUMN_DEF"));
                    cd.setNullableInt(Integer.valueOf(rs.getInt("NULLABLE")));
                    cd.setNullableStr(rs.getString("IS_NULLABLE"));
                    cd.setTypeName(rs.getString("TYPE_NAME"));
                    computedColumnsData.add(cd);
                }
                this.columnsData.compareAndSet(null, computedColumnsData);
            }
            catch(SQLException e)
            {
                LOG.error("Error loading existing table : " + e.getMessage(), e);
                throw new JaloSystemException(e);
            }
            finally
            {
                JdbcUtils.closeResultSet(rs);
            }
        }
    }


    private void assureTablesInitialized(DatabaseMetaData md, String schemaName)
    {
        if(this.tableSchemas.get() == null)
        {
            ResultSet rs = null;
            try
            {
                List<TableSchemaData> myTableSchemaData = new ArrayList<>();
                rs = md.getTables(null, schemaName, null, TABLE_TYPES);
                while(rs.next())
                {
                    String tbl = rs.getString("TABLE_NAME");
                    String tblSchema = rs.getString("TABLE_SCHEM");
                    myTableSchemaData.add(new TableSchemaData(tbl, tblSchema));
                }
                this.tableSchemas.compareAndSet(null, myTableSchemaData);
            }
            catch(SQLException e)
            {
                LOG.error("Error loading existing table : " + e.getMessage(), e);
                throw new JaloSystemException(e);
            }
            finally
            {
                JdbcUtils.closeResultSet(rs);
            }
        }
    }
}
