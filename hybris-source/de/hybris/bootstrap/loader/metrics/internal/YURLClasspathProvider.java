package de.hybris.bootstrap.loader.metrics.internal;

import de.hybris.bootstrap.loader.PlatformInPlaceClassLoader;
import de.hybris.bootstrap.loader.metrics.ClassLoaderMetricException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class YURLClasspathProvider
{
    public List<String> getClasspath()
    {
        try
        {
            List<String> classpath = new ArrayList<>();
            Class<?> registryClass = Thread.currentThread().getContextClassLoader().loadClass("de.hybris.platform.core.Registry");
            if(registryClass.getClassLoader() instanceof PlatformInPlaceClassLoader)
            {
                PlatformInPlaceClassLoader classLoader = (PlatformInPlaceClassLoader)registryClass.getClassLoader();
                for(URL url : classLoader.getInPlaceURLs())
                {
                    classpath.add(url.getFile());
                }
                return classpath;
            }
            return Collections.emptyList();
        }
        catch(ClassNotFoundException e)
        {
            throw new ClassLoaderMetricException("Failed to read YURLClassLoader classpath.", e);
        }
    }
}
