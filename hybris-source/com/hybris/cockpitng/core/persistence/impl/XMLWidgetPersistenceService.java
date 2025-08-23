/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.persistence.impl;

import static java.util.function.Predicate.not;

import com.google.common.collect.Sets;
import com.hybris.cockpitng.core.CockpitComponentDefinitionService;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.WidgetConnection;
import com.hybris.cockpitng.core.WidgetDefinition;
import com.hybris.cockpitng.core.WidgetService;
import com.hybris.cockpitng.core.persistence.ConfigurationImportSupport;
import com.hybris.cockpitng.core.persistence.ConfigurationInterpreter;
import com.hybris.cockpitng.core.persistence.WidgetPersistenceService;
import com.hybris.cockpitng.core.persistence.exception.WidgetPersistenceException;
import com.hybris.cockpitng.core.persistence.impl.jaxb.Import;
import com.hybris.cockpitng.core.persistence.impl.jaxb.Requirement;
import com.hybris.cockpitng.core.persistence.impl.jaxb.Socket;
import com.hybris.cockpitng.core.persistence.impl.jaxb.VirtualSockets;
import com.hybris.cockpitng.core.persistence.impl.jaxb.WidgetExtension;
import com.hybris.cockpitng.core.persistence.impl.jaxb.WidgetMove;
import com.hybris.cockpitng.core.persistence.impl.jaxb.WidgetRemoveAllEntry;
import com.hybris.cockpitng.core.persistence.impl.jaxb.WidgetRemoveEntry;
import com.hybris.cockpitng.core.persistence.impl.jaxb.Widgets;
import com.hybris.cockpitng.core.persistence.packaging.WidgetLibUtils;
import com.hybris.cockpitng.core.util.ClassLoaderUtils;
import com.hybris.cockpitng.core.util.CockpitProperties;
import com.hybris.cockpitng.core.util.template.TemplateEngine;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;

/**
 * Service responsible for storing and loading widget tree from XML
 */
public class XMLWidgetPersistenceService implements WidgetPersistenceService
{
    /**
     * This field should become private. Access to the import root file should happen via dedicated method.
     *
     * @see DefaultConfigurationImportSupport#setImportRootFile(String)
     * @deprecated since 1808
     */
    @Deprecated(since = "1808", forRemoval = true)
    protected static final File IMPORT_ROOT_FILE = new File(".");
    protected static final String DEFAULT_FILE_NAME_WIDGETS_XML = "widgets.xml";
    private static final Logger LOG = LoggerFactory.getLogger(XMLWidgetPersistenceService.class);
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final XMLWidgetPersistenceJAXBLookupHandler jaxbLookupHandler;
    private final XMLWidgetPersistenceJAXBConverter jaxbConverter;
    private Marshaller marshaller;
    private Unmarshaller unmarshaller;
    private CockpitProperties cockpitProperties;
    private String defaultWidgetConfigSpring;
    private String defaultWidgetConfig;
    private WidgetLibUtils widgetLibUtils;
    private WidgetService widgetService;
    private CockpitComponentDefinitionService widgetDefinitionService;
    private boolean storeAsExtension;
    private DefaultWidgetAccessResolver widgetAccessResolver;
    private ConfigurationImportSupport importSupport;


    /**
     * Constructs the service with default implementations of the jaxb lookup and converter.
     *
     * @see XMLWidgetPersistenceJAXBLookupHandler
     * @see XMLWidgetPersistenceJAXBConverter
     */
    public XMLWidgetPersistenceService()
    {
        jaxbLookupHandler = new XMLWidgetPersistenceJAXBLookupHandler();
        jaxbConverter = new XMLWidgetPersistenceJAXBConverter(jaxbLookupHandler);
    }


    XMLWidgetPersistenceService(final XMLWidgetPersistenceJAXBLookupHandler jaxbLookupHandler,
                    final XMLWidgetPersistenceJAXBConverter jaxbConverter)
    {
        this.jaxbLookupHandler = jaxbLookupHandler;
        this.jaxbConverter = jaxbConverter;
    }


    private static void mergeVirtualSockets(final String widgetId, final List<Socket> widgetSockets,
                    final List<Socket> extensionSockets)
    {
        final Map<String, Socket> definedSockets = widgetSockets.stream()
                        .collect(Collectors.toMap(Socket::getId, Function.identity()));
        final Set<String> incompatibleDuplicates = extensionSockets.stream()
                        .filter(socket -> definedSockets.containsKey(socket.getId()))
                        .peek(socket -> LOG.warn("Duplicated definition of virtual socket '{}' for widget '{}'", socket.getId(), widgetId))
                        .filter(not(socket -> isExtensionSocketCompatible(socket, definedSockets))).map(Socket::getId)
                        .collect(Collectors.toSet());
        if(!incompatibleDuplicates.isEmpty())
        {
            final String incompatibleDuplicatesIds = StringUtils.join(incompatibleDuplicates, ", ");
            LOG.error("Illegal virtual socket definition duplicates for widget '{}' have been detected: {}", widgetId,
                            incompatibleDuplicatesIds);
        }
        extensionSockets.stream().filter(not(socket -> definedSockets.containsKey(socket.getId()))).forEach(widgetSockets::add);
    }


    private static boolean isExtensionSocketCompatible(final Socket extensionSocket, final Map<String, Socket> widgetSockets)
    {
        final Socket definedSocket = widgetSockets.get(extensionSocket.getId());
        if(definedSocket != null)
        {
            return StringUtils.equals(extensionSocket.getType(), definedSocket.getType())
                            && Objects.equals(extensionSocket.getMultiplicity(), definedSocket.getMultiplicity())
                            && Objects.equals(extensionSocket.getVisibility(), definedSocket.getVisibility());
        }
        return true;
    }


    @Override
    public Widget loadWidgetTree(final String widgetId)
    {
        try
        {
            return loadWidgetTree(widgetId, getWidgetsFileAsStream());
        }
        catch(final FileNotFoundException e)
        {
            LOG.error("could not load widget tree for widget ID '" + widgetId + "'", e);
            return null;
        }
    }


    /**
     * Loads widget's tree. Widget can consist of any number of children which can form a tree.
     *
     * @param widgetId
     *           an id of widget's tree root
     * @param inputStream
     *           from tree is loaded
     * @return a widget tree
     */
    public Widget loadWidgetTree(final String widgetId, final InputStream inputStream)
    {
        final Widgets widgets = loadWidgets(inputStream);
        try
        {
            applyImports(widgets);
            applyExtensions(widgets);
        }
        catch(final IOException ex)
        {
            LOG.error(ex.getLocalizedMessage(), ex);
        }
        final com.hybris.cockpitng.core.persistence.impl.jaxb.Widget widget = getWidgetById(widgets, widgetId);
        return (widget == null ? null : createWidgetTree(widgets, widget));
    }


    /**
     * @see #applyImports(File, Widgets, Set)
     * @deprecated since 6.5
     */
    @Deprecated(since = "6.5", forRemoval = true)
    protected void applyImports(final Widgets widgets, final Set<String> alreadyImportedResources)
    {
        try
        {
            applyImports(getImportRootFile(), widgets, alreadyImportedResources);
        }
        catch(final IOException e)
        {
            LOG.error(e.getLocalizedMessage(), e);
        }
    }


    protected void applyImports(final Widgets widgets) throws IOException
    {
        final ConfigurationInterpreter<? super Widgets> interpreter = new ConfigurationInterpreter<>()
        {
            @Override
            public Widgets load(final Import importConfiguration, final InputStream configurationStream)
            {
                final Widgets loadedWidgets = loadWidgetsInternal(configurationStream);
                final String contextId = importConfiguration.getModuleUrl();
                if(contextId != null)
                {
                    setContextId(loadedWidgets, contextId);
                }
                return loadedWidgets;
            }


            @Override
            public List<Import> getImports(final Widgets configuration)
            {
                return configuration.getImports();
            }


            @Override
            public List<Requirement> getRequirements(final Widgets configuration)
            {
                return configuration.getRequires();
            }


            @Override
            public Set<String> getRequiredParameters(final Widgets configuration)
            {
                final String[] split = StringUtils.split(configuration.getRequiredParameters(), ",");
                return Optional.ofNullable(split).<Set<String>>map(Sets::newHashSet).orElseGet(Collections::emptySet);
            }


            @Override
            public Widgets merge(final Import importConfiguration, final Widgets target, final Widgets source)
            {
                mergeWidgets(target, source, importConfiguration);
                return target;
            }
        };
        getImportSupport().resolveImports(widgets, interpreter);
        applyImports(getImportRootFile(), widgets, new HashSet<>());
    }


    /**
     * Refer: ConfigurationImportSupport#resolveImports(Object, ConfigurationInterpreter)
     * @deprecated since 2005
     */
    @Deprecated(since = "2005", forRemoval = true)
    protected boolean applyImports(final File currentPath, final Widgets widgets, final Set<String> importedResources)
                    throws IOException
    {
        return false;
    }


    /**
     * Refer: ConfigurationImportSupport#resolveImports(Object, ConfigurationInterpreter)
     * @deprecated since 2005
     */
    @Deprecated(since = "2005", forRemoval = true)
    protected boolean checkRequirements(final File currentPath, final Widgets widgets, final Set<String> importedResources)
    {
        return false;
    }


    /**
     * Refer: ConfigurationImportSupport#resolveImports(Object, ConfigurationInterpreter)
     * @deprecated since 2005
     */
    @Deprecated(since = "2005", forRemoval = true)
    protected void loadImports(final File currentPath, final Widgets widgets, final Set<String> importedResources)
                    throws IOException
    {
    }


    /**
     * Refer: ConfigurationImportSupport#resolveImports(Object, ConfigurationInterpreter)
     * @deprecated since 2005
     */
    @Deprecated(since = "2005", forRemoval = true)
    protected void applyImport(final File currentPath, final Widgets widgets, final Set<String> importedResources,
                    final Import resourceImport) throws IOException
    {
    }


    protected void setContextId(final Widgets importedWidgets, final String contextId)
    {
        importedWidgets.getImports().stream().filter(e -> StringUtils.isBlank(e.getModuleUrl()))
                        .forEach(e -> e.setModuleUrl(contextId));
        importedWidgets.getWidgetExtension().stream().filter(e -> StringUtils.isBlank(e.getContextId()))
                        .forEach(e -> e.setContextId(contextId));
        importedWidgets.getWidgetConnection().stream().filter(e -> StringUtils.isBlank(e.getModuleUrl()))
                        .forEach(e -> e.setModuleUrl(contextId));
    }


    /**
     * Refer: ConfigurationImportSupport#resolveImports(Object, ConfigurationInterpreter)
     * @deprecated since 2005
     */
    @Deprecated(since = "2005", forRemoval = true)
    protected InputStream resolveResource(final String resourcePath, final Map<String, Object> resourceParameters)
                    throws IOException
    {
        return InputStream.nullInputStream();
    }


    protected String resolveResourcePath(final File resource) throws IOException
    {
        final Path rootPath = Paths.get(getImportRootFile().getCanonicalPath());
        final Path resourcePath = Paths.get(resource.getCanonicalPath());
        final Path relativePath = resourcePath.startsWith(rootPath)
                        ? resourcePath.subpath(rootPath.getNameCount(), resourcePath.getNameCount())
                        : resourcePath;
        return relativePath.toString();
    }


    /**
     * Refer: ConfigurationImportSupport#resolveImports(Object, ConfigurationInterpreter)
     * @deprecated since 2005
     */
    @Deprecated(since = "2005", forRemoval = true)
    protected File getImportRootFile()
    {
        return IMPORT_ROOT_FILE;
    }


    /**
     * @see #loadImports(File, Widgets, Set)
     * @deprecated since 6.5
     */
    @Deprecated(since = "6.5", forRemoval = true)
    protected void loadImports(final Widgets widgets, final Set<String> alreadyImportedResources)
    {
        try
        {
            loadImports(getImportRootFile(), widgets, alreadyImportedResources);
        }
        catch(final IOException e)
        {
            LOG.error(e.getLocalizedMessage(), e);
        }
    }


    protected Set<String> getUndefinedParameters(final Widgets widgets, final Set<String> definedValues)
    {
        final String[] required = StringUtils.split(widgets.getRequiredParameters());
        if(required != null)
        {
            return Arrays.stream(required).filter(parameter -> !definedValues.contains(parameter)).collect(Collectors.toSet());
        }
        else
        {
            return Collections.emptySet();
        }
    }


    protected void mergeWidgets(final Widgets widgets, final Widgets toAdd, final Import config)
    {
        widgets.getWidget().addAll(toAdd.getWidget());
        widgets.getWidgetExtension().addAll(toAdd.getWidgetExtension());
        widgets.getWidgetConnection().addAll(toAdd.getWidgetConnection());
        widgets.getWidgetConnectionRemove().addAll(toAdd.getWidgetConnectionRemove());
    }


    /**
     * @see #mergeWidgets(Widgets, Widgets, Import)
     * @deprecated since 6.5
     */
    @Deprecated(since = "6.5", forRemoval = true)
    protected void mergeWidgets(final Widgets widgets, final Widgets toAdd)
    {
        mergeWidgets(widgets, toAdd, new Import());
    }


    protected void applyExtensions(final Widgets widgets)
    {
        final List<WidgetExtension> widgetExtensions = extractWidgetExtensions(widgets);
        final Map<String, Set<String>> addedWidgets = new HashMap<>();
        for(final WidgetExtension widgetExtension : widgetExtensions)
        {
            final String widgetId = widgetExtension.getWidgetId();
            final String contextId = StringUtils.isBlank(widgetExtension.getContextId()) ? null : widgetExtension.getContextId();
            final com.hybris.cockpitng.core.persistence.impl.jaxb.Widget widget = getWidgetById(widgets, widgetId);
            if(widget == null)
            {
                LOG.error("Could not apply extension to widget with id '{}', widget doesn't exist.", widgetId);
                continue;
            }
            final Set<String> appliedExtensions = applyExtension(widgets, widget, widgetExtension, addedWidgets);
            final Set<String> contextWidgets = addedWidgets.get(contextId);
            if(contextWidgets == null)
            {
                addedWidgets.put(contextId, appliedExtensions);
            }
            else
            {
                contextWidgets.addAll(appliedExtensions);
            }
        }
        widgets.getWidgetExtension().clear();
    }


    protected List<WidgetExtension> extractWidgetExtensions(final Widgets widgets)
    {
        if(widgets == null)
        {
            return new ArrayList<>();
        }
        return new ArrayList<>(widgets.getWidgetExtension());
    }


    protected List<com.hybris.cockpitng.core.persistence.impl.jaxb.WidgetConnection> extractWidgetConnections(
                    final Widgets widgets)
    {
        if(widgets == null)
        {
            return new ArrayList<>();
        }
        return new ArrayList<>(widgets.getWidgetConnection());
    }


    protected Set<String> applyExtension(final Widgets widgets,
                    final com.hybris.cockpitng.core.persistence.impl.jaxb.Widget widget, final WidgetExtension extension,
                    final Map<String, Set<String>> addedWidgets)
    {
        applyExtensionRemove(widget, extension, addedWidgets);
        applyExtensionMove(widgets, widget, extension);
        widget.getSetting().addAll(extension.getSetting());
        applyExtensionVirtualSockets(widget, extension);
        applyExtensionAccess(widget, extension);
        return applyExtensionAdd(widget, extension);
    }


    protected void applyExtensionVirtualSockets(final com.hybris.cockpitng.core.persistence.impl.jaxb.Widget widget,
                    final WidgetExtension extension)
    {
        if(extension.getVirtualSockets() != null)
        {
            if(widget.getVirtualSockets() == null)
            {
                widget.setVirtualSockets(new VirtualSockets());
            }
            mergeVirtualSockets(widget.getId(), widget.getVirtualSockets().getInput(), extension.getVirtualSockets().getInput());
            mergeVirtualSockets(widget.getId(), widget.getVirtualSockets().getOutput(), extension.getVirtualSockets().getOutput());
        }
    }


    protected void applyExtensionRemove(final com.hybris.cockpitng.core.persistence.impl.jaxb.Widget widget,
                    final WidgetExtension extension, final Map<String, Set<String>> addedWidgets)
    {
        final Set<String> removeWidgetIds = new HashSet<>();
        boolean clearAll = false;
        boolean clearSettings = true;
        for(final WidgetRemoveAllEntry removeAllEntry : extension.getRemoveAll())
        {
            if(StringUtils.isBlank(removeAllEntry.getContextId()))
            {
                clearAll = true;
                clearSettings = removeAllEntry.isIncludeSettings();
                break;
            }
            else
            {
                final Set<String> contextWidgets = addedWidgets.get(removeAllEntry.getContextId());
                if(contextWidgets != null)
                {
                    removeWidgetIds.addAll(contextWidgets);
                }
            }
        }
        if(clearAll)
        {
            if(clearSettings)
            {
                widget.getSetting().clear();
            }
            widget.getWidget().clear();
        }
        else
        {
            for(final WidgetRemoveEntry entry : extension.getRemove())
            {
                removeWidgetIds.add(entry.getWidgetId());
            }
            removeWidgets(widget, removeWidgetIds);
        }
    }


    protected void removeWidgets(final com.hybris.cockpitng.core.persistence.impl.jaxb.Widget widget,
                    final Set<String> removeWidgetIds)
    {
        if(CollectionUtils.isNotEmpty(removeWidgetIds))
        {
            final List<com.hybris.cockpitng.core.persistence.impl.jaxb.Widget> children = new ArrayList<>(widget.getWidget());
            for(final com.hybris.cockpitng.core.persistence.impl.jaxb.Widget child : children)
            {
                if(removeWidgetIds.contains(child.getId()))
                {
                    widget.getWidget().remove(child);
                }
            }
        }
    }


    protected Set<String> applyExtensionAdd(final com.hybris.cockpitng.core.persistence.impl.jaxb.Widget widget,
                    final WidgetExtension extension)
    {
        final Set<String> ret = new HashSet<>();
        for(final com.hybris.cockpitng.core.persistence.impl.jaxb.Widget child : extension.getWidget())
        {
            widget.getWidget().add(child);
            ret.add(child.getId());
        }
        return ret;
    }


    protected void applyExtensionMove(final Widgets widgets,
                    final com.hybris.cockpitng.core.persistence.impl.jaxb.Widget widgetToMove, final WidgetExtension extension)
    {
        for(final WidgetMove move : extension.getMove())
        {
            try
            {
                validateMove(move, widgetToMove, widgets);
            }
            catch(final WidgetPersistenceException wpe)
            {
                if(move.isFailOnError())
                {
                    throw wpe;
                }
                else
                {
                    LOG.warn(wpe.getMessage());
                    continue;
                }
            }
            final String fromWidgetId = move.getWidgetId();
            final String toWidgetId = move.getTargetWidgetId();
            getWidgetById(widgets, fromWidgetId).getWidget().remove(widgetToMove);
            widgetToMove.setSlotId(move.getTargetSlotId());
            final com.hybris.cockpitng.core.persistence.impl.jaxb.Widget toWidget = getWidgetById(widgets, toWidgetId);
            final List<com.hybris.cockpitng.core.persistence.impl.jaxb.Widget> widgetsInTargetSlot = getWidgetsInSlot(toWidget,
                            move.getTargetSlotId());
            final Integer position = move.getPosition();
            if(position == null || widgetsInTargetSlot.size() <= position.intValue())
            {
                toWidget.getWidget().add(widgetToMove);
            }
            else
            {
                final com.hybris.cockpitng.core.persistence.impl.jaxb.Widget currentWidgetOnPosition = widgetsInTargetSlot
                                .get(position.intValue());
                final int computedIndex = toWidget.getWidget().indexOf(currentWidgetOnPosition);
                toWidget.getWidget().add(computedIndex, widgetToMove);
            }
        }
    }


    private void applyExtensionAccess(final com.hybris.cockpitng.core.persistence.impl.jaxb.Widget widget,
                    final WidgetExtension extension)
    {
        final String newWidgetAccess = widgetAccessResolver.resolveAccess(widget.getAccess(), extension.getAccess());
        widget.setAccess(newWidgetAccess);
    }


    /**
     * @see XMLWidgetPersistenceJAXBLookupHandler#findWidgetsInSlot(com.hybris.cockpitng.core.persistence.impl.jaxb.Widget,
     *      java.lang.String)
     * @deprecated since 6.7
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected List<com.hybris.cockpitng.core.persistence.impl.jaxb.Widget> getWidgetsInSlot(
                    final com.hybris.cockpitng.core.persistence.impl.jaxb.Widget widget, final String slotId)
    {
        return jaxbLookupHandler.findWidgetsInSlot(widget, slotId);
    }


    protected void validateMove(final WidgetMove move, final com.hybris.cockpitng.core.persistence.impl.jaxb.Widget widgetToMove,
                    final Widgets widgets)
    {
        if(widgetToMove.getId().equals(move.getTargetWidgetId()))
        {
            throw new WidgetPersistenceException("Widget may not become its own child.");
        }
        final com.hybris.cockpitng.core.persistence.impl.jaxb.Widget sourceWidget = getWidgetById(widgets, move.getWidgetId());
        final com.hybris.cockpitng.core.persistence.impl.jaxb.Widget targetWidget = getWidgetById(widgets,
                        move.getTargetWidgetId());
        if(sourceWidget == null)
        {
            throw new WidgetPersistenceException(String.format("Widget '%s' does not exist", move.getWidgetId()));
        }
        if(targetWidget == null)
        {
            throw new WidgetPersistenceException(String.format("Target widget '%s' does not exist", move.getTargetWidgetId()));
        }
        if(!sourceWidget.getWidget().contains(widgetToMove))
        {
            throw new WidgetPersistenceException(
                            String.format("Widget %s is not parent of %s", move.getWidgetId(), widgetToMove.getId()));
        }
        if(isChildWidget(widgetToMove, targetWidget))
        {
            throw new WidgetPersistenceException(
                            String.format("Cannot move parent %s to its child %s.", widgetToMove.getId(), targetWidget.getId()));
        }
    }


    /**
     * @see XMLWidgetPersistenceJAXBLookupHandler#isChildWidget(com.hybris.cockpitng.core.persistence.impl.jaxb.Widget,
     *      com.hybris.cockpitng.core.persistence.impl.jaxb.Widget)
     * @deprecated since 6.7
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected boolean isChildWidget(final com.hybris.cockpitng.core.persistence.impl.jaxb.Widget parent,
                    final com.hybris.cockpitng.core.persistence.impl.jaxb.Widget prospectiveChild)
    {
        return jaxbLookupHandler.isChildWidget(parent, prospectiveChild);
    }


    /**
     * @see XMLWidgetPersistenceJAXBLookupHandler#findWidgetById(com.hybris.cockpitng.core.persistence.impl.jaxb.Widgets,
     *      java.lang.String)
     * @deprecated since 6.7, this method will be removed
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected com.hybris.cockpitng.core.persistence.impl.jaxb.Widget getWidgetById(final Widgets widgets, final String widgetId)
    {
        return jaxbLookupHandler.findWidgetById(widgets, widgetId);
    }


    private Widget createWidgetTree(final Widgets widgets, final com.hybris.cockpitng.core.persistence.impl.jaxb.Widget widget)
    {
        final Map<com.hybris.cockpitng.core.persistence.impl.jaxb.Widget, Widget> map = new HashMap<>();
        final Map<WidgetDefinition, Widget> stubs = new HashMap<>();
        final Widget rootNode = jaxbConverter.createWidgetTreeFromJAXB(widget, null, map, this.widgetService);
        // create necessary widgets stubs
        createNecessaryWidgetStubs(rootNode, stubs);
        final Map<com.hybris.cockpitng.core.persistence.impl.jaxb.WidgetConnection, WidgetConnection> connectionMap = new HashMap<>();
        final List<com.hybris.cockpitng.core.persistence.impl.jaxb.WidgetConnection> connectors = widgets.getWidgetConnection();
        for(final com.hybris.cockpitng.core.persistence.impl.jaxb.WidgetConnection conn : connectors)
        {
            if(!connectionMap.containsKey(conn))
            {
                final com.hybris.cockpitng.core.persistence.impl.jaxb.Widget src = getWidgetById(widgets, conn.getSourceWidgetId());
                final com.hybris.cockpitng.core.persistence.impl.jaxb.Widget tgt = getWidgetById(widgets, conn.getTargetWidgetId());
                Widget sourceWidget = map.get(src);
                Widget targetWidget = map.get(tgt);
                final WidgetDefinition sourceStubDefinition = getWidgetDefinitionService().getStubWidgetDefinitions()
                                .get(conn.getSourceWidgetId());
                final WidgetDefinition targetStubDefinition = getWidgetDefinitionService().getStubWidgetDefinitions()
                                .get(conn.getTargetWidgetId());
                if(sourceStubDefinition != null)
                {
                    sourceWidget = stubs.get(sourceStubDefinition);
                }
                if(targetStubDefinition != null)
                {
                    targetWidget = stubs.get(targetStubDefinition);
                }
                if(sourceWidget != null && targetWidget != null)
                {
                    final WidgetConnection widgetConnection = jaxbConverter.createWidgetConnection(conn, sourceWidget, targetWidget);
                    connectionMap.put(conn, widgetConnection);
                    sourceWidget.addOutConnection(widgetConnection);
                    targetWidget.addInConnection(widgetConnection);
                }
            }
        }
        return rootNode;
    }


    private void createNecessaryWidgetStubs(final Widget rootWidget, final Map<WidgetDefinition, Widget> stubs)
    {
        final Map<String, WidgetDefinition> stubWidgetDefinitions = getWidgetDefinitionService().getStubWidgetDefinitions();
        if(MapUtils.isNotEmpty(stubWidgetDefinitions))
        {
            final Set<String> rootChildWidgetIDs = new HashSet<>();
            final List<Widget> children = rootWidget.getChildren();
            for(final Widget widget : children)
            {
                rootChildWidgetIDs.add(widget.getId());
                final WidgetDefinition childDefinition = getWidgetDefinitionService()
                                .getComponentDefinitionForCode(widget.getWidgetDefinitionId(), WidgetDefinition.class);
                if(childDefinition != null && stubWidgetDefinitions.containsKey(childDefinition.getCode()))
                {
                    stubs.put(childDefinition, widget);
                }
            }
            for(final Map.Entry<String, WidgetDefinition> entry : stubWidgetDefinitions.entrySet())
            {
                if(!rootChildWidgetIDs.contains(entry.getKey()))
                {
                    final Widget widget = widgetService.createWidget(rootWidget, entry.getKey(), "cockpitWidgetChildrenInvisible",
                                    entry.getValue().getCode());
                    widget.getWidgetSettings().put("_isStubWidget", Boolean.TRUE);
                    stubs.put(entry.getValue(), widget);
                }
            }
        }
    }


    @Override
    public void storeWidgetTree(final Widget widget)
    {
        if(storeAsExtension)
        {
            LOG.warn("Extension based widget setup storing not implemented.");
        }
        try
        {
            storeWidgetTree(widget, getWidgetsFile());
        }
        catch(final FileNotFoundException e)
        {
            LOG.error("could not load widget tree for widget '" + widget + "'", e);
        }
    }


    /**
     * Stores widget's tree in file
     *
     * @param widget
     *           tree to store
     * @param widgetsFile
     *           file where to store
     * @throws FileNotFoundException
     *            thrown if specified widgets file cannot be found
     */
    public void storeWidgetTree(final Widget widget, final File widgetsFile) throws FileNotFoundException
    {
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try
        {
            fileInputStream = new FileInputStream(widgetsFile);
            final Widgets widgets = loadWidgets(fileInputStream);
            fileOutputStream = new FileOutputStream(widgetsFile);
            storeWidgetTree(widget, widgets, fileOutputStream);
        }
        finally
        {
            IOUtils.closeQuietly(fileInputStream);
            IOUtils.closeQuietly(fileOutputStream);
        }
    }


    /**
     * Stores widget's tree in file
     *
     * @param widget
     *           tree to store
     * @param widgets
     *           schema
     * @param outputStream
     *           stream where to store
     */
    public void storeWidgetTree(final Widget widget, final Widgets widgets, final OutputStream outputStream)
    {
        jaxbConverter.updateWidgetsFromModel(widgets, widget, getWidgetDefinitionService());
        jaxbConverter.storeWidgetConnectionsFromModel(widgets, widget);
        jaxbConverter.removeWidgetStubsFromJAXB(widgets, getWidgetDefinitionService());
        deleteOrphanedConnections(widgets);
        jaxbConverter.removeImportsFromJAXB(widgets);
        storeWidgets(widgets, outputStream);
    }


    /**
     * Gets widget tree as String
     *
     * @param widget
     *           tree to store
     * @param widgets
     *           schema
     * @return tree
     */
    public String getWidgetTreeAsString(final Widget widget, final Widgets widgets)
    {
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        storeWidgetTree(widget, widgets == null ? new Widgets() : widgets, stream);
        return new String(stream.toByteArray(), Charset.defaultCharset());
    }


    @Override
    public void deleteWidgetTree(final Widget node)
    {
        try
        {
            deleteWidgetTree(node, getWidgetsFile());
        }
        catch(final FileNotFoundException e)
        {
            LOG.warn(e.getMessage());
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Error while deleting widget tree", e);
            }
        }
    }


    /**
     * Deletes widget's tree.
     *
     * @param node
     *           root node of the widget's tree
     * @param file
     *           where tree is stored
     * @throws FileNotFoundException
     *            thrown if specified widgets file cannot be found
     */
    public void deleteWidgetTree(final Widget node, final File file) throws FileNotFoundException
    {
        if(file == null)
        {
            throw new FileNotFoundException("Unable to find file: null");
        }
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        try
        {
            inputStream = new FileInputStream(file);
            final Widgets widgets = loadWidgets(inputStream);
            deleteWidgetTreeRecursive(widgets, node);
            deleteOrphanedConnections(widgets);
            outputStream = new FileOutputStream(file);
            storeWidgets(widgets, outputStream);
        }
        finally
        {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        }
    }


    protected void deleteWidgetTreeRecursive(final Widgets widgets, final Widget node)
    {
        final com.hybris.cockpitng.core.persistence.impl.jaxb.Widget widget = getWidgetById(widgets, node.getId());
        if(widget != null)
        {
            final List<Widget> children = node.getChildren();
            deleteConnections(widgets, widget);
            for(final Widget child : children)
            {
                deleteWidgetTreeRecursive(widgets, child);
            }
            final com.hybris.cockpitng.core.persistence.impl.jaxb.Widget parent = jaxbLookupHandler.findWidgetParent(widgets,
                            widget);
            if(parent == null)
            {
                widgets.getWidget().remove(widget);
            }
            else
            {
                parent.getWidget().remove(widget);
            }
        }
    }


    protected void deleteConnections(final Widgets widgets, final com.hybris.cockpitng.core.persistence.impl.jaxb.Widget widget)
    {
        final Set<com.hybris.cockpitng.core.persistence.impl.jaxb.WidgetConnection> toRemove = new HashSet<>();
        for(final com.hybris.cockpitng.core.persistence.impl.jaxb.WidgetConnection conn : widgets.getWidgetConnection())
        {
            if(conn.getSourceWidgetId().equals(widget.getId()) || conn.getTargetWidgetId().equals(widget.getId()))
            {
                toRemove.add(conn);
            }
        }
        for(final com.hybris.cockpitng.core.persistence.impl.jaxb.WidgetConnection conn : toRemove)
        {
            widgets.getWidgetConnection().remove(conn);
        }
    }


    /**
     * @see XMLWidgetPersistenceJAXBConverter#isStubWidget(java.lang.String,
     *      com.hybris.cockpitng.core.CockpitComponentDefinitionService)
     * @deprecated since 6.7
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected boolean isStubWidget(final String widgetId)
    {
        return jaxbConverter.isStubWidget(widgetId, getWidgetDefinitionService());
    }


    /**
     * @see XMLWidgetPersistenceJAXBConverter#removeOrphanedConnectionsFromJAXB(com.hybris.cockpitng.core.persistence.impl.jaxb.Widgets,
     *      com.hybris.cockpitng.core.CockpitComponentDefinitionService)
     * @deprecated since 6.7
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected void deleteOrphanedConnections(final Widgets widgets)
    {
        jaxbConverter.removeOrphanedConnectionsFromJAXB(widgets, getWidgetDefinitionService());
    }


    protected File getWidgetsFile()
    {
        final String fileName = getDefaultWidgetConfig() == null ? DEFAULT_FILE_NAME_WIDGETS_XML : getDefaultWidgetConfig();
        final File file = new File(widgetLibUtils.getRootDir(), fileName);
        if(!file.exists())
        {
            try
            {
                final boolean fileCreated = file.createNewFile();
                if(fileCreated)
                {
                    fillDefault(file);
                }
            }
            catch(final IOException e)
            {
                LOG.warn(e.getMessage());
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Error while getting widget file", e);
                }
            }
        }
        return file;
    }


    protected InputStream getWidgetsFileAsStream() throws FileNotFoundException
    {
        return new FileInputStream(getWidgetsFile());
    }


    protected boolean hasDefaultConfig()
    {
        InputStream resourceAsStream = null;
        try
        {
            resourceAsStream = ClassLoaderUtils.getCurrentClassLoader(this.getClass()).getResourceAsStream(getDefaultWidgetConfig());
            return resourceAsStream != null;
        }
        finally
        {
            IOUtils.closeQuietly(resourceAsStream);
        }
    }


    private void fillDefault(final File file) throws IOException
    {
        if(getDefaultWidgetConfig() != null)
        {
            final InputStream resourceAsStream = ClassLoaderUtils.getCurrentClassLoader(this.getClass())
                            .getResourceAsStream(getDefaultWidgetConfig());
            if(resourceAsStream == null)
            {
                return;
            }
            FileUtils.copyInputStreamToFile(resourceAsStream, file);
        }
    }


    protected Widgets loadWidgets(final InputStream inputStream)
    {
        return loadWidgetsInternal(inputStream);
    }


    protected final Widgets loadWidgetsInternal(final InputStream inputStream)
    {
        try
        {
            return (Widgets)this.unmarshaller.unmarshal(new StreamSource(inputStream));
        }
        catch(final XmlMappingException e)
        {
            LOG.info("Could not load widget tree from stream. This can be caused by an invalid or empty widgets config file.");
            LOG.debug("error loading widget tree", e);
        }
        catch(final IOException e)
        {
            LOG.error("error loading widget tree", e);
        }
        finally
        {
            IOUtils.closeQuietly(inputStream);
        }
        return new Widgets();
    }


    protected void storeWidgets(final Widgets widgets, final OutputStream outputStream)
    {
        try
        {
            this.marshaller.marshal(widgets, new StreamResult(outputStream));
        }
        catch(final XmlMappingException | IOException e)
        {
            LOG.error("error storing widget tree", e);
        }
        finally
        {
            IOUtils.closeQuietly(outputStream);
        }
    }


    @Required
    public void setMarshaller(final Marshaller marshaller)
    {
        this.marshaller = marshaller;
    }


    @Required
    public void setUnmarshaller(final Unmarshaller unmarshaller)
    {
        this.unmarshaller = unmarshaller;
    }


    @Required
    public void setWidgetService(final WidgetService widgetService)
    {
        this.widgetService = widgetService;
    }


    protected String getDefaultWidgetConfig()
    {
        if(this.defaultWidgetConfig == null)
        {
            if(getCockpitProperties() == null)
            {
                this.defaultWidgetConfig = defaultWidgetConfigSpring;
            }
            else
            {
                final String property = getCockpitProperties().getProperty("cockpitng.widgetConfig.filename");
                if(StringUtils.isNotBlank(property))
                {
                    this.defaultWidgetConfig = property;
                }
                else
                {
                    this.defaultWidgetConfig = this.defaultWidgetConfigSpring;
                }
            }
            if(this.defaultWidgetConfig != null && this.defaultWidgetConfig.charAt(0) == '/')
            {
                this.defaultWidgetConfig = this.defaultWidgetConfig.substring(1);
            }
        }
        return this.defaultWidgetConfig;
    }


    /**
     * @deprecated since 2205, use the {@link XMLWidgetPersistenceService#setDefaultWidgetConfigSpring(String)} instead.
     */
    @Deprecated(since = "2205", forRemoval = true)
    public void setDefaultWidgetConfig(final String defaultWidgetConfig)
    {
        this.defaultWidgetConfigSpring = defaultWidgetConfig;
    }


    public void setDefaultWidgetConfigSpring(final String defaultWidgetConfigSpring)
    {
        this.defaultWidgetConfigSpring = defaultWidgetConfigSpring;
    }


    protected WidgetLibUtils getWidgetLibUtils()
    {
        return this.widgetLibUtils;
    }


    @Required
    public void setWidgetLibUtils(final WidgetLibUtils widgetLibUtils)
    {
        this.widgetLibUtils = widgetLibUtils;
    }


    public void setStoreAsExtension(final boolean storeAsExtension)
    {
        this.storeAsExtension = storeAsExtension;
    }


    @Override
    public void resetToDefaults()
    {
        // delete widget file
        final File widgetsFile = getWidgetsFile();
        if(widgetsFile != null)
        {
            final String absolutePath = widgetsFile.getAbsolutePath();
            if(!widgetsFile.delete())
            {
                LOG.warn("Failed to delete file : {}.", absolutePath);
            }
        }
    }


    /**
     * @see XMLWidgetPersistenceJAXBConverter#extractAccessRestrictions(java.lang.String)
     * @deprecated since 6.7
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected List<String> getAccessRestrictions(final String access)
    {
        return jaxbConverter.extractAccessRestrictions(access);
    }


    protected CockpitProperties getCockpitProperties()
    {
        return cockpitProperties;
    }


    @Autowired(required = false)
    public void setCockpitProperties(final CockpitProperties cockpitProperties)
    {
        this.cockpitProperties = cockpitProperties;
    }


    protected CockpitComponentDefinitionService getWidgetDefinitionService()
    {
        return widgetDefinitionService;
    }


    @Required
    public void setWidgetDefinitionService(final CockpitComponentDefinitionService widgetDefinitionService)
    {
        this.widgetDefinitionService = widgetDefinitionService;
    }


    /**
     * @see #getImportSupport()
     * @deprecated since 2005
     */
    @Deprecated(since = "2005", forRemoval = true)
    public TemplateEngine getTemplateEngine()
    {
        return null;
    }


    /**
     * @see #getImportSupport()
     * @deprecated since 2005
     */
    @Deprecated(since = "2005", forRemoval = true)
    public void setTemplateEngine(final TemplateEngine templateEngine)
    {
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


    protected void requestReadLock()
    {
        rwLock.readLock().lock();
    }


    protected void releaseReadLock()
    {
        rwLock.readLock().unlock();
    }


    protected void requestWriteLock()
    {
        rwLock.writeLock().lock();
    }


    protected void releaseWriteLock()
    {
        rwLock.writeLock().unlock();
    }


    public DefaultWidgetAccessResolver getWidgetAccessResolver()
    {
        return widgetAccessResolver;
    }


    public void setWidgetAccessResolver(final DefaultWidgetAccessResolver widgetAccessResolver)
    {
        this.widgetAccessResolver = widgetAccessResolver;
    }
}
