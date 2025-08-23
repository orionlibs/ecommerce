package de.hybris.platform.persistence.security;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.threadregistry.OperationInfo;
import de.hybris.platform.core.threadregistry.RevertibleUpdate;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jdbcwrapper.HybrisDataSource;
import de.hybris.platform.persistence.EJBInternalException;
import de.hybris.platform.persistence.meta.MetaInformationEJB;
import de.hybris.platform.persistence.property.JDBCValueMappings;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Utilities;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import org.apache.log4j.Logger;

public class ACLEntryJDBC
{
    public static final String PERMISSION = "PermissionPK";
    public static final String NEGATIVE = "Negative";
    public static final String PRINCIPAL = "PrincipalPK";
    public static final String ITEM = "ItemPK";


    public static final String ACLENTRIES_TABLE()
    {
        return Config.getString("db.tableprefix", "") + "aclentries";
    }


    private static final Logger log = Logger.getLogger(ACLEntryJDBC.class);


    public static final ACLCache getACLCache(PK itemPK, long version, HybrisDataSource ds)
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
            RevertibleUpdate theUpdate = OperationInfo.updateThread(OperationInfo.builder().withCategory(OperationInfo.Category.SYSTEM).build());
        }
        catch(SQLException e)
        {
            throw new EJBInternalException(e, "error reading acl entries " + e, 0);
        }
        finally
        {
            Utilities.tryToCloseJDBC(conn, pstmt, rs);
        }
    }


    public static final ACLCache getGlobalACLCache(PK principalPK, long version, HybrisDataSource ds)
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
            RevertibleUpdate theUpdate = OperationInfo.updateThread(OperationInfo.builder().withCategory(OperationInfo.Category.SYSTEM).build());
        }
        catch(SQLException e)
        {
            throw new EJBInternalException(e, "error reading acl entries " + e, 0);
        }
        finally
        {
            Utilities.tryToCloseJDBC(conn, pstmt, rs);
        }
    }


    public static final void writeCache(ACLCache cache, HybrisDataSource ds)
    {
        Connection conn = null;
        PreparedStatement insertStmt = null;
        PreparedStatement updateStmt = null;
        PreparedStatement removeStmt = null;
        StringBuilder query = null;
        PK itemPK = cache.getItemPK();
        try
        {
            RevertibleUpdate theUpdate = OperationInfo.updateThread(OperationInfo.builder().withCategory(OperationInfo.Category.SYSTEM).build());
            try
            {
                conn = ds.getConnection();
                for(Iterator<ACLEntryCache> it = cache.getUpdateableACLs().iterator(); it.hasNext(); )
                {
                    PreparedStatement stmt;
                    ACLEntryCache ec = it.next();
                    if(!ec.isInDatabase())
                    {
                        if(insertStmt == null)
                        {
                            insertStmt = conn.prepareStatement(INSERT_QUERY());
                        }
                        fillInsertQuery(insertStmt, itemPK, ec);
                        stmt = insertStmt;
                    }
                    else if(ec.getNegative() == null)
                    {
                        if(removeStmt == null)
                        {
                            removeStmt = conn.prepareStatement(REMOVE_QUERY());
                        }
                        fillRemoveQuery(removeStmt, itemPK, ec);
                        stmt = removeStmt;
                    }
                    else
                    {
                        if(updateStmt == null)
                        {
                            updateStmt = conn.prepareStatement(UPDATE_QUERY());
                        }
                        fillUpdateQuery(updateStmt, itemPK, ec);
                        stmt = updateStmt;
                    }
                    int changed = stmt.executeUpdate();
                    if(changed != 1)
                    {
                        throw new EJBInternalException(null, "unexpected result count " + changed + " for entry " + ec + " of cache " + cache + " for item " + itemPK + " , query = " + query, 0);
                    }
                }
                if(theUpdate != null)
                {
                    theUpdate.close();
                }
            }
            catch(Throwable throwable)
            {
                if(theUpdate != null)
                {
                    try
                    {
                        theUpdate.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                }
                throw throwable;
            }
        }
        catch(SQLException e)
        {
            throw new JaloSystemException(e, "sql error saving acl cache " + cache + " for item " + itemPK + " , query = " + query + ", SQLException was: " + e, 0);
        }
        finally
        {
            if(insertStmt != null)
            {
                try
                {
                    insertStmt.close();
                }
                catch(SQLException e)
                {
                    if(log.isDebugEnabled())
                    {
                        log.debug(e.getMessage());
                    }
                }
            }
            if(removeStmt != null)
            {
                try
                {
                    removeStmt.close();
                }
                catch(SQLException e)
                {
                    if(log.isDebugEnabled())
                    {
                        log.debug(e.getMessage());
                    }
                }
            }
            if(updateStmt != null)
            {
                try
                {
                    updateStmt.close();
                }
                catch(SQLException e)
                {
                    if(log.isDebugEnabled())
                    {
                        log.debug(e.getMessage());
                    }
                }
            }
            if(conn != null)
            {
                try
                {
                    conn.close();
                }
                catch(SQLException e)
                {
                    if(log.isDebugEnabled())
                    {
                        log.debug(e.getMessage());
                    }
                }
            }
            cache.wroteChanges();
        }
    }


    public static final void removeAllEntries(PK itemPK, HybrisDataSource ds)
    {
        PreparedStatement stmt = null;
        Connection conn = null;
        try
        {
            RevertibleUpdate theUpdate = OperationInfo.updateThread(OperationInfo.builder().withCategory(OperationInfo.Category.SYSTEM).build());
            try
            {
                conn = ds.getConnection();
                stmt = conn.prepareStatement(REMOVE_ALL_QUERY());
                fillSelectOrRemoveAllQuery(stmt, itemPK);
                stmt.executeUpdate();
                if(theUpdate != null)
                {
                    theUpdate.close();
                }
            }
            catch(Throwable throwable)
            {
                if(theUpdate != null)
                {
                    try
                    {
                        theUpdate.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                }
                throw throwable;
            }
        }
        catch(SQLException e)
        {
            throw new JaloSystemException(e);
        }
        finally
        {
            Utilities.tryToCloseJDBC(conn, stmt, null);
        }
    }


    public static final void removeAllGlobalEntries(PK principalPK, HybrisDataSource ds)
    {
        PreparedStatement stmt = null;
        Connection conn = null;
        try
        {
            RevertibleUpdate theUpdate = OperationInfo.updateThread(OperationInfo.builder().withCategory(OperationInfo.Category.SYSTEM).build());
            try
            {
                conn = ds.getConnection();
                stmt = conn.prepareStatement(REMOVE_ALL_GLOBAL_QUERY());
                fillSelectOrRemoveAllGlobalQuery(stmt, principalPK);
                stmt.executeUpdate();
                if(theUpdate != null)
                {
                    theUpdate.close();
                }
            }
            catch(Throwable throwable)
            {
                if(theUpdate != null)
                {
                    try
                    {
                        theUpdate.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                }
                throw throwable;
            }
        }
        catch(SQLException e)
        {
            throw new JaloSystemException(e);
        }
        finally
        {
            Utilities.tryToCloseJDBC(conn, stmt, null);
        }
    }


    public static final void removeAllEntriesForUserright(PK userrightPK, HybrisDataSource ds)
    {
        PreparedStatement stmt = null;
        Connection conn = null;
        try
        {
            RevertibleUpdate theUpdate = OperationInfo.updateThread(OperationInfo.builder().withCategory(OperationInfo.Category.SYSTEM).build());
            try
            {
                conn = ds.getConnection();
                stmt = conn.prepareStatement(REMOVE_ALL_QUERY_FOR_USERRIGHT());
                fillSelectOrRemoveAllQuery(stmt, userrightPK);
                stmt.executeUpdate();
                if(theUpdate != null)
                {
                    theUpdate.close();
                }
            }
            catch(Throwable throwable)
            {
                if(theUpdate != null)
                {
                    try
                    {
                        theUpdate.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                }
                throw throwable;
            }
        }
        catch(SQLException e)
        {
            throw new JaloSystemException(e);
        }
        finally
        {
            Utilities.tryToCloseJDBC(conn, stmt, null);
        }
    }


    private static final ACLEntryCache readRow(ResultSet rs, PK itemPK) throws SQLException
    {
        JDBCValueMappings vm = JDBCValueMappings.getInstance();
        JDBCValueMappings.ValueReader<PK, ?> pkReader = vm.PK_READER;
        JDBCValueMappings.ValueReader boolVR = vm.getValueReader(Boolean.class);
        try
        {
            return ACLEntryCache.load((PK)pkReader.getValue(rs, "PrincipalPK"), (PK)pkReader.getValue(rs, "PermissionPK"), boolVR.getBoolean(rs, "Negative"));
        }
        catch(Exception e)
        {
            log.error("Error reading entry for item " + itemPK, e);
            if(e instanceof SQLException)
            {
                throw (SQLException)e;
            }
            throw new JaloSystemException(e);
        }
    }


    private static final String UPDATE_QUERY()
    {
        return "UPDATE " + ACLENTRIES_TABLE() + " SET Negative=? WHERE ItemPK=? AND PrincipalPK=? AND PermissionPK=?";
    }


    private static final void fillUpdateQuery(PreparedStatement stmt, PK itemPK, ACLEntryCache ec) throws SQLException
    {
        JDBCValueMappings vm = JDBCValueMappings.getInstance();
        JDBCValueMappings.ValueWriter<PK, ?> pkWriter = vm.PK_WRITER;
        vm.getValueWriter(Boolean.class).setValue(stmt, 1, ec.getNegative());
        pkWriter.setValue(stmt, 2, itemPK);
        pkWriter.setValue(stmt, 3, ec.getPrincipal());
        pkWriter.setValue(stmt, 4, ec.getPermission());
    }


    private static final String REMOVE_QUERY()
    {
        return "DELETE FROM " + ACLENTRIES_TABLE() + " WHERE ItemPK=? AND PrincipalPK=? AND PermissionPK=?";
    }


    private static final void fillRemoveQuery(PreparedStatement stmt, PK itemPK, ACLEntryCache ec) throws SQLException
    {
        JDBCValueMappings vm = JDBCValueMappings.getInstance();
        JDBCValueMappings.ValueWriter<PK, ?> pkWriter = vm.PK_WRITER;
        pkWriter.setValue(stmt, 1, itemPK);
        pkWriter.setValue(stmt, 2, ec.getPrincipal());
        pkWriter.setValue(stmt, 3, ec.getPermission());
    }


    private static final String INSERT_QUERY()
    {
        return "INSERT INTO " + ACLENTRIES_TABLE() + " (ItemPK,PrincipalPK,PermissionPK,Negative) VALUES ( ?,?,?,? )";
    }


    private static final void fillInsertQuery(PreparedStatement stmt, PK itemPK, ACLEntryCache ec) throws SQLException
    {
        JDBCValueMappings vm = JDBCValueMappings.getInstance();
        JDBCValueMappings.ValueWriter<PK, ?> pkWriter = vm.PK_WRITER;
        pkWriter.setValue(stmt, 1, itemPK);
        pkWriter.setValue(stmt, 2, ec.getPrincipal());
        pkWriter.setValue(stmt, 3, ec.getPermission());
        vm.getValueWriter(Boolean.class).setValue(stmt, 4, ec.getNegative());
    }


    private static final String REMOVE_ALL_QUERY_FOR_USERRIGHT()
    {
        return "DELETE FROM " + ACLENTRIES_TABLE() + " WHERE PermissionPK=?";
    }


    private static final String SELECT_QUERY()
    {
        return "SELECT * FROM " + ACLENTRIES_TABLE() + " WHERE ItemPK=?";
    }


    private static final String REMOVE_ALL_QUERY()
    {
        return "DELETE FROM " + ACLENTRIES_TABLE() + " WHERE ItemPK=?";
    }


    private static final void fillSelectOrRemoveAllQuery(PreparedStatement stmt, PK itemPK) throws SQLException
    {
        (JDBCValueMappings.getInstance()).PK_WRITER.setValue(stmt, 1, itemPK);
    }


    private static final String REMOVE_ALL_GLOBAL_QUERY()
    {
        return "DELETE FROM " + ACLENTRIES_TABLE() + " WHERE ItemPK=? AND PrincipalPK=?";
    }


    private static final String SELECT_GLOBAL_QUERY()
    {
        return "SELECT * FROM " + ACLENTRIES_TABLE() + " WHERE ItemPK=? AND PrincipalPK=?";
    }


    private static final void fillSelectOrRemoveAllGlobalQuery(PreparedStatement stmt, PK principalPK) throws SQLException
    {
        JDBCValueMappings vm = JDBCValueMappings.getInstance();
        JDBCValueMappings.ValueWriter<PK, ?> pkWriter = vm.PK_WRITER;
        pkWriter.setValue(stmt, 1, MetaInformationEJB.DEFAULT_PRIMARY_KEY);
        pkWriter.setValue(stmt, 2, principalPK);
    }
}
