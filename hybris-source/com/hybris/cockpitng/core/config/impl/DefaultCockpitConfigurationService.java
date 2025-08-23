/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl;

import com.ctc.wstx.exc.WstxLazyException;
import com.google.common.collect.Sets;
import com.hybris.cockpitng.core.config.CockpitConfigurationAdapter;
import com.hybris.cockpitng.core.config.CockpitConfigurationContextStrategy;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.CockpitConfigurationFallbackStrategy;
import com.hybris.cockpitng.core.config.CockpitConfigurationNotFoundException;
import com.hybris.cockpitng.core.config.CockpitConfigurationPersistenceStrategy;
import com.hybris.cockpitng.core.config.CockpitConfigurationService;
import com.hybris.cockpitng.core.config.CockpitConfigurationUnavailable;
import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.WidgetConfigurationContextDecorator;
import com.hybris.cockpitng.core.config.impl.cache.ConfigurationCache;
import com.hybris.cockpitng.core.config.impl.cache.ConfigurationContextResolver;
import com.hybris.cockpitng.core.config.impl.cache.ConfigurationSearchFilter;
import com.hybris.cockpitng.core.config.impl.cache.ContextAttributeComparator;
import com.hybris.cockpitng.core.config.impl.cache.ContextQuery;
import com.hybris.cockpitng.core.config.impl.jaxb.Config;
import com.hybris.cockpitng.core.config.impl.jaxb.Context;
import com.hybris.cockpitng.core.config.impl.model.ContextSearchNode;
import com.hybris.cockpitng.core.config.impl.model.ContextSearchNodeReference;
import com.hybris.cockpitng.core.config.impl.model.ContextSearchNodeRelevance;
import com.hybris.cockpitng.core.config.impl.model.ContextSearchProgress;
import com.hybris.cockpitng.core.config.impl.model.ContextSearchRequest;
import com.hybris.cockpitng.core.config.impl.model.ContextSearchRestriction;
import com.hybris.cockpitng.core.config.impl.model.ContextSearchTerms;
import com.hybris.cockpitng.core.config.impl.model.DefaultContextSearchRestriction;
import com.hybris.cockpitng.core.persistence.ConfigurationImportSupport;
import com.hybris.cockpitng.core.persistence.ConfigurationInterpreter;
import com.hybris.cockpitng.core.persistence.impl.jaxb.Import;
import com.hybris.cockpitng.core.persistence.impl.jaxb.Requirement;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.core.util.CockpitProperties;
import com.hybris.cockpitng.core.util.Resettable;
import com.hybris.cockpitng.core.util.impl.IdentityUtils;
import com.hybris.cockpitng.core.util.impl.MergeUtils;
import com.hybris.cockpitng.core.util.impl.StringBufferOutputStream;
import com.hybris.cockpitng.core.util.jaxb.JAXBContextFactory;
import com.hybris.cockpitng.core.util.jaxb.SchemaConfigValidator;
import com.hybris.cockpitng.core.util.jaxb.SchemaValidationStatus;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.StampedLock;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.dom.DOMResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Default implementation of the cockpit configuration service. Uses XML file to store to and load the configuration
 * from.
 */
public class DefaultCockpitConfigurationService implements CockpitConfigurationService, Resettable
{
    public static final String CHARSET_NAME = Charset.defaultCharset().toString();
    protected static final String PROPERTY_READ_ATTEMPT_TIMEOUT = "cockpitng.configuration.read.timeout";
    protected static final String PROPERTY_WRITE_ATTEMPT_TIMEOUT = "cockpitng.configuration.write.timeout";
    private static final Logger LOG = LoggerFactory.getLogger(DefaultCockpitConfigurationService.class);
    private static final long PROPERTY_DEFAULT_READ_ATTEMPT_TIMEOUT = 15L;
    private static final long PROPERTY_DEFAULT_WRITE_ATTEMPT_TIMEOUT = 45L;
    private final ThreadLocal<Boolean> isDelayedPersistEnabled = new ThreadLocal<>();
    private final ThreadLocal<Config> configToPersist = new ThreadLocal<>();
    private final ThreadLocal<LockState> readLockState = ThreadLocal.withInitial(LockState::new);
    private final ThreadLocal<LockState> writeLockState = ThreadLocal.withInitial(LockState::new);
    private final StampedLock lock = new StampedLock();
    private Runnable onCacheInvalidationCallback;
    /**
     * @deprecated since 2005
     */
    @Deprecated(since = "2005", forRemoval = true)
    private final Map<QName, QName> namespaceReplacements = new HashMap<>();
    protected List<String> obligatoryMergeAttributes = Collections.emptyList();
    private Map<String, CockpitConfigurationContextStrategy> contextStrategies = Collections.emptyMap();
    private Map<String, List<CockpitConfigurationFallbackStrategy>> fallbackStrategies = Collections.emptyMap();
    private Map<Map<String, String>, CockpitConfigurationAdapter<?>> adapters = Collections.emptyMap();
    private Map<String, CockpitConfigurationAdapter<?>> configTypesAdapters = Collections.emptyMap();
    private MergeUtils mergeUtils;
    private JAXBContextFactory jaxbContextFactory;
    private CockpitProperties cockpitProperties;
    private CockpitConfigurationPersistenceStrategy persistenceStrategy;
    private ConfigurationCache configurationCache;
    private ConfigurationImportSupport importSupport;
    private List<WidgetConfigurationContextDecorator> widgetConfigurationContextDecoratorList;
    private SchemaConfigValidator cockpitConfigValidator;


    /**
     * Converts flat single attributes needle into multiple search needle compatible with {@link ContextSearchRequest}
     *
     * @param attributesNeedle
     *           single search needle
     * @return a needle compatible with {@link ContextSearchRequest}
     */
    public static Map<String, List<String>> convertAttributes(final Map<String, String> attributesNeedle)
    {
        return attributesNeedle.entrySet().stream().collect(
                        Collectors.toMap(Entry::getKey, e -> Collections.singletonList(e.getValue()), (e1, e2) -> e1, LinkedHashMap::new));
    }


    private static boolean isAvailable(final InputStream configFileInputStream) throws IOException
    {
        return configFileInputStream.available() > 0;
    }


    private static Map<String, String> getNotBlankAttributes(final ConfigContext context)
    {
        return context.getAttributeNames().stream()
                        .filter(key -> StringUtils.isNotBlank(key) && StringUtils.isNotBlank(context.getAttribute(key)))
                        .collect(Collectors.toMap(key -> key, context::getAttribute, (e1, e2) -> e1, LinkedHashMap::new));
    }


    private static List<Context> readAllContexts(final Config config)
    {
        final List<Context> all = new ArrayList<>();
        final List<Context> root = new ArrayList<>(config.getContext());
        for(final Context context : root)
        {
            all.addAll(readAllContextsRecursive(context, new ArrayList<>()));
        }
        return Collections.unmodifiableList(all);
    }


    private static List<Context> readAllContextsRecursive(final Context context, final List<Context> list)
    {
        list.add(context);
        for(final Context child : context.getContext())
        {
            readAllContextsRecursive(child, list);
        }
        return list;
    }


    private static Map<String, String> getContextAttributes(final Context context)
    {
        final Map<String, String> result = new HashMap<>();
        if(context.getComponent() != null)
        {
            result.put(DefaultConfigContext.CONTEXT_COMPONENT, context.getComponent());
        }
        if(context.getType() != null)
        {
            result.put(DefaultConfigContext.CONTEXT_TYPE, context.getType());
        }
        if(context.getPrincipal() != null)
        {
            result.put(DefaultConfigContext.CONTEXT_PRINCIPAL, context.getPrincipal());
        }
        if(context.getOtherAttributes() != null)
        {
            result.putAll(context.getOtherAttributes().entrySet().stream().filter(entry -> StringUtils.isNotBlank(entry.getValue()))
                            .collect(Collectors.toMap(entry -> entry.getKey().getLocalPart(), Entry::getValue)));
        }
        return result;
    }


    private static List<Context> getContextWithRelatives(final Context context)
    {
        final List<Context> contextPath = getContextPath(context);
        final List<Context> contextChildren = getContextChildren(context);
        return ListUtils.union(contextPath, contextChildren);
    }


    /**
     * Gets stream of attributes values of parents, that are explicitly pointed in context by <code>merge-by</code> and
     * <code>parent</code>
     *
     * @param contextStream
     *           stream of contexts available
     * @param attributes
     *           names of attributes that should be taken into account - any attributes out of this collection should be
     *           returned
     * @return stream of values of parents in form &lt;attribute name, parent attribute value&gt;
     */
    private static Stream<Pair<String, String>> getExplicitMergeAttributes(final Stream<Context> contextStream,
                    final Collection<String> attributes)
    {
        return contextStream
                        .filter(ctx -> StringUtils.isNotEmpty(ctx.getMergeBy()) && attributes.contains(ctx.getMergeBy())
                                        && StringUtils.isNotEmpty(ctx.getParent()) && !"auto".equals(ctx.getParent()))
                        .map(ctx -> new ImmutablePair<>(ctx.getMergeBy(), ctx.getParent()));
    }


    /**
     * Groups provided merge parent attributes into map
     *
     * @param mergeAttributes
     *           stream of pairs &lt;attribute name, parent attribute values&gt;
     * @return parent attributes merged into form of map &lt;attribute name, list of attribute values found&gt;
     */
    private static Map<String, List<List<String>>> groupMergeAttributes(final Stream<Pair<String, List<String>>> mergeAttributes)
    {
        final Collector<Pair<String, List<String>>, ?, List<List<String>>> valueMapping = Collectors.mapping(Pair::getValue,
                        Collectors.toList());
        return mergeAttributes.collect(Collectors.groupingBy(Pair::getKey, valueMapping));
    }


    private static Map<String, List<String>> mergeAttributes(final Map<String, List<String>> attributesNeedle,
                    final Map<String, List<String>> mergeAttributesNeedle)
    {
        final Map<String, List<String>> parentContext = new LinkedHashMap<>(attributesNeedle.size());
        for(final Entry<String, List<String>> entry : attributesNeedle.entrySet())
        {
            if(mergeAttributesNeedle.containsKey(entry.getKey()))
            {
                parentContext.put(entry.getKey(), mergeAttributesNeedle.get(entry.getKey()));
            }
            else
            {
                parentContext.put(entry.getKey(), entry.getValue());
            }
        }
        return parentContext;
    }


    private static List<Context> getContextPath(final Context context)
    {
        if(context.getParentContext() == null)
        {
            return Collections.singletonList(context);
        }
        else
        {
            return getContextPathRecursively(context, new LinkedList<>());
        }
    }


    private static List<Context> getContextPathRecursively(final Context context, final List<Context> list)
    {
        list.add(0, context);
        if(context.getParentContext() != null)
        {
            getContextPathRecursively(context.getParentContext(), list);
        }
        return list;
    }


    private static List<Context> getContextChildren(final Context context)
    {
        return getContextChildrenRecursively(context, new LinkedList<>());
    }


    private static List<Context> getContextChildrenRecursively(final Context context, final List<Context> list)
    {
        for(final Context child : context.getContext())
        {
            list.add(child);
            getContextChildrenRecursively(child, list);
        }
        return list;
    }


    /**
     * specify obligatory merge-by attribute names. If configuration should be merged (has merge-by attribute) it will be
     * also merged by attributes specified by this property.
     */
    public void setObligatoryMergeAttributes(final List<String> obligatoryMergeAttributes)
    {
        try
        {
            executeWriteOperation(() -> setObligatoryMergeAttributesImmediately(obligatoryMergeAttributes));
        }
        catch(final CockpitConfigurationException e)
        {
            LOG.error(e.getLocalizedMessage(), e);
        }
    }


    protected void setObligatoryMergeAttributesImmediately(final List<String> obligatoryMergeAttributes)
    {
        assureSecure(true);
        this.obligatoryMergeAttributes = obligatoryMergeAttributes;
        invalidateConfigurationCache();
    }


    protected Config getRootConfig() throws CockpitConfigurationException
    {
        return executeReadOperation(this::getRootConfigImmediately);
    }


    protected Config getRootConfigImmediately() throws CockpitConfigurationException
    {
        assureSecure(false);
        if(!isRootConfigurationCacheValid())
        {
            invalidateRootConfigurationCache();
        }
        return getCacheValue(getConfigurationCache()::getRootConfiguration, this::loadRootConfigImmediately);
    }


    protected void loadRootConfigImmediately()
    {
        assureSecure(true);
        long configTime = 0L;
        Config rootConfig = null;
        InputStream configFileInputStream = null;
        try
        {
            configTime = getLastModification();
            configFileInputStream = getConfigFileInputStream();
            if(!isAvailable(configFileInputStream))
            {
                LOG.debug("Provided input stream seems to be empty. Resetting the configuration.");
                configFileInputStream.close();
                resetToDefaults();
                configFileInputStream = getConfigFileInputStream();
            }
            rootConfig = loadRootConfiguration(configFileInputStream);
        }
        catch(final IOException e)
        {
            LOG.error("Error while loading root configuration.", e);
            configTime = getCurrentTimeInMillis();
            rootConfig = new Config();
        }
        finally
        {
            IOUtils.closeQuietly(configFileInputStream);
            if(rootConfig != null && rootConfig.getContext() != null)
            {
                cacheRootConfiguration(rootConfig, configTime);
            }
        }
    }


    protected <CONFIG> ConfigContext buildConfigurationContext(final ConfigContext additionalContext,
                    final Class<CONFIG> configurationType, final WidgetInstance currentWidgetInstance)
    {
        ConfigContext decoratedContext = additionalContext;
        if(CollectionUtils.isNotEmpty(getWidgetConfigurationContextDecoratorList()))
        {
            for(final WidgetConfigurationContextDecorator decorator : getWidgetConfigurationContextDecoratorList())
            {
                decoratedContext = decorator.decorateContext(decoratedContext, configurationType, currentWidgetInstance);
            }
        }
        return decoratedContext;
    }


    @Override
    public <C> C loadConfiguration(final ConfigContext context, final Class<C> configType,
                    final WidgetInstance currentWidgetInstance) throws CockpitConfigurationException
    {
        return executeReadOperation(() -> loadConfigurationImmediately(
                        buildConfigurationContext(context, configType, currentWidgetInstance), configType));
    }


    @Override
    public <C> C loadConfiguration(final ConfigContext context, final Class<C> configType) throws CockpitConfigurationException
    {
        return executeReadOperation(
                        () -> loadConfigurationImmediately(buildConfigurationContext(context, configType, null), configType));
    }


    protected <C> C loadConfigurationImmediately(final ConfigContext context, final Class<C> configType)
                    throws CockpitConfigurationException
    {
        assureSecure(false);
        if(!isRootConfigurationCacheValid())
        {
            invalidateRootConfigurationCache();
        }
        else
        {
            final C configFromCache = getConfigFromCache(context, configType);
            if(configFromCache != null)
            {
                return configFromCache;
            }
        }
        final Config root = getRootConfig();
        final C config = loadConfiguration(context, configType, root);
        if(config == null)
        {
            throw new CockpitConfigurationNotFoundException("Cockpit configuration for '" + context + "' not found");
        }
        putConfigToCache(context, configType, config);
        return config;
    }


    /**
     * Checks whether current root configuration cache is valid.
     *
     * @return <code>true</code> if of any reason, cache needs to be reloaded
     */
    protected boolean isRootConfigurationCacheValid()
    {
        final long timestamp = getLastModification();
        return (timestamp == 0 || getConfigurationCache().isValid(timestamp));
    }


    /**
     * Configuration cache must be invalidated if this returns true.
     *
     * @return returns true if the configuration cache must be invalidated
     * @deprecated since 1811, unused
     */
    @Deprecated(since = "1811", forRemoval = true)
    protected boolean cacheMustBeInvalidated()
    {
        return !isRootConfigurationCacheValid();
    }


    /**
     * Get configuration from cache
     *
     * @param context
     * @param configType
     */
    protected <C> C getConfigFromCache(final ConfigContext context, final Class<C> configType)
    {
        try
        {
            return executeReadOperation(() -> getConfigFromCacheImmediately(context, configType));
        }
        catch(final CockpitConfigurationException e)
        {
            LOG.error(e.getLocalizedMessage(), e);
            return null;
        }
    }


    protected <C> C getConfigFromCacheImmediately(final ConfigContext context, final Class<C> configType)
    {
        assureSecure(false);
        ConfigContext configContext = new ImmutableConfigContext(context);
        for(final CockpitConfigurationContextStrategy strategy : getContextStrategies().values())
        {
            configContext = strategy.getConfigurationCacheKey(configContext);
        }
        return getConfigurationCache().getConfiguration(configContext, configType);
    }


    /**
     * Put configuration to cache.
     *
     * @param context
     * @param configType
     * @param config
     */
    protected <C> void putConfigToCache(final ConfigContext context, final Class<C> configType, final C config)
    {
        if(config != null)
        {
            try
            {
                executeWriteOperation(() -> putConfigToCacheImmediately(context, configType, config));
            }
            catch(final CockpitConfigurationException e)
            {
                LOG.error(e.getLocalizedMessage(), e);
            }
        }
    }


    protected <C> void putConfigToCacheImmediately(final ConfigContext context, final Class<C> configType, final C config)
    {
        assureSecure(true);
        ConfigContext configContext = new ImmutableConfigContext(context);
        for(final CockpitConfigurationContextStrategy strategy : getContextStrategies().values())
        {
            configContext = strategy.getConfigurationCacheKey(configContext);
        }
        getConfigurationCache().cacheConfiguration(configContext, configType, config);
    }


    /**
     * Loads configuration of specified type from provided stream
     *
     * @param context
     *           configuration context matched by configuration to be loaded
     * @param configType
     *           JAXB class representing configuration to be loaded
     * @param inputStream
     *           stream that contains contains configuration xml to be interpreted
     * @param <C>
     *           type of configuration
     * @return xml from stream parsed to specified configuration class
     * @throws CockpitConfigurationException
     *            when read operation fails
     */
    public <C> C loadConfiguration(final ConfigContext context, final Class<C> configType, final InputStream inputStream)
                    throws CockpitConfigurationException
    {
        return executeReadOperation(() -> loadConfigurationImmediately(context, configType, inputStream));
    }


    protected <C> C loadConfigurationImmediately(final ConfigContext context, final Class<C> configType,
                    final InputStream inputStream) throws CockpitConfigurationException
    {
        assureSecure(false);
        final Config root = loadRootConfiguration(inputStream);
        return loadConfiguration(context, configType, root);
    }


    private <C> C loadConfiguration(final ConfigContext context, final Class<C> configType, final Config root)
                    throws CockpitConfigurationException
    {
        final Map<String, String> notBlankAttributes = getNotBlankAttributes(context);
        // find matching context elements
        final List<Context> contextElements = findContext(root, notBlankAttributes, true, false);
        // load containing element and do merge if applicable
        C loadedConfig = mergeContexts(context, contextElements, configType);
        // use fallback strategy if no configuration found and fallback strategy available
        if(loadedConfig == null)
        {
            final List<CockpitConfigurationFallbackStrategy> strategyList = getFallbackStrategies().get(configType.getName());
            if(strategyList != null)
            {
                for(final CockpitConfigurationFallbackStrategy strategy : strategyList)
                {
                    loadedConfig = (C)strategy.loadFallbackConfiguration(context, configType);
                    if(loadedConfig != null)
                    {
                        loadedConfig = adaptConfigBeforeMerge(loadedConfig, configType, context);
                        break;
                    }
                }
            }
        }
        return adaptConfigAfterLoad(loadedConfig, configType, context);
    }


    /**
     * Merges provided configurations into single JAXB configuration bean.
     *
     * @param context
     *           search context that resulted in provided configuration elements
     * @param contextElements
     *           configuration elements matching provided search context
     * @param configType
     *           expected JAXB configuration bean type
     * @param <C>
     *           expected result type
     * @return parsed and merged configuration
     * @throws CockpitConfigurationException
     *            thrown when problem with parsing or merging occurred
     */
    public <C> C mergeContexts(final ConfigContext context, final List<Context> contextElements, final Class<C> configType)
                    throws CockpitConfigurationException
    {
        C finalConfig = null;
        // load containing element and do merge if applicable
        if(!contextElements.isEmpty())
        {
            for(int i = contextElements.size() - 1; i >= 0; i--)
            {
                C currentConfig = getConfigElement(configType, contextElements.get(i).getAny());
                currentConfig = adaptConfigBeforeMerge(currentConfig, configType, context);
                if(finalConfig == null)
                {
                    finalConfig = currentConfig;
                }
                if(currentConfig != null)
                {
                    getMergeUtils().merge(finalConfig, currentConfig);
                }
            }
        }
        return finalConfig;
    }


    protected <C> C adaptConfigBeforeMerge(final C loadedConfig, final Class<C> configType, final ConfigContext context)
                    throws CockpitConfigurationException
    {
        C adaptedConfig = loadedConfig;
        if(adaptedConfig != null)
        {
            final CockpitConfigurationAdapter<C> adapter = getAdapter(context, configType);
            if(adapter != null)
            {
                adaptedConfig = adapter.adaptBeforeMerge(context, adaptedConfig);
            }
            final Optional<CockpitConfigurationAdapter<C>> typeAdapter = getConfigTypeAdapter(configType);
            if(typeAdapter.isPresent())
            {
                adaptedConfig = typeAdapter.get().adaptBeforeMerge(context, adaptedConfig);
            }
        }
        return adaptedConfig;
    }


    /**
     * Calls {@link CockpitConfigurationAdapter#adaptAfterLoad(ConfigContext, Object)} on all adapters defined for provided
     * final configuration.
     *
     * @param loadedConfig
     *           merged configuration to be adopted
     * @param configType
     *           type of JAXB configuration bean
     * @param context
     *           config search context
     * @param <C>
     *           expected result type
     * @return adapted configuration
     * @throws CockpitConfigurationException
     *            thrown when problem with parsing or merging occurred
     */
    public <C> C adaptConfigAfterLoad(final C loadedConfig, final Class<C> configType, final ConfigContext context)
                    throws CockpitConfigurationException
    {
        C adaptedConfig = loadedConfig;
        if(adaptedConfig != null)
        {
            final CockpitConfigurationAdapter<C> adapter = getAdapter(context, configType);
            if(adapter != null)
            {
                adaptedConfig = adapter.adaptAfterLoad(context, adaptedConfig);
            }
            final Optional<CockpitConfigurationAdapter<C>> typeAdapter = getConfigTypeAdapter(configType);
            if(typeAdapter.isPresent())
            {
                adaptedConfig = typeAdapter.get().adaptAfterLoad(context, adaptedConfig);
            }
        }
        return adaptedConfig;
    }


    private <C> C getConfigElement(final Class<C> configType, final Object configElementOrNode)
                    throws CockpitConfigurationException
    {
        if(configElementOrNode instanceof Node)
        {
            return loadConfigElement(configType, (Node)configElementOrNode);
        }
        else
        {
            return (C)configElementOrNode;
        }
    }


    private <C> CockpitConfigurationAdapter<C> getAdapter(final ConfigContext context, final Class<C> configType)
    {
        if(adapters == null)
        {
            return null;
        }
        for(final Entry<Map<String, String>, CockpitConfigurationAdapter<?>> entry : adapters.entrySet())
        {
            if(contextMatches(context, entry.getKey()))
            {
                final CockpitConfigurationAdapter<?> adapter = entry.getValue();
                if(adapter.getSupportedType().isAssignableFrom(configType))
                {
                    return (CockpitConfigurationAdapter<C>)adapter;
                }
            }
        }
        return null;
    }


    private <C> Optional<CockpitConfigurationAdapter<C>> getConfigTypeAdapter(final Class<C> configTypeClass)
    {
        if(configTypesAdapters != null && configTypeClass != null)
        {
            final CockpitConfigurationAdapter<?> adapter = configTypesAdapters.get(configTypeClass.getName());
            return adapter != null && configTypeClass.isAssignableFrom(adapter.getSupportedType())
                            ? Optional.of((CockpitConfigurationAdapter<C>)adapter)
                            : Optional.empty();
        }
        return Optional.empty();
    }


    private boolean contextMatches(final ConfigContext context, final Map<String, String> contextMap)
    {
        return contextMap.entrySet().stream()
                        .allMatch(entry -> contextAttributesMatches(entry.getKey(), context.getAttribute(entry.getKey()), entry.getValue()));
    }


    private boolean contextAttributesMatches(final String attributeName, final String pattern, final String value)
    {
        final CockpitConfigurationContextStrategy strategy = getContextStrategies().get(attributeName);
        if(strategy != null)
        {
            return strategy.valueMatches(pattern, value);
        }
        else
        {
            return StringUtils.equals(pattern, value);
        }
    }


    /**
     * Loads root configuration
     *
     * @return root configuration
     * @throws CockpitConfigurationException
     *            when cockpit configuration cannot be loaded
     */
    public Config loadRootConfiguration() throws CockpitConfigurationException
    {
        final Config cachedConfig = configToPersist.get();
        if(BooleanUtils.isTrue(isDelayedPersistEnabled.get()) && cachedConfig != null)
        {
            LOG.debug("Returning cached intermediate configuration reset result: {} contexts", cachedConfig.getContext().size());
            return cachedConfig;
        }
        return getRootConfig();
    }


    /**
     * Loads root configuration
     *
     * @param inputStream
     *           of configuration
     * @return loaded configuration
     */
    public Config loadRootConfiguration(final InputStream inputStream)
    {
        try
        {
            final Config config = loadRootConfig(new BufferedInputStream(inputStream));
            final ConfigurationInterpreter<Config> interpreter = new ConfigurationInterpreter<>()
            {
                @Override
                public Config load(final Import importConfiguration, final InputStream configurationStream) throws IOException
                {
                    try
                    {
                        return loadRootConfig(new BufferedInputStream(configurationStream));
                    }
                    catch(final CockpitConfigurationException e)
                    {
                        throw new IOException(e);
                    }
                }


                @Override
                public List<Import> getImports(final Config configuration)
                {
                    return configuration.getImports();
                }


                @Override
                public List<Requirement> getRequirements(final Config configuration)
                {
                    return configuration.getRequires();
                }


                @Override
                public Set<String> getRequiredParameters(final Config configuration)
                {
                    final String[] split = StringUtils.split(configuration.getRequiredParameters(), ",");
                    return Optional.ofNullable(split).<Set<String>>map(Sets::newHashSet).orElseGet(Collections::emptySet);
                }


                @Override
                public Config merge(final Import importConfiguration, final Config target, final Config source)
                {
                    target.getContext().addAll(source.getContext());
                    return target;
                }
            };
            return getImportSupport().resolveImports(config, interpreter);
        }
        catch(final CockpitConfigurationException | IOException e)
        {
            LOG.error("Could not load cockpit configuration root element; creating new one", e);
            return new Config();
        }
    }


    @Override
    public <C> void storeConfiguration(final ConfigContext context, final C configuration) throws CockpitConfigurationException
    {
        executeWriteOperation(() -> storeConfigurationImmediately(context, configuration));
    }


    protected <C> void storeConfigurationImmediately(final ConfigContext context, final C configuration)
                    throws CockpitConfigurationException
    {
        assureSecure(true);
        try(final BufferedInputStream inputStream = new BufferedInputStream(getConfigFileInputStream()))
        {
            final Config root = loadRootConfig(inputStream);
            final Map<String, String> notBlankAttributes = getNotBlankAttributes(context);
            final List<Context> contextElements = findContext(root, notBlankAttributes, false, true);
            final Context contextElement;
            if(contextElements.isEmpty())
            {
                contextElement = new Context();
                setContext(contextElement, notBlankAttributes);
                if(root != null)
                {
                    root.getContext().add(contextElement);
                }
            }
            else
            {
                contextElement = contextElements.iterator().next();
            }
            final C config = adaptConfigBeforeStore(configuration, context);
            if(config != null)
            {
                contextElement.setAny(storeConfigElement(config));
            }
            storeRootConfig(root);
        }
        catch(final IOException ioe)
        {
            throw new CockpitConfigurationException(ioe.getMessage(), ioe);
        }
    }


    private <C> C adaptConfigBeforeStore(final C configuration, final ConfigContext context) throws CockpitConfigurationException
    {
        C adaptedConfig = configuration;
        if(adaptedConfig != null)
        {
            final Class<C> configTypeClass = (Class<C>)configuration.getClass();
            final CockpitConfigurationAdapter<C> adapter = getAdapter(context, configTypeClass);
            if(adapter != null)
            {
                adaptedConfig = adapter.adaptBeforeStore(context, adaptedConfig);
            }
            final Optional<CockpitConfigurationAdapter<C>> typeAdapter = getConfigTypeAdapter(configTypeClass);
            if(typeAdapter.isPresent())
            {
                adaptedConfig = typeAdapter.get().adaptBeforeStore(context, adaptedConfig);
            }
        }
        return adaptedConfig;
    }


    /**
     * Searches for contexts that matches provided attributes list. A context is considered as matching, when contains all
     * provided attributes and their values meets {@link CockpitConfigurationContextStrategy} requirements.
     *
     * @param root
     *           root configuration
     * @param request
     *           search request
     * @return full search progress after finishing search
     * @see CockpitConfigurationContextStrategy#valueMatches(String, String)
     */
    public ContextSearchProgress findContext(final Config root, final ContextSearchRequest request)
    {
        final ContextSearchProgress progress = createSearchProgress(request);
        final ContextSearchNodeRelevance relevance = createHighestRelevance(request);
        final ContextSearchTerms terms = createSearchTerms(relevance, request);
        try
        {
            executeReadOperation(() -> findContexts(request, progress, terms, relevance));
            progress.commit();
            logContextSearchResults(request, progress);
        }
        catch(final CockpitConfigurationException e)
        {
            LOG.error(e.getLocalizedMessage(), e);
        }
        return progress;
    }


    protected void logContextSearchResults(final ContextSearchRequest request, final ContextSearchProgress progress)
    {
        if(LOG.isDebugEnabled())
        {
            try(final OutputStream out = new StringBufferOutputStream(LOG::debug))
            {
                LOG.debug("Context search result for {}", request.getAttributesNeedle());
                final Config cfg = new Config();
                cfg.getContext().addAll(progress.getResult());
                createMarshaller(Config.class).marshal(cfg, out);
            }
            catch(final Exception ex)
            {
                LOG.debug(ex.getLocalizedMessage(), ex);
            }
            logContextSearchProgress(request, progress);
        }
    }


    protected void logContextSearchProgress(final ContextSearchRequest request, final ContextSearchProgress progress)
    {
        if(LOG.isTraceEnabled())
        {
            LOG.debug("Search tree for {}", request.getAttributesNeedle());
            try
            {
                progress.toXML(new StringBufferOutputStream(LOG::trace));
            }
            catch(final IOException ex)
            {
                LOG.trace(ex.getLocalizedMessage(), ex);
            }
            LOG.debug("All matching for {}", request.getAttributesNeedle());
            try(final OutputStream out = new StringBufferOutputStream(LOG::debug))
            {
                final Config cfg = new Config();
                cfg.getContext().addAll(getAllResults(progress));
                createMarshaller(Config.class).marshal(cfg, out);
            }
            catch(final JAXBException | IOException ex)
            {
                LOG.trace(ex.getLocalizedMessage(), ex);
            }
        }
    }


    /**
     * Searches for contexts that matches provided attributes list. A context is considered as matching, when contains all
     * provided attributes and their values meets {@link CockpitConfigurationContextStrategy} requirements.
     *
     * @param configStack
     *           configuration root
     * @param attributesNeedle
     *           attributes to meet
     * @param ignoreEmpty
     *           <code>true</code> if empty attributes should be ignored
     * @param exactMatch
     *           <code>true</code> if merge-by should be ignored and only actual attributes of context should be taken under
     *           consideration
     * @return list of contexts that meets requirements
     * @see CockpitConfigurationContextStrategy#valueMatches(String, String)
     */
    public List<Context> findContext(final Config configStack, final Map<String, String> attributesNeedle,
                    final boolean ignoreEmpty, final boolean exactMatch)
    {
        try
        {
            return executeReadOperation(() -> findContextImmediately(configStack, attributesNeedle, ignoreEmpty, exactMatch));
        }
        catch(final CockpitConfigurationException e)
        {
            LOG.error(e.getLocalizedMessage(), e);
            return Collections.emptyList();
        }
    }


    protected List<Context> findContextImmediately(final Config configStack, final Map<String, String> attributesNeedle,
                    final boolean ignoreEmpty, final boolean exactMatch)
    {
        assureSecure(false);
        if(exactMatch)
        {
            return findContextExactMatchInternal(configStack, attributesNeedle);
        }
        assureConfigCached(configStack);
        final Map<String, List<String>> convertedNeedle = convertAttributes(attributesNeedle);
        final ContextSearchRequest request = createSearchRequest(configStack, convertedNeedle);
        request.setNotEmpty(ignoreEmpty);
        return findContext(configStack, request).getResult();
    }


    protected ContextSearchTerms createSearchTerms(final ContextSearchNodeRelevance relevance, final ContextSearchRequest request)
    {
        final Map<String, List<String>> needles = request.getAttributesNeedle();
        final Map<String, List<String>> resetState = needles.entrySet().stream()
                        .filter(needle -> getContextStrategies().containsKey(needle.getKey()))
                        .filter(needle -> getContextStrategies().get(needle.getKey()).isResettable())
                        .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
        final Map<String, Integer> resetRelevance = resetState.keySet().stream()
                        .collect(Collectors.toMap(attr -> attr, relevance::getLevel));
        return createContextSearchTerms(resetState, resetRelevance, request);
    }


    protected ContextSearchTerms createUpdatedSearchTerms(final ContextSearchTerms terms,
                    final ContextSearchNodeRelevance relevance, final ContextSearchRequest updatedRequest)
    {
        final Map<String, List<String>> resetState = new LinkedHashMap<>(terms.getResetState());
        final Map<String, Integer> resetRelevance = new LinkedHashMap<>(terms.getResetRelevance());
        updatedRequest.getAttributesNeedle().entrySet().stream().filter(merge -> getContextStrategies().containsKey(merge.getKey()))
                        .filter(merge -> getContextStrategies().get(merge.getKey()).isResettable()).forEach(merge -> {
                            resetState.put(merge.getKey(), merge.getValue());
                            resetRelevance.put(merge.getKey(), relevance.getLevel(merge.getKey()));
                        });
        return createContextSearchTerms(resetState, resetRelevance, updatedRequest);
    }


    protected ContextSearchTerms createContextSearchTerms(final Map<String, List<String>> resetState,
                    final Map<String, Integer> resetRelevance, final ContextSearchRequest request)
    {
        final ContextSearchTerms searchTerms = new ContextSearchTerms(resetState, resetRelevance);
        searchTerms.setExclusions(getSearchExclusions(request));
        return searchTerms;
    }


    private ConfigurationSearchFilter getSearchExclusions(final ContextSearchRequest request)
    {
        final Set<String> constants = request.getAttributesNeedle().keySet().stream()
                        .filter(attribute -> !getContextStrategies().containsKey(attribute)).collect(Collectors.toSet());
        return getConfigurationCache().prepareSearchFilter(request, constants, new AttributeStrategyComparator());
    }


    private void resetSearch(final ContextSearchTerms searchTerms, final Map<String, List<String>> searchNeedle,
                    final ContextSearchNodeRelevance searchRelevance, final Set<String> restricted)
    {
        searchNeedle.entrySet().stream().filter(needle -> getContextStrategies().containsKey(needle.getKey()))
                        .filter(needle -> getContextStrategies().get(needle.getKey()).isResettable())
                        .filter(needle -> !restricted.contains(needle.getKey()))
                        .filter(needle -> searchTerms.getResetState().containsKey(needle.getKey())).forEach(merge -> {
                            merge.setValue(searchTerms.getResetState().get(merge.getKey()));
                            searchRelevance.setLevel(merge.getKey(), searchTerms.getResetRelevance().get(merge.getKey()));
                        });
    }


    private List<Context> getAllResults(final ContextSearchNode node)
    {
        final Stream<Context> children = node.getChildren().stream().filter(child -> child.getNotEmptyRelevance() != null)
                        .filter(child -> !(child instanceof ContextSearchNodeReference)).map(this::getAllResults).flatMap(Collection::stream);
        return Stream.concat(node.getNodeResult().stream(), children).filter(new IdentityUtils.DistinctFilter<>())
                        .collect(Collectors.toList());
    }


    private List<Context> findContextExactMatchInternal(final Config root, final Map<String, String> context)
    {
        ContextQuery query = getConfigurationCache().createContextQuery(root, new AttributeStrategyComparator());
        for(final Entry<String, String> entry : context.entrySet())
        {
            query = query.matchesAny(entry.getKey(), Collections.singleton(entry.getValue()));
        }
        final ContextQuery completeQuery = query;
        try
        {
            return executeReadOperation(() -> {
                assureConfigCached(root);
                return new ArrayList<>(completeQuery.execute());
            });
        }
        catch(final CockpitConfigurationException e)
        {
            LOG.error(e.getLocalizedMessage(), e);
            return Collections.emptyList();
        }
    }


    protected ContextSearchRequest createSearchRequest(final Config configStack, final Map<String, List<String>> attributesNeedle)
    {
        return new ContextSearchRequest(configStack, attributesNeedle, ContextSearchRestriction.EMPTY);
    }


    protected ContextSearchProgress createSearchProgress(final ContextSearchRequest request)
    {
        return new ContextSearchProgress(getRelevanceZones(request));
    }


    protected ContextSearchNodeRelevance createHighestRelevance(final ContextSearchRequest request)
    {
        return ContextSearchNodeRelevance.mostRelevant(getRelevanceZones(request));
    }


    protected List<String> getRelevanceZones(final ContextSearchRequest request)
    {
        return request.getAttributesNeedle().keySet().stream().filter(zone -> getContextStrategies().containsKey(zone))
                        .collect(Collectors.toList());
    }


    protected ContextSearchNodeRelevance createMergeRelevance(final ContextSearchNodeRelevance currentRelevance,
                    final Set<String> mergingAttributes)
    {
        final ContextSearchNodeRelevance mergeRelevance = new ContextSearchNodeRelevance(currentRelevance);
        mergingAttributes.forEach(mergeRelevance::decreaseLevel);
        return mergeRelevance;
    }


    protected ContextSearchRestriction createParentRestriction(final ContextSearchRequest request, final Set<String> attributes)
    {
        final List<String> relevanceZones = getRelevanceZones(request);
        final Optional<Integer> max = attributes.stream().map(relevanceZones::indexOf).max(Comparator.naturalOrder());
        return max
                        .<ContextSearchRestriction>map(integer -> new DefaultContextSearchRestriction(relevanceZones.subList(0, integer)))
                        .orElse(ContextSearchRestriction.EMPTY);
    }


    private void findContexts(final ContextSearchRequest request, final ContextSearchProgress progress,
                    final ContextSearchTerms searchTerms, final ContextSearchNodeRelevance relevance)
    {
        assureSecure(false);
        final Iterator<ContextSearchNode> nodes = progress.expand(request, searchTerms, relevance);
        final AttributeStrategyComparator attributeComparator = new AttributeStrategyComparator();
        while(nodes.hasNext())
        {
            final ContextSearchNode node = nodes.next();
            progress.selectNode(node);
            ContextQuery contextQuery = getConfigurationCache().createContextQuery(request.getContextStack(), attributeComparator);
            if(request.isNotEmpty())
            {
                contextQuery = contextQuery.notEmpty();
            }
            for(final Entry<String, String> entry : node.getSearchNeedle().getAttributes().entrySet())
            {
                contextQuery = contextQuery.matchesAny(entry.getKey(), Collections.singleton(entry.getValue()));
            }
            final Collection<Context> matching = contextQuery.execute();
            if(matching == null)
            {
                throw new IllegalStateException(
                                "Context search was triggered on invalidated cache! Please make sure that timezones on server and database are aligned!");
            }
            else if(matching.isEmpty())
            {
                if(searchTerms.getHighestMatchingRelevance() == null
                                || searchTerms.getHighestMatchingRelevance().compareTo(relevance) < 0)
                {
                    final Map<String, List<String>> attributesNeedle = convertAttributes(node.getSearchNeedle().getAttributes());
                    findContextByStrategies(request.createAlternative(attributesNeedle), progress, searchTerms, relevance);
                }
                else
                {
                    LOG.debug("A child search with matches and higher relevance found: {}. Interrupting search at {}",
                                    searchTerms.getHighestMatchingRelevance(), node);
                }
            }
            else
            {
                if(searchTerms.getHighestMatchingRelevance() == null
                                || searchTerms.getHighestMatchingRelevance().compareTo(relevance) < 0)
                {
                    searchTerms.setHighestMatchingRelevance(relevance);
                }
                matching.forEach(ctx -> {
                    node.addNodeResult(ctx);
                    node.setEgalitarian(true);
                    final Map<String, List<String>> attributesNeedle = convertAttributes(node.getSearchNeedle().getAttributes());
                    final Map<String, List<String>> mergingAttributes = getMergeAttributes(searchTerms, ctx, attributesNeedle);
                    if(!mergingAttributes.isEmpty())
                    {
                        final Map<String, List<String>> mergedAttributes = mergeAttributes(attributesNeedle, mergingAttributes);
                        final ContextSearchNodeRelevance mergeRelevance = createMergeRelevance(relevance, mergingAttributes.keySet());
                        resetSearch(searchTerms, mergedAttributes, mergeRelevance, mergingAttributes.keySet());
                        final ContextSearchRequest mergedRequest = request.createAlternative(mergedAttributes);
                        final ContextSearchTerms mergeTerms = createUpdatedSearchTerms(searchTerms, mergeRelevance, mergedRequest);
                        findContexts(mergedRequest, progress, mergeTerms, mergeRelevance);
                    }
                    final Map<String, List<String>> obligatoryMergingAttributes = getObligatoryMergeAttributes(searchTerms,
                                    attributesNeedle, mergingAttributes.keySet());
                    if(!obligatoryMergingAttributes.isEmpty())
                    {
                        final Map<String, List<String>> mergedAttributes = mergeAttributes(attributesNeedle,
                                        obligatoryMergingAttributes);
                        final ContextSearchNodeRelevance mergeRelevance = createMergeRelevance(relevance,
                                        obligatoryMergingAttributes.keySet());
                        final ContextSearchRestriction parentRestriction = createParentRestriction(request,
                                        obligatoryMergingAttributes.keySet());
                        final Set<String> restrictions = mergedAttributes.keySet().stream()
                                        .filter(parentRestriction::isAttributeRestricted).collect(Collectors.toSet());
                        restrictions.addAll(obligatoryMergingAttributes.keySet());
                        resetSearch(searchTerms, mergedAttributes, mergeRelevance, restrictions);
                        final ContextSearchRequest mergedRequest = request.createAlternative(mergedAttributes, parentRestriction);
                        final ContextSearchTerms mergeTerms = createUpdatedSearchTerms(searchTerms, mergeRelevance, mergedRequest);
                        findContexts(mergedRequest, progress, mergeTerms, mergeRelevance);
                    }
                });
            }
            progress.collapse();
        }
    }


    /**
     * Gets parent values of an attribute from strategy
     *
     * @param attribute
     *           attribute name
     * @param values
     *           values, which parents are requested
     * @return list of unique parent values or empty list if no strategy defined for attribute
     */
    private List<String> getParentValues(final ContextSearchTerms terms, final String attribute, final Collection<String> values)
    {
        final CockpitConfigurationContextStrategy strategy = getContextStrategies().get(attribute);
        if(strategy == null)
        {
            return Collections.emptyList();
        }
        final List<String> strategyParents = values.stream().map(strategy::getParentContexts).flatMap(Collection::stream).distinct()
                        .collect(Collectors.toList());
        if(terms.getExclusions() == null)
        {
            return strategyParents;
        }
        return strategyParents.stream().flatMap(child -> {
            if(terms.getExclusions().filter(attribute, child))
            {
                LOG.debug("{} for {} is excluded", child, attribute);
                return getParentValues(terms, attribute, Collections.singleton(child)).stream();
            }
            else
            {
                return Stream.of(child);
            }
        }).distinct().collect(Collectors.toList());
    }


    private void findContextByStrategies(final ContextSearchRequest request, final ContextSearchProgress progress,
                    final ContextSearchTerms searchTerms, final ContextSearchNodeRelevance relevance)
    {
        final List<ImmutablePair<String, List<String>>> parents = request.getAttributesNeedle().entrySet().stream()
                        .filter(needle -> getContextStrategies().containsKey(needle.getKey()))
                        .filter(needle -> !request.getParentRestriction().isAttributeRestricted(needle.getKey()))
                        .flatMap(needle -> needle.getValue().stream().map(value -> new ImmutablePair<>(needle.getKey(), value)))
                        .map(needle -> {
                            final List<String> parentValues = getParentValues(searchTerms, needle.getKey(),
                                            Collections.singletonList(needle.getValue()));
                            return new ImmutablePair<>(needle.getKey(), parentValues);
                        }).distinct().collect(Collectors.toList());
        Collections.reverse(parents);
        parents.forEach(needle -> {
            if(CollectionUtils.isNotEmpty(needle.getValue()))
            {
                final Map<String, List<String>> parentAttributes = mergeAttributes(request.getAttributesNeedle(),
                                Collections.singletonMap(needle.getKey(), needle.getValue()));
                if(MapUtils.isNotEmpty(parentAttributes))
                {
                    final ContextSearchNodeRelevance childRelevance = new ContextSearchNodeRelevance(relevance);
                    childRelevance.decreaseLevel(needle.getKey());
                    findContexts(request.createAlternative(parentAttributes), progress, searchTerms, childRelevance);
                }
            }
        });
    }


    private void assureConfigCached(final Config config)
    {
        assureSecure(false);
        getAllContexts(config);
    }


    private Collection<Context> getAllContexts(final Config config)
    {
        assureSecure(false);
        try
        {
            return getCacheValue(() -> getConfigurationCache().getContexts(config),
                            () -> cacheAllContexts(config, readAllContexts(config)));
        }
        catch(final CockpitConfigurationException e)
        {
            LOG.error(e.getLocalizedMessage(), e);
            return Collections.emptyList();
        }
    }


    private Collection<Context> cacheAllContexts(final Config config, final Collection<Context> contexts)
    {
        assureSecure(true);
        getConfigurationCache().cacheContexts(config, contexts, new ReadContextResolver());
        return getConfigurationCache().getContexts(config);
    }


    /**
     * Retrieves all attributes from context and stores them as map &lt;attribute name, attribute value&gt;
     *
     * @param context
     *           context to be represented
     * @return map of attributes
     */
    public Map<String, String> getContext(final Context context)
    {
        try
        {
            return executeReadOperation(() -> getContextImmediately(context));
        }
        catch(final CockpitConfigurationException e)
        {
            LOG.error(e.getLocalizedMessage(), e);
            return Collections.emptyMap();
        }
    }


    protected Map<String, String> getContextImmediately(final Context context)
    {
        assureSecure(false);
        Map<String, String> result = getConfigurationCache().getAttributes(context);
        if(result == null)
        {
            result = readAttributes(context);
        }
        return result;
    }


    private Map<String, String> readAttributes(final Context context)
    {
        return getContextPath(context).stream().map(DefaultCockpitConfigurationService::getContextAttributes).map(Map::entrySet)
                        .flatMap(Collection::stream).collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1));
    }


    private Map<String, List<String>> getMergeAttributes(final ContextSearchTerms searchTerms, final Context context,
                    final Map<String, List<String>> attributes)
    {
        final List<Context> contextPath = getContextWithRelatives(context);
        final Stream<Pair<String, List<String>>> explicit = getExplicitMergeAttributes(contextPath.stream(), attributes.keySet())
                        .map(attribute -> new ImmutablePair<>(attribute.getKey(), Collections.singletonList(attribute.getValue())));
        final Stream<Pair<String, List<String>>> auto = getAutoMergeAttributes(searchTerms, contextPath.stream(), attributes);
        final Map<String, List<List<String>>> collected = groupMergeAttributes(Stream.concat(explicit, auto));
        return flattenMergeAttributes(collected);
    }


    /**
     * Gets stream of attributes values of parents, that are not pointed in context by <code>merge-by</code> and
     * <code>parent</code> and are determined by strategies
     *
     * @param searchTerms
     * @param contextStream
     *           stream of contexts available
     * @param attributes
     *           attributes that should be taken into account and their values - any attributes out of this collection
     *           should be returned
     * @return stream of values of parents in form &lt;attribute name, parent attribute values&gt;
     */
    private Stream<Pair<String, List<String>>> getAutoMergeAttributes(final ContextSearchTerms searchTerms,
                    final Stream<Context> contextStream, final Map<String, List<String>> attributes)
    {
        return contextStream.filter(ctx -> StringUtils.isNotEmpty(ctx.getMergeBy()) && attributes.containsKey(ctx.getMergeBy())
                        && (StringUtils.isEmpty(ctx.getParent()) || "auto".equals(ctx.getParent()))
                        && getContextStrategies().containsKey(ctx.getMergeBy())).map(ctx -> {
            final String attribute = ctx.getMergeBy();
            final List<String> values = attributes.get(attribute);
            final List<String> parentValues = getParentValues(searchTerms, attribute, values);
            return new ImmutablePair<>(attribute, parentValues);
        });
    }


    /**
     * Turns provided map of lists of parent attribute values into flat map of unique values
     *
     * @param mergeAttributes
     *           map &lt;attribute name, list of attribute values&gt; of parents
     * @return parent attributes in form of map &lt;name, values&gt;
     */
    private Map<String, List<String>> flattenMergeAttributes(final Map<String, List<List<String>>> mergeAttributes)
    {
        return mergeAttributes.entrySet().stream()
                        .collect(Collectors.toMap(Entry::getKey,
                                        entry -> entry.getValue().stream().flatMap(Collection::stream).distinct().collect(Collectors.toList()),
                                        (e1, e2) -> e1, LinkedHashMap::new));
    }


    private Map<String, List<String>> getObligatoryMergeAttributes(final ContextSearchTerms searchTerms,
                    final Map<String, List<String>> attributesNeedle, final Set<String> mergeAttributes)
    {
        return obligatoryMergeAttributes.stream()
                        .filter(attr -> attributesNeedle.containsKey(attr) && getContextStrategies().containsKey(attr)
                                        && !mergeAttributes.contains(attr))
                        .collect(Collectors.toMap(attr -> attr,
                                        attr -> attributesNeedle.get(attr).stream()
                                                        .map(value -> getParentValues(searchTerms, attr, Collections.singleton(value)))
                                                        .flatMap(Collection::stream).distinct().collect(Collectors.toList())));
    }


    /**
     * Sets context to the context element
     *
     * @param contextElement
     *           a context element
     * @param context
     *           a map of properties
     */
    public void setContext(final Context contextElement, final Map<String, String> context)
    {
        setContextImmediately(contextElement, context);
        if(context != null)
        {
            invalidateAttributesCache(contextElement);
        }
    }


    protected void setContextImmediately(final Context contextElement, final Map<String, String> context)
    {
        if(context != null)
        {
            for(final Entry<String, String> entry : context.entrySet())
            {
                if(DefaultConfigContext.CONTEXT_COMPONENT.equals(entry.getKey()))
                {
                    contextElement.setComponent(entry.getValue());
                }
                else if(DefaultConfigContext.CONTEXT_TYPE.equals(entry.getKey()))
                {
                    contextElement.setType(entry.getValue());
                }
                else if(DefaultConfigContext.CONTEXT_PRINCIPAL.equals(entry.getKey()))
                {
                    contextElement.setPrincipal(entry.getValue());
                }
                else
                {
                    contextElement.getOtherAttributes().put(new QName(entry.getKey()), entry.getValue());
                }
            }
        }
    }


    /**
     * Stores root configuration
     *
     * @param root
     *           which will be stored
     * @throws CockpitConfigurationException
     *            when cockpit configuration cannot be loaded
     */
    public void storeRootConfig(final com.hybris.cockpitng.core.config.impl.jaxb.Config root) throws CockpitConfigurationException
    {
        if(BooleanUtils.isTrue(isDelayedPersistEnabled.get()))
        {
            LOG.debug("Caching intermediate configuration reset result: {} contexts", root.getContext().size());
            configToPersist.set(root);
        }
        else
        {
            executeWriteOperation(() -> storeRootConfigImmediately(root));
        }
    }


    protected void commitRootConfig() throws CockpitConfigurationException
    {
        final Config cachedConfig = configToPersist.get();
        if(cachedConfig != null)
        {
            try
            {
                LOG.info("Storing to database intermediate configuration reset result: {} contexts",
                                cachedConfig.getContext().size());
                executeWriteOperation(() -> storeRootConfigImmediately(cachedConfig));
            }
            catch(final CockpitConfigurationUnavailable ex)
            {
                throw new CockpitConfigurationException(ex);
            }
        }
    }


    protected void storeRootConfigImmediately(final com.hybris.cockpitng.core.config.impl.jaxb.Config root)
                    throws CockpitConfigurationException
    {
        assureSecure(true);
        try(final OutputStream out = new BufferedOutputStream(getConfigFileOutputStream()))
        {
            final Marshaller marshaller = createMarshaller(com.hybris.cockpitng.core.config.impl.jaxb.Config.class);
            marshaller.marshal(root, out);
        }
        catch(final JAXBException e)
        {
            Throwable originalException = e;
            if(e.getLinkedException() != null)
            {
                originalException = e.getLinkedException();
            }
            throw new CockpitConfigurationException(originalException.getMessage(), originalException);
        }
        catch(final IOException ioe)
        {
            throw new CockpitConfigurationException(ioe);
        }
        invalidateRootConfigurationCache();
        cacheRootConfiguration(root, getCurrentTimeInMillis());
    }


    private com.hybris.cockpitng.core.config.impl.jaxb.Config loadRootConfig(final InputStream inputStream)
                    throws CockpitConfigurationException
    {
        try
        {
            final XMLInputFactory xif = XMLInputFactory.newFactory();
            xif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
            xif.setProperty(XMLInputFactory.SUPPORT_DTD, false);
            final Unmarshaller unmarshaller = createUnmarshaller(com.hybris.cockpitng.core.config.impl.jaxb.Config.class);
            return (com.hybris.cockpitng.core.config.impl.jaxb.Config)unmarshaller
                            .unmarshal(xif.createXMLStreamReader(inputStream));
        }
        catch(final JAXBException e)
        {
            Throwable originalException = e;
            if(e.getLinkedException() != null)
            {
                originalException = e.getLinkedException();
            }
            throw new CockpitConfigurationException(originalException.getMessage(), originalException);
        }
        catch(final WstxLazyException e)
        {
            throw new CockpitConfigurationException(e.getMessage(), e);
        }
        catch(final XMLStreamException e)
        {
            LOG.error(
                            "Errors occurred while processing configuration file. Configuration may contain not allowed content (XML external entities and DTD are not supported):"
                                            + e.getLocalizedMessage(),
                            e);
        }
        finally
        {
            IOUtils.closeQuietly(inputStream);
        }
        return null;
    }


    private <C> C loadConfigElement(final Class<C> configType, final Node node) throws CockpitConfigurationException
    {
        synchronized(node)
        {
            try
            {
                final Unmarshaller unmarshaller = createUnmarshaller(configType);
                if(node instanceof Element)
                {
                    replaceNamespaces((Element)node);
                }
                return (C)unmarshaller.unmarshal(node);
            }
            catch(final JAXBException e)
            {
                Throwable originalException = e;
                if(e.getLinkedException() != null)
                {
                    originalException = e.getLinkedException();
                }
                throw new CockpitConfigurationException(originalException.getMessage(), originalException);
            }
        }
    }


    /**
     * @deprecated since 2005 {@link com.hybris.cockpitng.config.common.ActionsParamTransitionConcurrentTest} should be
     *             removed along with this method
     */
    @Deprecated(since = "2005", forRemoval = true)
    private void replaceNamespaces(final Element element)
    {
        if(element.getLocalName() != null)
        {
            final QName nodeName = new QName(element.getNamespaceURI(), element.getLocalName());
            if(getNamespaceReplacements().containsKey(nodeName))
            {
                final QName replacement = getNamespaceReplacements().get(nodeName);
                final Element newChild = element.getOwnerDocument().createElementNS(replacement.getNamespaceURI(),
                                replacement.getLocalPart());
                for(int attr = 0; attr < element.getAttributes().getLength(); attr++)
                {
                    newChild.appendChild(element.getAttributes().item(attr));
                }
                for(int chld = 0; chld < element.getChildNodes().getLength(); chld++)
                {
                    final Node child = element.getChildNodes().item(chld);
                    newChild.appendChild(child);
                }
                if(element.getParentNode() != null)
                {
                    element.getParentNode().replaceChild(newChild, element);
                }
            }
        }
        for(int chld = 0; chld < element.getChildNodes().getLength(); chld++)
        {
            final Node child = element.getChildNodes().item(chld);
            if(child instanceof Element)
            {
                replaceNamespaces((Element)child);
            }
        }
    }


    public <C> Node storeConfigElement(final C config) throws CockpitConfigurationException
    {
        try
        {
            final Marshaller marshaller = createMarshaller(config.getClass());
            final DOMResult domResult = new DOMResult();
            marshaller.marshal(config, domResult);
            Node node = domResult.getNode();
            if(node instanceof Document)
            {
                node = ((Document)node).getDocumentElement();
            }
            return node;
        }
        catch(final JAXBException e)
        {
            Throwable originalException = e;
            if(e.getLinkedException() != null)
            {
                originalException = e.getLinkedException();
            }
            throw new CockpitConfigurationException(originalException.getMessage(), originalException);
        }
    }


    /**
     * the Unmarshaller classes is not thread safe.
     * And each time you need to unmarshal a document, just create a new Unmarshaller from this context.
     *
     * refer to: https://javaee.github.io/jaxb-v2/doc/user-guide/ch06.html
     */
    private Unmarshaller createUnmarshaller(final Class<?> clazz) throws JAXBException
    {
        final JAXBContext context = getJAXBContext(clazz);
        return context.createUnmarshaller();
    }


    /**
     * the Marshaller classes is not thread safe.
     * And each time you need to marshal a document, just create a new Marshaller from this context.
     *
     * refer to: https://javaee.github.io/jaxb-v2/doc/user-guide/ch06.html
     */
    private Marshaller createMarshaller(final Class<?> clazz) throws JAXBException
    {
        final Marshaller marshaller = getJAXBContext(clazz).createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        return marshaller;
    }


    /**
     * the JAXBContext class is thread safe.
     * @param clazz
     * @return
     * @throws JAXBException
     */
    private JAXBContext getJAXBContext(final Class<?> clazz) throws JAXBException
    {
        if(jaxbContextFactory == null)
        {
            return JAXBContext.newInstance(clazz);
        }
        else
        {
            return jaxbContextFactory.createContext(clazz);
        }
    }


    protected File getRootDir()
    {
        return null;
    }


    public Map<String, CockpitConfigurationContextStrategy> getContextStrategies()
    {
        return contextStrategies;
    }


    public void setContextStrategies(final Map<String, CockpitConfigurationContextStrategy> contextStrategies)
    {
        this.contextStrategies = contextStrategies;
    }


    /**
     * Loads current configuration in form of XML
     *
     * @return configuration XML
     */
    public String getConfigAsString()
    {
        try
        {
            return executeReadOperation(this::getConfigAsStringImmediately);
        }
        catch(final CockpitConfigurationException e)
        {
            LOG.error(e.getLocalizedMessage(), e);
            return null;
        }
    }


    /**
     * Stores new configuration
     *
     * @param content
     *           new configuration in form of XML
     */
    public void setConfigAsString(final String content)
    {
        try
        {
            executeWriteOperation(() -> setConfigAsStringImmediately(content));
        }
        catch(final CockpitConfigurationException e)
        {
            LOG.error(e.getLocalizedMessage(), e);
        }
    }


    protected String getConfigAsStringImmediately()
    {
        assureSecure(false);
        final StringBuilder builder = new StringBuilder();
        try(final InputStream configFileInputStream = getConfigFileInputStream();
                        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(configFileInputStream, CHARSET_NAME)))
        {
            String line;
            while((line = bufferedReader.readLine()) != null)
            {
                builder.append(line).append("\n");
            }
        }
        catch(final IOException e)
        {
            if(LOG.isErrorEnabled())
            {
                LOG.error("could not load cockpit configuration", e);
            }
        }
        return builder.toString();
    }


    protected void setConfigAsStringImmediately(final String content)
    {
        assureSecure(true);
        try(final OutputStream out = new BufferedOutputStream(getConfigFileOutputStream()))
        {
            out.write(content.getBytes(CHARSET_NAME));
            invalidateRootConfigurationCache();
        }
        catch(final IOException e)
        {
            if(LOG.isErrorEnabled())
            {
                LOG.error("could not store cockpit configuration", e);
            }
        }
    }


    @Override
    public SchemaValidationStatus validate(final InputStream inputStream)
    {
        SchemaValidationStatus status;
        try
        {
            status = cockpitConfigValidator.validate(inputStream, getJAXBContext(Config.class));
        }
        catch(final JAXBException e)
        {
            LOG.error("Errors occurred while processing configuration file", e);
            status = SchemaValidationStatus.error(e.getMessage());
        }
        return status;
    }


    /**
     * Returns string content of the stream.
     *
     * @param inputStream
     * @return string content of the stream.
     */
    protected String convertConfigToString(final InputStream inputStream)
    {
        final StringBuilder builder = new StringBuilder();
        try
        {
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, CHARSET_NAME));
            String line;
            while((line = bufferedReader.readLine()) != null)
            {
                builder.append(line).append("\n");
            }
            bufferedReader.close();
        }
        catch(final IOException e)
        {
            if(LOG.isErrorEnabled())
            {
                LOG.error("could not load cockpit configuration", e);
            }
        }
        return builder.toString();
    }


    /**
     * Reset cockpit configuration to default. The method looks up configuration file that is defined as a cockpit
     * configuration entry <b>cockpitng.config.default</b>, for example :
     * cockpitng.config.default=/backoffice-web/cockpit-config-default.xml <b>The file path must be relative to resource
     * folder</b>. If <b>cockpitng.config.default</b> is not defined or the file does not exist, a new 'default'
     * configuration will be generated from all default configuration snippets of all widgets present in the system.
     */
    @Override
    public void resetToDefaults()
    {
        try
        {
            executeWriteOperation(this::resetToDefaultsInternal);
        }
        catch(final CockpitConfigurationException e)
        {
            LOG.error(e.getLocalizedMessage(), e);
        }
    }


    /**
     * Allows to perform arbitrary calls to {@link #storeRootConfig(Config)} without writing the actual data into the
     * backing storage. Only at the end of this call the storage is written.
     *
     * @param logic
     *           the business logic potentially invoking {@link #storeRootConfig(Config)}
     */
    @Override
    public void withDelayedWrite(final Runnable logic)
    {
        // nested --> just run logic
        if(BooleanUtils.isTrue(isDelayedPersistEnabled.get()))
        {
            logic.run();
        }
        // outer --> delay write, run logic, commit
        else
        {
            try
            {
                isDelayedPersistEnabled.set(true);
                logic.run();
                commitRootConfig();
            }
            catch(final CockpitConfigurationException e)
            {
                LOG.error("Exception thrown while committing cockpit configuration: ", e);
            }
            finally
            {
                isDelayedPersistEnabled.remove();
                configToPersist.remove();
            }
        }
    }


    protected void resetToDefaultsInternal()
    {
        withDelayedWrite(() -> {
            assureSecure(true);
            try(final InputStream defaultConfigStream = persistenceStrategy.getDefaultConfigurationInputStream())
            {
                if(defaultConfigStream != null)
                {
                    setConfigAsString(StringUtils.defaultIfBlank(convertConfigToString(defaultConfigStream), StringUtils.EMPTY));
                }
                else
                {
                    setConfigAsString(StringUtils.EMPTY);
                }
            }
            catch(final IOException e)
            {
                LOG.error(e.getLocalizedMessage(), e);
            }
        });
    }


    /**
     * Caches a root configuration with specified cache time.
     * <p>
     * Cache timestamp may determine whether to invalidate cache or not. If at any moment current root configuration origin
     * time is greater then cache timestamp, then cache is invalidated and configuration is loaded again.
     *
     * @param rootConfig
     *           configuration to be cached
     * @param cacheTimestamp
     *           cache timestamp
     */
    protected void cacheRootConfiguration(final Config rootConfig, final long cacheTimestamp)
    {
        assureSecure(true);
        LOG.info("Storing root config in cache: {} contexts, timestamp={}", rootConfig.getContext().size(), cacheTimestamp);
        synchronized(this)
        {
            getConfigurationCache().cacheRootConfiguration(rootConfig, cacheTimestamp);
            getConfigurationCache().cacheContexts(rootConfig, readAllContexts(rootConfig), new ReadContextResolver());
        }
    }


    protected InputStream getConfigFileInputStream()
    {
        try
        {
            return persistenceStrategy.getConfigurationInputStream();
        }
        catch(final IOException e)
        {
            LOG.error(e.getLocalizedMessage(), e);
            return null;
        }
    }


    protected OutputStream getConfigFileOutputStream()
    {
        try
        {
            return persistenceStrategy.getConfigurationOutputStream();
        }
        catch(final IOException e)
        {
            LOG.error(e.getLocalizedMessage(), e);
            return null;
        }
    }


    protected long getLastModification()
    {
        return persistenceStrategy.getLastModification();
    }


    /**
     * Marks configuration as invalid and force service to load root configuration once again, whenever it is requested.
     */
    protected void invalidateRootConfigurationCache()
    {
        try
        {
            executeWriteOperation(this::invalidateRootConfigurationCacheImmediately);
        }
        catch(final CockpitConfigurationException e)
        {
            LOG.error(e.getLocalizedMessage(), e);
        }
    }


    protected void invalidateRootConfigurationCacheImmediately()
    {
        assureSecure(true);
        writeLockState.get().setCallback(onCacheInvalidationCallback);
        getConfigurationCache().clear();
    }


    /**
     * Registers callback which is invoked when cache is invalidating.
     *
     * @param callback
     *           which is invoked when {@link #invalidateRootConfigurationCache()} is called.
     */
    public void onCacheInvalidation(final Runnable callback)
    {
        this.onCacheInvalidationCallback = callback;
    }


    /**
     * Marks context configurations invalid and forces service to load tem once again from root
     */
    protected void invalidateConfigurationCache()
    {
        try
        {
            executeWriteOperation(this::invalidateConfigurationCacheImmediately);
        }
        catch(final CockpitConfigurationException e)
        {
            LOG.error(e.getLocalizedMessage(), e);
        }
    }


    protected void invalidateConfigurationCacheImmediately()
    {
        assureSecure(true);
        getConfigurationCache().clearConfigurations();
    }


    /**
     * Marks context attributes invalid and forces service to load them once again from root
     */
    protected void invalidateAttributesCache(final Context context)
    {
        try
        {
            executeWriteOperation(() -> invalidateAttributesCacheImmediately(context));
        }
        catch(final CockpitConfigurationException e)
        {
            LOG.error(e.getLocalizedMessage(), e);
        }
    }


    protected void invalidateAttributesCacheImmediately(final Context context)
    {
        assureSecure(true);
        getConfigurationCache().clearAttributes(context);
    }


    protected long getCurrentTimeInMillis()
    {
        return System.currentTimeMillis();
    }


    protected MergeUtils getMergeUtils()
    {
        return mergeUtils;
    }


    public void setMergeUtils(final MergeUtils mergeUtils)
    {
        this.mergeUtils = mergeUtils;
    }


    public void setJaxbContextFactory(final JAXBContextFactory jaxbContextFactory)
    {
        this.jaxbContextFactory = jaxbContextFactory;
    }


    public Map<String, List<CockpitConfigurationFallbackStrategy>> getFallbackStrategies()
    {
        return fallbackStrategies;
    }


    @Required
    public void setFallbackStrategies(final Map<String, List<CockpitConfigurationFallbackStrategy>> fallbackStrategies)
    {
        this.fallbackStrategies = fallbackStrategies;
    }


    public Map<Map<String, String>, CockpitConfigurationAdapter<?>> getAdapters()
    {
        return adapters;
    }


    public void setAdapters(final Map<Map<String, String>, CockpitConfigurationAdapter<?>> adapters)
    {
        this.adapters = adapters;
    }


    public Map<String, CockpitConfigurationAdapter<?>> getConfigTypesAdapters()
    {
        return configTypesAdapters;
    }


    public void setConfigTypesAdapters(final Map<String, CockpitConfigurationAdapter<?>> configTypesAdapters)
    {
        this.configTypesAdapters = configTypesAdapters;
    }


    protected CockpitProperties getCockpitProperties()
    {
        return cockpitProperties;
    }


    public void setCockpitProperties(final CockpitProperties cockpitProperties)
    {
        this.cockpitProperties = cockpitProperties;
    }


    public CockpitConfigurationPersistenceStrategy getPersistenceStrategy()
    {
        return persistenceStrategy;
    }


    @Required
    public void setPersistenceStrategy(final CockpitConfigurationPersistenceStrategy persistenceStrategy)
    {
        this.persistenceStrategy = persistenceStrategy;
    }


    public List<WidgetConfigurationContextDecorator> getWidgetConfigurationContextDecoratorList()
    {
        return widgetConfigurationContextDecoratorList;
    }


    public void setWidgetConfigurationContextDecoratorList(
                    final List<WidgetConfigurationContextDecorator> widgetConfigurationContextDecoratorList)
    {
        this.widgetConfigurationContextDecoratorList = widgetConfigurationContextDecoratorList;
    }


    protected ConfigurationCache getConfigurationCache()
    {
        return configurationCache;
    }


    @Required
    public void setConfigurationCache(final ConfigurationCache configurationCache)
    {
        this.configurationCache = configurationCache;
    }


    @Required
    public void setCockpitConfigValidator(final SchemaConfigValidator cockpitConfigValidator)
    {
        this.cockpitConfigValidator = cockpitConfigValidator;
    }


    protected ConfigurationImportSupport getImportSupport()
    {
        return importSupport;
    }


    @Required
    public void setImportSupport(final ConfigurationImportSupport importSupport)
    {
        this.importSupport = importSupport;
    }


    /**
     * @deprecated since 2005
     */
    @Deprecated(since = "2005", forRemoval = true)
    protected Map<QName, QName> getNamespaceReplacements()
    {
        return Collections.unmodifiableMap(namespaceReplacements);
    }


    /**
     * @deprecated since 2005
     */
    @Deprecated(since = "2005", forRemoval = true)
    public void setNamespaceReplacements(final Map<String, String> namespaceReplacements)
    {
        this.namespaceReplacements.clear();
        namespaceReplacements.forEach((from, to) -> {
            final QName qualifiedFrom = getQualifiedName(from);
            final QName qualifiedTo = getQualifiedName(to);
            if(qualifiedFrom != null && qualifiedTo != null)
            {
                this.namespaceReplacements.put(qualifiedFrom, qualifiedTo);
            }
        });
    }


    private QName getQualifiedName(final String name)
    {
        final Matcher matcher = Pattern.compile("^\\{([^}]+)}([^{}]+)$").matcher(name);
        if(matcher.matches())
        {
            return new QName(matcher.group(1), matcher.group(2));
        }
        else
        {
            return null;
        }
    }


    @Override
    public void reset()
    {
        invalidateRootConfigurationCache();
    }


    protected void assureSecure(final boolean write)
    {
        if(write && writeLockState.get().getRegisteredLocks() == 0)
        {
            throw new IllegalStateException(
                            "Write cache method called with insufficient rights. Please use #executeWriteOperation to assure secure cache access!");
        }
        else if(!write && readLockState.get().getRegisteredLocks() == 0)
        {
            throw new IllegalStateException(
                            "Read cache method called with insufficient rights. Please use #executeReadOperation to assure secure cache access!");
        }
    }


    protected <V> V executeReadOperation(final CacheOperationWithResult operation) throws CockpitConfigurationException
    {
        boolean locked = false;
        try
        {
            acquireReadLock();
            locked = true;
            return (V)operation.execute();
        }
        catch(final CockpitConfigurationUnavailable ex)
        {
            throw new CockpitConfigurationException(ex);
        }
        finally
        {
            if(locked)
            {
                releaseReadLock();
            }
        }
    }


    protected void executeReadOperation(final CacheOperation operation) throws CockpitConfigurationException
    {
        boolean locked = false;
        try
        {
            acquireReadLock();
            locked = true;
            operation.execute();
        }
        catch(final CockpitConfigurationUnavailable ex)
        {
            throw new CockpitConfigurationException(ex);
        }
        finally
        {
            if(locked)
            {
                releaseReadLock();
            }
        }
    }


    protected void executeWriteOperation(final CacheOperation operation) throws CockpitConfigurationException
    {
        boolean locked = false;
        try
        {
            acquireWriteLock();
            locked = true;
            operation.execute();
        }
        catch(final CockpitConfigurationUnavailable ex)
        {
            throw new CockpitConfigurationException(ex);
        }
        finally
        {
            if(locked)
            {
                releaseWriteLock();
            }
        }
    }


    protected <V> V getCacheValue(final Supplier<V> value, final CacheOperation cacheOperation)
                    throws CockpitConfigurationException
    {
        final AtomicReference<V> cacheValue = new AtomicReference<>(value.get());
        while(cacheValue.get() == null)
        {
            executeWriteOperation(() -> {
                if(cacheValue.updateAndGet(v -> value.get()) == null)
                {
                    cacheOperation.execute();
                    cacheValue.set(value.get());
                }
            });
        }
        return cacheValue.get();
    }


    protected void acquireReadLock()
    {
        final LockState rlState = readLockState.get();
        if(!writeLockState.get().isLockStampRegistered() && !rlState.isLockStampRegistered())
        {
            rlState.registerLockStamp(tryAcquireReadLockImmediately());
        }
        rlState.registerLockEntry();
    }


    private long tryAcquireReadLockImmediately()
    {
        try
        {
            final long timeout = getCockpitProperties().getInteger(PROPERTY_READ_ATTEMPT_TIMEOUT);
            final long stamp = lock.tryReadLock(timeout > 0 ? timeout : PROPERTY_DEFAULT_READ_ATTEMPT_TIMEOUT, TimeUnit.SECONDS);
            if(stamp == 0)
            {
                throw new CockpitConfigurationUnavailable("Unable to get read access to configuration in " + timeout + " seconds");
            }
            return stamp;
        }
        catch(final InterruptedException e)
        {
            Thread.currentThread().interrupt();
            throw new CockpitConfigurationUnavailable(e);
        }
    }


    protected void releaseReadLock()
    {
        final LockState rlState = readLockState.get();
        if(rlState.isLockStampRegistered() && rlState.getRegisteredLocks() == 1)
        {
            lock.unlockRead(rlState.getLockStamp());
            rlState.unregisterLockStamp();
        }
        // in case of an exception above (wrong stamp) countdown *does not* take place!!!
        if(rlState.unregisterLockEntry() == 0)
        {
            try
            {
                rlState.executeCallback();
            }
            finally
            {
                // ensure we remove read lock state
                readLockState.remove();
            }
        }
    }


    protected void acquireWriteLock()
    {
        final LockState rlState = readLockState.get();
        final LockState wlState = writeLockState.get();
        boolean writeLockSuccess = false;
        try
        {
            // got read lock --> drop it
            // !!! note that we *keep* the read lock count --> release write lock will re-establish read lock!!!
            if(rlState.isLockStampRegistered())
            {
                lock.unlockRead(rlState.getLockStamp());
                rlState.unregisterLockStamp();
            }
            // no write lock yet --> get one
            if(!wlState.isLockStampRegistered())
            {
                wlState.registerLockStamp(tryAcquireWriteLockImmediately());
            }
            writeLockSuccess = true;
        }
        finally
        {
            // success --> increase read lock counter
            if(writeLockSuccess)
            {
                wlState.registerLockEntry();
            }
            // failure --> re-create read lock if one was present before
            else if(rlState.getRegisteredLocks() > 0)
            {
                boolean reCreatedReadLock = false;
                try
                {
                    rlState.registerLockStamp(tryAcquireReadLockImmediately());
                    reCreatedReadLock = true;
                }
                catch(final Exception e)
                {
                    // don't throw exception to avoid hiding the one from outer try clause
                    LOG.error(e.getMessage(), e);
                }
                finally
                {
                    // failure again --> clean up thread read lock state at least
                    if(!reCreatedReadLock)
                    {
                        readLockState.remove();
                    }
                }
            }
        }
    }


    private long tryAcquireWriteLockImmediately()
    {
        try
        {
            final long timeout = getCockpitProperties().getInteger(PROPERTY_WRITE_ATTEMPT_TIMEOUT);
            final long stamp = lock.tryWriteLock(timeout > 0 ? timeout : PROPERTY_DEFAULT_WRITE_ATTEMPT_TIMEOUT, TimeUnit.SECONDS);
            if(stamp == 0)
            {
                throw new CockpitConfigurationUnavailable("Unable to get write access to configuration in " + timeout + " seconds");
            }
            return stamp;
        }
        catch(final InterruptedException e)
        {
            Thread.currentThread().interrupt();
            throw new CockpitConfigurationUnavailable(e);
        }
    }


    protected void releaseWriteLock()
    {
        final LockState wlState = writeLockState.get();
        final LockState rlState = readLockState.get();
        boolean writeLockRemoved = false;
        boolean readLockReCreated = false;
        if(!wlState.isLockStampRegistered() || !lock.isWriteLocked() || wlState.getRegisteredLocks() < 1)
        {
            throw new IllegalStateException("No write lock existing stamp=" + wlState.isLockStampRegistered()
                            + " lock.isWriteLocked=" + lock.isWriteLocked() + " count=" + wlState.getRegisteredLocks());
        }
        try
        {
            // outer holder --> release write lock
            if(wlState.getRegisteredLocks() == 1)
            {
                lock.unlockWrite(wlState.getLockStamp());
                writeLockRemoved = true;
                wlState.unregisterLockStamp();
                wlState.executeCallback();
                // re-create read lock in case it was present before
                if(rlState.getRegisteredLocks() > 0)
                {
                    rlState.registerLockStamp(tryAcquireReadLockImmediately());
                    readLockReCreated = true;
                }
            }
            writeLockRemoved = true;
        }
        // ensure 'countdown' always happen
        finally
        {
            if(writeLockRemoved && wlState.unregisterLockEntry() == 0)
            {
                writeLockState.remove();
                // cleanup read lock state, except it was re-created
                if(!readLockReCreated)
                {
                    readLockState.remove();
                }
            }
        }
    }


    protected static class LockState
    {
        private Long stamp;
        private int count;
        private Runnable callback;


        public boolean isLockStampRegistered()
        {
            return stamp != null;
        }


        public long getLockStamp()
        {
            if(!isLockStampRegistered())
            {
                throw new IllegalStateException("No lock stamp acquired!");
            }
            return stamp;
        }


        public void registerLockStamp(final long stamp)
        {
            if(isLockStampRegistered())
            {
                throw new IllegalStateException("Lock stamp already acquired: " + this.stamp);
            }
            this.stamp = stamp;
        }


        public void unregisterLockStamp()
        {
            if(!isLockStampRegistered())
            {
                throw new IllegalStateException("No lock stamp acquired!");
            }
            this.stamp = null;
        }


        public int getRegisteredLocks()
        {
            return count;
        }


        public int registerLockEntry()
        {
            return ++count;
        }


        public int unregisterLockEntry()
        {
            if(count == 0)
            {
                throw new IllegalStateException("No locks registered");
            }
            return --count;
        }


        public void setCallback(final Runnable callback)
        {
            if(this.callback != null && this.callback != callback)
            {
                throw new IllegalStateException("Callback already set!");
            }
            this.callback = callback;
        }


        public void executeCallback()
        {
            if(this.callback != null)
            {
                this.callback.run();
            }
            this.callback = null;
        }
    }


    @FunctionalInterface
    protected interface CacheOperation
    {
        void execute() throws CockpitConfigurationException;
    }


    @FunctionalInterface
    protected interface CacheOperationWithResult
    {
        Object execute() throws CockpitConfigurationException;
    }


    private static final class ImmutableConfigContext implements ConfigContext
    {
        private final ConfigContext configContext;


        private ImmutableConfigContext(final ConfigContext configContext)
        {
            this.configContext = configContext;
        }


        @Override
        public String getAttribute(final String name)
        {
            return configContext.getAttribute(name);
        }


        @Override
        public Set<String> getAttributeNames()
        {
            return Collections.unmodifiableSet(configContext.getAttributeNames());
        }


        @Override
        public boolean equals(final Object o)
        {
            if(this == o)
            {
                return true;
            }
            if(o == null || getClass() != o.getClass())
            {
                return false;
            }
            final ImmutableConfigContext that = (ImmutableConfigContext)o;
            return configContext.equals(that.configContext);
        }


        @Override
        public int hashCode()
        {
            return configContext.hashCode();
        }
    }


    private class ReadContextResolver implements ConfigurationContextResolver
    {
        @Override
        public Map<String, String> getContextAttributes(final Context context)
        {
            return readAttributes(context);
        }
    }


    private class AttributeStrategyComparator implements ContextAttributeComparator
    {
        @Override
        public boolean matches(final String attribute, final String pattern, final String value)
        {
            return contextAttributesMatches(attribute, pattern, value);
        }
    }
}
