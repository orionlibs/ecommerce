package de.hybris.platform.util;

import de.hybris.platform.cache.AbstractCacheUnit;
import de.hybris.platform.cache.Cache;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.persistence.EJBInternalException;
import de.hybris.platform.persistence.property.JDBCValueMappings;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IsItemAliveCheckUnit extends AbstractCacheUnit
{
    private final PK pk;


    IsItemAliveCheckUnit(Cache cache, PK pk)
    {
        super(cache);
        this.pk = pk;
    }


    public Object[] createKey()
    {
        return createItemKey(this.pk);
    }


    public static final Object[] createItemKey(PK pk)
    {
        return new Object[] {Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITYISALIVE, pk
                        .getTypeCodeAsString(), pk};
    }


    public final Object compute()
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
            Tenant t = Registry.getCurrentTenantNoFallback();
            if(t == null)
            {
                throw new IllegalStateException("no tenant active");
            }
            if(!t.equals(getCache().getTenant()))
            {
                throw new IllegalStateException("tenant mismatch for check is alive unit " + this.pk + " attached to " +
                                getCache().getTenant() + " but current is " + t);
            }
            conn = t.getDataSource().getConnection();
            pstmt = conn.prepareStatement("SELECT 1 FROM " + t
                            .getPersistenceManager()
                            .getItemDeployment(this.pk.getTypeCode())
                            .getDatabaseTableName() + " WHERE PK = ? ");
            (JDBCValueMappings.getInstance()).PK_WRITER.setValue(pstmt, 1, this.pk);
            rs = pstmt.executeQuery();
            return Boolean.valueOf(rs.next());
        }
        catch(SQLException e)
        {
            throw new EJBInternalException(e);
        }
        finally
        {
            Utilities.tryToCloseJDBC(conn, pstmt, rs);
        }
    }
}
