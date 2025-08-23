package de.hybris.platform.persistence.meta;

import de.hybris.platform.core.Constants;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.threadregistry.OperationInfo;
import de.hybris.platform.core.threadregistry.RevertibleUpdate;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jdbcwrapper.HybrisDataSource;
import de.hybris.platform.persistence.EJBInternalException;
import de.hybris.platform.persistence.ItemRemote;
import de.hybris.platform.persistence.ManagerEJB;
import de.hybris.platform.util.Utilities;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Set;
import org.apache.log4j.Logger;

public class MetaInformationManagerEJB extends ManagerEJB
{
    private String systemName;
    private volatile String systemPK;
    private static final Logger LOG = Logger.getLogger(MetaInformationManagerEJB.class);


    private MetaInformationRemote getGlobalMetaInformation()
    {
        return getGlobalMetaInformation(true);
    }


    private MetaInformationRemote getGlobalMetaInformation(boolean failFast)
    {
        try
        {
            RevertibleUpdate theUpdate = OperationInfo.updateThread(OperationInfo.builder().withCategory(OperationInfo.Category.SYSTEM).build());
            try
            {
                Tenant sys = Registry.getCurrentTenant();
                String jndi = sys.getPersistenceManager().getJNDIName(55);
                MetaInformationRemote metaInformationRemote = (MetaInformationRemote)sys.getPersistencePool().findEntityByPK(jndi, MetaInformationEJB.DEFAULT_PRIMARY_KEY);
                if(theUpdate != null)
                {
                    theUpdate.close();
                }
                return metaInformationRemote;
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
        catch(Exception e)
        {
            if(failFast)
            {
                throw new EJBInternalException(e, "System not initialized (no meta information object found)", 0);
            }
            return null;
        }
    }


    public Set getPropertyNames()
    {
        return getGlobalMetaInformation().getPropertyNames();
    }


    public Object setProperty(String name, Object value)
    {
        return getGlobalMetaInformation().setProperty(name, value);
    }


    public Object getProperty(String name)
    {
        return getGlobalMetaInformation().getProperty(name);
    }


    public Object getPropertyRaw(String name)
    {
        return getGlobalMetaInformation().getPropertyRaw(name);
    }


    public Object removeProperty(String name)
    {
        return getGlobalMetaInformation().removeProperty(name);
    }


    public void setSystemInitUpdateTimestamp(long timestamp)
    {
        setProperty(Constants.CACHE_KEYS.METAINFO_SYSTEM_INITUPDATE_TS, Long.valueOf(timestamp));
    }


    public void resetSystemPK()
    {
        this.systemPK = null;
    }


    public long getSystemInitUpdateTimestamp()
    {
        Date metaInfoCreationTime = getGlobalMetaInformation().getCreationTime();
        if(metaInfoCreationTime != null)
        {
            return metaInfoCreationTime.getTime();
        }
        return getLegacySystemInitUpdateTimestamp();
    }


    private long getLegacySystemInitUpdateTimestamp()
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        try
        {
            Tenant tenant = Registry.getCurrentTenantNoFallback();
            HybrisDataSource hybrisDataSource = tenant.getDataSource();
            String tablePrefix = hybrisDataSource.getTablePrefix();
            conn = hybrisDataSource.getConnection();
            pstmt = conn.prepareStatement("SELECT VALUESTRING1 FROM " + ((
                            tablePrefix != null && tablePrefix.length() > 0) ? (tablePrefix + "props ") : "props ") + "WHERE ITEMPK IN ( SELECT PK FROM " + (
                            (tablePrefix != null && tablePrefix.length() > 0) ? (tablePrefix + "metainformations ") : "metainformations ") + "WHERE SystemName = ? ) AND REALNAME = ?");
            pstmt.setString(1, tenant.getTenantID());
            pstmt.setString(2, Constants.CACHE_KEYS.METAINFO_SYSTEM_INITUPDATE_TS);
            resultSet = pstmt.executeQuery();
            if(resultSet.next())
            {
                return resultSet.getLong(1);
            }
            return -1L;
        }
        catch(SQLException e)
        {
            if(LOG.isInfoEnabled())
            {
                LOG.info("error getting system init/update timestamp: " + e.getMessage() + " - ignored");
            }
            return -1L;
        }
        finally
        {
            Utilities.tryToCloseJDBC(conn, pstmt, resultSet);
        }
    }


    public String getSystemName()
    {
        if(this.systemName == null)
        {
            this.systemName = getGlobalMetaInformation().getSystemName();
        }
        return this.systemName;
    }


    public String getSystemPK()
    {
        if(this.systemPK == null)
        {
            this.systemPK = getGlobalMetaInformation().getSystemPk();
        }
        return this.systemPK;
    }


    public void removeItem(ItemRemote item) throws ConsistencyCheckException
    {
        throw new ConsistencyCheckException(null, "MetaManager.removeItem() cannot handle this item:" + item, 0);
    }
}
