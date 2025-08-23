/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.collectionbrowser.mold.impl;

import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.core.config.impl.jaxb.collectionbrowser.CollectionBrowser;
import com.hybris.cockpitng.core.config.impl.jaxb.collectionbrowser.Mold;
import com.hybris.cockpitng.core.config.impl.jaxb.collectionbrowser.MoldList;
import com.hybris.cockpitng.core.util.impl.TypedSettingsMap;
import com.hybris.cockpitng.util.BackofficeSpringUtil;
import com.hybris.cockpitng.widgets.collectionbrowser.CollectionBrowserController;
import com.hybris.cockpitng.widgets.collectionbrowser.mold.CollectionBrowserDelegateController;
import com.hybris.cockpitng.widgets.collectionbrowser.mold.CollectionBrowserMoldContext;
import com.hybris.cockpitng.widgets.collectionbrowser.mold.CollectionBrowserMoldStrategy;
import com.hybris.cockpitng.widgets.collectionbrowser.mold.DefaultCollectionBrowserContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

public class CollectionBrowserConfigurationLoader implements CollectionBrowserDelegateController
{
    private static final Logger LOG = LoggerFactory.getLogger(CollectionBrowserConfigurationLoader.class);
    private CollectionBrowserController controller;


    public CollectionBrowserConfigurationLoader()
    {
    }


    /**
     * @deprecated since 6.7 - not used anymore, please use @link{{@link #CollectionBrowserConfigurationLoader()}}
     */
    @Deprecated(since = "6.7", forRemoval = true)
    public CollectionBrowserConfigurationLoader(final CollectionBrowserController controller)
    {
        this.controller = controller;
    }


    public List<CollectionBrowserMoldStrategy> loadAvailableMolds()
    {
        final List<CollectionBrowserMoldStrategy> availableMolds = new ArrayList<>(loadAvailableMoldsMap().keySet());
        Collections.sort(availableMolds, AnnotationAwareOrderComparator.INSTANCE);
        return availableMolds;
    }


    protected Map<CollectionBrowserMoldStrategy, Mold> loadAvailableMoldsMap()
    {
        final String currentTypeCode = controller.getCurrentTypeCode();
        final CollectionBrowser collectionBrowserConfiguration = loadCollectionBrowserConfiguration(currentTypeCode);
        final MoldList moldList = collectionBrowserConfiguration.getAvailableMolds();
        final Map<CollectionBrowserMoldStrategy, Mold> availableMolds;
        if(moldList == null)
        {
            availableMolds = getAllBeans(CollectionBrowserMoldStrategy.class).entrySet().stream().collect(
                            HashMap::new, (m, v) -> m.put(v.getValue(), null), HashMap::putAll);
            availableMolds.keySet().forEach(this::initializeMoldStrategy);
        }
        else
        {
            availableMolds = new HashMap<>();
            moldList.getMold().forEach(mold -> {
                final Optional<CollectionBrowserMoldStrategy> optionalMoldStrategy = getCollectionBrowserMoldStrategy(mold);
                optionalMoldStrategy.ifPresent(moldStrategy -> availableMolds.put(moldStrategy, mold));
            });
        }
        return availableMolds;
    }


    protected <T> Map<String, T> getAllBeans(final Class<T> clazz)
    {
        return BackofficeSpringUtil.getAllBeans(clazz);
    }


    public Boolean loadEnableMultiSelect()
    {
        final String currentTypeCode = controller.getCurrentTypeCode();
        final CollectionBrowser collectionBrowserConfiguration = loadCollectionBrowserConfiguration(currentTypeCode);
        return collectionBrowserConfiguration.isEnableMultiSelect();
    }


    public Boolean loadEnableMultiSelect(final CollectionBrowserMoldStrategy moldStrategy)
    {
        if(moldStrategy != null)
        {
            final Map<CollectionBrowserMoldStrategy, Mold> moldStrategyMoldMap = loadAvailableMoldsMap();
            final Mold mold = moldStrategyMoldMap.entrySet().stream()
                            .filter(entry -> entry.getValue() != null)
                            .filter(entry -> entry.getKey().getName().equals(moldStrategy.getName())).map(Map.Entry::getValue).findFirst()
                            .orElse(null);
            if(mold != null && mold.isEnableMultiSelect() != null)
            {
                return mold.isEnableMultiSelect();
            }
        }
        return loadEnableMultiSelect();
    }


    public String loadDefaultMold()
    {
        final String currentTypeCode = controller.getCurrentTypeCode();
        final CollectionBrowser collectionBrowser = loadCollectionBrowserConfiguration(currentTypeCode);
        final MoldList availableMolds = collectionBrowser.getAvailableMolds();
        if(availableMolds != null)
        {
            return availableMolds.getDefaultMold();
        }
        return null;
    }


    protected CollectionBrowser loadCollectionBrowserConfiguration(final String typeCode)
    {
        final TypedSettingsMap settings = controller.getWidgetInstanceManager().getWidgetSettings();
        final String defaultConfigCtx = CollectionBrowserController.DEFAULT_COLLECTION_BROWSER_CONFIG_CTX;
        final String readConfigCtx = settings.getString(CollectionBrowserController.SETTING_COLLECTION_BROWSER_CONFIG_CTX);
        final String collectionBrowserConfigCtx = StringUtils.defaultIfBlank(readConfigCtx, defaultConfigCtx);
        final DefaultConfigContext context = new DefaultConfigContext(collectionBrowserConfigCtx, typeCode);
        try
        {
            return controller.getWidgetInstanceManager().loadConfiguration(context, CollectionBrowser.class);
        }
        catch(final CockpitConfigurationException e)
        {
            LOG.warn(String.format("Could not load Collection Browser configuration for type [%s] ", typeCode), e);
        }
        return emptyCollectionBrowserConfiguration();
    }


    protected CollectionBrowser emptyCollectionBrowserConfiguration()
    {
        final CollectionBrowser configuration = new CollectionBrowser();
        configuration.setAvailableMolds(new MoldList());
        return configuration;
    }


    protected List<CollectionBrowserMoldStrategy> parseAvailableMoldsConfiguration(final List<Mold> moldConfigurations)
    {
        final List<CollectionBrowserMoldStrategy> moldStrategies = new ArrayList<>();
        if(moldConfigurations != null)
        {
            for(final Mold moldConfiguration : moldConfigurations)
            {
                final Optional<CollectionBrowserMoldStrategy> collectionBrowserMoldStrategy = getCollectionBrowserMoldStrategy(
                                moldConfiguration);
                collectionBrowserMoldStrategy.ifPresent(moldStrategies::add);
            }
        }
        return moldStrategies;
    }


    protected Optional<CollectionBrowserMoldStrategy> getCollectionBrowserMoldStrategy(final Mold moldConfiguration)
    {
        try
        {
            final String moldBeanName = moldConfiguration.getSpringBean();
            final String moldClassName = moldConfiguration.getClazz();
            validateMoldElement(moldBeanName, moldClassName);
            return Optional.ofNullable(instantiateMoldStrategy(moldBeanName, moldClassName));
        }
        catch(final IllegalStateException e)
        {
            final String msgFormat = "Error loading Collection Browser mold configuration: [class: %s], [spring-bean: %s]";
            LOG.warn(String.format(msgFormat, moldConfiguration.getClazz(), moldConfiguration.getSpringBean()), e);
            return Optional.empty();
        }
    }


    protected void validateMoldElement(final String springBeanName, final String className) throws IllegalStateException
    {
        final String simpleName = CollectionBrowserMoldStrategy.class.getSimpleName();
        if(StringUtils.isNotBlank(springBeanName) && StringUtils.isNotBlank(className))
        {
            final String msg = simpleName.concat(" is allowed to have only class or spring-bean defined, not both.");
            throw new IllegalStateException(msg);
        }
        else if(StringUtils.isBlank(springBeanName) && StringUtils.isBlank(className))
        {
            final String msg = simpleName.concat(" is missing class definition. Use 'class' or 'spring-bean' attribute.");
            throw new IllegalStateException(msg);
        }
    }


    protected CollectionBrowserMoldStrategy instantiateMoldStrategy(final String moldBeanName, final String moldClassName)
    {
        final CollectionBrowserMoldStrategy moldInstance;
        if(StringUtils.isNotBlank(moldBeanName))
        {
            moldInstance = BackofficeSpringUtil.getBean(moldBeanName);
        }
        else
        {
            moldInstance = BackofficeSpringUtil.createClassInstance(moldClassName, CollectionBrowserMoldStrategy.class);
        }
        initializeMoldStrategy(moldInstance);
        return moldInstance;
    }


    protected void initializeMoldStrategy(final CollectionBrowserMoldStrategy moldInstance)
    {
        moldInstance.setContext(createMoldContext());
    }


    protected CollectionBrowserMoldContext createMoldContext()
    {
        return new DefaultCollectionBrowserContext(controller);
    }


    protected CollectionBrowserController getController()
    {
        return controller;
    }


    @Override
    public void setController(final CollectionBrowserController controller)
    {
        this.controller = controller;
    }
}
