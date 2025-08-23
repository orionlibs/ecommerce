package de.hybris.platform.core;

import com.google.common.base.Splitter;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Iterables;
import com.google.common.collect.ObjectArrays;
import de.hybris.platform.spring.ctx.CoreScopeTenantIgnoringDocReader;
import de.hybris.platform.spring.ctx.ScopeTenantIgnoreDocReader;
import de.hybris.platform.util.CoreUtilities;
import de.hybris.platform.util.Utilities;
import java.io.File;
import java.util.Collection;
import java.util.StringTokenizer;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

abstract class HybrisContextFactory
{
    private static final Logger LOG = Logger.getLogger(HybrisContextFactory.class.getName());
    private static final String SPRING_PROFILES_ACTIVE = "spring.profiles.active";
    private static final Splitter ACTIVE_PROFILES_SPLITTER = Splitter.on(',').omitEmptyStrings().trimResults();
    private final String messageTemplate;
    private volatile CoreUtilities config = null;
    String contextLocationPartkey;
    String defaultContextName;
    private static volatile Exception loadingFailed = null;


    protected abstract GenericApplicationContext createNewContext();


    HybrisContextFactory(String messageTemplate)
    {
        this.messageTemplate = messageTemplate;
    }


    void setLoadingFailure(Exception loadingFailed)
    {
        HybrisContextFactory.loadingFailed = loadingFailed;
    }


    Exception getLoadingFailure()
    {
        return loadingFailed;
    }


    protected void refreshContext(GenericApplicationContext ctx)
    {
        Stopwatch stopWatch = Stopwatch.createUnstarted();
        try
        {
            stopWatch.start();
            ctx.refresh();
        }
        catch(Exception e)
        {
            throw handleContexLoadingError(e, ctx);
        }
        finally
        {
            stopWatch.stop();
            LOG.info("- Refreshing SpringContext (" + ctx + ") took: (" + stopWatch.toString() + ")");
        }
    }


    protected RuntimeException handleContexLoadingError(Exception exception, GenericApplicationContext context)
    {
        LOG.error("Error initializing global application context!", exception);
        try
        {
            setLoadingFailure(exception);
            context.destroy();
        }
        catch(Exception ex)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Failed during destroying the context " + context, ex);
            }
        }
        if(exception instanceof RuntimeException)
        {
            return (RuntimeException)exception;
        }
        return new RuntimeException(exception);
    }


    private ClassLoader getDefaultClassLoader()
    {
        return Registry.class.getClassLoader();
    }


    protected Collection<String> getPlatformExtensions()
    {
        return getCoreUtils().getAllConfiguredExtensionNames();
    }


    GenericApplicationContext build(String... additionalProfiles) throws FatalBeanException
    {
        if(getLoadingFailure() != null)
        {
            throw new FatalBeanException("Context " + this + " couldn't  be created correctly due to, " +
                            getLoadingFailure().getMessage());
        }
        Stopwatch stopWatch = Stopwatch.createUnstarted();
        GenericApplicationContext context = createNewContext();
        context.setResourceLoader((ResourceLoader)new DefaultResourceLoader(getDefaultClassLoader()));
        context.setClassLoader(getDefaultClassLoader());
        context.getBeanFactory().setBeanClassLoader(getDefaultClassLoader());
        addActiveProfiles(context, additionalProfiles);
        for(String name : getPlatformExtensions())
        {
            try
            {
                stopWatch.start();
                String locations = buildLocationKey(name);
                if(StringUtils.isBlank(locations))
                {
                    locations = buildLocationKeyWithFallback(name);
                }
                boolean atLeastOneLocationExists = false;
                for(StringTokenizer toki = new StringTokenizer(locations, ","); toki.hasMoreTokens(); )
                {
                    if(loadSpringConfig(context, name, toki.nextToken()))
                    {
                        atLeastOneLocationExists = true;
                    }
                }
                if(atLeastOneLocationExists)
                {
                    LOG.info(String.format(this.messageTemplate, new Object[] {name, locations, stopWatch.toString()}));
                }
            }
            finally
            {
                stopWatch.reset();
            }
        }
        return context;
    }


    void addActiveProfiles(GenericApplicationContext context, String[] additionalProfiles)
    {
        String[] configuredProfiles = getConfiguredActiveProfilesFromProperties();
        String[] profiles = (String[])ObjectArrays.concat((Object[])configuredProfiles, (Object[])additionalProfiles, String.class);
        if(ArrayUtils.isNotEmpty((Object[])profiles))
        {
            ConfigurableEnvironment environment = context.getEnvironment();
            for(String profile : profiles)
            {
                environment.addActiveProfile(profile);
            }
        }
    }


    private String[] getConfiguredActiveProfilesFromProperties()
    {
        String configuredProfiles = getConfigParameter("spring.profiles.active");
        if(StringUtils.isNotBlank(configuredProfiles))
        {
            return (String[])Iterables.toArray(ACTIVE_PROFILES_SPLITTER.split(configuredProfiles), String.class);
        }
        return ArrayUtils.EMPTY_STRING_ARRAY;
    }


    CoreUtilities getCoreUtils()
    {
        if(this.config == null)
        {
            this
                            .config = new CoreUtilities(Utilities.getPlatformConfig(), Registry.isStandaloneMode(), Registry.getPreferredClusterID());
        }
        return this.config;
    }


    abstract String getConfigParameter(String paramString1, String paramString2);


    abstract String getConfigParameter(String paramString);


    private String buildLocationKeyWithFallback(String name)
    {
        return getConfigParameter(name + name, "/" + name + "-" + this.defaultContextName);
    }


    private String buildLocationKey(String name)
    {
        return getConfigParameter(name + name);
    }


    private boolean loadSpringConfig(GenericApplicationContext context, String extension, String location)
    {
        Resource resource = getResource(extension, location);
        if(resource != null)
        {
            XmlBeanDefinitionReader xmlReader = createXmlBeanReader(context);
            xmlReader.loadBeanDefinitions(resource);
            return true;
        }
        return false;
    }


    protected Resource getResource(String extension, String location)
    {
        try
        {
            File file = new File(location);
            if(file.isFile() && file.exists())
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(String.format("Found context file (%s) for extension (%s)", new Object[] {location, extension}));
                }
                return (Resource)new FileSystemResource(location);
            }
            String resourceLocation = location;
            if(resourceLocation.charAt(0) != '/')
            {
                resourceLocation = "/" + resourceLocation;
            }
            if(Registry.class.getResource(resourceLocation) != null)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(String.format("Found resource-based context file (%s) for extension (%s)", new Object[] {resourceLocation, extension}));
                }
                return (Resource)new ClassPathResource(resourceLocation, Registry.class.getClassLoader());
            }
        }
        catch(BeanDefinitionStoreException e)
        {
            LOG.error("Error while loading application context file " + location + " for extension " + extension, (Throwable)e);
        }
        return null;
    }


    XmlBeanDefinitionReader createXmlBeanReader(GenericApplicationContext context)
    {
        XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader((BeanDefinitionRegistry)context);
        xmlReader.setDocumentReaderClass(getDocumentReaderClass());
        return xmlReader;
    }


    protected Class<? extends ScopeTenantIgnoreDocReader> getDocumentReaderClass()
    {
        return (Class)CoreScopeTenantIgnoringDocReader.class;
    }
}
