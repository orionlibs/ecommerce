package de.hybris.platform.masterserver.collector.system.impl.spring;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

class BeanInfoProvider
{
    private final Function<String, Optional<String>> beanNameToDeclaringExtensionName;
    private final Function<String, Optional<Class<?>>> beanNameToDeclaredType;
    private final Function<String, Collection<String>> beanNameToDeclaredAliases;


    BeanInfoProvider(Function<String, Optional<String>> beanNameToDeclaringExtensionName, Function<String, Optional<Class<?>>> beanNameToDeclaredType, Function<String, Collection<String>> beanNameToDeclaredAliases)
    {
        this.beanNameToDeclaringExtensionName = Objects.<Function<String, Optional<String>>>requireNonNull(beanNameToDeclaringExtensionName);
        this.beanNameToDeclaredType = Objects.<Function<String, Optional<Class<?>>>>requireNonNull(beanNameToDeclaredType);
        this.beanNameToDeclaredAliases = Objects.<Function<String, Collection<String>>>requireNonNull(beanNameToDeclaredAliases);
    }


    public static BeanInfoProvider from(ConfigurableListableBeanFactory beanFactory)
    {
        Objects.requireNonNull(beanFactory);
        ExtensionNameFromPathExtractor pathToExtensionCache = ExtensionNameFromPathExtractor.fromConfig();
        PathFromBeanDefinitionExtractor pathExtractor = new PathFromBeanDefinitionExtractor();
        Objects.requireNonNull(pathToExtensionCache);
        Objects.requireNonNull(pathExtractor);
        BeanDataFromBeanFactoryExtractor extractor = new BeanDataFromBeanFactoryExtractor(beanFactory, pathToExtensionCache::getExtensionName, pathExtractor::getPath);
        Objects.requireNonNull(extractor);
        Objects.requireNonNull(extractor);
        Objects.requireNonNull(extractor);
        return new BeanInfoProvider(extractor::getDeclaringExtensionName, extractor::getDeclaredType, extractor::getDeclaredAliases);
    }


    public Optional<BeanInfo> getBeanInfo(String beanName)
    {
        Optional<String> extensionName = getDeclaringExtensionName(beanName);
        if(extensionName.isEmpty())
        {
            return Optional.empty();
        }
        Optional<Class<?>> type = getDeclaredType(beanName);
        if(type.isEmpty())
        {
            return Optional.empty();
        }
        Collection<String> aliases = getDeclaredAliases(beanName);
        return Optional.of(new BeanInfo(beanName, extensionName.get(), type.get(), aliases));
    }


    private Optional<String> getDeclaringExtensionName(String beanName)
    {
        return this.beanNameToDeclaringExtensionName.apply(beanName);
    }


    private Optional<Class<?>> getDeclaredType(String beanName)
    {
        return this.beanNameToDeclaredType.apply(beanName);
    }


    private Collection<String> getDeclaredAliases(String beanName)
    {
        return this.beanNameToDeclaredAliases.apply(beanName);
    }
}
