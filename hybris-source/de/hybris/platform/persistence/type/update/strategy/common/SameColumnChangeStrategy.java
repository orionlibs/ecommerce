package de.hybris.platform.persistence.type.update.strategy.common;

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
import org.apache.log4j.Logger;

public class SameColumnChangeStrategy implements ChangeColumnStrategy
{
    private static final Logger LOG = Logger.getLogger(SameColumnChangeStrategy.class.getName());


    public boolean doChangeColumn(String targetDefinition, DBColumn originalMetaData, AttributeDescriptorRemote attributeDescr)
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = null;
        try
        {
            conn = Registry.getCurrentTenant().getDataSource().getConnection();
            if(Config.isHSQLDBUsed() || Config.isSQLServerUsed())
            {
                sql = "ALTER TABLE  " + originalMetaData.getTable().getName() + " ALTER COLUMN " + originalMetaData.getColumnName() + " " + targetDefinition;
            }
            else
            {
                String catalog = conn.getCatalog();
                sql = "ALTER TABLE  " + ((catalog == null) ? "" : (catalog + ".")) + originalMetaData.getTable().getName() + " MODIFY " + originalMetaData.getColumnName() + " " + targetDefinition;
            }
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Performing [" + Config.getDatabase() + "] finalize with command <<" + sql + ">>");
            }
            stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
            return true;
        }
        catch(SQLException e)
        {
            LOG.warn("Error while updating column type " + (
                            (attributeDescr == null) ? "" : ("of attribute " +
                                            attributeDescr.getEnclosingType().getCode() + "." + attributeDescr.getQualifier())) + " using query [" + sql + "]. Error message is: " + e
                            .getMessage());
            return false;
        }
        finally
        {
            Utilities.tryToCloseJDBC(conn, stmt, null);
            SchemaAnalyzer.invalidateCache();
        }
    }
}
