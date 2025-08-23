package de.hybris.platform.persistence.type.update.strategy.oracle;

import de.hybris.platform.core.Registry;
import de.hybris.platform.persistence.type.AttributeDescriptorRemote;
import de.hybris.platform.persistence.type.update.strategy.ChangeColumnStrategy;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.jdbc.DBColumn;
import de.hybris.platform.util.jdbc.SchemaAnalyzer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class RawToBlobChangeStrategy implements ChangeColumnStrategy
{
    private static final Logger LOG = Logger.getLogger(RawToBlobChangeStrategy.class.getName());
    protected static final String SUFFIX = "_RAW_BLOB_MIG";


    public boolean doChangeColumn(String targetDefinition, DBColumn originalMetaData, AttributeDescriptorRemote attributeDescr)
    {
        if(isOracleRawToBlob(targetDefinition, originalMetaData))
        {
            return migrateRawToBlob(targetDefinition, originalMetaData, attributeDescr);
        }
        return false;
    }


    private boolean migrateRawToBlob(String targetDefinition, DBColumn originalMetaData, AttributeDescriptorRemote attributeDescr)
    {
        Connection conn = null;
        List<PreparedStatement> stmts = new ArrayList<>();
        try
        {
            conn = getConnection();
            conn.setAutoCommit(false);
            String catalog = conn.getCatalog();
            String tableName = getTableName(originalMetaData, catalog);
            String origColName = getOriginalColumnName(originalMetaData);
            String tempColName = origColName + "_RAW_BLOB_MIG";
            stmts.add(conn.prepareStatement("alter table " + tableName + " add " + tempColName + " " + targetDefinition));
            stmts.add(conn.prepareStatement("update " + tableName + " set " + tempColName + " = " + origColName));
            stmts.add(conn.prepareStatement("alter table " + tableName + " drop column " + origColName));
            stmts.add(conn.prepareStatement("alter table " + tableName + " rename column " + tempColName + " to " + origColName));
            for(PreparedStatement stmt : stmts)
            {
                stmt.execute();
            }
            conn.commit();
            return true;
        }
        catch(SQLException e)
        {
            try
            {
                if(conn != null)
                {
                    conn.rollback();
                }
            }
            catch(SQLException sqlException)
            {
                LOG.error("Rollback failed", sqlException);
            }
            LOG.warn("Automatic conversion from RAW to " + targetDefinition + " failed for attribute " + attributeDescr
                            .getEnclosingType()
                            .getCode() + "." + attributeDescr.getQualifier() + ". Error message is: " + e
                            .getMessage());
        }
        finally
        {
            for(PreparedStatement stmt : stmts)
            {
                Utilities.tryToCloseJDBC(null, stmt, null);
            }
            Utilities.tryToCloseJDBC(conn, null, null);
            SchemaAnalyzer.invalidateCache();
        }
        return false;
    }


    protected boolean isOracleRawToBlob(String targetDefinition, DBColumn originalMetaData)
    {
        return (Config.isOracleUsed() &&
                        StringUtils.equalsIgnoreCase(StringUtils.trim(originalMetaData.getSQLTypeDefinition()), "raw") &&
                        StringUtils.equalsIgnoreCase(StringUtils.trim(targetDefinition), "blob"));
    }


    protected String getTableName(DBColumn originalMetaData, String catalog)
    {
        return ((catalog == null) ? "" : (catalog + ".")) + ((catalog == null) ? "" : (catalog + "."));
    }


    protected String getOriginalColumnName(DBColumn originalMetaData)
    {
        return originalMetaData.getColumnName();
    }


    protected Connection getConnection() throws SQLException
    {
        return Registry.getCurrentTenant().getDataSource().getConnection();
    }
}
