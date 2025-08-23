/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.web.js.impl;

import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.core.persistence.packaging.CockpitResourceLoader;
import com.hybris.cockpitng.core.persistence.packaging.WidgetJarLibInfo;
import com.hybris.cockpitng.core.persistence.packaging.WidgetLibConstants;
import com.hybris.cockpitng.core.persistence.packaging.WidgetLibUtils;
import com.hybris.cockpitng.core.util.CockpitProperties;
import com.hybris.cockpitng.engine.impl.DefaultCockpitWidgetEngine;
import com.hybris.cockpitng.util.impl.jaxb.DependencyProtocol;
import com.hybris.cockpitng.util.impl.jaxb.Libraries;
import com.hybris.cockpitng.util.impl.jaxb.Library;
import com.hybris.cockpitng.web.js.ChainedDependencies;
import com.hybris.cockpitng.web.js.DependenciesManager;
import com.hybris.cockpitng.web.js.DependenciesResolver;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.sys.PageCtrl;
import org.zkoss.zul.Html;

/**
 *
 *
 */
public class DefaultDependenciesManager implements DependenciesManager
{
    private static final String CACHING_ENABLED = "cockpitng.dependencies.cache.enabled";
    private static final Logger LOG = LoggerFactory.getLogger(DefaultDependenciesManager.class);
    private static final String JS_DEPENDENCY_PATH = ResourcePath.JS_DEPENDENCY_ROOT + "/js-libraries.xml";
    private WidgetLibUtils widgetLibUtils;
    private CockpitResourceLoader cockpitResourceLoader;
    private CockpitProperties cockpitProperties;
    private Map<DependencyDefinition, ResolvedDependency> resolvedDependencies;
    private List<ChainedDependencies> chained;
    private List<DependenciesResolver> resolvers;
    private Boolean dependenciesCacheEnabled;


    @Override
    public Map<DependencyDefinition, ResolvedDependency> getScriptDependencies()
    {
        final Map<DependencyDefinition, ResolvedDependency> resolved = new LinkedHashMap<>();
        if(resolvedDependencies == null)
        {
            try
            {
                final JAXBContext jc = JAXBContext.newInstance(Libraries.class);
                final Unmarshaller unmarshaller = jc.createUnmarshaller();
                if(widgetLibUtils.loadLibProps() != null)
                {
                    final Collection<Object> modules = widgetLibUtils.loadLibProps().keySet();
                    modules.stream().filter(Objects::nonNull).forEach(module -> {
                        final File moduleJar = new File(ObjectUtils.toString(module));
                        final Collection<WidgetJarLibInfo> widgetJarLibInfos = widgetLibUtils.getModuleJarLibInfos(moduleJar);
                        final List<ResolvedDependency> dependencies = resolveScriptDependencies(unmarshaller, moduleJar,
                                        widgetJarLibInfos);
                        dependencies.forEach(dependency -> {
                            final ResolvedDependency previous = resolved.put(dependency.getDefinition(), dependency);
                            if(previous != null)
                            {
                                dependency.addDependingWidgets(previous.getDependingWidgets());
                            }
                        });
                    });
                    if(isCachingEnabled())
                    {
                        resolvedDependencies = resolved;
                    }
                }
            }
            catch(final JAXBException e)
            {
                LOG.error("An error occurred while trying to fetch script dependencies", e);
            }
        }
        else
        {
            resolved.putAll(resolvedDependencies);
        }
        return resolved;
    }


    @Override
    public Map<DependencyDefinition, ResolvedDependency> getScriptDependencies(final WidgetJarLibInfo widget)
    {
        final Map<DependencyDefinition, ResolvedDependency> resolved = new LinkedHashMap<>();
        if(resolvedDependencies == null)
        {
            try
            {
                final JAXBContext jc = JAXBContext.newInstance(Libraries.class);
                final Unmarshaller unmarshaller = jc.createUnmarshaller();
                final List<ResolvedDependency> dependencies = resolveScriptDependencies(unmarshaller, widget.getJarPath(),
                                Collections.singleton(widget));
                dependencies.forEach(dependency -> {
                    final ResolvedDependency previous = resolved.put(dependency.getDefinition(), dependency);
                    if(previous != null)
                    {
                        dependency.addDependingWidgets(previous.getDependingWidgets());
                    }
                });
            }
            catch(final JAXBException e)
            {
                LOG.error("An error occurred while trying to fetch script dependencies", e);
            }
        }
        else
        {
            resolvedDependencies.entrySet().stream().filter(entry -> entry.getValue().getDependingWidgets().contains(widget.getId()))
                            .forEach(entry -> resolved.put(entry.getKey(), entry.getValue()));
        }
        return resolved;
    }


    @Override
    public void manageScriptDependencies(final Map<DependencyDefinition, ResolvedDependency> dependencies, final Component comp)
    {
        final Page page = comp.getPage();
        if(page instanceof PageCtrl)
        {
            final PageCtrl pageCtrl = (PageCtrl)page;
            dependencies.values().stream().forEach(dependency -> {
                if(dependency != null && dependency.getPath() != null)
                {
                    final String script = String.format("<script type=\"%s\" src=\"%s\"></script>", dependency.getType(),
                                    dependency.getPath().toString());
                    switch(dependency.getInjectionPoint())
                    {
                        case ResolvedDependency.POINT_HEADER:
                            pageCtrl.addAfterHeadTags(script);
                            break;
                        case ResolvedDependency.POINT_BEFORE_BODY:
                            comp.insertBefore(new Html(script), comp.getFirstChild());
                            break;
                        case ResolvedDependency.POINT_AFTER_BODY:
                            comp.appendChild(new Html(script));
                            break;
                        case ResolvedDependency.POINT_BEFORE_WIDGET:
                        case ResolvedDependency.POINT_AFTER_WIDGET:
                            insertDependenciesToWidgets(comp, dependency.getDependingWidgets(), script,
                                            dependency.getInjectionPoint() == ResolvedDependency.POINT_BEFORE_WIDGET);
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }


    private void insertDependenciesToWidgets(final Component comp, final Set<String> widgets, final String script,
                    final boolean before)
    {
        if(comp instanceof Widgetslot)
        {
            final Widgetslot slot = (Widgetslot)comp;
            if(slot.getWidgetInstance() != null && widgets.contains(slot.getWidgetInstance().getWidget().getWidgetDefinitionId()))
            {
                Component body = slot;
                for(final Component child : slot.getChildren())
                {
                    if((child instanceof HtmlBasedComponent)
                                    && ((HtmlBasedComponent)child).getSclass().contains(DefaultCockpitWidgetEngine.SCLASS_WIDGET_BODY))
                    {
                        body = child;
                        break;
                    }
                }
                if(before)
                {
                    body.insertBefore(new Html(script), body.getFirstChild());
                }
                else
                {
                    body.appendChild(new Html(script));
                }
            }
        }
        comp.getChildren().stream().forEach(child -> insertDependenciesToWidgets(child, widgets, script, before));
    }


    protected List<ResolvedDependency> resolveScriptDependencies(final Unmarshaller unmarshaller, final File moduleJar,
                    final Collection<WidgetJarLibInfo> widgets)
    {
        final List<ResolvedDependency> result = new ArrayList<>();
        final String resourcePath = String.format("%s%s", WidgetLibConstants.RESOURCES_SUBFOLDER, JS_DEPENDENCY_PATH);
        if(cockpitResourceLoader.hasResource(moduleJar, resourcePath))
        {
            try(InputStream dependenciesFile = cockpitResourceLoader.getResourceAsStream(moduleJar, resourcePath))
            {
                if(dependenciesFile != null)
                {
                    final Libraries dependenciesDef = (Libraries)unmarshaller.unmarshal(new StreamSource(dependenciesFile));
                    dependenciesDef.getLibrary().stream().forEach(library -> {
                        String name = library.getName();
                        if(name == null && DependencyProtocol.HTTP.equals(library.getProtocol()))
                        {
                            name = FilenameUtils.getBaseName(library.getUrl());
                        }
                        else if(name == null)
                        {
                            name = moduleJar.getName() + "!" + FilenameUtils.getBaseName(library.getUrl());
                        }
                        final DependencyDefinition definition = new DependencyDefinition(name, library.getVersion());
                        final ResourcePath path;
                        if(library.getProtocol() != null)
                        {
                            final byte pathType;
                            switch(library.getProtocol())
                            {
                                case CLASSPATH:
                                    pathType = ResourcePath.CLASSPATH_RESOURCE;
                                    break;
                                case HTTP:
                                    pathType = ResourcePath.REMOTE_RESOURCE;
                                    break;
                                case RESOURCE:
                                    pathType = ResourcePath.JAR_RESOURCE;
                                    break;
                                default:
                                    pathType = ResourcePath.REMOTE_RESOURCE;
                                    break;
                            }
                            path = new ResourcePath(pathType, library.getUrl(), moduleJar.getName());
                        }
                        else
                        {
                            path = new ResourcePath(library.getUrl(), moduleJar.getName());
                        }
                        ResolvedDependency resolvedDependency = new ResolvedDependency(library.getType(), definition, path);
                        setInjectionPoint(library, resolvedDependency);
                        resolvedDependency
                                        .addDependingWidgets(widgets.stream().map(WidgetJarLibInfo::getId).collect(Collectors.toSet()));
                        if(getResolvers() != null)
                        {
                            for(final DependenciesResolver resolver : getResolvers())
                            {
                                resolvedDependency = resolver.resolveDependency(resolvedDependency);
                            }
                        }
                        result.add(resolvedDependency);
                        result.addAll(getChainedDependencies(moduleJar, resolvedDependency,
                                        widgets.stream().map(WidgetJarLibInfo::getId).collect(Collectors.toSet())));
                    });
                }
            }
            catch(final JAXBException | IOException e)
            {
                LOG.error(e.getMessage(), e);
            }
        }
        return result;
    }


    protected void setInjectionPoint(final Library library, final ResolvedDependency resolvedDependency)
    {
        if(library.getPoint() != null)
        {
            switch(library.getPoint())
            {
                case HEADER:
                    resolvedDependency.setInjectionPoint(ResolvedDependency.POINT_HEADER);
                    break;
                case BEFORE_BODY:
                    resolvedDependency.setInjectionPoint(ResolvedDependency.POINT_BEFORE_BODY);
                    break;
                case AFTER_BODY:
                    resolvedDependency.setInjectionPoint(ResolvedDependency.POINT_AFTER_BODY);
                    break;
                case BEFORE_WIDGET:
                    resolvedDependency.setInjectionPoint(ResolvedDependency.POINT_BEFORE_WIDGET);
                    break;
                case AFTER_WIDGET:
                    resolvedDependency.setInjectionPoint(ResolvedDependency.POINT_AFTER_WIDGET);
                    break;
            }
        }
    }


    protected Set<ResolvedDependency> getChainedDependencies(final File moduleJar, final ResolvedDependency dependency,
                    final Collection<String> widgets)
    {
        if(getChained() != null)
        {
            return getChained().stream()
                            .map(dep -> dep.getChainedDependencies(moduleJar, dependency, widgets.stream().collect(Collectors.toSet())))
                            .flatMap(dependencies -> dependencies.stream()).collect(Collectors.toSet());
        }
        else
        {
            return Collections.emptySet();
        }
    }


    protected boolean isCachingEnabled()
    {
        if(dependenciesCacheEnabled == null)
        {
            dependenciesCacheEnabled = getCockpitProperties().getBoolean(CACHING_ENABLED);
        }
        return dependenciesCacheEnabled.booleanValue();
    }


    protected WidgetLibUtils getWidgetLibUtils()
    {
        return widgetLibUtils;
    }


    @Required
    public void setWidgetLibUtils(final WidgetLibUtils widgetLibUtils)
    {
        this.widgetLibUtils = widgetLibUtils;
    }


    protected CockpitResourceLoader getCockpitResourceLoader()
    {
        return cockpitResourceLoader;
    }


    @Required
    public void setCockpitResourceLoader(final CockpitResourceLoader cockpitResourceLoader)
    {
        this.cockpitResourceLoader = cockpitResourceLoader;
    }


    protected CockpitProperties getCockpitProperties()
    {
        return cockpitProperties;
    }


    @Required
    public void setCockpitProperties(final CockpitProperties cockpitProperties)
    {
        this.cockpitProperties = cockpitProperties;
    }


    public List<ChainedDependencies> getChained()
    {
        return chained;
    }


    public void setChained(final List<ChainedDependencies> chained)
    {
        this.chained = chained;
    }


    public List<DependenciesResolver> getResolvers()
    {
        return resolvers;
    }


    public void setResolvers(final List<DependenciesResolver> resolvers)
    {
        this.resolvers = resolvers;
    }
}
