package de.hybris.platform.core;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.internal.ShutdownTokenFileCreator;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.licence.Licence;
import de.hybris.platform.licence.internal.LicenseFactory;
import de.hybris.platform.persistence.EJBInternalException;
import de.hybris.platform.persistence.meta.MetaInformationRemote;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Token;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.config.ConfigIntf;
import de.hybris.platform.util.config.HybrisConfig;
import de.hybris.platform.util.encryption.EncryptionUtil;
import de.hybris.platform.util.encryption.ValueEncryptor;
import de.hybris.platform.util.jdbc.DatabaseNameResolver;
import java.security.Provider;
import java.security.Security;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;
import javax.crypto.SecretKey;
import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class MasterTenant extends AbstractTenant
{
    public static final long NO_CLUSTER_ISLAND_PK = -1L;


    public static void setSecurityProvider()
    {
        Security.addProvider((Provider)new BouncyCastleProvider());
    }


    static
    {
        setSecurityProvider();
    }

    private static final Logger LOG = Logger.getLogger(MasterTenant.class.getName());
    public static final String MASTERTENANT_ID = "master";
    private final ConfigIntf config;
    private final Object valueEncryprorLock = new Object();
    private volatile ValueEncryptor valueEncryptor;
    private final AtomicReference<PK> clusterIslandPK = new AtomicReference<>(PK.NULL_PK);
    private final Licence licence;
    private final Token localShutdownToken;


    public Token getLocalShutdownToken()
    {
        return this.localShutdownToken;
    }


    public static MasterTenant getInstance()
    {
        return Registry.getMasterTenant();
    }


    MasterTenant()
    {
        super("master");
        this.config = (ConfigIntf)new HybrisConfig(getPlatformProperties(), Registry.isStandaloneMode(), Registry.getPreferredClusterID());
        this.licence = (new LicenseFactory()).getCurrentLicense(getDatabaseName());
        this.localShutdownToken = Token.generateNew();
        ShutdownTokenFileCreator creator = new ShutdownTokenFileCreator(this.localShutdownToken, getConfig());
        creator.writeShutdownTokenFile();
    }


    public String getDatabaseName()
    {
        String dbName = "hsqldb";
        if(isJNDIConfigured())
        {
            dbName = this.config.getParameter("db.pool.fromJNDI.dbtype");
        }
        else
        {
            String dbUrl = this.config.getParameter(Config.SystemSpecificParams.DB_URL);
            try
            {
                dbName = DatabaseNameResolver.guessDatabaseNameFromURL(dbUrl);
            }
            catch(IllegalArgumentException e)
            {
                LOG.error("Cannot derive database name from properties. Check configuration! (Falling back to " + dbName + ")", e);
            }
        }
        return dbName;
    }


    private boolean isJNDIConfigured()
    {
        return (this.config.getString(Config.SystemSpecificParams.DB_POOL_FROMJNDI, null) != null);
    }


    public Licence getLicence()
    {
        return this.licence;
    }


    Properties getPlatformProperties()
    {
        return Utilities.loadPlatformProperties();
    }


    public List<String> getTenantSpecificExtensionNames()
    {
        return Utilities.getInstalledExtensionNames((Tenant)this);
    }


    public ConfigIntf getConfig()
    {
        return this.config;
    }


    public ValueEncryptor getValueEncryptor()
    {
        if(this.valueEncryptor == null)
        {
            synchronized(this.valueEncryprorLock)
            {
                if(this.valueEncryptor == null)
                {
                    ConfigIntf cfg = getConfig();
                    Map<String, SecretKey> keyFiles = EncryptionUtil.getSecretKeysFromConfig();
                    this
                                    .valueEncryptor = new ValueEncryptor(cfg, cfg.getParameter("encryption.provider.class"), cfg.getParameter("encryption.provider.signature"), keyFiles, cfg.getParameter("symmetric.algorithm"),
                                    (cfg.getParameter("symmetric.key.file.default") != null) ? cfg.getParameter("symmetric.key.file.default") : "1");
                }
            }
        }
        return this.valueEncryptor;
    }


    public void startUp()
    {
    }


    public Locale getTenantSpecificLocale()
    {
        return Locale.getDefault();
    }


    public TimeZone getTenantSpecificTimeZone()
    {
        return TimeZone.getDefault();
    }


    public void shutDown()
    {
        shutDown(AbstractTenant.ShutDownMode.SYSTEM_SHUTDOWN);
    }


    void shutDown(AbstractTenant.ShutDownMode mode)
    {
        if(mode == AbstractTenant.ShutDownMode.SYSTEM_SHUTDOWN)
        {
            for(SlaveTenant entry : getSlaveTenantsMap().values())
            {
                entry.doShutDown();
            }
        }
    }


    @Deprecated(since = "5.0", forRemoval = true)
    protected Set<MetaInformationRemote> getSlaveSystemMetaInfos()
    {
        throw new UnsupportedOperationException("This operation is not allowed, please provide the offline platform/tenant_{tenantID}.properties or config/local_tenant_{tenantID}.properties configuration instead");
    }


    @Deprecated(since = "5.0", forRemoval = true)
    protected MetaInformationRemote createSlaveTenantMetaInfo(String tenantID, String dbUrl, String dbUser, String dbPassword, String dbDriver, String dbTablePrefix, String fromJNDI, String locDef, String tzDef, String dataSourceClassName)
    {
        throw new UnsupportedOperationException("This operation is not allowed, please provide the offline platform/tenant_{tenantID}.properties or config/local_tenant_{tenantID}.properties configuration instead");
    }


    @Deprecated(since = "5.0", forRemoval = true)
    protected void removeSlaveTenantMetaInfo(SlaveTenant tenant)
    {
        throw new UnsupportedOperationException("This operation is not allowed, please provide the offline platform/tenant_{tenantID}.properties or config/local_tenant_{tenantID}.properties configuration instead");
    }


    protected Map<String, SlaveTenant> getSlaveTenantsMap()
    {
        return Registry.getSlaveTenants();
    }


    public Set<String> getSlaveTenantIDs()
    {
        return getSlaveTenantsMap().keySet();
    }


    @Deprecated(since = "5.0", forRemoval = true)
    public void removeSlaveTenant(String systemID)
    {
        throw new UnsupportedOperationException("This operation is not allowed, please provide the offline platform/tenant_{tenantID}.properties or config/local_tenant_{tenantID}.properties configuration instead");
    }


    @Deprecated(since = "5.0", forRemoval = true)
    protected void removeSlaveTenant(SlaveTenant toRemove)
    {
        throw new UnsupportedOperationException("This operation is not allowed, please provide the offline platform/tenant_{tenantID}.properties or config/local_tenant_{tenantID}.properties configuration instead");
    }


    @Deprecated(since = "5.0", forRemoval = true)
    public SlaveTenant createSlaveTenant(String tenantID, String dbUrl, String dbUser, String dbPassword, String driver, String tableprefix, String fromJNDI, String locale, String timeZone) throws ConsistencyCheckException
    {
        return createSlaveTenant(tenantID, dbUrl, dbUser, dbPassword, driver, tableprefix, fromJNDI, locale, timeZone, null);
    }


    @Deprecated(since = "5.0", forRemoval = true)
    public SlaveTenant createSlaveTenant(String tenantID, String dbUrl, String dbUser, String dbPassword, String driver, String tableprefix, String fromJNDI, String locale, String timeZone, String dataSourceFactoryClassName) throws ConsistencyCheckException
    {
        throw new UnsupportedOperationException("This operation is not allowed, please provide the offline platform/tenant_{tenantID}.properties or config/local_tenant_{tenantID}.properties configuration instead");
    }


    @Deprecated(since = "5.0", forRemoval = true)
    public SlaveTenant getSlaveTenant(String tenantID)
    {
        return getSlaveTenantsMap().get(tenantID);
    }


    public long getClusterIslandPK()
    {
        if(getState() == AbstractTenant.State.STARTED)
        {
            PK pk = this.clusterIslandPK.get();
            if(PK.NULL_PK != pk)
            {
                return pk.getLongValue();
            }
            PK pkToSet = Optional.<Long>ofNullable(tryToGetCLusterIslandPK()).map(PK::fromLong).orElse(PK.NULL_PK);
            if(pkToSet == PK.NULL_PK)
            {
                return ((PK)this.clusterIslandPK.get()).getLongValue();
            }
            PK currentPK = this.clusterIslandPK.accumulateAndGet(pkToSet, (prev, next) -> (prev == pk) ? next : prev);
            Preconditions.checkArgument((currentPK != PK.NULL_PK), "NULL_PK not expected here.");
            return currentPK.getLongValue();
        }
        return -1L;
    }


    private Long tryToGetCLusterIslandPK()
    {
        try
        {
            String pkStr = (String)performWithinOwnSystem((AbstractTenant.CodeWrapper)new Object(this));
            return (pkStr != null) ? Long.valueOf(pkStr) : Long.valueOf(0L);
        }
        catch(EJBInternalException e)
        {
            LOG.debug(e);
            return null;
        }
    }


    public void updateClusterIslandPKFromDatabase()
    {
        this.clusterIslandPK.updateAndGet(p -> PK.NULL_PK);
    }
}
