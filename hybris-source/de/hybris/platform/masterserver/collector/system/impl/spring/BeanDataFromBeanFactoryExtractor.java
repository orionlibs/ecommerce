package de.hybris.platform.masterserver.collector.system.impl.spring;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

class BeanDataFromBeanFactoryExtractor
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BeanDataFromBeanFactoryExtractor.class);
    private final ConfigurableListableBeanFactory beanFactory;
    private final Function<Path, Optional<String>> pathToExtensionName;
    private final Function<BeanDefinition, Optional<Path>> pathExtractor;


    BeanDataFromBeanFactoryExtractor(ConfigurableListableBeanFactory beanFactory, Function<Path, Optional<String>> pathToExtensionName, Function<BeanDefinition, Optional<Path>> pathExtractor)
    {
        this.beanFactory = Objects.<ConfigurableListableBeanFactory>requireNonNull(beanFactory);
        this.pathToExtensionName = Objects.<Function<Path, Optional<String>>>requireNonNull(pathToExtensionName);
        this.pathExtractor = Objects.<Function<BeanDefinition, Optional<Path>>>requireNonNull(pathExtractor);
    }


    public Optional<String> getDeclaringExtensionName(String beanName)
    {
        if(beanName == null || !this.beanFactory.containsBeanDefinition(beanName))
        {
            return Optional.empty();
        }
        Objects.requireNonNull(this.beanFactory);
        return handlePossibleBeansException(beanName, this.beanFactory::getBeanDefinition)
                        .<Path>flatMap(this.pathExtractor)
                        .flatMap(this.pathToExtensionName);
    }


    public Optional<Class<?>> getDeclaredType(String beanName)
    {
        Objects.requireNonNull(this.beanFactory);
        return handlePossibleBeansException(beanName, this.beanFactory::getType);
    }


    public Collection<String> getDeclaredAliases(String beanName)
    {
        if(beanName == null)
        {
            return Collections.emptyList();
        }
        return List.of(this.beanFactory.getAliases(beanName));
    }


    private <T> Optional<T> handlePossibleBeansException(String beanName, Function<String, T> function)
    {
        try
        {
            return Optional.<String>ofNullable(beanName).map(function);
        }
        catch(BeansException e)
        {
            LOGGER.debug("Failed to obtain data for `{}`.", beanName, e);
            return Optional.empty();
        }
    }
}
