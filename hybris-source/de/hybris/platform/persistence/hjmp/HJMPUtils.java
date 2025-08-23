package de.hybris.platform.persistence.hjmp;

import de.hybris.platform.core.ItemDeployment;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.directpersistence.exception.ConcurrentModificationException;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.config.ConfigIntf;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DuplicateKeyException;

public class HJMPUtils
{
    public static final String HJMP_LOOKUP_RETRY_INTERVAL = "hjmp.lookup.retry.interval";
    public static final String HJMP_LOOKUP_RETRY_TIMEOUT = "hjmp.lookup.retry.timeout";
    public static final String HJMP_LOOKUP_RETRY_ENABLED = "hjmp.lookup.retry.enabled";
    private static final Logger LOG = Logger.getLogger(HJMPUtils.class);
    private static final ThreadLocal<OptimisticLockingContext> HJMP_TS_MODEL_REGISTRY = (ThreadLocal<OptimisticLockingContext>)new Object();


    public static String getPKFromHJMPKey(Object[] key)
    {
        return (String)key[3];
    }


    public static boolean isConcurrentModificationException(Throwable e)
    {
        return (Utilities.getRootCauseOfType(e, ConcurrencyFailureException.class) != null ||
                        Utilities.getRootCauseOfType(e, ConcurrentModificationException.class) != null);
    }


    public static boolean isOptimisticLockingEnabledForType(PK typePkString)
    {
        return ((OptimisticLockingContext)HJMP_TS_MODEL_REGISTRY.get()).isOptimisticLockingEnabledForType(typePkString);
    }


    public static boolean isOptimisticLockingEnabledForType(String typeCode)
    {
        return ((OptimisticLockingContext)HJMP_TS_MODEL_REGISTRY.get()).isOptimisticLockingEnabledForType(typeCode);
    }


    public static void enableOptimisticLocking()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Enabling optimistic locking for thread " + Thread.currentThread());
        }
        ((OptimisticLockingContext)HJMP_TS_MODEL_REGISTRY.get()).enableOptimisticLocking();
    }


    public static void disableOptimisticLocking()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Disabling optimistic locking for thread " + Thread.currentThread());
        }
        ((OptimisticLockingContext)HJMP_TS_MODEL_REGISTRY.get()).disableOptimisticLocking();
    }


    public static boolean isOptimisticLockingEnabled()
    {
        boolean enabled = ((OptimisticLockingContext)HJMP_TS_MODEL_REGISTRY.get()).isOptimisticLockingEnabled();
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Optimistic Locking for thread " + Thread.currentThread() + " is " + enabled);
        }
        return enabled;
    }


    public static void clearOptimisticLockingSetting()
    {
        ((OptimisticLockingContext)HJMP_TS_MODEL_REGISTRY.get()).clearOptimisticLockingSetting();
    }


    public static void setBlacklistedTypesForOptimisticLocking(String types)
    {
        ((OptimisticLockingContext)HJMP_TS_MODEL_REGISTRY.get()).setDisabledTypesForOptimisticLocking(types);
    }


    public static String getBeanNameFromHJMPKey(Object[] key)
    {
        return (String)key[2];
    }


    public static void registerVersionsForPks(Map<PK, Long> mapping)
    {
        ((OptimisticLockingContext)HJMP_TS_MODEL_REGISTRY.get()).registerVersionsForPks(mapping);
    }


    public static void clearVersionsForPks()
    {
        ((OptimisticLockingContext)HJMP_TS_MODEL_REGISTRY.get()).clearVersionsForPks();
    }


    public static boolean isFromServiceLayer(PK pk)
    {
        return ((OptimisticLockingContext)HJMP_TS_MODEL_REGISTRY.get()).isFromServiceLayer(pk);
    }


    public static Long getVersionForPk(PK pk)
    {
        return ((OptimisticLockingContext)HJMP_TS_MODEL_REGISTRY.get()).getVersionForPk(pk);
    }


    public static void updateVersionForPk(PK pk, Long version)
    {
        ((OptimisticLockingContext)HJMP_TS_MODEL_REGISTRY.get()).updateVersionForPk(pk, version);
    }


    public static void tryToClose(Connection connection, Statement statement, Statement statement2, ResultSet resultSet)
    {
        Exception ex = null;
        try
        {
            if(resultSet != null)
            {
                resultSet.close();
            }
        }
        catch(SQLException e)
        {
            ex = e;
        }
        try
        {
            if(statement != null)
            {
                statement.close();
            }
        }
        catch(SQLException e)
        {
            ex = e;
        }
        try
        {
            if(statement2 != null)
            {
                statement2.close();
            }
        }
        catch(SQLException e)
        {
            ex = e;
        }
        try
        {
            if(connection != null)
            {
                connection.close();
            }
        }
        catch(SQLException e)
        {
            ex = e;
        }
        if(ex != null)
        {
            throw new HJMPException(ex);
        }
    }


    public static void tryToClose(Connection connection, Statement statement, ResultSet resultSet)
    {
        tryToClose(connection, statement, null, resultSet);
    }


    public static void tryToClose(Connection connection, Statement statement, Statement statement2)
    {
        tryToClose(connection, statement, statement2, null);
    }


    public static void tryToClose(Connection connection, Statement statement)
    {
        tryToClose(connection, statement, null, null);
    }


    public static void tryToClose(Statement statement, ResultSet resultSet)
    {
        tryToClose(null, statement, null, resultSet);
    }


    public static void tryToClose(Statement statement)
    {
        tryToClose(statement, null);
    }


    public static boolean isPKLookupRetryEnabled()
    {
        return isPKLookupRetryEnabled(Registry.getCurrentTenant().getConfig());
    }


    public static boolean isPKLookupRetryEnabled(ConfigIntf cfg)
    {
        return cfg.getBoolean("hjmp.lookup.retry.enabled", false);
    }


    public static Object enablePKLookupRetry(long timeout, long interval)
    {
        return enablePKLookupRetry(Registry.getCurrentTenant().getConfig(), timeout, interval);
    }


    public static Object enablePKLookupRetry(ConfigIntf cfg, long timeout, long interval)
    {
        String[] token = {cfg.getParameter("hjmp.lookup.retry.enabled"), cfg.getParameter("hjmp.lookup.retry.timeout"), cfg.getParameter("hjmp.lookup.retry.interval")};
        Config.setParameter("hjmp.lookup.retry.enabled", Boolean.TRUE.toString());
        Config.setParameter("hjmp.lookup.retry.timeout", Long.toString(timeout));
        Config.setParameter("hjmp.lookup.retry.interval", Long.toString(interval));
        return token;
    }


    public static Object disablePKLookupRetry()
    {
        return disablePKLookupRetry(Registry.getCurrentTenant().getConfig());
    }


    public static Object disablePKLookupRetry(ConfigIntf cfg)
    {
        String[] token = {cfg.getParameter("hjmp.lookup.retry.enabled"), cfg.getParameter("hjmp.lookup.retry.timeout"), cfg.getParameter("hjmp.lookup.retry.interval")};
        Config.setParameter("hjmp.lookup.retry.enabled", Boolean.FALSE.toString());
        return token;
    }


    public static void restorPKLookupRetry(Object token)
    {
        restorPKLookupRetry(Registry.getCurrentTenant().getConfig(), token);
    }


    public static void restorPKLookupRetry(ConfigIntf cfg, Object token)
    {
        String[] before = (String[])token;
        cfg.setParameter("hjmp.lookup.retry.enabled", before[0]);
        cfg.setParameter("hjmp.lookup.retry.timeout", before[1]);
        cfg.setParameter("hjmp.lookup.retry.interval", before[2]);
    }


    public static ResultSet retryMissingPKLookup(ResultSet previousResultSet, PreparedStatement pstmt, ConfigIntf cfg) throws SQLException
    {
        tryToClose(null, previousResultSet);
        if(isPKLookupRetryEnabled(cfg))
        {
            long timeout = cfg.getLong("hjmp.lookup.retry.timeout", 5000L);
            long interval = cfg.getLong("hjmp.lookup.retry.interval", 500L);
            return retryMissingPKLookup(pstmt, timeout, interval);
        }
        return null;
    }


    public static ResultSet retryMissingPKLookup(PreparedStatement pstmt, long timeout, long delay) throws SQLException
    {
        long maxWait = System.currentTimeMillis() + timeout;
        ResultSet rs = null;
        boolean success = false;
        do
        {
            if(rs != null)
            {
                tryToClose(null, rs);
                rs = null;
            }
            try
            {
                Thread.sleep(delay);
            }
            catch(InterruptedException e)
            {
                Thread.currentThread().interrupt();
                return rs;
            }
            rs = pstmt.executeQuery();
            success = rs.next();
        }
        while(!success && System.currentTimeMillis() < maxWait);
        if(!success)
        {
            tryToClose(null, rs);
            rs = null;
        }
        return rs;
    }


    public static boolean isDuplicateKeyExceptionOnTypeCode(Exception e, int typeCode)
    {
        if(Utilities.getRootCauseOfType(e, DuplicateKeyException.class) == null)
        {
            return false;
        }
        HJMPException hjmpException = (HJMPException)Utilities.getRootCauseOfType(e, HJMPException.class);
        if(hjmpException == null)
        {
            return false;
        }
        ItemDeployment deployment = hjmpException.getDeployment();
        boolean result = (deployment != null && deployment.getTypeCode() == typeCode);
        return result;
    }
}
