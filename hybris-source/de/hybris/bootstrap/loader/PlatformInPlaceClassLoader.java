package de.hybris.bootstrap.loader;

import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.bootstrap.config.ExtensionInfo;
import de.hybris.bootstrap.config.PlatformConfig;
import de.hybris.bootstrap.config.SystemConfig;
import de.hybris.bootstrap.loader.metrics.ClassLoaderMetricEventListener;
import de.hybris.bootstrap.loader.metrics.ClassLoaderMetricRegistry;
import de.hybris.bootstrap.loader.rule.IgnoreClassLoadingEvent;
import de.hybris.bootstrap.loader.rule.IgnoreClassLoadingRuleExecutor;
import de.hybris.bootstrap.loader.rule.internal.ClassNotFoundRuleEvent;
import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class PlatformInPlaceClassLoader extends YURLClassLoader
{
    private static final ClassLoaderMetricRegistry CLASSLOADER_METRICS = new ClassLoaderMetricRegistry();
    private final IgnoreClassLoadingRuleExecutor ruleListExecutor;
    private final List<URL> inPlaceURLs;
    private final boolean isForWeb;

    static
    {
        registerAsParallelCapable();
    }

    public PlatformInPlaceClassLoader(String platformHome, String additionalClasspath, ClassLoader parent, boolean noteAboutStartup)
    {
        this(loadConfig(platformHome), additionalClasspath, parent, noteAboutStartup);
    }


    public PlatformInPlaceClassLoader(String platformHome, String additionalClasspath, ClassLoader parent, boolean noteAboutStartup, boolean isForWeb)
    {
        this(loadConfig(platformHome), additionalClasspath, parent, noteAboutStartup, isForWeb);
    }


    public PlatformInPlaceClassLoader(PlatformConfig platformConfig, String additionalClasspath, ClassLoader parent, boolean noteAboutStartup)
    {
        this(platformConfig, additionalClasspath, parent, noteAboutStartup, false);
    }


    public PlatformInPlaceClassLoader(PlatformConfig platformConfig, String additionalClasspath, ClassLoader parent, boolean noteAboutStartup, boolean isForWeb)
    {
        super(new URL[0], parent);
        this.isForWeb = isForWeb;
        List<URL> urls = getLibPatchesURLs(platformConfig);
        urls.addAll(Arrays.asList(getInPlaceURLs(platformConfig, additionalClasspath)));
        this.inPlaceURLs = urls;
        for(URL url : this.inPlaceURLs)
        {
            addURL(url);
        }
        if(noteAboutStartup)
        {
            start();
        }
        this
                        .ruleListExecutor = IgnoreClassLoadingRuleExecutor.createRuleFilterClassListExecutorWithRuleParams(platformConfig
                        .getSystemConfig().getIgnoreClassLoaderRuleParamList());
        this.ruleListExecutor.registerClassloaderMetricEventListener((ClassLoaderMetricEventListener)CLASSLOADER_METRICS);
        initializeLog4j2();
        initializeLog4j();
    }


    private List<URL> getLibPatchesURLs(PlatformConfig platformConfig)
    {
        List<URL> urls = new ArrayList<>();
        try
        {
            Properties properties = new Properties();
            ConfigUtil.loadRuntimeProperties(properties, platformConfig);
            addClassPathURLs(urls, properties.getProperty("platformInPlaceClassLoader.libPatches.classpath", ""));
            if(!urls.isEmpty())
            {
                System.out.println("Loading additional classes from: " + urls);
            }
        }
        catch(MalformedURLException e)
        {
            System.err.print(e);
        }
        return urls;
    }


    private void initializeLog4j2()
    {
        try
        {
            Class<?> loggerFactoryClass = Class.forName("org.slf4j.LoggerFactory", true, (ClassLoader)this);
            Method getLoggerMethod = loggerFactoryClass.getMethod("getLogger", new Class[] {Class.class});
            Object logger = getLoggerMethod.invoke(null, new Object[] {PlatformInPlaceClassLoader.class});
            Class<?> loggerClass = Class.forName("org.slf4j.Logger", true, (ClassLoader)this);
            Method infoMethod = loggerClass.getMethod("info", new Class[] {String.class});
            infoMethod.invoke(logger, new Object[] {"log4j2 is now correctly initialized"});
        }
        catch(ReflectiveOperationException e)
        {
            System.err.println(e);
        }
    }


    private void initializeLog4j()
    {
        try
        {
            Class<?> log4JUtilsClass = Class.forName("de.hybris.platform.core.Log4JUtils", true, (ClassLoader)this);
            Method startupMethod = log4JUtilsClass.getMethod("startup", null);
            startupMethod.invoke(null, new Object[0]);
        }
        catch(Exception e)
        {
            System.err.println(e);
        }
    }


    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException
    {
        synchronized(getClassLoadingLock(name))
        {
            ClassNotFoundException exceptionThrown = null;
            try
            {
                if(this.ruleListExecutor != null && this.ruleListExecutor.shouldClassBeSkipped(name))
                {
                    throw new ClassNotFoundException();
                }
                return super.loadClass(name, resolve);
            }
            catch(ClassNotFoundException ne)
            {
                exceptionThrown = ne;
                throw ne;
            }
            finally
            {
                if(exceptionThrown != null)
                {
                    if(this.ruleListExecutor != null)
                    {
                        this.ruleListExecutor.publishEvent((IgnoreClassLoadingEvent)new ClassNotFoundRuleEvent(exceptionThrown, name));
                    }
                }
            }
        }
    }


    private static PlatformConfig loadConfig(String platformHome)
    {
        SystemConfig systemConfig = ConfigUtil.getSystemConfig(platformHome);
        return PlatformConfig.getInstance(systemConfig);
    }


    public final URL[] getInPlaceURLs(String platformHome, String additionalClasspath)
    {
        return getInPlaceURLs(loadConfig(platformHome), additionalClasspath);
    }


    public final URL[] getInPlaceURLs(PlatformConfig config, String additionalClasspath)
    {
        try
        {
            List<URL> urls = new ArrayList<>();
            addClassPathURLs(urls, additionalClasspath);
            File file = new File(new File(config.getPlatformHome(), "bootstrap/bin"), "ybootstrap.jar");
            urls.add(buildURL(file, YURLClassLoader.TYPE.FILE));
            file = new File(config.getSystemConfig().getBootstrapBinDir(), "models.jar");
            if(!file.exists())
            {
                file = new File(new File(config.getPlatformHome(), "bootstrap/bin"), "models.jar");
            }
            urls.add(buildURL(file, YURLClassLoader.TYPE.FILE));
            file = new File(new File(config.getSystemConfig().getConfigDir(), "licence"), "hybrislicence.jar");
            if(file.exists())
            {
                urls.add(buildURL(file, YURLClassLoader.TYPE.FILE));
            }
            for(ExtensionInfo info : config.getExtensionInfosInBuildOrder())
            {
                addExtensionURLs(info, urls);
            }
            file = new File(new File(config.getPlatformHome(), "lib"), "dbdriver");
            if(file.listFiles() != null)
            {
                for(File lib : file.listFiles())
                {
                    urls.add(buildURL(lib, YURLClassLoader.TYPE.FILE));
                }
            }
            if(!this.isForWeb)
            {
                file = new File(new File(config.getPlatformHome(), "tomcat"), "lib");
                if(file.listFiles() != null)
                {
                    for(File lib : file.listFiles())
                    {
                        urls.add(buildURL(lib, YURLClassLoader.TYPE.FILE));
                    }
                }
            }
            return urls.<URL>toArray(new URL[urls.size()]);
        }
        catch(MalformedURLException e)
        {
            throw new IllegalArgumentException(e);
        }
    }


    private void addExtensionURLs(ExtensionInfo info, List<URL> urls) throws MalformedURLException
    {
        File file = new File(info.getExtensionDirectory(), "resources");
        if(file.exists())
        {
            urls.add(buildURL(file, YURLClassLoader.TYPE.DIR));
        }
        file = new File(info.getExtensionDirectory(), "lib");
        if(file.exists())
        {
            for(File lib : file.listFiles())
            {
                urls.add(buildURL(lib, YURLClassLoader.TYPE.FILE));
            }
        }
        file = new File(info.getExtensionDirectory(), "classes");
        if(file.exists())
        {
            urls.add(buildURL(file, YURLClassLoader.TYPE.DIR));
        }
        file = new File(new File(info.getExtensionDirectory(), "bin"), info.getName() + "server.jar");
        if(file.exists())
        {
            urls.add(buildURL(file, YURLClassLoader.TYPE.FILE));
        }
    }


    public static ClassLoaderMetricRegistry getClassloaderMetrics()
    {
        return CLASSLOADER_METRICS;
    }


    public List<URL> getInPlaceURLs()
    {
        return Collections.unmodifiableList(this.inPlaceURLs);
    }
}
