/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.impl;

import com.hybris.cockpitng.core.AbstractCockpitComponentDefinition;
import com.hybris.cockpitng.core.CockpitComponentDefinitionFactory;
import com.hybris.cockpitng.core.CockpitComponentDefinitionService;
import com.hybris.cockpitng.core.CockpitComponentInfo;
import com.hybris.cockpitng.core.CockpitComponentLoader;
import com.hybris.cockpitng.core.WidgetDefinition;
import com.hybris.cockpitng.core.WidgetSocket;
import com.hybris.cockpitng.core.spring.CockpitApplicationContext;
import com.hybris.cockpitng.core.util.CockpitProperties;
import com.hybris.cockpitng.core.util.Resettable;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.core.util.impl.TypedSettingsMap;
import com.hybris.cockpitng.dependencies.factory.impl.SpringApplicationContextInjectableObjectFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * This implementation of the {@link CockpitComponentDefinitionService} has a list of {@link CockpitComponentLoader}s to
 * load widget definitions using them. It provides all {@link WidgetDefinition}s from all {@link CockpitComponentLoader}
 * s it holds.
 */
public class DefaultCockpitComponentDefinitionService implements CockpitComponentDefinitionService, ApplicationContextAware
{
    public static final String DEFAULT_VIEW_ZUL = "view.zul";
    public static final String NO_VIEW_ZUL = "none";
    public static final String PROPERTY_WIDGET_LIBRARY_BLACKLIST = "widget.library.blacklist";
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCockpitComponentDefinitionService.class);
    private static final String WIDGET_STUB_PREFIX = "STUB_";
    private final Set<String> filled = new HashSet<>();
    private final Map<String, WidgetDefinition> stubWidgetDefinitions = new LinkedHashMap<>();
    private final Map<AbstractCockpitComponentDefinition, CockpitComponentInfo> definitionsToInfos = new HashMap<>();
    private final ConcurrentMap<String, ComponentLazyFactory> cachedAutowiredComponents = new ConcurrentHashMap<>();
    private final Map<String, AbstractCockpitComponentDefinition> componentDefinitions = new HashMap<>();
    private CockpitApplicationContext appctx;
    private List<CockpitComponentLoader> definitionLoaders;
    private Map<String, CockpitComponentDefinitionFactory> factories;
    private CockpitProperties cockpitProperties;
    private List<Resettable> toResetAfterReload;


    @Override
    public Map<String, WidgetDefinition> getStubWidgetDefinitions()
    {
        assureDefinitionsLoaded();
        return Collections.unmodifiableMap(this.stubWidgetDefinitions);
    }


    @Override
    public <T> T createAutowiredComponent(final AbstractCockpitComponentDefinition root, final String className,
                    final ApplicationContext context) throws ReflectiveOperationException
    {
        return createAutowiredComponent(root, className, context, true);
    }


    @Override
    public <T> T createAutowiredComponent(final AbstractCockpitComponentDefinition root, final String className,
                    final ApplicationContext context, final boolean cachedInstance) throws ReflectiveOperationException
    {
        if(cachedInstance && !isStubWidget(String.format("%s%s", WIDGET_STUB_PREFIX, root.getCode())))
        {
            return cachedAutowiredComponents.computeIfAbsent(className, k -> new ComponentLazyFactory(this))
                            .createIfAbsentAndGet(root, className, context);
        }
        else
        {
            return createClassInstance(context, Class.forName(className, false, getApplicationContext().getClassLoader()));
        }
    }


    protected <T> T createClassInstance(final ApplicationContext context, final Class clazz)
                    throws InstantiationException, IllegalAccessException
    {
        if(context == null)
        {
            return (T)clazz.newInstance();
        }
        return (T)new SpringApplicationContextInjectableObjectFactory<>(context, clazz).createAndInjectDependencies();
    }


    @Override
    public List<AbstractCockpitComponentDefinition> getAllComponentDefinitions()
    {
        assureDefinitionsLoaded();
        return new ArrayList<>(this.componentDefinitions.values());
    }


    @Override
    public <T extends AbstractCockpitComponentDefinition> List<T> getComponentDefinitionsByClass(final Class<T> componentType)
    {
        assureDefinitionsLoaded();
        final List<T> result = new ArrayList<>();
        for(final AbstractCockpitComponentDefinition def : this.componentDefinitions.values())
        {
            if(componentType.isAssignableFrom(def.getClass()))
            {
                result.add((T)def);
            }
        }
        return result;
    }


    @Override
    public AbstractCockpitComponentDefinition getComponentDefinitionForCode(final String code)
    {
        assureDefinitionsLoaded();
        return this.componentDefinitions.get(code);
    }


    @Override
    public <T extends AbstractCockpitComponentDefinition> T getComponentDefinitionForCode(final String code,
                    final Class<T> componentType)
    {
        final AbstractCockpitComponentDefinition componentDefinitionForCode = getComponentDefinitionForCode(code);
        if(componentDefinitionForCode != null && componentType.isAssignableFrom(componentDefinitionForCode.getClass()))
        {
            return (T)componentDefinitionForCode;
        }
        else
        {
            return null;
        }
    }


    @Override
    public Collection<AbstractCockpitComponentDefinition> getExtensionsDefinitions()
    {
        return definitionsToInfos.keySet().stream()
                        .filter(definition -> StringUtils.isBlank(definition.getCode()) && StringUtils.isNotBlank(definition.getParentCode()))
                        .filter(definition -> definition instanceof WidgetDefinition).collect(Collectors.toList());
    }


    public void setDefinitionFactories(final Map<String, CockpitComponentDefinitionFactory> factories)
    {
        this.factories = factories;
    }


    private void assureDefinitionsLoaded()
    {
        if(this.componentDefinitions.isEmpty())
        {
            loadDefinitions();
        }
    }


    @Override
    public synchronized void reloadDefinitions()
    {
        clearDefinitions();
        loadDefinitions();
        if(toResetAfterReload != null)
        {
            toResetAfterReload.forEach(com.hybris.cockpitng.core.util.Resettable::reset);
        }
    }


    protected synchronized void loadDefinitions()
    {
        if(CollectionUtils.isNotEmpty(this.definitionLoaders))
        {
            final Collection<String> widgetBlacklist = fetchWidgetBlacklist();
            for(final CockpitComponentLoader loader : this.definitionLoaders)
            {
                final Map<String, String> componentPaths = new HashMap<>();
                loadInfos(loader).forEach(info -> processComponentInfo(info, widgetBlacklist, definition -> {
                    if(componentPaths.containsKey(info.getRootPath()))
                    {
                        LOGGER.error("Duplicated component root path for {}: {}@{} and {}", definition.getCode(), definition.getName(),
                                        definition.getDeclaringModule(), componentPaths.get(info.getRootPath()));
                        return false;
                    }
                    else
                    {
                        componentPaths.put(info.getRootPath(),
                                        String.format("%s@%s", definition.getName(), definition.getDeclaringModule()));
                        return true;
                    }
                }));
            }
            filled.clear();
            processInheritanceHierarchy();
            processExtensions();
            this.definitionsToInfos.forEach((definition, info) -> {
                final Optional<CockpitComponentDefinitionFactory> factory = getFactoryForInfo(info);
                factory.ifPresent(definitionFactory -> definitionFactory.initialize(info, definition));
            });
        }
    }


    protected void processComponentInfo(final CockpitComponentInfo info, final Collection<String> widgetBlacklist,
                    final Predicate<AbstractCockpitComponentDefinition> definitionTest)
    {
        beforeCreateDefinition(info);
        final Optional<AbstractCockpitComponentDefinition> componentDefinition = loadDefinitionForComponentInfo(info,
                        widgetBlacklist);
        if(componentDefinition.isPresent())
        {
            final AbstractCockpitComponentDefinition definition = componentDefinition.get();
            if(definitionTest.test(definition))
            {
                registerWidgetStubDefinitions(definition);
                if(StringUtils.isNotBlank(definition.getCode()))
                {
                    this.componentDefinitions.put(definition.getCode(), definition);
                }
                this.definitionsToInfos.put(definition, info);
            }
        }
    }


    protected Optional<AbstractCockpitComponentDefinition> loadDefinitionForComponentInfo(final CockpitComponentInfo info,
                    final Collection<String> widgetBlacklist)
    {
        final Optional<CockpitComponentDefinitionFactory> factory = getFactoryForInfo(info);
        if(factory.isPresent())
        {
            final AbstractCockpitComponentDefinition definition = factory.get().create(info);
            if(isBlacklisted(definition, widgetBlacklist))
            {
                LOGGER.debug("Skipping blacklisted component: {}", definition);
                return Optional.empty();
            }
            else if(isDuplicate(definition))
            {
                final AbstractCockpitComponentDefinition previous = getComponentDefinitionForCode(definition.getCode());
                if(previous != null)
                {
                    LOGGER.error("Duplicated component definition for {}: {}@{} and {}@{}", definition.getCode(), definition.getName(),
                                    definition.getDeclaringModule(), previous.getName(), previous.getDeclaringModule());
                }
                else
                {
                    LOGGER.error("Duplicated component definition for {}: {}@{}", definition.getCode(), definition.getName(),
                                    definition.getDeclaringModule());
                }
                return Optional.empty();
            }
            else
            {
                return Optional.of(definition);
            }
        }
        else
        {
            LOGGER.error("Unable to determine component info factory for {}", info.getProperties());
            return Optional.empty();
        }
    }


    protected boolean isBlacklisted(final AbstractCockpitComponentDefinition definition, final Collection<String> blacklist)
    {
        Validate.notNull("Definition may not be null", definition);
        if(blacklist != null)
        {
            return blacklist.contains(definition.getCode()) || blacklist.contains(definition.getParentCode());
        }
        return false;
    }


    protected Collection<String> fetchWidgetBlacklist()
    {
        final Set<String> result = new HashSet<>();
        final String property = cockpitProperties.getProperty(PROPERTY_WIDGET_LIBRARY_BLACKLIST);
        if(StringUtils.isNotBlank(property))
        {
            final String[] rawIds = StringUtils.split(property, ',');
            for(final String rawId : rawIds)
            {
                final String id = rawId.trim();
                if(StringUtils.isNotBlank(id))
                {
                    result.add(id);
                }
            }
        }
        return result;
    }


    protected boolean isDuplicate(final AbstractCockpitComponentDefinition definition)
    {
        return this.componentDefinitions.containsKey(definition.getCode());
    }


    @Override
    public synchronized void clearDefinitions()
    {
        this.componentDefinitions.clear();
        this.definitionsToInfos.clear();
        this.stubWidgetDefinitions.clear();
    }


    protected void registerWidgetStubDefinitions(final AbstractCockpitComponentDefinition definition)
    {
        if(requiresWidgetStub(definition))
        {
            final WidgetDefinition widgetStubDefinition = new WidgetDefinition();
            widgetStubDefinition.setCode(WIDGET_STUB_PREFIX + definition.getCode());
            widgetStubDefinition.setInputs(definition.getInputs());
            widgetStubDefinition.setOutputs(definition.getOutputs());
            widgetStubDefinition.setViewURI(NO_VIEW_ZUL);
            widgetStubDefinition.setStubWidget(true);
            this.stubWidgetDefinitions.put(widgetStubDefinition.getCode(), widgetStubDefinition);
            this.componentDefinitions.put(widgetStubDefinition.getCode(), widgetStubDefinition);
        }
    }


    private static boolean requiresWidgetStub(final AbstractCockpitComponentDefinition definition)
    {
        boolean ret = false;
        if(!(definition instanceof WidgetDefinition))
        {
            ret = CollectionUtils.isNotEmpty(definition.getInputs()) || CollectionUtils.isNotEmpty(definition.getOutputs());
        }
        return ret;
    }


    /**
     * Override to do manipulations to component info before creation of definition
     *
     * @param info
     */
    protected void beforeCreateDefinition(final CockpitComponentInfo info)
    {
        // NOP
    }


    protected Set<CockpitComponentInfo> loadInfos(final CockpitComponentLoader loader)
    {
        return loader.load();
    }


    protected void processInheritanceHierarchy()
    {
        for(final AbstractCockpitComponentDefinition componentDefinition : this.componentDefinitions.values())
        {
            if(componentDefinition instanceof WidgetDefinition)
            {
                final WidgetDefinition def = (WidgetDefinition)componentDefinition;
                final String parentDefinitionID = def.getParentCode();
                if(StringUtils.isNotBlank(parentDefinitionID))
                {
                    fillInheritedFields(def);
                }
            }
        }
    }


    protected void fillInheritedFields(final WidgetDefinition definition)
    {
        final AbstractCockpitComponentDefinition parentDef = componentDefinitions.get(definition.getParentCode());
        if(parentDef instanceof WidgetDefinition)
        {
            if(!filled.contains(parentDef.getCode()))
            {
                fillInheritedFields((WidgetDefinition)this.componentDefinitions.get(parentDef.getCode()));
            }
            final WidgetDefinition parent = (WidgetDefinition)parentDef;
            fillInheritedDefinition(definition, parent, function -> checkIfEmpty(definition, function));
            final TypedSettingsMap settings = fillInheritedSettings(definition, parent);
            definition.setDefaultSettings(settings);
            // in order to minimize upgrade issues - we cannot override view uri
            definition.setViewURI(parent.getViewURI());
            filled.add(definition.getCode());
        }
    }


    protected void processExtensions()
    {
        getExtensionsDefinitions().forEach(extension -> {
            final AbstractCockpitComponentDefinition parentDefinition = componentDefinitions.get(extension.getParentCode());
            if(!(parentDefinition instanceof WidgetDefinition))
            {
                LOGGER.error("Could not find parent widget {}", extension.getParentCode());
            }
            else
            {
                fillInheritedDefinition((WidgetDefinition)parentDefinition, (WidgetDefinition)extension,
                                function -> !checkIfEmpty((WidgetDefinition)extension, function));
                final TypedSettingsMap settings = fillInheritedSettings((WidgetDefinition)extension,
                                (WidgetDefinition)parentDefinition);
                parentDefinition.setDefaultSettings(settings);
            }
        });
    }


    protected void fillInheritedDefinition(final WidgetDefinition definition, final WidgetDefinition extension,
                    final Predicate<Function<WidgetDefinition, ?>> overrideCheck)
    {
        this.inheritFieldValue(definition, extension, overrideCheck, WidgetDefinition::getController, definition::setController);
        this.inheritFieldValue(definition, extension, overrideCheck, WidgetDefinition::getCategoryTag, definition::setCategoryTag);
        this.inheritFieldValue(definition, extension, overrideCheck, WidgetDefinition::getControllerID,
                        definition::setControllerID);
        this.inheritFieldValue(definition, extension, overrideCheck, WidgetDefinition::getDefaultTitle,
                        definition::setDefaultTitle);
        this.inheritFieldValue(definition, extension, overrideCheck, WidgetDefinition::getForwardMap, definition::setForwardMap);
        fillInheritedSockets(definition, extension);
    }


    private void fillInheritedSockets(final WidgetDefinition definition, final WidgetDefinition extension)
    {
        final List<WidgetSocket> inputs = new ArrayList<>(definition.getInputs());
        final List<WidgetSocket> outputs = new ArrayList<>(definition.getOutputs());
        for(final WidgetSocket input : extension.getInputs())
        {
            if(!inputs.stream().map(WidgetSocket::getId).collect(Collectors.toList()).contains(input.getId()))
            {
                inputs.add(input);
            }
        }
        for(final WidgetSocket output : extension.getOutputs())
        {
            if(!outputs.stream().map(WidgetSocket::getId).collect(Collectors.toList()).contains(output.getId()))
            {
                outputs.add(output);
            }
        }
        definition.setInputs(inputs);
        definition.setOutputs(outputs);
    }


    protected TypedSettingsMap fillInheritedSettings(final WidgetDefinition override, final WidgetDefinition base)
    {
        final TypedSettingsMap settingsOverride = override.getDefaultSettings();
        final TypedSettingsMap settings = new TypedSettingsMap();
        if(MapUtils.isNotEmpty(base.getDefaultSettings()))
        {
            for(final String settingKey : base.getDefaultSettings().keySet())
            {
                settings.put(settingKey, base.getDefaultSettings().get(settingKey));
            }
        }
        if(MapUtils.isNotEmpty(settingsOverride))
        {
            settings.putAll(settingsOverride);
        }
        return settings;
    }


    protected <T> void inheritFieldValue(final WidgetDefinition definition, final WidgetDefinition extension,
                    final Predicate<Function<WidgetDefinition, ?>> overrideCheck, final Function<WidgetDefinition, T> getter,
                    final Consumer<T> setter)
    {
        if(overrideCheck.test(getter))
        {
            final T value = getter.apply(extension);
            setter.accept(value);
        }
    }


    protected boolean checkIfEmpty(final WidgetDefinition target, final Function<WidgetDefinition, ?> override)
    {
        final Object value = override.apply(target);
        if(value instanceof String)
        {
            return StringUtils.isEmpty((String)value);
        }
        else if(value instanceof Map)
        {
            return MapUtils.isEmpty((Map)value);
        }
        else if(value instanceof Collection)
        {
            return CollectionUtils.isEmpty((Collection)value);
        }
        else
        {
            return value == null;
        }
    }


    protected Optional<CockpitComponentDefinitionFactory> getFactoryForInfo(final CockpitComponentInfo info)
    {
        CockpitComponentDefinitionFactory factory = null;
        final String type = info.getProperties().getProperty("component-type");
        if(!StringUtils.isBlank(type))
        {
            factory = this.factories.get(type);
        }
        if(factory == null)
        {
            LOGGER.error("Unable to determine component factory for: {}", info.getProperties());
        }
        return Optional.ofNullable(factory);
    }


    /**
     * @deprecated since 1808, please use {@link CockpitApplicationContext#getClassLoader()}
     */
    @Override
    @Deprecated(since = "1808", forRemoval = true)
    public synchronized ClassLoader getClassLoader(final AbstractCockpitComponentDefinition definition)
    {
        return getApplicationContext().getClassLoader();
    }


    private boolean isStubWidget(final String widgetId)
    {
        return getStubWidgetDefinitions().containsKey(widgetId);
    }


    /**
     * @return the definitionLoaders
     */
    public List<CockpitComponentLoader> getDefinitionLoaders()
    {
        return definitionLoaders;
    }


    public void setDefinitionLoaders(final List<CockpitComponentLoader> definitionLoaders)
    {
        this.definitionLoaders = definitionLoaders;
    }


    @Override
    public ApplicationContext getApplicationContext(final AbstractCockpitComponentDefinition definition)
    {
        return getApplicationContext();
    }


    protected CockpitApplicationContext getApplicationContext()
    {
        return this.appctx;
    }


    @Override
    public void setApplicationContext(final ApplicationContext ctx)
    {
        this.appctx = CockpitApplicationContext.getCockpitApplicationContext(ctx);
    }


    public CockpitProperties getCockpitProperties()
    {
        return cockpitProperties;
    }


    @Required
    public void setCockpitProperties(final CockpitProperties cockpitProperties)
    {
        this.cockpitProperties = cockpitProperties;
    }


    public void setToResetAfterReload(final List<Resettable> toResetAfterReload)
    {
        this.toResetAfterReload = toResetAfterReload;
    }


    private static class ComponentLazyFactory
    {
        private final DefaultCockpitComponentDefinitionService service;
        private Object instance;


        public ComponentLazyFactory(final DefaultCockpitComponentDefinitionService service)
        {
            this.service = service;
        }


        public synchronized <T> T createIfAbsentAndGet(final AbstractCockpitComponentDefinition root, final String className,
                        final ApplicationContext context) throws ReflectiveOperationException
        {
            if(instance == null)
            {
                instance = service.createClassInstance(context,
                                Class.forName(className, false, service.getApplicationContext().getClassLoader()));
            }
            return (T)instance;
        }
    }
}
