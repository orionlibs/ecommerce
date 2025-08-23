package de.hybris.platform.util.typesystem;

import de.hybris.bootstrap.typesystem.YExtension;
import de.hybris.bootstrap.typesystem.YTypeSystem;
import de.hybris.bootstrap.typesystem.YTypeSystemHandler;
import de.hybris.bootstrap.typesystem.YTypeSystemLoader;
import de.hybris.bootstrap.typesystem.xml.HybrisTypeSystemParser;
import de.hybris.bootstrap.xml.ParseAbortException;
import de.hybris.platform.core.DeploymentImpl;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.JaloConnection;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.jdbc.JDBCTypeSystemReader;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;

public class TypeSystemUtils
{
    private static final Logger log = Logger.getLogger(TypeSystemUtils.class.getName());


    public static YPersistedTypeSystem loadViaJDBC(List<String> extensionNames)
    {
        if(log.isInfoEnabled())
        {
            log.info("loading type system from database...");
        }
        YTypeSystemLoader loader = null;
        JDBCTypeSystemReader reader = null;
        HybrisTypeSystemParser parser = null;
        Connection conn = null;
        try
        {
            reader = new JDBCTypeSystemReader(conn = Registry.getCurrentTenant().getDataSource().getConnection(), (YTypeSystemHandler)(loader = new YTypeSystemLoader((YTypeSystem)new YPersistedTypeSystem())), extensionNames);
            reader.read();
            parser = new HybrisTypeSystemParser((YTypeSystemHandler)loader, false);
            for(String extName : extensionNames)
            {
                if(log.isInfoEnabled())
                {
                    log.info("parsing " + extName + " deployments...");
                }
                parser.parseExtensionDeploymentsFromSystem(loader.getSystem().getExtension(extName),
                                getTypeSystemAsStream(extName));
                parser.parseExtensionDeployments(loader.getSystem().getExtension(extName), getDeploymentsAsStream(extName));
            }
            loader.finish();
            loader.validate();
            if(log.isInfoEnabled())
            {
                log.info("done loading type system from database");
            }
            return (YPersistedTypeSystem)loader.getSystem();
        }
        catch(ParseAbortException e)
        {
            throw new IllegalArgumentException("unexpected parse error : " + e.getMessage(), e);
        }
        catch(SQLException e)
        {
            throw new IllegalArgumentException("unexpected parse error : " + e.getMessage(), e);
        }
        finally
        {
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
        }
    }


    public static YTypeSystem loadViaClassLoader(List<String> extensionNames)
    {
        return loadViaClassLoader(extensionNames, true);
    }


    public static YTypeSystem loadViaClassLoader(List<String> extensionNames, boolean buildMode)
    {
        YTypeSystemLoader loader = null;
        HybrisTypeSystemParser parser = null;
        try
        {
            loader = new YTypeSystemLoader(buildMode);
            parser = new HybrisTypeSystemParser((YTypeSystemHandler)loader, buildMode);
            for(String extName : extensionNames)
            {
                parser.parseExtensionSystem(extName, getTypeSystemAsStream(extName));
                YExtension extension = loader.getSystem().getExtension(extName);
                if(extension == null)
                {
                    log.debug("Skipping load of advanced-deployments for " + extName + " because can not find extension object (no items.xml exists)");
                    continue;
                }
                parser.parseExtensionDeployments(loader.getSystem().getExtension(extName), getDeploymentsAsStream(extName));
            }
            loader.finish();
            loader.validate();
            return loader.getSystem();
        }
        catch(ParseAbortException e)
        {
            throw new IllegalArgumentException("unexpected parse error : " + e.getMessage(), e);
        }
    }


    public static DeploymentImpl loadDeployments(String extName, boolean update)
    {
        return loadDeployments(Collections.singletonList(extName), false, update, false);
    }


    public static DeploymentImpl loadDeploymentsForInitialization(String extName, boolean forceClean)
    {
        return loadDeployments(Collections.singletonList(extName), false, true, forceClean);
    }


    public static DeploymentImpl loadDeployments(List<String> extNames, boolean validate, boolean update)
    {
        return loadDeployments(extNames, validate, update, false);
    }


    public static DeploymentImpl loadDeployments(List<String> extNames, boolean validate, boolean update, boolean ignoreDeploymentsTable)
    {
        if(log.isDebugEnabled())
        {
            log.debug("loading deployments (update=" + update + ")");
        }
        YTypeSystemLoader loader = null;
        HybrisTypeSystemParser parser = null;
        boolean autoLoadDBTypeMappings = !extNames.contains("core");
        try
        {
            loader = new YTypeSystemLoader(new YTypeSystem(false), update);
            parser = new HybrisTypeSystemParser((YTypeSystemHandler)loader, false);
            if(!ignoreDeploymentsTable)
            {
                YDeploymentJDBC.loadDeployments(loader);
            }
            if(autoLoadDBTypeMappings)
            {
                YExtension ext = loader.addExtension("core", null);
                parser.parseExtensionDeployments(ext, getDeploymentsAsStream("core"));
            }
            for(String extName : extNames)
            {
                YExtension ext = loader.addExtension(extName, null);
                parser.parseExtensionDeploymentsFromSystem(ext, getTypeSystemAsStream(extName));
                parser.parseExtensionDeployments(ext, getDeploymentsAsStream(extName));
            }
            loader.finish();
            if(validate)
            {
                loader.validate();
            }
            ParsedDeployments depls = new ParsedDeployments(loader.getSystem(), autoLoadDBTypeMappings ? Collections.<YExtension>singleton(loader.getSystem().getExtension("core")) : Collections.EMPTY_SET);
            if(log.isDebugEnabled())
            {
                log.debug("done loading deployments (update=" + update + ")");
            }
            return (DeploymentImpl)depls;
        }
        catch(ParseAbortException e)
        {
            log.error(e.getMessage());
            log.error(Utilities.getStackTraceAsString((Throwable)e));
            throw new IllegalArgumentException("unexpected parse error : " + e.getMessage(), e);
        }
    }


    public static DeploymentImpl loadDeploymentsForMigration(String extensionName, boolean update, boolean validate)
    {
        if(log.isDebugEnabled())
        {
            log.debug("loading deployments for migration (update=" + update + ")");
        }
        try
        {
            YTypeSystemLoader loader = new YTypeSystemLoader(new YTypeSystem(false), update);
            HybrisTypeSystemParser parser = new HybrisTypeSystemParser((YTypeSystemHandler)loader, false);
            List<String> extNames = Registry.getCurrentTenant().getTenantSpecificExtensionNames();
            Set<YExtension> excludeExtensions = new LinkedHashSet<>();
            for(String extName : extNames)
            {
                YExtension ext = loader.addExtension(extName, null);
                if(!extName.equalsIgnoreCase(extensionName))
                {
                    excludeExtensions.add(ext);
                }
                parser.parseExtensionDeploymentsFromSystem(ext, getTypeSystemAsStream(extName));
                parser.parseExtensionDeployments(ext, getDeploymentsAsStream(extName));
            }
            loader.finish();
            if(validate)
            {
                loader.validate();
            }
            ParsedDeployments depls = new ParsedDeployments(loader.getSystem(), excludeExtensions);
            if(log.isDebugEnabled())
            {
                log.debug("done loading deployments via file system (update=" + update + ")");
            }
            return (DeploymentImpl)depls;
        }
        catch(ParseAbortException e)
        {
            throw new IllegalArgumentException("unexpected parse error ", e);
        }
    }


    private static InputStream getTypeSystemAsStream(String extName)
    {
        Class<JaloConnection> clazz = JaloConnection.class;
        InputStream inputStream = clazz.getResourceAsStream("/" + extName + "-items.xml");
        if(inputStream == null)
        {
            inputStream = clazz.getResourceAsStream("/" + extName + ".items.xml");
            if(inputStream != null)
            {
                log.warn("loading " + extName + " from OLD location");
            }
        }
        else if(log.isDebugEnabled())
        {
            log.debug("loading " + extName + " from new location");
        }
        return inputStream;
    }


    private static InputStream getDeploymentsAsStream(String extName)
    {
        Class<JaloConnection> clazz = JaloConnection.class;
        InputStream inputStream = clazz.getResourceAsStream("/" + extName + "-advanced-deployment.xml");
        if(inputStream == null)
        {
            inputStream = clazz.getResourceAsStream("/" + extName + ".advanced-deployment.xml");
        }
        return inputStream;
    }
}
