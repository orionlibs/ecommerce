package de.hybris.platform.basecommerce.util;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.extension.ExtensionManager;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextLoader;
import org.springframework.util.Assert;

public class SpringCustomContextLoader
{
    private static final Logger log = LoggerFactory.getLogger(SpringCustomContextLoader.class);
    private static final String DEFAULT_CONTEXT_LOADER_CLASS_NAME = "org.springframework.test.context.support.GenericXmlContextLoader";
    private ContextLoader contextLoader = null;
    private String[] locations = null;


    public SpringCustomContextLoader(Class<?> testClazz)
    {
        ContextConfiguration contextConfiguration = (ContextConfiguration)testClazz.getAnnotation(ContextConfiguration.class);
        if(contextConfiguration == null)
        {
            if(log.isInfoEnabled())
            {
                log.info("@ContextConfiguration not found for class [" + testClazz + "]");
            }
        }
        else
        {
            if(log.isTraceEnabled())
            {
                log.trace("Retrieved @ContextConfiguration [" + contextConfiguration + "] for class [" + testClazz + "]");
            }
            Class<? extends ContextLoader> contextLoaderClass = contextConfiguration.loader();
            if(ContextLoader.class.equals(contextLoaderClass))
            {
                try
                {
                    contextLoaderClass = (Class)getClass().getClassLoader().loadClass("org.springframework.test.context.support.GenericXmlContextLoader");
                }
                catch(ClassNotFoundException ex)
                {
                    throw new IllegalStateException("Could not load default ContextLoader class [org.springframework.test.context.support.GenericXmlContextLoader]. Specify @ContextConfiguration's 'loader' attribute or make the default loader class available.", ex);
                }
            }
            this.contextLoader = (ContextLoader)BeanUtils.instantiateClass(contextLoaderClass);
            this.locations = retrieveContextLocations(this.contextLoader, testClazz);
        }
    }


    public void loadApplicationContexts(GenericApplicationContext globalCtx) throws Exception
    {
        if(this.locations == null)
        {
            return;
        }
        for(String location : this.locations)
        {
            try
            {
                loadCustomContext(globalCtx, location);
            }
            catch(BeanDefinitionStoreException e)
            {
                log.error("Error while loading application context file " + location, (Throwable)e);
            }
        }
    }


    public void loadApplicationContextByConvention(GenericApplicationContext globalCtx)
    {
        for(Extension extension : ExtensionManager.getInstance().getExtensions())
        {
            String location = "classpath:/" + extension.getName() + "-spring-test-context.xml";
            try
            {
                loadCustomContext(globalCtx, location);
            }
            catch(BeanDefinitionStoreException e)
            {
                log.debug("No test spring context for " + extension.getName());
            }
        }
    }


    private void loadCustomContext(GenericApplicationContext globalCtx, String location)
    {
        File file = new File(location);
        if(file.isFile() && file.exists())
        {
            log.debug("Loading Spring config from (" + location + ")");
            XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader((BeanDefinitionRegistry)globalCtx);
            xmlReader.loadBeanDefinitions((Resource)new FileSystemResource(location));
        }
        else
        {
            String resourceLocation = location;
            if(resourceLocation.charAt(0) != '/')
            {
                resourceLocation = "/" + resourceLocation;
            }
            if(Registry.class.getResource(resourceLocation) != null)
            {
                XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader((BeanDefinitionRegistry)globalCtx);
                xmlReader.loadBeanDefinitions((Resource)new ClassPathResource(resourceLocation, Registry.class.getClassLoader()));
            }
            else
            {
                log.info("Loading Spring config from (" + resourceLocation + ")");
                XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader((BeanDefinitionRegistry)globalCtx);
                xmlReader.loadBeanDefinitions(location);
            }
        }
    }


    protected String[] retrieveContextLocations(ContextLoader contextLoader, Class<?> clazz)
    {
        Assert.notNull(contextLoader, "ContextLoader must not be null");
        Assert.notNull(clazz, "Class must not be null");
        List<String> locationsList = new ArrayList<>();
        Class<ContextConfiguration> annotationType = ContextConfiguration.class;
        Class<?> declaringClass = AnnotationUtils.findAnnotationDeclaringClass(annotationType, clazz);
        Assert.notNull(declaringClass, "Could not find an 'annotation declaring class' for annotation type [" + annotationType + "] and class [" + clazz + "]");
        while(declaringClass != null)
        {
            ContextConfiguration contextConfiguration = declaringClass.<ContextConfiguration>getAnnotation(annotationType);
            if(log.isTraceEnabled())
            {
                log.trace("Retrieved @ContextConfiguration [" + contextConfiguration + "] for declaring class [" + declaringClass + "]");
            }
            String[] locations = contextLoader.processLocations(declaringClass, contextConfiguration.locations());
            locationsList.addAll(0, Arrays.asList(locations));
            declaringClass = contextConfiguration.inheritLocations() ? AnnotationUtils.findAnnotationDeclaringClass(annotationType, declaringClass.getSuperclass()) : null;
        }
        return locationsList.<String>toArray(new String[locationsList.size()]);
    }
}
