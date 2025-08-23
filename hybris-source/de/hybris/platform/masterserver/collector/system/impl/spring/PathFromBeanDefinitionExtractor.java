package de.hybris.platform.masterserver.collector.system.impl.spring;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;

class PathFromBeanDefinitionExtractor
{
    private static final Logger LOGGER = LoggerFactory.getLogger(PathFromBeanDefinitionExtractor.class);


    public Optional<Path> getPath(BeanDefinition definition)
    {
        if(definition == null)
        {
            return Optional.empty();
        }
        if(definition instanceof AbstractBeanDefinition)
        {
            Resource resource = ((AbstractBeanDefinition)definition).getResource();
            if(resource == null)
            {
                LOGGER.debug("No resource has been found for `{}`. {}", definition.getClass(), definition);
                return Optional.empty();
            }
            try
            {
                if(resource instanceof org.springframework.core.io.ClassPathResource || resource instanceof org.springframework.core.io.UrlResource)
                {
                    URL resourceURL = ResourceUtils.extractJarFileURL(resource.getURL());
                    return Optional.of(Path.of(resourceURL.getPath(), new String[0]));
                }
                if(resource instanceof org.springframework.core.io.FileSystemResource)
                {
                    return Optional.of(resource.getFile().toPath());
                }
            }
            catch(IOException | RuntimeException e)
            {
                LOGGER.debug("Failed to obtain the the path from `{}`.", resource.getClass());
                return Optional.empty();
            }
            LOGGER.debug("Unsupported resource `{}`.", resource.getClass());
        }
        else
        {
            LOGGER.debug("Unsupported definition `{}`.", definition.getClass());
        }
        return Optional.empty();
    }
}
