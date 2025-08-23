package de.hybris.platform.persistence;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.persistence.enumeration.EnumerationManagerEJB;
import de.hybris.platform.persistence.extension.ExtensionManagerEJB;
import de.hybris.platform.persistence.link.LinkManagerEJB;
import de.hybris.platform.persistence.meta.MetaInformationEJB;
import de.hybris.platform.persistence.meta.MetaInformationManagerEJB;
import de.hybris.platform.persistence.meta.MetaInformationRemote;
import de.hybris.platform.persistence.property.PropertyJDBC;
import de.hybris.platform.persistence.security.EJBPasswordEncoderNotFoundException;
import de.hybris.platform.persistence.security.PasswordEncoder;
import de.hybris.platform.persistence.type.TypeManagerEJB;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.jeeapi.YFinderException;
import de.hybris.platform.util.jeeapi.YObjectNotFoundException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import javax.naming.NamingException;
import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.log4j.Logger;

public class SystemEJB
{
    private static final Logger LOG = Logger.getLogger(SystemEJB.class.getName());
    private static final String DEFAULT_ENCODER_CONFIG = "MD5=de.hybris.platform.persistence.security.SaltedMD5PasswordEncoder,*=de.hybris.platform.persistence.security.PlainTextPasswordEncoder";
    public static final String DEFAULT_ENCODING = "*";
    public static final String PASSWORD_ENCODER_KEY = "password.encoders";
    public static final String[] WRITE_ON_INITIALIZATION = new String[] {"build.date", "build.number", "build.releasedate", "build.version"};
    private Map<String, PasswordEncoder> passwordEncoders;
    private final Tenant tenant;


    public static SystemEJB getInstance()
    {
        return Registry.getCurrentTenant().getSystemEJB();
    }


    public SystemEJB(Tenant tenant)
    {
        this.tenant = tenant;
    }


    public ItemRemote findRemoteObjectByPK(PK pk) throws EJBItemNotFoundException, EJBInvalidParameterException
    {
        try
        {
            return findRemoteObjectByPKInternal(pk);
        }
        catch(YObjectNotFoundException e)
        {
            EJBItemNotFoundException itemNotFoundException = new EJBItemNotFoundException((Throwable)e, "item " + pk + " not found", 0);
            itemNotFoundException.setInvalidPKs(Collections.singleton(pk));
            throw itemNotFoundException;
        }
    }


    public ItemRemote findRemoteObjectByPKInternal(PK pk) throws YObjectNotFoundException, EJBInvalidParameterException
    {
        int typecode = pk.getTypeCode();
        String jndiName = this.tenant.getPersistenceManager().getJNDIName(typecode);
        if(jndiName == null)
        {
            throw new EJBInvalidParameterException(null, "illegal typecode " + typecode + " - cannot find JNDI name", 0);
        }
        return (ItemRemote)this.tenant.getPersistencePool().findEntityByPK(jndiName, pk);
    }


    public Map<PK, ItemRemote> findRemoteObjectsByPK(Collection<PK> pks, Set<PK> preloadLanguagePKs, boolean ignoreMissing) throws EJBItemNotFoundException, EJBInvalidParameterException
    {
        if(pks == null)
        {
            return null;
        }
        if(pks.isEmpty())
        {
            return Collections.EMPTY_MAP;
        }
        if(pks.size() == 1)
        {
            PK pk = pks.iterator().next();
            try
            {
                ItemRemote rem = findRemoteObjectByPK(pk);
                return Collections.singletonMap(pk, rem);
            }
            catch(EJBItemNotFoundException e)
            {
                if(ignoreMissing)
                {
                    return Collections.EMPTY_MAP;
                }
                throw e;
            }
        }
        Map<PK, ItemRemote> ret = new HashMap<>(pks.size());
        for(Map.Entry<Integer, Set<PK>> e : groupByTypeCode(pks).entrySet())
        {
            int typeCode = ((Integer)e.getKey()).intValue();
            Set<PK> typedPKs = e.getValue();
            String jndiName = this.tenant.getPersistenceManager().getJNDIName(typeCode);
            if(jndiName == null)
            {
                throw new EJBInvalidParameterException(null, "illegal typecode " + typeCode + " - cannot find JNDI name", 0);
            }
            Collection<? extends ItemRemote> typedItems = null;
            try
            {
                ItemHome home = (ItemHome)this.tenant.getPersistencePool().getHomeProxy(jndiName);
                typedItems = home.findByPKList(typedPKs);
                if(!ignoreMissing && typedItems.size() != typedPKs.size())
                {
                    Set<PK> missing = getMissingPKs(typedPKs, (Collection)typedItems);
                    EJBItemNotFoundException itemNotFoundException = new EJBItemNotFoundException(null, "got missing items for pks " + missing + " from collection " + pks, 0);
                    itemNotFoundException.setInvalidPKs(missing);
                    throw itemNotFoundException;
                }
                if(preloadLanguagePKs != null && !preloadLanguagePKs.isEmpty() && !typedItems.isEmpty() && typedItems
                                .iterator().next() instanceof de.hybris.platform.persistence.c2l.LocalizableItemRemote)
                {
                    PropertyJDBC.preloadLocalizedProperties(preloadLanguagePKs, typedItems);
                }
                for(ItemRemote ir : typedItems)
                {
                    ret.put(ir.getPK(), ir);
                }
            }
            catch(YObjectNotFoundException ex)
            {
                if(!ignoreMissing)
                {
                    Set<PK> missing = getMissingPKs(typedPKs, (Collection)typedItems);
                    EJBItemNotFoundException exx = new EJBItemNotFoundException((Throwable)ex, "got missing items for pks " + missing + " from collection " + pks, 0);
                    exx.setInvalidPKs(missing);
                    throw exx;
                }
            }
            catch(YFinderException ex)
            {
                throw new EJBInternalException(ex);
            }
        }
        return ret;
    }


    protected Set<PK> getMissingPKs(Set<PK> required, Collection<ItemRemote> items)
    {
        Set<PK> ret = new HashSet<>(required);
        if(items != null)
        {
            for(ItemRemote i : items)
            {
                ret.remove(i.getPK());
            }
        }
        return ret;
    }


    public static Map<Integer, Set<PK>> groupByTypeCode(Collection<PK> pks)
    {
        return (Map<Integer, Set<PK>>)pks.stream().collect(Collectors.groupingBy(PK::getTypeCode, Collectors.toSet()));
    }


    @Deprecated(since = "ages", forRemoval = true)
    public Collection<String> getInstalledEncodings()
    {
        Collection<String> ret = new ArrayList<>(5);
        for(Map.Entry<String, PasswordEncoder> e : getPasswordEncoders().entrySet())
        {
            ret.add(e.getKey());
        }
        return ret;
    }


    @Deprecated(since = "ages", forRemoval = true)
    public PasswordEncoder getPasswordEncoder(String encoding) throws EJBPasswordEncoderNotFoundException
    {
        PasswordEncoder enc = Registry.getCurrentTenant().getJaloConnection().getPasswordEncoderFactory().getEncoder(encoding);
        if(enc == null && encoding != null)
        {
            try
            {
                Class<?> clazz = Class.forName(encoding);
                enc = (PasswordEncoder)clazz.newInstance();
                LOG.info("PasswordEncoder fallback: created encoder for " + encoding);
            }
            catch(ClassNotFoundException e)
            {
                LOG.warn("password encoder not found: " + e.getMessage());
            }
            catch(InstantiationException e)
            {
                LOG.warn("password encoder not found: " + e.getMessage());
            }
            catch(IllegalAccessException e)
            {
                LOG.warn("password encoder not found: " + e.getMessage());
            }
        }
        if(enc == null)
        {
            throw new EJBPasswordEncoderNotFoundException(null, "cannot find password encoder for encoding '" + encoding + "'", 0);
        }
        return enc;
    }


    @Deprecated(since = "ages", forRemoval = true)
    protected Map<String, PasswordEncoder> getPasswordEncoders()
    {
        synchronized(this)
        {
            if(this.passwordEncoders == null)
            {
                if(Config.getParameter("password.encoders") == null ||
                                Config.getParameter("password.encoders").trim().length() == 0)
                {
                    LOG.warn("Missing password encoder configuration -- It is highly recommend that you configure these one in your advanced.properties! Right now the following setings will be used: MD5=de.hybris.platform.persistence.security.SaltedMD5PasswordEncoder,*=de.hybris.platform.persistence.security.PlainTextPasswordEncoder");
                }
                CaseInsensitiveMap<String, PasswordEncoder> caseInsensitiveMap = new CaseInsensitiveMap();
                String mappingString = Config.getString("password.encoders", "MD5=de.hybris.platform.persistence.security.SaltedMD5PasswordEncoder,*=de.hybris.platform.persistence.security.PlainTextPasswordEncoder");
                if(mappingString != null)
                {
                    for(StringTokenizer st = new StringTokenizer(mappingString, ",;\t\n\r\f"); st.hasMoreTokens(); )
                    {
                        String mapping = st.nextToken();
                        int index = mapping.indexOf('=');
                        String prefix = mapping.substring(0, index).trim();
                        String className = mapping.substring(index + 1).trim();
                        try
                        {
                            caseInsensitiveMap.put(prefix, (PasswordEncoder)Class.forName(className).newInstance());
                            if(LOG.isDebugEnabled())
                            {
                                LOG.debug("added password encoder '" + prefix + "' class '" + className + "'");
                            }
                        }
                        catch(ClassNotFoundException e)
                        {
                            LOG.error("could not load password encoder class '" + className + "' (removed from mapping) : " + e);
                        }
                        catch(InstantiationException e)
                        {
                            LOG.error("could not instantiate password encoder class '" + className + "' (removed from mapping) : " + e);
                        }
                        catch(IllegalAccessException e)
                        {
                            LOG.error("could not access password encoder class '" + className + "' (removed from mapping) : " + e);
                        }
                    }
                }
                this.passwordEncoders = Collections.unmodifiableMap((Map<? extends String, ? extends PasswordEncoder>)caseInsensitiveMap);
            }
            return this.passwordEncoders;
        }
    }


    public ExtensionManagerEJB getExtensionManager()
    {
        return (ExtensionManagerEJB)Registry.getSingleton(ExtensionManagerEJB.class);
    }


    public MetaInformationManagerEJB getMetaInformationManager()
    {
        return (MetaInformationManagerEJB)Registry.getSingleton(MetaInformationManagerEJB.class);
    }


    public LinkManagerEJB getLinkManager()
    {
        return (LinkManagerEJB)Registry.getSingleton(LinkManagerEJB.class);
    }


    public TypeManagerEJB getTypeManager()
    {
        return (TypeManagerEJB)Registry.getSingleton(TypeManagerEJB.class);
    }


    public EnumerationManagerEJB getEnumerationManager()
    {
        return (EnumerationManagerEJB)Registry.getSingleton(EnumerationManagerEJB.class);
    }


    public boolean isLocked()
    {
        try
        {
            return (Utilities.isSystemInitialized(getMetaInformationManager().getPersistencePool().getDataSource()) && Boolean.TRUE
                            .equals(getGlobalMetaInformation().getProperty("system.locked")));
        }
        catch(Exception e)
        {
            LOG.error("could not read locking flag : " + e);
            return false;
        }
    }


    public void setLocked(boolean locked)
    {
        if(!locked && Config.getBoolean("system.unlocking.disabled", false) && isLocked())
        {
            throw new IllegalStateException("system cannot be unlocked - see platform configuration.");
        }
        try
        {
            getGlobalMetaInformation().setProperty("system.locked", Boolean.valueOf(locked));
        }
        catch(Exception e)
        {
            LOG.debug("could not set locking flag to " + locked + " : " + e.getMessage());
        }
    }


    public void setInitializedFlag(boolean initialized)
    {
        if(!initialized && isLocked())
        {
            throw new RuntimeException("cannot uninitialize system since it is locked");
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("setInitializedFlag( " + initialized + " )");
        }
        try
        {
            getGlobalMetaInformation().setInitializedFlag(initialized);
            if(initialized)
            {
                for(int i = 0; i < WRITE_ON_INITIALIZATION.length; i++)
                {
                    getGlobalMetaInformation().setProperty(WRITE_ON_INITIALIZATION[i],
                                    "build.date".equals(WRITE_ON_INITIALIZATION[i]) ? (new Date()).toString() :
                                                    Config.getParameter(WRITE_ON_INITIALIZATION[i]));
                }
            }
        }
        catch(SQLException e)
        {
            throw new RuntimeException("could not set initialized flag to " + initialized + " : " + e.getMessage(), e);
        }
        catch(NamingException e)
        {
            throw new RuntimeException("could not set initialized flag to " + initialized + " : " + e.getMessage(), e);
        }
        catch(YFinderException e)
        {
            throw new RuntimeException("could not set initialized flag to " + initialized + " : " + e.getMessage(), e);
        }
    }


    public MetaInformationRemote getGlobalMetaInformation() throws SQLException, NamingException, YFinderException
    {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try
        {
            connection = getMetaInformationManager().getPersistencePool().getDataSource().getConnection();
            try
            {
                String tabName = Config.getString("db.tableprefix", "") + "metainformations";
                if(Config.isMySQLUsed())
                {
                    tabName = tabName.toLowerCase();
                }
                statement = connection.createStatement();
                resultSet = statement.executeQuery("SELECT * FROM " + tabName);
            }
            catch(SQLException e)
            {
                throw new YFinderException("conn ok, but query failed: " + e, e);
            }
        }
        finally
        {
            Utilities.tryToCloseJDBC(connection, statement, resultSet);
        }
        String jndi = this.tenant.getPersistenceManager().getJNDIName(55);
        return (MetaInformationRemote)this.tenant.getPersistencePool().findEntityByPK(jndi, MetaInformationEJB.DEFAULT_PRIMARY_KEY);
    }
}
